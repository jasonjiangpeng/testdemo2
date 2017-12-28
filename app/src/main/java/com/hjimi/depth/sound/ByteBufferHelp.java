package com.hjimi.depth.sound;

import com.hjimi.api.iminect.ImiImageFrame;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ByteBufferHelp {
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

        ByteBuffer wrap = ByteBuffer.allocate(bytes.length);
             wrap.put(bytes);
             wrap.rewind();

        return  wrap;
    }
    public static ByteBuffer byteBufferOpearter(ByteBuffer byteBuffer){

        short[] shorts = bytebuffer2ShortArray(byteBuffer);
        return bytes2ByteBuffer(shortToByteArray(shorts));
    }
    public static void reflestObject(ImiImageFrame nextFrame, ByteBuffer newBytebuffer) {

        Field field = null;
        Field field2 = null;
        Method method=null;
        byte sd=1;
        try {
            field = ImiImageFrame.class.getDeclaredField("mData");
            method = ByteBuffer.class.getDeclaredMethod("put",int.class,byte.class);
            method.setAccessible(true);
            field.setAccessible(true);
            for (int i = 0; i <100000 ; i++) {

                method.invoke(newBytebuffer,i,sd);
            }

            field.set(nextFrame, newBytebuffer);

        } catch (NoSuchFieldException e) {
            System.out.println("NoSuchFieldException");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
