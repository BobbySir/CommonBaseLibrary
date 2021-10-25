package com.bean;

import java.io.Serializable;

/**
 * Created by：bobby on 2021-08-19 16:38.
 * Describe：相册对象
 */
public class AlbumBean implements Serializable {
    public String name;  //照片文件名
    public String model; //拍摄设备名称（基于Exif信息
    public String make;	 //拍摄者（基于Exif信息）
    public long time;	 //拍摄时间（13位毫秒）
    public int width;    //照片宽度
    public int height;   //照片高度
    public double longitude;//经度（基于Exif信息）
    public double latitude; //纬度（基于Exif信息）

    @Override
    public String toString() {
        return "AlbumBean{" +
                "name='" + name + '\'' +
                ", model='" + model + '\'' +
                ", make='" + make + '\'' +
                ", time=" + time +
                ", width=" + width +
                ", height=" + height +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}