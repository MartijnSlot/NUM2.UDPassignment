package com.nedap.university.protocols.protocol1;

import com.nedap.university.application.FileSplitter;
import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.DatagramSocket;

/**
 * Created by martijn.slot on 18/04/2017.
 */
public class Receiver {

    private String fileToReceive;
    private UDPFileServer server;
    private DatagramSocket socket;
    private PacketSender packetSender;

    public Receiver(DatagramSocket socket, UDPFileServer server, String fileToReceive) {
        this.socket = socket;
        this.server = server;
        this.fileToReceive = fileToReceive;


    }
    public void initiateReceiver() {
        packetSender = new PacketSender(server, socket);
        packetSender.setSendDataAcks(true);
        packetSender.start();

    }

    public void sendAck(int seqNum) {

    }
}
