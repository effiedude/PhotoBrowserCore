package com.townspriter.android.photobrowser.core.api.bean;

import java.util.List;
import org.json.JSONObject;
import com.townspriter.android.photobrowser.core.api.constant.JsonConstant;
import com.townspriter.base.foundation.utils.log.Logger;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path PhotoBrowserCore:BrowserArticleItem
 * @Describe 图片浏览器入口类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class BrowserArticleItem extends ArticleItem implements JsonConstant
{
    private static final String TAG="BrowserArticleItem";
    /**
     * 图片展示索引
     */
    public int imageIndex;
    /**
     * 图片信息包(信息流里面缺少需要字段.因此重写此字段的解析)
     */
    public List<BrowserImageBean> browserImages;
    /**
     * 服务器错误码
     */
    private int mCode;
    
    public static BrowserArticleItem parse(JSONObject object)
    {
        BrowserArticleItem article=new BrowserArticleItem();
        if(object==null)
        {
            Logger.w(TAG,"parse-object:NULL");
            return article;
        }
        article.parseFrom(object);
        return article;
    }
    
    @Override
    public void parseFrom(@Nullable JSONObject object)
    {
        super.parseFrom(object);
        if(null==object)
        {
            Logger.w(TAG,"parseFrom-object:NULL");
            return;
        }
        browserImages=BrowserImageBean.parseListBrowser(object);
        imageIndex=object.optInt(JSON_KEY_INDEX);
    }
    
    public int getCode()
    {
        return mCode;
    }
}
