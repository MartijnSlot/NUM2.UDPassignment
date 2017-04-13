package com.nedap.university.udpFileServer;

import com.nedap.university.packetTypes.mDNSSyn;
import com.nedap.university.packetTypes.mDNSSynAck;

import java.io.IOException;
import java.net.*;


/**
 * Created by martijn.slot on 07/04/2017.
 */
public class PacketSender extends Thread {

    private final DatagramSocket socket;
    private final UDPFileServer server;
    private static final String MULTICAST_ADDRESS = "192.168.40.255";
    private boolean multicast = true;
    private boolean multicastAck;
    private int sleeptimer = 5000;

    public PacketSender(UDPFileServer server, DatagramSocket serverSocket) {
        this.server = server;
        this.socket = serverSocket;
    }

    @Override
    public void run() {

        while(socket != null) {

            if (multicast) {
                sendMulticastPacket();
                try {
                    Thread.sleep(sleeptimer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (multicastAck) {
                sendMulticastPacketResponse(server.externalhost);
                try {
                    Thread.sleep(sleeptimer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendMulticastPacket() {
        try {
            byte[] mDNSPacket = new mDNSSyn().createPacket();
            DatagramPacket sendpkt = new DatagramPacket(mDNSPacket, mDNSPacket.length, InetAddress.getByName(MULTICAST_ADDRESS), UDPFileServer.PORT);
            System.out.println("Sending mDNS packet......");
            socket.send(sendpkt);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: niet zo cool ouwe, multicast packet niet verzonden.");
        }
    }

    private void sendMulticastPacketResponse(InetAddress packetAddress) {
        try {
            byte[] mDNSResponse = new mDNSSynAck().createPacket();
            System.out.println(packetAddress.toString());
            DatagramPacket sendpkt = new DatagramPacket(mDNSResponse, mDNSResponse.length, server.externalhost, UDPFileServer.PORT);
            System.out.println("Sending mDNS response......");
            socket.send(sendpkt);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: niet zo cool ouwe, multicast packet Response niet verzonden.");
        }
    }

    void setMulticastAck(boolean multicastAck) {
        this.multicastAck = multicastAck;
    }

    void setMulticast(boolean multicast) {
        this.multicast = multicast;
    }
}

