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

import com.android.volley.Request;
import com.example.capk.antivirus.server.MyVolley;

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
        String posturl = "http://172.19.0.166:8081";
        String geturl = "http://172.19.0.166:8082/Data/result.txt";
        String geturlsvm = "http://172.19.0.166:8082/Data/svm-result.txt";
        List<ApplicationInfo> pkgAppsList;
        DBContract dbContract;
        String scanType;
//        CloudConnection cloudConnection;
        public ScanService(String name) {
            super(name);
        }
        public ScanService(){
            super("ScanService");

        }
        @Override
        protected void onHandleIntent(@Nullable Intent intent) {
            scanType = intent.getDataString();
                dbContract = new DBContract(getApplicationContext());
                dbContract.dropTable();
                scan();
        }

        public void scan(){
            PackageManager packageManager = getApplicationContext().getPackageManager();
            pkgAppsList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            if (scanType.equals("partial")){
                removeSystemApps();
            }
                ScanHelper helper = new ScanHelper();
            for (int a = 0;a<pkgAppsList.size();a++) {
                ApplicationInfo applicationInfo = pkgAppsList.get(a);
                String packageName = applicationInfo.packageName;
                String[] permissions = GetFiles.getPermissions(packageManager,applicationInfo);
                android.util.Log.d(TAG, "partialScan: "+pkgAppsList.size()+" Calling");
                helper.getPathList(applicationInfo);

                String[] hashes =new String[hashList.size()];
                String hashSHA1 = "";
                String hashSHA256 = "";

                boolean check = false;
                for (int i = 0; i < nameList.size(); i++) {
//                    hashSHA256 = FileToHash.calculateSHA(applicationInfo.sourceDir, nameList.get(i),"SHA-256");
                    hashSHA1 = FileToHash.calculateSHA(applicationInfo.sourceDir, nameList.get(i),"SHA1");
                    hashes[i] = hashSHA1;
                    check = check || hashSHA1.equals(hashList.get(i));

                }
                PackageInfo pinfo = null;
                try {
                    pinfo = packageManager.getPackageInfo(packageName, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                writePermissions(packageName,permissions,hashes);

                int percent = (int)(((a+1) * 100.0f) / pkgAppsList.size());
                updateProgress( percent);

                String dexCheck = "YES";
                if (!check){
                    dexCheck = "NO";
                }
                String data;
                if (permissions!=null){
                    data = "hashes="+TextUtils.join("\n",hashes)+"permissions="+TextUtils.join(",",permissions);
                }else{

                    data = "hashes="+TextUtils.join("\n",hashes);
                }
                android.util.Log.d(TAG, "partialScan: "+data);
                MyVolley myVolley = new MyVolley(getApplicationContext());
                myVolley.request(Request.Method.POST,posturl,packageName,data);
                myVolley.request(Request.Method.GET,geturl,packageName,data);
                myVolley.request(Request.Method.GET,geturlsvm,packageName,data);

                String scanStatus = "CLEAN";
                dbContract.insertData(packageName,dexCheck,pinfo.versionName, String.valueOf(pinfo.versionCode),scanStatus,"NORMAL");

                }
            }

        public void removeSystemApps(){
            for (int i=0;i<pkgAppsList.size();i++){
                if(isSystemPackage(pkgAppsList.get(i))|| getApplicationContext().getPackageName().equals(pkgAppsList.get(i).packageName)){
                    pkgAppsList.remove(i);
                    --i;
                }
            }
        }

        public void fullScan(){

        }

        public void updateProgress(int progress){
            Intent intent = new Intent("DAGON_SCAN");
            intent.putExtra("progress",progress);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        }

        private void writePermissions(String packageName,String[] data,String[] hashes){
            if (data!=null && hashes!=null){//                File file = new File(getApplicationContext().getFilesDir(),"Permissions.txt");

//                File file = new File(getApplicationContext().getFilesDir(),"Permissions.txt");
                FileOutputStream outputStream = null;
                try {
                    outputStream = openFileOutput("permission", Context.MODE_PRIVATE);
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

        private boolean isSystemPackage(ApplicationInfo applicationInfo) {
            return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
        }


        public class ScanHelper {
            public void getPathList(ApplicationInfo applicationInfo) {
                MyFileRead fileRead = new MyFileRead(getApplicationContext(), applicationInfo.sourceDir);
                fileRead.start();
                while (fileRead.isAlive()){

                }
                LinkedList<String> list = fileRead.getList();
                String[] str;
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0 || i % 2 == 0) {
                        str = list.get(i).split(" ");
                        android.util.Log.d(TAG, "getPathList: "+list.get(i));
                        nameList.add(str[str.length - 1]);
                    }
                    if (i % 2 != 0) {
                        if (list.get(i).contains("SHA1")){
                            str = list.get(i).split(" ");
                            hashList.add(str[str.length - 1]);
                        }
                        else {
                            nameList.removeLast();
                        }
                    }
                }
            }}


    }
