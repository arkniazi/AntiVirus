package com.example.capk.antivirus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.jaredrummler.apkparser.ApkParser;
import com.jaredrummler.apkparser.model.DexClass;
import com.jaredrummler.apkparser.model.DexInfo;
import com.jaredrummler.apkparser.struct.dex.DexHeader;

import java.io.IOException;
import java.util.List;

/**
 * Created by capk on 4/11/18.
 */

public class GetFiles {

    public DexClass[] getDexFiles(ApplicationInfo applicationInfo){
        DexClass[] dexClasses = null;
        ApkParser apkParser = ApkParser.create(applicationInfo);
        List<DexInfo> dexFiles = null; // if size > 1 then app is using multidex
        try {
            dexFiles = apkParser.getDexInfos();
            for (DexInfo dexInfo : dexFiles) {
                dexClasses = dexInfo.classes;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dexClasses;
    }


    public static String[] getPermissions(PackageInfo packageInfo) {
        return packageInfo.requestedPermissions;
    }
}
