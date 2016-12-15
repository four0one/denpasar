package com.chenwei.denpasar.util;

import java.io.*;

/**
 * Created by chenwei on 2016/12/8.
 */
public class SerializeUtil {

    public static byte[] serialize(Object object) {
        byte[] bytes = null;
        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baoStream);
            oos.writeObject(object);
            oos.flush();
            bytes = baoStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baoStream.close();
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public static Object deserialize(byte[] bytes) {
        Object object = null;
        ByteArrayInputStream baiStream = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(baiStream);
            object = ois.readObject();
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baiStream.close();
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

}
