package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.header.StandardHeader;
import com.nedap.university.protocols.protocol1.Receiver;
import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by martijn.slot on 15/04/2017.
 */
public class DataPacketHandler implements packetHandler {

    static int blocklength = StandardHeader.getBlockLength();

    private byte[] datalength = new byte[blocklength];
    private byte[] seqNum = new byte[blocklength];
    private byte[] checkSum = new byte[blocklength];
    private byte[] fileName = new byte[20];
    int protocolID;

    public DataPacketHandler(){

    }

    @Override
    public void start(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender apekop, byte[] data) {

        apekop.setFinishedSending(true);
        System.out.println("Received Data packet");
        System.out.println("Received file list from PI");
        byte[] datafile = new byte[data.length - 3*blocklength];

        System.arraycopy(data, 0, datalength, 0, blocklength);
        System.arraycopy(data, 4, seqNum, 0, blocklength);
        System.arraycopy(data, 8, checkSum, 0, blocklength);
        System.arraycopy(data, 13, fileName, 0, 20);
        System.arraycopy(data, 13, datafile, 0, data.length - 3*blocklength);

        if(checkChecksum(datafile)) {
            protocolID = udpFileServer.getProtocol();
            if (protocolID == 1 ) {
                if (ByteBuffer.wrap(seqNum).getInt() == 1){
                    Receiver receiver = new Receiver(); //TODO
                    receiver.initiateReceiver();
                }
            }

        } else {
            System.out.println("wat een gekkenhuis, je checksum komt niet overeen voor pakketje : " + ByteBuffer.wrap(seqNum).getInt());
        }

    }


    private boolean checkChecksum(byte[] data) {
        StandardHeader header = new StandardHeader();
        return Arrays.equals(header.getChecksum(data), checkSum);
    }

}
