package com.hjimi.depth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

import com.hjimi.depth.depth.DepthDraw;
import com.hjimi.depth.depth.DepthRect;
import com.hjimi.depth.depth.TriangleD;
import com.hjimi.depth.depth.Whrc;
import com.hjimi.depth.devices.ParameterControl;
import com.hjimi.depth.utils.Mlog;

import java.nio.ByteBuffer;
import java.util.logging.Handler;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

@SuppressLint("NewApi")
public class GLPanel extends GLSurfaceView
{
    private GLGraphics mGLGraphics;
    private ByteBuffer mBufferImage;

    private boolean bWork = true;
    private int mFrameWidth;
    private int mFrameHeight;
    private boolean  isFirst=false;
    private Whrc whrc=new Whrc(100,100,1,5);

    public Whrc getWhrc() {
        return whrc;
    }

    public void setWhrc(Whrc whrc) {
        this.whrc = whrc;
    }

    @SuppressLint("NewApi")
    public GLPanel(Context context) {
        super(context);
        init(context);
    }

    public GLPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        setRenderer(new Scene());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void paint(float[] vertices, ByteBuffer buffer, int width, int height){
		if(mGLGraphics == null){
			return;
		}
        mFrameWidth = width;
        mFrameHeight = height;
		mGLGraphics.setVertexData(vertices);
        mBufferImage = buffer;
        if (!isFirst){
            drawLine(mBufferImage);
        }
        if(bWork){
            requestRender();
        }

	}

    @Override
    public void onPause() {
        super.onPause();
        bWork = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        bWork = true;
    }
    DepthDraw depthDraw =new DepthDraw();
    private void drawLine(ByteBuffer byteBuffer){

        if (point.x+whrc.getW()>=320||point.y+whrc.getH()>=240){
            new android.os.Handler(getContext().getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(),"坐标值和宽高超出界面值",Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }
        depthDraw.setRect(DepthRect.getDepthrect().getTriangleD2());
        depthDraw.drawMulRect(byteBuffer);
    }
    @SuppressLint("NewApi")
    private class Scene implements Renderer {
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            if (mBufferImage != null) {
                mBufferImage.position(0);
                mGLGraphics.buildTextures(mBufferImage, mFrameWidth, mFrameHeight);
            }
            mGLGraphics.draw();
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            mGLGraphics = new GLGraphics();
            if (!mGLGraphics.isProgramBuilt()) {
                mGLGraphics.buildProgram();
            }
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }
    }
 private     Point  point=new Point(50,50);
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction()==MotionEvent.ACTION_DOWN){

            int  x= (int) (event.getX()/2);
            int  y= (int) (event.getY()/2);
            point =new Point(x,y);
            DepthRect.getDepthrect().getTriangleD().setPoint(point);
        }
        return super.onTouchEvent(event);
    }
}
