package com.nedap.university.protocols;

import com.nedap.university.protocols.HeaderInterface;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class LengthChecksumHeader implements HeaderInterface {

    private int length;
    private int checkSum = 12;

    public LengthChecksumHeader(int length) {
        this.length = length;
    }

    @Override
    public byte[] toBytes() {
        byte[] lengthBytes = intToByteArray(length);
        byte[] checksumBytes = intToByteArray(checkSum);

        return concat(lengthBytes, checksumBytes);
    }

    @Override
    public byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[0] = (byte) (a & 0xFF);
        ret[1] = (byte) ((a >> 8) & 0xFF);
        ret[2] = (byte) ((a >> 16) & 0xFF);
        ret[3] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    private byte[] concat(byte[] lengthBytes, byte[] checksumBytes) {
        int lLen = lengthBytes.length;
        int csLen = checksumBytes.length;

        byte[] header = new byte[lLen + csLen];
        System.arraycopy(lengthBytes, 0, header, 0, lLen);
        System.arraycopy(checksumBytes, 0, header, lLen, csLen);
        return header;
    }
}
