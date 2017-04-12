package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class mDNSPacket extends StandardPacket {

    public mDNSPacket() {
        super();
    }

    @Override
    public byte[] createPacket() {
        data = "Hello,".getBytes();

        StandardHeader sh = new StandardHeader();
        sh.setFlags("00000001");
        sh.setLength(data.length);
        sh.setSequenceNumber(1);
        sh.setChecksum(data);
        header = sh.toBytes();


        byte[] dataPacket = new byte[data.length + header.length];
        System.arraycopy(header, 0, dataPacket, 0, header.length);
        System.arraycopy(data, 0, dataPacket, header.length, data.length);

        return dataPacket;
    }
}
