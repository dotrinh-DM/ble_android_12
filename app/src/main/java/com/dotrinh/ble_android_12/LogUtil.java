/*
 * Created by dotrinh on 6/30/20 8:27 PM
 * Copyright (c) 2020. dotr Inc. All rights reserved.
 */

package com.dotrinh.ble_android_12;

import android.util.Log;

public class LogUtil {
    public static void LogD(String Tag, String Message) {
        Log.d(Tag, Message);
    }

    public static void LogE(String Tag, String Message) {
        Log.e(Tag, Message);
    }

    public static void LogI(String Message) {
            Log.i("viewlog", Message);
    }

    public static void LogW(String Tag, String Message) {
        Log.w(Tag, Message);
    }

}

