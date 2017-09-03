package com.example.jingle.jinglebizhi;

import android.content.Context;

/**
 * Created by liujian on 2017/9/3.
 */

public class DisplayUtil {
   /* Context mContext;
    DisplayUtil(Context mContext){
        this.mContext = mContext;
    }*/
    public static  int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dip2px(Context context, float dipValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(scale*dipValue+0.5f);
    }

    public static int px2dip(Context context, float pxValue){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
}
