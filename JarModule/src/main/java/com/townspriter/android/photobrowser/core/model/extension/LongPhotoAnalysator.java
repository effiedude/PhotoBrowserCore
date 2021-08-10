package com.townspriter.android.photobrowser.core.model.extension;

import java.io.InputStream;
import com.townspriter.android.foundation.utils.io.IOUtil;
import com.townspriter.android.foundation.utils.log.Logger;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

/******************************************************************************
 * @Path PhotoBrowserCore:LongPhotoAnalysator
 * @Describe 大图长图解析器
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
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
        if(mOptions!=null)
        {
            mOptions.inSampleSize=inSampleSize;
        }
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
            Bitmap bitmap=mRegionDecoder.decodeRegion(mRegionRect,mOptions);
            return bitmap;
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
        Logger.d(TAG,"reloadBitmapIfNeeded-dragDistance:"+dragDistance);
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
