package com.bean;

import java.io.Serializable;

/**
 * Created by：bobby on 2021-08-19 16:24.
 * Describe：APP应用包信息
 */
public class AppInfoBean implements Serializable {
    public String appName; //APP名称
    public String packageName; //APP包名
    public String installTime; //安装时间（13位毫秒 转换为年月日）
    public String firstInstallTime; //更新时间（13位毫秒 转换为年月日）
    public int systemApp;//是否预装 0：否、1：是 （isPreInstalled）
    public String versionCode; //
    public String versionName; //APP版本号，对应VERSION_NAME

    public String appId; //app包名
    public String creationTime; //创建时间
    public String lastUpdateTime; //最后更新时间

    @Override
    public String toString() {
        return "AppInfoBean{" +
                "appName='" + appName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", installTime='" + installTime + '\'' +
                ", firstInstallTime='" + firstInstallTime + '\'' +
                ", systemApp=" + systemApp +
                ", versionCode='" + versionCode + '\'' +
                ", versionName='" + versionName + '\'' +
                ", appId='" + appId + '\'' +
                ", creationTime='" + creationTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                '}';
    }
}