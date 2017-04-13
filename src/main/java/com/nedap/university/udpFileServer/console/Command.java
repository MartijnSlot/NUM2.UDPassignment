package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.UDPFileServer;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public abstract class Command {

    public String[] splitMessage;
    public UDPFileServer server;

    public abstract void execute();

}
