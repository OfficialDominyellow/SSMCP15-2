package org.secmem.gn.ctos.samdwich.global;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by 김희중 on 2016-01-16.
 */
public class GlobalVariable {
    public static int stickWidth;
    public static int stickHeight;
    public static int HideImageWidth;
    public static int HideImageheight;

    public static int displayMaxLeft;
    public static int displayMaxRight;
    public static int displayMaxTop;
    public static int displayMaxBottom;
    public static int displayWidthPx;
    public static int displayHeightPx;
    public static final String STOP_SERVICE = "SEND_STOP_SERVICE";
    public static final String HIDE_SERVICE = "SEND_HIDE_SERVICE";
    public static final String DISP_SERVICE = "SEND_DISP_SERVICE";
    public static final String CHANGE_SIZE = "SEND_CHANGE_SIZE";
    public static final String CHANGE_PROG = "SEND_CHANGE_PROG";

    public static final int size1=80;
    public static final int size2=100;
    public static final int size3=120;

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
