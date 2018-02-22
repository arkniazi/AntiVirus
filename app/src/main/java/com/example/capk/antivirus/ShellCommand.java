package com.example.capk.antivirus;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by capk on 2/20/18.
 */

public class ShellCommand {
//    unzip -p Name-of-apk.apk META-INF/CERT.RSA | keytool -printcert
public static String sudoForResult(String string) {
    String res = "";
    DataOutputStream outputStream = null;
    InputStream response = null;
    try{
        Process su = Runtime.getRuntime().exec("su");
        outputStream = new DataOutputStream(su.getOutputStream());
        response = su.getInputStream();

//        for (String s : strings) {
            outputStream.writeBytes(string+"\n");
            outputStream.flush();
//        }

        outputStream.writeBytes("exit\n");
        outputStream.flush();
        try {
            su.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        res = readFully(response);
    } catch (IOException e){
        e.printStackTrace();
    } finally {
//        Closer.closeSilently(outputStream, response);
    }
    return res;
}
    public static String readFully(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = is.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return baos.toString("UTF-8");
    }

}
