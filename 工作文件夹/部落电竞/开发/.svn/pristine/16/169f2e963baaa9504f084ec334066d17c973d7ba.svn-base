package com.esportzoo.esport.controller.aliyun;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esportzoo.esport.client.aliyun.AliyunVideo;
import com.esportzoo.esport.util.MD5;

/**
 * @author tingting.shen
 * @date 2019/06/04
 */
public class AliyunRequestValid {
	
	private static final Logger logger = LoggerFactory.getLogger(AliyunRequestValid.class);
	private static String logPrefix= "阿里云回调鉴权_";
	
	public static boolean passValid(HttpServletRequest request, AliyunVideo aliyunVideo) {
		String timeStamp = request.getHeader("X-VOD-TIMESTAMP");
		String signature = request.getHeader("X-VOD-SIGNATURE");
		logger.info(logPrefix + "timeStamp={},signature={},callbackUrl={},privateKey={}", timeStamp, signature, aliyunVideo.getValidCallbackUrl(), aliyunVideo.getValidPrivateKey());
		String caledSign = getSign(aliyunVideo.getValidCallbackUrl(), timeStamp, aliyunVideo.getValidPrivateKey());
		logger.info(logPrefix + "caledSign={}", caledSign);
		return caledSign.equals(signature);
	}
	
	public static String getSign(String url, String timeStamp, String privateKey) {
		return MD5.md5Encode(url + "|" + timeStamp + "|" + privateKey);
	}
	
	public static void main(String[] args) {
		String url = "https://www.example.com/your/callback";
		String timeStamp = "1519375990";
		String privateKey = "test123";
		String signStr = getSign(url, timeStamp, privateKey);
		System.out.println(signStr);
		System.out.println(signStr.equals("c72b60894140fa98920f1279219b7ed4"));
	}
	
}
