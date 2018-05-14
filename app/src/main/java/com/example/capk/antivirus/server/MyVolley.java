package com.example.capk.antivirus.server;
import android.content.Context;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.capk.antivirus.DBContract;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by capk on 4/19/18.
 */

public class MyVolley {

    private Context context;
    public static String response;
    public MyVolley(Context applicationContext) {
        this.context = applicationContext;
    }

    public void request(int type,String url, final String packageName, final String data){

        StringRequest stringRequest = new StringRequest(type, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: "+response);
                if (response.contains("infected")){
                    MyVolley.response = response;
                    String[] list = response.split("\n");
                    DBContract dbContract = new DBContract(context);
                    dbContract.updateScanData(list[0],"INFECTED");
                }

                else if(response.contains("1")){
                    MyVolley.response = response;
                    String[] list = response.split("\n");
                    DBContract dbContract = new DBContract(context);
                    dbContract.updateZDMData(list[0],"VIRUS");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse:  "+error);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();

                map.put(packageName, data);

                return map;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(stringRequest);

    }
}

