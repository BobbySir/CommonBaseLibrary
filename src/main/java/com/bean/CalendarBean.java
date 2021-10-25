package com.bean;

import java.io.Serializable;

/**
 * Created by：bobby on 2021-08-19 16:41.
 * Describe：日历信息
 */
public class CalendarBean implements Serializable {
    public String eventId;   //事件ID
    public long endTime;     //事件开始时间（13位毫秒）
    public long startTime;   //事件结束时间（13位毫秒）
    public String eventTitle;//事件标题
    public String description;//事件描述
} 