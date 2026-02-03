package com.mass.sdk.example;

import java.security.SecureRandom;

public class RandomHelper {
    public static final String UPPER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String LETTERS = UPPER_LETTERS + LOWER_LETTERS;
    public static final String NUMBERS = "0123456789";
    public static final String DEFAULT = LETTERS + NUMBERS;
    public static final String UPPER_HEX = "0123456789ABCDEF";
    public static final String LOWER_HEX = "0123456789abcdef";

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String getString(int length) {
        return getString(length, DEFAULT);
    }

    public static String getString(int length, String chars) {
        if (length <= 0) return "";
        if (chars == null || chars.isEmpty()) {
            chars = DEFAULT;
        }

        StringBuilder sb = new StringBuilder(length);
        int charsLength = chars.length();

        for (int i = 0; i < length; i++) {
            int randomIndex = RANDOM.nextInt(charsLength);
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static String getMacAddress() {
        byte[] macBytes = new byte[6];
        RANDOM.nextBytes(macBytes);
        macBytes[0] = (byte)(macBytes[0] | 0x02);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < macBytes.length; i++) {
            if (i > 0) sb.append(":");
            sb.append(String.format("%02X", macBytes[i]));
        }
        return sb.toString();
    }

    public static String getHex(int length) {
        byte[] buffer = new byte[length];
        RANDOM.nextBytes(buffer);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < buffer.length; i++) {
            if (i > 0) sb.append("-");
            sb.append(String.format("%02X", buffer[i]));
        }
        return sb.toString();
    }
}