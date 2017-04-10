package com.nedap.university.Protocols.udp;

import com.nedap.university.Protocols.PacketProtocol;
import com.sun.tools.javac.util.ArrayUtils;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class UDPheader implements PacketProtocol {

    private int sourcePort;
    private int destPort;
    private int length;
    private int checkSum;

    public UDPheader(int sourcePort, int destPort, int length, int checksum) {
        this.sourcePort = sourcePort;
        this.destPort = destPort;
        this.length = length;
        this.checkSum = checksum;
    }

    @Override
    public byte[] toBytes() {
        byte[] sourcePortBytes = intToByteArray(sourcePort);
        byte[] destPortBytes = intToByteArray(destPort);
        byte[] lengthBytes = intToByteArray(length);
        byte[] checksumBytes = intToByteArray(checkSum);

        return concat(sourcePortBytes, destPortBytes, lengthBytes, checksumBytes);
    }

    @Override
    public byte[] intToByteArray(int a) {
        byte[] byteArray = new byte[4];
        byteArray[3] = (byte) (a & 0xFF);
        byteArray[2] = (byte) ((a >> 8) & 0xFF);
        byteArray[1] = (byte) ((a >> 16) & 0xFF);
        byteArray[0] = (byte) ((a >> 24) & 0xFF);
        return byteArray;
    }

    private byte[] concat(byte[] sourcePortBytes, byte[] destPortBytes, byte[] lengthBytes, byte[] checksumBytes) {
        int spbLen = sourcePortBytes.length;
        int dpLen = destPortBytes.length;
        int lLen = lengthBytes.length;
        int csLen = checksumBytes.length;

        byte[] header = new byte[spbLen + dpLen + lLen + csLen];
        System.arraycopy(sourcePortBytes, 0, header, 0, spbLen);
        System.arraycopy(destPortBytes, 0, header, spbLen, dpLen);
        System.arraycopy(lengthBytes, 0, header, dpLen, lLen);
        System.arraycopy(checksumBytes, 0, header, lLen, csLen);
        return header;
    }
}
