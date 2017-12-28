package com.hjimi.depth.devices;

/**
 * Created by Administrator on 2017/12/21.
 */

public class ParameterControl {
    public static  final int AREATOP=1300;
    public static  final int AREABOTTOM=1270;
    public static  final int CAMERA3DW=240;
    public static  final int CAMERA3DH=320;
    private final  int errorValue=15*100;
    public volatile static  int  wscreen=100;
    public volatile static  int  hscreen=100;
    public volatile static  int  ROWS=4;
    public volatile static  int  CLOS=5;
}
