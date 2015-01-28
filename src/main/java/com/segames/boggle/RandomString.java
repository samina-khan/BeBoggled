package com.segames.boggle;

import java.util.Random;

public class RandomString {

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = 'A'; ch <= 'Z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    private final Random random = new Random();

    private final String[] buf;

    public RandomString(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new String[length];
    }

    public String[] nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = String.valueOf(symbols[random.nextInt(symbols.length)]);
        return buf;
    }
}
