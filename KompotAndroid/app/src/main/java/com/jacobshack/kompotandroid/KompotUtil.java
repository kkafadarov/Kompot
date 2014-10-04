package com.jacobshack.kompotandroid;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kkafadarov on 10/4/14.
 */
public class KompotUtil {
    public static void makeWarning(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
