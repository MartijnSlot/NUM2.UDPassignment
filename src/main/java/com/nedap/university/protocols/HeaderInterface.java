package com.nedap.university.protocols;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public interface HeaderInterface {

    int checkSum = 12;

    byte[] toBytes();
    byte[] intToByteArray(int a);


}
