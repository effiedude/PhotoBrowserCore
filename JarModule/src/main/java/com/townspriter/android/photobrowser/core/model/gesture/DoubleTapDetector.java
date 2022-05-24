package com.townspriter.android.photobrowser.core.model.gesture;

import com.townspriter.android.photobrowser.core.model.extension.engine.PhotoViewEngine;
import com.townspriter.base.foundation.utils.log.Logger;

import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

/******************************************************************************
 * @Path PhotoBrowserCore:DoubleTapDetector
 * @Describe 双击手势实现类
 * @Name 张飞
 * @Email zhangfei@townspriter.com
 * @Data 21-4-6
 * CopyRight(C)2021 小镇精灵工作室版权所有
 * *****************************************************************************
 */
public class DoubleTapDetector implements GestureDetector.OnDoubleTapListener
{
    private final String TAG="DoubleTapDetector";
    private final int ALPHA=255;
    private PhotoViewEngine photoViewEngine;
    
    /**
     * @param photoViewEngine
     * 图片展示引擎
     */
    public DoubleTapDetector(PhotoViewEngine photoViewEngine)
    {
        setPhotoViewEngine(photoViewEngine);
    }
    
    /**
     * @param photoViewEngine
     * 图片展示引擎
     */
    public void setPhotoViewEngine(PhotoViewEngine photoViewEngine)
    {
        this.photoViewEngine=photoViewEngine;
    }
    
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent)
    {
        if(this.photoViewEngine==null)
        {
            Logger.e(TAG,"onSingleTapConfirmed-photoViewEngine:NULL");
            return false;
        }
        ImageView imageView=photoViewEngine.getImageView();
        if(null!=photoViewEngine.getPhotoTapListener())
        {
            final RectF displayRect=photoViewEngine.getDisplayRect();
            if(null!=displayRect)
            {
                final float x=motionEvent.getX(),y=motionEvent.getY();
                // 检测是否点击图片
                if(displayRect.contains(x,y))
                {
                    float xResult=(x-displayRect.left)/displayRect.width();
                    float yResult=(y-displayRect.top)/displayRect.height();
                    photoViewEngine.getPhotoTapListener().onPhotoTap(imageView,xResult,yResult);
                    return true;
                }
            }
        }
        // 是否点击图片外位置
        if(null!=photoViewEngine.getViewTapListener())
        {
            photoViewEngine.getViewTapListener().onViewTap(imageView,motionEvent.getX(),motionEvent.getY());
        }
        return false;
    }
    
    @Override
    public boolean onDoubleTap(MotionEvent motionEvent)
    {
        if(photoViewEngine==null||photoViewEngine.getBackgroundAlphaByGesture()!=ALPHA)
        {
            return false;
        }
        try
        {
            float scale=photoViewEngine.getScale();
            float x=motionEvent.getX();
            float y=motionEvent.getY();
            if(scale<photoViewEngine.getMaximumScale())
            {
                photoViewEngine.setScale(photoViewEngine.getMaximumScale(),x,y,true);
            }
            else
            {
                photoViewEngine.setScale(photoViewEngine.getMinimumScale(),x,y,true);
            }
        }
        catch(ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException)
        {
            Logger.w(TAG,"onDoubleTap:ArrayIndexOutOfBoundsException",arrayIndexOutOfBoundsException);
        }
        return true;
    }
    
    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent)
    {
        return false;
    }
}
