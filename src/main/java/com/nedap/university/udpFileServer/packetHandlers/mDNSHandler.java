package com.nedap.university.udpFileServer.packetHandlers;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class mDNSHandler implements packetHandler {

    public void start(UDPFileServer udpFileServer, InetAddress packetAddress) {
        if (udpFileServer.externalhost == null) {
            udpFileServer.externalhost = packetAddress;
            System.out.println("CONNECTED with external host on : " + udpFileServer.externalhost.getHostAddress());
        } else {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            udpFileServer.establishConnex();
        }
    }

}
