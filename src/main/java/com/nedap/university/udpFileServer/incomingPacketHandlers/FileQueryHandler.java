package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;

/**
 * Created by martijn.slot on 14/04/2017.
 */
public class FileQueryHandler implements PacketHandler {

    @Override
    public void initiateHandler(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender apekop, byte[] data) {
        apekop.setFinishedSending(true);
        System.out.println("Received Download Query");
        PacketSender packetSender = new PacketSender(udpFileServer, udpFileServer.getSocket());
        packetSender.setSendFile(true);
        packetSender.start();
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        packetSender.setFinishedSending(true);
    }
}
