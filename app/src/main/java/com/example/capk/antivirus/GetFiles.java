package com.example.capk.antivirus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by capk on 4/11/18.
 */

public class GetFiles {


    public static String[] getPermissions(PackageManager packageManager, ApplicationInfo applicationInfo) {
        PackageInfo packageInfo=null;
        try {
             packageInfo=packageManager.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo.requestedPermissions;
    }


}
