package com.hjimi.depth.colorview;

import android.os.Handler;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiFrameType;
import com.hjimi.api.iminect.ImiImageFrame;
import com.hjimi.api.iminect.ImiPixelFormat;
import com.hjimi.api.iminect.Utils;
import com.hjimi.depth.GLPanel;
import com.hjimi.depth.depth.DepthRect;
import com.hjimi.depth.devices.ParameterControl;
import com.hjimi.depth.utils.Mlog;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class ColorViewer extends Thread {

    private boolean mShouldRun = false;

    private ImiFrameType mFrameType;
    private GLPanel2 mGLPanel;
    private DecodePanel mDecodePanel;
    private ImiDevice mDevice;
    private ImiFrameMode mCurrentMode;

    public ColorViewer(ImiDevice device, ImiFrameType frameType) {
        mDevice = device;
        mFrameType = frameType;
    }

    public void setGLPanel(GLPanel2 GLPanel) {
        this.mGLPanel = GLPanel;

    }

    public void setDecodePanel(DecodePanel decodePanel) {
        this.mDecodePanel = decodePanel;
    }

    @Override
    public void run() {
        super.run();

        //open stream.
        mDevice.openStream(mFrameType);


        //get current framemode.
        mCurrentMode = mDevice.getCurrentFrameMode(mFrameType);

        //start read frame.
        while (mShouldRun) {
            ImiImageFrame nextFrame = mDevice.readNextFrame(mFrameType, 25);

            //frame maybe null, if null, continue.
            if(nextFrame == null){
                continue;
            }

            switch (mFrameType)
            {
                case COLOR:
                    //draw color.
                    drawColor(nextFrame);

            }
        }
    }


    private void drawColor(ImiImageFrame nextFrame) {
        ByteBuffer frameData = nextFrame.getData();
        int width = nextFrame.getWidth();
        int height = nextFrame.getHeight();
Mlog.log("mCurrentMode"+mCurrentMode.getFormat());
        //draw color image.
        switch (mCurrentMode.getFormat())
        {
            case IMI_PIXEL_FORMAT_IMAGE_H264:
                if(mDecodePanel != null){
                    mDecodePanel.paint(frameData, nextFrame.getTimeStamp());
                }
                break;
            case IMI_PIXEL_FORMAT_IMAGE_YUV420SP:
                frameData = Utils.yuv420sp2RGB(nextFrame);
                if(mGLPanel != null){
                    mGLPanel.paint(null, frameData, width, height);
                }
                break;
            case IMI_PIXEL_FORMAT_IMAGE_RGB24:
                if(mGLPanel != null){
                    mGLPanel.paint(null, frameData, width, height);
                }
                break;
            default:
                break;
        }
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
