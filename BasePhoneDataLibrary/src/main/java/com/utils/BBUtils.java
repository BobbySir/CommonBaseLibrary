package com.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;


import java.util.ArrayList;
import java.util.List;

import com.bean.MessageBean;

/**
 * Created by：bobby on 2021-08-20 17:28.
 * Describe：
 */
public class BBUtils {

    public static List<MessageBean> getSmsInfos(Context context) {
        List<MessageBean> list = new ArrayList();
        Cursor cursor = null;

        label138: {
            try {
                ContentResolver cr = context.getContentResolver();
                String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "read"};
                String SMS_URI_ALL = "content://sms/";
                Uri uri = Uri.parse(SMS_URI_ALL);
                cursor = cr.query(uri, projection, null, null, "date desc");
                if (cursor != null) {
                    while(true) {
                        int dateColumn;
                        int readColumn;
                        String name;
                        String phoneNumber;
                        String smsbody;
                        String typeStr;
                        String smsId;
                        MessageBean messageInfo;
                        while(true) {
                            if (!cursor.moveToNext()) {
                                break label138;
                            }

                            messageInfo = new MessageBean();
                            int nameColumn = cursor.getColumnIndex("person");
                            int phoneNumberColumn = cursor.getColumnIndex("address");
                            int smsbodyColumn = cursor.getColumnIndex("body");
                            dateColumn = cursor.getColumnIndex("date");
                            int typeColumn = cursor.getColumnIndex("type");
                            readColumn = cursor.getColumnIndex("read");
                            int smsIdColumn = cursor.getColumnIndex("_id");
                            name = cursor.getString(nameColumn);
                            phoneNumber = cursor.getString(phoneNumberColumn);
                            smsbody = cursor.getString(smsbodyColumn);
                            typeStr = cursor.getString(typeColumn);
                            smsId = cursor.getString(smsIdColumn);
                            if (!TextUtils.isEmpty(name) || TextUtils.isEmpty(phoneNumber)) {
                                break;
                            }

                            //Uri personUri = Uri.withAppendedPath(Contacts.Phones.CONTENT_FILTER_URL, phoneNumber);
                            //Cursor localCursor = context.getContentResolver().query(personUri, PHONE_PROJECTION, null, null, null);

                            //Uri personUri = Uri.withAppendedPath(Uri.withAppendedPath(Uri.parse(TypeUtils.getContentPhoneLookup()), "phone_lookup"), phoneNumber);
                            //Cursor localCursor = cr.query(personUri, new String[]{"display_name", "photo_id", "_id"}, (String)null, (String[])null, (String)null);

                            ContentResolver localCursor = context.getContentResolver();
                            Uri uria = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, phoneNumber);
                            Cursor c = localCursor.query(uria , new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                            String myName = "";
                            if(c.moveToNext()){
                                 myName  = c.getString(0);
                                 LogUtils.e("myname==" + myName);
                                c.close();
                                break;
                            }

//                            if (localCursor != null) {
//                                if (localCursor.getCount() != 0) {
//                                    localCursor.moveToFirst();
//                                    name = localCursor.getString(localCursor.getColumnIndex("display_name"));
//                                }
//
//                                localCursor.close();
//                                break;
//                            }

                            messageInfo.type = Integer.parseInt(typeStr);
                            messageInfo.phone = TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber;
                            messageInfo.time = cursor.getLong(dateColumn);
                            messageInfo.content = smsbody;
                            messageInfo.read = cursor.getInt(readColumn);
                            messageInfo.name = TextUtils.isEmpty(name) ? "" : name;
                            messageInfo.smsId = smsId;
                            LogUtils.e("获取到的内容：" + messageInfo.toString());
                            list.add(messageInfo);
                        }
                    }
                }

            } catch (Exception var25) {
                var25.printStackTrace();
                break label138;
            } finally {
                if (cursor != null) {
                    cursor.close();
                }

            }

        }

        return list;
    }
} 