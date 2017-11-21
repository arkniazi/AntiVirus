package com.example.capk.antivirus;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by capk on 11/21/17.
 */

public class FileRead extends AsyncTask <Context,Void,String>{


    @Override
    protected String doInBackground(Context... contexts) {
        String res = "";
        try{
            File file = contexts[0].getFilesDir();
            InputStream inputStream = contexts[0].openFileInput("test");
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
        return res;
    }
}
