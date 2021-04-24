package com.esportzoo.esport.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.BizSystem;
import com.esportzoo.esport.manager.UboxH5Manager;
import com.esportzoo.esport.util.RealIPUtils;
import com.esportzoo.esport.vo.ubox.UboxUserH5Vo;

/**
 * @author tingting.shen
 * @date 2019/07/24
 */
@Controller
@RequestMapping("third")
public class ThirdLoginController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UboxH5Manager uboxH5Manager;

	@RequestMapping("/uboxOauth")
	public ModelAndView uboxSendOauthRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			String agentId = request.getParameter("agentId");
			if (StringUtils.isBlank(agentId)) {
				agentId = "100103";
			}
			String biz = request.getParameter("biz");
			if (StringUtils.isBlank(biz)) {
				biz = "2";
			}
			String redirectUrl = request.getParameter("redirect");
			logger.info("友宝授权请求redirectUrl={}", redirectUrl);
			if (StringUtils.isBlank(redirectUrl)) {
				redirectUrl = "https://m.esportzoo.com";
			}
			logger.info("友宝授权请求最终redirectUrl={}", redirectUrl);
			String callBackUrl = uboxH5Manager.getOauthRequestUrl(agentId, biz, redirectUrl);
			return new ModelAndView(new RedirectView(callBackUrl));
		} catch (Exception e) {
			logger.info("向友宝发送授权请求发生异常，exception={}", e.getMessage(), e);
			return null;
		}
	}

	@RequestMapping("/uboxcallback")
	public ModelAndView uboxCallBack(HttpServletRequest request, HttpServletResponse response) {
		String logPrefix = "友宝回调处理_";
		logger.info(logPrefix + "接收到请求");
		ModelAndView view = new ModelAndView();
		try {
			String agentId = request.getParameter("agentId");
			if (StringUtils.isBlank(agentId)) {
				agentId = "100103";
			}
			String biz = request.getParameter("biz");
			if (StringUtils.isBlank(biz)) {
				biz = "2";
			}
			String uboxAuthCode = request.getParameter("uboxauthcode");
			if (StringUtils.isBlank(uboxAuthCode)) {
				logger.info(logPrefix + "uboxAuthCode为空");
				return null;
			}
			UboxUserH5Vo uboxUserH5Vo = uboxH5Manager.getUboxUserInfo(uboxAuthCode);
			if (uboxUserH5Vo != null) {
				uboxH5Manager.uboxUserRegistAndLogin(uboxUserH5Vo, Long.parseLong(agentId), request, response);
			}
			String callBackUrl = request.getParameter("callBackUrl");
			logger.info(logPrefix + "callBackUrl={}", callBackUrl);
			view.setView(new RedirectView(callBackUrl + "?agentId=" + agentId + "&biz=" + biz));
			return view;
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常，exception={}", e.getMessage(), e);
			return null;
		}
	}

	@RequestMapping("/uboxIndex")
	public void uboxIndex(HttpServletRequest request, HttpServletResponse response) {
		try {
			String agentId = request.getParameter("agentId");
			if (StringUtils.isBlank(agentId)) {
				agentId = "100103";
			}
			String biz = request.getParameter("biz");
			if (StringUtils.isBlank(biz)) {
				biz = "2";
			}
			String redirectUrl = request.getParameter("redirect");
			logger.info("友宝请求redirectUrl={}", redirectUrl);
			if (StringUtils.isBlank(redirectUrl)) {
				redirectUrl = "https://m.esportzoo.com";
			}
			logger.info("友宝请求最终redirectUrl={}", redirectUrl);
			String code = request.getParameter("uboxauthcode");
			if (StringUtils.isBlank(code)) {
				logger.info("重定向到友宝授权");
				response.sendRedirect("/third/uboxOauth?agentId=" + agentId + "&biz=" + biz + "&redirect=" + redirectUrl);
			} else {
				UboxUserH5Vo uboxUserH5Vo = uboxH5Manager.getUboxUserInfo(code);
				if (uboxUserH5Vo != null) {
					uboxH5Manager.uboxUserRegistAndLogin(uboxUserH5Vo, Long.parseLong(agentId), request, response);
				}
				response.sendRedirect(redirectUrl + "?agentId=" + agentId + "&biz=" + biz);
			}
		} catch (Exception e) {
			logger.info("从友宝进入h5首页发生异常，exception={}", e.getMessage(), e);
		}
	}

	@RequestMapping(value = "/login")
	@ResponseBody
	public CommonResponse<String> thirdlogin(HttpServletRequest request, HttpServletResponse response) {
		String logPrefix = "第三方系统登录_";
		try {
			String agentId = request.getParameter("agentId");
			String biz = request.getParameter("biz");
			String redirect = request.getParameter("redirect");
			logger.info(logPrefix + "agentId={}, biz={}, redirect={}", agentId, biz, redirect);
			if (StringUtils.isBlank(agentId) || StringUtils.isBlank(biz) || StringUtils.isBlank(redirect)) {
				logger.info(logPrefix + "必要参数为空");
				return CommonResponse.withErrorResp("必要参数为空");
			}

			String h5Domain = "https://m.esportzoo.com";
			String apiDomain = "https://api.esportzoo.com";
			String serviceIp = RealIPUtils.getRealIp();
			logger.info(logPrefix + "获得服务器IP为serviceIp={}", serviceIp);
			if (serviceIp.equals("192.168.9.230")) {
				logger.info(logPrefix + "是daily环境");
				h5Domain = "https://daily-m.esportzoo.com";
				apiDomain = "https://daily-api.esportzoo.com";
			} else if (serviceIp.equals("47.112.96.253") || serviceIp.equals("172.30.30.168")) {
				logger.info(logPrefix + "是beta环境");
				h5Domain = "https://beta-m.esportzoo.com";
				apiDomain = "https://beta-api.esportzoo.com";
			}

			// String redirectUrl = h5Domain + redirect + "?agentId=" + agentId
			// + "&biz=" + biz;

			String redirectUrl = "";
			try {
				redirectUrl = URLDecoder.decode(redirect, "utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("回调地址解码异常");
			}

			logger.info(logPrefix + "redirectUrl={}", redirectUrl);
			redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
			logger.info(logPrefix + "encodedRedirectUrl={}", redirectUrl);

			if (biz.equals(BizSystem.UBOX.getIndex() + "")) {
				logger.info(logPrefix + "重定向到友宝授权");
				String oauthPath = apiDomain + "/third/uboxOauth1?agentId=" + agentId + "&redirect=" + redirectUrl;
				logger.info(logPrefix + "oauthPath={}", oauthPath);
				response.sendRedirect(oauthPath);
			}

			logger.info(logPrefix + "返回给前端");
			return CommonResponse.withSuccessResp("success");

		} catch (Exception e) {
			logger.info(logPrefix + "发生异常，exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp("exception");
		}
	}

	@RequestMapping("/uboxOauth1")
	public ModelAndView uboxSendOauthRequest1(HttpServletRequest request, HttpServletResponse response) {
		try {
			String agentId = request.getParameter("agentId");
			String redirectUrl = request.getParameter("redirect");
			logger.info("友宝授权请求1,agentId={},redirectUrl={}", agentId, redirectUrl);
			redirectUrl = URLDecoder.decode(redirectUrl, "utf-8");
			logger.info("友宝授权请求1,redirectUrl={}", redirectUrl);
			String callBackUrl = uboxH5Manager.getOauthRequestUrl1(redirectUrl, agentId);
			logger.info("友宝授权请求1,callBackUrl={}", callBackUrl);
			return new ModelAndView(new RedirectView(callBackUrl));
		} catch (Exception e) {
			logger.info("向友宝发送授权请求1,发生异常，exception={}", e.getMessage(), e);
			return null;
		}
	}

	@RequestMapping("/uboxcallback1")
	public ModelAndView uboxCallBack1(HttpServletRequest request, HttpServletResponse response) {
		String logPrefix = "友宝回调处理1_";
		logger.info(logPrefix + "接收到请求");
		ModelAndView view = new ModelAndView();
		try {
			String uboxAuthCode = request.getParameter("uboxauthcode");
			String callBackUrl = request.getParameter("callBackUrl");
			String agentId = request.getParameter("agentId");
			String biz = request.getParameter("biz");
			logger.info(logPrefix + "uboxAuthCode={},callBackUrl={},agentId={},biz={}", uboxAuthCode, callBackUrl, agentId, biz);
			if (StringUtils.isBlank(uboxAuthCode) || StringUtils.isBlank(callBackUrl) || StringUtils.isBlank(agentId)) {
				logger.info(logPrefix + "必要参数为空");
				return null;
			}
			UboxUserH5Vo uboxUserH5Vo = uboxH5Manager.getUboxUserInfo(uboxAuthCode);
			if (uboxUserH5Vo != null) {
				uboxH5Manager.uboxUserRegistAndLogin(uboxUserH5Vo, Long.parseLong(agentId), request, response);
			}
			if (!callBackUrl.contains("biz=")) {
				if (callBackUrl.endsWith("?")) {
					callBackUrl += "biz=" + biz;
				} else {
					callBackUrl += "&biz=" + biz;
				}
			}
			callBackUrl += "&loginflag=1";
			logger.info(logPrefix + "callBackUrl={}", callBackUrl);
			view.setView(new RedirectView(callBackUrl));
			return view;
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常，exception={}", e.getMessage(), e);
			return null;
		}
	}

}
