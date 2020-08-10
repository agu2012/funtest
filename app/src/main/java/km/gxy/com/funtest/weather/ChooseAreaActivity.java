package km.gxy.com.funtest.weather;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import km.gxy.com.funtest.R;

public class ChooseAreaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fw_activity_choose_area);
    }

    public static void intentChooseAreaActivity(Context context) {
        Intent i = new Intent(context, ChooseAreaActivity.class);
        context.startActivity(i);
    }
}
