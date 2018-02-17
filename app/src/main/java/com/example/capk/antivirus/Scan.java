package com.example.capk.antivirus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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
//                    Log.d("Name", packageName+"");
                    String name = (String) packageManager.getApplicationLabel(applicationInfo);
                    Log.d("Name", name+"");
                    if(name.equals(apkName)){
                        String hashSHA1 = "";
                        String hashMD5 = "";
                        FileRead fileRead = new FileRead(getContext(),applicationInfo.dataDir);
                        String data = fileRead.getRes();
                        SHA1 sha1 = new SHA1();
                        MD5 md5 = new MD5();
                        try {
                            hashSHA1 = sha1.SHA1(data);
                            hashMD5 = md5.getMD5(data);

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        PackageInfo pinfo = null;
                        try {
                            pinfo = packageManager.getPackageInfo(packageName, 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        int versionNumber = pinfo.versionCode;
                        String versionName = pinfo.versionName;
                        Log.d("SHA1", " "+hashSHA1);
                        textView.setText("MD5: "+hashMD5+"\n \nSHA1: "+hashSHA1
                        +"\n"+versionNumber+"\n"+versionName);

                    }


                }
//                List<ResolveInfo> pkgAppsList = getContext().getPackageManager().queryIntentActivities( mainIntent, 0);
                //      //  this code reads the apk content even if it is not installed
//                final PackageManager packageManager = getActivity().getPackageManager();
//                String path = Environment.getExternalStorageDirectory()+"/"+apkName;
//                PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path,0);
//                Signature[] signature  = packageInfo.signatures;
//                Toast.makeText(getContext(),signature[1]+"",Toast.LENGTH_LONG).show();

            }
        });


        return rootView;
    }

    // answer from stack_over_flow  https://stackoverflow.com/questions/2695746/how-to-get-a-list-of-installed-android-applications-and-pick-one-to-run
    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }
}
