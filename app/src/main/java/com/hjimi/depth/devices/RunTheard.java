package com.hjimi.depth.devices;

import android.os.Handler;
import android.os.SystemClock;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiFrameType;
import com.hjimi.api.iminect.ImiImageFrame;
import com.hjimi.depth.utils.Mlog;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;




/**
 * Created by Administrator on 2017/12/20.
 */

public class RunTheard extends Thread {

  private   boolean isRun=true;
  private   boolean isStop=false;
    private   short[][] original=null;
    private Handler handler;
    private ImiDevice mStream;
    public RunTheard(ImiDevice ImiStream, Handler handler) {
        this.handler=handler;
        this.mStream=ImiStream;
    }
    public short[][]  trans2short(short[] data){
        short[][]  sd=new short[ParameterControl.CAMERA3DW][ParameterControl.CAMERA3DH];
        int position=0;
        for (int i = 0; i <ParameterControl.CAMERA3DW ; i++) {
            for (int j = 0; j <ParameterControl.CAMERA3DH ; j++) {
                sd[i][j]=data[position];
                position++;
            }
        }
        return sd;
    }
    @Override
    public void run() {
       int  w=320;
        int  h=240;
        mStream.openStream(ImiFrameType.DEPTH);
           while (isRun){
               ImiImageFrame nextFrame = mStream.readNextFrame(ImiFrameType.DEPTH,25);
               if(nextFrame == null){
                   continue;
               }
               Mlog.log("====================================");
             long start= System.currentTimeMillis();
               if (isStop){
                   ByteBuffer data = nextFrame.getData().order(ByteOrder.nativeOrder());
                   short[]  comdata=new short[w*h];
                   data.asShortBuffer().get(comdata);
                   original=trans2short(comdata);
                   minValue(original);
                   int type = comparedata2(original);
                   if (type>0){
                       isStop=false;
                    }
                   handler.obtainMessage(type).sendToTarget();
               }
               long end= System.currentTimeMillis()-start;
               if (end<triggleTime){
                   long  time=triggleTime-end;
                   SystemClock.sleep(time);
               }
            //   System.out.println("计算时间"+end);
           }
    }
    private int  triggleTime=100;
    public int minValue(short[][] data2){
        int a=9999;
      int x=0;
      int y=0;
        for (int i = 100; i <140 ; i++) {
            for (int j = 100; j <200 ; j++) {
              if (data2[i][j]>0&&data2[i][j]<a){
                  a=data2[i][j];
                  x=i;
                  y=j;
              }
            }
        }
        Mlog.log("最小值"+a+"坐标x"+x+"坐标:"+y);
        return a;
    }
    private  PlanePoint planePoint=new PlanePoint();
    private boolean runone=true;
    public int comparedata2(short[][] data2){
        if (runone){
            runone=false;
            planePoint.maxAndMinPoint(data2);
        }
        short  mindata=0;
        int a=0;
        int b=0;
        for (int i = 0; i <ParameterControl.CAMERA3DW ; i++) {
            for (int j = 0; j <ParameterControl.CAMERA3DW ; j++) {
               if (planePoint.getRect().contains(i,j)){
                       if (0!=data2[i][j]&&data2[i][j]<ParameterControl.AREATOP){
                           if (data2[i][j]>ParameterControl.AREABOTTOM) {
                               if (data2[i][j]>mindata){
                                   mindata=data2[i][j];
                                   a=i;
                                   b=j;
                               }
                           }
                       }
               }

            }
        }
        if (a!=0&&b!=0){
            return planePoint.getType(a,b);
        }

     return 0;
    }
   public void ondestory(){
       isRun=false;
   }
   public void restart(){
       isRun =true;
         isStop=true;
         start();
   }

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }
    private void timeCtrol(int triggleTime){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isStop=true;
            }
        },triggleTime*1000);
    }
}
