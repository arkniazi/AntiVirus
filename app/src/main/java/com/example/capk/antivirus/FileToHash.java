package com.example.capk.antivirus;

import android.util.*;
import android.util.Log;

import com.google.common.io.BaseEncoding;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    static long size = 0;
    public static String calculateSHA256(String apkName,String filename){


        try {
            MessageDigest md = MessageDigest.getInstance("SHA256");
            InputStream inputStream = get(apkName,filename);

            byte[] dataBytes = new byte[4096];
            int count;
            while ((count = inputStream.read(dataBytes)) >= 0) {
                md.update(dataBytes, 0, count);
            }
            inputStream.close();

            char[] charArray = SHA1.convertToHex(md.digest()).toCharArray();

            byte[] byteArray = Hex.decodeHex(charArray);

            byte[] mdbytes = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }
//            Log.d(TAG, "calculateSHA256: "+ sb.toString()+" size "+size);
            //convert the byte to hex format method 2

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<mdbytes.length;i++) {
                String hex=Integer.toHexString(0xff & mdbytes[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
//            Log.d(TAG, "calculateSHA256: "+ hexString.toString());

//            Log.d(TAG, "calculateSHA256 Base64: "+BaseEncoding.base64().encode(byteArray));
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
            size = zzz.getSize();

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
