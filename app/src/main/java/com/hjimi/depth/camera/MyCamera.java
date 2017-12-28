package com.hjimi.depth.camera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;


import com.hjimi.depth.R;
import com.hjimi.depth.devices.DevicesImi;
import com.hjimi.depth.utils.Mlog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Arrays;


@EActivity(R.layout.cameramain)
public class MyCamera extends Activity {
private DevicesImi devicesImi;
private Handler handler =new Handler(){
    @Override
    public void handleMessage(Message msg) {
        Mlog.log("=====================");
    }
};
    @AfterViews
    public void afterView() {
        devicesImi=new DevicesImi(this,handler);
        devicesImi.start();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //   drawRect();
                initCamera2();

            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {

            }
        });

    }

private Handler  childHandle;
private Handler  mainHanlde;
    private ImageReader imagereader;

    @Override
    protected void onDestroy() {
        devicesImi.destory();
        super.onDestroy();

    }

    private void initCamera2() {
        HandlerThread han=new HandlerThread("Camera2");
        han.start();
        childHandle =new Handler(han.getLooper());
        mainHanlde=new Handler(getMainLooper());
        imagereader=ImageReader.newInstance(1080,1920, ImageFormat.JPEG,1);
        imagereader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {


            }
        },mainHanlde);
        CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraManager.openCamera(cameraIdList[0], stateCallback,mainHanlde);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @ViewById(R.id.surfaceView)
    SurfaceView  surfaceView;

private CameraDevice mCameraDevice;
    CameraDevice.StateCallback  stateCallback =new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice=camera;
           takePreview();

        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
             if (mCameraDevice!=null){
                 mCameraDevice.close();
                 mCameraDevice=null;
             }
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };



    private CameraCaptureSession mCameraCaptureSession ;

private void takePreview(){
    try {
        final CaptureRequest.Builder capture=mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        capture.addTarget(surfaceView.getHolder().getSurface());

         mCameraDevice.createCaptureSession(Arrays.asList(surfaceView.getHolder().getSurface(), imagereader.getSurface()), new CameraCaptureSession.StateCallback() {
             @Override
             public void onConfigured(@NonNull CameraCaptureSession session) {
              if (mCameraDevice==null){
                  return;
              }
                 mCameraCaptureSession=session;
                 try {
                     capture.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                     // 打开闪光灯
                     capture.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                     // 显示预览
                     CaptureRequest previewRequest = capture.build();
                     mCameraCaptureSession.setRepeatingRequest(previewRequest, null, childHandle);
                 } catch (CameraAccessException e) {
                     e.printStackTrace();
                 }

             }

             @Override
             public void onConfigureFailed(@NonNull CameraCaptureSession session) {

             }
         },childHandle);
    } catch (CameraAccessException e) {
        e.printStackTrace();
    }
}



}

