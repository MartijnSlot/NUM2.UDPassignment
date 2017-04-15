package com.nedap.university.application;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class FileSplitter {

    private static final int PACKETSIZE = 3000;
    private static final int HEADERSIZE = 13;


    public FileSplitter() {

    }

    /**
     * Gets the contents of the specified file.
     * @param fileName the file name
     * @return the array of integers, representing the contents of the file to transmit
     */
    public Integer[] getFileContents(String fileName) {
        File fileToTransmit = new File(fileName);
        try (FileInputStream fileStream = new FileInputStream(fileToTransmit)) {
            System.out.println(fileToTransmit.toString());
            Integer[] fileContents = new Integer[(int) fileToTransmit.length()];

            for (int i = 0; i < fileContents.length; i++) {
                int nextByte = fileStream.read();
                if (nextByte == -1) {
                    throw new Exception("File size is smaller than reported");
                }

                fileContents[i] = nextByte;
            }
            return fileContents;
        } catch (Exception e) {
            System.out.println("Something went wrong with getting the filecontents.");
            return null;
        }
    }

    /**
     * creates a packet
     * @param i the number of the packet (1 to numberofFragments)
     *@param fileContents total file contents  @return portion of file in Integer array
     */
    public Integer[] createPacket(int i, Integer[] fileContents) {

        Integer[] pkt = null;
        int filePointer = PACKETSIZE * (i-1);
        int datalen = Math.min(PACKETSIZE, fileContents.length - filePointer);
        if (datalen > -1) {
            pkt = new Integer[datalen];
            System.arraycopy(fileContents, filePointer, pkt, 0, datalen);
        }
        return pkt;
    }

    public int getPacketSize() {
        return PACKETSIZE;
    }
}
