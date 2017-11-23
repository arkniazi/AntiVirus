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

public class FileRead extends AsyncTask <Context,Void,String>{

    @Override
    protected String doInBackground(Context... contexts) {
        String res = "";
        try{
            File file = contexts[0].getFilesDir();
            InputStream inputStream = contexts[0].openFileInput("I:\\hello1.txt");
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

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
