package com.nedap.university.protocols.protocol1;

import com.nedap.university.application.FileSplitter;
import com.nedap.university.packetTypes.DataPacket;
import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by martijn.slot on 18/04/2017.
 */
public class Sender1 {

    private String fileToSend;
    private UDPFileServer server;
    private DatagramSocket zocket;
    private PacketSender packetSender;
    private FileSplitter fileSplitter = new FileSplitter();
    private Integer[] datafile = fileSplitter.getFileContents(server.getFilePath() + "/" + fileToSend);
    private int numberOfFragments = datafile.length / fileSplitter.getPacketSize() + 1;
    private int seqNumber = 1;

    public Sender1(DatagramSocket socket, UDPFileServer server, String fileToSend) {
        this.zocket = socket;
        this.server = server;
        this.fileToSend = fileToSend;

    }
    public void initiateSender() {
        System.out.println("Number of packets to send in total = " + numberOfFragments);
        packetSender = new PacketSender(server, zocket);
        packetSender.setSendFile(true);
        packetSender.run();
    }

    public byte[] getData() {
        return createDataPacket(fileSplitter, datafile, seqNumber);
    }

    private byte[] createDataPacket(FileSplitter fileSplitter, Integer[] data, int seqNumber) {
        data = fileSplitter.createPacket(seqNumber, data);
        return new DataPacket().createPacket(data, fileToSend, seqNumber);

    }

}
