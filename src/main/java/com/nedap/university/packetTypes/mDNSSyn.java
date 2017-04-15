package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class mDNSSyn extends StandardPacket {

    public mDNSSyn() {
        super();
    }

    public byte[] createPacket() {

        byte[] data = "Hello?".getBytes();
        int seqNumber = 1;

        StandardHeader mDNSheader = new StandardHeader();
        mDNSheader.setFlags("00000001");
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
