package com.hjimi.depth.devices;

import android.graphics.Point;
import android.graphics.Rect;

import com.hjimi.depth.utils.Mlog;

/**
 * Created by Administrator on 2017/12/21.
 */

public class PlanePoint {
    private Point point =new Point(120,160);
    private  int  xrows=240;
    private  int  ycols=320;
    private  int  pointx=120;
    private  int  pointy=160;
    private  float coefficient=0.55f;
    private  int  rows= (int) (xrows*coefficient);
    private  int  cols= (int) (ycols*coefficient);
    private  int leftborder= (int) ((xrows-rows)/2);
    private  int topborder= (int) ((ycols-cols)/2);
    private  int rightborder=leftborder+rows;
    private  int bottomborder=topborder+cols;
    private Rect rect =new Rect(leftborder,topborder,rightborder,bottomborder);
    public Point point1=new Point(leftborder+rows/4, topborder+cols/6);
    public Point point2=new Point(leftborder+rows/4, topborder+cols/2);
    public Point point3=new Point(leftborder+rows/4, topborder+cols*5/6);
    public Point point4=new Point(leftborder+rows*3/4, topborder+cols/6);
    public Point point5=new Point(leftborder+rows*3/4, topborder+cols/2);
    public Point point6=new Point(leftborder+rows*3/4, topborder+cols*5/6);
    private int radius=(rows/4)-1;
    public Rect getRect(){
        return rect;
    }
    public int  getType(int x, int y){
        type=0;
        if (calucatevalue(point1,x,y)){
         return    type=1;
        }
        if (calucatevalue(point2,x,y)){
            return  type=2;
        }
        if (calucatevalue(point3,x,y)){
            return  type=3;
        }
        if (calucatevalue(point4,x,y)){
            return  type=4;
        }
        if (calucatevalue(point5,x,y)){
            return  type=5;
        }
        if (calucatevalue(point6,x,y)){
            return  type=6;
        }
        return type;
    }
    public boolean calucatevalue(Point point, int x, int y){
        double  value=  Math.pow( Math.abs(point.x-x),2)+  Math.pow( Math.abs(point.y-y),2);
        if (Math.sqrt(value)<radius){
            return true;
        }
        return false;
    }
    private int type=0;
    public void   maxAndMinPoint(short[][]  shorts){
        int  xmin=99999;
        int  ymax=0;
        for (int i = leftborder; i <rightborder ; i++) {
            for (int j = topborder; j <bottomborder ; j++) {
                int  c=shorts[i][j];
                if (c<xmin){
                    xmin=c;
                }
                if (c>ymax){
                    ymax=c;
                }

            }
        }
        Mlog.log("最小值"+xmin);
        Mlog.log("最大值"+ymax);

    }
    public int   scopeTest(short[][]  shorts){
    int type=0;
        int limit=1400;
        for (int i = leftborder; i <rightborder ; i++) {
            for (int j = topborder; j <bottomborder ; j++) {
                  if (shorts[i][j]!=0&&shorts[i][j]<1400){
                      type=1;
                      break;
                  }

            }
        }
       return type;
    }

}
