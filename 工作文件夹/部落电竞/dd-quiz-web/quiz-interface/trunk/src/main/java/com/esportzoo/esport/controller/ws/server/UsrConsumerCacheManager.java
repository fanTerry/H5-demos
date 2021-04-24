package com.esportzoo.esport.controller.ws.server;

import com.esportzoo.common.util.MD5;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.service.common.manager.NearcacheManager;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/***
 * 用户相关
 * @author zl
 */
public class UsrConsumerCacheManager {

	private transient Logger logger = LoggerFactory.getLogger(UsrConsumerCacheManager.class);

	@Autowired  
	@Qualifier("redisNearcache")
	private NearcacheManager nearcacheManager;

	private HttpClient httpClient;
 
	private static final String UsrConsumerGroup = "UsrConsumerGroup_"; 
	 
	private static final String userConsumerByIdKey = "userConsumerByIdKey_";
	
	private static final String socketUrlSigByMd5Key = "socketUrlSigByMd5Key_";

	private static final String usrConsumerImeiGroup = "UsrConsumerImei";
	private static final String imeiKey = "imeiKey";
	@Value("${ws_host}")
	private String host;

	public UsrConsumerCacheManager() {
		// 创建httpClient实例对象
		httpClient = new HttpClient();
		// 设置httpClient连接主机服务器超时时间：15000毫秒
		HttpConnectionManagerParams params = httpClient.getHttpConnectionManager().getParams();
		params.setParameter(HttpClientParams.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0");
		params.setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 5000);
		params.setConnectionTimeout(5000);
		params.setSoTimeout(5000);
	}

	/***
 * 根据用户ID缓存用户（基本实时缓存,因为修改会删除缓存）
 * @param userId
 * @param usrConsumer
 */
	public void putUsrConsumerById(long userId,UserConsumer usrConsumer){
		if(usrConsumer!=null){
			nearcacheManager.put(UsrConsumerGroup, userConsumerByIdKey + userId, usrConsumer, 600);//10分钟	
		}
	}
	/***
	 * 根据用户ID缓存用户（基本实时缓存,因为修改会删除缓存）
	 * @param userId
	 * @return
	 */
	public UserConsumer getUsrConsumerById(long userId){
		return nearcacheManager.get(UsrConsumerGroup, userConsumerByIdKey+ userId);
	}
	/***
	 * 根据用户ID缓存用户（基本实时缓存,因为修改会删除缓存）
	 * @param userId
	 */
	public void deleteUsrConsumerById(long userId){
		nearcacheManager.delete(UsrConsumerGroup, userConsumerByIdKey+ userId);
	}
	//签名秘钥
	private String socketSignKey = "d!lR1vdI#1CzH46XhY1Y";
	/***
	 * 生产Md5校验
	 * @return
	 */
	public String buildSocketUrlSigByMd5() {
		String  currTime = new Date().getTime() + "";
		String md5 = MD5.md5Encode(currTime+socketSignKey);
		nearcacheManager.put(UsrConsumerGroup, socketUrlSigByMd5Key + md5, currTime, 300);//300秒	
		return md5;
	}
	
	public String buildWSURL() {
		String sign = buildSocketUrlSigByMd5();
		return host + sign;
	}
	
	private String getSocketUrlSigById(String md5){
		return nearcacheManager.get(UsrConsumerGroup, socketUrlSigByMd5Key + md5); 
	}
	/**
	 * 验证socketUrl合法性
	 * @param md5
	 * @return true表示合法 
	 */
	public boolean checkSocketUrlSign(String md5){
		String time = getSocketUrlSigById(md5);
		if(time==null || time.length()<=0){
			return false;
		}
		String validMd5 = MD5.md5Encode(time+socketSignKey);
		if(validMd5.equals(md5)){
			return true;
		}
		return false;
	}
	
	/**
	 * 6小时 只更新一次
	 * @param imei
	 */
	public void putImei(String imei,int sellClient){
		if(imei!=null && imei.length()>0){
			//6小时-过期
			nearcacheManager.put(usrConsumerImeiGroup, imeiKey+imei+sellClient, imei, 60*60*6);//60*60*6  6小时	
		}
	}
	/***
	 * 是否有缓存
	 * @param imei
	 * @return
	 */
	public String getImei(String imei,int sellClient){
		return nearcacheManager.get(usrConsumerImeiGroup, imeiKey+imei+sellClient);
	}
}
