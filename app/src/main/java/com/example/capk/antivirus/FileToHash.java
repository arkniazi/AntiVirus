package com.example.capk.antivirus;

import android.util.Log;

import com.google.common.io.BaseEncoding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static android.content.ContentValues.TAG;

/**
 * Created by capk on 2/26/18.
 */

public class FileToHash {

    public static String calculateSHA256(String apkName,String filename){


        try {
            MessageDigest md = MessageDigest.getInstance("SHA256");
            InputStream inputStream = get(apkName,filename);
            if (inputStream==null){
                return "";
            }
            byte[] dataBytes = new byte[4096];
            int count;
            while ((count = inputStream.read(dataBytes)) >= 0) {
                md.update(dataBytes, 0, count);
            }
            inputStream.close();
            char[] charArray = SHA.convertToHex(md.digest()).toCharArray();
            byte[] byteArray = Hex.decodeHex(charArray);

            byte[] mdbytes = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<mdbytes.length;i++) {
                String hex=Integer.toHexString(0xff & mdbytes[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            return BaseEncoding.base64().encode(byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (DecoderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream get(String apkPath,String filename){

        JarFile containerJar = null;

        try {
            Log.d(TAG, "get: "+apkPath);
            Log.d(TAG, "get: "+filename);
            containerJar = new JarFile(apkPath);

            ZipEntry zzz = containerJar.getEntry(filename);
            if (zzz != null) {

                InputStream in = containerJar.getInputStream(zzz);

                return in;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
