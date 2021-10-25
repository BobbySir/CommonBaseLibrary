package com;

import android.view.View;

/**
 * 防止过快点击造成多次事件
 */
public abstract class SingleClickListener implements View.OnClickListener {
    private long firstTime;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - firstTime > 2000) {
            onSingleClick(v);
            firstTime = System.currentTimeMillis();
        }
    }
}
