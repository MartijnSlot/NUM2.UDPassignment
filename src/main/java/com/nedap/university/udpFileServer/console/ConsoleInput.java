package com.nedap.university.udpFileServer.console;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.net.DatagramSocket;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class ConsoleInput implements ConsoleCommands {

    public ConsoleInput() {

    }

    public Command input(String message, UDPFileServer server, DatagramSocket socket) {
        Command command;
        String[] splitMessage = message.split(DELIMITER);
        switch (splitMessage[0]) {
            case LS:
                command = new LSCommand(server);
                break;
            case LSPI:
                command = new LSPICommand(server, socket);
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
