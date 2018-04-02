package com.example.capk.antivirus;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.security.MessageDigest;

import static eu.chainfire.libsuperuser.Debug.TAG;

/**
 * Created by CAPK on 1/29/2018.
 */

public class Log extends android.support.v4.app.Fragment {


    private static final int VALID = 0;

    private static final int INVALID = 1;
     private  static final String SIGNATURE = "";
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_log, container, false);
        Button button = rootView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });
        return rootView;
    }


    public static int checkAppSignature(Context context) {

        try {

            PackageInfo packageInfo = context.getPackageManager()

                    .getPackageInfo(context.getPackageName(),

                            PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {

                byte[] signatureBytes = signature.toByteArray();

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                android.util.Log.d("REMOVE_ME", "Include this string as a value for SIGNATURE:" + currentSignature);

                //compare signatures

                if (SIGNATURE.equals(currentSignature)){

                    return VALID;

                };

            }

        } catch (Exception e) {

//assumes an issue in checking signature., but we let the caller decide on what to do.

        }

        return INVALID;

    }

}
