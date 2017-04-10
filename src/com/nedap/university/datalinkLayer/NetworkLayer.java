package com.nedap.university.datalinkLayer;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class NetworkLayer {

    private DatagramSocket socket;
    private InetAddress hostName;
    private PacketReceiver packetReceiver;
    private PacketSender packetSender;

    /**
     * Constructs the network layer
     */
    public NetworkLayer(InetAddress hostName, int serverPort){
        this.hostName = hostName;
        try {
            this.socket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a packet through the unreliable medium
     */
    public void sendPacket(byte[] data) {
        packetSender = new PacketSender(data);
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
}