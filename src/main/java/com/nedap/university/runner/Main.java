package com.nedap.university.runner;

import com.nedap.university.udpFileServer.UDPFileServer;

public class Main {

    private static boolean keepAlive = true;
    private static boolean running = false;

    private Main() {}

    public static void main(String[] args) {
        running = true;
        String filePath;
        System.out.println("Hello, Nedap University!");

        if (args.length == 0) {
            filePath = "src/main/resources";
        } else {
            filePath = args[0];
        }
        System.out.println("Resources filepath: " + filePath);


        UDPFileServer UDPFileServer = new UDPFileServer(filePath);;
        UDPFileServer.init();

        initShutdownHook();

        while (keepAlive) {
            try {

                Thread.sleep(100);
            } catch (InterruptedException e) {
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
