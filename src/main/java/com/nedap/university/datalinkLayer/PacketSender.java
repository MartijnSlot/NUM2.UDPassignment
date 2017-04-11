package com.nedap.university.datalinkLayer;

import com.nedap.university.protocols.udp.UDPheader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;


/**
 * Created by martijn.slot on 07/04/2017.
 */
public class PacketSender extends Thread {

    private final byte[] data;
    private static final int HEADER_LENGTH = 8;
    private static final int MULTICAST_PORT = 1234;
    private static final int OWN_PORT = 1235;
    private int checkSum = 12;
    private int port = 1236;

    public PacketSender(byte[] data, int port) {
        this.data = data;
        this.port = port;
    }

    @Override
    public void run() {
        if (data.length == 0) {
            try {
                MulticastSocket multicastSocket = new MulticastSocket(OWN_PORT);
                byte[] sendHeader = new UDPheader(OWN_PORT, MULTICAST_PORT, HEADER_LENGTH, checkSum).toBytes();
                DatagramPacket sendpkt = createDataPacket(sendHeader, data);
                System.out.println("Sending mDNS packet......");
                multicastSocket.send(sendpkt);
                multicastSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                DatagramSocket datagramSocket = new DatagramSocket(OWN_PORT);
                byte[] sendHeader = new UDPheader(OWN_PORT, port, HEADER_LENGTH, checkSum).toBytes();
                DatagramPacket sendpkt = createDataPacket(sendHeader, data);
                datagramSocket.send(sendpkt);
                System.out.println("Sending UDP packet......");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private DatagramPacket createmDNSPacket(byte[] sendHeader) {
        byte[] dataPacket = new byte[HEADER_LENGTH];
        System.arraycopy(sendHeader, 0, dataPacket, 0, HEADER_LENGTH);
        return new DatagramPacket(dataPacket, HEADER_LENGTH);
    }

    private DatagramPacket createDataPacket(byte[] sendHeader, byte[] data) {
        byte[] dataPacket = new byte[HEADER_LENGTH + data.length];
        System.arraycopy(sendHeader, 0, dataPacket, 0, HEADER_LENGTH);
        System.arraycopy(data, 0, dataPacket, HEADER_LENGTH, data.length);

        return new DatagramPacket(dataPacket, HEADER_LENGTH + data.length);
    }
}
