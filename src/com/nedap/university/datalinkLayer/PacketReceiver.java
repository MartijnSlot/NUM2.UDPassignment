package com.nedap.university.datalinkLayer;

import com.nedap.university.UDPFileServer.UDPFileServer;

import java.io.IOException;
import java.net.*;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class PacketReceiver extends Thread {

    private int port;
    private UDPFileServer server;

    public PacketReceiver(int port, UDPFileServer server) {
        this.port = port;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            byte[] receiveData = new byte[8];

            System.out.printf("Listening on udp:%s:%d%n", InetAddress.getLocalHost().getHostAddress(), port);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while(true) {
                serverSocket.receive(receivePacket);
                server.handleReceivedPacket(receivePacket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
