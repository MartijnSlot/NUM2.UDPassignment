package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.application.FileCompiler;
import com.nedap.university.header.StandardHeader;
import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by martijn.slot on 15/04/2017.
 */
public class DataPacketHandler implements PacketHandler {

    private static int blocklength = StandardHeader.getBlockLength();

    private byte[] datalength = new byte[blocklength];
    private byte[] seqNum = new byte[blocklength];
    private byte[] checkSum = new byte[blocklength];
    private byte[] fileNameBytes = new byte[20];
    private FileCompiler fileCompiler = new FileCompiler();


    public DataPacketHandler(){
    }

    @Override
    public void initiateHandler(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender apekop, byte[] data) {

        apekop.setFinishedSending(true);
        byte[] datafile = new byte[data.length - 3*blocklength];
        byte[] onlyData = new byte[data.length - (3*blocklength + fileNameBytes.length)];


        System.arraycopy(data, 0, datalength, 0, blocklength);
        System.arraycopy(data, 4, seqNum, 0, blocklength);
        System.arraycopy(data, 8, checkSum, 0, blocklength);
        System.arraycopy(data, 12, fileNameBytes, 0, 20);
        System.arraycopy(data, 12, datafile, 0, data.length - 3*blocklength);
        System.arraycopy(data, 32, onlyData, 0, data.length - (3*blocklength + fileNameBytes.length));

        int sequenceNum = ByteBuffer.wrap(seqNum).getInt();
        String fileName = new String(fileNameBytes);
        fileName = fileName.replaceAll("[^a-zA-Z.]", "");
        fileName = fileName.toLowerCase();

        if(checkChecksum(datafile)) {
            System.out.println("Received data packet " + sequenceNum + ", from file " + fileName);
            udpFileServer.sendAck(sequenceNum);
            fileCompiler.setFileName(fileName);
            fileCompiler.setPacketInPacketMap(sequenceNum, byteArrayToIntArray(onlyData));
            udpFileServer.checkAllAcks(fileCompiler);
        } else {
            System.out.println("wat een gekkenhuis, je checksum komt niet overeen voor pakketje : " + sequenceNum);
        }

    }

    private boolean checkChecksum(byte[] data) {
        StandardHeader header = new StandardHeader();
        return Arrays.equals(header.getChecksum(data), checkSum);
    }

    private Integer[] byteArrayToIntArray (byte[] byteArray) {
        IntBuffer intBuf = ByteBuffer.wrap(byteArray).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
        int[] dataInts = new int[intBuf.remaining()];
        intBuf.get(dataInts);
        Integer[] dataIntegers = new Integer[dataInts.length];
        int i = 0;
        for (int value : dataInts) {
            dataIntegers[i++] = value;
        }
        return dataIntegers;
    }
}
