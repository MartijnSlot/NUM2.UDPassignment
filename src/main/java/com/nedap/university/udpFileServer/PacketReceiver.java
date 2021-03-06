package com.nedap.university.udpFileServer;

import java.io.IOException;
import java.net.*;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class PacketReceiver extends Thread {

    private static final int RECEIVER_SLEEP = 50;
    private final UDPFileServer server;
    private DatagramSocket serverSocket;
    private int length = 50000;
    private byte[] buffer = new byte[length];
    private boolean finishedReceiving = false;

    public PacketReceiver(UDPFileServer server, DatagramSocket serverSocket) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {

            System.out.printf("Listening on address : %s:%d%n", server.localhost, server.PORT);

            while(serverSocket != null && !finishedReceiving) {
                byte[] receiveData;
                DatagramPacket packet = new DatagramPacket(buffer, length);
//                System.out.printf("Waiting for next packet on : %s:%d%n", server.localhost.getHostAddress(), server.PORT);
                serverSocket.receive(packet);

                receiveData = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), packet.getOffset(), receiveData, 0, packet.getLength());

                int packetPort = packet.getPort();
                InetAddress packetAddress = packet.getAddress();

//                System.out.printf("Received packet from : %s:%d%n", packetAddress.getHostAddress(), packetPort);

                server.handleReceivedPacket(receiveData, packetAddress);

//                Thread.sleep(RECEIVER_SLEEP);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFinishedReceiving(boolean finishedReceiving) {
        this.finishedReceiving = finishedReceiving;
    }
}
