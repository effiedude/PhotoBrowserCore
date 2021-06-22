package com.townspriter.android.photobrowser.core.model.extension.engine;

import com.townspriter.android.photobrowser.core.model.util.AnimationCompat;
import android.widget.ImageView;

/******************************************************************************
 * @Path PhotoBrowserCore:AnimatedZoomRunnable
 * @Describe 图片缩放动画辅助类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class AnimatedZoomRunnable implements Runnable
{
    private final String TAG="AnimatedZoomRunnable";
    private final float mFocusX,mFocusY;
    private final long mStartTime;
    private final float mZoomStart,mZoomEnd;
    private final PhotoViewEngine engine;
    
    public AnimatedZoomRunnable(final float currentZoom,final float targetZoom,final float focusX,final float focusY,PhotoViewEngine engine)
    {
        this.engine=engine;
        mFocusX=focusX;
        mFocusY=focusY;
        mStartTime=System.currentTimeMillis();
        mZoomStart=currentZoom;
        mZoomEnd=targetZoom;
    }
    
    @Override
    public void run()
    {
        ImageView imageView=engine.getImageView();
        if(imageView==null)
        {
            return;
        }
        float t=interpolate();
        float scale=mZoomStart+t*(mZoomEnd-mZoomStart);
        float deltaScale=scale/engine.getScale();
        if(null!=engine.mScaleDragDetector)
        {
            engine.mScaleDragDetector.processOnScale(deltaScale,mFocusX,mFocusY);
        }
        if(t<1.0F)
        {
            AnimationCompat.postOnAnimation(imageView,this);
        }
    }
    
    private float interpolate()
    {
        float t=1.0F*(System.currentTimeMillis()-mStartTime)/engine.mZoomDuration;
        t=Math.min(1.0F,t);
        t=engine.INTERPOLATOR.getInterpolation(t);
        return t;
    }
}
