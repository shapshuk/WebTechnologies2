package com.restraunt.shapshuk.utils;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;

class RegexTest {

    @Test
    void shouldVerifyPhoneNumber() {

        Pattern pattern2 = Pattern.compile("^\\+\\d{12}$");
        Matcher matcher = pattern2.matcher("+375297577197");
        assertTrue(matcher.matches());
    }
}
