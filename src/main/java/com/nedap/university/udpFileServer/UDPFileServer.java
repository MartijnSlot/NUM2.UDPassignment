package com.nedap.university.udpFileServer;

import com.nedap.university.udpFileServer.packetHandlers.*;
import static com.nedap.university.udpFileServer.Flags.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;




/**
 * Created by martijn.slot on 10/04/2017.
 */
public class UDPFileServer{


    private PacketReceiver packetReceiver;
    private PacketSender packetSender;
    private DatagramSocket zocket;
    final InetAddress localhost;
    static final int PORT = 1234;
    private static final int HEADER_LENGTH = 13;
    public InetAddress externalhost;
    private Map<Byte, Flags> allPackets = new HashMap<>();
    private BufferedReader humanInput;


    public UDPFileServer() {
        localhost = getLocalAddress();

    }

    public static int getHeaderLength() {
        return HEADER_LENGTH;
    }

    public void init() {
        try {
            zocket = new DatagramSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpAllPackets();

        humanInput = new BufferedReader(new InputStreamReader(System.in));

        packetReceiver = new PacketReceiver(this, zocket);
        packetSender = new PacketSender(this, zocket);
        packetReceiver.start();
        packetSender.start();

        try {
            packetReceiver.join();
            System.out.println("packetReceiver joined");
            packetSender.join();
            System.out.println("PacketSender joined");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("uit de while loop");
    }

    private void setUpAllPackets() {
        allPackets.put(Byte.parseByte("00000001", 2), MDNS); //int 1
        allPackets.put(Byte.parseByte("00100001", 2), MDNS_ACK); //int 33
        allPackets.put(Byte.parseByte("01100001", 2), MDNS_ACK_FIN); //int 33
        allPackets.put(Byte.parseByte("00000010", 2), FILECONTENTQUERY); //int 2
        allPackets.put(Byte.parseByte("00100010", 2), FILECONTENTQUERY_ACK); //int 34
        allPackets.put(Byte.parseByte("00000100", 2), FILEQUERY); //int 4
        allPackets.put(Byte.parseByte("00100100", 2), FILEQUERY_ACK); //int 36
        allPackets.put(Byte.parseByte("00001000", 2), DATA); //int 8
        allPackets.put(Byte.parseByte("00101000", 2), DATA_ACK); //int 40
        allPackets.put(Byte.parseByte("01101000", 2), DATA_ACK_FIN); //int 104
    }

    void handleReceivedPacket(byte[] packet, InetAddress packetAddress) {
        byte[] packetFlags = Arrays.copyOfRange(packet, 0, 1);

        for (byte i : allPackets.keySet()){
            if (packetFlags[0] == i) {
                determinePacket(i, packetAddress);
            }
        }
    }

    private void determinePacket(byte i, InetAddress packetAddress) {
        switch (allPackets.get(i)) {
            case MDNS:
                if (!localhost.equals(packetAddress)) {
                    packetSender.setMulticastAck(true);
                    packetHandler mDNShandler = new mDNSHandler();
                    mDNShandler.start(this, packetAddress);
                }
                break;
            case MDNS_ACK:
                if (externalhost != null) {
                    packetSender.setMulticast(false);
                    packetHandler mDNSAckHandler = new mDNSHandler();
                    mDNSAckHandler.start(this, packetAddress);
                }
                break;
            case FILECONTENTQUERY:

                break;
            case FILECONTENTQUERY_ACK:

                break;
            case FILEQUERY:

                break;
            case FILEQUERY_ACK:

                break;
            case DATA:

                break;
            case DATA_ACK:

                break;
            case DATA_ACK_FIN:

                break;
            default:
                System.out.println("No flags have been set. Packet will be dropped.");
                break;
        }
    }

    private static InetAddress getLocalAddress(){
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if ( f.getAddress().isSiteLocalAddress() && f.getAddress().getHostAddress().startsWith("192"))
                        return f.getAddress();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void waitForInput() {
        String fromPlayer;
        try {
            System.out.println("\nEnter the following commands: \n-- ls -- Retreive local list of files \n-- ls -pi --  Send fileQuery to pi \n" +
                    "-- upload  'fileID' -- Upload file with fileID \n-- download 'fileID' -- Download file with fileID\n");
            fromPlayer = humanInput.readLine();
            if (fromPlayer != null) {
                if (fromPlayer.startsWith("ls")) {
                    System.out.println("Sending file query request");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void establishConnex() {
        packetSender.setMulticastAck(false);
        waitForInput();
    }
}