package com.nedap.university.packetTypes;

import com.nedap.university.header.StandardHeader;


/**
 * Created by martijn.slot on 11/04/2017.
 */
public class DataPacket extends StandardPacket {

    private static final int FILE_NAME_BYTES = 20;

    public DataPacket(){
        super();
    }

    public byte[] createPacket(byte[] dataBytes, String fileToSend, int seqNumber) {

        byte[] fileNameFlag = new byte[FILE_NAME_BYTES];
        byte[] fileNameBytes = fileToSend.getBytes();
        System.arraycopy(fileNameBytes, 0, fileNameFlag, 0, fileNameBytes.length);

        byte[] data = new byte[fileNameFlag.length + dataBytes.length];
        System.arraycopy(fileNameFlag, 0, data, 0, fileNameFlag.length);
        System.arraycopy(dataBytes, 0, data, fileNameFlag.length, dataBytes.length);

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

    public byte[] createFinPacket(byte[] dataBytes, String fileToSend, int seqNumber) {

        byte[] fileNameFlag = new byte[FILE_NAME_BYTES];
        byte[] fileNameBytes = fileToSend.getBytes();
        System.arraycopy(fileNameBytes, 0, fileNameFlag, 0, fileNameBytes.length);

        byte[] data = new byte[fileNameFlag.length + dataBytes.length];
        System.arraycopy(fileNameFlag, 0, data, 0, fileNameFlag.length);
        System.arraycopy(dataBytes, 0, data, fileNameFlag.length, dataBytes.length);

        StandardHeader dataHeader = new StandardHeader();
        dataHeader.setFlags("01101000");
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
