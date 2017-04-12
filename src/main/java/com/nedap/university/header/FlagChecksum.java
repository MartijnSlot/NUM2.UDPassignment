package com.nedap.university.header;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class FlagChecksum extends HeaderInterface {

    public FlagChecksum() {
        super();
    }

    @Override
    public byte[] toBytes() {
        return concat(flags, length, checkSum);
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

    @Override
    byte[] setChecksum(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] thedigest = md.digest(data);
        return Arrays.copyOfRange(thedigest, 0, 4);
    }

    @Override
    byte[] setFlags(byte[] a) {
        return flags;
    }

    private byte[] concat(byte[] flag, byte[] length, byte[] checksum) {
        int fLen = flag.length;
        int lLen = length.length;
        int csLen = checkSum.length;

        byte[] header = new byte[fLen + + lLen + csLen];
        System.arraycopy(flag, 0, header, 0, fLen);
        System.arraycopy(length, 0, header, fLen, lLen);
        System.arraycopy(checksum, 0, header, lLen, csLen);
        return header;
    }
}
