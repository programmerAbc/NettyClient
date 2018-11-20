package com.practice.protocol.util;

public class ByteUtil {
    public static final String[] alphabet = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    public static String convertBytesToHex(byte[] bytes) {
        return convertBytesToHex(bytes, " ");
    }

    public static String convertBytesToHex(byte[] bytes, String separator) {
        StringBuilder builder = new StringBuilder();
        byte b = bytes[0];
        builder.append(alphabet[(b & 0xf0) >> 4])
                .append(alphabet[(b & 0x0f)]);
        for (int i = 1; i < bytes.length; ++i) {
            b = bytes[i];
            builder.append(separator)
                    .append(alphabet[(b & 0xf0) >> 4])
                    .append(alphabet[(b & 0x0f)]);
        }
        return builder.toString();
    }


}
