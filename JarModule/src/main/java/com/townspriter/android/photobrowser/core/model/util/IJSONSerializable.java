package com.townspriter.android.photobrowser.core.model.util;

import org.json.JSONException;
import org.json.JSONObject;

/******************************************************************************
 * @Path PhotoBrowserCore:IJSONSerializable
 * @Describe
 * @Name 张飞
 * @Email zhangfei@personedu.com
 * @Data 21-4-6-下午2:42
 * CopyRight(C)2021 智慧培森科技版权所有
 * *****************************************************************************
 */
public interface IJSONSerializable
{
    JSONObject serializeTo() throws JSONException;
    
    void parseFrom(JSONObject jsonObj);
}
