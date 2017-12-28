package com.hjimi.depth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiDeviceAttribute;
import com.hjimi.api.iminect.ImiDeviceState;
import com.hjimi.api.iminect.ImiDriverInfo;
import com.hjimi.api.iminect.ImiErrorCode;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiFrameType;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.ImiPixelFormat;
import com.hjimi.depth.colorview.ColorViewer;
import com.hjimi.depth.colorview.DecodePanel;

import com.hjimi.depth.colorview.GLPanel2;
import com.hjimi.depth.depth.DepthRect;
import com.hjimi.depth.depth.Whrc;
import com.hjimi.depth.ui.SetValueUI;
import com.hjimi.depth.ui.TextViewDouble;
import com.hjimi.depth.utils.Mlog;

import java.util.Locale;

import static com.hjimi.api.iminect.ImiDriverType.IMI_DRIVER_FILE;
import static com.hjimi.api.iminect.ImiDriverType.IMI_DRIVER_NET;
import static com.hjimi.api.iminect.ImiDriverType.IMI_DRIVER_USB;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    private GLPanel mGLPanel;
    private GLPanel2 mGLPanel0;

    private TextView tvDepthValue;

    private ImiDevice imiDevice;
    private SimpleViewer mViewer = null;
    private ColorViewer colorViewer = null;

    private ImiFrameMode mCurrDepthMode = null;
    private ImiDeviceAttribute mDeviceAttribute = null;
    private static final int DEVICE_OPEN_SUCCESS = 0;
    private static final int DEVICE_OPEN_FALIED = 1;
    private static final int DEVICE_DISCONNECT = 2;
    private static final int SHOW_DEPTH_VALUE = 3;
    private ImiFrameMode colormode;
    private DecodePanel decodePanel;
    private SetValueUI  setValueUI;
    private TextView tv2;
    private TextViewDouble textViewDouble;


    private ImiDeviceState deviceState = ImiDeviceState.IMI_DEVICE_STATE_CONNECT;

    private Handler MainHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case DEVICE_OPEN_FALIED:
                case DEVICE_DISCONNECT:
                    showMessageDialog();
                    break;
                case DEVICE_OPEN_SUCCESS:
                    runViewer();
                    break;
                case SHOW_DEPTH_VALUE:
                    ShowDepthValue();
                    break;
            }
        }
    };

    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The device is not connected!!!");
        builder.setPositiveButton("quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {

        tv2.setText("触发点发生在"+msg.what+"区域");
        Point  point =DepthRect.getDepthrect().getpoint();
        String  v1="触发距离长:"+point.x;
        String  v2="触发距离宽:"+point.y;
        textViewDouble.setValue(v1,v2);

    }
};
    private void runViewer() {
        //create viewer
        colorViewer = new ColorViewer(imiDevice, ImiFrameType.COLOR);
        mViewer = new SimpleViewer(imiDevice, ImiFrameType.DEPTH,handler);

        colorViewer.setGLPanel(mGLPanel0);
         mViewer.setGLPanel(mGLPanel);
        //start viewer
        colorViewer.onStart();
        mViewer.onStart();

        MainHandler.sendEmptyMessage(SHOW_DEPTH_VALUE);
    }

    private void ShowDepthValue() {
        tvDepthValue.setText(String.format(Locale.getDefault(), "Depth %dx%d@%dFPS      ", mViewer.getStreamWidth(), mViewer.getStreamHeight(), mViewer.getFps()));
        tvDepthValue.append("Point(" + mViewer.getStreamWidth()/2 + "," + mViewer.getStreamHeight()/2 + "): " + mViewer.getDepthValue() + " mm");
        MainHandler.sendEmptyMessageDelayed(SHOW_DEPTH_VALUE, 15);
    }



    private class MainListener implements ImiDevice.OpenDeviceListener,ImiDevice.DeviceStateListener {
        @Override
        public void onOpenDeviceSuccess() {
            mDeviceAttribute = imiDevice.getAttribute();
            colormode=imiDevice.getCurrentFrameMode(ImiFrameType.COLOR);

            setDepthExpectMode(320, 240);
            MainHandler.sendEmptyMessage(DEVICE_OPEN_SUCCESS);
        }

        @Override
        public void onOpenDeviceFailed(String errorMsg) {
            MainHandler.sendEmptyMessage(DEVICE_OPEN_FALIED);
        }

        @Override
        public void onDeviceStateChanged(String deviceUri, ImiDeviceState state) {
            if (state == ImiDeviceState.IMI_DEVICE_STATE_DISCONNECT) {
                //device disconnect.
                //Toast.makeText(MainActivity.this, deviceUri+" DISCONNECT", Toast.LENGTH_SHORT).show();
                if(mDeviceAttribute != null && mDeviceAttribute.getUri().equals(deviceUri)) {
                    deviceState = ImiDeviceState.IMI_DEVICE_STATE_DISCONNECT;
                    MainHandler.sendEmptyMessage(DEVICE_DISCONNECT);
                }
            }else if(state == ImiDeviceState.IMI_DEVICE_STATE_CONNECT){
                //Toast.makeText(MainActivity.this, deviceUri+" CONNECT", Toast.LENGTH_SHORT).show();
                if(mDeviceAttribute != null && mDeviceAttribute.getUri().equals(deviceUri)) {
                    deviceState = ImiDeviceState.IMI_DEVICE_STATE_CONNECT;
                }
            }
        }
    }

    private void setDepthExpectMode(int width, int height) {
        ImiFrameMode frameMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_DEP_16BIT, width, height);
        imiDevice.setFrameMode(ImiFrameType.DEPTH, frameMode);

}

    private class OpenDeviceRunnable implements Runnable{

        @Override
        public void run() {
            ImiNect    m_ImiNect = ImiNect.create(MainActivity.this);
            imiDevice=m_ImiNect.Device();
            MainListener listener = new MainListener();
            imiDevice.addDeviceStateListener(listener);
            imiDevice.open(listener);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depth_view);
        tv2= (TextView) findViewById(R.id.tv2);
        textViewDouble = (TextViewDouble) findViewById(R.id.textviewdou);

        mGLPanel = (GLPanel) findViewById(R.id.sv_depth_view);
        mGLPanel0 = (GLPanel2) findViewById(R.id.sv_depth_view0);
        setValueUI= (SetValueUI) findViewById(R.id.setvalueview);
        tvDepthValue = (TextView) findViewById(R.id.tv_depthvalue);

        new Thread(new OpenDeviceRunnable()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mViewer != null){
            mViewer.onResume();
        }
        if (colorViewer!=null){
            colorViewer.onResume();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mViewer != null){
            mViewer.onPause();
        }

        if(mViewer != null){
            mViewer.onDestroy();
        }
        if (colorViewer!=null){
            colorViewer.onPause();
        }
        imiDevice.close();

        ImiNect.create(this).destroy();

        finish();

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (colorViewer!=null){
            colorViewer.onDestroy();
        }
    }
}
