package com.nedap.university.packetTypes;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class mDNSPacket extends StandardPacket {

    public mDNSPacket(String address, int port) {
        super();
        this.address = address;
        this.port = port;

    }

    @Override
    public DatagramPacket createPacket() {
        String hello = "Hello,";
        byte[] dataPacket = new byte[hello.getBytes().length];
        System.arraycopy(hello.getBytes(), 0, dataPacket, 0, hello.getBytes().length);

        DatagramPacket dgPacket = null;
        try {
            dgPacket = new DatagramPacket(dataPacket, hello.getBytes().length, InetAddress.getByName(address), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return dgPacket;
    }
}
