package com.esportzoo.esport.expert.interceptor;


import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;


public class DateEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.isBlank(text)) {
		    setValue(null);
		} 
		else {
			boolean flag = Pattern.matches("\\d{4}-\\d{2}-\\d{2}", text);
		    if (flag) {
		    	
		    	setValue(parseYYYY_MM_DD(text));
		    } 
		    else {
		    	if (Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}", text)) {
		    		text += ":00";
		    	}
		    	else if (Pattern.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}", text)){
		    		text += ":00:00";
		    	}
		    	setValue(parseYYYY_MM_DD_HH_MM_SS(text));
		    }
		}
    }
    
    public static Date parseYYYY_MM_DD(String parameterValue) {
        if (parameterValue == null || parameterValue.length() == 0) {
            return null;
        }
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(parameterValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Date parseYYYY_MM_DD_HH_MM_SS(String parameterValue) {
        if (parameterValue == null || parameterValue.length() == 0) {
            return null;
        }
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(parameterValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
