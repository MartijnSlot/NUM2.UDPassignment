package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.application.FileCompiler;
import com.nedap.university.header.StandardHeader;
import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by martijn.slot on 15/04/2017.
 */
public class DataPacketHandler implements PacketHandler {

    private static final int BLOCK_LENGTH = StandardHeader.getBlockLength();

    private byte[] datalength = new byte[BLOCK_LENGTH];
    private byte[] seqNum = new byte[BLOCK_LENGTH];
    private byte[] checkSum = new byte[BLOCK_LENGTH];
    private byte[] fileNameBytes = new byte[20];
    private Set<Integer> receivedDataPackets = new HashSet<>();

    private FileCompiler fileCompiler = new FileCompiler();
    private boolean finIsReceived;
    private int finSequenceNumber;


    public DataPacketHandler(){
    }

    @Override
    public void initiateHandler(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender apekop, byte[] data) {

        byte[] datafile = new byte[data.length - 3* BLOCK_LENGTH];
        byte[] onlyData = new byte[data.length - (3* BLOCK_LENGTH + fileNameBytes.length)];

        System.arraycopy(data, 0, datalength, 0, BLOCK_LENGTH);
        System.arraycopy(data, 4, seqNum, 0, BLOCK_LENGTH);
        System.arraycopy(data, 8, checkSum, 0, BLOCK_LENGTH);
        System.arraycopy(data, 12, fileNameBytes, 0, 20);
        System.arraycopy(data, 12, datafile, 0, data.length - 3* BLOCK_LENGTH);
        System.arraycopy(data, 32, onlyData, 0, data.length - (3* BLOCK_LENGTH + fileNameBytes.length));

        int sequenceNum = ByteBuffer.wrap(seqNum).getInt();
        String fileName = new String(fileNameBytes);
        fileName = fileName.replaceAll("[^a-zA-Z.]", "");
        fileName = fileName.toLowerCase();

        if(checkChecksum(datafile)) {
            System.out.println("Received data packet " + sequenceNum + ", from file " + fileName);
            if(!receivedDataPackets.contains(sequenceNum)) {
                receivedDataPackets.add(sequenceNum);
                udpFileServer.sendAck(sequenceNum);
                fileCompiler.setPacketInPacketMap(sequenceNum, onlyData);
                if (finIsReceived && allPacketsPresent()) {
                    System.out.println("Finalizing packets...");
                    fileCompiler.setFileName(fileName);
                    udpFileServer.finalizeSendMethod(fileCompiler);
                }

            }
        } else {
            System.out.println("wat een gekkenhuis, je checksum komt niet overeen voor pakketje : " + sequenceNum);
        }

    }


    public void initiateFin(UDPFileServer server, InetAddress packetAddress, PacketSender packetSender, byte[] data) {

        System.arraycopy(data, 4, seqNum, 0, BLOCK_LENGTH);
        finSequenceNumber = ByteBuffer.wrap(seqNum).getInt();
        finIsReceived = true;
        initiateHandler(server, packetAddress, packetSender, data);

    }

    private boolean allPacketsPresent() {
        for (int index = 1; index <= finSequenceNumber; index++){
            if (!receivedDataPackets.contains(index)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkChecksum(byte[] data) {
        StandardHeader header = new StandardHeader();
        return Arrays.equals(header.getChecksum(data), checkSum);
    }

    public Set<Integer> getReceivedDataPackets() {
        return receivedDataPackets;
    }

}
