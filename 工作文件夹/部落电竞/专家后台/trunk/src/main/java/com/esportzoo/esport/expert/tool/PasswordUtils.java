package com.esportzoo.esport.expert.tool;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

public final class PasswordUtils {
	
    static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9a-zA-Z]{6,15}$");

    public PasswordUtils() {
    }

    public static Map<String, String> validatePwd(String pwd) {
        HashMap<String, String> result = new HashMap<>(4);
        pwd = StringUtils.trim(pwd);
        if(StringUtils.isBlank(pwd)) {
            result.put("status", "fail");
            result.put("errMsg", "输入密码不能为空。");
            return result;
        } else if(pwd.length() >= 6 && pwd.length() <= 20) {
            boolean baseValidate = PASSWORD_PATTERN.matcher(pwd).matches();
            if(!baseValidate) {
                result.put("status", "fail");
                result.put("errMsg", "密码不能为数字、字母以外字符。");
                return result;
            } else if(isRepeat(pwd)) {
                result.put("status", "fail");
                result.put("errMsg", "密码不能为重复数字或字母。");
                return result;
            } else if(isOrder(pwd)) {
                result.put("status", "fail");
                result.put("errMsg", "密码不能为连续数字或字母。");
                return result;
            } else {
                result.put("status", "success");
                return result;
            }
        } else {
            result.put("status", "fail");
            result.put("errMsg", "请输入长度在6-15位内的密码。");
            return result;
        }
    }

    private static boolean isRepeat(String pwd) {
        String regex = pwd.substring(0, 1) + "{" + pwd.length() + "}";
        return pwd.matches(regex);
    }

    private static boolean isOrder(String pwd) {
        char[] chars = pwd.toCharArray();
        boolean orderFlag = true;

        int i;
        int j;
        for(i = 1; i < chars.length; ++i) {
            j = i - 1;
            if(chars[i] - chars[j] != 1) {
                orderFlag = false;
                break;
            }
            chars[j] = chars[i];
        }
        if(!orderFlag) {
            for(i = 1; i < chars.length; ++i) {
                j = i - 1;
                if(chars[j] - chars[i] != 1) {
                    orderFlag = false;
                    break;
                }
                chars[j] = chars[i];
            }
        }
        return orderFlag;
    }

    
}