package com.nedap.university.Runner;

import com.nedap.university.UDPFileServer.Server;

import java.io.IOException;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class Runner {

    public static int serverPort;
    public static String hostName;

    public static void main(String[] args) throws IOException {
        try {

            hostName = args[0];
            serverPort = Integer.parseInt(args[1]);

        } catch (NumberFormatException e) {
            System.out.println("wrong input. correct input has 2 arguments divided by a space: ipadress port \n"
                    + "For starting own server, enter '0.0.0.0' as ipaddress.");
        }
        Server server = new Server(hostName, serverPort);
        server.start();
    }

}
