package com.townspriter.android.photobrowser.core.api.bean;

import java.security.cert.Extension;
import java.util.List;
import org.json.JSONObject;
import com.townspriter.android.photobrowser.core.model.util.InfoFlowJsonConstDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/******************************************************************************
 * @Path PhotoBrowserCore:ArticleItem
 * @Describe 图片浏览器入口类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class ArticleItem extends AbstractItem implements InfoFlowJsonConstDef
{
    public String title;
    public String sub_title;
    public String url;
    public @Nullable Extension extension;
    public boolean commentEnabled;
    public int commentCount;
    public boolean shareEnable;

    public static ArticleItem parse(JSONObject object)
    {
        ArticleItem article=new ArticleItem();
        if(object == null)
        {
            return article;
        }
        article.parseFrom(object);
        return article;
    }
    
    @Override
    public void parseFrom(JSONObject object)
    {

    }
    
    @Override
    public JSONObject serializeTo()
    {
        return null;
    }
    
    @Override
    public String getTitle()
    {
        return title;
    }
    
    @Override
    public String getUrl()
    {
        return url;
    }
}
