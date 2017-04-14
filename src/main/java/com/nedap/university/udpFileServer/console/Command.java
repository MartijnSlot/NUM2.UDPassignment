package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.DatagramSocket;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public abstract class Command {

    public String[] splitMessage;
    public UDPFileServer server;
    public DatagramSocket socket;

    public abstract void execute();

}
