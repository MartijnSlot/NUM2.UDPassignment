package com.nedap.university.udpFileServer.dataHandlers;

import java.io.*;
import java.util.Map;

/**
 * Created by martijn.slot on 13/04/2017.
 */
public class MapToBytesAndBack {

    public static byte[] serialize(Map <Integer, String> files) throws IOException {
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(files);
            }
            return b.toByteArray();
        }
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream b = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                return o.readObject();
            }
        }
    }

}
