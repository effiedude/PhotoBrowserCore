package com.townspriter.android.photobrowser.core.api.constant;

import com.townspriter.android.photobrowser.core.model.util.InfoFlowJsonConstDef;

/******************************************************************************
 * @Path PhotoBrowserCore:JsonConstant
 * @Describe 解析相关常量列表
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface JsonConstant extends InfoFlowJsonConstDef
{
    int STATUS_CODE_NORMAL=0;
    int STATUS_CODE_OFFLINE=1;
    int STATUS_CODE_NETWORK_ERROR=2110001;
    String JSON_KEY_IMAGES="images";
    String JSON_KEY_CODE="code";
    String JSON_KEY_INDEX="imgIndex";
    String JSON_KEY_SIZE="size";
    String JSON_KEY_WIDTH="width";
    String JSON_KEY_HEIGHT="height";
    String JSON_KEY_TITLE="title";
    String JSON_KEY_URL="url";
    String JSON_KEY_DESC="desc";
    String JSON_KEY_TYPE="type";
    String JSON_KEY_HASH="verify_hash";
    /**
     * 文章来源
     */
    String JSON_KEY_SOURCE="show_source";
    /**
     * 是否支持收藏功能
     */
    String JSON_KEY_FAV_ENABLE="fav_enable";
    /**
     * 文章作者
     */
    String JSON_KEY_CREATOR_NAME="creator_name";
    /**
     * 文章编辑
     */
    String JSON_KEY_EDITOR_NAME="editors_name";
    /**
     * 文章作者单位
     */
    String JSON_KEY_CREATOR_DEPT="creator_dept";
    /**
     * 资源类型(埋点使用)
     */
    String JSON_KEY_CONTENT_SOURCE="content_source";
    /**
     * 内容分类标示(评论联想词模版使用)
     */
    String JSON_KEY_CATEGORY_ID="category_id";
}
