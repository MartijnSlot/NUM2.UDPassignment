package com.nedap.university.header;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class StandardHeader {

    byte[] checkSum;
    byte[] length;
    byte[] sequenceNumber;
    byte[] flags = new byte[1];
    static int HEADERLENGTH = 13;
    static int BLOCKLENGTH = 4;

    public StandardHeader() {
        super();
    }

    public static int getBlockLength() {
        return BLOCKLENGTH;
    }

    public byte[] getChecksum(byte[] data) {
        return setChecksum(data);
    }

    public byte[] toBytes() {
        return concat(flags, length, sequenceNumber, checkSum);
    }

    public byte[] intToByteArray(int a) {
        byte[] ret = new byte[BLOCKLENGTH];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public byte[] setChecksum(byte[] data) {

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(data);
            this.checkSum = Arrays.copyOfRange(thedigest, 0, 4);
            return checkSum;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Checksum failed to compose!");
        }
        return null;
    }

    public void setLength(int a) {
        length = intToByteArray(HEADERLENGTH + a);
    }

    public void setFlags(String a) {
        byte flag = Byte.parseByte(a, 2);
        flags[0] = flag;
    }

    public void setSequenceNumber(int a) {
        sequenceNumber = intToByteArray(a);
    }

    private byte[] concat(byte[] flag, byte[] length, byte[] sequenceNumber, byte[] checkSum) {
        int fLen = flag.length;

        byte[] header = new byte[fLen + 3*BLOCKLENGTH];
        System.arraycopy(flag, 0, header, 0, fLen);
        System.arraycopy(length, 0, header, fLen, BLOCKLENGTH);
        System.arraycopy(sequenceNumber, 0, header, fLen + BLOCKLENGTH, BLOCKLENGTH);
        System.arraycopy(checkSum, 0, header, fLen + 2*BLOCKLENGTH, BLOCKLENGTH);
        return header;
    }
}
