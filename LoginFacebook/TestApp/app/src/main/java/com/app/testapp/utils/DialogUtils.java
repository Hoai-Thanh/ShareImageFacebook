package com.app.testapp.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Thanh Nguyen on 8/10/2015.
 */
public class DialogUtils {
    public static void toastMessage(Context context, int id) {
        Toast toast = Toast.makeText(context, id, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }

    public static void toastMessage(Context context, String content) {
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        toast.setGravity(Gravity.CENTER, 0, 0);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }
}
