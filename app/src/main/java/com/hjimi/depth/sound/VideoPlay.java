package com.hjimi.depth.sound;

import android.media.MediaPlayer;
import android.view.SurfaceHolder;

import com.hjimi.depth.MainApplication;
import com.hjimi.depth.R;

/**
 * Created by Administrator on 2017/12/21.
 */

public class VideoPlay {
    CallCompletion callCompletion;
  private MediaPlayer player;

    public CallCompletion getCallCompletion() {
        return callCompletion;
    }

    public void setCallCompletion(CallCompletion callCompletion) {
        this.callCompletion = callCompletion;
    }

    public void videoplay(SurfaceHolder surfaceHolder){
        player= MediaPlayer.create(MainApplication.context, R.raw.kiwi);
        player.setDisplay(surfaceHolder);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                callCompletion.playonCompletion();
            }
        });
        player.start();
    }
    public void destory(){
        if (player!=null){
            player.release();
        }
    }
   public interface  CallCompletion{
        void playonCompletion();
    }
}
