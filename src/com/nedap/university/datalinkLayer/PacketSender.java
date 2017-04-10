package com.nedap.university.datalinkLayer;

import com.nedap.university.Protocols.udp.UDPheader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;


/**
 * Created by martijn.slot on 07/04/2017.
 */
public class PacketSender extends Thread {

    private final byte[] data;
    private static final int PORT = 1234;
    private static final int DEST_PORT = 1234;
    private static final int HEADER_LENGTH = 8;

    public PacketSender(byte[] data) {
        this.data = data;
    }

    @Override
    public void run() {
        if (data.length == 0) {
            try {
                MulticastSocket multicastSocket = new MulticastSocket(PORT);
                byte[] sendHeader = new UDPheader(PORT, DEST_PORT, HEADER_LENGTH, 12).toBytes();
                DatagramPacket sendpkt = createDataPacket(sendHeader, data);
                multicastSocket.send(sendpkt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                DatagramSocket datagramSocket = new DatagramSocket(PORT);
                byte[] sendHeader = new UDPheader(PORT, DEST_PORT, HEADER_LENGTH, 12).toBytes();
                DatagramPacket sendpkt = createDataPacket(sendHeader, data);
                datagramSocket.send(sendpkt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private DatagramPacket createDataPacket(byte[] sendHeader, byte[] data) {
        byte[] dataPacket = new byte[HEADER_LENGTH + data.length];
        System.arraycopy(sendHeader, 0, dataPacket, 0, HEADER_LENGTH);
        System.arraycopy(data, 0, dataPacket, HEADER_LENGTH, data.length);

        return new DatagramPacket(dataPacket, HEADER_LENGTH + data.length);
    }
}
