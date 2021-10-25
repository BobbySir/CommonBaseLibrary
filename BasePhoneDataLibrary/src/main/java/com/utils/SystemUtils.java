package com.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileFilter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by：bobby on 2021-08-21 15:27.
 * Describe：
 */
public class SystemUtils {

    @SuppressLint("MissingPermission")
    public static String getIMEI(Context context) {
        String imei = null;

        try {
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
            imei = telephonyManager.getDeviceId();
        } catch (Exception var3) {
        }

        if (imei == null) {
            imei = "";
        }

        return imei;
    }

    /**
     * 根据给定Key获取值.
     *
     * @return 如果不存在该key则返回空字符串
     * @throws IllegalArgumentException 如果key超过32个字符则抛出该异常
     */
    private static String getIMEL(Context context, String key) throws IllegalArgumentException {
        String ret = "";
        try {
            ClassLoader cl = context.getClassLoader();
            @SuppressWarnings("rawtypes")
            Class SystemProperties = cl.loadClass("android.os.SystemProperties");
            //参数类型
            @SuppressWarnings("rawtypes")
            Class[] paramTypes = new Class[1];
            paramTypes[0] = String.class;
            Method get = SystemProperties.getMethod("get", paramTypes);
            //参数
            Object[] params = new Object[1];
            params[0] = new String(key);
            ret = (String) get.invoke(SystemProperties, params);
        } catch (IllegalArgumentException iAE) {
            throw iAE;
        } catch (Exception e) {
            ret = "";
            //TODO
        }
        return ret;
    }


