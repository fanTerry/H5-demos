package com.esportzoo.esport.connect.request;

import lombok.Data;

/**
 * @description: 获取微信form相关参数
 *
 * @author: Haitao.Li
 *
 * @create: 2019-08-13 14:11
 **/
@Data
public class FormRequest {

	private  String formId ;

	private Long userId;

	private String sourceType;


}
