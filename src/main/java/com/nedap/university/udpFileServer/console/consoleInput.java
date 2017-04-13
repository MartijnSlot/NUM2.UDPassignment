package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.UDPFileServer;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class consoleInput implements consoleCommands {

    public consoleInput() {

    }

    public Command input(String message, UDPFileServer server) {
        Command command;
        String[] splitMessage = message.split(DELIMITER);
        switch (splitMessage[0]) {
            case LS:
                command = new LSCommand(splitMessage[0], server);
                break;
            case LSPI:
                command = new LSPICommand(splitMessage[0], server);
                break;
            case UPLOAD:
                command = new UploadCommand(splitMessage[0], server);
                break;
            case DOWNLOAD:
                command = new DownloadCommand(splitMessage[0], server);
                break;
            default:
                command = new EmptyCommand(server);
                break;
        }
        return command;
    }
}
