package com.example.calendarreminddemo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bean.CalenderDataBean
import com.example.calendarreminddemo.util.PermissionUtils
import com.utils.*
import kotlinx.android.synthetic.main.activity_tab_main.*

class TabMainActivity : AppCompatActivity() {
    val MESS_CODE = 101
    val CONTACT_CODE = 102
    val CALLS_CODE = 103
    val DEVICE_CODE = 104
    val APP_CODE = 105
    val ALBUM_CODE = 106
    val CALEN_CODE = 107

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_main)
        initData()
    }

    fun initData(){
        //短信
        bt_message.setOnClickListener {
            initSMSMess()
        }

        //通讯录
        bt_book.setOnClickListener {
            initContact()
        }

        //通话记录
        bt_readCalls.setOnClickListener {
            initCalls()
        }

        //设备信息
        bt_device.setOnClickListener {
            initDevice()
        }

        //安装APP
        bt_app.setOnClickListener {
            initApps()
        }

        //相册
        bt_album.setOnClickListener {
            initAlbums()
        }

        //日历
        bt_calen.setOnClickListener {
            initCalendar()
        }
    }

    fun initSMSMess(){
        if (isPermission(MESS_CODE)) {
            val sb = StringBuffer()
            val messagesList = MessageUtils.getInstance().getMessageInfos(this);
            for (message in messagesList){
                sb.append(message.toString())
                LogUtils.e("获取短信信息:" + message.toString())
            }
            tv_log.text = sb.toString()
        }
    }

    fun initContact(){
        val sb = StringBuffer()
        if (isPermission(CONTACT_CODE)) {
//                val contactS = TxlUtilsa.getContentTxls(this)
            val contactS = com.utils.ContactUtils.getInstance().getContactList(this)
            for (bean in contactS){
                sb.append(bean.toString())
                com.utils.LogUtils.e("获取通讯录信息:" + bean.toString())
            }
            tv_log.text = sb.toString()
        }
    }

    fun initCalls(){
        if (isPermission(CALLS_CODE)) {
            val calls = readCallsUtils.instance?.readCalls(this)
            val sb = StringBuffer()
            if(!EmptyUtil.isEmpty(calls)){
                for (cal in calls!!){
                    LogUtils.e(cal.toString())
                    sb.append(cal.toString())
                }
                tv_log.text = sb.toString()
            }
        }
    }

    fun initDevice(){
        if (isPermission(DEVICE_CODE)) {
            val ds = com.utils.DeviceInfoUtils.getInstance().getDeviceInfo(this, BuildConfig.VERSION_NAME)
            com.utils.LogUtils.e("获取的设备信息：" + ds.toString())
            tv_log.text = ds.toString()
        }
    }

    fun initAlbums(){
        if (isPermission(ALBUM_CODE)) {
            val sb = StringBuffer()
            val albums = AlbumUtils.getInstance().getAlbumBeans(this)
            for (bean in albums){
                com.utils.LogUtils.e("获取相册信息:" + bean.toString())
                sb.append(bean.toString())
            }
            tv_log.text = sb.toString()
        }
    }

    fun initApps(){
        if (isPermission(APP_CODE)) {
            val sb = StringBuffer()

            val apps = AppInfoUtils.getInstance().getInstallApps(this)
            for(app in apps){
                com.utils.LogUtils.e("所有安装APP信息：" + app.toString())
                sb.append(app.toString())
            }
            tv_log.text = sb.toString()
        }
    }

    fun initCalendar() {
        if (isPermission(CALEN_CODE)) {
            val sb = StringBuffer()
//            CalendarReminderUtils.addCalendarEvent(this, titlte, description, 0,0,8);
            val beanList: List<CalenderDataBean> = CalendarUtil.getCalenderDataList(this)
            LogUtils.e("有多少日程：" + beanList.size)
            for (i in 1 until beanList.size - 1) {
                sb.append(beanList[i].toString())
                LogUtils.e("总日程：" + beanList.size + "  当前第" + i + "个日程信息= " + beanList[i].toString())
            }

            tv_log.text = sb.toString()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
                MESS_CODE ->{
                    if(isPermission(MESS_CODE))initSMSMess()
                }
                CONTACT_CODE ->{
                    if(isPermission(CONTACT_CODE))initContact()
                }
                CALLS_CODE ->{
                    if(isPermission(CALLS_CODE))initCalls()
                }
                DEVICE_CODE ->{
                    if(isPermission(DEVICE_CODE))initDevice()
                }
                APP_CODE ->{
                    if(isPermission(APP_CODE))initApps()
                }
                ALBUM_CODE ->{
                    if(isPermission(ALBUM_CODE))initAlbums()
                }
                CALEN_CODE ->{
                    if(isPermission(CALEN_CODE))initCalendar()
                }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size > 0) {
            var isAllGranted = true
            // 判断是否所有的权限都已经授予了
            for (grant in grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false
                    break
                }
            }
            if (isAllGranted) {
                //有权限
                //initCalendar()
            } else {
                PermissionUtils.showDialog(this, "Please set the permissions required to allow the app")
            }
        }
    }

    private fun isPermission(requestCode: Int) : Boolean{
        if (!PermissionUtils.hasPermission(this, PermissionUtils.mPermissions)) {
            PermissionUtils.requestPermission(this, PermissionUtils.mPermissions, requestCode)
            return false
        }
        return true
    }
}