package com.example.capk.antivirus;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CAPK on 1/29/2018.
 */

public class AppList extends android.support.v4.app.Fragment {

    PackageManager packageManager;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    List<ApplicationInfo> applicationInfoList;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_installed_apps, container, false);

        packageManager = getContext().getPackageManager();
        applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        removeSystemApps(applicationInfoList);

        recyclerView = rootView.findViewById(R.id.recyclerView);

        myAdapter = new MyAdapter(applicationInfoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);

        return rootView;
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private List<ApplicationInfo> applicationInfoList;
        public MyAdapter(List<ApplicationInfo> applicationInfoList) {
            this.applicationInfoList = applicationInfoList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView appName;
            public ImageView appIcon;

            public ViewHolder(View itemView) {
                super(itemView);
                appName = itemView.findViewById(R.id.appName);
                appIcon = itemView.findViewById(R.id.appIcon);
                
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.app_list_row, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ApplicationInfo applicationInfo = applicationInfoList.get(position);
            holder.appIcon.setImageDrawable(packageManager.getApplicationIcon(applicationInfo));
            holder.appName.setText(packageManager.getApplicationLabel(applicationInfo));
            
        }

        @Override
        public int getItemCount() {
            return applicationInfoList.size();
        }

    }

    public void removeSystemApps(List<ApplicationInfo> applicationInfoList1){
       for (int i=0;i<applicationInfoList.size();i++){
            if(isSystemPackage(applicationInfoList.get(i))){
                applicationInfoList.remove(i);
                --i;
            }
       }
    }

    // answer from stack_over_flow
    private boolean isSystemPackage(ApplicationInfo applicationInfo) {
        return ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
