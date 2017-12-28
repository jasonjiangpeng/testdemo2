package com.hjimi.depth.devices;

import android.graphics.Point;

/**
 * Created by Administrator on 2017/12/20.
 */

public class TypeHelp {
    private int  type=0;

    public TypeHelp() {
    }

    public void setTpye(int x, int y){
        if (x<120){
            if (y<107){
                type=1;
            }else  if (y<214){
                type=2;
            }else {
                type=3;
            }

        }else {
            if (y<107){
                type=4;
            }else  if (y<214){
                type=5;
            }else {
                type=6;
            }
        }
    }
    public void setTpye2(int x, int y){
              if (calucatevalue(point1,x,y)){
            type=1;
        }
        if (calucatevalue(point2,x,y)){
            type=2;
        }
        if (calucatevalue(point3,x,y)){
            type=3;
        }
        if (calucatevalue(point4,x,y)){
            type=4;
        }
        if (calucatevalue(point5,x,y)){
            type=5;
        }
        if (calucatevalue(point6,x,y)){
            type=6;
        }
    }

    public int getType() {

        return type;
    }
    public boolean calucatevalue(Point point, int x, int y){
       double  value=  Math.pow( Math.abs(point.x-x),2)+  Math.pow( Math.abs(point.y-y),2);
        if (Math.sqrt(value)<radius){
            return true;
        }
        return false;
    }
    private Point point1=new Point(60,54);
    private Point point2=new Point(60,162);
    private Point point3=new Point(60,270);
    private Point point4=new Point(180,54);
    private Point point5=new Point(180,162);
    private Point point6=new Point(180,270);
    private int radius=30;
}
