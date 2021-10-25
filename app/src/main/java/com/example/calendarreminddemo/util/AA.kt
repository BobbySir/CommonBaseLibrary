package com.example.calendarreminddemo.util

import android.content.Context
import android.net.Uri
import com.ys.dc.base.utils.TypeUtils
import org.json.JSONArray
import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by：bobby on 2021-08-20 16:47.
 * Describe：
 */
object AA{

    //===================================获取短信内容=====================================
    @JvmStatic
    fun readSms(context: Context) {
        val mJSONArray = JSONArray()
        val mSmsMeeageMap = HashMap<String, Any>()
        val SMS_URI_ALL = "content://sms/"
        val cr = context.contentResolver
        val projection = arrayOf("_id", "address", "person", "body", "date", "type", "read")
        val uri = Uri.parse(SMS_URI_ALL)
        val cur = cr.query(uri, projection, null, null, "date desc") ?: return
        if (cur.moveToFirst()) {
            var name: String
            var phoneNumber: String
            var smsbody: String
            var date: String
            var read : String

            val nameColumn = cur.getColumnIndex("person")
            val phoneNumberColumn = cur.getColumnIndex("address")
            val smsbodyColumn = cur.getColumnIndex("body")
            val dateColumn = cur.getColumnIndex("date")
            //type：短信类型1是接收到的，2是已发出
            val typeColumn = cur.getColumnIndex("type")
            //read：是否阅读0未读，1已读
            val readColumn = cur.getColumnIndex("read")

            do {
                name = com.utils.StringUtils.isEmptyString(cur.getString(nameColumn))
                phoneNumber = com.utils.StringUtils.isEmptyString(cur.getString(phoneNumberColumn))
                smsbody = com.utils.StringUtils.isEmptyString(cur.getString(smsbodyColumn))
                val type = cur.getInt(typeColumn)
                read = com.utils.StringUtils.isEmptyString(cur.getInt(readColumn).toString())

                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val d = Date(Long.parseLong(cur.getString(dateColumn)))
                date = dateFormat.format(d)

                val personUri = Uri.withAppendedPath(Uri.withAppendedPath(Uri.parse(TypeUtils.getContentPhoneLookup()), "phone_lookup"), phoneNumber)
                val localCursor = cr.query(personUri, arrayOf("display_name", "photo_id", "_id"), null as String?, null as Array<String?>?, null as String?)
                if (localCursor != null) {
                    if (localCursor.count != 0) {
                        localCursor.moveToFirst()
                        name = localCursor.getString(localCursor.getColumnIndex("display_name"))
                        com.utils.LogUtils.e("有名字：：" + name)
                    }
                    localCursor.close()
                }
                com.utils.LogUtils.e("手机号码：" + phoneNumber + "  名称：" + name + " 内容： " + smsbody + " type==" + type)
            } while (cur.moveToNext() && cur.count > 0)
        } else {
            cur.close()
        }
    }
}