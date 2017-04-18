package com.nedap.university.udpFileServer;

import com.nedap.university.application.FileSplitter;
import com.nedap.university.protocols.protocol1.Protocol1;
import com.nedap.university.udpFileServer.console.*;
import com.nedap.university.udpFileServer.incomingPacketHandlers.*;
import com.nedap.university.udpFileServer.dataHandlers.*;
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
public class UDPFileServer {

    private static final int PROTOCOLID = 1;
    private PacketReceiver packetReceiver;
    private PacketSender packetSender;
    private DatagramSocket zocket;
    public InetAddress externalhost;
    private Map<Byte, Flags> allPackets = new HashMap<>();
    private BufferedReader humanInput;
    private final String filePath;
    static final int PORT = 1234;
    final InetAddress localhost;
    Map<Integer, String> localFiles;
    private DataPacketAckHandler dataPacketAckHandler = new DataPacketAckHandler();
    private Protocol1 protocol;

    public UDPFileServer(String filePath) {
        this.filePath = filePath;
        localhost = getLocalAddress();

    }

    public void init() {
        try {
            zocket = new DatagramSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setUpAllPackets();
        localFiles = getFiles();

        humanInput = new BufferedReader(new InputStreamReader(System.in));

        packetReceiver = new PacketReceiver(this, zocket);
        packetSender = new PacketSender(this, zocket);
        packetSender.setMulticast(true);
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
        allPackets.put(Byte.parseByte("00000010", 2), FILE_QUERY); //int 2
        allPackets.put(Byte.parseByte("00100010", 2), FILE_QUERY_ACK); //int 34
        allPackets.put(Byte.parseByte("00000100", 2), FILE_LIST_QUERY); //int 4
        allPackets.put(Byte.parseByte("00100100", 2), FILE_LIST_QUERY_ACK); //int 36
        allPackets.put(Byte.parseByte("00001000", 2), DATA); //int 8
        allPackets.put(Byte.parseByte("00101000", 2), DATA_ACK); //int 40
        allPackets.put(Byte.parseByte("01101000", 2), DATA_ACK_FIN); //int 104
    }

    void handleReceivedPacket(byte[] packet, InetAddress packetAddress) {
        byte[] packetFlags = Arrays.copyOfRange(packet, 0, 1);
        byte[] data = Arrays.copyOfRange(packet, 1, packet.length);
        System.out.println("data length = " + data.length);

        for (byte i : allPackets.keySet()) {
            if (packetFlags[0] == i) {
                determinePacket(i, packetAddress, data);
            }
        }
    }

    private void determinePacket(byte i, InetAddress packetAddress, byte [] packet) {
        switch (allPackets.get(i)) {
            case MDNS:
                if (!localhost.equals(packetAddress)) {
                    packetHandler mDNShandler = new mDNSHandler();
                    mDNShandler.start(this, packetAddress, packetSender, packet);
                }
                break;
            case MDNS_ACK:
                if (externalhost != null) {
                    packetHandler mDNSAckHandler = new mDNSHandler();
                    mDNSAckHandler.start(this, packetAddress, packetSender, packet);
                }
                break;
            case FILE_QUERY:
                break;
            case FILE_QUERY_ACK:
                break;
            case FILE_LIST_QUERY:
                packetHandler fileListQueryHandler = new FileListQueryHandler();
                fileListQueryHandler.start(this, packetAddress, packetSender, packet);
                break;
            case FILE_LIST_QUERY_ACK:
                packetHandler listQueryResponseHandler = new ListQueryResponseHandler();
                listQueryResponseHandler.start(this, packetAddress, new PacketSender(this, zocket), packet);
                break;
            case DATA:
                packetHandler dataPacketHandler = new DataPacketHandler();
                dataPacketHandler.start(this, packetAddress, new PacketSender(this, zocket), packet);

                break;
            case DATA_ACK:
                dataPacketAckHandler.start(this, packetAddress, new PacketSender(this, zocket), packet);
                break;
            default:
                System.out.println("No flags have been set. Packet will be dropped.");
                break;
        }
    }

    private static InetAddress getLocalAddress() {
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements()) {
                for (InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if (f.getAddress().isSiteLocalAddress() && f.getAddress().getHostAddress().startsWith("192"))
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
            System.out.println("\nEnter one of the following commands: \n" +
                    "-- ls --                Retrieve local list of files \n" +
                    "-- ls-pi --             Send fileQuery to pi \n" +
                    "-- upload  'fileID' --  Upload file with fileID \n" +
                    "-- download 'fileID' -- Download file with fileID\n");
            PacketReceiver packetReceiver = new PacketReceiver(this, zocket);
            packetReceiver.start();
            fromPlayer = humanInput.readLine();
            if (fromPlayer != null) {
                ConsoleInput consoleInput = new ConsoleInput();
                Command command = consoleInput.input(fromPlayer, this, zocket);
                command.execute();
            }
        } catch (IOException e) {
            System.out.println("Unable to readline while waiting for input.");
        }
    }

    public void establishConnex() {
        System.out.println("Established connected!");
        closeThreads();
        waitForInput();


    }

    public Map<Integer, String> getFiles() {
        return GenerateFileListData.getFileMap(filePath);
    }

    public void printFiles(Map<Integer, String> fileMap) {
        System.out.println("\n===================\nIndex: filename");
        for (int index : fileMap.keySet()) {
            System.out.println(index + ":     " + fileMap.get(index));
        }
        System.out.println("===================\n");
    }

    public byte[] getMapBytes(Map<Integer, String> files) {
        try {
            return MapToBytesAndBack.serialize(files);
        } catch (IOException e) {
            return new byte[0];
        }
    }

    public DatagramSocket getSocket() {
        return zocket;
    }

    public String getLocalFile(int id) {
        return localFiles.get(id);
    }

    public void closeThreads() {
        packetSender.setMulticast(false);
        packetSender.setMulticastAck(false);
        packetSender.setFinishedSending(true);
        packetReceiver.setFinishedReceiving(true);
    }

    public void uploadFiles(int fileID) {
        String fileToSend = getLocalFile(fileID);
        printFiles(localFiles);
        System.out.println("Uploading file : " + filePath + "/" + fileToSend);
        runProtocol(fileID);
    }

    public String getFilePath() {
        return filePath;
    }

    public void runProtocol(int fileID) {

        protocol = new Protocol1(zocket, this, localFiles.get(fileID), dataPacketAckHandler);
        packetSender = new PacketSender(this, zocket, protocol);
        packetSender.setSendFile(true);
        packetSender.start();
        packetReceiver = new PacketReceiver(this, zocket);
        packetReceiver.start();
        protocol.initiateProtocol();
    }
}