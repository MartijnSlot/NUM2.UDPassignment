package com.nedap.university.datalinkLayer;

import com.nedap.university.Protocols.udp.UDPdata;
import com.nedap.university.Protocols.udp.UDPheader;
import com.sun.xml.internal.ws.api.message.Packet;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class NetworkLayer {

    private DatagramSocket socket;
    private InetAddress hostName;
    private int serverPort;
    private PacketReceiver packetReceiver;
    private PacketSender packetSender;

    /**
     * Constructs the network layer
     */
    public NetworkLayer(InetAddress hostName, int serverPort){
        this.hostName = hostName;
        this.serverPort = serverPort;
        try {
            this.socket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a packet through the unreliable medium
     */
    public void sendPacket(DatagramPacket udpHeader) {
        packetSender = new PacketSender(this, udpHeader);
        packetSender.start();
    }

    public void sendPacket(DatagramPacket UDPheader, DatagramPacket UDPdata) {
        packetSender = new PacketSender(this, UDPheader, UDPdata);
        packetSender.start();
    }


    /**
     * Receive a packet from the unreliable medium
     * @return The content of the packet as an array of Integers, or null if no packet was received
     */
    public void receivePacket(){
        packetReceiver = new PacketReceiver(this);
        packetReceiver.start();
    }

    public InetAddress getHostName() {
        return hostName;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void sendMDNSpacket() {
        sendPacket();
    }
}