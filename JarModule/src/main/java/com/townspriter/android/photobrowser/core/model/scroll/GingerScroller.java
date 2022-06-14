package com.townspriter.android.photobrowser.core.model.scroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.widget.OverScroller;

/******************************************************************************
 * @Path PhotoBrowserCore:GingerScroller
 * @Describe 手势实现类.适配API-9
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class GingerScroller extends BaseScrollerProxy
{
    protected final OverScroller mScroller;
    private boolean mFirstScroll=false;
    
    public GingerScroller(Context context)
    {
        mScroller=new OverScroller(context);
    }
    
    @Override
    public boolean computeScrollOffset()
    {
        if(mFirstScroll)
        {
            mScroller.computeScrollOffset();
            mFirstScroll=false;
        }
        return mScroller.computeScrollOffset();
    }
    
    @Override
    public void fling(int startX,int startY,int velocityX,int velocityY,int minX,int maxX,int minY,int maxY,int overX,int overY)
    {
        mScroller.fling(startX,startY,velocityX,velocityY,minX,maxX,minY,maxY,overX,overY);
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