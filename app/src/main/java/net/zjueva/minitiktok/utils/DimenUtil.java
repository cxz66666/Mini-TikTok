package net.zjueva.minitiktok.utils;

import android.content.Context;

public class DimenUtil {
    public static int dp2px(Context ctx, float dp) {
        float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
