package com.example.demo.util;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtil {
    public static String randomString(int length) {
        boolean useLetters = false;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
}
