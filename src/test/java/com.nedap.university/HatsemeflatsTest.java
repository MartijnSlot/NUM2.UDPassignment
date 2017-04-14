package com.nedap.university;

import com.nedap.university.udpFileServer.UDPFileServer;
import com.nedap.university.udpFileServer.dataHandlers.MapToBytesAndBack;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author hendrik.vanderlinde
 */

public class HatsemeflatsTest {

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

}