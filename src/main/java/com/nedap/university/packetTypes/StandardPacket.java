package com.nedap.university.packetTypes;

import java.net.DatagramPacket;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public abstract class StandardPacket {
    public int port;
    public String address;

    public abstract DatagramPacket createPacket();

}
