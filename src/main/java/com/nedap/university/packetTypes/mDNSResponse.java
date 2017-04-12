package com.nedap.university.packetTypes;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class mDNSResponse extends StandardPacket {

    public mDNSResponse() {
        super();
    }

    @Override
    public byte[] createPacket() {
        data = "Is it me you are looking for?".getBytes();
//        header =
        byte[] dataPacket = new byte[data.length];
        System.arraycopy(data, 0, dataPacket, 0, data.length);
        return dataPacket;
    }
}
