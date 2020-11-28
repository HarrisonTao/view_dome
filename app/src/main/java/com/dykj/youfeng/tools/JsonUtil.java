package com.dykj.youfeng.tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * JSON工具
 */
public class JsonUtil {
    /**
     * 获取KEY 值
     * @param json 数据格式
     * @param key 名称
     * @return 值
     */
    public static String getJsonData(String json, String key){
        String str="";
        try {
            JSONObject obj =new JSONObject(json);
           str= obj.optString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  str;
    }
    public static int getIntJsonData(String json, String key){
        int str=0;
        try {
            JSONObject obj =new JSONObject(json);
            str= obj.optInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  str;
    }

}
