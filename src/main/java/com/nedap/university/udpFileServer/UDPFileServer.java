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
    private FileSplitter fileSplitter;
    private FileCompiler fileCompiler;
    private NetworkLayer networkLayer;
    private ServerConnection serverConnection;



    public UDPFileServer() throws UnknownHostException {
        networkLayer = new NetworkLayer(this);
        fileCompiler = new FileCompiler();
        fileSplitter = new FileSplitter();
    }

    public void init() throws InterruptedException {
        byte[] init = new byte[0];
        networkLayer.sendMulticastPacket(PORT);
        Thread.sleep(50);
        networkLayer.receivePacket(PORT);
    }

    public void handleReceivedPacket(DatagramPacket receivePacket) {
        if (receivePacket.getAddress() == serverConnection.getInetAddress() && receivePacket.getPort() == serverConnection.getPort()) {

        }
    }

    public void handleReceivedmDNSPacket(InetAddress packetAddress, int packetPort) {
            serverConnection = new ServerConnection(packetPort, packetAddress);
            networkLayer.sendMulticastPacketResponse(PORT);

    }


    public void handleReceivedmDNSResponse(byte[] receiveData, InetAddress packetAddress, int packetPort) {
        if (serverConnection.getPort() == packetPort && serverConnection.getInetAddress().equals(packetAddress)) {
            System.out.println("Coolio");
//            byte[] data = createBroadcastResponse();
//            networkLayer.sendPacket(data, serverConnection.getPort());
        }

    }
}
