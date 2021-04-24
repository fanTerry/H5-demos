package com.esportzoo.esport.expert.interceptor;

import java.beans.PropertyEditorSupport;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.esportzoo.common.util.DateUtil;



public class CalendarEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.isBlank(text)) {
		    setValue(null);
		} 
		else {
		    boolean flag = Pattern.matches("\\d{4}-\\d{2}-\\d{2}", text);
		    if (flag) {
		    	setValue(DateUtil.parseYYYY_MM_DD(text));
		    } 
		    else {
		    	if (Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", text)) {
		    		text += ":00";
		    	}
		    	else if (Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}", text)){
		    		text += ":00:00";
		    	}
		    	setValue(DateUtil.parseYYYY_MM_DD_HH_MM_SS(text));
		    }
		}
    }

}
