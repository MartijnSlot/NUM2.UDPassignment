package com.nedap.university.application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class FileCompiler {

    private String fileName;
    private Map<Integer, byte[]> packetMap = new HashMap<>();

    public FileCompiler() {
    }

    public void glueAndSavePackets(String filePath) {
        byte[] fileContents = new byte[0];
        int packetPosition = 0;
        System.out.println("Gluing packets! ");

        if (packetMap.size() != 0) {
            for (int index = 1; index <= packetMap.keySet().size(); index++) {
                if (packetMap.get(index) != null) {
                    int datalen = packetMap.get(index).length;
                    fileContents = Arrays.copyOf(fileContents, packetPosition + datalen);
                    System.arraycopy(packetMap.get(index), 0, fileContents, packetPosition, datalen);
                    packetPosition = packetPosition + packetMap.get(index).length;
                }
            }

            if (Collections.max(packetMap.keySet()) == packetMap.size()) {
                setFileContents(fileContents, filePath);
            }
        }
    }

    /**
     * Writes the contents of the fileContents array to the specified file.
     * @param fileContents the contents to write
     */
    private void setFileContents(byte[] fileContents, String filePath) {
        String filename = fileName;
        File fileToWrite = new File(filePath + "/" + filename);
        try (BufferedOutputStream fileStream = new BufferedOutputStream(new FileOutputStream(fileToWrite))) {
            fileStream.write(fileContents);
            fileStream.flush();
            fileStream.close();
        } catch (Exception e) {
            System.out.println("Something went wrong with gluing filecontents.");
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPacketInPacketMap(int fileSeq, byte[] datafragment) {
        packetMap.put(fileSeq, datafragment);
    }

    public Map<Integer, byte[]> getPacketMap() {
        return packetMap;
    }
}
