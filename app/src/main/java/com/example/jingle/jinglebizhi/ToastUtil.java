package com.example.jingle.jinglebizhi;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by liujian on 2017/9/3.
 */

public class ToastUtil {
    private static Toast mToast;

    public static void showToast(Context context, String content) {
        if (mToast == null) {
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.show();
    }
}
