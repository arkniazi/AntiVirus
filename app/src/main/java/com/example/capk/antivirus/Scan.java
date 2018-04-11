package com.example.capk.antivirus;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by CAPK on 1/29/2018.
 */


public class Scan extends android.support.v4.app.Fragment {
    String apkName;
    LinkedList<String> nameList = new LinkedList<>();
    LinkedList<String> hashList = new LinkedList<>();
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


                apkName = String.valueOf(name.getText());

                CheckInstaller checkInstaller = new CheckInstaller(getContext());

                List<ApplicationInfo> pkgAppsList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

                for (ApplicationInfo applicationInfo : pkgAppsList){
                    String packageName =applicationInfo.packageName;
                    String name = (String) packageManager.getApplicationLabel(applicationInfo);

                    // Retrieve Permissions
//                    try {
//                      String []permissions = APKPermissions.getPermissions(packageManager.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS));
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }


                    if(name.equals(apkName) && isStoragePermissionGranted()){
                    boolean installerVerified  = checkInstaller.isLegal(packageName);
                        getPathList(applicationInfo);
                        String hashSHA1 = "";
                        boolean check = true;
                        Log.d(TAG, "onClick: "+nameList.size());
                        for (int i=0;i<nameList.size();i++){
//                            Log.d(TAG, "onClick: "+nameList.get(i));
                            hashSHA1 = FileToHash.calculateSHA256(applicationInfo.sourceDir,nameList.get(i));
                            check = check && hashSHA1.equals(hashList.get(i));
                            Log.d(TAG, "onClick: "+nameList.get(i));
                            Log.d(TAG, "onClick: "+hashSHA1);
                            Log.d(TAG, "onClick: "+hashList.get(i));
                        }

                        PackageInfo pinfo = null;
                        try {
                            pinfo = packageManager.getPackageInfo(packageName, 0);
                            int versionNumber = pinfo.versionCode;

                            String versionName = pinfo.versionName;

                            textView.setText("Installer Verified: "+installerVerified+ "\nApp Version Number:"+
                                    versionNumber+"\nApp Version Name: "+versionName+"\nClasses Hash matches: "+check);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


        return rootView;
    }

    public void getPathList(ApplicationInfo applicationInfo){
        MyFileRead fileRead = new MyFileRead(getContext(),applicationInfo.sourceDir);
        fileRead.start();
        while (fileRead.isAlive()){

        }
        LinkedList<String> list = fileRead.getList();
        String[] str ;
        for (int i = 0;i<list.size();i++) {

            Log.d(TAG, "getPathList: "+list.get(i));
            if ( i==0 || i%2==0 ){
                str = list.get(i).split(" ");
                nameList.add(str[str.length-1]);
            }
            if(i%2!=0 ){
                str = list.get(i).split(" ");
//                Log.d(TAG, "getPathList: "+list.get(i));
                hashList.add(str[str.length-1]);
            }
        }

    }




    public boolean isStoragePermissionGranted(){
        if (Build.VERSION.SDK_INT>=23){
            if (ContextCompat.checkSelfPermission(getContext(),permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "isStoragePermissionGranted: True");
                return true;
            }
            else {
//                Log.d(TAG, "isStoragePermissionGranted: False");
                ActivityCompat.requestPermissions(getActivity(),new String[]{permission.READ_EXTERNAL_STORAGE},1);
                return false;
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
            return true;
        }

    }


}
