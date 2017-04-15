package com.nedap.university.packetTypes;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class DataPacketACK extends StandardPacket {

    public byte[] createPacket(Integer[] data, int seqNumber) {
        return new byte[0];
    }
}
