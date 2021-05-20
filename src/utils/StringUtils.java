package utils;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Arrays;
import java.util.Random;


public class StringUtils {
    private final static Random random = new Random();

    public static long hash(String str) {
        byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] thedigest = md.digest(bytesOfMessage);
        byte[] bytes = Arrays.copyOfRange(thedigest, 0, 8);
        return bytesToLong(bytes);
    }

    public static String generateRandomString(int length) {
        byte[] array = new byte[length];
        random.nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    private static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
