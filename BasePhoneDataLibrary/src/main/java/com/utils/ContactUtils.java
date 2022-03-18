package com.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.bean.ContactBean;

import org.json.JSONException;

/**
 * Created by：bobby on 2021-08-21 13:59.
 * Describe：
 */
public class ContactUtils {
    private static ContactUtils instance;

    public static ContactUtils getInstance() {
        if (instance == null) {
            synchronized (ContactUtils.class) {
                if (instance == null) {
                    instance = new ContactUtils();
                }
            }
        }
        return instance;
    }

    //获取手机联系人（通讯录）列表信息
    public List<ContactBean> getContactList(Context context) {
        List<ContactBean> contactBeans = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null) {
                int nameColumn;     //姓名
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
                        if (contactBean.updateTime > 0) {
                            contactBean.inputTime = DateUtil.DateToLong(contactBean.updateTime);
                        }
                        contactBean.timesContacted = cursor.getInt(timesContactedColumn);
                        contactBean.lastTimeContacted = cursor.getLong(lastTimeContactedColumn);
                        contactBean.source = cursor.getString(sourceColumn).contains("sim") ? 2 : 1;
                        contactBeans.add(contactBean);
                    } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contactBeans;
    }

    //获取SIM卡联系人信息
    public void getSimContact(Context context, String adn, List<ContactBean> list) {
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


    public List<ContactBean> getContactInfo(Context context) throws JSONException {
        // 获得通讯录信息 ，URI是ContactsContract.Contacts.CONTENT_URI
        List<ContactBean> list = new ArrayList<>();
        ContactBean contactBean = null;
        String mimetype = "";
        int oldrid = -1;
        int contactId = -1;
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, null, null, ContactsContract.Data.RAW_CONTACT_ID);
        int numm = 0;
        if (cursor == null) return null;
        else {
            if (cursor.moveToFirst()) {
                do {
                    contactId = cursor.getInt(cursor.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID));
                    if (oldrid != contactId) {
                        contactBean = new ContactBean();

                        list.add(contactBean);
                        numm++;
                        oldrid = contactId;
                    }

                    // 取得mimetype类型
                    mimetype = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));
                    contactBean.updateTime = cursor.getLong(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP));
                    if (contactBean.updateTime > 0) {
                        contactBean.inputTime = DateUtil.DateToLong(contactBean.updateTime);
                    }
                    LogUtils.e(contactBean.inputTime + "---联系人：" + mimetype);
                    // 获得通讯录中每个联系人的ID
                    // 获得通讯录中联系人的名字
                    if (ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE.equals(mimetype)) {
                        String display_name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
                        contactBean.name = display_name;
                        String prefix = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX));
//                        jsonObject.put("prefix", prefix);
                        String firstName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
//                        jsonObject.put("firstName", firstName);
                        contactBean.firstName = firstName;
                        String middleName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
//                        jsonObject.put("middleName", middleName);
                        contactBean.middleName = middleName;
                        String lastname = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
//                        jsonObject.put("lastname", lastname);
                        contactBean.lastName = lastname;
                        String suffix = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX));
//                        jsonObject.put("suffix", suffix);
                        String phoneticFirstName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME));
//                        jsonObject.put("phoneticFirstName", phoneticFirstName);
                        String phoneticMiddleName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME));
//                        jsonObject.put("phoneticMiddleName", phoneticMiddleName);
                        String phoneticLastName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME));
//                        jsonObject.put("phoneticLastName", phoneticLastName);
                    }
                    // 获取电话信息
                    if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE.equals(mimetype)) {
                        // 取出电话类型
                        int phoneType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
//                        Log.e(TAG, "getContactInfo: phoneType = " + phoneType);
                        // 手机
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                            String mobile = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            jsonObject.put("mobile", mobile);
                            contactBean.phone = mobile;
                        }
//                        // 住宅电话
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_HOME) {
                            String homeNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            jsonObject.put("homeNum", homeNum);
                            if (TextUtils.isEmpty(contactBean.phone)) {
                                contactBean.phone = homeNum;
                            }
                        }
//                        // 单位电话
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_WORK) {
                            String jobNum = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            jsonObject.put("jobNum", jobNum);
                            if (TextUtils.isEmpty(contactBean.phone)) {
                                contactBean.phone = jobNum;
                            }
                        }
//                        // 单位传真
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK) {
                            String workFax = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            jsonObject.put("workFax", workFax);
                            if (TextUtils.isEmpty(contactBean.phone)) {
                                contactBean.phone = workFax;
                            }
                        }
//                        // 住宅传真
                        if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME) {
                            String homeFax = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            jsonObject.put("homeFax", homeFax);
                            if (TextUtils.isEmpty(contactBean.phone)) {
                                contactBean.phone = homeFax;
                            }
                        }
                    }
//                    // 获取备注信息
                    if (ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE.equals(mimetype)) {
                        String remark = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
//                        jsonObject.put("remark", remark);
                        contactBean.phoneLabel = remark;
                    }
//                    // 获取昵称信息
//                    if (ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE.equals(mimetype)) {
//                        String nickName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
//                        jsonObject.put("nickName", nickName);
//                    }
                    // 获取组织信息
                    if (ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE.equals(mimetype)) {
                        // 取出组织类型
                        int orgType = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE));
                        // 单位
                        if (orgType == ContactsContract.CommonDataKinds.Organization.TYPE_CUSTOM) {
                            //             if (orgType == Organization.TYPE_WORK) {
                            String company = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY));
//                            jsonObject.put("company", company);
                            contactBean.companyName = company;
                            String jobTitle = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
//                            jsonObject.put("jobTitle", jobTitle);
                            contactBean.jobTitle = jobTitle;
//                            String department = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DEPARTMENT));
//                            jsonObject.put("department", department);
                        }
                    }
                    LogUtils.e("通讯：" + contactBean.name + "  ---  " + contactBean.phone);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        LinkedHashSet<ContactBean> hashSet = new LinkedHashSet<>(list);
        ArrayList<ContactBean> repeatList = new ArrayList<>(hashSet);
//        System.out.println(listWithoutDuplicates);
        List<ContactBean> list2 = new ArrayList<>();
        if (repeatList.size() > 0) {
            for (int i = 0; i < repeatList.size(); i++) {
                if (!TextUtils.isEmpty(repeatList.get(i).phone)) {
                    list2.add(repeatList.get(i));
                }
            }
        }

        List<ContactBean> contactList = getContactList(context);
        LinkedHashSet<ContactBean> contactHashSet = new LinkedHashSet<>(contactList);
        List<ContactBean> contactRepeatList = new ArrayList<>(contactHashSet);
        list2.addAll(contactRepeatList);
        LinkedHashSet<ContactBean> listHashSet = new LinkedHashSet<>(list2);
        List<ContactBean> listRepeatList = new ArrayList<>(listHashSet);

        return listRepeatList;
    }
} 