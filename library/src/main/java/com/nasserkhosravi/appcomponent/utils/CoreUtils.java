package com.nasserkhosravi.appcomponent.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.text.NumberFormat;

/**
 * Created by Nasser Khosravi on 12/8/2017.
 */

public class CoreUtils {
    public static Spanned loadHtmlFrom(String html) {
        Spanned spanned = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(html);
        }
        return spanned;
    }

    /**
     * format number for separating number per 3 digit base local of device
     *
     * @param number 10000
     * @return 1, 0000
     */
    public static String formatNumber(String number) {
        return NumberFormat.getInstance().format(Long.valueOf(number));
    }

    public static String formatNumber(long number) {
        return NumberFormat.getInstance().format(number);
    }

    public static boolean isAppInstalled(Activity activity, String packageName) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return false;
    }
}
