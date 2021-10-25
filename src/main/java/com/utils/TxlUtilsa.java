package com.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;


import java.util.ArrayList;

import com.bean.ContactBean;

/**
 * Created by：bobby on 2021-08-23 15:05.
 * Describe：
 */
public class TxlUtilsa {

    public static ArrayList<ContactBean> getContentTxls(Context context) {
        ArrayList<ContactBean> txls = new ArrayList();
        Cursor cursor = null;
        ContentResolver cr = context.getContentResolver();

        try {
            cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[])null, (String)null, (String[])null, "sort_key");
//            cursor = cr.query(Uri.parse(TypeUtils.getContentData()), (String[])null, (String)null, (String[])null, "sort_key");
            if (cursor != null) {
                int displayNameIndex = cursor.getColumnIndex("display_name");
                int mobileNoIndex = cursor.getColumnIndex("data1");
                int updateTimeIndex = cursor.getColumnIndex("contact_last_updated_timestamp");
                int times_contactedIndex = cursor.getColumnIndex("times_contacted");
                int last_time_contactedIndex = cursor.getColumnIndex("last_time_contacted");
                int accountTypeIndex = cursor.getColumnIndex("account_type");

                while(cursor.moveToNext()) {
                    try {
                        ContactBean txl = new ContactBean();
                        String mobileNo = cursor.getString(mobileNoIndex);
                        String displayName = cursor.getString(displayNameIndex);
                        int times_contacted = cursor.getInt(times_contactedIndex);
                        long last_time_contacted = cursor.getLong(last_time_contactedIndex);
                        String accountType = cursor.getString(accountTypeIndex);
                        txl.name = displayName;
                        txl.phone = mobileNo;
                        txl.updateTime = cursor.getLong(updateTimeIndex);
                        txl.lastTimeContacted = last_time_contacted;
                        txl.timesContacted = times_contacted;
                        txl.source = accountType.contains("sim") ? 2 : 1;
                        txls.add(txl);
                    } catch (Exception var22) {
                    }
                }
            }
        } catch (Exception var23) {
            var23.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }

        return txls;
    }
} 