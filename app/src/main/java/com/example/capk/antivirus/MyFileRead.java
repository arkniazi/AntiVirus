package com.example.capk.antivirus;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static android.content.ContentValues.TAG;

/**
 * Created by capk on 11/21/17.
 */
// Need to modify this class ... Use raw Thread instead of AsyncTask
public class MyFileRead extends Thread {
    String filename;
    LinkedList<String> linkedList = new LinkedList<>();
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
//                Log.d(TAG, "run: "+jarEntry.getName());
                if (jarEntry.getName().equals("MANIFEST.MF")){
                    jarEntry = jarFile.getJarEntry(jarEntry.getName());
                    break;
                }

            }
            InputStream inputStream = jarFile.getInputStream(jarEntry);

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String result;
            String hashResult;
            String result1;
            String hashResult1;
            String result2;
            String hashResult2;
            //need to improve for old apps e.g who don't use sha256
            while ((result = bufferedReader.readLine()) != null) {
                if (result.contains("Name: classes.dex")){
                    Log.d(TAG, "run: inside");
                    hashResult = bufferedReader.readLine();
                    bufferedReader.readLine();
                    result1 = bufferedReader.readLine();
                    hashResult1 = bufferedReader.readLine();
                    bufferedReader.readLine();

                    result2 = bufferedReader.readLine();
                    hashResult2 = bufferedReader.readLine();
                    if (result1.contains("classes") && result2.contains("classes") ){
                        linkedList.add(result);
                        linkedList.add(hashResult);
                        linkedList.add(result1);
                        linkedList.add(hashResult1);
                        linkedList.add(result2);
                        linkedList.add(hashResult2);
                        break;

                    }else {
                        linkedList.add(result);
                        linkedList.add(hashResult);

                    }
                }
            }
            bufferedReader.close();
            inputStream.close();
            jarFile.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }

    public LinkedList<String> getList(){
            return linkedList;
    }
}
