package com.townspriter.android.photobrowser.core.model.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import com.bumptech.glide.Glide;
import com.townspriter.android.foundation.utils.concurrent.ThreadManager;
import com.townspriter.android.foundation.utils.io.IOUtil;
import com.townspriter.android.foundation.utils.log.Logger;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path Notes:LongBitmapUtil
 * @version 1.0.0.0
 * @describe 大长图工具类
 * @author 张飞
 * @email zhangfei@townspriter.com
 * @date
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class LongBitmapUtil
{
    private static final String TAG="LongBitmapUtil";
    
    /*********************************
     * @function loadBitmapByGlide
     * @since JDK 1.7.0-79
     * @describe
     * <p>
     * 通过三方引擎加载大长图到本地磁盘.之后通过分块加载解析出指定区域的位图
     * @param context
     * 环境上下文
     * @param url
     * 图片的网络地址
     * @param regionRect
     * 分块加载指定区域
     * @param inSampleSize
     * 指定采样率.防止图片宽度过大{@link com.townspriter.android.foundation.utils.bitmap.BitmapUtils#calculateInSampleSize(int,int)}
     * @param listener
     * 可选图片下载监听器{@link OnBitmapLoadListener}
     * @exception
     * @return
     * void
     * @date
     * @version 1.0.0.0
     * ********************************
     */
    public static void loadBitmapByGlide(@NonNull final Context context,@NonNull final String url,@NonNull final Rect regionRect,@IntRange(from=1) final int inSampleSize,@Nullable final OnBitmapLoadListener listener)
    {
        ThreadManager.post(ThreadManager.THREADxWORK,new Runnable()
        {
            @Override
            public void run()
            {
                FileInputStream inputStream=null;
                Bitmap bitmap;
                try
                {
                    // 长图直接下载原始图到磁盘缓存中.之后提取第一屏的数据展示
                    File file=Glide.with(context).load(url).downloadOnly(regionRect.right,regionRect.bottom).get();
                    inputStream=new FileInputStream(file);
                    bitmap=decodeRegion(inputStream,regionRect,inSampleSize,listener,url);
                    final Bitmap finalBitmap=bitmap;
                    if(listener!=null)
                    {
                        listener.onSucceed(finalBitmap,url);
                    }
                }
                catch(InterruptedException interruptedException)
                {
                    Logger.w(TAG,"bindData:InterruptedException",interruptedException);
                    if(listener!=null)
                    {
                        listener.onFailed(interruptedException,url);
                    }
                }
                catch(FileNotFoundException fileNotFoundException)
                {
                    Logger.w(TAG,"bindData:FileNotFoundException",fileNotFoundException);
                    if(listener!=null)
                    {
                        listener.onFailed(fileNotFoundException,url);
                    }
                }
                catch(ExecutionException executionException)
                {
                    Logger.w(TAG,"bindData:ExecutionException",executionException);
                    if(listener!=null)
                    {
                        listener.onFailed(executionException,url);
                    }
                }
                finally
                {
                    IOUtil.safeClose(inputStream);
                }
            }
        });
    }
    
    private static @Nullable Bitmap decodeRegion(@NonNull InputStream is,@NonNull Rect regionRect,@IntRange(from=1) int inSampleSize,@Nullable OnBitmapLoadListener listener,@NonNull final String url)
    {
        try
        {
            BitmapRegionDecoder regionDecoder=BitmapRegionDecoder.newInstance(is,false);
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize=inSampleSize;
            Bitmap bitmap=regionDecoder.decodeRegion(regionRect,options);
            return bitmap;
        }
        catch(Exception exception)
        {
            Logger.w(TAG,"decodeRegion:Exception",exception);
            if(listener!=null)
            {
                listener.onFailed(exception,url);
            }
        }
        finally
        {
            IOUtil.safeClose(is);
        }
        return null;
    }
    
    public interface OnBitmapLoadListener
    {
        /**
         * onSucceed
         * 图片下载并解析成功
         *
         * @param bitmap
         * 下载的位图
         * @param url
         * 位图对应的网络地址
         */
        void onSucceed(@NonNull Bitmap bitmap,@NonNull String url);
        
        /**
         * onFailed
         * 图片下载失败
         * 
         * @param exception
         * 具体异常信息
         * @param url
         * 位图对应的网络地址
         */
        void onFailed(Exception exception,@NonNull String url);
    }
}
