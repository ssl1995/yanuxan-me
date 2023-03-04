package com.msb.im.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class DigestUtil {

    private DigestUtil() {

    }

    public static long md52Long(String str) {
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
        char[] chs = md5.toCharArray();
        List<Long> longList = new LinkedList<>();
        for (char ch : chs) {
            longList.add((long) ch);
        }
        return longList.stream().reduce(0L, Long::sum);
    }

}
