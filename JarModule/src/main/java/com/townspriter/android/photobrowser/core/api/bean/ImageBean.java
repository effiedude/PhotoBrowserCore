package com.townspriter.android.photobrowser.core.api.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.townspriter.android.photobrowser.core.model.util.IJSONSerializable;
import com.townspriter.android.photobrowser.core.model.util.InfoFlowJsonConstDef;
import com.townspriter.base.foundation.utils.bitmap.BitmapUtils;
import com.townspriter.base.foundation.utils.net.URLUtil;
import com.townspriter.base.foundation.utils.net.mime.MimeUtil;
import com.townspriter.base.foundation.utils.text.StringUtil;

/******************************************************************************
 * @Path PhotoBrowserCore:ImageBean
 * @Describe 图片浏览器入口类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class ImageBean implements IJSONSerializable,InfoFlowJsonConstDef
{
    public String url;
    public String desc;
    public String type;
    public String mediaType;
    public int width;
    public int height;
    
    public ImageBean()
    {}
    
    public static List<ImageBean> parseList(JSONObject object)
    {
        JSONArray imagesArray=object.optJSONArray(IMAGES);
        if(null==imagesArray||0>=imagesArray.length())
        {
            return null;
        }
        List<ImageBean> imageBeans=new ArrayList<>();
        for(int i=0;i<imagesArray.length();++i)
        {
            ImageBean imageBean=new ImageBean();
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
    
    public static ImageBean parse(JSONObject object)
    {
        if(object==null)
        {
            return null;
        }
        ImageBean imageBean=new ImageBean();
        imageBean.setUrl(object.optString(URL));
        imageBean.setDescription(object.optString(DESC));
        imageBean.setWidth(object.optInt(WIDTH));
        imageBean.setHeight(object.optInt(HEIGHT));
        imageBean.setType(object.optString(PARAMxIMAGExTYPE));
        imageBean.setType(object.optString(PARAMxMEDIAxTYPE));
        return imageBean;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public void setUrl(String url)
    {
        this.url=url;
    }
    
    public String getDescription()
    {
        return desc;
    }
    
    public void setDescription(String desc)
    {
        this.desc=desc;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setHeight(int height)
    {
        this.height=height;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public void setWidth(int width)
    {
        this.width=width;
    }
    
    public String getType()
    {
        return type;
    }
    
    public void setType(String type)
    {
        this.type=type;
    }
    
    @Override
    public JSONObject serializeTo() throws JSONException
    {
        JSONObject img=new JSONObject();
        img.put(URL,url);
        img.put(DESC,desc);
        img.put(PARAMxIMAGExTYPE,type);
        img.put(PARAMxMEDIAxTYPE,mediaType);
        img.put(WIDTH,width);
        img.put(HEIGHT,height);
        return img;
    }
    
    @Override
    public void parseFrom(JSONObject jsonObj)
    {
        if(jsonObj==null)
        {
            return;
        }
        url=jsonObj.optString(URL);
        desc=jsonObj.optString(DESC);
        width=jsonObj.optInt(WIDTH);
        height=jsonObj.optInt(HEIGHT);
        type=jsonObj.optString(PARAMxIMAGExTYPE);
        mediaType=jsonObj.optString(PARAMxMEDIAxTYPE);
        if(!StringUtil.isEmpty(mediaType)&&mediaType.equalsIgnoreCase(MEDIAxTYPExIMAGE))
        {
            boolean isNetUrl=URLUtil.isNetworkUrl(url);
            if(!isNetUrl)
            {
                if(width==0)
                {
                    width=BitmapUtils.getWidth(url);
                }
                if(height==0)
                {
                    height=BitmapUtils.getHeight(url);
                }
            }
            if(StringUtil.isEmpty(type))
            {
                if(MimeUtil.isGifType(MimeUtil.guessMediaMimeType(url)))
                {
                    type=IMAGExTYPExGIF;
                }
                else
                {
                    type=IMAGExTYPExNORMAL;
                }
            }
        }
    }
}
