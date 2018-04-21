package com.example.capk.antivirus;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;

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

    public void uninstall(String packageName){

        Uri packageUri = Uri.fromParts("package",packageName,null);
        Intent uninstallIntent =
                new Intent(Intent.ACTION_DELETE);
        uninstallIntent.setData(packageUri);
//        uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT,true);
        startActivityForResult(uninstallIntent,1);


    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                android.util.Log.d("TAG", "onActivityResult: user accepted the (un)install ");
            } else if (resultCode == RESULT_CANCELED) {
                android.util.Log.d("TAG", "onActivityResult: user canceled the (un)install ");
            } else if (resultCode == RESULT_FIRST_USER) {
                android.util.Log.d("TAG", "onActivityResult: failed to (un)install");
            }
            Antivirus.mViewPager.getAdapter().notifyDataSetChanged();
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

        private List<ApplicationInfo> applicationInfoList;
        public MyAdapter(List<ApplicationInfo> applicationInfoList) {
            this.applicationInfoList = applicationInfoList;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView appName;
            public ImageView appIcon;
            public ImageView delete;

            public ViewHolder(View itemView) {
                super(itemView);
                appName = itemView.findViewById(R.id.appName);
                appIcon = itemView.findViewById(R.id.appIcon);
                delete = itemView.findViewById(R.id.delete);
            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.app_list_row, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final ApplicationInfo applicationInfo = applicationInfoList.get(position);
            holder.appIcon.setImageDrawable(packageManager.getApplicationIcon(applicationInfo));
            holder.appName.setText(packageManager.getApplicationLabel(applicationInfo));
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String packageName = applicationInfo.packageName;
                    uninstall(packageName);

                    applicationInfoList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,applicationInfoList.size());

                }
            });
            
        }

        @Override
        public int getItemCount() {
            return applicationInfoList.size();
        }

    }

    public void removeSystemApps(List<ApplicationInfo> applicationInfoList1){
       for (int i=0;i<applicationInfoList.size();i++){
            if(isSystemPackage(applicationInfoList.get(i))|| getActivity().getPackageName().equals(applicationInfoList1.get(i).packageName)){
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
