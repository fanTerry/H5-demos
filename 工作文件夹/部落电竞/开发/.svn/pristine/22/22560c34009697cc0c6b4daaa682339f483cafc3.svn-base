package com.esportzoo.esport.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: url工具
 *
 * @author: Haitao.Li
 *
 * @create: 2020-03-19 17:41
 **/
public class URLUtil {


	/** 获取url参数的方法*/
	public static String getParameter(String url, String neededParameter) throws MalformedURLException {
		//logger.info("获取url的参数 url={},neededParameter={}", url, neededParameter);
		URL geturl = new URL(url);
		String parameter = null;
		String urlRef = null;
		//获取#后面的内容
		if (url.contains("#")) {
			urlRef = geturl.getRef();
		} else {
			urlRef = geturl.getQuery();
		}
		if (urlRef.indexOf(neededParameter) >= 0) {
			String[] array = urlRef.split("&");
			for (String str : array) {
				if (str.indexOf(neededParameter) >= 0) {
					parameter = str.substring(str.indexOf("=") + 1);
					break;
				}
			}
		}
		//logger.info("获得的参数 parameter={}", parameter);
		return parameter;
	}

	public static Map<String, List<String>> getQueryParams(String url) {
		try {
			Map<String, List<String>> params = new HashMap<String, List<String>>();
			String[] urlParts = url.split("\\?");
			if (urlParts.length > 1) {
				String query = urlParts[1];
				for (String param : query.split("&")) {
					String[] pair = param.split("=");
					String key = URLDecoder.decode(pair[0], "UTF-8");
					String value = "";
					if (pair.length > 1) {
						value = URLDecoder.decode(pair[1], "UTF-8");
					}

					List<String> values = params.get(key);
					if (values == null) {
						values = new ArrayList<String>();
						params.put(key, values);
					}
					values.add(value);
				}
			}

			return params;
		} catch (UnsupportedEncodingException ex) {
			throw new AssertionError(ex);
		}
	}


}
