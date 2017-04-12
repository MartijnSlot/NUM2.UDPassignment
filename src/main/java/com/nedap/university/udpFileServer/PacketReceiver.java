package com.nedap.university.udpFileServer;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class PacketReceiver extends Thread {

    private final UDPFileServer server;
    private DatagramSocket serverSocket;
    private int length = 50000;
    private byte[] buffer = new byte[length];

    public PacketReceiver(UDPFileServer server, DatagramSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {

            System.out.printf("Listening on address : %s:%d%n", server.localhost, server.PORT);

            while(serverSocket != null) {
                byte[] receiveData;
                DatagramPacket packet = new DatagramPacket(buffer, length);
                System.out.printf("Waiting for next packet on : %s:%d%n", server.localhost.getHostAddress(), server.PORT);
                serverSocket.receive(packet);
                receiveData = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), receiveData, 0, packet.getLength());

                int packetPort = packet.getPort();
                InetAddress packetAddress = packet.getAddress();
                String str = new String(receiveData, StandardCharsets.UTF_8);

                System.out.printf("Received packet from : %s:%d%n", packetAddress.getHostAddress(), packetPort);
                System.out.println("Received Packet containing String: " + str);

                if (str.contains("Hello,") && !packetAddress.equals(server.localhost)) {
                    server.handleReceivedmDNSPacket(packetAddress);
                }

                if (str.contains("Is it me")) {
                    server.handleReceivedmDNSResponse(packetAddress);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finished!!");
    }
}
