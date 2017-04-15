package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class FileListQuery extends StandardPacket {

    public byte[] createPacket() {

        byte [] data = "List Query".getBytes();
        int seqNumber = 1;

        StandardHeader lsHeader = new StandardHeader();
        lsHeader.setFlags("00000100");
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
