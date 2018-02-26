package com.example.capk.antivirus;

import android.util.*;
import android.util.Log;

import com.google.common.io.BaseEncoding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static android.content.ContentValues.TAG;

/**
 * Created by capk on 2/26/18.
 */

public class FileToHash {

    public static String calculateMD5(String filename){
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
        JarEntry jarEntry = null;
        while (jarEntryEnumeration.hasMoreElements()){
            jarEntry = jarEntryEnumeration.nextElement();
            if (jarEntry.getName().equals("classes.dex")){
                jarEntry = jarFile.getJarEntry(jarEntry.getName());
                break;
            }

        }
        InputStream is = null;
        try {
            is = jarFile.getInputStream(jarEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            android.util.Log.d(TAG, "Exception while getting digest", e);
            return null;
        }

        byte[] buffer = new byte[8192];
        int read;
        try {
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            byte[] md5sum = digest.digest();
            BigInteger bigInt = new BigInteger(1, md5sum);
            String output = bigInt.toString(16);
            // Fill to 32 chars
            output = String.format("%32s", output).replace(' ', '0');
            Log.d(TAG, "calculateMD5: "+output);

            byte[] byteArray = Hex.decodeHex(output.toCharArray());
            String sha1=  BaseEncoding.base64().encode(byteArray);
            Log.d(TAG, "calculateSHA1: "+sha1);
            return output;
        } catch (IOException e) {
            throw new RuntimeException("Unable to process file for MD5", e);
        } catch (DecoderException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                android.util.Log.d(TAG, "Exception on closing MD5 input stream", e);
            }
        }
        return null;
    }
}
