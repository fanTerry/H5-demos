package com.esportzoo.esport.manager;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.enumerate.JedisEnum;
import com.esportzoo.common.redisclient.core.JedisClusterClientImpl;
import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.common.util.RandomUtil;
import com.esportzoo.esport.client.service.cms.CmsContentServiceClient;
import com.esportzoo.esport.constant.CachedKeyAndTimeLong;
import com.esportzoo.esport.constants.BizSystem;
import com.esportzoo.esport.constants.CacheType;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.util.RealIPUtils;
import com.esportzoo.esport.vo.MemberSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.SocketException;
import java.util.Date;
import java.util.Set;

@Component("cachedManager")
public class CachedManager {

	/*@Autowired
	private MemcachedClient memcachedClient;*/
	private transient static final Logger logger = LoggerFactory.getLogger(CachedManager.class);

	@Autowired
	private JedisClusterClientImpl redisClusterClient;

	@Autowired
	private RedisClientManager redisClientManager;

	@Autowired
	@Qualifier("cmsContentServiceClient")
	private CmsContentServiceClient cmsContentServiceClient;

	@Value("${environ}")
	public String currentEnviron;

	private final String environ = "beta";

	public static final String CACHE_KEY_REMOVE_REPEAT_LIST = "cache_key_remove_repeat_list";
	public static final String CACHE_KEYID_LIST = "cache_keyid_list";
	public static final String GOOD_PURCHASE = "good_purchase_";

	public <T> T get(final String key) {
		return redisClusterClient.getObj(key);
	}

	public boolean setNx(final String key, final int expireTimeInSecond, final String value) {
		return redisClusterClient.setNX(key, value, expireTimeInSecond);
	}

	public <T> boolean set(final String key, final int expireTimeInSecond, final Object value) {
		return redisClusterClient.setObj(key, expireTimeInSecond, value);
	}

	public void delete(final String key) {
		redisClusterClient.del(key);
	}

	/**
	 * 缓存用户信息有效
	 *
	 * @param member
	 * @param sid
	 */
	public void cachedMemberSession(UserConsumer member, String sid) {

		final String key = CachedKeyAndTimeLong.buildKey(sid);
		MemberSession mSession = new MemberSession();
		mSession.setMember(member);
		mSession.setLastTime(String.valueOf(System.currentTimeMillis()));
		//如果是beta环境 信息缓存到线上缓存
		if (environ.equals(currentEnviron)) {
			logger.info("in beta --- cachedMemberSession set to production");
			JedisEnum.JEDISPOOL.setObj(key, mSession, CachedKeyAndTimeLong.MEMBER_MEMCACHE_EXP);
		} else {
			redisClusterClient.setObj(key, CachedKeyAndTimeLong.MEMBER_MEMCACHE_EXP, mSession);
		}
	}


	/**
	 * 缓存用户信息有效,默认缓存30天
	 *
	 * @param member
	 * @param sid
	 */
	public void cachedMemberSessionByDay(UserConsumer member, String sid) {

		final String key = CachedKeyAndTimeLong.buildKey(sid);
		MemberSession mSession = new MemberSession();
		mSession.setMember(member);
		mSession.setLastTime(String.valueOf(System.currentTimeMillis()));
//		logger.info("app登录，设置一个月的登录态时间");
		Integer timeExpr = CachedKeyAndTimeLong.MEMBER_MEMCACHE_EXP_MONTH;
		if (environ.equals(currentEnviron)) {
//			logger.info("in beta --- cachedMemberSessionByDay set to production");
			JedisEnum.JEDISPOOL.setObj(key, mSession, timeExpr);
		} else {
			redisClusterClient.setObj(key, timeExpr, mSession);
		}
	}

	/**
	 * 取缓存的 member
	 * @param sid
	 * @return
	 */
	public MemberSession getCachedMemberSession(String sid) {

		final String key = CachedKeyAndTimeLong.buildKey(sid);
		if (environ.equals(currentEnviron)) {
			MemberSession obj = JedisEnum.JEDISPOOL.getObj(key);
//			logger.info("in beta --- getCachedMemberSession get to production  [{}]", JSONObject.toJSONString(obj));
			return obj;
		} else {
			return redisClusterClient.getObj(key);
		}
	}


