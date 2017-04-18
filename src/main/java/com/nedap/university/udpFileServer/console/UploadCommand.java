package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.UDPFileServer;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class UploadCommand extends Command {

    private final String fileID;

    public UploadCommand(String fileID, UDPFileServer server) {
        super();
        this.fileID = fileID;
        this.server = server;
    }

    @Override
    public void execute() {
        int fileIntID = 0;
        try {
            fileIntID = Integer.parseInt(this.fileID);
        } catch (NumberFormatException e) {
            System.out.println("\nfileID: --" + this.fileID + "-- must be an integer");
            server.waitForInput();
        }

        server.uploadFiles(fileIntID);
        server.waitForInput();

    }
}
