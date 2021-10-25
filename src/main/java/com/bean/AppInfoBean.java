package com.bean;

import java.io.Serializable;

/**
 * Created by：bobby on 2021-08-19 16:24.
 * Describe：APP应用包信息
 */
public class AppInfoBean implements Serializable {
    public String appName; //APP名称
    public String version; //APP版本号，对应VERSION_NAME
    public String pkgName; //APP包名
    public long installTime; //安装时间（13位毫秒）
    public long updateTime; //更新时间（13位毫秒）
    public int isPreInstalled;//是否预装 0：否、1：是

    @Override
    public String toString() {
        return "AppInfoBean{" +
                "appName='" + appName + '\'' +
                ", version='" + version + '\'' +
                ", pkgName='" + pkgName + '\'' +
                ", installTime=" + installTime +
                ", updateTime=" + updateTime +
                ", isPreInstalled=" + isPreInstalled +
                '}';
    }
}