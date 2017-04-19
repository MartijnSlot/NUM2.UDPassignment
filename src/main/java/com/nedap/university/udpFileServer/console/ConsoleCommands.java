package com.nedap.university.udpFileServer.console;

/**
 * Created by martijn.slot on 13/04/2017.
 */

public interface ConsoleCommands {
    /**
     * ls
     *
     * retrieve local file structure
     */
    String LS = "ls";

    /**
     * ls-pi
     *
     * retrieve pi file structure
     */
    String LSPI = "ls-pi";

    /**
     * upload filename
     *
     * upload file from local to pi
     */
    String UPLOAD = "upload";

    /**
     * download filename
     *
     * download file from pi to local
     */
    String DOWNLOAD = "download";

    /**
     * Delimiter for splitting input
     */
    String DELIMITER = " ";

    /**
     * for closing threads
     */
    String CLOSE = "close";
}
