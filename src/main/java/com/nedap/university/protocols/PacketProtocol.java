package com.nedap.university.protocols;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public interface PacketProtocol {

    byte[] toBytes();
    byte[] intToByteArray(int a);
}
