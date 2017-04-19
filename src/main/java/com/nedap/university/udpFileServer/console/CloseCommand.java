package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.UDPFileServer;

/**
 * Created by martijn.slot on 19/04/2017.
 */
public class CloseCommand extends Command {

    public CloseCommand(UDPFileServer server) {
        super();
        this.server = server;
    }

    @Override
    public void execute() {
        server.closeThreads();
    }
}
