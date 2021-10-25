package com.utils;

import android.text.TextUtils;

/**
 * Created by：bobby on 2021-10-25 18:20.
 * Describe：
 */
public class LatLonRational2FloatConverter {
    public LatLonRational2FloatConverter() {
    }

    public static float convertRationalLatLonToFloat(String rationalString, String ref) {
        if (!TextUtils.isEmpty(rationalString) && !TextUtils.isEmpty(ref)) {
            try {
                String[] parts = rationalString.split(",");
                String[] pair = parts[0].split("/");
                double degrees = parseDouble(pair[0].trim(), 0.0D) / parseDouble(pair[1].trim(), 1.0D);
                pair = parts[1].split("/");
                double minutes = parseDouble(pair[0].trim(), 0.0D) / parseDouble(pair[1].trim(), 1.0D);
                pair = parts[2].split("/");
                double seconds = parseDouble(pair[0].trim(), 0.0D) / parseDouble(pair[1].trim(), 1.0D);
                double result = degrees + minutes / 60.0D + seconds / 3600.0D;
                return !"S".equals(ref) && !"W".equals(ref) ? (float)result : (float)(-result);
            } catch (NumberFormatException var12) {
                return 0.0F;
            } catch (ArrayIndexOutOfBoundsException var13) {
                return 0.0F;
            } catch (Throwable var14) {
                return 0.0F;
            }
        } else {
            return 0.0F;
        }
    }

    private static double parseDouble(String doubleValue, double defaultValue) {
        try {
            return Double.parseDouble(doubleValue);
        } catch (Throwable var4) {
            return defaultValue;
        }
    }
} 