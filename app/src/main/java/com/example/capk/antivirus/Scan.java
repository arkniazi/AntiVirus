package com.example.capk.antivirus;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import at.grabner.circleprogress.CircleProgressView;

/**
 * Created by CAPK on 1/29/2018.
 */


public class Scan extends android.support.v4.app.Fragment {
    CircleProgressView mCircleView ;
    Button partialScan;
    Button fullScan;


    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_scan, container, false);
        partialScan = rootView.findViewById(R.id.partialScan);
        fullScan = rootView.findViewById(R.id.fullScan);
        mCircleView = rootView.findViewById(R.id.circleView);

        Helper helper = new Helper();
        IntentFilter intentFilter = new IntentFilter("DAGON_SCAN");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(helper,intentFilter);
        partialScan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(),ScanService.class);
                intent.setData(Uri.parse("partial"));
                getActivity().startService(intent);

                partialScan.setVisibility(View.INVISIBLE);
                fullScan.setVisibility(View.INVISIBLE);
                mCircleView.setVisibility(View.VISIBLE);



            }
        });

        fullScan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ScanService.class);
                intent.setData(Uri.parse("full"));
//                getActivity().startService(intent);
//                partialScan.setVisibility(View.INVISIBLE);
//                fullScan.setVisibility(View.INVISIBLE);
//                mCircleView.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getContext(), ScanService.class);
        getContext().stopService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMyServiceRunning(ScanService.class)){
            partialScan.setVisibility(View.INVISIBLE);
            fullScan.setVisibility(View.INVISIBLE);
            mCircleView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public class Helper extends BroadcastReceiver{
        public  boolean isStoragePermissionGranted() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(getContext(), permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{permission.READ_EXTERNAL_STORAGE}, 1);
                    return false;
                }
            } else {
                return true;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra("progress",0);
            mCircleView.setValue(progress);
            if (progress>=100){
                partialScan.setVisibility(View.VISIBLE);
                fullScan.setVisibility(View.VISIBLE);
                mCircleView.setVisibility(View.INVISIBLE);
                Antivirus.mViewPager.arrowScroll(View.FOCUS_RIGHT);
                Antivirus.mViewPager.getAdapter().notifyDataSetChanged();
            }
        }
    }
}