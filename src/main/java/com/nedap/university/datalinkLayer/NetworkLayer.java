package com.nedap.university.datalinkLayer;

import com.nedap.university.packetTypes.mDNSPacket;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class NetworkLayer {

    private PacketReceiver packetReceiver;
    private PacketSender packetSender;
    private UDPFileServer server;

    /**
     * Constructs the network layer
     */
    public NetworkLayer(UDPFileServer server){
        this.server = server;
    }

    /**
     * Send a packet through the unreliable medium
     */
    public void sendPacket(byte[] data, int port) {
        packetSender = new PacketSender(port);
        packetSender.start();
    }

    /**
     * Receive a packet from the unreliable medium
     * @return The content of the packet as an array of Integers, or null if no packet was received
     * @param port
     */
    public void receivePacket(int port){
        packetReceiver = new PacketReceiver(port, server);
        packetReceiver.start();
    }

    public void sendMulticastPacket(int multicastPort) {
        packetSender = new PacketSender(multicastPort);
        packetSender.sendMulticastPacket();

    }

    public void sendMulticastPacketResponse(InetAddress packetAddress, int port) {
        packetSender = new PacketSender(port);
        packetSender.sendMulticastPacketResponse(packetAddress);

    }
}