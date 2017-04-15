package com.nedap.university.udpFileServer;

import com.nedap.university.application.FileSplitter;
import com.nedap.university.packetTypes.*;

import java.io.File;
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
    private static final int INITIATION_TIMER = 100;
    private static final int RESPONSE_TIMER = 2000;
    private boolean sendFile;
    private FileSplitter fileSplitter;
    private String fileToSend;
    private Integer[] datafile;
    private int numberOfFragments;

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
                sendListQuery();
            }

            if (sendListQueryResponse) {
                sendListQueryResponse();

            }

            if (sendFile) {
                sendFile();
            }
        }
    }

    private void sendFile() {
        initDataToSend();

                for (int i = 1; i <= numberOfFragments; i++) {
                    byte[] dataPacket = createDataPacket(fileSplitter, datafile, i);
                    DatagramPacket sendpkt = new DatagramPacket(dataPacket, dataPacket.length, server.externalhost, UDPFileServer.PORT);
                    try {
                        socket.send(sendpkt);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("ERROR: niet zo cool ouwe, datapacket : " + i + " packet niet verzonden.");
                    }
                }
                setFinishedSending(true);
    }


    public void initDataToSend() {
        datafile = fileSplitter.getFileContents(server.getFilePath() + "/" + fileToSend);
        numberOfFragments = datafile.length / fileSplitter.getPacketSize() + 1;
        System.out.println("Number of packets to send in total = " + numberOfFragments);
    }

    public byte[] createDataPacket(FileSplitter fileSplitter, Integer[] data, int seqNumber) {
        data = fileSplitter.createPacket(seqNumber, data);
        return new DataPacket().createPacket(data, fileToSend, seqNumber);

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

    public void setFileSplitter(FileSplitter fileSplitter) {
        this.fileSplitter = fileSplitter;
    }

    public void setFileName(String fileToSend) {
        this.fileToSend = fileToSend;
    }
}

