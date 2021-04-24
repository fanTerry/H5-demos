package com.esportzoo.esport.util;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class JsonUtil {

	/**
	 * 用fastjson 将jsonString 解析成 Object
	 * 
	 * @param jsonStr
	 * @return
	 * @create_time 2016年5月31日 下午3:04:37
	 */
	public final static JSONObject strJson2JsonObject(String jsonStr) {
		JSONObject jsonObject = JSON.parseObject(jsonStr);
		return jsonObject;
	}

	/**
	 * 用fastjson 将object 解析成jsonString
	 * 
	 * @param object
	 * @return
	 * @create_time 2016年5月31日 下午3:04:58
	 */
	public static String createJsonString(Object object) {
		String jsonString = "";
		try {
			jsonString = JSON.toJSONString(object);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jsonString;
	}

	/**
	 * 用fastjson 将json字符串解析为一个 JavaBean
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 * @create_time 2016年5月31日 下午3:04:05
	 */
	public static <T> T strJson2Object(String jsonString, Class<T> cls) {
		T t = null;
		try {
			t = JSON.parseObject(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return t;
	}

	/**
	 * 用fastjson 将json字符串 解析成为一个 List<JavaBean> 及 List<String>
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 * @create_time 2016年5月31日 下午3:03:52
	 */
	public static <T> List<T> strJson2List(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		try {
			list = JSON.parseArray(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	/** 
     * list集合转json格式字符串 
     */ 
	public static String list2StrJson(List<Object> list){
		String jsonText = JSON.toJSONString(list, true);
		return jsonText;
	}
	
	/**
	 * 用fastjson 将jsonString 解析成 List<Map<String,Object>>
	 * 
	 * @param jsonString
	 * @return
	 * @create_time 2016年5月31日 下午3:03:08
	 */
	public static List<Map<String, Object>> strJson2ListMap(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			// 两种写法
			// list = JSON.parseObject(jsonString, new
			// TypeReference<List<Map<String, Object>>>(){}.getType());

			list = JSON.parseObject(jsonString,
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;

	}

	/**
	 * 用fastjson 将jsonString 解析成 Map<String,Object>
	 * 
	 * @param jsonStr
	 * @return
	 * @throws ParseException
	 * @create_time 2016年5月31日 下午3:04:15
	 */
	public final static Map<String, Object> strJson2Map(String jsonStr){
		Map<String, Object> dataMap = JSON.parseObject(jsonStr,
				new TypeReference<Map<String, Object>>() {
				});
		return dataMap;
	}

	 /** 
     * map转json格式字符串 
     */  
    public final static String map2StrJson(Map<String, Object> map){
    	String jsonText = JSON.toJSONString(map, true); 
    	return jsonText;
    }
    
    /**
     * 数组转json格式字符串 
     * @param arr
     * @return
     * @create_time 2016年5月31日 下午3:21:14
     */
    public static String array2Json(Object[] arr){
    	String jsonText = JSON.toJSONString(arr, true); 
    	return jsonText;
    }
    
    /** 
     * json格式字符串转数组 
     */  
    public static JSONArray srtJson2Array(String jsonText){  
        JSONArray jsonArr = JSON.parseArray(jsonText);  
        return jsonArr;
    }
    
}