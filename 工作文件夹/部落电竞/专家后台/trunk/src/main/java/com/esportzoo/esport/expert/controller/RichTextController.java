package com.esportzoo.esport.expert.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author tingting.shen
 * @date 2019/06/26
 */
@Controller
@RequestMapping(value = "/richText")
public class RichTextController {
	
	@RequestMapping(value = "example5")
	public String example5(HttpServletRequest request){
		return "/richtext/example5";
	}
	
}
