package com.hjimi.depth.colorview;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiFrameType;
import com.hjimi.api.iminect.ImiImageFrame;
import com.hjimi.api.iminect.ImiPixelFormat;

/**
 * Created by Administrator on 2017/12/27.
 */

public class DecodeTheard extends Thread {
    private ImiDevice device;
   private ImiFrameType frameType;
   private  boolean  isrun=true;
   private  boolean  isPause=true;
    private ImiFrameMode mCurrentMode;
    private DecodePanel decodePanel;
    public DecodeTheard(ImiDevice device, ImiFrameType frameType,DecodePanel decodePanel) {
        this.device=device;
        this.frameType=frameType;
        this.decodePanel=decodePanel;
    }

    @Override
    public void run() {
        device.openStream(frameType);
        mCurrentMode = device.getCurrentFrameMode(frameType);
        while (isrun){
            ImiImageFrame nextFrame = device.readNextFrame(frameType, 25);
              if (nextFrame==null){
                  continue;
              }
              if (isPause){
                    if (mCurrentMode.getFormat()== ImiPixelFormat.IMI_PIXEL_FORMAT_IMAGE_H264){
                        if(decodePanel != null){
                            decodePanel.paint(nextFrame.getData(), nextFrame.getTimeStamp());
                        }
                    }
              }


        }

    }
   public void  onstart(){
        this.start();
    }
    public void  onresume(){
      isPause=true;
  }
    public void  onstop(){
      isPause=false;
  }
    public  void ondestory(){
      isrun=false;
  }
}
