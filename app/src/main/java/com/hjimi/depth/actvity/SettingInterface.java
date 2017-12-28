package com.hjimi.depth.actvity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.hjimi.depth.R;

/**
 * Created by Administrator on 2017/12/25.
 */

public class SettingInterface extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depth_view);
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(intent);

        finish();
    }
}
