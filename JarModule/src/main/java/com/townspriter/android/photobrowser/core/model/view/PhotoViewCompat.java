package com.townspriter.android.photobrowser.core.model.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.townspriter.android.photobrowser.core.api.bean.BrowserImageBean;
import com.townspriter.android.photobrowser.core.model.extension.IPhotoView;
import com.townspriter.android.photobrowser.core.model.extension.LongPhotoAnalysator;
import com.townspriter.android.photobrowser.core.model.listener.OnPhotoLoadListener;
import com.townspriter.base.foundation.utils.concurrent.ThreadManager;
import com.townspriter.base.foundation.utils.io.IOUtil;
import com.townspriter.base.foundation.utils.log.Logger;
import com.townspriter.base.foundation.utils.system.SystemInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @path PhotoViewCompat
 * @describe 图片显示类
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PhotoViewCompat extends PhotoViewProxy
{
    private final String TAG="PhotoViewCompat";
    private final Handler mHandler;
    private final RequestListener mRequestListener;
    private @Nullable OnPhotoLoadListener mPhotoLoadListener;
    
    public PhotoViewCompat(Context context)
    {
        this(context,null);
    }
    
    public PhotoViewCompat(Context context,AttributeSet attrs)
    {
        this(context,attrs,0);
    }
    
    public PhotoViewCompat(Context context,AttributeSet attr,int windowType)
    {
        super(context,attr,windowType);
        mHandler=new Handler();
        mRequestListener=new RequestListener()
        {
            @Override
            public boolean onLoadFailed(@Nullable GlideException glideException,Object model,Target target,boolean isFirstResource)
            {
                if(null!=mPhotoLoadListener)
                {
                    mPhotoLoadListener.onPhotoLoadFailed();
                }
                return false;
            }
            
            @Override
            public boolean onResourceReady(Object resource,Object model,Target target,DataSource dataSource,boolean isFirstResource)
            {
                // 获取当前页面的图片
                if(null!=mPhotoLoadListener)
                {
                    mPhotoLoadListener.onPhotoLoadSucceed();
                }
                return false;
            }
        };
    }
    
    public void setPhotoLoadListener(@NonNull OnPhotoLoadListener listener)
    {
        mPhotoLoadListener=listener;
    }
    
    public void bindData(final BrowserImageBean photoViewBean)
    {
        if(null!=photoViewBean)
        {
            if(null!=mPhotoLoadListener)
            {
                mPhotoLoadListener.onPhotoLoading(0);
            }
            // 更新图片类型
            int imageTypeInt = photoViewBean.getTypeInt();
            setImageType(imageTypeInt);
            switch(imageTypeInt)
            {
                case BrowserImageBean.IMAGExLONG:
                    downloadAndShowLongImage(photoViewBean);
                    break;
                case BrowserImageBean.IMAGExGIF:
                    downloadAndShowGif(photoViewBean);
                    break;
                case BrowserImageBean.IMAGExNORMAL:
                default:
                    downloadAndShowNormalImage(photoViewBean);
                    break;
            }
        }
    }
    
    public void resetMatrix()
    {
        setMaximumScale(IPhotoView.DEFAULTxMAXxSCALE);
        if(getScale()>IPhotoView.DEFAULTxMINxSCALE)
        {
            setScale(IPhotoView.DEFAULTxMINxSCALE,true);
        }
    }
    
    /**
     * calculateInSampleSizeIfDecoderRegion
     * <p>
     * 在使用分块加载时计算原图的采样率.如果原图宽度小于屏幕宽度.原图采样
     * 如果原图宽度大于屏幕宽度.采样率*2.直到原图宽度小于屏幕宽度
     *
     * @return
     */
    private int calculateInSampleSizeIfDecoderRegion(BrowserImageBean photoViewBean)
    {
        int reqWidth=SystemInfo.INSTANCE.getScreenWidth(getContext());
        int halfWidth=photoViewBean.width/2;
        int inSampleSize=1;
        while(halfWidth>reqWidth)
        {
            inSampleSize*=2;
            halfWidth/=2;
        }
        return inSampleSize;
    }
    
    private void downloadAndShowNormalImage(final BrowserImageBean photoViewBean)
    {
        // 更新图片缩放类型
        setScaleTypeSafely(ScaleType.CENTER_INSIDE);
        Glide.with(this).asDrawable().load(photoViewBean.url).fitCenter().listener(mRequestListener).into(this);
    }
    
    private void downloadAndShowGif(final BrowserImageBean photoViewBean)
    {
        // 更新图片缩放类型
        setScaleTypeSafely(ScaleType.CENTER_INSIDE);
        Glide.with(this).asGif().load(photoViewBean.url).fitCenter().listener(mRequestListener).into(this);
    }
    
    private void downloadAndShowLongImage(final BrowserImageBean photoViewBean)
    {
        // 更新图片缩放类型
        setScaleTypeSafely(ScaleType.CENTER_CROP);
        final LongPhotoAnalysator longPhotoAnalysator=new LongPhotoAnalysator();
        longPhotoAnalysator.setOriginBitmapHeight(photoViewBean.height);
        // 设置图片分块读取区域
        Rect rect=new Rect();
        rect.left=0;
        rect.top=0;
        int inSampleSize=calculateInSampleSizeIfDecoderRegion(photoViewBean);
        longPhotoAnalysator.setInSampleSize(inSampleSize);
        rect.right=rect.left+photoViewBean.width;
        rect.bottom=rect.top+SystemInfo.INSTANCE.getScreenHeight(getContext());
        longPhotoAnalysator.setRegionRect(rect);
        setLongPhotoAnalysator(longPhotoAnalysator);
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
                    File file=Glide.with(PhotoViewCompat.this).load(photoViewBean.url).downloadOnly(photoViewBean.width,photoViewBean.height).get();
                    inputStream=new FileInputStream(file);
                    bitmap=longPhotoAnalysator.decodeRegion(inputStream);
                    final Bitmap finalBitmap=bitmap;
                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            setImageBitmap(finalBitmap);
                        }
                    });
                    mRequestListener.onResourceReady(null,null,null,null,true);
                }
                catch(InterruptedException interruptedException)
                {
                    Logger.w(TAG,"bindData:InterruptedException",interruptedException);
                    mRequestListener.onLoadFailed(null,null,null,true);
                }
                catch(FileNotFoundException fileNotFoundException)
                {
                    Logger.w(TAG,"bindData:FileNotFoundException",fileNotFoundException);
                    mRequestListener.onLoadFailed(null,null,null,true);
                }
                catch(ExecutionException executionException)
                {
                    Logger.w(TAG,"bindData:ExecutionException",executionException);
                    mRequestListener.onLoadFailed(null,null,null,true);
                }
                finally
                {
                    IOUtil.safeClose(inputStream);
                }
            }
        });
    }
}
