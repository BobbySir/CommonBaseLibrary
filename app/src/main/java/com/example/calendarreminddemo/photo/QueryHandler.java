package com.example.calendarreminddemo.photo;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.example.calendarreminddemo.PhotosActivity;

/**
 * Created by：bobby on 2021-08-18 17:36.
 * Describe：
 */
public class QueryHandler extends AsyncQueryHandler {
    private PhotosActivity helloCordova;

    public QueryHandler(ContentResolver cr, PhotosActivity hc) {
        super(cr);
        this.helloCordova = hc;
    }


    public void startQuery(int token, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        super.startQuery(token, null, uri, projection, selection, selectionArgs, orderBy);
    }


    @SuppressLint("InlinedApi")
    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        super.onQueryComplete(token, cookie, cursor);

        helloCordova.handleCursor(token, cursor);
    }
}