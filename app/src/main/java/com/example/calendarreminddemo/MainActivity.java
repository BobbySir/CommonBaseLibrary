package com.example.calendarreminddemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


import java.util.List;

import com.bean.CalenderDataBean;
import com.utils.CalendarUtil;
import com.utils.LogUtils;
import com.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    private String titlte = "测试添加日历标题";
    private String description = "测试添加日历内容";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initCalendar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            initCalendar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                //有权限
                initCalendar();
            }else{
                PermissionUtils.showDialog(this, getString(R.string.need_camera_stogafe_permission));
            }
        }
    }

    private void initCalendar(){
        //检查日历权限
        if(PermissionUtils.hasPermission(this, new String[]{Manifest.permission.READ_CALENDAR})){
//            CalendarReminderUtils.addCalendarEvent(this, titlte, description, 0,0,8);
            List<CalenderDataBean> beanList = CalendarUtil.getCalenderDataList(this);
            LogUtils.e("有多少日程：" + beanList.size());
            for(int i = 1 ; i< beanList.size()-1; i ++){
                LogUtils.e("总日程：" + beanList.size() +"  当前第" + i + "个日程信息= " + beanList.get(i).toString());
            }
//            for (CalenderDataBean com.bean : beanList){
//                LogUtils.e("日程：" );
//            }
        }else{
            PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_CALENDAR}, 101);
        }
    }
}