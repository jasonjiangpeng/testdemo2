package com.hjimi.depth.camera;

import android.Manifest;
import android.content.Context;

import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hjimi.depth.utils.Mlog;

import java.io.IOException;
import java.util.Arrays;

import static android.content.Context.CAMERA_SERVICE;

/**
 * Created by Administrator on 2017/12/26.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {


    private static SurfaceHolder holder;


    public MySurfaceView(Context context) {
        this(context,null);

    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();//后面会用到！
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Mlog.log("==========surfaceCreated============");
             initCamera2();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    private Handler childHandle;
    private Handler mainHanlde;
    private ImageReader imageReader;
    private void initCamera2() {
        HandlerThread han=new HandlerThread("Camera2");
        han.start();
        childHandle =new Handler(han.getLooper());
        mainHanlde=new Handler(getContext().getMainLooper());
        imageReader=ImageReader.newInstance(1080,1920, ImageFormat.JPEG,1);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {

            }
        },mainHanlde);
        CameraManager cameraManager = (CameraManager) getContext().getSystemService(CAMERA_SERVICE);
        try {
            String[] cameraIdList = cameraManager.getCameraIdList();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraManager.openCamera(cameraIdList[0], stateCallback,mainHanlde);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private CameraDevice mCameraDevice;
    private  CameraCaptureSession mCameraCaptureSession ;
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
    private void takePreview() {
        try {
            final CaptureRequest.Builder capture = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            capture.addTarget(holder.getSurface());
            mCameraDevice.createCaptureSession(Arrays.asList(holder.getSurface(), imageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (mCameraDevice == null) {
                        return;
                    }
                    mCameraCaptureSession = session;
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
            }, childHandle);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
