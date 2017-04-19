package com.nedap.university.protocols.protocol1;

import com.nedap.university.application.FileSplitter;
import com.nedap.university.packetTypes.DataPacket;
import com.nedap.university.udpFileServer.UDPFileServer;
import com.nedap.university.udpFileServer.incomingPacketHandlers.DataPacketAckHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by martijn.slot on 18/04/2017.
 */
public class Protocol1 {

    private static final int WINDOWSIZE = 1000;
    private static final long SLEEPING_TIME = 20;
    private final DataPacketAckHandler dataPacketAckHandler;
    private String fileToSend;
    private UDPFileServer server;
    private FileSplitter fileSplitter = new FileSplitter();
    private int seqNumber = 1;
    private Map<Integer, byte[]> sendData = new ConcurrentHashMap<>();

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

    public Map<Integer, byte[]> getData() {
        return sendData;
    }

    public byte[] createDataPacket(FileSplitter fileSplitter, byte[] data, int seqNumber) {
        data = fileSplitter.createPacket(seqNumber, data);
        return new DataPacket().createPacket(data, fileToSend, seqNumber);
    }

    public byte[] createDataFinPacket(FileSplitter fileSplitter, byte[] data, int seqNumber) {
        data = fileSplitter.createPacket(seqNumber, data);
        return new DataPacket().createFinPacket(data, fileToSend, seqNumber);
    }

    private void preparePackagesToSend() {
        byte[] datafile = fileSplitter.getFileContents(server.getFilePath() + "/" + fileToSend);
        int numberOfFragments = datafile.length / fileSplitter.getPacketSize() + 1;
        Map<Integer, byte[]> sendTemp = new HashMap<>();
        System.out.println("Number of fragments to send int total : " + numberOfFragments);
        Set<Integer> receivedAcks = new HashSet<>();

        while (receivedAcks.size() != numberOfFragments) {
            int fragmentCounter;
            int lowerbound;
            int upperbound;

            Set<Integer> tempSet = checkForAcks(dataPacketAckHandler);

            for (int i : tempSet) {
                if (!receivedAcks.contains(i)) {
                    receivedAcks.add(i);
                }
            }
            tempSet.clear();

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
                    if (fragmentCounter == numberOfFragments) {
                        sendTemp.put(numberOfFragments, createDataFinPacket(fileSplitter, datafile, numberOfFragments));
                    } else {
                        sendTemp.put(seqNumber, createDataPacket(fileSplitter, datafile, seqNumber));
                    }
                }
                sendData = new HashMap<>(sendTemp);
                if (sendTemp.size() != 0) sendTemp.clear();

//                try {
//                    Thread.sleep(SLEEPING_TIME);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                //remove from received acks if some data has not been acked properly
                if (sendData.size() <= 1) {
                    for (int key : sendData.keySet()) {
                        receivedAcks.remove(key);
                    }
                }
            }
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
