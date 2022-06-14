package com.townspriter.android.photobrowser.core.model.extension.engine;

import com.townspriter.android.photobrowser.core.model.scroll.BaseScrollerProxy;
import com.townspriter.android.photobrowser.core.model.util.AnimationCompat;
import com.townspriter.base.foundation.utils.log.Logger;
import android.content.Context;
import android.graphics.RectF;
import android.widget.ImageView;

/******************************************************************************
 * @path FlingRunnable
 * @describe 图片滑动辅助类
 * @author 张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class FlingRunnable implements Runnable
{
    private final String TAG="FlingRunnable";
    private final BaseScrollerProxy mScroller;
    private final PhotoViewEngine engine;
    private int mCurrentX,mCurrentY;
    
    public FlingRunnable(Context context,PhotoViewEngine engine)
    {
        this.engine=engine;
        mScroller=BaseScrollerProxy.getScroller(context);
    }
    
    public void cancelFling()
    {
        mScroller.forceFinished(true);
    }
    
    public void fling(int viewWidth,int viewHeight,int velocityX,int velocityY)
    {
        final RectF rect=engine.getDisplayRect();
        if(null==rect)
        {
            Logger.w(TAG,"fling-rect:NULL");
            return;
        }
        final int startX=Math.round(-rect.left);
        final int minX,maxX;
        if(viewWidth<rect.width())
        {
            minX=0;
            maxX=Math.round(rect.width()-viewWidth);
        }
        else
        {
            minX=maxX=startX;
        }
        // 注意:长图分块加载显示区域的计算方式不同于大图
        final int startY,minY,maxY;
        if(engine.getLongPhotoAnalysator()!=null&&engine.getLongPhotoAnalysator().getRegionRect()!=null)
        {
            startY=Math.round(engine.getLongPhotoAnalysator().getRegionRect().top*engine.getScale());
            minY=0;
            maxY=Math.round(engine.getLongPhotoAnalysator().getOriginBitmapHeight()*engine.getScale());
        }
        else
        {
            startY=Math.round(-rect.top);
            if(viewHeight<rect.height())
            {
                minY=0;
                maxY=Math.round(rect.height()-viewHeight);
            }
            else
            {
                minY=maxY=startY;
            }
        }
        // 如果大图或长图存在可以继续滑动的空间允许其继续滑动
        mCurrentX=startX;
        mCurrentY=startY;
        if(startX!=maxX||((int)rect.right==viewWidth&&velocityX<0)||startY!=maxY||((int)rect.bottom==viewHeight&&velocityY<0))
        {
            mScroller.fling(startX,startY,velocityX,velocityY,minX,maxX,minY,maxY,0,0);
        }
    }
    
    @Override
    public void run()
    {
        if(mScroller.isFinished())
        {
            Logger.w(TAG,"run:FINISHED");
            return;
        }
        ImageView imageView=engine.getImageView();
        if(null!=imageView&&mScroller.computeScrollOffset())
        {
            final int newX=mScroller.getCurrentX();
            final int newY=mScroller.getCurrentY();
            engine.mSuppMatrix.postTranslate(mCurrentX-newX,mCurrentY-newY);
            engine.setImageViewMatrix(engine.getDrawMatrix());
            // 在大图或长图上面执行手势滑动过程中.需要不断更新它们的显示矩阵.刷新图片的显示区域
            if(engine.getLongPhotoAnalysator()!=null)
            {
                engine.checkMatrixRegionBounds(mCurrentY-newY);
            }
            mCurrentX=newX;
            mCurrentY=newY;
            AnimationCompat.postOnAnimation(imageView,this);
        }
    }
}
