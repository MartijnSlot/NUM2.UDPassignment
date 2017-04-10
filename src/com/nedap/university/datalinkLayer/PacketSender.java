package com.nedap.university.datalinkLayer;

import com.nedap.university.Protocols.udp.UDPdata;
import com.nedap.university.Protocols.udp.UDPheader;
import com.sun.xml.internal.ws.api.message.Packet;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;


/**
 * Created by martijn.slot on 07/04/2017.
 */
public class PacketSender extends Thread {

    private NetworkLayer networkLayer;
    private DatagramPacket udpHeader;
    private DatagramPacket udpData;
    private static final int PORT = 1234;
    private static final int DEST_PORT = 1234;
    private static final int HEADER_LENGTH = 8;
    private int datalength;

    public PacketSender(NetworkLayer networkLayer, DatagramPacket udpHeader) {
        this.networkLayer = networkLayer;
        this.udpHeader = udpHeader;
    }

    public PacketSender(NetworkLayer networkLayer, DatagramPacket udpHeader, DatagramPacket udpData) {
        this.networkLayer = networkLayer;
        this.udpHeader = udpHeader;
        this.udpData = udpData;
    }

    @Override
    public void run() {
        while (networkLayer.getSocket() != null && networkLayer.getSocket().isConnected()) {
            if (udpData == null) {
                try {
                    MulticastSocket multicastSocket = new MulticastSocket(PORT);
                    byte[] sendData = new UDPheader(PORT, DEST_PORT, HEADER_LENGTH, 12).toBytes();
                    DatagramPacket pkt = new DatagramPacket(sendData, HEADER_LENGTH);
                    multicastSocket.send(pkt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    DatagramSocket datagramSocket = new DatagramSocket(PORT);
                    byte[] sendData = new UDPheader(PORT, DEST_PORT, HEADER_LENGTH + datalength, 12).toBytes(); //TODO initialize datalen
                    DatagramPacket pkt = new DatagramPacket(sendData, HEADER_LENGTH);
                    datagramSocket.send(pkt);
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
