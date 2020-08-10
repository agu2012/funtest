package km.gxy.com.funtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import km.gxy.com.funtest.weather.ChooseAreaActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnFunWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();

    }

    private void initWidgets() {
        mBtnFunWeather = findViewById(R.id.fun_weather);

        mBtnFunWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fun_weather) {
            ChooseAreaActivity.intentChooseAreaActivity(MainActivity.this);
        }
    }
}
