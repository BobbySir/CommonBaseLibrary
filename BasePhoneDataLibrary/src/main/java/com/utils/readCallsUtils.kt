package com.utils

import android.content.Context
import android.database.Cursor
import android.os.SystemClock
import android.provider.CallLog
import android.text.TextUtils
import com.bean.CallsBean
import com.bean.DeviceInfoBean
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by：bobby on 2021-08-21 14:56.
 * Describe：设备信息工具类
 */
class readCallsUtils {

    companion object {
        var instance: readCallsUtils? = null
            get() {
                if (field == null) {
                    synchronized(readCallsUtils::class.java) {
                        if (field == null) {
                            field = readCallsUtils()
                        }
                    }
                }
                return field
            }
            private set
    }

    //===================================获取通话记录=====================================
    public fun readCalls(context: Context): List<CallsBean> {
        val mCallLogModelList = arrayListOf<CallsBean>()
        var name: String
        var phoneNumber: String
        var callTime: String
        var time: String
        var type: Int

        val projection = arrayOf(
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.TYPE,
                CallLog.Calls.DATE,
                CallLog.Calls.DURATION
        )

        val uri = CallLog.Calls.CONTENT_URI
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(
                    uri, projection, null, null,
                    CallLog.Calls.DEFAULT_SORT_ORDER
            )
            while (cursor != null && cursor.moveToNext() && cursor.count > 0) {
                val callType: Int
                val strDate: String
                val dateLong: Long?
                val callCnt = 1
                name = StringUtils.isEmptyString(cursor.getString(0))        //姓名
                phoneNumber = StringUtils.isEmptyString(cursor.getString(1)) //号码
                callType = cursor.getInt(2)                                  //获取通话类型：1.呼入2.呼出3.未接
                dateLong = cursor.getLong(3)                                 //获取通话日期
                callTime = StringUtils.isEmptyString(cursor.getString(4))    //获取通话时长，值为多少秒
                val dateFormat = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss"
                )

                val d = Date(dateLong)
                strDate = dateFormat.format(d)
                time = strDate

                when (callType) {
                    CallLog.Calls.INCOMING_TYPE -> type = 1     //接听
                    CallLog.Calls.OUTGOING_TYPE -> type = 2        //呼出
                    CallLog.Calls.MISSED_TYPE -> type = 3        //未接听
                    CallLog.Calls.VOICEMAIL_TYPE -> type = 4        //语音邮箱
                    CallLog.Calls.REJECTED_TYPE -> type = 5        //拒接
                    CallLog.Calls.BLOCKED_TYPE -> type = 6         //自动拦截
                    else -> type = 1
                }

                if (!StringUtils.isEmpty(phoneNumber) && !StringUtils.isEmpty(callTime)
                        && !StringUtils.isEmpty(time)
                ) {
                    val model = CallsBean()
                    model.name = StringUtils.isEmptyString(name)
                    model.phone = phoneNumber
                    model.callDuration = callTime
                    model.callTime = time
                    model.callCnt = callCnt
                    model.type = type

                    mCallLogModelList.add(model)
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return mCallLogModelList
    }
}