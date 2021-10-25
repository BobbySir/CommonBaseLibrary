package com.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import java.util.List;

/**
 * Created by：bobby on 2021-08-21 17:24.
 * Describe：
 */
public class LocationUtils {

    public static LocationUtils.GsmData getCellLac(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService("phone");
        @SuppressLint("MissingPermission") CellLocation location = tm.getCellLocation();
        LocationUtils.GsmData data = new LocationUtils.GsmData();
        if (location instanceof CdmaCellLocation) {
            CdmaCellLocation var4 = (CdmaCellLocation)location;
        } else if (location instanceof GsmCellLocation) {
            GsmCellLocation l1 = (GsmCellLocation)location;
            data.cellId = l1.getCid();
            data.lac = l1.getLac();
        }

        return data;
    }

    public static LocationUtils.LocationData getLocationInfo(Context context) {
        LocationUtils.LocationData location = new LocationUtils.LocationData();
        LocationManager locationManager = (LocationManager)context.getSystemService("location");
        List<String> providerList = locationManager.getProviders(true);
        String provider;
        if (providerList.contains("network")) {
            provider = "network";
        } else {
            if (!providerList.contains("gps")) {
                return location;
            }

            provider = "gps";
        }

        @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
        if (l != null) {
            location.latitude = l.getLatitude();
            location.longitude = l.getLongitude();
        }

        return location;
    }

    public static class LocationData {
        public double longitude;
        public double latitude;

        public LocationData() {
        }
    }

    public static class GsmData {
        public int cellId;
        public int lac;

        public GsmData() {
        }
    }
} 