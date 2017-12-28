package com.hjimi.depth.utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;
import java.lang.reflect.Field;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
/**
 * Created by Administrator on 2017/12/25.
 */

public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG ="CrashHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler instance=new CrashHandler();
    private Context mcontext;

    /*使用Properties 来保存设备的信息和错误堆栈信息*/
    private Properties mDeviceCrashInfo =new Properties();

    //存储设备信息
    private Map<String,String> mDevInfos=new HashMap<String, String>();
    private SimpleDateFormat formatdate=new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private CrashHandler(){

    }

    //保证只有一个实例
    public static CrashHandler getInstance()
    {
        if(instance==null)
            synchronized (CrashHandler.class){
            if (instance==null){
                instance =new CrashHandler();
            }

            }

        return instance;
    }
    public void init(Context context)
    {
        mcontext=context;
        //获得默认的handle
        mDefaultHandler=Thread.getDefaultUncaughtExceptionHandler();
        //重新设置handle  设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);

    }
    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    public void uncaughtException(Thread thread, Throwable ex) {
// TODO Auto-generated method stub
//如果用户没有处理则让系统默认的异常处理器来处理
        Mlog.log("===============================thread 错误"+   ex.getMessage());
        Mlog.log("===============================thread 错误"+   ex.toString());
        Mlog.log("===============================thread 错误"+   ex.getLocalizedMessage());
        Mlog.log("===============================thread 错误"+   thread.getName());
        StringBuffer stringBuffer= new StringBuffer();
        StackTraceElement[] stackTrace = ex.getStackTrace();
           if (stackTrace!=null){
         //      Mlog.log("===============================thread 错误"+   stackTrace.length);
               for(StackTraceElement stackTraceElement:stackTrace){
                   stringBuffer.append("类名:"+stackTraceElement.getClassName()+"\n");
                   stringBuffer.append("文件名:"+stackTraceElement.getFileName()+"\n");
                   stringBuffer.append("行数:"+stackTraceElement.getLineNumber()+"\n");
                   stringBuffer.append("方法名:"+stackTraceElement.getMethodName()+"\n");
               }
           }
           writeData(stringBuffer.toString());
               System.exit(0);

    }
    private void writeData(String  value){
        try {
            FileOutputStream  fileOutputStream =mcontext.openFileOutput("data3.txt",Context.MODE_PRIVATE);
            fileOutputStream.write(value.getBytes());
            fileOutputStream.flush();

            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    public String getErrLog(){
        FileInputStream fileInputStream=null;
        try {
             fileInputStream  = mcontext.openFileInput("data3.txt");
 if (fileInputStream==null){
     return null;
 }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String line = "";
        try {
            InputStreamReader reader = new InputStreamReader(fileInputStream);
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
    public String readFile() {
        FileInputStream inputStream;
        byte[] buffer = null;
        try {
            inputStream = mcontext.openFileInput("data3.txt");
            try {
                // 获取文件内容长度
                int fileLen = inputStream.available();
                // 读取内容到buffer
                buffer = new byte[fileLen];
                inputStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 返回文本信息
        if (buffer != null){
            try {
                return new String(buffer,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "";
            }
        }

        else{
            return "";
        }



    }


}

