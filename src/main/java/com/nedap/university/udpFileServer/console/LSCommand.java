package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.UDPFileServer;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class LSCommand extends Command {

    public LSCommand(UDPFileServer server) {
        super();
        this.server = server;
    }

    @Override
    public void execute() {
        server.printFiles(server.getFiles());
        server.waitForInput();
    }
}
