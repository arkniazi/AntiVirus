package com.example.capk.antivirus;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static android.content.ContentValues.TAG;

/**
 * Created by capk on 11/21/17.
 */
// Need to modify this class ... Use raw Thread instead of AsyncTask
public class MyFileRead extends Thread {
    String filename;
    StringBuilder res;
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
                Log.d(TAG, "run: "+jarEntry.getName());
                if (jarEntry.getName().equals("MANIFEST.MF")){
                    jarEntry = jarFile.getJarEntry(jarEntry.getName());
                    break;
                }

            }
            InputStream inputStream = jarFile.getInputStream(jarEntry);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                    String result = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    Log.d("Reading ", "run: Reading");
                    while ((result = bufferedReader.readLine()) != null) {
//                        Log.d(TAG, "run: "+result);
                        stringBuilder.append(result+"\n");

                    }
                    inputStream.close();
                    res = stringBuilder;
//                Log.d(TAG, "run: "+res);
                } catch (IOException e1) {
            e1.printStackTrace();
        }


    }
        public byte[] getByte(){
        return bytes;
    }

    public StringBuilder getRes() {
        return res;
    }
}
