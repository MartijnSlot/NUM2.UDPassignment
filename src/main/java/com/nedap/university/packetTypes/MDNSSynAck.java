package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class MDNSSynAck extends StandardPacket {

    public MDNSSynAck() {
        super();
    }

    public byte[] createPacket() {

        byte[] data = "Is it me you are looking for?".getBytes();
        int sequenceNumber = 1;

        StandardHeader mDNSResponseHeader = new StandardHeader();
        mDNSResponseHeader.setFlags("00100001");
        mDNSResponseHeader.setLength(data.length);
        mDNSResponseHeader.setSequenceNumber(sequenceNumber);
        mDNSResponseHeader.setChecksum(data);
        header = mDNSResponseHeader.toBytes();


        byte[] dataPacket = new byte[data.length + header.length];
        System.arraycopy(header, 0, dataPacket, 0, header.length);
        System.arraycopy(data, 0, dataPacket, header.length, data.length);

        return dataPacket;
    }
}
