package com.hjimi.depth.actvity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.hjimi.depth.R;
import com.hjimi.depth.utils.CrashHandler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017/12/25.
 */

public class LogAct  extends Activity{
    private TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logshow);
        tv= (TextView) findViewById(R.id.tv);
       // tv.setMovementMethod(ScrollingMovementMethod.getInstance());
       // tv.setText(CrashHandler.getInstance().readFile());



      }


}
