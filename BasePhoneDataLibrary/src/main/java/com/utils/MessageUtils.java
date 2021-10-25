package com.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


import java.util.ArrayList;
import java.util.List;

import com.bean.MessageBean;

/**
 * Created by：bobby on 2021-08-20 14:08.
 * Describe：
 */
public class MessageUtils {
    private static MessageUtils instance;

    public static MessageUtils getInstance(){
        if(instance == null){
            synchronized(MessageUtils.class){
                if(instance == null){
                    instance = new MessageUtils();
                }
            }
        }
        return instance;
    }

    public List<MessageBean> getMessageInfos(Context context){
        List<MessageBean> messageBeans = new ArrayList<>();
        Cursor cursor = null;
        String SMS_URI_ALL = "content://sms/";
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type", "read"};
        Uri uri = Uri.parse(SMS_URI_ALL);
        try {
            cursor = context.getContentResolver().query(uri,projection,null,null,"date desc");
            if(cursor !=null){
                if(cursor.moveToFirst()){
                    String name;     //收发人
                    String phone;    //手机号码
                    int type;	    //收发标识 1：发送、2：接收
                    int read;        //是否已读 0：未读、1：已读
                    long time;       //短信时间（13位毫秒）
                    String content;  //短信内容
                    String  smsId;   //短信ID

                    //ID
                    int smsIdColumn = cursor.getColumnIndex("_id");
                    int phoneColumn = cursor.getColumnIndex("address");
                    int nameColumn = cursor.getColumnIndex("person");
                    int contentColumn = cursor.getColumnIndex("body");
                    int dateColumn = cursor.getColumnIndex("date");
                    //type：短信类型1是接收到的，2是已发出
                    int typeColumn = cursor.getColumnIndex("type");
                    //read：是否阅读0未读，1已读
                    int readColumn = cursor.getColumnIndex("read");

                    do{
                        smsId = StringUtils.isEmptyString(cursor.getString(smsIdColumn));
                        name = StringUtils.isEmptyString(cursor.getString(nameColumn));
                        phone = StringUtils.isEmptyString(cursor.getString(phoneColumn));
                        content = StringUtils.isEmptyString(cursor.getString(contentColumn));
                        type = cursor.getInt(typeColumn);
                        read = cursor.getInt(readColumn);
                        time = cursor.getLong(dateColumn);


                        Uri personUri = Uri.withAppendedPath(Uri.withAppendedPath(Uri.parse(TypeUtils.getContentPhoneLookup()), "phone_lookup"), phone);
                        Cursor localCursor = context.getContentResolver().query(personUri, new String[]{"display_name", "photo_id", "_id"}, (String)null, (String[])null, (String)null);
                        if (localCursor != null) {
                            if (localCursor.getCount() != 0) {
                                localCursor.moveToFirst();
                                name = localCursor.getString(localCursor.getColumnIndex("display_name"));
                            }

                            localCursor.close();
                        }

                        MessageBean messageBean = new MessageBean(name,phone,type,read,time,content,smsId);
                        messageBeans.add(messageBean);
                    }while (cursor.moveToNext() && cursor.getCount() > 0);

                }else{
                    cursor.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return messageBeans;
    }
} 