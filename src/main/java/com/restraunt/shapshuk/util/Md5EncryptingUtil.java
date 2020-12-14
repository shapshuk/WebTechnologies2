package com.restraunt.shapshuk.util;

import org.apache.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

public final class Md5EncryptingUtil {

    private static final Logger LOGGER = Logger.getLogger(Md5EncryptingUtil.class.getName());

    private Md5EncryptingUtil() {

    }

    public static String encrypt(String password) throws NoSuchAlgorithmException {

        String md5 = "MD5";
        MessageDigest messageDigest = MessageDigest.getInstance(md5);
        LOGGER.info(String.format("Got algorithm - [%s]", md5));

        messageDigest.reset();
        messageDigest.update(password.getBytes());
        byte[] digest = messageDigest.digest();

        return printHexBinary(digest).toUpperCase();
    }
}
