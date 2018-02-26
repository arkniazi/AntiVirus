package com.example.capk.antivirus;

import android.content.Context;
import android.util.*;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static android.content.ContentValues.TAG;

/**
 * Created by capk on 11/21/17.
 */
// Need to modify this class ... Use raw Thread instead of AsyncTask
public class MyFileRead extends Thread {
    String filename;
    String res = "";
    byte[] bytes = null;
    Context context;
    MyFileRead(Context context, String file){
        this.context = context;
        filename = file;
    }

    @Override
    public void run() {
        super.run();
        try{
            //Reading apk file contents
            JarFile jarFile = new JarFile(filename);
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            JarEntry jarEntry = null;
            while (jarEntryEnumeration.hasMoreElements()){
            jarEntry = jarEntryEnumeration.nextElement();
                if (jarEntry.getName().equals("classes.dex")){
                    jarEntry = jarFile.getJarEntry(jarEntry.getName());
                    break;
                }

            }
            InputStream inputStream = jarFile.getInputStream(jarEntry);

            byte[] resultBytes = new  byte[(int) jarEntry.getSize()];
            Log.d(TAG, "run: "+(int)jarEntry.getSize());
            if (inputStream!=null){

                DataInputStream dis = new DataInputStream(inputStream);
                dis.readFully(resultBytes);
//                inputStream.read(resultBytes);
                bytes = resultBytes;
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String result = "";
                    StringBuilder stringBuilder = new StringBuilder();
//                    Log.d("Reading ", "run: Reading");
                    while ((result = bufferedReader.readLine()) != null) {
                        stringBuilder.append(result+"\n");

                    }
                    inputStream.close();
                    res = stringBuilder.toString();
                Log.d(TAG, "run: "+res);
                }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public byte[] getByte(){
        return bytes;
    }

    public String getRes() {
        return res;
    }
}

//                BufferedInputStream bufferedReader = new BufferedInputStream(inputStream);
//                int size = (int) jarEntry.getSize();
//                bytes = new byte[size];
//                bufferedReader.read(bytes,0,bytes.length);

//                byte[] buffer = new byte[8192];
//                int len = inputStream.read(buffer);
//
//                while (len != -1) {
//                    messageDigest.update(buffer, 0, len);
//                    len = inputStream.read(buffer);
//                }
//                Log.d(TAG, "run: "+convertToHex(messageDigest.digest()));