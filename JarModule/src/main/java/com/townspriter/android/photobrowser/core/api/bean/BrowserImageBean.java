package com.townspriter.android.photobrowser.core.api.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.townspriter.android.photobrowser.core.api.constant.JsonConstant;
import com.townspriter.android.photobrowser.core.model.util.LongBitmapUtil;

import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;

/******************************************************************************
 * @Path PhotoBrowserCore:BrowserImageBean
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class BrowserImageBean extends ImageBean implements JsonConstant
{
    public static final int IMAGExNORMAL=0;
    public static final int IMAGExGIF=1;
    public static final int IMAGExLONG=2;
    /** 图片类型.服务器端返回的是图片格式的后缀.需要转换之后才能继续使用 */
    private int imageType;
    private String title;
    
    /**
     * parseListBrowser
     * 解析Json数据对象并存储为BrowserImageBean链表
     *
     * @param object
     * Json数据对象
     * @return List<BrowserImageBean>
     */
    @Nullable
    public static List<BrowserImageBean> parseListBrowser(JSONObject object)
    {
        JSONArray imagesArray=object.optJSONArray(IMAGES);
        if(null==imagesArray||0>=imagesArray.length())
        {
            return null;
        }
        List<BrowserImageBean> imageBeans=new ArrayList<>();
        for(int i=0;i<imagesArray.length();++i)
        {
            BrowserImageBean imageBean=new BrowserImageBean();
            JSONObject imgObject=imagesArray.optJSONObject(i);
            if(null==imgObject)
            {
                continue;
            }
            imageBean.parseFrom(imgObject);
            imageBeans.add(imageBean);
        }
        return imageBeans;
    }
    
    @SuppressWarnings("ReturnOfNull")
    public static BrowserImageBean parse(JSONObject object)
    {
        if(object==null)
        {
            return null;
        }
        BrowserImageBean imageBean=new BrowserImageBean();
        imageBean.setTitle(object.optString(TITLE));
        imageBean.setUrl(object.optString(URL));
        imageBean.setDescription(object.optString(DESC));
        imageBean.setWidth(object.optInt(WIDTH));
        imageBean.setHeight(object.optInt(HEIGHT));
        imageBean.setType(object.optString(PARAMxIMAGExTYPE));
        return imageBean;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public void setTitle(String title)
    {
        this.title=title;
    }
    
    public int getTypeInt()
    {
        if(width!=0&&height!=0)
        {
            try
            {
                ExifInterface exifInterface=new ExifInterface(url);
                int orientation=exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
                switch(orientation)
                {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        @SuppressWarnings("SuspiciousNameCombination")
                        int finalWidth=height;
                        @SuppressWarnings("SuspiciousNameCombination")
                        int finalHeight=width;
                        width=finalWidth;
                        height=finalHeight;
                        break;
                    default:
                        break;
                }
            }
            catch(IOException exception)
            {
                exception.printStackTrace();
            }
        }
        if(LongBitmapUtil.shouldUsedRegionDecoder(width,height))
        {
            type=IMAGExTYPExLONG;
        }

        if(IMAGExTYPExGIF.equalsIgnoreCase(getType()))
        {
            imageType=IMAGExGIF;
        }
        else if(IMAGExTYPExLONG.equalsIgnoreCase(getType()))
        {
            imageType=IMAGExLONG;
        }
        else
        {
            imageType=IMAGExNORMAL;
        }
        return imageType;
    }
    
    @Override
    public JSONObject serializeTo() throws JSONException
    {
        JSONObject img=super.serializeTo();
        img.put(TITLE,title);
        return img;
    }
    
    @Override
    public void parseFrom(JSONObject jsonObj)
    {
        super.parseFrom(jsonObj);
        if(jsonObj==null)
        {
            return;
        }
        title=jsonObj.optString(TITLE);
    }
}
