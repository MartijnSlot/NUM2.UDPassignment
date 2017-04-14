package com.nedap.university.packetTypes;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public abstract class StandardPacket {
    public byte[] data;
    public byte[] header;

    public abstract byte[] createPacket(byte[] data);

}
