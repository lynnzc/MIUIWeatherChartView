package com.lynn.code.miuiweatherchartview;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Lynn on 7/12/16.
 */

public class WUtils {
    /**
     * convert dp to px
     */
    public static int dp2px(Context context, int dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return Math.round(dp * (dm.densityDpi / 160f));
    }

    /**
     * convert sp to px
     */
    public static int sp2px(Context context, int sp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return Math.round(sp * dm.scaledDensity);
    }

    /**
     * get screen width
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
