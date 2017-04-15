package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;


/**
 * Created by martijn.slot on 11/04/2017.
 */
public class DataPacket extends StandardPacket {

    public DataPacket(){
        super();
    }

    public byte[] createPacket(Integer[] dataInts, int seqNumber) {

        byte[] data = new byte[dataInts.length];
        for (int i = 0; i < dataInts.length; i++) {
            data[i] = (byte) ((dataInts[i] & 0x000000ff));
        }

        StandardHeader dataHeader = new StandardHeader();
        dataHeader.setFlags("00001000");
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
