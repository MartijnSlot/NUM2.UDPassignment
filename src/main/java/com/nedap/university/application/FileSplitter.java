package com.nedap.university.application;

import com.nedap.university.udpFileServer.UDPFileServer;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by martijn.slot on 10/04/2017.
 */
public class FileSplitter {

    public FileSplitter() {

    }

    private int windowSize;
    private byte[] data;

    /**
     * Gets the contents of the specified file.
     * @param id the file ID
     * @return the array of integers, representing the contents of the file to transmit
     */
    public Integer[] getFileContents(int id) {
        UDPFileServer.getLocalFile(id);
        File fileToTransmit = new File(String.format("rdtcInput%d.png", id));
        try (FileInputStream fileStream = new FileInputStream(fileToTransmit)) {
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
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            return null;
        }
    }

    public int getWindowSize() {
        return 10;
//                data.length + UDPFileServer.getHeaderLength();
    }
}
