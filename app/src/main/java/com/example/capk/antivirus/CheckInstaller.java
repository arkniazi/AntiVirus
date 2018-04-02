package com.example.capk.antivirus;

import android.content.Context;
import android.util.*;

import static android.content.ContentValues.TAG;

/**
 * Created by capk on 3/13/18.
 */

public class CheckInstaller {
    Context context;
    CheckInstaller(Context context){
        this.context = context;

    }
    public boolean isLegal(String packageName){
//            android.util.Log.d(TAG, "isLegal: "+packageName);
//            android.util.Log.d(TAG, "isLegal: "+context.getPackageManager().getInstallerPackageName(packageName));
        if ((context.getPackageManager().getInstallerPackageName(packageName) == "com.android.vending") ||
                (context.getPackageManager().getInstallerPackageName(packageName) == "com.amazon.venezia"))
        {
            return true;
        }
        return false;
    }
}
