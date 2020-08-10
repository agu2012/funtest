package km.gxy.com.funtest.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;

import km.gxy.com.funtest.BaseActivity;
import km.gxy.com.funtest.R;
import km.gxy.com.funtest.common.CommonUrls;
import km.gxy.com.funtest.gson.AQI;
import km.gxy.com.funtest.gson.Forecast;
import km.gxy.com.funtest.gson.Weather;
import km.gxy.com.funtest.util.HttpUtil;
import km.gxy.com.funtest.util.Utility;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends BaseActivity {
    private static final String INTENT_PARAM1 = "weather_id";
    private static final String SHARED_PREF_BING = "bing_pic";
    private static final String SHARED_PREF_WEATHER = "weather";

    private ScrollView mWeatherLay;

    private ImageView mBing;

    private TextView mTitleCity;
    private TextView mTitleUpdateTime;
    private TextView mDegree;
    private TextView mWeatherInfo;
    private LinearLayout mForecastLay;
    private TextView mAqi;
    private TextView mPm25;
    private TextView mComfort;
    private TextView mCarWash;
    private TextView mSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {// 让背景图与状态栏融为一体
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);// 活动布局显示在状态栏上
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.fw_activity_weather);

        initWidgets();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        initBingPic(prefs);
        String weatherId = getIntent().getStringExtra(INTENT_PARAM1);
        if (weatherId == null) {// 直接打开页面
            String weatherStr = prefs.getString("weather", null);
            Weather weather = Utility.handleWeatherResponse(weatherStr);
            showWeatherInfo(weather);
        } else {//地区选择跳转
            mWeatherLay.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
        }
    }

    private void requestWeather(final String weatherId) {
        String url = CommonUrls.URL_WEATHER_CITY + "&cityid=" + weatherId;
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("网络请求失败（#获取天气信息-1）！");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                if (responseText != null) {
                    final Weather weather = Utility.handleWeatherResponse(responseText);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (weather != null && "ok".equals(weather.status)) {
                                saveLocalWeather(responseText);
                                showWeatherInfo(weather);
                            } else {
                                showToast("网络请求失败（#获取天气信息-2）！");
                            }
                        }
                    });
                } else {
                    showToast("网络请求失败（#获取天气信息-3）！");
                }
            }
        });
    }

    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";
        String weatherInfo = weather.now.more.info;

        mTitleCity.setText(cityName);
        mTitleUpdateTime.setText(updateTime);
        mDegree.setText(degree);
        mWeatherInfo.setText(weatherInfo);

        mForecastLay.removeAllViews();

        for (Forecast f : weather.forecastList) {

            View view = LayoutInflater.from(this).inflate(R.layout.fw_forecast_item,
                    mForecastLay, false);

            TextView dateText = view.findViewById(R.id.tv_date);
            TextView infoText = view.findViewById(R.id.tv_info);
            TextView maxText = view.findViewById(R.id.tv_max);
            TextView minText = view.findViewById(R.id.tv_min);

            dateText.setText(f.date);
            infoText.setText(f.more != null ? f.more.info : "");
            maxText.setText(f.temperature != null ? f.temperature.max : "");
            minText.setText(f.temperature != null ? f.temperature.min : "");
            mForecastLay.addView(view);
        }

        AQI aqi = weather.aqi;
        if (aqi != null) {
            mAqi.setText(aqi.city != null ? aqi.city.aqi : "");
            mPm25.setText(aqi.city != null ? aqi.city.pm25 : "");
        }

        if (weather.suggestion != null) {
            String comfort = "舒适度：" + weather.suggestion.comfort.info;
            String carWash = "洗车指数：" + weather.suggestion.carwash.info;
            String sport = "运动建议：" + weather.suggestion.sport.info;

            mComfort.setText(comfort);
            mCarWash.setText(carWash);
            mSport.setText(sport);
        }
        mWeatherLay.setVisibility(View.VISIBLE);
    }

    private void initWidgets() {
        mWeatherLay = findViewById(R.id.weather_lay);
        mBing = findViewById(R.id.iv_bing);
        mTitleCity = findViewById(R.id.tv_title_city);
        mTitleUpdateTime = findViewById(R.id.tv_title_update_time);
        mDegree = findViewById(R.id.tv_degree);
        mWeatherInfo = findViewById(R.id.tv_weather_info);
        mForecastLay = findViewById(R.id.forecast_lay);
        mAqi = findViewById(R.id.tv_api);
        mPm25 = findViewById(R.id.tv_pm25);
        mComfort = findViewById(R.id.tv_comfort);
        mCarWash = findViewById(R.id.tv_car_wash);
        mSport = findViewById(R.id.tv_sport);
    }

    private void initBingPic(SharedPreferences prefs) {
        String bg = prefs.getString("SHARED_BING", null);
        if (bg != null) {
            loadBingImage(bg);
        } else {
            loadBingPic();
        }
    }

    private void loadBingPic() {
        HttpUtil.sendOkHttpRequest(CommonUrls.URL_BING, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response == null || !response.isSuccessful()) {
                    showToast("网络请求失败：获取背景");
                    return;
                }
                final String bingPic = response.body().string();
                saveLocalBing(bingPic);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadBingImage(bingPic);
                    }
                });
            }
        });
    }

    private void saveSharedPreference(String index, String val) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(this).edit();
        editor.putString(index, val);
        editor.apply();
    }

    private void saveLocalWeather(String val) {
        saveSharedPreference(SHARED_PREF_WEATHER, val);
    }

    private void saveLocalBing(String val) {
        saveSharedPreference(SHARED_PREF_BING, val);
    }

    private void loadBingImage(String url) {
        loadImage(mBing, url);
    }

    private void loadImage(ImageView iv, String url) {
        Glide.with(this).load(url).into(iv);
    }

    public static void intentWithParam1(Context context, String weatherId) {
        Intent intent = new Intent(context, WeatherActivity.class);
        intent.putExtra(INTENT_PARAM1, weatherId);
        context.startActivity(intent);
    }

}
