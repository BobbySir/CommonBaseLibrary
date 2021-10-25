package com.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;



/**
 * Created by：bobby on 2021-08-21 17:29.
 * Describe：手机电池相关工具类
 */
public class BatteryUtils {
    private Context mContext;
    private Action mAction1;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            BatteryUtils.BatteryData data = BatteryUtils.this.new BatteryData();
            int rawlevel = intent.getIntExtra("level", -1);
            int scale = intent.getIntExtra("scale", -1);
            int status = intent.getIntExtra("status", -1);
            int plugged = intent.getIntExtra("plugged", 0);
            int level = 0;
            int max = 0;
            if (rawlevel >= 0 && scale > 0) {
                level = rawlevel * 100 / scale;
            }

            if (scale > 0) {
                max = 100 / scale;
            }

            data.level = level;
            data.status = status;
            data.max = max;
            BatteryUtils.this.mAction1.call(data);
            BatteryUtils.this.mContext.unregisterReceiver(BatteryUtils.this.mReceiver);
        }
    };


    public BatteryUtils(Context context, Action<BatteryData> action) {
        this.mContext = context;
        if (Build.VERSION.SDK_INT >= 21) {
            BatteryManager batteryManager = (BatteryManager)context.getSystemService("batterymanager");
            BatteryUtils.BatteryData data = new BatteryUtils.BatteryData();
            data.level = batteryManager.getIntProperty(4);
            data.status = batteryManager.getIntProperty(6);
            data.max = batteryManager.getIntProperty(1);
            action.call(data);
        } else {
            IntentFilter filter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
            context.registerReceiver(this.mReceiver, filter);
        }

    }

    public class BatteryData {
        public int status;
        public int level;
        public int max;
        public int plugged;

        public BatteryData() {
        }
    }
} 