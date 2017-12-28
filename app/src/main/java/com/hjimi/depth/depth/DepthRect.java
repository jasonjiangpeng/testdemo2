package com.hjimi.depth.depth;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

import com.hjimi.depth.utils.Mlog;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */

public class DepthRect{
    private static DepthRect depthRect;
    private int limitDex=2;
    private int offset=50;
    private  int comValue=1320;
    private boolean readScan=false;
   public Point  getpoint(){
       return new Point(triangleD.getWeight(),triangleD.getHeight());
   }
    public boolean isReadScan() {
        return readScan;
    }

    public void setReadScan(boolean readScan) {
        this.readScan = readScan;
    }

    private DepthRect() {
        this.triangleD=new TriangleD(new Point(100,50),100,100,2,4);

    }
public static DepthRect getDepthrect(){
        if (depthRect==null){
            synchronized (DepthRect.class){
                if (depthRect==null){
                    depthRect=new DepthRect();
                }
            }
        }
        return depthRect;
}
private TriangleD  triangleD;
private List<Rect>  rects;
    private List<Integer>  limits;
    public List<Rect> getrect(){
        List<Rect> rects1=new ArrayList<>();
        int  x=triangleD.getx();
        int  y=triangleD.gety();
        int  xde=triangleD.xdivide();
        int  yde=triangleD.ydivide();
        for (int i = 0; i <triangleD.getRows() ; i++) {
            for (int j = 0; j <triangleD.getCols() ; j++) {
            Rect  rect =new Rect(x+j*xde,y+i*yde,x+j*xde+xde,y+i*yde+yde);
                rects1.add(rect);
        }
      }
      return  rects1;
    }
    public void destroy(){
        if (rects!=null){
            rects.clear();
        }
        if (limits!=null){
            limits.clear();
        }
    }
    public TriangleD getTriangleD() {
        readScan=false;
        return triangleD;
    }
    public TriangleD getTriangleD2() {
        return triangleD;
    }
    public void setTriangleD(TriangleD triangleD) {
        this.triangleD = triangleD;
    }
    public List<Rect> getRects() {
        return rects;
    }
    public int getMaxValue(short[][]  shorts){
        if (rects==null){
            rects=getrect();
        }
        if (limits==null){
            limits=getLimits(shorts,rects);
            for (int i = 0; i <limits.size() ; i++) {
                Mlog.log(i+"位置"+limits.get(i));
            }
        }
        int  a=-999;
      for (int m = 0; m <rects.size() ; m++) {
          for (int i = rects.get(m).left+limitDex; i <rects.get(m).right-limitDex ; i++) {
              for (int j = rects.get(m).top+limitDex; j <rects.get(m).bottom-limitDex ; j++) {
                    if (isRanglevalue(shorts[i][j],limits.get(m))){
                       a=m+1;
                       break;
                    }
              }
          }

      }
      return a;
  }
  private  boolean isfirst=true;
  public int startScan(short[][]  shorts){
        int  o=-1;
      if (readScan){
          o=getMaxValue(shorts);
        }else {
          if (limits!=null){
              limits.clear();
              limits=null;
          }
          if (rects!=null){
              rects.clear();
              rects=null;
          }


      }
        return o;
  }
  public List<Integer> getLimits(short[][]  shorts,List<Rect> rects){
        List<Integer> save=new ArrayList<>();
       for (int m = 0; m <rects.size() ; m++) {
          int c=9999;
          for (int i = rects.get(m).left+limitDex; i <rects.get(m).right-limitDex ; i++) {
              for (int j = rects.get(m).top+limitDex; j <rects.get(m).bottom-limitDex ; j++) {
                     if (shorts[i][j]!=0&&c>shorts[i][j]){
                         c=shorts[i][j];
                     }
              }
          }
           save.add(c);
      }
      return save;
  }

  public boolean isRanglevalue(int v){
      int value=v-comValue;
      if (   value>50&&value<100){
          return true;
      }
      return false;
  }
    public boolean isRanglevalue(int v,int v2){
        int value=v2-v;
        if (   value>50&&value<90){
            return true;
        }
        return false;
    }



}
