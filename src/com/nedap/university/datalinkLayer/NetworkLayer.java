package com.nedap.university.datalinkLayer;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class NetworkLayer {

    String hostName;
    int serverPort;

    /**
     * Constructs the network layer
     */
    public NetworkLayer(String hostName, int serverPort){
        this.hostName = hostName;
        this.serverPort = serverPort;
    }

    /**
     * Send a packet through the unreliable medium
     * @param packet
     */
    public void sendPacket(Integer[] packet) throws IllegalArgumentException{
    }

    /**
     * Receive a packet from the unreliable medium
     * @return The content of the packet as an array of Integers, or null if no packet was received
     */
    public void receivePacket(){

    }
}