package com.example.calendarreminddemo

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_tab_main.*
import com.utils.AlbumUtils
import com.utils.AppInfoUtils
import com.utils.BBUtils

class TabMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_main)
        initData()
    }

    fun initData(){
        bt_album.setOnClickListener {
            initAlbums()
        }
        bt_app.setOnClickListener {
            initApps()
        }
        bt_message.setOnClickListener {
            if (com.utils.PermissionUtils.hasPermission(this, com.utils.PermissionUtils.mPermissions)) {
//                val messages = MessageUtils.getInstance().getMessageInfos(this);
//                for (com.bean in messages){
//                    LogUtils.e("获取短信信息:" + com.bean.toString())
//                }
//                AA.readSms(this)
                BBUtils.getSmsInfos(this)
            }else {
                com.utils.PermissionUtils.requestPermission(this, com.utils.PermissionUtils.mPermissions, 101)
            }
        }
        bt_contact.setOnClickListener {
            if (com.utils.PermissionUtils.hasPermission(this, com.utils.PermissionUtils.mPermissions)) {
//                val contactS = TxlUtilsa.getContentTxls(this)
                val contactS = com.utils.ContactUtils.getInstance().getContactList(this)
                for (bean in contactS){
                    com.utils.LogUtils.e("获取联系人信息:" + bean.toString())
                }

            }else{
                com.utils.PermissionUtils.requestPermission(this, com.utils.PermissionUtils.mPermissions, 101)
            }
        }
        bt_device.setOnClickListener {
            if (com.utils.PermissionUtils.hasPermission(this, com.utils.PermissionUtils.mPermissions)) {
                val ds = com.utils.DeviceInfoUtils.getInstance().getDeviceInfo(this)
                com.utils.LogUtils.e("获取的设备信息：" + ds.toString())
            }else{
                com.utils.PermissionUtils.requestPermission(this, com.utils.PermissionUtils.mPermissions, 101)
            }
        }

        bt_calen.setOnClickListener {
            val intent = Intent(this@TabMainActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun initAlbums(){
        if (com.utils.PermissionUtils.hasPermission(this, com.utils.PermissionUtils.mPermissions)) {
            Thread {
                val albums = AlbumUtils.getInstance().getAlbumBeans(this)
                for (bean in albums){
                    com.utils.LogUtils.e("获取相册信息:" + bean.toString())
                }
            }.start()
        } else {
            com.utils.PermissionUtils.requestPermission(this, com.utils.PermissionUtils.mPermissions, 101)
        }
    }

    fun initApps(){
        if (com.utils.PermissionUtils.hasPermission(this, com.utils.PermissionUtils.mPermissions)) {
            val apps = AppInfoUtils.getInstance().getInstallApps(this)
            for(app in apps){
                com.utils.LogUtils.e("所有安装APP信息：" + app.toString())
            }
        } else {
            com.utils.PermissionUtils.requestPermission(this, com.utils.PermissionUtils.mPermissions, 101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            initCalendar()
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
                initCalendar()
            } else {
                com.utils.PermissionUtils.showDialog(this, getString(R.string.need_all_permission))
            }
        }
    }

    private fun initCalendar() {
        if (!com.utils.PermissionUtils.hasPermission(this,  com.utils.PermissionUtils.mPermissions)) {
            com.utils.PermissionUtils.requestPermission(this, com.utils.PermissionUtils.mPermissions, 101)
        }
    }
}