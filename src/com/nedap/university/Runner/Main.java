package com.nedap.university.Runner;

import com.nedap.university.UDPFileServer.UDPFileServer;

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
                UDPFileServer UDPFileServer = new UDPFileServer(InetAddress.getLocalHost(), 1234);
                UDPFileServer.start();
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
