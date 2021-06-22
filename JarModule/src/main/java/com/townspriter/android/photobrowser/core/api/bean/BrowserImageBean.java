package com.townspriter.android.photobrowser.core.api.bean;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.townspriter.android.photobrowser.core.api.constant.JsonConstant;
import androidx.annotation.Nullable;

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
    public static final int IMAGE_NORMAL=0;
    public static final int IMAGE_GIF=1;
    public static final int IMAGE_LONG=2;
    /** 图片类型.服务器端返回的是图片格式的后缀.需要转换之后才能继续使用 */
    private int type;
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
        imageBean.setType(object.optString(TYPE));
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
        return type;
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
        if("gif".equalsIgnoreCase(getType()))
        {
            type=IMAGE_GIF;
        }
        else if("long".equalsIgnoreCase(getType()))
        {
            type=IMAGE_LONG;
        }
        else
        {
            type=IMAGE_NORMAL;
        }
    }
}
