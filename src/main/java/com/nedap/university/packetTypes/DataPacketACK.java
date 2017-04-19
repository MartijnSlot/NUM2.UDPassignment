package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class DataPacketACK extends StandardPacket {

    public DataPacketACK(){
        super();
    }

    public byte[] createPacket(int seqNumber) {
        byte [] data = "ACK".getBytes();

        StandardHeader dataHeader = new StandardHeader();
        dataHeader.setFlags("00101000");
        dataHeader.setLength(data.length);
        dataHeader.setSequenceNumber(seqNumber);
        dataHeader.setChecksum(data);
        header = dataHeader.toBytes();


        byte[] dataPacket = new byte[data.length + header.length];
        System.arraycopy(header, 0, dataPacket, 0, header.length);
        System.arraycopy(data, 0, dataPacket, header.length, data.length);

        return dataPacket;

    }
}
