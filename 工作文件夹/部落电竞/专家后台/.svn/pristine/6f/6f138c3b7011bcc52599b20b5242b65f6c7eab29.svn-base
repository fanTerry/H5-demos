package com.esportzoo.esport.expert.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.esportzoo.esport.domain.RecExpert;
import com.esportzoo.esport.expert.constant.CookieConstant;
import com.esportzoo.esport.util.MD5;


public class LoginUtils {
    
	private static final String domainRegex = "(?<=http://|\\.)([^.]*?\\.(gov.cn|com.cn|com|cn|net|org|biz|info|cc|tv))";
	private static final Pattern DOMAIN_PATTERN = Pattern.compile(domainRegex, Pattern.CASE_INSENSITIVE);
	
	public static void setLoginCookie(HttpServletRequest request, HttpServletResponse response, RecExpert expert) {		
		String md5Id = MD5.md5Encode(expert.getId() + CookieConstant.LOING_COOKIE_MD5);
		String loginInfo = md5Id + "|" + expert.getNickName() + "|" + expert.getId();
		setCookie(request, response, CookieConstant.LOGIN_COOKIE_NAME, loginInfo, -1);
  	}
	
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue, int time) {
    	try {
    		cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(time);
        String domain = getDomain(request.getServerName());
        if (StringUtils.isNotBlank(domain)){
            cookie.setDomain(domain);
        }
        response.addCookie(cookie);
    }
	
	public static String getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return null;
		}
		for (Cookie c : cookies) {
			if (c.getName().equals(cookieName)) {
				String cval = c.getValue();
				try {
					cval = URLDecoder.decode(cval, "UTF-8");
					return cval;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return cval;
				}
			}
		}
		return null;
	}
	
	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieValue) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return;
		}
		for (Cookie cookie : cookies) {
			String tempName = cookie.getName();
			if (cookieValue.equals(tempName)) {
				cookie.setPath("/");
				cookie.setMaxAge(0);
				cookie.setValue("INVILD");
				String domain = getDomain(request.getServerName());
				if (StringUtils.isNotBlank(domain)) {
					cookie.setDomain(domain);
				}
				response.addCookie(cookie);
			}
		}
	}
	
	public static String getDomain(String serverName) {
        String domain = "";
        if (StringUtils.isNotBlank(serverName)) {
            Matcher matcher = DOMAIN_PATTERN.matcher(serverName);
            if (matcher.find()){
                domain = matcher.group(1);
            }
        }
        return domain;
    }
	
   
}
