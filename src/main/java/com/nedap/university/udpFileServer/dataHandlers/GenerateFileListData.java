package com.nedap.university.udpFileServer.dataHandlers;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martijn.slot on 14/04/2017.
 */
public class GenerateFileListData {


    public Map<Integer, String> getFileMap(String filePath) {
        File folder = new File(filePath);
        File[] listOfFiles = folder.listFiles();
        Map<Integer, String> fileMap = new HashMap<>();

        if (listOfFiles != null) {
            int fileIndex = 1;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileMap.put(fileIndex, file.getName());
                    fileIndex++;
                }
            }
        }
        return fileMap;
    }
}
