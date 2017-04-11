package com.nedap.university.datalinkLayer;

import com.nedap.university.UDPFileServer.UDPFileServer;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class NetworkLayer {

    private DatagramSocket socket;
    private InetAddress hostName;
    private PacketReceiver packetReceiver;
    private PacketSender packetSender;
    private UDPFileServer server;

    /**
     * Constructs the network layer
     */
    public NetworkLayer(UDPFileServer server, InetAddress hostName){
        this.hostName = hostName;
        this.server = server;
    }

    /**
     * Send a packet through the unreliable medium
     */
    public void sendPacket(byte[] data, int port) {
        packetSender = new PacketSender(data, port);
        packetSender.start();
    }

    /**
     * Receive a packet from the unreliable medium
     * @return The content of the packet as an array of Integers, or null if no packet was received
     * @param port
     */
    public void receiveMulticastPacket(int port){
        packetReceiver = new PacketReceiver(port, server);
        packetReceiver.start();
    }
}