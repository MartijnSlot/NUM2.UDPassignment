package com.nedap.university.Runner;

import com.nedap.university.UDPFileServer.Server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    private static boolean keepAlive = true;
    private static boolean running = false;

    private Main() {}

    public static void main(String[] args) {
        running = true;
        System.out.println("Hello, Nedap University!");

        initShutdownHook();

        while (keepAlive) {
            try {
                Server server = new Server(InetAddress.getLocalHost(), 1234);
                server.start();
                Thread.sleep(5000);
            } catch (UnknownHostException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Stopped");
        running = false;
    }

    private static void initShutdownHook() {
        final Thread shutdownThread = new Thread() {
            @Override
            public void run() {
                keepAlive = false;
                while (running) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownThread);
    }
}
