package com.yue99520.tool.file.transferor.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Utils {

    public static String encode(String text) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        return new String(encoder.encode(textBytes), StandardCharsets.UTF_8);
    }

    public static String decode(String encoded) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encodedBytes = encoded.getBytes(StandardCharsets.UTF_8);
        return new String(decoder.decode(encodedBytes), StandardCharsets.UTF_8);
    }
}
