package com.townspriter.android.photobrowser.core.model.util;

import java.io.InputStream;
import com.townspriter.base.foundation.utils.lang.AssertUtil;
import com.townspriter.base.foundation.utils.system.SystemInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/******************************************************************************
 * @Path PhotoBrowserCore:LongImageDecoder
 * @Describe 大长图解码
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class LongImageDecoder
{
    public static int calculateInSampleSize(int outWidth,int outHeight,int reqWidth,int reqHeight)
    {
        // 源图片的高度和宽度
        final int halfHeight;
        final int halfWidth;
        int inSampleSize=1;
        if(outHeight >reqHeight|| outWidth >reqWidth)
        {
            // 计算出实际宽高和目标宽高的比率
            halfHeight= outHeight /2;
            halfWidth= outWidth /2;
            // 选择宽和高中最小的比率作为inSampleSize的值.这样可以保证最终图片的宽和高一定都会大于等于目标的宽和高
            while((halfHeight/inSampleSize)>reqHeight&&(halfWidth/inSampleSize)>reqWidth)
            {
                inSampleSize*=2;
            }
        }
        while((DeviceOpenGLUtil.getGLESLimitTexture()>0)&&(DeviceOpenGLUtil.getGLESLimitTexture()<(outHeight /inSampleSize)))
        {
            inSampleSize*=2;
        }
        return inSampleSize;
    }
    
    public Bitmap decodeBitmap(Context context,InputStream inputStream)
    {
        try
        {
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            BitmapFactory.decodeStream(inputStream,null,options);
            float rate=((float)options.outHeight)/options.outWidth;
            int reqWidth=SystemInfo.INSTANCE.getDeviceWidth(context);
            int reqHeight=(int)(reqWidth*rate);
            options.inSampleSize=calculateInSampleSize(options.outWidth,options.outHeight,reqWidth,reqHeight);
            options.inJustDecodeBounds=false;
            Bitmap srcBitmap=BitmapFactory.decodeStream(inputStream,null,options);
            return srcBitmap;
        }
        catch(Exception exception)
        {
            AssertUtil.fail(exception);
            return null;
        }
    }
}
