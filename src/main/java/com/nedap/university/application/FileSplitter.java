package com.nedap.university.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class FileSplitter {

    private static final int PACKETSIZE = 1400;

    public FileSplitter() {

    }

    /**
     * Gets the contents of the specified file.
     * @param fileName the file name
     * @return the array of integers, representing the contents of the file to transmit
     */
    public byte[] getFileContents(String fileName) {
        File fileToTransmit = new File(fileName);
        try (FileInputStream fileStream = new FileInputStream(fileToTransmit)) {
            System.out.println(fileToTransmit.toString());
            byte[] fileContents = new byte[(int) fileToTransmit.length()];

            try {
                fileStream.read(fileContents);
            } catch (IOException e) {
                e.printStackTrace();

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
    public byte[] createPacket(int i, byte[] fileContents) {

        byte[] pkt = null;
        int filePointer = PACKETSIZE * (i-1);
        int datalen = Math.min(PACKETSIZE, fileContents.length - filePointer);
        if (datalen > -1) {
            pkt = new byte[datalen];
            System.arraycopy(fileContents, filePointer, pkt, 0, datalen);
        }
        return pkt;
    }

    public int getPacketSize() {
        return PACKETSIZE;
    }

}
