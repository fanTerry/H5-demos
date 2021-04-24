package com.esportzoo.esport.connect.request.yeyun;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tingjun.wang
 * @date 2019/9/25 10:27
 */
@Data
public class YeYunUserRequest extends BaseRequest implements Serializable {
	private static final long serialVersionUID = -429742352591057131L;

	/**
	 * 本平台 userId
	 */
	private Long userId;
	/**
	 * 请求获取的url类型
	 */
	private Integer urlType;


}
