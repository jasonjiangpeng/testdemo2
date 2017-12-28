package com.hjimi.depth.devices;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiDeviceAttribute;
import com.hjimi.api.iminect.ImiDeviceState;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiFrameType;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.ImiPixelFormat;



/**
 * Created by Administrator on 2017/12/25.
 */

public class DevicesImi {
    private Context context;
    private ImiNect imiNect;
    private Handler handler;
    private  RunTheard runTheard;

    private  int SUCESS=1000;
    private  int FAIL=999;
    private  int Change=889;

 //   private ImiDeviceAttribute mDeviceAttribute;

    public DevicesImi(Context context, Handler handler) {
        this.context=context;
        this.handler=handler;
            init();
    }
    public void resume(){
        if (runTheard!=null){
            runTheard.setStop(true);
        }

    }
    public void stop(){
        if (runTheard!=null) runTheard.setStop(false);
    }
    public void destory(){
        if (runTheard!=null){
            runTheard.ondestory();
        }


    }
    private void init(){
        imiNect=ImiNect.create(context);
        DeviceLister deviceLister=new DeviceLister();
        imiNect.Device().addDeviceStateListener(deviceLister);
        imiNect.Device().open(deviceLister);
    }

    private void setDepthExpectMode(int width, int height) {
        ImiFrameMode frameMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_DEP_16BIT, width, height);
        imiNect.Device().setFrameMode(ImiFrameType.DEPTH, frameMode);
     }
    public void start(){
        runTheard =new RunTheard(imiNect.Device(),handler);
        runTheard.restart();
    }
    private class DeviceLister implements ImiDevice.OpenDeviceListener,ImiDevice.DeviceStateListener {
        @Override
        public void onOpenDeviceSuccess() {
        //    mDeviceAttribute = imiNect.Device().getAttribute();
            setDepthExpectMode(320, 240);
            handler.obtainMessage(SUCESS).sendToTarget();
        }

        @Override
        public void onOpenDeviceFailed(String errorMsg) {
            handler.obtainMessage(FAIL).sendToTarget();

        }

        @Override
        public void onDeviceStateChanged(String deviceUri, ImiDeviceState state) {
            if (state == ImiDeviceState.IMI_DEVICE_STATE_DISCONNECT) {
                //device disconnect.
                //Toast.makeText(MainActivity.this, deviceUri+" DISCONNECT", Toast.LENGTH_SHORT).show();
            //    if(mDeviceAttribute != null && mDeviceAttribute.getUri().equals(deviceUri)) ;
                  //  deviceState = ImiDeviceState.IMI_DEVICE_STATE_DISCONNECT
                handler.obtainMessage(Change).sendToTarget();
            }else if(state == ImiDeviceState.IMI_DEVICE_STATE_CONNECT){
                //Toast.makeText(MainActivity.this, deviceUri+" CONNECT", Toast.LENGTH_SHORT).show();
              //  if(mDeviceAttribute != null && mDeviceAttribute.getUri().equals(deviceUri))   ;
                    // deviceState = ImiDeviceState.IMI_DEVICE_STATE_CONNECT;


            }
        }
    }
}
