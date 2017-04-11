package com.nedap.university.packetTypes;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class mDNSResponse extends StandardPacket {

    public mDNSResponse(String multicastAddress, int port) {
        super();
    }

    @Override
    public DatagramPacket createPacket() {
        String mDNSResponse = "Is it me you are looking for?";
        byte[] dataPacket = new byte[mDNSResponse.getBytes().length];
        System.arraycopy(mDNSResponse.getBytes(), 0, dataPacket, 0, mDNSResponse.getBytes().length);

        DatagramPacket dgPacket = null;
        try {
            dgPacket = new DatagramPacket(dataPacket, mDNSResponse.getBytes().length, InetAddress.getByName(address), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return dgPacket;
    }
}
