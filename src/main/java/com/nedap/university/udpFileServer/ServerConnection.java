package com.nedap.university.udpFileServer;

import java.net.InetAddress;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class ServerConnection {

    private int port;
    private InetAddress inetAddress;

    public ServerConnection(int port, InetAddress inetAddress) {
        this.port = port;
        this.inetAddress = inetAddress;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }
}
