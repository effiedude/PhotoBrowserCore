package com.townspriter.android.photobrowser.core.model.extension;

import java.io.InputStream;

import com.townspriter.base.foundation.utils.io.IOUtil;
import com.townspriter.base.foundation.utils.log.Logger;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.widget.ImageView;

/******************************************************************************
 * @path LongPhotoAnalysator
 * @describe 大图长图解析器
 * @author  张飞
 * @email zhangfei@personedu.com
 * @date 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class LongPhotoAnalysator implements LongPhotoAnalysable
{
    private final String TAG="LongPhotoAnalysator";
    private final BitmapFactory.Options mOptions=new BitmapFactory.Options();
    private int mOriginBitmapHeight;
    private volatile Rect mRegionRect;
    private BitmapRegionDecoder mRegionDecoder;
    
    public void setInSampleSize(int inSampleSize)
    {
        mOptions.inSampleSize=inSampleSize;
    }
    
    @Override
    public int getOriginBitmapHeight()
    {
        return mOriginBitmapHeight;
    }
    
    @Override
    public void setOriginBitmapHeight(int originBitmapHeight)
    {
        mOriginBitmapHeight=originBitmapHeight;
    }
    
    @Override
    public Rect getRegionRect()
    {
        return mRegionRect;
    }
    
    @Override
    public void setRegionRect(Rect regionRect)
    {
        mRegionRect=regionRect;
    }
    
    @Override
    public Bitmap decodeRegion(InputStream is)
    {
        if(mRegionRect==null)
        {
            Logger.w(TAG,"decodeRegion-mRegionRect:NULL");
            return null;
        }
        try
        {
            if(mRegionDecoder==null)
            {
                mRegionDecoder=BitmapRegionDecoder.newInstance(is,false);
            }
            return mRegionDecoder.decodeRegion(mRegionRect,mOptions);
        }
        catch(Exception exception)
        {
            Logger.w(TAG,"decodeRegion:Exception",exception);
        }
        finally
        {
            IOUtil.safeClose(is);
        }
        return null;
    }
    
    @Override
    public boolean reloadBitmapIfNeeded(ImageView imageView,float dragDistance)
    {
        if(mRegionRect==null)
        {
            Logger.w(TAG,"reloadBitmapIfNeeded-mRegionRect:NULL");
            return false;
        }
        int height=mRegionRect.bottom-mRegionRect.top;
        if(dragDistance<=0&&mRegionRect.top==mOriginBitmapHeight-height)
        {
            return true;
        }
        else if(dragDistance>0&&mRegionRect.top==0)
        {
            return true;
        }
        mRegionRect.top=mRegionRect.top-(int)dragDistance;
        if(mRegionRect.top<=0)
        {
            mRegionRect.top=0;
        }
        if(mRegionRect.top>=mOriginBitmapHeight-height)
        {
            mRegionRect.top=mOriginBitmapHeight-height;
        }
        mRegionRect.bottom=mRegionRect.top+height;
        Logger.d(TAG,"reloadBitmapIfNeeded-mRegionRect:"+mRegionRect.toString());
        if(mRegionDecoder!=null)
        {
            try
            {
                imageView.setImageBitmap(mRegionDecoder.decodeRegion(mRegionRect,mOptions));
                return true;
            }
            catch(Exception exception)
            {
                Logger.w(TAG,"reLoadBitmapIfNeeded:Exception",exception);
            }
        }
        else
        {
            Logger.w(TAG,"reloadBitmapIfNeeded-mRegionDecoder:NULL");
        }
        return false;
    }
}
