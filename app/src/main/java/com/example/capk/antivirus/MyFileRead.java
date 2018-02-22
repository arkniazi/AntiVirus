package com.example.capk.antivirus;

import android.content.Context;
import android.os.AsyncTask;
import android.util.*;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by capk on 11/21/17.
 */
// Need to modify this class ... Use raw Thread instead of AsyncTask
public class MyFileRead extends Thread {
    String filename;
    String res = "";
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
                if (jarEntry.getName().equals("AndroidManifest.xml")){
                    jarEntry = jarFile.getJarEntry(jarEntry.getName());
                    break;
                }

            }
            InputStream inputStream = jarFile.getInputStream(jarEntry);

//            InputStream inputStream = getClass().getResourceAsStream(filename+"/classes.dex");//new FileInputStream(new File(filename+"/classes.dex"));

//            Log.d("filename ", "file "+filename);
            if (inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String result = "";
                StringBuilder stringBuilder = new StringBuilder();
//                    Log.d("Reading ", "run: Reading");
                while((result = bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(result);
                }
                inputStream.close();
                res = stringBuilder.toString();

            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public String getRes() {
        return res;
    }
}