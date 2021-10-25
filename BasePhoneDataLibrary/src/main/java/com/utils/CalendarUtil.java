package com.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.bean.CalenderDataBean;

/**
 * Created by：bobby on 2021-08-19 14:30.
 * Describe：日历日程时间
 */
public class CalendarUtil {
    //日历列表
    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    //日历事件
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    //日历提醒
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";

    //获取日历列表数据
    public static ArrayList<CalenderDataBean> getCalenderDataList(Context context){
        String startTime = "";
        String endTime = "";
        String eventTitle = "";
        String description = "";
        String location = "";
        String week = "";
        String eventId = "";
        ArrayList<CalenderDataBean> arr=new ArrayList<CalenderDataBean>();
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null,
                null, null,  "dtstart"+" DESC");
        int count = 0;
        while (eventCursor.moveToNext()){

            eventId = eventCursor.getString(eventCursor.getColumnIndex("_id"));
            eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
            description = eventCursor.getString(eventCursor.getColumnIndex("description"));
            location = eventCursor.getString(eventCursor.getColumnIndex("eventLocation"));
            String aa =  eventCursor.getString(eventCursor.getColumnIndex("dtstart"));
            count++;
            LogUtils.e("TabMainActivity" + aa  +"  id== " + eventId   +"____" + eventCursor.getString(eventCursor.getColumnIndex("calendar_id")));

            //如果开始时间和结束时间>0的话
            if(eventCursor.getLong(eventCursor.getColumnIndex("dtstart")) > 0 && eventCursor.getLong(eventCursor.getColumnIndex("dtend")) > 0) {
                startTime = timeStamp2Date(eventCursor.getLong(eventCursor.getColumnIndex("dtstart")));
                endTime = timeStamp2Date(eventCursor.getLong(eventCursor.getColumnIndex("dtend")));

                week = ""+ (getWeek(startTime));
                CalenderDataBean item = new CalenderDataBean(eventTitle, startTime, endTime,description, location,week);

                arr.add(item);
            }

        }
        LogUtils.e("原先多少日程：" + count);
        return arr;
    }
    /**
     * 时间戳转换为字符串
     * @param time:时间戳
     * @return
     */
    private static String timeStamp2Date(long time) {
        String format = "yyyy-MM-dd HH:mm:ss";
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(time));
    }

    /**
     * 判断当前日期是星期几
     *
     * @param  pTime     设置的需要判断的时间  //格式如2012-09-08
     *
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

//  String pTime = "2012-03-12";
    private static int getWeek(String pTime) {


        int Week = 0;

        if(!TextUtils.isEmpty(pTime)) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar c = Calendar.getInstance();
            try {

                c.setTime(format.parse(pTime));

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                Week = 0;
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 2) {
                Week = 1;
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 3) {
                Week = 2;
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 4) {
                Week = 3;
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 5) {
                Week = 4;
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 6) {
                Week = 5;
            }
            if (c.get(Calendar.DAY_OF_WEEK) == 7) {
                Week = 6;
            }
        }
        return Week;
    }
}
