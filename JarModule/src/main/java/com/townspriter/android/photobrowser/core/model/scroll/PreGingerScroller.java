package com.townspriter.android.photobrowser.core.model.scroll;

import android.content.Context;
import android.widget.Scroller;

/******************************************************************************
 * @Path PhotoBrowserCore:PreGingerScroller
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class PreGingerScroller extends BaseScrollerProxy
{
    private final Scroller mScroller;
    
    public PreGingerScroller(Context context)
    {
        mScroller=new Scroller(context);
    }
    
    @Override
    public boolean computeScrollOffset()
    {
        return mScroller.computeScrollOffset();
    }
    
    @Override
    public void fling(int startX,int startY,int velocityX,int velocityY,int minX,int maxX,int minY,int maxY,int overX,int overY)
    {
        mScroller.fling(startX,startY,velocityX,velocityY,minX,maxX,minY,maxY);
    }
    
    @Override
    public void forceFinished(boolean finished)
    {
        mScroller.forceFinished(finished);
    }
    
    @Override
    public boolean isFinished()
    {
        return mScroller.isFinished();
    }
    
    @Override
    public int getCurrentX()
    {
        return mScroller.getCurrX();
    }
    
    @Override
    public int getCurrentY()
    {
        return mScroller.getCurrY();
    }
}