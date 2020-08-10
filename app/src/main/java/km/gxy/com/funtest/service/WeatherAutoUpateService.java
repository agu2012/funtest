package km.gxy.com.funtest.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.io.IOException;

import km.gxy.com.funtest.common.CommonUrls;
import km.gxy.com.funtest.gson.Weather;
import km.gxy.com.funtest.util.HttpUtil;
import km.gxy.com.funtest.util.SharePreferencesUtil;
import km.gxy.com.funtest.util.Utility;
import km.gxy.com.funtest.weather.WeatherActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author xiayi.gu@2020/8/10
 */
public class WeatherAutoUpateService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateBingPic();
        updateWeather();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 8 * 60 * 60 * 1000;//8小时的ms
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, WeatherAutoUpateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        String weatherStr = SharePreferencesUtil.readStrSharedPreference(this,
                WeatherActivity.SHARED_PREF_WEATHER);
        if (TextUtils.isEmpty(weatherStr)) return;
        Weather weather = Utility.handleWeatherResponse(weatherStr);
        String weatherId = weather.basic.weatherId;
        String url = CommonUrls.URL_WEATHER_CITY + "&cityid=" + weatherId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || !response.isSuccessful()) {
                    return;
                }
                final String responseText = response.body().string();
                if (responseText != null) {
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                    if (weather != null && "ok".equals(weather.status)) {
                        saveLocalWeather(responseText);
                    }
                }
            }
        });
    }

    private void updateBingPic() {
        HttpUtil.sendOkHttpRequest(CommonUrls.URL_BING, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || !response.isSuccessful()) {
                    return;
                }
                final String bingPic = response.body().string();
                saveLocalBing(bingPic);
            }
        });
    }

    private void saveLocalWeather(String val) {
        SharePreferencesUtil.saveSharedPreference(this, WeatherActivity.SHARED_PREF_WEATHER, val);
    }

    private void saveLocalBing(String val) {
        SharePreferencesUtil.saveSharedPreference(this, WeatherActivity.SHARED_PREF_BING, val);
    }
}
