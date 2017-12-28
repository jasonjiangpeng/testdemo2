package com.hjimi.depth.actvity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.hjimi.depth.R;
import com.hjimi.depth.utils.Mlog;

/**
 * Created by Administrator on 2017/12/21.
 */

public class MedeoAcitvity extends Activity {
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder surfaceHolder;
    private ImageView imageView;
    private int[]  image={R.drawable.gangzhou_oranges, R.drawable.philippines_emperorbanana, R.drawable.qingzhou_peach,
            R.drawable.australia_manguo, R.raw.kiwi, R.raw.orange1};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medeoactivity);
        int  value=getIntent().getIntExtra("Value",1);
        getValue(value);

    }
    public void getValue(int value){
      if (value>3){
          videoplay(value);
      }else {
          showimage(value);
      }
    }
    public void showimage(int value){
        imageView= (ImageView) findViewById(R.id.imgview);
        imageView.setVisibility(View.VISIBLE);
        imageView.setBackgroundResource(image[value]);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              finish();
            }
        },10*1000);
    }
    public void videoplay(final int value){
        surfaceView= (SurfaceView) findViewById(R.id.surfaceview);
        surfaceView.setVisibility(View.VISIBLE);
        surfaceView.setBackgroundResource(R.drawable.bg);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                player= MediaPlayer.create(MedeoAcitvity.this,image[value]);
                player.setDisplay(holder);
                player.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        Mlog.log("onInfo");
                        if(what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                            //隐藏缩略图
                            surfaceView.setBackgroundResource(0);
                        }
                        return false;
                    }
                });
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Mlog.log("onPrepared");
                        player.start();
                    }
                });

                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        finish();
                    }
                });

            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (player!=null) {
                    player.stop();
                    //释放资源
                    player.release();

                }
            }
        });
    }
}
