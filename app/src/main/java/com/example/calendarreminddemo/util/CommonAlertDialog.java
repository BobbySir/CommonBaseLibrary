package com.example.calendarreminddemo.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.calendarreminddemo.R;


public class CommonAlertDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout lLayout_bg;
    private TextView txt_title;
    private TextView txt_msg;
    private Button btn_neg;
    private Button btn_pos;
    private ImageView img_line;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public CommonAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public CommonAlertDialog builder() {
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_alert_dialog, null);

        lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_msg = (TextView) view.findViewById(R.id.txt_msg);
        btn_neg = (Button) view.findViewById(R.id.btn_neg);
        btn_pos = (Button) view.findViewById(R.id.btn_pos);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        setGone();
        dialog = new Dialog(context, R.style.Theme_AppCompat_Dialog);
        dialog.setContentView(view);
        lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.72), FrameLayout.LayoutParams.WRAP_CONTENT));
        setCancelable(false);
        return this;
    }

    /**
     * 恢复初始
     *
     * @return
     */
    public CommonAlertDialog setGone() {
        if (lLayout_bg != null) {
            txt_title.setVisibility(View.GONE);
            txt_msg.setVisibility(View.GONE);
            btn_neg.setVisibility(View.GONE);
            btn_pos.setVisibility(View.GONE);
            img_line.setVisibility(View.GONE);

        }
        showTitle = false;
        showMsg = false;
        showPosBtn = false;
        showNegBtn = false;
        return this;
    }

    /**
     * 设置title
     *
     * @param title
     * @return
     */
    public CommonAlertDialog setTitle(String title) {
        return setTitle(title, 0);
    }
    public CommonAlertDialog setTitle(String title, int color) {
        showTitle = true;
        if(color > 0){
            txt_title.setTextColor(ContextCompat.getColor(context, color));
        }

        if (!TextUtils.isEmpty(title)) {
            txt_title.setText(title);
        } else {
            if(context != null){
                txt_title.setText("加载中...");
            }
        }
        return this;
    }

    /**
     * 设置Message
     *
     * @param msg
     * @return
     */
    public CommonAlertDialog setMsg(String msg) {
        return setMsg(msg, 0);
    }
    public CommonAlertDialog setMsg(String msg, int color) {
        showMsg = true;
        if(color > 0){
            txt_msg.setTextColor(ContextCompat.getColor(context, color));
        }
        if (TextUtils.isEmpty(msg)) {
            txt_msg.setText("");
        } else {
            txt_msg.setText(msg);
        }
        return this;
    }

    /**
     * 设置Message
     *
     * @param msg
     * @return
     */
    public CommonAlertDialog setMsg(CharSequence msg) {
        showMsg = true;
        if (TextUtils.isEmpty(msg)) {
            txt_msg.setText("");
        } else {
            txt_msg.setMovementMethod(LinkMovementMethod.getInstance());
            txt_msg.setText(msg);
            txt_msg.setGravity(Gravity.START);
        }
        return this;
    }

    /**
     * 设置点击外部是否消失
     *
     * @param cancel
     * @return
     */
    public CommonAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }


    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(onKeyListener);
    }


    /**
     * 右侧按钮
     * @param listener
     * @return
     */
    public CommonAlertDialog setPositiveButton(final View.OnClickListener listener) {
        return setPositiveButton(null, -1, listener);
    }

    public CommonAlertDialog setPositiveButton(String text, final View.OnClickListener listener) {
        return setPositiveButton(text, -1, listener);
    }

    public CommonAlertDialog setPositiveButton(String text, int color, final View.OnClickListener listener) {
        showPosBtn = true;
        if (!TextUtils.isEmpty(text)) {
            btn_pos.setText(text);
        }
        if (color != -1) {
            btn_pos.setTextColor(ContextCompat.getColor(context, color));
        }
        btn_pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }

    /**
     * 左侧按钮
     * @param listener
     * @return
     */
    public CommonAlertDialog setNegativeButton(final View.OnClickListener listener) {
        return setNegativeButton(null, -1, listener);
    }

    public CommonAlertDialog setNegativeButton(String text, final View.OnClickListener listener) {
        return setNegativeButton(text, -1, listener);
    }

    public CommonAlertDialog setNegativeButton(String text, int color,
                                                                               final View.OnClickListener listener) {
        showNegBtn = true;
        if (!TextUtils.isEmpty(text)) {
            btn_neg.setText(text);
        }
//        if (color == -1) {
//            color = Color.BLACK;
//        }
        if(color > -1){
            btn_neg.setTextColor(context.getResources().getColor(color));
        }
//        btn_neg.setTextColor(ContextCompat.getColor(context, color));

        btn_neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(v);
                dismiss();
            }
        });
        return this;
    }

    /**
     * 设置显示
     */
    private void setLayout() {
        if (!showTitle && !showMsg) {
            txt_title.setText("");
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && !showNegBtn) {
            btn_pos.setText("");
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_dialog_selector);
            btn_pos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_dialog_right_selector);
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alert_dialog_left_selector);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            btn_pos.setVisibility(View.VISIBLE);
            btn_pos.setBackgroundResource(R.drawable.alert_dialog_selector);
        }

        if (!showPosBtn && showNegBtn) {
            btn_neg.setVisibility(View.VISIBLE);
            btn_neg.setBackgroundResource(R.drawable.alert_dialog_selector);
        }
    }

    public void show() {
        setLayout();
        try {
            if(dialog != null){
                dialog.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            if (dialog.isShowing())
                return true;
            else
                return false;
        }
        return false;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }

    }
}
