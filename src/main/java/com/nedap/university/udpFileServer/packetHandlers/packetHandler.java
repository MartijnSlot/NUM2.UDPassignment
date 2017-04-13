package com.nedap.university.udpFileServer.packetHandlers;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public interface packetHandler {

    void start(UDPFileServer udpFileServer, InetAddress packetAddress);

}
