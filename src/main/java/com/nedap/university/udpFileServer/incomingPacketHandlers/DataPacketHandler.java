package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;

/**
 * Created by martijn.slot on 15/04/2017.
 */
public class DataPacketHandler implements packetHandler {

    @Override
    public void start(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender packetSender, byte[] data) {
        //TODO chop header

    }
}
