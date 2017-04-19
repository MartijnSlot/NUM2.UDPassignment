package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.InetAddress;
import java.nio.ByteBuffer;

import static com.nedap.university.udpFileServer.incomingPacketHandlers.DataPacketAckHandler.blocklength;

/**
 * Created by martijn.slot on 14/04/2017.
 */
public class FileQueryHandler implements PacketHandler {

    @Override
    public void initiateHandler(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender apekop, byte[] data) {

        byte[] onlyData = new byte[data.length - (3*blocklength)];

        System.arraycopy(data, 12, onlyData, 0, data.length - (3*blocklength));

        System.out.println("Received Download Query");
        int fileID = ByteBuffer.wrap(onlyData).getInt();

        udpFileServer.uploadFiles(fileID);
    }
}
