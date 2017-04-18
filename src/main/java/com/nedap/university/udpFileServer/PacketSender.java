package com.nedap.university.udpFileServer;

import com.nedap.university.packetTypes.*;
import com.nedap.university.protocols.protocol1.Protocol1;

import java.io.IOException;
import java.net.*;


/**
 * Created by martijn.slot on 07/04/2017.
 */
public class PacketSender extends Thread {

    private final DatagramSocket socket;
    private final UDPFileServer server;
    private static final String MULTICAST_ADDRESS = "192.168.40.255";
    private Protocol1 protocol1;
    private boolean multicast;
    private boolean multicastAck;
    private boolean sendListQuery;
    private boolean listQueryAck = false;
    private boolean sendListQueryResponse;
    private boolean finishedSending = false;
    private static final int INITIATION_TIMER = 100;
    private static final int RESPONSE_TIMER = 2000;
    private boolean sendFile;
    private boolean sendDataAcks;

    public PacketSender(UDPFileServer server, DatagramSocket serverSocket) {
        this.server = server;
        this.socket = serverSocket;
    }

    public PacketSender(UDPFileServer server, DatagramSocket serverSocket, Protocol1 protocol1) {
        this.server = server;
        this.socket = serverSocket;
        this.protocol1 = protocol1;
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
                sendListQuery();
            }

            if (sendListQueryResponse) {
                sendListQueryResponse();
            }

            if (sendFile) {
                sendDataPacket();
            }

        }
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendDataAcks() {

    }


    public void sendDataPacket() {
        byte [] dataPacket = protocol1.getData();
        if (dataPacket != null) {
            DatagramPacket packet = new DatagramPacket(dataPacket, dataPacket.length, server.externalhost, UDPFileServer.PORT);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void sendListQuery() {
        try {
            byte[] fileListQuery = new FileListQuery().createPacket();
            DatagramPacket sendpkt = new DatagramPacket(fileListQuery,
                    fileListQuery.length, server.externalhost, UDPFileServer.PORT);
            System.out.println("Sending File List Query......");
            socket.send(sendpkt);
            waiting(RESPONSE_TIMER);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: niet zo cool ouwe, list query packet niet verzonden.");
        }
        sendListQuery = false;
    }

    private void sendListQueryResponse() {
        sendListQuery = false;
        try {
            byte[] fileListQueryResponse = new FileListQueryResponse().createPacket(server.getMapBytes(server.localFiles));
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
            byte[] mDNSPacket = new mDNSSyn().createPacket();
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
            byte[] mDNSResponse = new mDNSSynAck().createPacket();
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

    public void setSendFile(boolean sendFile) {
        this.sendFile = sendFile;
    }

    public void setSendDataAcks(boolean sendDataAcks) {
        this.sendDataAcks = sendDataAcks;
    }

}

