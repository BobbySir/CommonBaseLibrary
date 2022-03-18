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
    public DeviceInfoBean getDeviceInfo(Context context, String versionName){
        DeviceInfoBean bean = new DeviceInfoBean();
        bean.imei = SystemUtils.getIMEL(context);
        bean.osType = VirtualUtils.isHarmonyOs();
        bean.osChannel = VirtualUtils.isHarmonyOs();
        bean.country = SystemUtils.getSimCountry(context);

        String curCpuFreq = SystemUtils.getCurCpuFreq();
        curCpuFreq = curCpuFreq.length() != 0 ? curCpuFreq : "0";
        bean.cpuSpeed = curCpuFreq;


        bean.deviceId = SystemUtils.getAndroidId(context);
        bean.androidId = SystemUtils.getAndroidId(context);
        bean.version = SystemUtils.getSystemVersion();
        bean.rooted = SystemUtils.isRootSystem() ? 1 : 0;
        bean.is_simulator = VirtualUtils.isSimulator(context) ? 0 : 1; //是否是虚拟机，0是 1否
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

        bean.ram = StorageUtils.getTotalMemory(context);
        bean.rom = StorageUtils.getTotalInternalMemorySize(context);
//        LogUtils.e("内存总大小：" + StorageUtils.formatSize(bean.ramTotal) + "  可用内存大小：" + StorageUtils.formatSize(bean.ramCanUse)
//         +" 总:" + StorageUtils.getSDCardTotalStorage(bean.ramTotal)  +"  内：" + StorageUtils.getSDCardTotalStorage(bean.ramCanUse));
        bean.osVersion = versionName;

        bean.deviceInfo = SystemUtils.getDeviceName();
        bean.brand = SystemUtils.getDeviceName();
        bean.phoneName = SystemUtils.getDeviceBrand();
        bean.mobileModel = SystemUtils.getSystemPhoneModel();
        bean.cpuCore = SystemUtils.getNumAvailableCores();
        bean.deviceWidth = SystemUtils.getScreenWidth(context);
        bean.deviceHeight = SystemUtils.getScreenHeight(context);
        bean.resolution =  bean.deviceHeight + "*" + bean.deviceWidth;
        bean.mno = SystemUtils.getMno(context);
        bean.simSerialNumber = SystemUtils.getSimSerialNumber(context);
        bean.mac = SystemUtils.getMac(context);
        bean.deviceUuid = SystemUtils.getUniqueID(context);
        bean.deviceTz = TimeZone.getDefault().getID();

        bean.hardwareSerial = SystemUtils.getSerial();
        bean.physicalSize = SystemUtils.getScreenPhysicalSize(context);
        bean.gpsLongitude = LocationUtils.getLocationInfo(context).longitude;
        bean.gpsLatitude = LocationUtils.getLocationInfo(context).latitude;
        new BatteryUtils(context, new Action<BatteryUtils.BatteryData>() {
            public void call(BatteryUtils.BatteryData batteryData) {
                bean.batteryLevel  = batteryData.level;
            }
        });
        bean.timeToPresent = SystemClock.elapsedRealtime();
        bean.workTimeToPresent = SystemClock.uptimeMillis();
        bean.netType = NetworkUtil.getNetworkState(context);
        bean.netIpv = NetworkUtil.getGPRSIP();


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