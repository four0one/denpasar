package com.plumeria.denpasar.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by chenwei on 2016/9/1.
 */
public class ShortUrlGenerator {

    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h",
            "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"

    };

    public static String key() {
        int length = chars.length;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 4; i++) {
            int r = random.nextInt(length);
            sb.append(chars[r]);
        }
        return sb.toString();
    }

    public static String[] shortUrl(String url) {
        String key = key();

        String hex = md5(key + url);

        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                long index = 0x0000003D & lHexLong;
                outChars += chars[(int) index];
                lHexLong = lHexLong >> 5;
            }
            resUrl[i] = outChars;
        }
        return resUrl;
    }

    public static String md5(String src) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        byte[] b = src.getBytes();
        messageDigest.reset();
        messageDigest.update(b);
        byte[] hash = messageDigest.digest();
        String hs = "";
        String stmp = "";
        for (int i = 0; i < hash.length; i++) {
            stmp = Integer.toHexString(hash[i] & 0xFF);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

}
