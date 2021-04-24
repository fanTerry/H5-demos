package com.esportzoo.esport.expert.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.esportzoo.esport.client.service.expert.RecExpertServiceClient;
import com.esportzoo.esport.constants.ExpertStatus;
import com.esportzoo.esport.domain.RecExpert;
import com.esportzoo.esport.expert.constant.CookieConstant;
import com.esportzoo.esport.expert.tool.LoginUtils;
import com.esportzoo.esport.util.MD5;

@Controller
public class LoginController extends BaseController{
	
	@Autowired
	private RecExpertServiceClient recExpertServiceClient;

	@RequestMapping(value = "/login", method=RequestMethod.GET)
	public String doGet(Model model){
		model.addAttribute("isLoginPage", true);
		return "login";
	}
	
	@RequestMapping(value = "/login", method=RequestMethod.POST)
	public String doPost(String nickName, String password, String code,HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr){
		try {
			logger.info("专家后台登录，nickName={}，password={}，code={}", nickName, password, code);
			String randCaptcha = (String)request.getSession().getAttribute("randCaptcha");
			if(StringUtils.isBlank(randCaptcha) || !randCaptcha.equalsIgnoreCase(code)){
				logger.info("专家后台登录，验证码错误");
				attr.addFlashAttribute("msg", "验证码错误");
				return "redirect:login";
			}
			if(StringUtils.isBlank(nickName)){
				logger.info("专家后台登录，昵称不能为空");
				attr.addFlashAttribute("msg", "昵称不能为空");
				return "redirect:login";
			}
			if(StringUtils.isBlank(password)){
				logger.info("专家后台登录，密码不能为空");
				attr.addFlashAttribute("msg", "密码不能为空");
				return "redirect:login";
			}
			RecExpert recExpert = recExpertServiceClient.queryByNickNameAndPassword(nickName, MD5.md5Encode(password)).getModel();
			if (recExpert == null) {
				logger.info("专家后台登录，根据昵称密码从专家表没有查到数据，account={},encodedPassword={}", nickName, MD5.md5Encode(password));
				attr.addFlashAttribute("msg", "昵称或密码错误");
				return "redirect:login";
			}
			if (recExpert.getStatus().intValue() == ExpertStatus.INVALID.getIndex()) {
				logger.info("专家后台登录，专家状态无效，account={},encodedPassword={}", nickName, MD5.md5Encode(password));
				attr.addFlashAttribute("msg", "专家状态无效");
				return "redirect:login";
			}
			logger.info("专家后台登录，登录成功，设置cookie");
			LoginUtils.setLoginCookie(request, response, recExpert);
			if (recExpert.getPassword().equals(MD5.md5Encode("000000"))) {
				return "redirect:/user/editPwd";
			} else {
				return "redirect:/publish/index";
			}
			
		} catch (Exception e) {
			attr.addFlashAttribute("msg", "发生异常" + e.getMessage());
			return "redirect:login";
		}
	}
	
	@RequestMapping("loginOut")
	public String loginOut(HttpServletRequest request, HttpServletResponse response){
		LoginUtils.deleteCookie(request, response, CookieConstant.LOGIN_COOKIE_NAME);
		return "redirect:login";
	}

	
}