    /**
     * 获取设备唯一码
     *  0：IMEL1  1: IMEL2 否则MEID
     * @return
     */
    public static String getIMEL(Context mContext) {
        String imei1 = "gsm.mtk.imei1";
        String imei2 = "gsm.mtk.imei2";
        String meid = "gsm.mtk.meid";

        String sb = "";
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if (mContext != null) {
                //如果>9.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    if (!TextUtils.isEmpty(getUUID(mContext,appName))) {
//                    if (!TextUtils.isEmpty(getNewDeviceId(mContext,appName, 0))) {
//                        sb=(getUUID(mContext,appName));
//                    }
                    if (!TextUtils.isEmpty(getNewDeviceId(mContext,0))) {
                        sb = (getNewDeviceId(mContext,0));
                    }
                    else {
                        sb=(getAndroidId(mContext));
                    }
                } else {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        if (!TextUtils.isEmpty(getNewDeviceId(mContext, 0))) {
                            sb=(getNewDeviceId(mContext,0));
                        }
                    } else {
                        sb=(getIMEL(mContext, imei1));
                    }

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        if (!TextUtils.isEmpty(getNewDeviceId(mContext, 1))) {
                            sb = (getNewDeviceId(mContext, 1));
                        }
                    } else {
                        sb = (getIMEL(mContext, imei2));
                    }

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        if (!TextUtils.isEmpty(getNewDeviceId(mContext, 3))) {
                            sb = (getNewDeviceId(mContext,3));
                        }
                    } else {
                        sb = (getIMEL(mContext, meid));
                    }
                }
            }
        }
        return sb;
    }


    /**
     * 获取设备唯一码
     * @param type  0：IMEL1  1: IMEL2 否则MEID
     * @return
     */
    @SuppressLint({"MissingPermission"})
    private static String getNewDeviceId(final Context mContext,final int type) {
        String did = "";
        if (mContext != null) {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                if (tm != null) {
                    Method method = tm.getClass().getMethod("getDeviceId", int.class);
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        //如果>9.0
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                            //did = tm.getImei(type);
                            if(!TextUtils.isEmpty(getUniqueID(mContext))){
                                did = getUniqueID(mContext);
                            }else{
                                did = getIMEI(mContext);
                            }
                        }else{
                            switch (type) {
                                case 0:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        did = tm.getImei(type);
                                    } else {
                                        did = tm.getDeviceId();
                                    }
                                    break;
                                case 1:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        did = tm.getImei(type);
                                    } else {
                                        did = (String) method.invoke(tm, TelephonyManager.PHONE_TYPE_GSM);
                                    }
                                    break;
                                default:
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        did = tm.getMeid();
                                    } else {
                                        did = (String) method.invoke(tm, TelephonyManager.PHONE_TYPE_CDMA);
                                    }
                                    break;
                            }
                        }
                    } else {
                        Log.i("out", "没有权限");
                    }
                } else {
                    did = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return did;
    }

    public static String getUniqueID(Context context) {
        String id = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
            try {
                UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                id = uuid.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getUUID(context);
        }
        return TextUtils.isEmpty(id) ? UUID.randomUUID().toString() : id;
    }

    @SuppressLint("MissingPermission")
    private static String getUUID(Context mContext) {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                ((null != Build.CPU_ABI) ? Build.CPU_ABI.length() : 0) % 10 +

                Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 +

                Build.HOST.length() % 10 + Build.ID.length() % 10 +

                Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 +

                Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 +

                Build.TYPE.length() % 10 + Build.USER.length() % 10; //13 位

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                        return "8765421120112";
                    }else{
                        serial = android.os.Build.getSerial();
                    }
                } else {
                    serial = Build.SERIAL;
                }
                //API>=9 使用serial号
                return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            } catch (Exception exception) {
                serial = "serial"; // 随便一个初始化
            }
        } else {
            serial = android.os.Build.UNKNOWN; // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }

    //硬件设备序列号
    public static String getSerial() {
        return Build.SERIAL;
    }

    //安卓id
    public static String getAndroidId(Context context) {
        return Settings.System.getString(context.getContentResolver(), "android_id");
    }

    //获取当前手机系统版本号
    public static String getSystemVersion(){
        return Build.VERSION.RELEASE;
    }

    //是否Root了手机
    public static boolean isRootSystem() {
        return isRootSystem1() || isRootSystem2();
    }

    private static boolean isRootSystem1() {
        File f = null;
        String[] kSuSearchPaths = new String[]{"/system/bin/", "/system/xbin/", "/system/sbin/", "/sbin/", "/vendor/bin/"};

        try {
            for(int i = 0; i < kSuSearchPaths.length; ++i) {
                f = new File(kSuSearchPaths[i] + "su");
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception var3) {
        }

        return false;
    }

    private static boolean isRootSystem2() {
        List<String> pros = Arrays.asList(java.lang.System.getenv("PATH").split(":"));
        File f = null;

        try {
            for(int i = 0; i < pros.size(); ++i) {
                f = new File((String)pros.get(i), "su");
                java.lang.System.out.println("f.getAbsolutePath():" + f.getAbsolutePath());
                if (f != null && f.exists() && f.canExecute()) {
                    return true;
                }
            }
        } catch (Exception var3) {
        }

        return false;
    }

    //设置名称(参数)
    public static String getDeviceName() {
        return Build.DEVICE;
    }
    //获取手机品牌(手机制造厂商)
    public static String getDeviceBrand(){
        return Build.BRAND;
    }
    //获取手机型号
    public static String getSystemPhoneModel(){
        return Build.MODEL;
    }

    //尺寸
    public static double getScreenPhysicalSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(dm);
        double x = Math.pow(((float)dm.widthPixels / dm.xdpi), 2);
        double y = Math.pow(((float)dm.heightPixels / dm.ydpi), 2);
        double screenInches = Math.sqrt(x + y);
        return screenInches;
    }


  /*  public  static  int getScreenWidth(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return  outMetrics.widthPixels;
    }


    public  static  int getScreenHeight(Context context){
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return  outMetrics.heightPixels;
    }*/

    public  static  int getScreenWidth(Context context){
        WindowManager manager = null;
        if(context instanceof Activity){
            manager =((Activity) context).getWindowManager();
        }else{
            manager  = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        if(manager == null){
            return 0;
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getRealMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        return  width;
    }

    public  static  int getScreenHeight(Context context){
        WindowManager manager = null;
        if(context instanceof Activity){
            manager = ((Activity) context).getWindowManager();
        }else{
            manager  = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        if(manager == null){
            return 0;
        }
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getRealMetrics(outMetrics);
        int height = outMetrics.heightPixels;
        return  height;
    }


    //CPU核数
    public static int getNumAvailableCores() {
        int num = getNumCores();
        return num > 0 ? num : Runtime.getRuntime().availableProcessors();
    }
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");

            class CpuFilter implements FileFilter {
                CpuFilter() {
                }

                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            }

            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception var2) {
            var2.printStackTrace();
            return 1;
        }
    }

    //获取手机运营商
    public static String getMno(Context context){
        String mno = "";
        if(hasSim(context)){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            mno = tm.getSimOperatorName();
        }
        return mno;
    }
    /**
     * 判断是否有Sim卡
     *
     * @return
     */
    public static boolean hasSim(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getSimState() == TelephonyManager.SIM_STATE_READY) {
            return true;
        } else {
            return false;
        }
    }
} 