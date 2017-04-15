package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;


import java.net.InetAddress;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class FileListQueryHandler implements packetHandler {

    @Override
    public void start(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender apekop, byte[] data) {
        apekop.setFinishedSending(true);
        System.out.println("Received List Query");
            PacketSender packetSender = new PacketSender(udpFileServer, udpFileServer.getSocket());
            packetSender.setSendListQueryResponse(true);
            packetSender.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            packetSender.setFinishedSending(true);
    }
}
