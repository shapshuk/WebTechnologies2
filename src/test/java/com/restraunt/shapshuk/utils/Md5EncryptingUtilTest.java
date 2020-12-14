package com.restraunt.shapshuk.utils;

import com.restraunt.shapshuk.util.Md5EncryptingUtil;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.Assert.assertEquals;

class Md5EncryptingUtilTest {

    @Test
    void shouldEncryptPassword() throws NoSuchAlgorithmException {
        String hash = "FBD5FC9D0373D3BD981AB6E6905279DD";
        String password = "Qwerty3@123";
        String encryptResult = Md5EncryptingUtil.encrypt(password);

        System.out.println(hash);
        System.out.println(encryptResult);
        assertEquals(encryptResult, hash);
    }
}
