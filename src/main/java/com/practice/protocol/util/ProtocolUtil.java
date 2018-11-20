package com.practice.protocol.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class ProtocolUtil {
    public static final int FRAME_LENGTH_BLOCK = 8;
    public static final int CONTENT_TYPE_BLOCK = 2;
    public static final int HEADER_LENGTH = FRAME_LENGTH_BLOCK + CONTENT_TYPE_BLOCK;
    public static final byte[] CONTENT_TYPE_JSON = {0x00, 0x01};
    public static final byte[] CONTENT_TYPE_BIN = {0x00, 0x02};
    public static final byte[] CONTENT_TYPE_TIME = {0x00, 0x03};
    public static final byte[] TIME_FRAME = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x12, CONTENT_TYPE_TIME[0], CONTENT_TYPE_TIME[1], 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static byte[] convertLongToBytes(long value) {
        byte[] bytes = new byte[8];
        bytes[7] = (byte) value;
        bytes[6] = (byte) (value >> 8);
        bytes[5] = (byte) (value >> 16);
        bytes[4] = (byte) (value >> 24);
        bytes[3] = (byte) (value >> 32);
        bytes[2] = (byte) (value >> 40);
        bytes[1] = (byte) (value >> 48);
        bytes[0] = (byte) (value >> 56);
        return bytes;
    }

    public static long convertBytesToLong(byte[] bytes) {
        long value = 0;
        value = (value | (bytes[0] & 0xff)) << 8;
        value = (value | (bytes[1] & 0xff)) << 8;
        value = (value | (bytes[2] & 0xff)) << 8;
        value = (value | (bytes[3] & 0xff)) << 8;
        value = (value | (bytes[4] & 0xff)) << 8;
        value = (value | (bytes[5] & 0xff)) << 8;
        value = (value | (bytes[6] & 0xff)) << 8;
        value = (value | (bytes[7] & 0xff));
        return value;
    }


    public static byte[] makeJsonFrame(String json) {
        try {
            byte[] jsonBytes = json.getBytes("UTF-8");
            int frameLength = HEADER_LENGTH + jsonBytes.length;
            byte[] frame = new byte[frameLength];
            byte[] frameLengthBytes = convertLongToBytes(frameLength);
            System.arraycopy(frameLengthBytes, 0, frame, 0, frameLengthBytes.length);
            System.arraycopy(CONTENT_TYPE_JSON, 0, frame, FRAME_LENGTH_BLOCK, CONTENT_TYPE_JSON.length);
            System.arraycopy(jsonBytes, 0, frame, HEADER_LENGTH, jsonBytes.length);
            return frame;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static byte[] makeTimeFrame() {
        try {
            byte[] frame = new byte[HEADER_LENGTH + 8];
            System.arraycopy(TIME_FRAME, 0, frame, 0, frame.length);
            long time = System.currentTimeMillis();
            System.out.println("time" + time);
            System.arraycopy(convertLongToBytes(time), 0, frame, HEADER_LENGTH, 8);
            return frame;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public static void writeBinFrame(OutputStream output, File file) {
        FileInputStream fileInputStream = null;
        try {
            long fileLength = file.length();
            long frameLength = HEADER_LENGTH + fileLength;
            byte[] frameLengthBytes = convertLongToBytes(frameLength);
            output.write(frameLengthBytes);
            output.write(CONTENT_TYPE_BIN);
            fileInputStream = new FileInputStream(file);
            byte[] fileReadBuffer = new byte[1024];
            int fileReadNum = 0;
            while (true) {
                fileReadNum = fileInputStream.read(fileReadBuffer);
                if (fileReadNum == -1) break;
                output.write(fileReadBuffer, 0, fileReadNum);
            }
            fileInputStream.close();
        } catch (Exception e) {
            try {
                fileInputStream.close();
            } catch (Exception ex) {

            }
        }
    }
}
