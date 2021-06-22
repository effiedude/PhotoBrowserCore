package com.townspriter.android.photobrowser.core.model.util;

import android.util.Log;

/******************************************************************************
 * @Path PhotoBrowserCore:LogUtil
 * @Describe 日志打印类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class LogUtil
{
    private static final String APPNAME="PhotoBrowser-";
    private static boolean LOGxIxON=true;
    private static boolean LOGxDxON=true;
    private static boolean LOGxWxON=true;
    
    public static void logI(String classTag,String message)
    {
        if(LOGxIxON)
        {
            classTag=APPNAME+classTag;
            Log.i(classTag,message);
        }
    }
    
    public static void logI(String classTag,String message,Throwable throwable)
    {
        if(LOGxIxON)
        {
            classTag=APPNAME+classTag;
            Log.i(classTag,message,throwable);
        }
    }
    
    public static void logD(String classTag,String message)
    {
        if(LOGxDxON)
        {
            classTag=APPNAME+classTag;
            Log.d(classTag,message);
        }
    }
    
    public static void logD(String classTag,String message,Throwable throwable)
    {
        if(LOGxDxON)
        {
            classTag=APPNAME+classTag;
            Log.d(classTag,message,throwable);
        }
    }
    
    public static void logW(String classTag,String message)
    {
        if(LOGxWxON)
        {
            classTag=APPNAME+classTag;
            Log.w(classTag,message);
        }
    }
    
    public static void logW(String classTag,String message,Throwable throwable)
    {
        if(LOGxWxON)
        {
            classTag=APPNAME+classTag;
            Log.w(classTag,message,throwable);
        }
    }
    
    public static void setLogLevel(LogLevel logLevel)
    {
        switch(logLevel)
        {
            case LOGxLEVELxDEBUG:
                LOGxIxON=false;
                LOGxDxON=true;
                LOGxWxON=true;
                break;
            case LOGxLEVELxWARN:
                LOGxIxON=false;
                LOGxDxON=false;
                LOGxWxON=true;
                break;
            case LOGxLEVELxINFO:
            default:
                LOGxIxON=true;
                LOGxDxON=true;
                LOGxWxON=true;
                break;
        }
    }
    
    public enum LogLevel
    {
        /** 日志过滤类型 */
        LOGxLEVELxINFO,LOGxLEVELxDEBUG,LOGxLEVELxWARN
    }
}
