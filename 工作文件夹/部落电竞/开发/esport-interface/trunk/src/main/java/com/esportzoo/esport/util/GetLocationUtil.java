package com.esportzoo.esport.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据ip获取用户所在地 百度-->搜狐-->Pconline-->淘宝
 * 
 * @author: jing.wu
 * @date:2018年6月16日上午10:54:31
 */
public class GetLocationUtil {

	private static final Logger logger = LoggerFactory.getLogger(GetLocationUtil.class);
	
	// 淘宝根据ip取出所在地
	public static String get_ip_taobao_url = "http://ip.taobao.com/service/getIpInfo.php?ip=";

	// 搜狐根据ip取出所在地
	public static String get_ip_sohu_url = "http://pv.sohu.com/cityjson?ie=utf-8";

	// 百度根据ip取出所在地
	public static String get_ip_baidu_url = "https://api.map.baidu.com/location/ip";
	public static String baidu_ip_ak = "5t0d3UYwCs49ZP7D2jnp49rVp7HBP5lL";

	// pconline根据ip取出所在地
	public static String get_ip_pconline_url = "http://whois.pconline.com.cn/ip.jsp";
	
	
	public static Map getTaoBaoLocation(String clientIp) {
		Map<String, String> m = new HashMap<>();
		try {
			String httpContent = HttpUtil.get(get_ip_taobao_url + clientIp);
			JSONObject jsonObject = JSON.parseObject(httpContent);
			Map<String, Object> map = (Map) jsonObject;
			String code = String.valueOf(map.get("code"));// 0：成功，1：失败。
			if ("0".equals(code)) {// 成功
				Map<String, Object> data = (Map<String, Object>) map.get("data");
				m.put("region",String.valueOf(data.get("region")));// 省（自治区或直辖市）
				m.put("city",String.valueOf(data.get("city")));// 市
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return m;
	}

	public static String getBaiDuLocation(String clientIp) {
		String clientProvince = "";
		try {
			String baiduMapUrl = get_ip_baidu_url + "?ip=" + clientIp + "&ak=" + baidu_ip_ak;
			String httpContent = HttpUtil.get(baiduMapUrl);
			JSONObject json = JSON.parseObject(httpContent);
			if ("0".equals(json.getString("status")) && httpContent.indexOf("province") > -1) {
				clientProvince = json.getJSONObject("content").getJSONObject("address_detail").getString("province");
				if (StrUtil.isNotEmpty(clientProvince)) {
					clientProvince = clientProvince.substring(0, 2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientProvince;
	}

	public static String getSohuLocation(String clientIp) {
		String clientProvince = "";
		try {
			String httpContent = HttpUtil.get(get_ip_sohu_url);
			if (StrUtil.isNotBlank(httpContent) && httpContent.indexOf("=") > -1) {
				String jsonStr = httpContent.split("\\=")[1].replace(";", "");
				JSONObject json = JSON.parseObject(jsonStr);
				clientProvince = json.getString("cname");
				if (StrUtil.isNotEmpty(clientProvince)) {
					clientProvince = clientProvince.substring(0, 2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientProvince;
	}
	
	public static String getPcOnlineLocation(String clientIp) {
		String clientProvince = "";
		try {
			String httpContent = HttpUtil.get(get_ip_pconline_url).trim();
			if (StrUtil.isNotBlank(httpContent) && httpContent.length() >= 2) {
				clientProvince = httpContent.substring(0, 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientProvince;
	}

	public static String getLocationByIp(String ip) {
		if (StrUtil.isBlank(ip)) {
			return "";
		}
		String baiduPro = getBaiDuLocation(ip);
		if (StrUtil.isNotBlank(baiduPro)) {
			return baiduPro;
		}
		String sohuPro = getSohuLocation(ip);
		if (StrUtil.isNotBlank(sohuPro)) {
			return sohuPro;
		}
		String pcOnlinePro = getPcOnlineLocation(ip);
		if (StrUtil.isNotBlank(pcOnlinePro)) {
			return pcOnlinePro;
		}
		/*String taobaoPro = getTaoBaoLocation(ip);
		if (StrUtil.isNotBlank(taobaoPro)) {
			return taobaoPro;
		}*/
		return "";
	}

	/**
	 * @Description: 判断用户是否属于某些省份,如果ip在省份列表中，返回true,不在省份列表内，返回false
	 * @param proviceArray 省份列表(统一取各省份前两个字作为省名)
	 * @param other 当无法查询到ip省份或为空时，返回值
	 * @Return boolean
	 * @Date 2018/7/19 17:45
	 */
	public static boolean judeIpSourceByProvice(String clientIp, String [] proviceArray, boolean other){
		logger.info( "判断当前ip省份,当前ip[{}]", clientIp);
		if (StrUtil.isEmpty(clientIp)) {//ip为空的直接返回
			logger.error("该Ip为空");
			return true&&other;
		}
		logger.info("省份名单[{}]", JSONObject.toJSONString(proviceArray));
		try {
			/**根据ip获取用户省份*/
			String clientProvince = getIpInfo(clientIp);
			if (StrUtil.isNotBlank(clientProvince)) {
				/**截取省份前两位文字*/
				clientProvince = clientProvince.substring(0, 2);
			} else {
				return true&&other;
			}
			List<String> listProvice = Arrays.asList(proviceArray);
			if (listProvice.contains(clientProvince)) {
                return true;
            }
		} catch (Exception e) {
			logger.error("[GetLocationUtil.judeIpSourceByProvice.getClientIp]" + e.getStackTrace());
			e.printStackTrace();
            return true&&other;
		}
		return false;
	}
	
	/**全部走百度ip查询,不轮询处理,百度取不到直接返回*/
	public static String getIpInfo(String ip) {
		if (StrUtil.isBlank(ip)) {
			return "";
		}
		String baiduPro = GetLocationUtil.getBaiDuLocation(ip);
		return StrUtil.isNotBlank(baiduPro)? baiduPro : "";
	}
	
	
	public static void main(String[] args) {
		/*System.out.println("从百度获取=" + getBaiDuLocation("183.15.243.236"));
		System.out.println("从搜狐获取=" + getSohuLocation("183.15.243.236"));
		System.out.println("从淘宝获取=" + getTaoBaoLocation("183.15.243.236"));
		System.out.println(getPcOnlineLocation(""));
        String [] arry = new String[]{"广东"};
        System.out.println(judeIpSourceByProvice("",arry,true));*/
	}
	
}