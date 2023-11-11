package com.poly.elnr.utils;

import java.util.regex.Pattern;

public class RegexUtils {

    public static boolean isPhoneNumber(String input) {
        String phoneNumberPattern = "^(\\+\\d{1,})?\\d{9,}$";
        return Pattern.matches(phoneNumberPattern, input);
    }

    public static boolean isEmail(String input) {
        String gmailPattern = "^(.+)@(\\S+)$";
        return Pattern.matches(gmailPattern, input);
    }

}
