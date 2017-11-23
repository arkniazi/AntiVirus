package com.example.capk.antivirus;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by cracker on 11/22/2017.
 */

public class MD5 {

    public String getMD5 (String input) {

        String hashtext = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] strByte = messageDigest.digest(input.getBytes());
            BigInteger number = new BigInteger(1, strByte);
            hashtext = number.toString();
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;

            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hashtext;
    }
}
