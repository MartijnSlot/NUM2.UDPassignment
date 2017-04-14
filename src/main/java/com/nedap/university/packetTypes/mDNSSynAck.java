package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class mDNSSynAck extends StandardPacket {

    public mDNSSynAck() {
        super();
    }

    @Override
    public byte[] createPacket(byte[] data) {
        this.data = data;

        StandardHeader mDNSResponseHeader = new StandardHeader();
        mDNSResponseHeader.setFlags("00100001");
        mDNSResponseHeader.setLength(data.length);
        mDNSResponseHeader.setSequenceNumber(1);
        mDNSResponseHeader.setChecksum(data);
        header = mDNSResponseHeader.toBytes();


        byte[] dataPacket = new byte[data.length + header.length];
        System.arraycopy(header, 0, dataPacket, 0, header.length);
        System.arraycopy(data, 0, dataPacket, header.length, data.length);

        return dataPacket;
    }
}
