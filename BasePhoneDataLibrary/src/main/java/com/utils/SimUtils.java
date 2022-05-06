package com.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.bean.SimBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by：bobby on 2022-04-27 18:14.
 * Describe：手机工具类
 */
public class SimUtils {
    private static SimUtils instance;

    public static SimUtils getInstance() {
        if (instance == null) {
            synchronized (SimUtils.class) {
                if (instance == null) {
                    instance = new SimUtils();
                }
            }
        }
        return instance;
    }

    //获取本机所有手机号
    @SuppressLint("MissingPermission")
    public List<SimBean> getPhones(Context context) {
        List<SimBean> simBeanList = new ArrayList<>();
        try {
            //SIM默认id
            int defaultSubId = -1;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                SubscriptionManager subscriptionManager = SubscriptionManager.from(context);

                //获取默认通话的subId
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    defaultSubId = SubscriptionManager.getDefaultSmsSubscriptionId();
                }

                String permission;
                //如果没有读取手机状态的权限就不往下走
                if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    permission = Manifest.permission.READ_PHONE_NUMBERS;
                }else{
                    permission = Manifest.permission.READ_PHONE_STATE;
                }
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return simBeanList;
                }
                List<SubscriptionInfo> mSubcriptionInfos = subscriptionManager.getActiveSubscriptionInfoList();
                if (!EmptyUtil.isEmpty(mSubcriptionInfos)) {
                    for (int i = 0; i < mSubcriptionInfos.size(); i++) {
                        SubscriptionInfo info = mSubcriptionInfos.get(i);
                        String telNumber = info.getNumber();

                        //如果手机号码为空跳出当前循环
                        if (EmptyUtil.isEmpty(telNumber)) {
                            continue;
                        }

                        SimBean sb = new SimBean();
                        if (telNumber.startsWith("+") && telNumber.length() > 9) {
                            telNumber = telNumber.substring(3, telNumber.length());
                        }
                        sb.phone = telNumber;

                        //如果有两张SIM卡
                        if (mSubcriptionInfos.size() > 1) {
                            //获取默认通话的subId 7.0手机才有
                            if (defaultSubId > -1) {
                                if (defaultSubId == info.getSubscriptionId()) {
                                    sb.isDefaultVoicePhone = true;
                                }
                            } else {
                                //取第0个为默认
                                if (i == 0) {
                                    sb.isDefaultVoicePhone = true;
                                }
                            }
                        } else {
                            sb.isDefaultVoicePhone = true;
                        }
                        simBeanList.add(sb);
                    }
                }
            } else {
                SimBean sb = new SimBean();
                //少于5.0的话
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                sb.isDefaultVoicePhone = true;
                String telNumber = tm.getLine1Number();

                //如果手机号码不为空才添加
                if (!EmptyUtil.isEmpty(telNumber)) {
                    if (telNumber.startsWith("+") && telNumber.length() > 9) {
                        telNumber = telNumber.substring(3, telNumber.length());
                    }
                    sb.phone = telNumber;
                    simBeanList.add(sb);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //只有一张卡
        if(simBeanList.size() == 1){
            simBeanList.get(0).isDefaultVoicePhone = true;
        }
        return simBeanList;
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    @SuppressLint("MissingPermission")
    public boolean hasSimCard(Context context) {
        return getPhones(context).size() > 0;
    }

/*    *//**
     * 判断是否包含SIM卡
     *
     * @return 状态
     *//*
    @SuppressLint("MissingPermission")
    public boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        Log.d("try", result ? "有SIM卡" : "无SIM卡");
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return TODO;
//        }
        return result;
//        return result && !EmptyUtil.isEmpty(telMgr.getLine1Number());
    }*/
} 