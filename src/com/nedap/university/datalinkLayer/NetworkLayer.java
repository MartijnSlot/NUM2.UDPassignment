package com.nedap.university.datalinkLayer;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class NetworkLayer {
    Client client;

    /**
     * Constructs the network layer
     * @param client The challenge client to be used as transmission medium
     */
    public NetworkLayer(DRDTChallengeClient client){
        this.client = client;
    }

    /**
     * Send a packet through the unreliable medium
     * @param packet
     */
    public void sendPacket(Integer[] packet) throws IllegalArgumentException{
        client.sendPacket(packet);
    }

    /**
     * Receive a packet from the unreliable medium
     * @return The content of the packet as an array of Integers, or null if no packet was received
     */
    public Integer[] receivePacket(){
        return client.receivePacket();
    }
}