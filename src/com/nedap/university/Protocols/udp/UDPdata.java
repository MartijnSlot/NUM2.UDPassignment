package com.nedap.university.Protocols.udp;

import com.nedap.university.Protocols.PacketProtocol;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class UDPdata implements PacketProtocol {

    @Override
    public byte[] toBytes() {
        return new Byte[0];
    }
}
