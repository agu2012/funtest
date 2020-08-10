package km.gxy.com.funtest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author xiayi.gu@2020/8/10
 */
public class SharePreferencesUtil {

    /**
     * 保存一个参数
     *
     * @param context
     * @param index
     * @param val
     */
    public static void saveSharedPreference(Context context, String index, String val) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString(index, val);
        editor.apply();
    }

    public static String readStrSharedPreference(Context context, String index) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(index, null);
    }
}