	/**
	 * 利用Redis排重 ，例如根据用户ID排重统计文章阅读是
	 * @param type 排重类型，资讯文章，视频，专家文章等等 {@link com.esportzoo.esport.constants.CacheType}
	 * @param keyId 储存的key，一般为要统计的目标 例如文章ID
	 * @param targetId 排重列表 例如用户ID
	 */
	public void cacheSet(Integer type, String keyId, String targetId) {
		/*存储文章ID*/
		try {
			CacheType cacheType = CacheType.valueOf(type);
			String typeKey = CACHE_KEYID_LIST + cacheType.getDescription();
			redisClusterClient.sadd(typeKey, keyId);
			String key = CACHE_KEY_REMOVE_REPEAT_LIST + cacheType.getDescription() + keyId;
			redisClusterClient.sadd(key, targetId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("缓存获取文章阅读数出错-cacheSet，异常={}", e.getMessage());
		}

	}

	/**
	 * 获取排重集合的元素个数
	 * @param type
	 * @param keyId
	 * @return
	 */
	public Long getCacheSetSize(Integer type, String keyId) throws SocketException {
		Long aLong = 0L;
		String key = "";
		try {
			CacheType cacheType = CacheType.valueOf(type);
			key = CACHE_KEY_REMOVE_REPEAT_LIST + cacheType.getDescription() + keyId;
			aLong = redisClusterClient.scard(key);
		} catch (Exception e) {
			String realIp = RealIPUtils.getRealIp();
			if (realIp.equals("192.168.9.230") || realIp.equals("192.168.9.67")) {
			} else {
				logger.error("缓存获取文章阅读数出错-getCacheSetSize，key={},异常={},", key, e.getMessage());
			}

		}
		//logger.info("缓存返回阅读数：【{}】", aLong);
		return aLong;
	}

	/**
	 * 获取key列表的元素集合
	 * @param type
	 * @param keyId
	 * @return
	 */
	public Set<String> getCacheSet(Integer type, String keyId) {
		CacheType cacheType = CacheType.valueOf(type);
		String key = CACHE_KEY_REMOVE_REPEAT_LIST + cacheType.getDescription();
		return redisClientManager.setMembers(key);
	}

	/**
	 * 查看商品的发布时间，根据时间梯度递增虚增的商品购买人数
	 * @param good
	 * @return
	 */
	public Long getGoodPurchase(Long goodId,Date publishTime) {

		//根据发布时间，确定虚增的人数范围
		Date threeDaybefore = DateUtil.getDateBefore(new Date(), 3);
		Date senvenDaybefore = DateUtil.getDateBefore(new Date(), 7);
		Date fithtenDaybefore = DateUtil.getDateBefore(new Date(), 15);

		String saveLevel = "0";
		if (DateUtil.calcCompareDate3(publishTime, threeDaybefore) == 1) {

			saveLevel = "1";

		} else if (DateUtil.calcCompareDate3(publishTime, senvenDaybefore) == 1) {
			saveLevel = "2";

		} else if (DateUtil.calcCompareDate3(publishTime, fithtenDaybefore) == 1) {
			saveLevel = "3";
		}
		String key = GOOD_PURCHASE + goodId;
		String levelKey = "time_level_key_" + goodId;
		String num = redisClusterClient.get(key);
		String level = redisClusterClient.get(levelKey);

		//如果时间范围有变化，更新存放的虚增人数
		if (StringUtils.isEmpty(num) || !saveLevel.equals(level)) {
			long addNum = 0L;
			if (saveLevel.equals("0")) {
				addNum = goodId + RandomUtil.getLongRandom(goodId, 1000);
			} else if (saveLevel.equals("1")) {
				addNum = RandomUtil.getLongRandom(5, 10);
			} else if (saveLevel.equals("2")) {
				addNum = RandomUtil.getLongRandom(15, 30);
			} else if (saveLevel.equals("3")) {
				addNum = RandomUtil.getLongRandom(50, 150);
			}
			num = addNum + "";
			redisClusterClient.set(key, num);
			redisClusterClient.set(levelKey, saveLevel);

		}
		return Long.valueOf(num);

	}

	public String getUserSid(HttpServletRequest request, String biz, String clientType) {
		String sid = "";
		if (StringUtils.isNotBlank(biz) && Integer.parseInt(biz) == BizSystem.UBOX.getIndex()) {
			sid = CookieUtils.getCookieValue(request, MemberConstants.UBOX_LOGIN_COOKIE_SID);
		} else {
			if (StringUtils.isNotBlank(clientType) && ClientType.getH5ClientTypeIndexList().contains(Integer.parseInt(clientType))) {
				sid = CookieUtils.getCookieValue(request, MemberConstants.H5_LOGIN_COOKIE_SID);
			} else if (StringUtils.isNotBlank(clientType) && (Integer.parseInt(clientType) == ClientType.WXGZH.getIndex() || Integer.parseInt(clientType) == ClientType.WEB.getIndex())) {
				sid = CookieUtils.getCookieValue(request, MemberConstants.WX_ACCOUNT_LOGIN_COOKIE_SID);
			} else if (StringUtils.isNotBlank(clientType) && Integer.parseInt(clientType) == ClientType.WXXCY.getIndex()) {
				sid = request.getParameter("sid");
			} else {
				sid = request.getParameter("sid");
			}
		}
		return sid;
	}


}
