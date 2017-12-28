package com.hjimi.depth.sound;

import android.media.SoundPool;
import android.os.Build;

import com.hjimi.depth.MainApplication;
import com.hjimi.depth.R;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/20.
 */

public class Soundplay {
    private   int[]  sound={R.raw.navel_orange, R.raw.emperor_banana, R.raw.qingzhoupeach, R.raw.australian_mango};
    private SoundPool builder;
    private HashMap<Integer,Integer> hashMap;
    public Soundplay() {
        hashMap=new HashMap<>(6);
        builder = new SoundPool.Builder().build();
       for (int i = 0; i <sound.length ; i++) {
            int load = builder.load(MainApplication.context, sound[i], 1);
           hashMap.put(i,load);
        }
    }
    private int justId;
    public void play(int positon){
        if (positon>=sound.length){
            return;
        }
         justId = hashMap.get(positon);

         builder.play(justId,1,1,1,0,1);
    }
    public void stop(){
        builder.stop(justId);
    }
    public void destory(){
        hashMap.clear();
        builder.release();
    }
}
