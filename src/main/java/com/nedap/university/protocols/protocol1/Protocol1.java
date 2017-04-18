package com.nedap.university.protocols.protocol1;

import com.nedap.university.application.FileSplitter;
import com.nedap.university.packetTypes.DataPacket;
import com.nedap.university.packetTypes.DataPacketACK;
import com.nedap.university.udpFileServer.UDPFileServer;
import com.nedap.university.udpFileServer.incomingPacketHandlers.DataPacketAckHandler;

import java.util.*;

/**
 * Created by martijn.slot on 18/04/2017.
 */
public class Protocol1 {

    private static final int WINDOWSIZE = 500;
    private final DataPacketAckHandler dataPacketAckHandler;
    private String fileToSend;
    private UDPFileServer server;
    private FileSplitter fileSplitter = new FileSplitter();
    private int seqNumber = 1;
    private Map<Integer, byte[]> sendData = new HashMap<>();
    private Set<Integer> receivedAcks = new HashSet<>();

    public Protocol1(UDPFileServer server, String fileToSend, DataPacketAckHandler dataPacketAckHandler) {
        this.server = server;
        this.fileToSend = fileToSend;
        this.dataPacketAckHandler = dataPacketAckHandler;
    }

    public Protocol1(UDPFileServer server, DataPacketAckHandler dataPacketAckHandler) {
        this.server = server;
        this.dataPacketAckHandler = dataPacketAckHandler;
    }

    public void initiateProtocol() {
        preparePackagesToSend();
    }

    public byte[] getData() {
        return sendData.get(seqNumber);
    }

    public byte[] getDataAck(int seqNum) {
        return createAckPacket(seqNum);
    }

    private byte[] createDataPacket(FileSplitter fileSplitter, Integer[] data, int seqNumber) {
        data = fileSplitter.createPacket(seqNumber, data);
        return new DataPacket().createPacket(data, fileToSend, seqNumber);

    }

    private byte[] createAckPacket(int seqNumber) {
        return new DataPacketACK().createPacket(seqNumber);

    }

    private void preparePackagesToSend() {
        Integer[] datafile = fileSplitter.getFileContents(server.getFilePath() + "/" + fileToSend);
        int numberOfFragments = datafile.length / fileSplitter.getPacketSize() + 1;
        System.out.println("Number of fragments to send int total : " + numberOfFragments);

        while (receivedAcks.size() != numberOfFragments) {
            int fragmentCounter;
            int lowerbound;
            int upperbound;

            //define window boundaries for sliding window!
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

    public Set<Integer> getReceivedAcks() {
        return receivedAcks;
    }
}
