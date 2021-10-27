package com.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;


import java.util.ArrayList;
import java.util.List;

import com.bean.ContactBean;

/**
 * Created by：bobby on 2021-08-21 13:59.
 * Describe：
 */
public class ContactUtils {
    private static ContactUtils instance;

    public static ContactUtils getInstance(){
        if(instance == null){
            synchronized(ContactUtils.class){
                if(instance == null){
                    instance = new ContactUtils();
                }
            }
        }
        return instance;
    }

    //获取手机联系人（通讯录）列表信息
    public List<ContactBean> getContactList(Context context){
        List<ContactBean> contactBeans = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if(cursor != null ){
                int nameColumn;	 //姓名
                int phoneColumn; //手机号码
                int updateTimeColumn; //更新时间（13位毫秒）
                int timesContactedColumn; //联系次数
                int lastTimeContactedColumn; //与联系人最后联系时间（13位毫秒）
                int sourceColumn; //联系人来源 1：设备 2：sim卡

                nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                phoneColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                updateTimeColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP);
                timesContactedColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED);
                lastTimeContactedColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED);
                sourceColumn = cursor.getColumnIndex("account_type");

                while (cursor.moveToNext() && cursor.getCount() > 0) {
                    try {
                        ContactBean contactBean = new ContactBean();
                        contactBean.name = cursor.getString(nameColumn);
                        contactBean.phone = cursor.getString(phoneColumn);
                        contactBean.updateTime = cursor.getLong(updateTimeColumn);
                        if(contactBean.updateTime > 0) {
                            contactBean.inputTime = DateUtil.DateToLong(contactBean.updateTime);
                        }
                        contactBean.timesContacted = cursor.getInt(timesContactedColumn);
                        contactBean.lastTimeContacted = cursor.getLong(lastTimeContactedColumn);
                        contactBean.source = cursor.getString(sourceColumn).contains("sim") ? 2 : 1;
                        contactBeans.add(contactBean);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            // 获取sim卡的联系人--1
           /* try {
                getSimContact(context,"content://icc/adn", contactBeans);
                getSimContact(context,"content://sim/adn", contactBeans);

                getSimContact(context,"content://icc/adn/subId/#", contactBeans);

                getSimContact(context,"content://icc/sdn", contactBeans);

                getSimContact(context,"content://icc/sdn/subId/#", contactBeans);

                getSimContact(context,"content://icc/fdn", contactBeans);

                getSimContact(context,"content://icc/fdn/subId/#", contactBeans);

            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return contactBeans;
    }

    //获取SIM卡联系人信息
    public void getSimContact(Context context,String adn, List<ContactBean> list) {
        // 读取SIM卡手机号,有三种可能:content://icc/adn || content://icc/sdn || content://icc/fdn
        // 具体查看类 IccProvider.java
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse(adn);
            LogUtils.e(uri.toString());

            cursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext() && cursor.getCount() > 0) {
                    // 取得联系人名字
                    int nameIndex = cursor.getColumnIndex(Contacts.People.NAME);
                    // 取得电话号码
                    int numberIndex = cursor.getColumnIndex(Contacts.People.NUMBER);
                    //与联系人最后联系时间（13位毫秒
                  //  int lastTimeContactedIndex = cursor.getColumnIndex(Contacts.People.LAST_TIME_CONTACTED);
                    //联系次数
//                    int timesContactedIndex = cursor.getColumnIndex(Contacts.People.TIMES_CONTACTED);

                    String phone = cursor.getString(numberIndex);
                    String name = cursor.getString(nameIndex);
//                    int timesContacted = cursor.getInt(timesContactedIndex);
                   // long lastTimeContacted = cursor.getLong(lastTimeContactedIndex);

                    ContactBean simCardTemp = new ContactBean();
                    simCardTemp.phone = phone;
                    simCardTemp.name = name;
//                    simCardTemp.timesContacted = timesContacted;
//                    simCardTemp.lastTimeContacted = lastTimeContacted;
                    simCardTemp.source = 2;

                    if (!list.contains(simCardTemp)) {
                        list.add(simCardTemp);
                    }
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        }
    }
} 