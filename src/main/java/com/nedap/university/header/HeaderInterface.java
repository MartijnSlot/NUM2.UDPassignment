package com.nedap.university.header;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public abstract class HeaderInterface {

    byte[] checkSum;
    byte[] length;
    byte[] sequenceNumber;
    byte[] flags;


    abstract byte[] toBytes();
    abstract byte[] intToByteArray(int a);
    abstract byte[] setChecksum(byte[] data);
    abstract byte[] setFlags(byte[] a);


}
