package com.hjimi.depth.sound;

import com.hjimi.api.iminect.ImiImageFrame;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ByteBufferHelp2 {
    public static byte[] shortToByteArray(short[] src) {
        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2] = (byte) (src[i] >> 0);
            dest[i * 2 + 1] = (byte) (src[i] >> 8);
        }
        return dest;
    }
    public static short[] bytebuffer2ShortArray(ByteBuffer byteBuffer){
        short[]  shorts=new short[byteBuffer.capacity()>>1];
        ByteBuffer order = byteBuffer.order(ByteOrder.nativeOrder());
        order.asShortBuffer().get(shorts);
        return shorts;
    }
    public static ByteBuffer bytes2ByteBuffer(byte[] bytes){
        ByteBuffer wrap = ByteBuffer.wrap(bytes).order(ByteOrder.nativeOrder());
           wrap.flip();

        return  wrap;
    }
    public static ByteBuffer byteBufferOpearter(ByteBuffer byteBuffer){
        short[] shorts = bytebuffer2ShortArray(byteBuffer);
        return bytes2ByteBuffer(shortToByteArray(shorts));
    }
    public static void reflestObject(ImiImageFrame nextFrame, ByteBuffer newBytebuffer) {

        Field field = null;
        try {
            field = ImiImageFrame.class.getDeclaredField("mData");
            field.setAccessible(true);
            field.set(nextFrame, newBytebuffer);

        } catch (NoSuchFieldException e) {
            System.out.println("NoSuchFieldException");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException");
            e.printStackTrace();
        }

    }
}
