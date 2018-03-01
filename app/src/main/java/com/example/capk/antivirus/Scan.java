package com.example.capk.antivirus;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by CAPK on 1/29/2018.
 */


public class Scan extends android.support.v4.app.Fragment {
    String apkName;
    EditText name;
    PackageManager packageManager;
    TextView textView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_scan, container, false);
        name = rootView.findViewById(R.id.editText2);
        packageManager = getContext().getPackageManager();
        textView = rootView.findViewById(R.id.textView2);
        Button scan = rootView.findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override public void onClick(View view) {

//              Read list of installed packages
                String apkName = String.valueOf(name.getText());
                Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

                List<ApplicationInfo> pkgAppsList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

                for (ApplicationInfo applicationInfo : pkgAppsList){
                    String packageName =applicationInfo.packageName;

                    String name = (String) packageManager.getApplicationLabel(applicationInfo);

                    if(name.equals(apkName) && isStoragePermissionGranted()){
                        String hashSHA1 = "";
                        String hashSHA1Byte = "";
                        MyFileRead fileRead = new MyFileRead(getContext(),applicationInfo.sourceDir);
                        fileRead.start();
                        while (fileRead.isAlive()){
                        }
                        String data = FileToHash.calculateSHA1(applicationInfo.sourceDir);
                        byte[] dataBytes = fileRead.getByte();
                        StringBuilder fileData = fileRead.getRes();
                        PackageInfo pinfo = null;
                        try {
                            pinfo = packageManager.getPackageInfo(packageName, 0);
                            int versionNumber = pinfo.versionCode;

                            String versionName = pinfo.versionName;

                            textView.setText("SHA1: "+data+"\nSHA1 Bytes: "+hashSHA1Byte
                            +"\nVersion Number:"+versionNumber+"\nVersion Name: "+versionName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
            }
        });


        return rootView;
    }



    // answer from stack_over_flow  https://stackoverflow.com/questions/2695746/how-to-get-a-list-of-installed-android-applications-and-pick-one-to-ru
//    private boolean isSystemPackage(PackageInfo pkgInfo) {
//        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
//                : false;
//    }
    public boolean isStoragePermissionGranted(){
        if (Build.VERSION.SDK_INT>=23){
            if (ContextCompat.checkSelfPermission(getContext(),permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "isStoragePermissionGranted: True");
                return true;
            }
            else {
                Log.d(TAG, "isStoragePermissionGranted: False");
                ActivityCompat.requestPermissions(getActivity(),new String[]{permission.READ_EXTERNAL_STORAGE},1);
                return false;
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }

    }


}
