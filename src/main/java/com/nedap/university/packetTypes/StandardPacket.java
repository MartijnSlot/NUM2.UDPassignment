package com.nedap.university.packetTypes;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public abstract class StandardPacket {
    public int port;
    public InetAddress address;

    public abstract DatagramPacket createPacket();

}
