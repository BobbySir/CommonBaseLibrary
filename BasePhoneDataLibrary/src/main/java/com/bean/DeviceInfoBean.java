package com.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by：bobby on 2021-08-19 16:45.
 * Describe：设备信息
 */
public class DeviceInfoBean implements Serializable {
    public long ramTotal;      //内存大小（单位字节）
    public long ramCanUse;    //内存可用空间（单位字节）

    public long storageSize;    //手机自身存储空间大小（单位字节）
    public long storageUsableSize;  //手机自身可用存储空间（单位字节）
    public long sdcardSize;	        //sd卡存储空间大小（单位字节）
    public long sdcardUsableSize;	//sd卡可用存储空间（单位字节）
    public int deviceWidth;    //分辨率宽
    public int deviceHeight;    //分辨率高


    public String imei;	//imei设备标识
    public String deviceId; //安卓id
    public String version; //手机系统版本
    public int is_simulator;    //是否模拟器，取值 0：否、1：是
    public String ram; //ram2,8 MB/24,8 GB
    public String rom; //rom2,89 GB/24,87 GB
    public String osType = "android"; //操作系统，如：iOS
    public String osVersion;  //当前APP版本
    public String deviceInfo; //手机品牌
    public String phoneName;  //设备名称
    public String mobileModel;    //手机型号
    public int netType;    //网络类型，取值 1：wifi、2：2g、3：3g、4：4g
    public String osChannel = "android"; //渠道号
    public String mno; //运营商
    public String mac; //mac地址
    public int cpuCore;    //CPU内核数
    public String resolution; //分辨率100x122
    public String deviceUuid; //手机的唯一标识
    public String deviceTz; //时区的ID，格式America/Mexico_City


    public int containSd;    //是否有内置sd卡，取值 0：否、1：是
    public int rooted; //是否root，取值 0：否、1：是
    public String hardwareSerial; //硬件设备序列号
    public double physicalSize;    //物理尺寸，如5.25（英寸）
    public double gpsLongitude;    //gps经度
    public double gpsLatitude;    //gps纬度
    public int batteryLevel;    //电池电量，取值0~100
    public long timeToPresent;    //开机时间到现在的时间（毫秒）
    public long workTimeToPresent;    //开机时间到现在的时间（不含休眠时间）（毫秒）
    public String netIpv;    //当前网络 ip地址，网络&wifi必须传一个
    public String wifiIp;    //当前wifi ip地址，网络&wifi必须传一个
    public String wifiName;  //当前wifi 名称
    public String wifiMac;    //当前wifi mac地址
    public String wifiCount;  //wifi个数
    public List<WifiData> configuredWifi;    //配置过的wifi列表信息

//    configuredWifi 信息:
//    bassid	string	是	wifi mac地址
//    ssid	string	是	wifi 名称
    public static class WifiData implements Serializable{
        public String bssid;
        public String ssid;

        @Override
        public String toString() {
            return "WifiData{" +
                    "bssid='" + bssid + '\'' +
                    ", ssid='" + ssid + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DeviceInfoBean{" +
                "ramTotal=" + ramTotal +
                ", ramCanUse=" + ramCanUse +
                ", storageSize=" + storageSize +
                ", storageUsableSize=" + storageUsableSize +
                ", sdcardSize=" + sdcardSize +
                ", sdcardUsableSize=" + sdcardUsableSize +
                ", deviceWidth=" + deviceWidth +
                ", deviceHeight=" + deviceHeight +
                ", imei='" + imei + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", version='" + version + '\'' +
                ", is_simulator=" + is_simulator +
                ", ram='" + ram + '\'' +
                ", rom='" + rom + '\'' +
                ", osType='" + osType + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", phoneName='" + phoneName + '\'' +
                ", mobileModel='" + mobileModel + '\'' +
                ", netType=" + netType +
                ", osChannel='" + osChannel + '\'' +
                ", mno='" + mno + '\'' +
                ", mac='" + mac + '\'' +
                ", cpuCore=" + cpuCore +
                ", resolution='" + resolution + '\'' +
                ", deviceUuid='" + deviceUuid + '\'' +
                ", deviceTz='" + deviceTz + '\'' +
                ", containSd=" + containSd +
                ", rooted=" + rooted +
                ", hardwareSerial='" + hardwareSerial + '\'' +
                ", physicalSize=" + physicalSize +
                ", gpsLongitude=" + gpsLongitude +
                ", gpsLatitude=" + gpsLatitude +
                ", batteryLevel=" + batteryLevel +
                ", timeToPresent=" + timeToPresent +
                ", workTimeToPresent=" + workTimeToPresent +
                ", netIpv='" + netIpv + '\'' +
                ", wifiIp='" + wifiIp + '\'' +
                ", wifiName='" + wifiName + '\'' +
                ", wifiMac='" + wifiMac + '\'' +
                ", wifiCount='" + wifiCount + '\'' +
                ", configuredWifi=" + configuredWifi +
                '}';
    }
}