package com.example.capk.antivirus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CAPK on 1/29/2018.
 */


public class Scan extends android.support.v4.app.Fragment {
    String apkName;
    EditText name;
    PackageManager packageManager;
    TextView officialHash;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_scan, container, false);
        name = rootView.findViewById(R.id.editText2);
        packageManager = getContext().getPackageManager();
        officialHash = rootView.findViewById(R.id.textView4);
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

                        try {
                             Signature[] signatures = packageManager.getPackageInfo(packageName,PackageManager.GET_SIGNATURES).signatures;
                            for (Signature signature: signatures){
                                Log.d("Signature ", signature.hashCode()+"");
                                officialHash.setText(signature.hashCode()+"");
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }


                }
//                List<ResolveInfo> pkgAppsList = getContext().getPackageManager().queryIntentActivities( mainIntent, 0);
//                //      //  this code reads the apk content even if it is not installed
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
