package com.nedap.university.application;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by martijn.slot on 07/04/2017.
 */
public class FileCompiler {

    public FileCompiler() {

    }

    /**
     * Writes the contents of the fileContents array to the specified file.
     * @param fileContents the contents to write
     */
    public static void setFileContents(Integer[] fileContents) {
        String filename = getFileName(fileContents);
        File fileToWrite = new File(filename);
        try (FileOutputStream fileStream = new FileOutputStream(fileToWrite)) {
            for (Integer fileContent : fileContents) {
                fileStream.write(fileContent);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
        }
    }

    private static String getFileName(Integer[] fileContents) {
        String filename = "hoi";
        return filename;
    }
}
