package com.hjimi.depth.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjimi.depth.R;

/**
 * Created by Administrator on 2017/12/27.
 */

public class TextViewDouble extends LinearLayout {
    private TextView tv1,tv2;
    public TextViewDouble(Context context) {
        super(context);
    }

    public TextViewDouble(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.sub_textview,this);
     tv1= (TextView) findViewById(R.id.tv1);
     tv2= (TextView) findViewById(R.id.tv2);
    }
    public void setValue(String v1,String v2){
        tv1.setText(v1);
        tv2.setText(v2);
    }
}
