package com.nedap.university.udpFileServer;

import com.nedap.university.application.FileCompiler;
import com.nedap.university.application.FileSplitter;
import com.nedap.university.datalinkLayer.NetworkLayer;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class UDPFileServer{

    private static final int PORT = 1234;
    private NetworkLayer networkLayer;
    private ServerConnection serverConnection = new ServerConnection(0, null);



    public UDPFileServer() throws UnknownHostException {
        networkLayer = new NetworkLayer(this);
    }

    public void init() throws InterruptedException {
        networkLayer.sendMulticastPacket(PORT);
    }

    public void setForReceive() {
        networkLayer.receivePacket(PORT);
    }

    public void handleReceivedPacket(DatagramPacket receivePacket) {
        if (receivePacket.getAddress() == serverConnection.getInetAddress()) {

        }
    }

    public void handleReceivedmDNSPacket(InetAddress packetAddress, int packetPort) {
        serverConnection = new ServerConnection(packetPort, packetAddress); //TODO niet trekken bij jezelf
        networkLayer.sendMulticastPacketResponse(serverConnection.getInetAddress(), PORT);
        networkLayer.receivePacket(PORT);
    }


    public void handleReceivedmDNSResponse(InetAddress packetAddress) {
        if (serverConnection.getInetAddress().equals(packetAddress)) {
            networkLayer.receivePacket(serverConnection.getPort());
            networkLayer.receivePacket(PORT);
        }

    }
}
