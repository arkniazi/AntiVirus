package com.example.capk.antivirus.Service;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by capk on 5/9/18.
 */

public class ServiceCheck {
    Context context;
    public ServiceCheck(Context context){
        this.context = context;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}

