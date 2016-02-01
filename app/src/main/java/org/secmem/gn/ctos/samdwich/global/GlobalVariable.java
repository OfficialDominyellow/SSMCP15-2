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
    public static String STOP_SERVICE = "SEND_STOP_SERVICE";
    public static String HIDE_SERVICE = "SEND_HIDE_SERVICE";
    public static String DISP_SERVICE = "SEND_DISP_SERVICE";

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
