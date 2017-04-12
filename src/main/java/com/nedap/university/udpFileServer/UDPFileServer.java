package com.nedap.university.udpFileServer;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;


/**
 * Created by martijn.slot on 10/04/2017.
 */
public class UDPFileServer{


    private PacketReceiver packetReceiver;
    private PacketSender packetSender;
    private DatagramSocket serverSocket;
    final InetAddress localhost;
    static final int PORT = 1234;
    InetAddress externalhost;


    public UDPFileServer() {
        localhost = getLocalAddress();

    }

    public void init() {
        try {
            serverSocket = new DatagramSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        packetReceiver = new PacketReceiver(this, serverSocket);
        packetSender = new PacketSender(this, serverSocket);
        packetReceiver.start();
        packetSender.start();

        try {
            packetReceiver.join();
            System.out.println("packetReceiver joined");
            packetSender.join();
            System.out.println("PacketSender joined");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("uit de while loop");
    }


    public void handleReceivedmDNSPacket(InetAddress packetAddress) {
        externalhost = packetAddress;
        packetSender.setMulticastAcked(true);
    }


    public void handleReceivedmDNSResponse(InetAddress packetAddress) {
        if (!localhost.equals(packetAddress)) {
            externalhost = packetAddress;
            System.out.println("multicastAcked");
            packetSender.setMulticastAcked(true);
        }
    }




    private static InetAddress getLocalAddress(){
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if ( f.getAddress().isSiteLocalAddress() && f.getAddress().getHostAddress().startsWith("192"))
                        return f.getAddress();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

}
