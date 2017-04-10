package com.nedap.university.UDPFileServer;

import com.nedap.university.application.FileCompiler;
import com.nedap.university.datalinkLayer.NetworkLayer;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class Server extends Thread {

    FileCompiler fileCompiler;
    NetworkLayer networkLayer;
    PacketReceiver packetReceiver;
    PacketSender packetSender;

    public Server(String hostName, int serverPort) {
        networkLayer = new NetworkLayer(hostName, serverPort);

    }

    @Override
    public void run() {
        packetReceiver = new PacketReceiver();
        packetSender = new PacketSender();
        packetReceiver.start();
        packetSender.start();
    }
}
