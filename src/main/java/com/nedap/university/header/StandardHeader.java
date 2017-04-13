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

    public StandardHeader() {
        super();
    }

    public byte[] toBytes() {
        return concat(flags, length, sequenceNumber, checkSum);
    }

    public byte[] intToByteArray(int a) {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public void setChecksum(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(data);
            checkSum = Arrays.copyOfRange(thedigest, 0, 4);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Checksum failed to compose!");
        }
    }

    public void setLength(int a) {
        length = intToByteArray(UDPFileServer.getHeaderLength() + a);
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
        int lLen = length.length;
        int snLen = sequenceNumber.length;
        int csLen = checkSum.length;

        byte[] header = new byte[fLen + lLen + snLen + csLen];
        System.arraycopy(flag, 0, header, 0, fLen);
        System.arraycopy(length, 0, header, fLen, lLen);
        System.arraycopy(sequenceNumber, 0, header, fLen + lLen, snLen);
        System.arraycopy(checkSum, 0, header, fLen + lLen + snLen, csLen);
        return header;
    }
}
