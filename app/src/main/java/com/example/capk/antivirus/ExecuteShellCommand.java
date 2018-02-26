package com.example.capk.antivirus;

import android.util.*;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by capk on 2/19/18.
 */
// blog link http://muzikant-android.blogspot.com/2011/02/how-to-get-root-access-and-execute.html
public abstract class ExecuteShellCommand
{

    public static void runShell(String command){

        try {
            Runtime.getRuntime().exec("adb shell");
//            Runtime.getRuntime().exec("su");
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer stringBuffer = new StringBuffer();
            while ((read = bufferedReader.read(buffer))>0){
                stringBuffer.append(buffer,0,read);

            }
            bufferedReader.close();
            process.waitFor();
            android.util.Log.d("Certificate ", "runShell: "+stringBuffer.toString());
//            Runtime.getRuntime().exec("exit");

        } catch (IOException e) {;
            e.printStackTrace();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}