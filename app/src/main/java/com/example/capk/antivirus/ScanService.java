package com.example.capk.antivirus;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by capk on 4/12/18.
 */

public class ScanService extends IntentService {

    LinkedList<String> nameList = new LinkedList<>();
    LinkedList<String> hashList = new LinkedList<>();

    public ScanService(String name) {
        super(name);
    }
    public ScanService(){
        super("ScanService");

    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String scanType = intent.getDataString();
        if (scanType.equals("partial")){
            Log.d(TAG, "onHandleIntent: "+scanType);
        }
        partialScan();
    }

    public void partialScan(){
        PackageManager packageManager = getApplicationContext().getPackageManager();
        List<ApplicationInfo> pkgAppsList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        for (int a = 0;a<pkgAppsList.size()-1;a++) {
            ApplicationInfo applicationInfo = pkgAppsList.get(a);
            String packageName = applicationInfo.packageName;
            String[] permissions = GetFiles.getPermissions(packageManager,applicationInfo);

            ScanHelper helper = new ScanHelper();
            helper.getPathList(applicationInfo);

            String[] hashes =new String[hashList.size()];
            String hashSHA1 = "";

            boolean check = true;
            for (int i = 0; i < nameList.size(); i++) {
                hashSHA1 = FileToHash.calculateSHA256(applicationInfo.sourceDir, nameList.get(i));
                hashes[i] = hashSHA1;
                check = check && hashSHA1.equals(hashList.get(i));
                android.util.Log.d(TAG, "partialScan: "+hashSHA1);
            }

            PackageInfo pinfo = null;
            try {
                pinfo = packageManager.getPackageInfo(packageName, 0);
                int versionNumber = pinfo.versionCode;
                String versionName = pinfo.versionName;
//                   textView.setText("App Version Number:" +
//                           versionNumber + "\nApp Version Name: " + versionName + "\nClasses Hash matches: " + check);
            } catch (Exception e) {
                e.printStackTrace();
            }
            writePermissions(packageName,permissions,hashes);
            int percent = (int)((a * 100.0f) / pkgAppsList.size());
            updateProgress( percent);
        }

    }

    public void fullScan(){

    }

    public void updateProgress(int progress){
        Log.d(TAG, "updateProgress: "+progress);
        Intent intent = new Intent("DAGON_SCAN");
        intent.putExtra("progress",progress);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

    }

    private void writePermissions(String packageName,String[] data,String[] hashes){
        if (data!=null && hashes!=null){
            File file = new File(getApplicationContext().getFilesDir(),"Permissions.txt");
            FileOutputStream outputStream = null;
            try {
                outputStream = openFileOutput("Permissions.txt", Context.MODE_PRIVATE);
                outputStream.write(packageName.getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(TextUtils.join(",",data).getBytes());
                outputStream.write("\n".getBytes());
                outputStream.write(TextUtils.join(",",hashes).getBytes());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public class ScanHelper {

        public void getPathList(ApplicationInfo applicationInfo) {
            MyFileRead fileRead = new MyFileRead(getApplicationContext(), applicationInfo.sourceDir);
            fileRead.start();
            while (fileRead.isAlive()) {

            }
            LinkedList<String> list = fileRead.getList();
            String[] str;
            for (int i = 0; i < list.size(); i++) {

                if (i == 0 || i % 2 == 0) {
                    str = list.get(i).split(" ");
                    nameList.add(str[str.length - 1]);
                }
                if (i % 2 != 0) {
                    str = list.get(i).split(" ");
                    hashList.add(str[str.length - 1]);
                }
            }
        }}


}
