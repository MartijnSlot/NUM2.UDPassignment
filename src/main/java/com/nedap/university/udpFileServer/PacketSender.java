package com.nedap.university.udpFileServer;

import com.nedap.university.packetTypes.mDNSPacket;
import com.nedap.university.packetTypes.mDNSResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


/**
 * Created by martijn.slot on 07/04/2017.
 */
public class PacketSender extends Thread {

    private final DatagramSocket socket;
    private final UDPFileServer server;
    private static final String MULTICAST_ADDRESS = "192.168.40.255";
    private boolean multicastAcked = false;
    private BufferedReader humanInput;

    public PacketSender(UDPFileServer server, DatagramSocket serverSocket) {
        this.server = server;
        this.socket = serverSocket;
    }

    @Override
    public void run() {
        humanInput = new BufferedReader(new InputStreamReader(System.in));
        while(socket != null) {
            if (!multicastAcked) {
                sendMulticastPacket();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (multicastAcked && server.externalhost != null) {
                sendMulticastPacketResponse(server.externalhost);
                waitForInput();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void waitForInput() {
        String fromPlayer;
        try {
            System.out.println("\nEnter the following commands: \n-- ls --  Send fileQuery \n" +
                    "-- upload  'fileID' -- Upload file with fileID \n-- download 'fileID' -- Download file with fileID\n");
            fromPlayer = humanInput.readLine();
            if (humanInput != null) {
                if (fromPlayer.startsWith("ls")) {
                    System.out.println("Sending file query request");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setMulticastAcked(boolean multicastAcked) {
        this.multicastAcked = multicastAcked;
    }

    private void sendMulticastPacket() {
        try {
            byte[] mDNSPacket = new mDNSPacket().createPacket();
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
            byte[] mDNSResponse = new mDNSResponse().createPacket();
            System.out.println(packetAddress.toString());
            DatagramPacket sendpkt = new DatagramPacket(mDNSResponse, mDNSResponse.length, server.externalhost, UDPFileServer.PORT);
            System.out.println("Sending mDNS response......");
            socket.send(sendpkt);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: niet zo cool ouwe, multicast packet Response niet verzonden.");
        }
    }
}

