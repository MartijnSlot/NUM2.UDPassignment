package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public interface PacketHandler {

    void initiateHandler(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender packetSender, byte[] data);

}
