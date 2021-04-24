package com.esportzoo.esport.expert.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.esportzoo.esport.expert.tool.CaptchaUtil;

@Controller
@RequestMapping("captcha")
public class CaptchaController {
	
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	
	@RequestMapping("")
	public void index() {
		// 设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 在内存中创建图象
		Map<String, Object> codeMap = CaptchaUtil.getCaptcha(80, 30);
		BufferedImage image = (BufferedImage) codeMap.get("image");
		String code = (String) codeMap.get("code");
		//logger.info("生成的验证码:"+code) ;
		HttpSession session = request.getSession(true);
		session.setAttribute("randCaptcha", code);
		// 输出图象到页面
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
