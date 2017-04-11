package com.nedap.university.datalinkLayer;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class PacketReceiver extends Thread {

    private int port;
    private UDPFileServer server;
    int length = 50000;
    byte[] buffer = new byte[length];

    public PacketReceiver(int port, UDPFileServer server) {
        this.port = port;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            DatagramSocket serverSocket = new DatagramSocket(port);
            boolean fileComplete = false;

            System.out.printf("Listening on headers:%s:%d%n", InetAddress.getLocalHost().getHostAddress(), port);
//            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            while(!fileComplete) {
                byte[] receiveData;
                DatagramPacket packet = new DatagramPacket(buffer, length);
                serverSocket.receive(packet);
                receiveData = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), receiveData, 0, packet.getLength());

                int packetPort = packet.getPort();
                InetAddress packetAddress = packet.getAddress();
                String str = new String(receiveData, StandardCharsets.UTF_8);

                System.out.printf("Received packet from: %s:%d%n", packet.getAddress(), packet.getPort());
                System.out.println("Received Packet data:" + str);

                if (str.contains("Hello,")) {
                    server.handleReceivedmDNSPacket(packetAddress, packetPort);
                    fileComplete = true;
                }

                if (str.contains("Is it me")) {
                    server.handleReceivedmDNSResponse(receiveData, packetAddress, packetPort);
                    fileComplete = true;
                }
            }

            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
