package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class FileQuery extends StandardPacket {

    public byte[] createPacket(int fileID) {

        byte[] data = new byte[4];
        data[3] = (byte) (fileID & 0xFF);
        data[2] = (byte) ((fileID >> 8) & 0xFF);
        data[1] = (byte) ((fileID >> 16) & 0xFF);
        data[0] = (byte) ((fileID >> 24) & 0xFF);
        int seqNumber = 1;

        StandardHeader lsHeader = new StandardHeader();
        lsHeader.setFlags("00000010");
        lsHeader.setLength(data.length);
        lsHeader.setSequenceNumber(seqNumber);
        lsHeader.setChecksum(data);
        header = lsHeader.toBytes();


        byte[] dataPacket = new byte[data.length + header.length];
        System.arraycopy(header, 0, dataPacket, 0, header.length);
        System.arraycopy(data, 0, dataPacket, header.length, data.length);

        return dataPacket;
    }
}