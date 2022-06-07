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

  /*
    _id：短信序号，如100

    thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的

    address：发件人地址，即手机号，如+8613811810000

    person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null

    date：日期，long型，如1256539465022，可以对日期显示格式进行设置

    protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信

    read：是否阅读0未读，1已读

    status：短信状态-1接收，0complete,64pending,128failed

    type：短信类型1是接收到的，2是已发出

    body：短信具体内容
    */
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
                    //int nameColumn = cursor.getColumnIndex("person");
                    int contentColumn = cursor.getColumnIndex("body");
                    int dateColumn = cursor.getColumnIndex("date");
                    //type：短信类型1是接收到的，2是已发出
                    int typeColumn = cursor.getColumnIndex("type");
                    //read：是否阅读0未读，1已读
                    int readColumn = cursor.getColumnIndex("read");

                    do{
                        smsId = StringUtils.isEmptyString(cursor.getString(smsIdColumn));
                        //name = StringUtils.isEmptyString(cursor.getString(nameColumn));
                        phone = StringUtils.isEmptyString(cursor.getString(phoneColumn));
                        name = getPeopleNameFromPerson(context,phone);
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
                        MessageBean messageBean = null;
                        if(time > 0){
                            messageBean = new MessageBean(name,phone,type,read,time,DateUtil.DateToLong(time),content,smsId);
                        }else{
                            messageBean = new MessageBean(name,phone,type,read,time,"",content,smsId);
                        }
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


    /**
     * 通过address手机号关联Contacts联系人的显示名字
     * @param address
     * @return
     */
    private String getPeopleNameFromPerson(Context context,String address){
        if(address == null || address.equals("")){
            return null;
        }

        String strPerson = "null";
        String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};

        Uri uri_Person = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, address);  // address 手机号过滤
        Cursor cursor = context.getContentResolver().query(uri_Person, projection, null, null, null);

        if(cursor.moveToFirst()){
            int index_PeopleName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String strPeopleName = cursor.getString(index_PeopleName);
            strPerson = strPeopleName;
        }
        else{
            strPerson = address;
        }
        cursor.close();
        cursor=null;
        return strPerson;
    }


    public List<MessageBean> getMessageInfosTwo(Context context) {
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
                            if(messageInfo.time > 0) {
                                messageInfo.sendTime = DateUtil.DateToLong(messageInfo.time);
                            }
                            messageInfo.message = smsbody;
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