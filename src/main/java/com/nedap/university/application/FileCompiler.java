package com.nedap.university.application;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class FileCompiler {

    private String fileName;
    private Map<Integer, Integer[]> packetMap = new HashMap<>();

    public FileCompiler() {
    }

    public void glueAndSavePackets(String filePath) {
        Integer[] fileContents = new Integer[0];
        int newPacketPosition = 0;

        if (packetMap.size() != 0) {
            for (int index : packetMap.keySet()) {
                int datalen = packetMap.get(index).length;
                System.out.println("joepi");
                fileContents = Arrays.copyOf(fileContents, newPacketPosition + datalen);
                System.arraycopy(packetMap.get(index), 0, fileContents, newPacketPosition, datalen);
                newPacketPosition = newPacketPosition + packetMap.get(index).length;
            }

            setFileContents(fileContents, filePath);
        }
    }

    /**
     * Writes the contents of the fileContents array to the specified file.
     * @param fileContents the contents to write
     */
    public void setFileContents(Integer[] fileContents, String filePath) {
        String filename = fileName;
        File fileToWrite = new File(filePath + "/" + filename);
        try (FileOutputStream fileStream = new FileOutputStream(fileToWrite)) {
            for (Integer fileContent : fileContents) {
                fileStream.write(fileContent);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong with gluing filecontents.");
        }
    }

    private String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPacketInPacketMap(int fileSeq, Integer[] datafragment) {
        packetMap.put(fileSeq, datafragment);
    }

    public Map<Integer, Integer[]> getPacketMap() {
        return packetMap;
    }
}
