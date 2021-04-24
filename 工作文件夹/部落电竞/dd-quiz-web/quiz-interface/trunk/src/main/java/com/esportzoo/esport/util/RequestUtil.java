package com.esportzoo.esport.util;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestUtil {
	
	
	//重写getQueryString 方法，使得程序能获取post的参数
	public static String getQueryString(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer("");
		Enumeration<?> e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String name = (String) e.nextElement();
            String[] value = request.getParameterValues(name);
            sb.append(name).append("=").append(StringUtils.join(value, ",")).append("&");
        }
        if (sb.length() == 0) {
			return "";
		}
		return sb.substring(0, sb.length() - 1).toString();
	}
	
	/**
	 * 获取request对象
	 * @return
	 * @create_time 2013-4-18
	 */
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
     * 有值就会返回ip,没有就返回null
     * @param request
     * @return
     * @create_time 2013-1-15 上午10:50:27
     */
    public static String getClientIp(HttpServletRequest request){
//        x-forwarded-for ip是一个列表
        String ip = request.getHeader("x-forwarded-for");      
        try{
	        //Proxy-Client-IP 字段和 WL-Proxy-Client-IP 字段只在 Apache（Weblogic Plug-In Enable）+WebLogic 搭配下出现，其中“WL” 就是 WebLogic 的缩写。
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	            ip = request.getHeader("Proxy-Client-IP");      
	        }      
	        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	            ip = request.getHeader("WL-Proxy-Client-IP");      
	         }
	        //以上都为null 就调用原始获取ip的方法
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	             ip = request.getRemoteAddr();      
	        }  
	       //如果啥都没有就返回null
	       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	          return null; 
	       }else{
	           String[] ipArr = ip.split(",");
	           ip = ipArr[0];
	       }
        }catch(Exception e){
        	e.printStackTrace();
        }
        return ip;      
    }
    
    /**
	 * 得到请求url域名
	 * @return
	 */
	public static String getRequestDomainName(){
	    
		HttpServletRequest request = getRequest();
		return request.getScheme()+"://"+request.getServerName();
	}
    
    
    /**
     * 得到请求url域名
     * @return
     */
    public static String getOriginalDomainName(){
        
        HttpServletRequest request = getRequest();
        String scheme = request.getScheme();
        if (request.getHeader("x-forwarded-proto") != null) {
            if (request.getHeader("x-forwarded-proto").indexOf("https") != -1) {
                scheme = "https";
            }
        }
        return scheme + "://" + request.getServerName();
    }
    
}
