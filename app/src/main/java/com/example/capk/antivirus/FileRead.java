package com.example.capk.antivirus;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by capk on 11/21/17.
 */
// Need to modify this class ... Use raw Thread instead of AsyncTask
public class FileRead extends Thread {
    String filename;
    String res = "";
    Context context;
    FileRead(Context context,String file){
        this.context = context;
        filename = file;
    }

    @Override
    public void run() {
        super.run();
        try{
            File file = context.getFilesDir();
            InputStream inputStream = context.openFileInput(filename);
            if (inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String result = "";
                StringBuilder stringBuilder = new StringBuilder();
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