package com.hjimi.depth.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hjimi.depth.R;
import com.hjimi.depth.depth.DepthRect;
import com.hjimi.depth.devices.ParameterControl;
import com.hjimi.depth.utils.Mlog;

/**
 * Created by Administrator on 2017/12/26.
 */

public class SetValueUI extends LinearLayout {
    public SetValueUI(Context context) {
        super(context);
    }
         private EditText  tv1,tv2,tv3,tv4;
    private int  mweight=100;
    private int  mheiht=100;
    private int  cols=1;
    private int  rows=1;
      public SetValueUI(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.sub_value,this);
        tv1= (EditText) findViewById(R.id.tv1);
        tv2= (EditText) findViewById(R.id.tv2);
        tv3= (EditText) findViewById(R.id.tv3);
        tv4= (EditText) findViewById(R.id.tv4);
          Button button= (Button) findViewById(R.id.btn);
          button.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  DepthRect.getDepthrect().setReadScan(true);
              }
          });
        tv1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>1){
                    mweight=Integer.valueOf(s.toString()).intValue();
                    DepthRect.getDepthrect().getTriangleD().setWeight(mweight);

                }

            }
        });
        tv2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>1){
                    mheiht=Integer.valueOf(s.toString()).intValue();
                    DepthRect.getDepthrect().getTriangleD().setHeight(mheiht);

                }

            }
        });
        tv3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    rows=Integer.valueOf(s.toString()).intValue();
                    if (rows>0){
                        DepthRect.getDepthrect().getTriangleD().setRows(rows);
                    }

                }


            }
        });
        tv4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0){
                    cols=Integer.valueOf(s.toString()).intValue();
                    if (cols>0){
                        DepthRect.getDepthrect().getTriangleD().setCols(cols);
                    }


                }

            }
        });

    }

    public int getMweight() {
        return mweight;
    }

    public void setMweight(int mweight) {
        this.mweight = mweight;
    }

    public int getMheiht() {
        return mheiht;
    }

    public void setMheiht(int mheiht) {
        this.mheiht = mheiht;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }



}
