package com.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.SystemClock;


import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.bean.DeviceInfoBean;

/**
 * Created by：bobby on 2021-08-21 14:56.
 * Describe：设备信息工具类
 */
public class DeviceInfoUtils {
    private static DeviceInfoUtils instance;

    public static DeviceInfoUtils getInstance(){
        if(instance == null){
            synchronized(DeviceInfoUtils.class){
                if(instance == null){
                    instance = new DeviceInfoUtils();
                }
            }
        }
        return instance;
    }

    //获取设备信息
    public DeviceInfoBean getDeviceInfo(Context context){
        DeviceInfoBean bean = new DeviceInfoBean();
        bean.imei = SystemUtils.getIMEL(context);
        bean.hardwareSerial = SystemUtils.getSerial();
        bean.androidId = SystemUtils.getAndroidId(context);
        bean.systemVersions = SystemUtils.getSystemVersion();
        bean.rooted = SystemUtils.isRootSystem() ? 1 : 0;
        bean.isSimulator = VirtualUtils.isSimulator(context) ? 1 : 0; //是否是虚拟机，1是 0否
        bean.ramTotal = StorageUtils.getRAMTotal(context); //内存大小（单位字节）
        bean.ramCanUse = StorageUtils.getRAMAvailable(context); //可用内存大小（单位字节）
        bean.containSd = (StorageUtils.getInternalTotal() > 0L) ? 1 : 0;

        //手机自身存储空间大小（单位字节）
        bean.storageSize = StorageUtils.getInternalTotal();
        //手机自身可用存储空间（单位字节）
        bean.storageUsableSize = StorageUtils.getInternalAvailable();
        //sd卡存储空间大小（单位字节）
        bean.sdcardSize = StorageUtils.getExternalTotal();
        //sd卡可用存储空间（单位字节）
        bean.sdcardUsableSize = StorageUtils.getExternalAvailable();

        LogUtils.e("内存总大小：" + StorageUtils.formatSize(bean.ramTotal) + "  可用内存大小：" + StorageUtils.formatSize(bean.ramCanUse)
         +" 总:" + StorageUtils.getSDCardTotalStorage(bean.ramTotal)  +"  内：" + StorageUtils.getSDCardTotalStorage(bean.ramCanUse));

        bean.deviceName = SystemUtils.getDeviceName();
        bean.phoneBrand = SystemUtils.getDeviceBrand();
        bean.phoneType = SystemUtils.getSystemPhoneModel();
        bean.physicalSize = SystemUtils.getScreenPhysicalSize(context);
        bean.cpuNum = SystemUtils.getNumAvailableCores();
        bean.deviceWidth = SystemUtils.getScreenWidth(context);
        bean.deviceHeight = SystemUtils.getScreenHeight(context);
        bean.carrier = SystemUtils.getMno(context);
        bean.timeZoneId = TimeZone.getDefault().getID();
        bean.gpsLongitude = LocationUtils.getLocationInfo(context).longitude;
        bean.gpsLatitude = LocationUtils.getLocationInfo(context).latitude;
        new BatteryUtils(context, new Action<BatteryUtils.BatteryData>() {
            public void call(BatteryUtils.BatteryData batteryData) {
                bean.batteryLevel  = batteryData.level;
            }
        });
        bean.timeToPresent = SystemClock.elapsedRealtime();
        bean.workTimeToPresent = SystemClock.uptimeMillis();
        bean.networkType = NetworkUtil.getNetworkState(context);
        bean.netIp = NetworkUtil.getGPRSIP();
        bean.wifiIp = NetworkUtil.intToIP(NetworkUtil.getWIFIIP(context));
        WifiInfo wifiInfo = NetworkUtil.getWifiInfo(context);
        bean.wifiName = wifiInfo.getSSID();
        bean.wifiMac = wifiInfo.getMacAddress();
        bean.configuredWifi = new ArrayList<>();
        List<ScanResult> scanResults = NetworkUtil.getWifiList(context);
        for(int i = 0; i < scanResults.size(); ++i) {
            DeviceInfoBean.WifiData wifiData = new DeviceInfoBean.WifiData();
            ScanResult result = scanResults.get(i);
            wifiData.bssid = result.BSSID;
            wifiData.ssid = result.SSID;
            bean.configuredWifi.add(wifiData);
        }
        bean.wifiCount = String.valueOf(scanResults.size());
        return bean;
    }

} 