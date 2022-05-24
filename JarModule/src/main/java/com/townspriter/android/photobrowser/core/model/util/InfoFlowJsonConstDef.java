package com.townspriter.android.photobrowser.core.model.util;
/******************************************************************************
 * @Path PhotoBrowserCore:InfoFlowJsonConstDef
 * @Describe 图片浏览器入口类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface InfoFlowJsonConstDef
{
    String MESSAGE="message";
    String STATUS="status";
    String DATA="data";
    String RESPONSE="response";
    String SIGN="sign";
    String ITEMS="items";
    String ID="id";
    String CATEGORY_ID="category_id";
    String MAP="map";
    String BANNERS="banners";
    String ARTICLES="articles";
    String SPECIALS="specials";
    String MODULES="modules";
    String TITLE="title";
    String TIME_LINE_URL="timeLineUrl";
    String COLUMN_NAME="column_name";
    String AUDIO_ID="audio_id";
    String ALBUM_ID="album_id";
    String UMS_ID="ums_id";
    String CUT_POINTS="cut_points";
    String BEGIN_AT="begin_at";
    String ENDING_AT="ending_at";
    String ICON_URL="icon_url";
    String IS_SPECIAL="is_special";
    String SPECIAL_URL="special_url";
    String SUBTITLE="sub_title";
    String OP_MARK="op_mark";
    String OP_MARK_ICOLOR="op_mark_icolor";
    String OP_MARK_STM="op_mark_stm";
    String OP_MARK_ETM="op_mark_etm";
    String SUB_CHANNELS="sub_channels";
    String CHANNEL_KEY_FORCE_INSERT="force_insert";
    String CHANNEL_KEY_FORCE_INSERT_TIME="force_insert_time";
    String PUBLISH_TIME="publish_time";
    String SHOW_TIME="show_time";
    String FINAL_AUDIT_TIME="final_audit_time";
    String EXTENSION="extension";
    String ITEM_TYPE="item_type";
    String STYLE_TYPE="style_type";
    String SUB_ITEMS="sub_items";
    String GRAB_TIME="grab_time";
    String URL="url";
    String WEBP_URL="webp_url";
    String RECOID="recoid";
    String CONTENT="content";
    String THUMBNAILS="thumbnails";
    String SHARE_IMAGES="share_images";
    String WIDTH="width";
    String HEIGHT="height";
    String SIZE="size";
    String TYPE="type";
    String IMAGES="images";
    String SHOW_SUB_CHANNEL_ALL="show_sub_channel_all";
    String CHANNEL_TYPE="channel_type";
    String JUMP_URL="jump_url";
    String TRANSFER_INFO="transfer_info";
    String VOICES="voices";
    String BG_IMG="bg_img";
    String INDEX="index";
    String DESCRIPTION="description";
    String READ_STATUS="read_status";
    String AGGREGATE_ID="aggregated_id";
    String AGGREGATE_TYPE="aggregated_type";
    String AGGREGATE_STYLE="aggregated_style";
    String AGGREGATE_ID_INDEX_OF_LIST="aggregated_id_index_of_list";
    String AGGREGATE_TITLE="aggregated_title";
    String ON_TOP="on_top";
    String REMOVE_IDS="remove_ids";
    String IS_CLEAN_CACHE="is_clean_cache";
    String IS_CLEAN_BANNER="is_clean_banner";
    String LOAD_CONTROL="load_control";
    String UPDATE_TIME="update_time";
    String LOAD_NEXT_BEFORE="load_next_before";
    String PULL_DOWN_HINT="pull_down_hint";
    String PULL_DOWN_HINT_DESC="desc";
    String PULL_DOWN_HINT_IMAGE="image";
    String CHANNEL="channel";
    String NAME="name";
    String IS_FIXED="is_fixed";
    String REFRESH_TIME="refresh_time";
    String IS_DEFAULT="is_default";
    String DEFAULT_CHANNEL="default_channel";
    String RECO_CHANNEL="reco_channel";
    String PUBLISH_STATETY="publish_stategy";
    String ICON="icon";
    String OP_MARK_IURL="op_mark_iurl";
    String OP_MARK_TYPE="op_mark_type";
    String OP_SHWOMARK="op_showmark";
    String HYPERLINKS="hyperlinks";
    String DESC="desc";
    String VIEW_CNT="view_cnt";
    String VIDEO_ID="id";
    String SHARE_CNT="share_cnt";
    String SHARE_URL="share_url";
    String PAGE_INFO="page_info";
    String VIDEOS="videos";
    String AUDIOS="audios";
    String LENGTH="length";
    String POSTER="poster";
    String VIDEO_SIZE="video_size";
    String RESOLUTION="resolution";
    String QUALITIES="qualities";
    // SiteLogo
    String SITE_LOGO="site_logo";
    String SITE_LOGO_ID="id";
    String SITE_LOGO_IMAGE="img";
    String SITE_LOGO_STYLE="style";
    String SITE_LOGO_TITLE="desc";
    String SITE_LOGO_URL="link";
    String SITE_LOGO_EDITORS_NAME="editors_name";
    String SITE_LOGO_CREATOR_NAME="creator_name";
    String SITE_LOGO_CREATOR_DEPT="creator_dept";
    // Wm author
    String WM_AUTHORS="wm_authors";
    // 矩阵号卡片头部信息
    String WM_AUTHOR_TITLE="related_wm_author";
    // 已订阅矩阵号数量
    String WM_AUTHOR_COUNT="wm_cnt";
    int LOAD_CONTROL_NORMAL=0;
    int LOAD_CONTROL_FORBIDDEN_LOAD=1;
    String COMMENT_COUNT="commentCnt";
    String COMMENT_ENABLE="cmtEnabled";
    String LIKE_COUNT="like_count";
    String LIKE_ENABLE="like_enable";
    String SHARE_ENABLE="share_enable";
    String FAV_ENABLE="fav_enable";
    // share_info
    String SHARE_INFO="share_info";
    String SHARE_DESC="desc";
    String SHARE_IMAGE="image";
    // Op mark
    String OP_MARK_V2="op_mark_v2";
    String OP_MARK_TYPE_NEW="type";
    String OP_MARK_CONTENT="content";
    String OP_MARK_COLOR="color";
    String OP_MARK_ICON_URL="icon_url";
    String OP_MARK_ICON_COLOR="icon_color";
    String OP_MARK_BACKGROUND_COLOR="background_color";
    String CARD_LABEL="label";
    // live lines
    String LIVE_LINE_LIST_TYPE="type";
    String LIVE_LINE_LIST_IS_DEFAULT="is_default";
    String LIVE_LINE_LIST_TITLE="title";
    String LIVE_LINE_LIST_URL="url";
    String LIVE_LINE_ID="id";
    String LIVE_LINE_IS_DEFAULT="is_default";
    String LIVE_LINE_TITLE="title";
    String LIVE_LINE_LISTS="lists";
    String LIVE_STREAM="live_stream";
    String LIVE_LINES="live_lines";
    String PLAYABLE="playable";
    String PERFORMERS="performers";
    String SUMMARY="summary";
    String CORE_REASON="core_reason";
    String LOCAL_ADCODE="local_adcode";
    String VOICE_PEOPLE_NAME="voice_people_name";
    String VOICE_PEOPLE_GENDER="voice_people_gender";
    String VOICE_STORAGE_INFO="voice_storage_info";
    String VOICE_SEGMENTS="voice_segments";
    String PLAY_LENGTH="play_length";
    String FORMAT="format";
    String BITRATE="bitrate";
    String ON_OFF="on_off";
    /**
     * 直播观看人数
     */
    String WATCH_CNT="watch_cnt";
    /**
     * 直播间
     */
    String LIVE_ROOM="live_room";
    /**
     * 直播间id
     */
    String ROOM_ID="room_id";
    /**
     * 直播状态,0:未知,1:未开始,2:直播中,3:已结束,4:回看
     */
    String LIVE_STATUS="live_status";
    /**
     * 直播开始时间戳,单位ms
     */
    String START_TIME="start_time";
    /**
     * 直播结束时间戳,单位ms
     */
    String END_TIME="end_time";
    /**
     * 直播回看地址
     */
    String REPLAY_URL="replay_url";
    /**
     * 电视台或广播电台的台徽
     */
    String TV_ICON="tv_icon";
    /**
     * 文章阅读/播放次数
     */
    String PLAY_CNT="play_cnt";
    /**
     * 是否展示阅读/播放次数
     */
    String VIEW_ENABLE="view_enable";
    /**
     * 矩阵号作者信息
     */
    String RELATED_WM_AUTHOR="related_wm_author";
    /**
     * 活动参与人数（10.20）
     */
    String PARTICIPANT_CNT="participant_cnt";
    /**
     * 内容源.统一账号库打点用(1120)
     */
    String CONTENT_SOURCE="content_source";
    /**
     * 缩略图
     */
    String THUMBNAIL="thumbnail";
    /**
     * 是否已关注
     */
    String IS_FOLLOWED="is_followed";
    /**
     * 关注开关
     */
    String FOLLOW_ENABLE="follow_enable";
    /**
     * 卡片关联的频道
     */
    String RELATED_CHANNEL="related_channel";
}
