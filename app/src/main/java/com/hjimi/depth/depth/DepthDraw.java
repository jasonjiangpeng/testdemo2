package com.hjimi.depth.depth;

import android.graphics.Rect;

import com.hjimi.depth.utils.Mlog;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2017/12/26.
 */

public class DepthDraw {
    private  int  weight=320;
    private  int  height=240;
    private  int bytelenght=3;
    private  Rect rect;
    private byte[] color = new byte[3];
    private volatile int row=1;
    private volatile int col=1;

    public DepthDraw() {
        color[0]= (byte) 0xff;
        color[1]=0x00;
        color[2]=0x00;
    }

    public Rect getRect() {
        return rect;
    }
    public void setRect(int  left,int right,int top,int bottom) {
        this.rect = new Rect(left,top,right,bottom);
    }
    public void setRect(TriangleD top) {
        this.rect = new Rect(top.getPoint().x,top.getPoint().y,top.getPoint().x+top.getWeight(),top.getPoint().y+top.getHeight());

        this.row=top.getRows();
        this.col=top.getCols();
    }
    public void setRect(Rect rect) {
        this.rect = rect;
    }
    public void drawRect(ByteBuffer byteBuffer){
        int left=rect.left;
        int right=rect.right;
        int top=rect.top;
        int bottom=rect.bottom;
        byteBuffer.position(top*weight*bytelenght+left*bytelenght);
        int  w=right-left;
        for (int i = 0; i <w ; i++) {
            byteBuffer.put(color);
        }
        byteBuffer.position(bottom*weight*bytelenght+left*bytelenght);
        for (int i = 0; i <w ; i++) {
            byteBuffer.put(color);
        }
        for (int i = top; i <bottom ; i++) {
            byteBuffer.position(i*weight*bytelenght+left*bytelenght);
            byteBuffer.put(color);
            byteBuffer.position(i*weight*bytelenght+right*bytelenght);
            byteBuffer.put(color);
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void drawMulRect(ByteBuffer byteBuffer){
      //  Mlog.log(getRow()+"列"+getCol()+"=======2======");
        int left=rect.left;
        int right=rect.right;
        int top=rect.top;
        int bottom=rect.bottom;
        byteBuffer.position(top*weight*bytelenght+left*bytelenght);
        int  w=right-left;
        int  h=bottom-top;
        int ydiv=w/getCol();
        int xdiv=h/getRow();
        for (int j = 0; j <getRow()+1 ; j++) {
            byteBuffer.position((top+xdiv*j)*weight*bytelenght + left * bytelenght);
            for (int i = 0; i < w; i++) {
                byteBuffer.put(color);
            }
        }
        for (int i = top; i <bottom ; i++) {
            for (int j = 0; j <getCol()+1 ; j++) {
                byteBuffer.position(i*weight*bytelenght+(ydiv*j+left)*bytelenght);
                byteBuffer.put(color);
            }
        }

    }


}
