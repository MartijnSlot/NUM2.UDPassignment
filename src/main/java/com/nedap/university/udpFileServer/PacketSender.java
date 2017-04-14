package com.nedap.university.udpFileServer;

import com.nedap.university.packetTypes.FileListQuery;
import com.nedap.university.packetTypes.FileListQueryResponse;
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
    private boolean multicast;
    private boolean multicastAck;
    private boolean sendListQuery;
    private boolean listQueryAck = false;
    private boolean sendListQueryResponse;
    private boolean finishedSending = false;
    private static final int INITIATION_TIMER = 400;
    private static final int RESPONSE_TIMER = 2000;


    public PacketSender(UDPFileServer server, DatagramSocket serverSocket) {
        this.server = server;
        this.socket = serverSocket;
    }

    @Override
    public void run() {

        while (socket != null && !finishedSending) {

            if (multicast) {
                sendMulticastPacket();

            }

            if (multicastAck) {
                sendMulticastPacketResponse();
            }

            if (sendListQuery && !listQueryAck) {
                sendListQuery(1);
            }

            if (sendListQueryResponse) {
                sendListQueryResponse();

            }
        }
    }

    private void sendListQuery(int i) {
        for (int a = 1; a <= i; a++) {
            try {
                byte[] fileListQuery = new FileListQuery().createPacket("List request".getBytes());
                DatagramPacket sendpkt = new DatagramPacket(fileListQuery,
                        fileListQuery.length, server.externalhost, UDPFileServer.PORT);
                System.out.println("Sending File List Query......");
                socket.send(sendpkt);
                waiting(RESPONSE_TIMER);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("ERROR: niet zo cool ouwe, list query packet niet verzonden.");
            }
        }
        sendListQuery = false;
    }

    private void sendListQueryResponse() {
        sendListQuery = false;
        try {
            byte[] fileListQueryResponse = new FileListQueryResponse().createPacket(server.getFileBytes(server.localFiles));
            DatagramPacket sendpkt = new DatagramPacket(fileListQueryResponse,
                    fileListQueryResponse.length, server.externalhost, UDPFileServer.PORT);
            System.out.println("Sending File List......");
            socket.send(sendpkt);
            waiting(RESPONSE_TIMER);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: niet zo cool ouwe, list query response packet niet verzonden.");
        }
    }

    private void sendMulticastPacket() {
        try {
            byte[] mDNSPacket = new mDNSSyn().createPacket("Hello,".getBytes());
            DatagramPacket sendpkt = new DatagramPacket(mDNSPacket, mDNSPacket.length, InetAddress.getByName(MULTICAST_ADDRESS), UDPFileServer.PORT);
            System.out.println("Sending mDNS packet......");
            socket.send(sendpkt);
            waiting(INITIATION_TIMER);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: niet zo cool ouwe, multicast packet niet verzonden.");
        }
    }

    private void sendMulticastPacketResponse() {
        try {
            byte[] mDNSResponse = new mDNSSynAck().createPacket("Is it me you are looking for?".getBytes());
            DatagramPacket sendpkt = new DatagramPacket(mDNSResponse, mDNSResponse.length, server.externalhost, UDPFileServer.PORT);
            System.out.println("Sending mDNS response......");
            socket.send(sendpkt);
            waiting(INITIATION_TIMER);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: niet zo cool ouwe, multicast packet Response niet verzonden.");
        }
    }

    private void waiting(int Timer) {
        try {
            Thread.sleep(Timer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setMulticastAck(boolean multicastAck) {
        this.multicastAck = multicastAck;
    }

    public void setMulticast(boolean multicast) {
        this.multicast = multicast;
    }

    public void setSendListQuery(boolean sendListQuery) {
        this.sendListQuery = sendListQuery;
    }

    public void setSendListQueryResponse(boolean sendListQueryResponse) {
        this.sendListQueryResponse = sendListQueryResponse;
    }

    public void setFinishedSending(boolean finishedSending) {
        this.finishedSending = finishedSending;
    }


    public void setListQueryAck(boolean listQueryAck) {
        this.listQueryAck = listQueryAck;
    }
}

