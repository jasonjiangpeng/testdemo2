package com.hjimi.depth.depth;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by Administrator on 2017/12/26.
 */

public class TriangleD {
    private Point  point;
    private int weight;
    private int height;
    private int rows=1;
    private int cols=1;
    public TriangleD() {
    }
    public TriangleD(Point point, int weight, int height, int rows, int cols) {
        this.point = point;
        this.weight = weight;
        this.height = height;
        this.rows = rows;
        this.cols = cols;

    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getCols() {
        return cols;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
    public Point getPoint() {
        return point;
    }
    public void setPoint(Point point) {
        this.point = point;
    }
    public int getWeight() {
        return weight;
    }
    public void setWeight(int weight) {
        this.weight = weight;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public Rect  getRect(){
        return new Rect(point.x,point.y,point.x+weight,point.y+height);
    }
    public int xdivide(){
        return  weight/cols;
    }
    public int ydivide(){
        return  height/rows;
    }
    public int  rectCounts(){
        return cols*rows;
    }
    public int getx(){
        return point.x;
    }
    public int gety(){
        return point.y;
    }
}
