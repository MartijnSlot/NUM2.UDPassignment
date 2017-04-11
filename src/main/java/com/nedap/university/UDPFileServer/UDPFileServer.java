package com.nedap.university.UDPFileServer;

import com.nedap.university.application.FileCompiler;
import com.nedap.university.application.FileSplitter;
import com.nedap.university.datalinkLayer.NetworkLayer;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class UDPFileServer extends Thread {

    private static final int MULTICAST_PORT = 1234;
    private FileSplitter fileSplitter;
    private FileCompiler fileCompiler;
    private NetworkLayer networkLayer;
    private serverConnection serverConnection;


    public UDPFileServer(InetAddress hostName) {
        networkLayer = new NetworkLayer(this, hostName);
        fileCompiler = new FileCompiler();
        fileSplitter = new FileSplitter();
    }

    @Override
    public void run() {
        networkLayer.receiveMulticastPacket(MULTICAST_PORT);
        init();
    }

    private void init() {
        byte[] init = new byte[0];
        networkLayer.sendPacket(init, MULTICAST_PORT);
    }

    public void handleReceivedPacket(DatagramPacket receivePacket) {
        if (serverConnection == null){
            serverConnection.setInetAddress(receivePacket.getAddress());
            serverConnection.setPort(receivePacket.getPort());
            byte[] data = createBroadcastResponse(receivePacket);
            networkLayer.sendPacket(data, serverConnection.getPort());
        }
        if (receivePacket.getAddress() == serverConnection.getInetAddress() && receivePacket.getPort() == serverConnection.getPort()) {
            byte[] data = createBroadcastResponse(receivePacket);
            networkLayer.sendPacket(data, serverConnection.getPort());
        }
    }

    private byte[] createBroadcastResponse(DatagramPacket receivePacket) {
        String response = "hello";
        return response.getBytes();
    }

}
