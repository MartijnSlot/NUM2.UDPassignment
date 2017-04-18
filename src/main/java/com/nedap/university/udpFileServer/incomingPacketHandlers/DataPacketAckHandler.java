package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.header.StandardHeader;
import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by martijn.slot on 18/04/2017.
 */
public class DataPacketAckHandler implements PacketHandler {

    static int blocklength = StandardHeader.getBlockLength();
    private byte[] seqNumBytes = new byte[blocklength];
    private Set<Integer> receivedAcks = new HashSet<>();


    @Override
    public void initiateHandler(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender packetSender, byte[] data) {

//        packetSender.setFinishedSending(true);

        System.arraycopy(data, 4, seqNumBytes, 0, blocklength);

        int seqNum = ByteBuffer.wrap(seqNumBytes).getInt();
        System.out.println("Received ACK packet with header = " + seqNum);
        receivedAcks.add(seqNum);

    }

    public Set<Integer> getAcks() {
        return receivedAcks;
    }
}
