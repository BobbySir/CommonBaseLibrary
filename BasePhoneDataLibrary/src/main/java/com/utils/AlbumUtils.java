package com.utils;

import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.List;

import com.bean.AlbumBean;

/**
 * Created by：bobby on 2021-08-19 17:02.
 * Describe：相册工具类
 */
public class AlbumUtils {
    private static AlbumUtils instance;

    public static AlbumUtils getInstance(){
        if(instance == null){
            synchronized(AlbumUtils.class){
                if(instance == null){
                    instance = new AlbumUtils();
                }
            }
        }
        return instance;
    }

    //获取所有相册信息
    public List<AlbumBean> getAlbumBeans(Context context) {
        List<AlbumBean> listData = new ArrayList();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (String[])null, (String)null, (String[])null, (String)null);

            while(true) {
                String name;
                String path;
                String tag;
                do {
                    if (!cursor.moveToNext()) {
                        return listData;
                    }

                    name = cursor.getString(cursor.getColumnIndex("_display_name"));
                    byte[] data = cursor.getBlob(cursor.getColumnIndex("_data"));
                    path = new String(data, 0, data.length - 1);
                    tag = path.toLowerCase();
                } while(!tag.contains("/dcim/camera/") && !tag.contains("/dcim/100media/") && !tag.contains("/dcim/100andro/"));

                AlbumBean bean = initImgData(name, path);
                if (bean != null) {
                    int low_author = cursor.getColumnIndex("author");
                    if (TextUtils.isEmpty(bean.make) && low_author >= 0) {
                        bean.make = cursor.getString(low_author);
                    }

                    listData.add(bean);
                }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listData;
    }

    private  AlbumBean initImgData(String name, String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            String smodel = exifInterface.getAttribute("Model");
            String smake = exifInterface.getAttribute("Make");
            String swidth = exifInterface.getAttribute("ImageWidth");
            String sheight = exifInterface.getAttribute("ImageLength");
            String sdate = exifInterface.getAttribute("DateTime");
            long time = 0; //拍摄时间
            if (!TextUtils.isEmpty(sdate) && sdate.length() > 5) {
                //处理时间格式
                if(sdate.contains(".")){
                    time = DateUtil.date2TimeStamp(sdate, "yyyy.MM.dd HH:mm:ss");
                }else if(sdate.contains("/")){
                    time = DateUtil.date2TimeStamp(sdate, "yyyy/MM/dd HH:mm:ss");
                }else if(sdate.contains("-")){
                    time = DateUtil.date2TimeStamp(sdate, "yyyy-MM-dd HH:mm:ss");
                }else if(sdate.contains(":")){
                    time = DateUtil.date2TimeStamp(sdate, "yyyy:MM:dd HH:mm:ss");
                }
//                String[] date = sdate.split(" ");
//                if (date.length == 2) {
//                    sdate = date[0].replace(":", "-") + " " + date[1];
//                }
            }

            String slatitude = exifInterface.getAttribute("GPSLatitude");
            String slatitudeRef = exifInterface.getAttribute("GPSLatitudeRef");
            String latitudeG = String.valueOf(LatLonRational2FloatConverter.convertRationalLatLonToFloat(slatitude, slatitudeRef));
            String slongitude = exifInterface.getAttribute("GPSLongitude");
            String slongitudeRef = exifInterface.getAttribute("GPSLongitudeRef");
            String longitudeG = String.valueOf(LatLonRational2FloatConverter.convertRationalLatLonToFloat(slongitude, slongitudeRef));
            AlbumBean AlbumBean = new AlbumBean();
            AlbumBean.time = time;
            AlbumBean.height = StringUtils.isIntString(sheight);
            AlbumBean.width = StringUtils.isIntString(swidth);
            AlbumBean.name = name;
            AlbumBean.latitude = StringUtils.isDoubleString(latitudeG);
            AlbumBean.longitude = StringUtils.isDoubleString(longitudeG);
            AlbumBean.model = smodel;
            AlbumBean.make = smake;
            return AlbumBean;
        } catch (Exception var15) {
            var15.printStackTrace();
            return null;
        }
    }

} 