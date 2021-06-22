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
    public int view_cnt;
    /**
     * 直播间观看人数
     */
    public int watch_cnt;
    public int share_cnt;
    public String op_mark;
    public int op_mark_icolor;
    public String op_mark_iurl;
    // @Nullable
    // public OpMark op_mark_v2;
    // @Nullable
    // @Deprecated
    // public LiveStream liveStream;
    // @Nullable
    // public LiveRoom liveRoom;
    public String publish_time;
    public String showTime;
    public String desc;
    public String final_audit_time;
    @Nullable
    public Extension extension;
    // @Nullable
    // public SiteLogo site_logo;
    // @Nullable
    // public List<Thumbnail> thumbnails;
    // @Nullable
    // public List<Thumbnail> share_images;
    @Nullable
    public List<ImageBean> images;
    // @Nullable
    // public List<Video> videos;
    // @Nullable
    // public List<WeMediaAuthor> wmAuthors;
    public int wmAuthorCount;
    /**
     * 矩阵号卡片头部信息
     */
    // @Nullable
    // public WeMediaAuthor wmAuthor;
    @Nullable
    public List<ArticleItem> sub_items;
    // @Nullable
    // public List<HyperLink> hyperlinks;
    public boolean commentEnabled;
    public int commentCount;
    public String shareUrl;
    public String pageInfo;
    // @Nullable
    // public OpMark label;
    public boolean playable;
    public String summary;
    public String performers;
    public String coreReason;
    // @Nullable
    // public ArticleShareInfo articleShareInfo;
    public long cacheTime;
    // @Nullable
    // public List<VoiceItem> voices;
    public int likeCount;
    public boolean likeEnable;
    public boolean shareEnable;
    public boolean favEnable;
    public int playCnt;
    public boolean viewEnable;
    // @Nullable
    // public WeMediaAuthor mRelatedWmAuthor;
    /**
     * 活动参与人数
     */
    public int participantCnt;
    /**
     * 卡片背景图
     */
    // public Thumbnail bgImg;
    /**
     * 是否已关注
     */
    public boolean is_followed;
    /**
     * 关注开关
     */
    public boolean follow_enable;
    /**
     * 卡片关联的频道
     */
    public Long related_channel;
    
    /**
     * 音频
     */
    // public List<Audio> audios;
    public static ArticleItem parse(JSONObject object)
    {
        ArticleItem article=new ArticleItem();
        if(object==null)
        {
            return article;
        }
        article.parseFrom(object);
        return article;
    }
    
    @Nullable
    public void getDefaultThumbnail()
    {}
    
    @NonNull
    public String getDefaultThumbnailUrl()
    {
        return "";
    }
    
    @Override
    public void parseFrom(JSONObject object)
    {}
    
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
