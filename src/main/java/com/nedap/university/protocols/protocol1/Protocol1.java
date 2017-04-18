package com.nedap.university.protocols.protocol1;

import com.nedap.university.application.FileSplitter;
import com.nedap.university.packetTypes.DataPacket;
import com.nedap.university.udpFileServer.UDPFileServer;
import com.nedap.university.udpFileServer.incomingPacketHandlers.DataPacketAckHandler;

import java.net.DatagramSocket;
import java.util.*;

/**
 * Created by martijn.slot on 18/04/2017.
 */
public class Protocol1 {

    private static final int WINDOWSIZE = 500;
    private final DataPacketAckHandler dataPacketAckHandler;
    private String fileToSend;
    private UDPFileServer server;
    private DatagramSocket zocket;
    private FileSplitter fileSplitter = new FileSplitter();
    private Integer[] datafile;
    private int numberOfFragments;
    private int seqNumber = 1;
    private Map<Integer, byte[]> sendData = new HashMap<>();

    public Protocol1(DatagramSocket socket, UDPFileServer server, String fileToSend, DataPacketAckHandler dataPacketAckHandler) {
        this.zocket = socket;
        this.server = server;
        this.fileToSend = fileToSend;
        this.dataPacketAckHandler = dataPacketAckHandler;
        datafile = fileSplitter.getFileContents(server.getFilePath() + "/" + fileToSend);
        numberOfFragments = datafile.length / fileSplitter.getPacketSize() + 1;

    }
    public void initiateProtocol() {
        System.out.println("Number of packets to send in total = " + numberOfFragments);
        preparePackagesToSend();
    }

    public byte[] getData() {
        return sendData.get(seqNumber);
    }

    private byte[] createDataPacket(FileSplitter fileSplitter, Integer[] data, int seqNumber) {
        data = fileSplitter.createPacket(seqNumber, data);
        return new DataPacket().createPacket(data, fileToSend, seqNumber);

    }

    public void preparePackagesToSend() {
        Set<Integer> receivedAcks = new HashSet<>();
        while (receivedAcks.size() != numberOfFragments) {
            int fragmentCounter;
            int lowerbound;
            int upperbound;

            //define window boundaries
            if (receivedAcks.isEmpty()) {
                lowerbound = 1;
                upperbound = Math.min(WINDOWSIZE, numberOfFragments);
            } else {
                lowerbound = determineLowerbound(receivedAcks);
                upperbound = determineUpperbound(receivedAcks, numberOfFragments);
            }

            // create and send a new packet of appropriate size
            for (fragmentCounter = lowerbound; fragmentCounter <= upperbound; fragmentCounter++) {
                if (!receivedAcks.contains(fragmentCounter)) {
                    seqNumber = fragmentCounter;
                    sendData.put(seqNumber, createDataPacket(fileSplitter, datafile, seqNumber));
                }
            }

            receivedAcks = checkForAcks(dataPacketAckHandler);

        }
    }

    // determine lowerbound window size
    private int determineLowerbound(Set<Integer> receivedAcks) {
        if (!receivedAcks.isEmpty()) {
            for (int a = 1; a <= Collections.max(receivedAcks); a++) {
                if (!receivedAcks.contains(a)) {
                    return a;
                }
            }
            return Collections.max(receivedAcks);
        }
        return 1;
    }

    // determine upperbound window size
    private int determineUpperbound(Set<Integer> receivedAcks, int numberOfFragments) {
        int upperbound = determineLowerbound(receivedAcks) + WINDOWSIZE;
        if (upperbound > numberOfFragments) {
            upperbound = numberOfFragments;
        }
        return upperbound;

    }

    private Set<Integer> checkForAcks(DataPacketAckHandler dataPacketAckHandler) {
        return dataPacketAckHandler.getAcks();
    }



}
