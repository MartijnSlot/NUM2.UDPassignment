package com.nedap.university.packetTypes;

import java.net.DatagramPacket;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class DataPacket extends StandardPacket {

    @Override
    public byte[] createPacket(byte[] data, int sequence) {
        return new byte[0];
    }
}
