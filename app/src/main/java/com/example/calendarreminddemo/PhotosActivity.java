package com.example.calendarreminddemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.widget.Button;

import com.example.calendarreminddemo.photo.QueryHandler;

import com.utils.LogUtils;
import com.utils.PermissionUtils;

public class PhotosActivity extends AppCompatActivity {
    private String LOGTAG = "out";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Button bt = findViewById(R.id.bt_get);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCalendar();
            }
        });
        initCalendar();

       new Thread(new Runnable() {
           @Override
           public void run() {

           }
       }).start();
    }


    @SuppressLint("InlinedApi")
    private void getLocalPhotos() {
        ContentResolver cr = getContentResolver();
        QueryHandler qh = new QueryHandler(cr, this);

        String columns[] = new String[] { Media.DATA, Media._ID, Media.TITLE, Media.DISPLAY_NAME, Media.WIDTH, Media.HEIGHT,
//                Media.AUTHOR,
                Media.ORIENTATION, Media.LATITUDE, Media.LONGITUDE, Media.DATE_TAKEN, Media.DATE_ADDED, Media.DATE_MODIFIED,
                Media.SIZE, Media.IS_PRIVATE};

        String selection = Media.DATA + " LIKE ? ";
        String selectionArgs[] = new String[] { "%/DCIM/%" }; //100LGDSC;
        String sortOrder = Media.DATA;

        qh.startQuery(1, Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, sortOrder);
    }

    public void handleCursor(int token, Cursor cursor) {

        ContentResolver cr = getContentResolver();
        if(token == 1 && cursor !=null)
        {
            int count = cursor.getCount();
            LogUtils.i(LOGTAG,"相册数量count: " + count);

            int index = 0;
            while(cursor.moveToNext())
            {
                String data = cursor.getString(cursor.getColumnIndex(Media.DATA));

				/*
				if(!StringUtils.containsIgnoreCase(data, "/DCIM/"))
					continue;
				*/
                long id = cursor.getLong(cursor.getColumnIndex(Media._ID));
                String display_name = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
                int orientation = cursor.getInt(cursor.getColumnIndex(Media.ORIENTATION));
                float latitude = cursor.getFloat(cursor.getColumnIndex(Media.LATITUDE));
                float longitude = cursor.getFloat(cursor.getColumnIndex(Media.LONGITUDE));
                String date_taken = cursor.getString(cursor.getColumnIndex(Media.DATE_TAKEN));
                String date_added = cursor.getString(cursor.getColumnIndex(Media.DATE_ADDED));
                String date_modified = cursor.getString(cursor.getColumnIndex(Media.DATE_MODIFIED));
                long size = cursor.getLong(cursor.getColumnIndex(Media.SIZE));
                int is_private = cursor.getInt(cursor.getColumnIndex(Media.IS_PRIVATE));
                int width = cursor.getInt(cursor.getColumnIndex(Media.WIDTH));
                int height = cursor.getInt(cursor.getColumnIndex(Media.HEIGHT));
//                String author = cursor.getString(cursor.getColumnIndex(Media.AUTHOR));

                index++;
                String str = index + ". display_name: " + display_name + ", data: " + data + ", orientation: " + orientation + ", latitude: " + latitude
                        + ", longitude: " + longitude + ", date_taken: " + date_taken + ", date_added: " + date_added + ", date_modified: " + date_modified
                        + ", width: " + width + ", height: " + height + ", size: " + size + ", is_private: " + is_private
                        + ", id: " + id;


                String info = "\n照片名称：" + display_name +"\n 拍摄设备名称:" + Build.MODEL + "\n 拍摄者:"  +" \n拍摄时间：" +date_taken
                        +"\n 宽度：" + width +"\n高度：" + height + " \n纬度:" + latitude + "\n精度：" + longitude;

                LogUtils.e(info);

                String[] args = new String[] { String.valueOf(id) };
                String[] columns = new String[] { Images.Thumbnails.DATA, Images.Thumbnails.KIND, Images.Thumbnails.WIDTH, Images.Thumbnails.HEIGHT };
                Cursor c = cr.query(Images.Thumbnails.EXTERNAL_CONTENT_URI, columns, Images.Thumbnails.IMAGE_ID + "= ?", args, null);

                Log.i(LOGTAG, "c.count: " + c.getCount());

                while(c.moveToNext())
                {
                    String thumb_data = c.getString(c.getColumnIndex(Images.Thumbnails.DATA));
                    int thumb_kind = c.getInt(c.getColumnIndex(Images.Thumbnails.KIND));
                    int thumb_width = c.getInt(c.getColumnIndex(Images.Thumbnails.WIDTH));
                    int thumb_height = c.getInt(c.getColumnIndex(Images.Thumbnails.HEIGHT));

                    Log.i(LOGTAG, "thumb_data: " + thumb_data + ", thumb_kind: " + thumb_kind + ", thumb_width: " + thumb_width
                            + ", thumb_height: " + thumb_height);
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            initCalendar();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0) {
            boolean isAllGranted = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                //有权限
                initCalendar();
            }else{
                PermissionUtils.showDialog(this, getString(R.string.need_camera_stogafe_permission));
            }
        }
    }

    private void initCalendar(){
        if(PermissionUtils.hasPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})){
            getLocalPhotos();
        }else{
            PermissionUtils.requestPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
        }
    }
}