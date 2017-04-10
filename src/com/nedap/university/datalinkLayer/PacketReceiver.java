package com.nedap.university.datalinkLayer;

import com.nedap.university.datalinkLayer.NetworkLayer;

import java.net.DatagramSocket;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class PacketReceiver extends Thread {


    private NetworkLayer networkLayer;

    public PacketReceiver(NetworkLayer networkLayer) {
        this.networkLayer = networkLayer;
    }

    @Override
    public void run() {
        while (networkLayer.getSocket() != null && networkLayer.getSocket().isConnected()) {

        }
    }
}
