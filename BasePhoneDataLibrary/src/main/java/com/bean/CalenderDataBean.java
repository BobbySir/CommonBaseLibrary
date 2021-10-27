package com.bean;

/**
 * Created by：bobby on 2021-08-19 14:33.
 * Describe：
 */
public class CalenderDataBean {
    //事件ID
    public String eventId;
    //事件标题
    public String eventTitle = "";
    //开始时间
    public String startTime = "";
    //结束时间
    public String endTime = "";
    //事件描述
    public String description = "";
    //日程事件位置
    public String location = "";
    //指定时间时间星期几
    public String week = "";

    public CalenderDataBean(String eventId,String eventTitle, String startTime, String endTime, String description, String location, String week) {
        this.eventId = eventId;
        this.eventTitle = eventTitle;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.location = location;
        this.week = week;
    }

    @Override
    public String toString() {
        return eventTitle + startTime + endTime + description + location +"\n";
    }
}