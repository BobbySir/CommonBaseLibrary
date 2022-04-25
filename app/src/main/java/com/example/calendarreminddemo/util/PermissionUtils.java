package com.example.calendarreminddemo.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.SingleClickListener;
import com.utils.LogUtils;


/**
 * 获取权限的工具类
 */
public class PermissionUtils {

    private static AlertDialog alertDialog;
    private static CommonAlertDialog myDialog;

    public static final int REQUEST_INTENT_CODE = 400;

    public static String[] mPermissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,

    };

    private static OnPermissionListener mListener;
    public interface OnPermissionListener {
        /**
         * 授权成功
         */
        void onPermissionGranted();

        /**
         * 请求权限失败
         *
         * @param deniedPermissions 被拒绝的权限集合
         * @param alwaysDenied 拒绝后是否提示
         */
        void onPermissionDenied(String[] deniedPermissions, boolean alwaysDenied);
    }

    /**
     *
     * @param activity
     * @param permission
     * @param hint
     * @param requestCode
     * @return
     */
    public static boolean checkPermission(Fragment fragment, Activity activity, String permission, String hint, int requestCode){
       return checkPermission(fragment, activity, permission, hint,requestCode, false);
    }
    public static boolean checkPermission(Fragment fragment, Activity activity, String permission, String hint,
                                          int requestCode, boolean isExitApp){

        if(ContextCompat.checkSelfPermission(null != activity ? activity : fragment.getContext(), permission) ==
                PackageManager.PERMISSION_GRANTED){
            LogUtils.e("----------有权限----------------");
            //有权限
            return true;
        }else{
            //是否拒绝了永不提示
            if(hasPermission(null != activity ? activity : fragment.getActivity(), new String[]{permission})){
//            if(XPermissionUtils.hasAlwaysDeniedPermission(null != activity ? activity : fragment.getActivity(),permission)){
                //不在访问
                LogUtils.e("----------不再访问----------------");
                showDialog(null != activity ? activity : fragment.getActivity(), hint, isExitApp);
            }else{
                LogUtils.e("----------可以访问----------------");
                if(fragment == null){
                    ActivityCompat.requestPermissions(activity,
                            new String[]{permission}, requestCode);
                }else {
                    fragment.requestPermissions(
                            new String[]{permission},
                            requestCode);
                }
            }
            return false;
        }
    }


    /**
     *
     * @param activity
     * @param permission
     * @param hint
     * @param requestCode
     * @return
     */
    public static boolean checkPermission(Fragment fragment, Activity activity, String permission,
                                          int hint, int requestCode){
        return checkPermission(fragment, activity, permission,
                null != activity ? activity.getString(hint) : fragment.getContext().getString(hint), requestCode);
    }

    /**
     *
     * @param activity
     * @param hint
     */
    public static void showDialog(final Activity activity, String hint){
        showDialog(activity, hint, false,true);
    }
    public static void showDialog(final Activity activity, String hint,boolean isExitApp){
        showDialog(activity, hint, isExitApp,true);
    }
    public static void showDialog(final Activity activity, String hint, final boolean isExitApp,  boolean isShowCancel){
        if(null == activity || activity.isFinishing()){
            return;
        }

        myDialog = new CommonAlertDialog(activity).builder();
        myDialog.setGone().setTitle("Access Request").setMsg(hint)
                .setPositiveButton("Go to set", new SingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        //跳转系统动态权限页面
                        getAppDetailSettingIntent(activity);
                    }
                });
                if(isShowCancel){
                    myDialog.setNegativeButton("cancel", new SingleClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            if(isExitApp){
                                activity.finish();
                            }
                        }
                    });
                }

                myDialog.show();
    }

    /**
     *
     * @param activity
     * @param hint
     */
    public static void showDialog(final Activity activity, int hint){
        showDialog(activity, hint, false);
    }
    public static void showDialog(final Activity activity, int hint, boolean isExitApp){
        showDialog(activity, activity.getString(hint), isExitApp);
    }

    /**
     * 跳转到安装未知应用来源
     * @param activity
     * @param hint
     * @param i
     */
    public static void showDialog(final Activity activity, String hint, int i){
        if(null == activity || activity.isFinishing()){
            return;
        }
        alertDialog = new AlertDialog.Builder(activity)
                .setTitle("hint")
                .setMessage(hint)
                .setNegativeButton("cancel", null)
                .setPositiveButton("Go to set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                            activity.startActivityForResult(intent, 10086);
                        }
                    }
                })
                .show();
    }


    /**
     * 通过尝试打开相机的方式判断有无拍照权限（在6.0以下使用拥有root权限的管理软件可以管理权限）
     *
     * @return
     */
    public static boolean cameraIsCanUse() {
        boolean isCanUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            isCanUse = false;
        }

        if (mCamera != null) {
            try {
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
                return isCanUse;
            }
        }
        return isCanUse;
    }


    /**
     * 申请权限
     * @param activity
     * @param permission
     * @param requestCode
     */
    public static void requestPermission(Activity activity, String permission, int requestCode){
        ActivityCompat.requestPermissions(activity,
                new String[]{permission}, requestCode);
    }

    public static void requestPermission(Activity activity, String permission, int requestCode,OnPermissionListener listener){
        mListener = listener;
        if(hasPermission(activity, permission)){
            mListener.onPermissionGranted();
        }else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission}, requestCode);
        }
    }


    /**
     * 申请权限
     * @param activity
     * @param permission
     * @param requestCode
     */
    public static void requestPermission(final Activity activity, final String[] permission, final int requestCode){
        if(activity !=null && !activity.isFinishing()){
            ActivityCompat.requestPermissions(activity,
                    permission, requestCode);
        }
    }


    /**
     * 判断权限是否开启
     * @param activity
     * @param permission
     * @return
     */
    public static boolean  hasPermission(Activity activity, String permission){
        return ContextCompat.checkSelfPermission(activity, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断权限是否开启
     * @param context
     * @param permission
     * @return
     */
    public static boolean hasPermission(Context context, String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 请求动态权限回调
     * 有权限: PackageManager.PERMISSION_GRANTED = 0
     * 无权限: PackageManager.PERMISSION_DENIED  = -1
     */
    public static boolean hasPermission(Context context, String[] permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int sum = 0;
            for(int i = 0; i < permission.length; i++){
                sum = sum + (ContextCompat.checkSelfPermission(context, permission[i]) ==
                        PackageManager.PERMISSION_GRANTED ? 1 : 0);
            }
            return sum == permission.length;
        }
        return true;
    }

    /**
     * 获取权限状态
     * @param context
     * @param permission
     * @return
     */
    public static boolean getPermission(Context context, String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(context, permission) ==
                    PackageManager.PERMISSION_GRANTED){
                LogUtils.e("----------有权限----------------");
                //有权限
                return true;
            }
            return false;
        }
        return true;
    }


    /**
     * 跳转到权限设置界面
     */
    public static void getAppDetailSettingIntent(Activity context){
        Intent intent = new Intent();
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        //context.startActivity(intent);
        context.startActivityForResult(intent, REQUEST_INTENT_CODE);
    }

    /**
     * 判断状态栏定位GPS服务是否开启
     *
     * @param
     * @return true 表示开启
     */
    public boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}
