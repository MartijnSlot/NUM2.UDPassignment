package com.nedap.university;

import com.nedap.university.application.FileSplitter;
import com.nedap.university.protocols.protocol1.Protocol1;
import com.nedap.university.udpFileServer.UDPFileServer;
import com.nedap.university.udpFileServer.dataHandlers.MapToBytesAndBack;
import com.nedap.university.udpFileServer.incomingPacketHandlers.DataPacketAckHandler;
import com.nedap.university.udpFileServer.incomingPacketHandlers.DataPacketHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author hendrik.vanderlinde
 */

public class HatsemeflatsTest {

    private FileSplitter fileSplitter;
    private UDPFileServer server;

    @Before public void setUp() {
        fileSplitter = new FileSplitter();
        server = new UDPFileServer("src/main/resources");
    }


    @Test
    public void testHats() {
        assertThat(1, is(1));
    }

    @Test
    public void testConverter() {
        Map<Integer, String> apekop = new HashMap<>();
        Map<Integer, String> paardehoofd = new HashMap<>();
        apekop.put(1,"hoi");
        apekop.put(2, "doei");
        System.out.println(apekop.toString());
        try {
            try {
                paardehoofd = (Map<Integer, String>) MapToBytesAndBack.deserialize(MapToBytesAndBack.serialize(apekop));
                System.out.println(paardehoofd.toString());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertThat(paardehoofd, is(apekop));
    }

    @Test
    public void intToBytesTest() {
        int id = 1;

        byte[] ret = new byte[4];
        ret[3] = (byte) (id & 0xFF);
        ret[2] = (byte) ((id >> 8) & 0xFF);
        ret[1] = (byte) ((id >> 16) & 0xFF);
        ret[0] = (byte) ((id >> 24) & 0xFF);


        int fileID = ByteBuffer.wrap(ret).getInt();
        assertThat(id, is(fileID));
    }


}