package com.esportzoo.esport.expert.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.domain.RecExpert;
import com.esportzoo.esport.expert.constant.CookieConstant;
import com.esportzoo.esport.expert.result.ReturnResult;
import com.esportzoo.esport.expert.tool.LoginUtils;
import com.esportzoo.esport.expert.tool.PasswordUtils;
import com.esportzoo.esport.util.MD5;

/**
 * @author tingting.shen
 * @date 2019/05/20
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends BaseController {
	
	@RequestMapping(value = "/editPwd")
	public String editPwd(HttpServletRequest request, Model model){
		RecExpert expert = getLoginExpert(request);
		if (expert == null) {
			return "";
		}
		model.addAttribute("nickName", expert.getNickName());
		return "uc/edit-pwd";
	}
	
	@RequestMapping(value = "/savepwd", method=RequestMethod.POST)
	@ResponseBody
	public ReturnResult savepwd(String oldPwd, String newPwd, String pwdRepeat, Model model, 
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes attr){
		RecExpert expert = getLoginExpert(request);
		if (expert == null) {
			return new ReturnResult(false, "201", "未获取到登录用户");
		}
		if (StringUtils.isBlank(oldPwd) || StringUtils.isBlank(newPwd) || StringUtils.isBlank(pwdRepeat)){
			return new ReturnResult(false, "201", "参数为空");
		}
		if (oldPwd.equals(newPwd)) {
			return new ReturnResult(false, "201", "新密码不能与旧密码一样");
		}
		if (!newPwd.equals(pwdRepeat)) {
			return new ReturnResult(false, "201", "确认密码与新密码不一致");
		}
		if (!expert.getPassword().equals(MD5.md5Encode(oldPwd))) {
			return new ReturnResult(false, "202", "旧密码与数据库不符");
		}
		Map<String, String> pwdCheck = PasswordUtils.validatePwd(newPwd);
		String statuStr = pwdCheck.get("status");
		if(statuStr.equals("fail")) {
			return new ReturnResult(false, "201", pwdCheck.get("errMsg"));
		}
		
		ModelResult<Integer> modelResult = recExpertServiceClient.updatePasswordById(expert.getId(), MD5.md5Encode(newPwd));
		if (!modelResult.isSuccess()) {
			return new ReturnResult(false, "201", modelResult.getErrorMsg());
		}
		Integer affectCount = modelResult.getModel();
		if (affectCount==null || affectCount.intValue()<=0) {
			return new ReturnResult(false, "203", "没有修改成功");
		}
		LoginUtils.deleteCookie(request, response, CookieConstant.LOGIN_COOKIE_NAME);
		return new ReturnResult(true, "200", "修改成功");
	}
	
}
