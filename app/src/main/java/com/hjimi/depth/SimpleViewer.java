package com.hjimi.depth;

import android.os.Handler;
import android.speech.tts.TextToSpeech;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiFrameType;
import com.hjimi.api.iminect.ImiImageFrame;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.Utils;
import com.hjimi.depth.depth.DepthDraw;
import com.hjimi.depth.depth.DepthRect;
import com.hjimi.depth.devices.ParameterControl;
import com.hjimi.depth.utils.Mlog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;



public class SimpleViewer extends Thread {

    private boolean mShouldRun = false;

    private ImiDevice mDevice;
    private ImiFrameType mFrameType;
    private GLPanel mGLPanel;
    private int m_DepthValue = 0;
    private int mFps = 0;
    private int mCount = 0;
    private int m_width = 320;
    private int m_height = 240;
    private Handler handler ;

    private byte[] color = new byte[3];

    public SimpleViewer(ImiDevice device, ImiFrameType frameType) {
        mDevice = device;
        mFrameType = frameType;
    }
    public SimpleViewer(ImiDevice device, ImiFrameType frameType, Handler handler) {
        mDevice = device;
        mFrameType = frameType;
        this.handler = handler;
    }

    public void setGLPanel(GLPanel GLPanel) {
        this.mGLPanel = GLPanel;
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
    public short[][]  trans2short2(short[] data){
        short[][]  sd=new short[ParameterControl.CAMERA3DH][ParameterControl.CAMERA3DW];
        int position=0;
        for (int j = 0; j <ParameterControl.CAMERA3DW ; j++) {
          for (int i = 0; i <ParameterControl.CAMERA3DH ; i++) {

                sd[i][j]=data[position];
                position++;
            }
        }
        return sd;
    }
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
private short[][] original;
    @Override
    public void run() {
        super.run();

        //open stream.
        mDevice.openStream(mFrameType);

        color[0] = (byte)0xff;
        color[1] = (byte)0x00;
        color[2] = (byte)0x00;

        long startTime = 0;
        long endTime = 0;

        //start read frame.
        while (mShouldRun) {
            ImiImageFrame nextFrame = mDevice.readNextFrame(mFrameType, 25);

            //frame maybe null, if null, continue.
            if(nextFrame == null){
                continue;
            }
            ByteBuffer data = nextFrame.getData().order(ByteOrder.nativeOrder());
            short[]  comdata=new short[240*320];
            data.asShortBuffer().get(comdata);
            original=trans2short2(comdata);

            int maxValue = DepthRect.getDepthrect().startScan(original);
            if (maxValue>0){

                handler.obtainMessage(maxValue).sendToTarget();
            }

            //  minValue(original);
            mCount++;
            if(startTime == 0) {
                startTime = System.currentTimeMillis();
            }

            endTime = System.currentTimeMillis();
            if(endTime - startTime >= 1000) {
                mFps = mCount;
                mCount = 0;
                startTime = endTime;
            }

            //draw color.
            drawDepth(nextFrame);
        }
    }

    public int getDepthValue() {
        return m_DepthValue;
    }

    public int getFps() {
        return mFps;
    }

    private void drawDepth(ImiImageFrame nextFrame) {
        ByteBuffer frameData = nextFrame.getData();
        int width = nextFrame.getWidth();
        int height = nextFrame.getHeight();
        m_width = width;
        m_height = height;

        int X = width/2;
        int Y = height/2;
        int curIndex = width * Y + X;

        frameData.position(curIndex*2);
        m_DepthValue = (int) ((frameData.get() & 0xFF) | ((frameData.get() & 0xFF)<<8));

        //get rgb data
        frameData = Utils.depth2RGB888(nextFrame, false, false);

        int index = width * Y + X;

        if(width < 640) {
         /*   frameData.position((index - 1) * 3);
            for(int i = 0; i < 3; ++i) {
                frameData.put(color);
            }

            frameData.position((index - 1 - width) * 3);
            for(int i = 0; i < 3; ++i) {
                frameData.put(color);
            }

            frameData.position((index - 1 + width) * 3);
            for(int i = 0; i < 3; ++i) {
                frameData.put(color);
            }*/
      //   drawLine(frameData);
        }
        else {
            frameData.position((index - 2) * 3);
            for(int i = 0; i < 5; ++i) {
                frameData.put(color);
            }

            int index2 = index - width;
            frameData.position((index2 - 2) * 3);
            for(int i = 0; i < 5; ++i) {
                frameData.put(color);
            }

            index2 -= width;
            frameData.position((index2 - 2) * 3);
            for(int i = 0; i < 5; ++i) {
                frameData.put(color);
            }

            int index3 = index + width;
            frameData.position((index3 - 2) * 3);
            for(int i = 0; i < 5; ++i) {
                frameData.put(color);
            }

            index3 += width;
            frameData.position((index3 - 2) * 3);
            for(int i = 0; i < 5; ++i) {
                frameData.put(color);
            }
        }

        //draw depth image.
        mGLPanel.paint(null, frameData, width, height);
    }

    public int getStreamWidth(){
        return m_width;
    }

    public int getStreamHeight(){
        return m_height;
    }

    public void onPause(){
        if(mGLPanel != null){
            mGLPanel.onPause();
        }
    }

    public void onResume(){
        if(mGLPanel != null){
            mGLPanel.onResume();
        }
    }

    public void onStart(){
        if(!mShouldRun){
            mShouldRun = true;

            //start read thread
            this.start();
        }
    }

    public void onDestroy(){
        mShouldRun = false;

        //destroy stream.
        mDevice.closeStream(mFrameType);
    }


}
