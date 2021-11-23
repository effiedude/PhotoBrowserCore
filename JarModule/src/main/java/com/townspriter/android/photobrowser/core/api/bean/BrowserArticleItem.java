package com.townspriter.android.photobrowser.core.api.bean;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
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
     * 服务器错误码
     */
    private int mCode;
    /**
     * 图片展示索引
     */
    public int imageIndex;
    /**
     * 图片信息包(信息流里面缺少需要字段.因此重写此字段的解析)
     */
    public List<BrowserImageBean> browserImages;
    /**
     * 文章来源
     */
    private String mSource;
    /**
     * 是否支持收藏
     */
    private boolean favoriteEnable;
    /**
     * 文章作者
     */
    private String creatorName;
    /**
     * 文章编辑
     */
    private String editorNames;
    /**
     * 文章作者单位
     */
    private String creatorDepartment;
    /**
     * 资源类型(埋点使用)
     */
    private int contentSource;
    /**
     * 内容分类标示(评论联想词模版使用)
     */
    private String categoryId;
    
    @Override
    public void parseFrom(@Nullable JSONObject object)
    {
        super.parseFrom(object);
        if(null==object)
        {
            Logger.w(TAG,"parseFrom-object:NULL");
            return;
        }
        mCode=object.optInt(JSON_KEY_CODE);
        browserImages=BrowserImageBean.parseListBrowser(object);
        imageIndex=object.optInt(JSON_KEY_INDEX);
        mSource=object.optString(JSON_KEY_SOURCE);
        creatorName=object.optString(JSON_KEY_CREATOR_NAME);
        JSONArray array=object.optJSONArray(JSON_KEY_EDITOR_NAME);
        if(null!=array&&array.length()>0)
        {
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0;i<array.length();i++)
            {
                try
                {
                    if(i>0)
                    {
                        stringBuilder.append("/");
                    }
                    stringBuilder.append(array.get(i).toString());
                }
                catch(JSONException jsonException)
                {
                    Logger.w(TAG,"parseFrom:JSONException",jsonException);
                }
            }
            editorNames=stringBuilder.toString();
        }
        creatorDepartment=object.optString(JSON_KEY_CREATOR_DEPT);
        favoriteEnable=object.optBoolean(JSON_KEY_FAV_ENABLE);
        contentSource=object.optInt(JSON_KEY_CONTENT_SOURCE);
        categoryId=object.optString(JSON_KEY_CATEGORY_ID);
    }
    
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
    
    public int getCode()
    {
        return mCode;
    }
    
    public String getCreatorName()
    {
        return creatorName;
    }
    
    public String getEditorName()
    {
        return editorNames;
    }
    
    public String getCreatorDepartment()
    {
        return creatorDepartment;
    }
    
    public boolean isCommentEnable()
    {
        return commentEnabled;
    }
    
    public boolean isFavoriteEnable()
    {
        return favoriteEnable;
    }
    
    public boolean isShareEnable()
    {
        return shareEnable;
    }
    
    public String getSource()
    {
        return mSource;
    }
    
    public int getImageIndex()
    {
        return imageIndex;
    }
    
    public int getCommentCount()
    {
        return commentCount;
    }
    
    public int getContentSource()
    {
        return contentSource;
    }
    
    public String getCategoryId()
    {
        return categoryId;
    }
}
