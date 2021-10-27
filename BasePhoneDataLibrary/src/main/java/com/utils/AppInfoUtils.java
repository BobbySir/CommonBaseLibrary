package com.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;


import java.util.ArrayList;
import java.util.List;

import com.bean.AppInfoBean;

/**
 * Created by：bobby on 2021-08-20 10:43.
 * Describe：APP安装信息
 */
public class AppInfoUtils {

    private static AppInfoUtils instance;

    public static AppInfoUtils getInstance(){
        if(instance == null){
            synchronized(AppInfoUtils.class){
                if(instance == null){
                    instance = new AppInfoUtils();
                }
            }
        }
        return instance;
    }

    public List<AppInfoBean> getInstallApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<AppInfoBean> beanList = new ArrayList<>();
        try {
            @SuppressLint("QueryPermissionsNeeded") List<PackageInfo> list2 = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
            if(!EmptyUtil.isEmpty(list2)){
                for (PackageInfo packageInfo : list2) {
                    try {
                        AppInfoBean appInfoBean = new AppInfoBean();
                        //APP名称
                        appInfoBean.name = StringUtils.isEmptyString(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                        //包名
                        appInfoBean.packageName = StringUtils.isEmptyString(packageInfo.packageName);
                        //安装时间
                        if(packageInfo.firstInstallTime > 0){
                            appInfoBean.installTime = packageInfo.firstInstallTime;
                        }
                        //更新时间
                        if(packageInfo.lastUpdateTime > 0) {
                            appInfoBean.firstInstallTime = packageInfo.lastUpdateTime;
                        }
                        //APP版本号，对应VERSION_NAME
                        appInfoBean.versionName = packageInfo.versionName;
                        //VERSION_CODE
                        String versionCode = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                            versionCode = String.valueOf(packageInfo.getLongVersionCode());
                        }else{
                            versionCode = String.valueOf(packageInfo.versionCode);
                        }
                        appInfoBean.versionCode = versionCode;
                        //是否预装 0：否、1：是
                        appInfoBean.systemApp = (packageInfo.applicationInfo.flags & 1) > 0 ? 1 : 0;

                        beanList.add(appInfoBean);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return beanList;
    }
} 