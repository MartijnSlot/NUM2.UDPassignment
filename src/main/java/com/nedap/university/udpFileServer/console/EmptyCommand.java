package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.UDPFileServer;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class EmptyCommand extends Command {

    public EmptyCommand(UDPFileServer server) {
        super();
        this.server = server;
    }

    @Override
    public void execute() {
        System.out.println("Not an option, try again");
        server.waitForInput();
    }
}
