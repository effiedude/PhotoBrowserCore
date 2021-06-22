package com.townspriter.android.photobrowser.core.model.util;

import com.bumptech.glide.Glide;
import com.townspriter.android.foundation.utils.concurrent.ThreadManager;
import android.content.Context;
import android.os.Looper;

/******************************************************************************
 * @Path PhotoBrowserCore:GlideUtil
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class GlideUtil
{
    private static volatile GlideUtil instance;
    
    private GlideUtil()
    {}
    
    public static GlideUtil getInstance()
    {
        if(instance==null)
        {
            synchronized(GlideUtil.class)
            {
                if(instance==null)
                {
                    instance=new GlideUtil();
                }
            }
        }
        return instance;
    }
    
    /**
     * clearDiskCache
     * 清除Glide磁盘缓存.需要在子线程中运行
     *
     * @param context
     */
    public void clearDiskCache(final Context context)
    {
        try
        {
            if(Looper.myLooper()==Looper.getMainLooper())
            {
                ThreadManager.post(ThreadManager.THREADxBACKGROUND,new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Glide.get(context).clearDiskCache();
                    }
                });
            }
            else
            {
                Glide.get(context).clearDiskCache();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
    
    /**
     * clearMemoryCache
     * 清除Glide内存缓存.只能在主线程执行
     *
     * @param context
     */
    public void clearMemoryCache(Context context)
    {
        try
        {
            if(Looper.myLooper()==Looper.getMainLooper())
            {
                Glide.get(context).clearMemory();
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }
}
