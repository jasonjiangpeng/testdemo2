package com.hjimi.depth;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/12/20.
 */

public class MainApplication extends Application {
    public   static Context context;


    @Override
    public void onCreate() {
        super.onCreate();
          context=this.getApplicationContext();
    }

}
