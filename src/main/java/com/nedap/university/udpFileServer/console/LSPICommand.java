package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.PacketSender;
import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.DatagramSocket;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class LSPICommand extends Command {
    public LSPICommand(UDPFileServer server, DatagramSocket socket) {
        super();
        this.server = server;
        this.socket = socket;
    }

    @Override
    public void execute() {
        PacketSender packetSender = new PacketSender(server, socket);
        packetSender.setSendListQuery(true);
        packetSender.setBytes("List request".getBytes());
        packetSender.start();
    }
}
