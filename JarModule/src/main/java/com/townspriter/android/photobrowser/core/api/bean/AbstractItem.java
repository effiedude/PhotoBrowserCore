package com.townspriter.android.photobrowser.core.api.bean;

import org.json.JSONException;
import org.json.JSONObject;
import com.townspriter.android.photobrowser.core.model.util.IJSONSerializable;
import com.townspriter.android.photobrowser.core.model.util.InfoFlowConstDef;
import com.townspriter.android.photobrowser.core.model.util.InfoFlowJsonConstDef;

/******************************************************************************
 * @Path PhotoBrowserCore:AbstractItem
 * @Describe 图片浏览器入口类
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public class AbstractItem implements IJSONSerializable
{
    public String id;
    public String categoryId;
    public String recoid;
    /**
     * 透传给leaf的字段.暂时用于分页频道
     */
    public String transfer_info;
    public int item_type=InfoFlowConstDef.DEFAULT_INT_VALUE;
    public int style_type;
    /**
     * 服务器新下发字段(1120)
     * 内容源.统一账号库打点用
     */
    public int content_source=InfoFlowConstDef.DEFAULT_INT_VALUE;
    /**
     * 下面是本地数据
     */
    public long grab_time;
    public int aggregateType=InfoFlowConstDef.DEFAULT_INT_VALUE;
    public int aggregatedStyle=InfoFlowConstDef.DEFAULT_INT_VALUE;
    public String aggregatedId;
    public int aggregatedIdIndexOfList=-1;
    public String aggregatedTitle="";
    public boolean onTop;
    
    public String getTitle()
    {
        return "";
    }
    
    public String getUrl()
    {
        return "";
    }
    
    @Override
    public void parseFrom(JSONObject object)
    {
        if(object==null)
        {
            return;
        }
        id=object.optString(InfoFlowJsonConstDef.ID);
        categoryId=object.optString(InfoFlowJsonConstDef.CATEGORY_ID);
        item_type=object.optInt(InfoFlowJsonConstDef.ITEM_TYPE,InfoFlowConstDef.DEFAULT_INT_VALUE);
        recoid=object.optString(InfoFlowJsonConstDef.RECOID);
        transfer_info=object.optString(InfoFlowJsonConstDef.TRANSFER_INFO);
        grab_time=object.optLong(InfoFlowJsonConstDef.GRAB_TIME);
        aggregatedId=object.optString(InfoFlowJsonConstDef.AGGREGATE_ID);
        aggregateType=object.optInt(InfoFlowJsonConstDef.AGGREGATE_TYPE,InfoFlowConstDef.DEFAULT_INT_VALUE);
        aggregatedStyle=object.optInt(InfoFlowJsonConstDef.AGGREGATE_STYLE,InfoFlowConstDef.DEFAULT_INT_VALUE);
        aggregatedIdIndexOfList=object.optInt(InfoFlowJsonConstDef.AGGREGATE_ID_INDEX_OF_LIST,InfoFlowConstDef.DEFAULT_INT_VALUE);
        aggregatedTitle=object.optString(InfoFlowJsonConstDef.AGGREGATE_TITLE,"");
        onTop=object.optBoolean(InfoFlowJsonConstDef.ON_TOP);
        style_type=object.optInt(InfoFlowJsonConstDef.STYLE_TYPE);
        content_source=object.optInt(InfoFlowJsonConstDef.CONTENT_SOURCE);
    }
    
    @Override
    public JSONObject serializeTo()
    {
        JSONObject jsonObject=new JSONObject();
        try
        {
            jsonObject.put(InfoFlowJsonConstDef.ID,id);
            jsonObject.put(InfoFlowJsonConstDef.CATEGORY_ID,categoryId);
            jsonObject.put(InfoFlowJsonConstDef.RECOID,recoid);
            jsonObject.put(InfoFlowJsonConstDef.ITEM_TYPE,item_type);
            jsonObject.put(InfoFlowJsonConstDef.STYLE_TYPE,style_type);
            jsonObject.put(InfoFlowJsonConstDef.CONTENT_SOURCE,content_source);
            jsonObject.put(InfoFlowJsonConstDef.TRANSFER_INFO,transfer_info);
            jsonObject.put(InfoFlowJsonConstDef.GRAB_TIME,grab_time);
            jsonObject.put(InfoFlowJsonConstDef.AGGREGATE_ID,aggregatedId);
            jsonObject.put(InfoFlowJsonConstDef.AGGREGATE_TYPE,aggregateType);
            jsonObject.put(InfoFlowJsonConstDef.AGGREGATE_STYLE,aggregatedStyle);
            jsonObject.put(InfoFlowJsonConstDef.AGGREGATE_ID_INDEX_OF_LIST,aggregatedIdIndexOfList);
            jsonObject.put(InfoFlowJsonConstDef.AGGREGATE_TITLE,aggregatedTitle);
            jsonObject.put(InfoFlowJsonConstDef.ON_TOP,onTop);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }
    
    public String toSimpleString()
    {
        StringBuilder sb=new StringBuilder();
        sb.append("{").append("id:").append(id).append(",").append("style_type:").append(style_type).append(",").append("item_type:").append(item_type).append(",").append("title:").append(getTitle()).append("}");
        return sb.toString();
    }
}
