package com.utils;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;


import java.io.File;
import java.lang.reflect.Method;

import com.bean.SDCardBean;

/**
 * Created by：bobby on 2021-08-21 16:10.
 * Describe：内存工具
 */
public class StorageUtils {
    private static final int INTERNAL_STORAGE = 0;
    private static final int EXTERNAL_STORAGE = 1;
    public static final int STORAGE_AUDIO = 1;
    public static final int STORAGE_IMAGE = 2;
    public static final int STORAGE_VIDEO = 3;

    public StorageUtils() {
    }

    public static long getRAMTotal(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        return outInfo.totalMem;
    }

    public static long getRAMAvailable(Context context) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
        ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(outInfo);
        return outInfo.availMem;
    }

    public static long getInternalTotal() {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        return blockCountLong * blockSizeLong;
    }

    public static long getInternalAvailable() {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return availableBlocksLong * blockSizeLong;
    }

    public static long getExternalTotal() {
        if (isSDCardMount()) {
            File file = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(file.getPath());
            long blockSizeLong = statFs.getBlockSizeLong();
            long blockCountLong = statFs.getBlockCountLong();
            return blockCountLong * blockSizeLong;
        }else{
            return 0;
        }
    }

    public static long getExternalAvailable() {
        if (isSDCardMount()) {
            File file = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(file.getPath());
            long availableBlocksLong = statFs.getAvailableBlocksLong();
            long blockSizeLong = statFs.getBlockSizeLong();
            return availableBlocksLong * blockSizeLong;
        }else{
            return 0;
        }
    }

    /**
     * 外部存储是否可用 (存在且具有读写权限)
     */
    public static boolean isSDCardMount() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static boolean getStorageInfo(Context context, int type) {
        String path = getStoragePath(context, type);
        return isSDCardMount() && !TextUtils.isEmpty(path) && path != null;
    }

    public static String getStoragePath(Context context, int type) {
        StorageManager sm = (StorageManager)context.getSystemService("storage");

        try {
            Method getPathsMethod = sm.getClass().getMethod("getVolumePaths", String.class);
            String[] path = (String[])((String[])getPathsMethod.invoke(sm, Object.class));
            switch(type) {
                case 0:
                    return path[type];
                case 1:
                    if (path.length > 1) {
                        return path[type];
                    }

                    return null;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return null;
    }

    public static String getSDCardInfo() {
        SDCardBean sd = new SDCardBean();
        if (!isSDCardMount()) {
            return "SD card 未挂载!";
        } else {
            sd.isExist = true;
            StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
            sd.totalBlocks = sf.getBlockCountLong();
            sd.blockByteSize = sf.getBlockSizeLong();
            sd.availableBlocks = sf.getAvailableBlocksLong();
            sd.availableBytes = sf.getAvailableBytes();
            sd.freeBlocks = sf.getFreeBlocksLong();
            sd.freeBytes = sf.getFreeBytes();
            sd.totalBytes = sf.getTotalBytes();
            return sd.toString();
        }
    }

    public static String getSDCardTotalStorage(long totalByte) {
        double byte2GB = (double)totalByte / 1024.0D / 1024.0D / 1024.0D;
        double totalStorage;
        if (byte2GB > 1.0D) {
            totalStorage = Math.ceil(byte2GB);
            if (totalStorage > 1.0D && totalStorage < 3.0D) {
                return "2.0GB";
            }

            if (totalStorage > 2.0D && totalStorage < 5.0D) {
                return "4.0GB";
            }

            if (totalStorage >= 5.0D && totalStorage < 10.0D) {
                return "8.0GB";
            }

            if (totalStorage >= 10.0D && totalStorage < 18.0D) {
                return "16.0GB";
            }

            if (totalStorage >= 18.0D && totalStorage < 34.0D) {
                return "32.0GB";
            }

            if (totalStorage >= 34.0D && totalStorage < 50.0D) {
                return "48.0GB";
            }

            if (totalStorage >= 50.0D && totalStorage < 66.0D) {
                return "64.0GB";
            }

            if (totalStorage >= 66.0D && totalStorage < 130.0D) {
                return "128.0GB";
            }
        } else {
            totalStorage = (double)totalByte / 1024.0D / 1024.0D;
            if (totalStorage >= 515.0D && totalStorage < 1024.0D) {
                return "1GB";
            }

            if (totalStorage >= 260.0D && totalStorage < 515.0D) {
                return "512MB";
            }

            if (totalStorage >= 130.0D && totalStorage < 260.0D) {
                return "256MB";
            }

            if (totalStorage > 70.0D && totalStorage < 130.0D) {
                return "128MB";
            }

            if (totalStorage > 50.0D && totalStorage < 70.0D) {
                return "64MB";
            }
        }

        return totalStorage + "GB";
    }

    public static int getFilesByType(Context context, int type) {
        int count = 0;
        Cursor c = null;

        try {
            ContentResolver mContentResolver = context.getContentResolver();
            if (type == 1) {
                c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, (String[])null, (String)null, (String[])null, (String)null);
            } else if (type == 2) {
                c = mContentResolver.query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (String[])null, (String)null, (String[])null, (String)null);
            } else if (type == 3) {
                c = mContentResolver.query(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI, (String[])null, (String)null, (String[])null, (String)null);
            }

            while(c.moveToNext()) {
                ++count;
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }

        }

        return count;
    }

    public static String formatSize(long size) {
        String suffix = null;
        float fSize=0;

        if (size >= 1024) {
            suffix = "KB";
            fSize=size / 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
            }
            if (fSize >= 1024) {
                suffix = "GB";
                fSize /= 1024;
            }
        } else {
            fSize = size;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }



    /**
     * 获取内存总共空间
     * @return
     */
    public static String getTotalMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return byteToMB(mi.totalMem);
//        return byteToMB((long) Math.ceil(mi.totalMem));
    }

    /**
     * 获取手机内部空间总大小
     * @return
     */
    public static String getTotalInternalMemorySize(Context context) {
        //获取内部存储根目录
        File path = Environment.getDataDirectory();//Gets the Android data directory
        //系统的空间描述类
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();      //每个block 占字节数
        long totalBlocks = stat.getBlockCountLong();   //block总数
        return byteToMB(totalBlocks * blockSize);
//        return Formatter.formatFileSize(context,totalBlocks * blockSize);
    }

    //将字节数转化为MB
    private static String byteToMB(long size){
        long kb = 1024;
        long mb = kb*1024;

        float f = (float) size/mb;
        return String.format(f > 100 ?"%.0f MB":"%.1f MB",f);
    }
} 