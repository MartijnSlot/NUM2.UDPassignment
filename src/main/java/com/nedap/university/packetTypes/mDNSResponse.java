package com.nedap.university.packetTypes;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by martijn.slot on 11/04/2017.
 */
public class mDNSResponse extends StandardPacket {

    public mDNSResponse(InetAddress ipAddress, int sendport) {
        super();
        address = ipAddress;
        port = sendport;
    }

    @Override
    public DatagramPacket createPacket() {
        System.out.println("mDNSResponse to: " + address + ":" + port);
        String mDNSResponse = "Is it me you are looking for?";
        byte[] dataPacket = new byte[mDNSResponse.getBytes().length];
        System.arraycopy(mDNSResponse.getBytes(), 0, dataPacket, 0, mDNSResponse.getBytes().length);
        DatagramPacket dgPacket = new DatagramPacket(dataPacket, mDNSResponse.getBytes().length, address, port);
        return dgPacket;
    }
}
