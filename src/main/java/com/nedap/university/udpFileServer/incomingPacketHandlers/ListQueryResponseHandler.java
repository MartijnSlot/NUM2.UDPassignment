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
public class ListQueryResponseHandler implements PacketHandler {

    @Override
    public void initiateHandler(UDPFileServer udpFileServer, InetAddress packetAddress, PacketSender apekop, byte[] data) {

        System.out.println("Received file list from PI");
        byte [] datafile = new byte[data.length - 12];
        System.arraycopy(data, 12,datafile,0,data.length - 12);
        Map<Integer, String> fileList = new HashMap<>();
        try {
            fileList = (Map<Integer, String>) MapToBytesAndBack.deserialize(datafile);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        udpFileServer.printFiles(fileList);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        apekop.setListQueryAck(true);
        udpFileServer.waitForInput();
    }
}
