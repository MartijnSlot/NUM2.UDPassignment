package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;

import java.net.DatagramPacket;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class FileListQuery extends StandardPacket {

    @Override
    public byte[] createPacket(byte[] data, int seqNumber) {
        this.data = data;

        StandardHeader mDNSheader = new StandardHeader();
        mDNSheader.setFlags("00000100");
        mDNSheader.setLength(data.length);
        mDNSheader.setSequenceNumber(seqNumber);
        mDNSheader.setChecksum(data);
        header = mDNSheader.toBytes();


        byte[] dataPacket = new byte[data.length + header.length];
        System.arraycopy(header, 0, dataPacket, 0, header.length);
        System.arraycopy(data, 0, dataPacket, header.length, data.length);

        return dataPacket;
    }
}
