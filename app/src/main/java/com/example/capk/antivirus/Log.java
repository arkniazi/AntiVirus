package com.example.capk.antivirus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by CAPK on 1/29/2018.
 */

public class Log extends android.support.v4.app.Fragment {
    private PackageManager packageManager;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_log, container, false);
        packageManager = getContext().getPackageManager();
        recyclerView = rootView.findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapter();
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{


        List<DBSchema> list;
        RecyclerAdapter(){
            DBContract dbContract = new DBContract(getContext());
            list = dbContract.getData();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView appLabel;
            public ImageView appIcon;
            public TextView appVersion;
            public TextView scanStatus;
            public TextView zdmStatus;

            public ViewHolder(View itemView) {
                super(itemView);
                appLabel = itemView.findViewById(R.id.app_label);
                appIcon = itemView.findViewById(R.id.app_icon);
                appVersion = itemView.findViewById(R.id.app_version);
                scanStatus = itemView.findViewById(R.id.scan_status);
                zdmStatus = itemView.findViewById(R.id.zdm_status);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_layout, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            android.util.Log.d(TAG, "onBindViewHolder: "+"working");
            DBSchema dbSchema = list.get(position);
            try {
                 ApplicationInfo applicationInfo = packageManager.getApplicationInfo(dbSchema.getPackageName(),0);
                 holder.appIcon.setImageDrawable(packageManager.getApplicationIcon(applicationInfo));
                 holder.appLabel.setText(packageManager.getApplicationLabel(applicationInfo));
                 holder.appVersion.setText("Verified: "+dbSchema.getDexCheck());
                 if (dbSchema.getScanStatus().equals("INFECTED")){
                     holder.scanStatus.setTextColor(Color.RED);
                 }
                 holder.scanStatus.setText("Scan Status: "+dbSchema.getScanStatus());
                 holder.zdmStatus.setText("ZDM Analysis: "+dbSchema.getZdmAnalysis());

             } catch (PackageManager.NameNotFoundException e) {
                 e.printStackTrace();
             }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
