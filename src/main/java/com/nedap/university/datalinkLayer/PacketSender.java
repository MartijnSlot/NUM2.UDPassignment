package com.nedap.university.datalinkLayer;

import com.nedap.university.packetTypes.mDNSPacket;
import com.nedap.university.packetTypes.mDNSResponse;
import com.nedap.university.protocols.LengthChecksumHeader;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;


/**
 * Created by martijn.slot on 07/04/2017.
 */
public class PacketSender extends Thread {

    private static final int HEADER_LENGTH = 4;
    private int port;
    private static final String MULTICAST_ADDRESS = "192.168.40.255";

    public PacketSender(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(port);
            byte[] sendHeader = new LengthChecksumHeader(HEADER_LENGTH).toBytes();
            DatagramPacket sendpkt = createDataPacket(sendHeader, sendHeader);
            datagramSocket.send(sendpkt);
            System.out.println("Sending UDP packet......");
            datagramSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private DatagramPacket createDataPacket(byte[] sendHeader, byte[] data) throws UnknownHostException {
        byte[] dataPacket = new byte[HEADER_LENGTH + data.length];
        System.arraycopy(sendHeader, 0, dataPacket, 0, HEADER_LENGTH);
        System.arraycopy(data, 0, dataPacket, HEADER_LENGTH, data.length);

        return new DatagramPacket(dataPacket, HEADER_LENGTH + data.length, InetAddress.getByName(MULTICAST_ADDRESS), port);
    }

    void sendMulticastPacket() {
        try {
            DatagramSocket multicastSocket = new MulticastSocket();
            DatagramPacket sendpkt = new mDNSPacket(InetAddress.getByName(MULTICAST_ADDRESS), port).createPacket();
            System.out.println("Sending mDNS packet......");
            multicastSocket.send(sendpkt);
            multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
           System.out.println("ERROR: niet zo cool ouwe, multicast packet niet verzonden.");
        }
    }

    void sendMulticastPacketResponse(InetAddress packetAddress) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            System.out.println(packetAddress.toString());
            DatagramPacket sendpkt = new mDNSResponse(packetAddress, port).createPacket();
            System.out.println("Sending mDNS response......");
            datagramSocket.send(sendpkt);
            datagramSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR: niet zo cool ouwe, multicast packet Response niet verzonden.");
        }
    }
}
