package com.nedap.university.udpFileServer.incomingPacketHandlers;

import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;
import com.nedap.university.udpFileServer.dataHandlers.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class ListQueryResponseHandler implements packetHandler {

    @Override
    public void start(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender apekop, byte[] data) {
        System.out.println("Received file list from PI");
        Map<Integer, String> fileList = new HashMap<>();
        try {
            fileList = (Map<Integer, String>) MapToBytesAndBack.deserialize(data);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        udpFileServer.printFiles(fileList);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        apekop.setListQueryAck(true);
        udpFileServer.waitForInput();
    }
}
