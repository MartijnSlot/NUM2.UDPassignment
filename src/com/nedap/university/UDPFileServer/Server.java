package com.nedap.university.UDPFileServer;

import com.nedap.university.Protocols.udp.UDPheader;
import com.nedap.university.application.FileCompiler;
import com.nedap.university.application.FileSplitter;
import com.nedap.university.datalinkLayer.NetworkLayer;
import com.sun.xml.internal.ws.api.message.Packet;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class Server extends Thread {

    private FileSplitter fileSplitter;
    private FileCompiler fileCompiler;
    private NetworkLayer networkLayer;


    public Server(InetAddress hostName, int serverPort) {
        networkLayer = new NetworkLayer(hostName, serverPort);
        fileCompiler = new FileCompiler();
        fileSplitter = new FileSplitter();
    }

    @Override
    public void run() {
        networkLayer.receivePacket();
        init();
    }

    private void init() {
        networkLayer.sendMDNSpacket();
    }
}
