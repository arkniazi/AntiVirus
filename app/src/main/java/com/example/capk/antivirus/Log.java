package com.example.capk.antivirus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

}
