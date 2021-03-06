package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class MDNSHandler implements PacketHandler {

    @Override
    public void initiateHandler(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender packetSender, byte[] data) {
        if (udpFileServer.externalhost == null) {
            udpFileServer.externalhost = packetAddress;
            System.out.println("FOUND external host on : " + udpFileServer.externalhost.getHostAddress());
            try {
                Thread.sleep(210);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            packetSender.setMulticast(false);
            try {
                Thread.sleep(210);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            packetSender.setMulticastAck(true);
            udpFileServer.establishConnex();
        }
    }

}
