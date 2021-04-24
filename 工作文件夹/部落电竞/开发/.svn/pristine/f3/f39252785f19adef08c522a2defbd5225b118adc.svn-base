package com.esportzoo.esport.manager.hd;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.connect.response.hd.HdSignResponse;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.hd.constants.HdSignFlagBit;
import com.esportzoo.esport.hd.constants.HdSignStatus;
import com.esportzoo.esport.hd.entity.HdGift;
import com.esportzoo.esport.hd.entity.HdInfo;
import com.esportzoo.esport.hd.entity.HdSign;
import com.esportzoo.esport.hd.sign.HdSignServiceClient;
import com.esportzoo.esport.manager.UserWalletManager;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class HdSignManager {
	private static final Logger logger = LoggerFactory.getLogger(HdSignManager.class);

	public static final String logPrefix = "签到活动[102]相关接口-";
	@Autowired
	@Qualifier("hdSignServiceClient")
	HdSignServiceClient hdSignServiceClient;

	@Autowired
	UserWalletManager userWalletManager;
	public void queryStatusParam(JSONObject jsonObject, ModelResult<List<HdGift>> result,
			ModelResult<HdSign> hdSignModelResult, UserConsumer userConsumer) {
		List<HdSignResponse> resp = new ArrayList<HdSignResponse>();
		List<HdGift> hdGift = result.getModel();
		for (HdGift hdGift2 : hdGift) {
			HdSignResponse hsr = new HdSignResponse();
			if (null == hdSignModelResult || !hdSignModelResult.isSuccess() || null == hdSignModelResult.getModel()) {
				hsr.setHdGiftId(hdGift2.getId());
				hsr.setHdGiftName(hdGift2.getGiftName());
				hsr.setSignFlagBit(JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit"));
				if (JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit").intValue() == 7) {
					hsr.setSevenFlag("随机");
				} else {
					hsr.setHdGiftCount(JSONObject.parseObject(hdGift2.getGiftProp()).getBigDecimal("count"));
				}
				if (JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit").intValue() == 1) {
					hsr.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
				} else {
					hsr.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
				}
			} else {
				HdSign hdSignInfo = hdSignModelResult.getModel();
				String signFlagBit = hdSignInfo.getSignFlagBit();
				Date lastSignTime = hdSignInfo.getLatestSignTime();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				LocalDateTime second = LocalDateTime.parse(DateFormatUtils.format(lastSignTime, "yyyy-MM-dd HH:mm:ss"),
						formatter);
				LocalDateTime first = LocalDateTime.parse(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"),
						formatter);
				boolean signStatus = isNow(lastSignTime);
				if (!checkWeekBetween(first, second) && !signStatus) {
					for (HdGift hdGift3 : hdGift) {
						HdSignResponse hsresp = new HdSignResponse();
						hsresp.setHdGiftId(hdGift3.getId());
						hsresp.setHdGiftName(hdGift3.getGiftName());
						hsresp.setSignFlagBit(JSONObject.parseObject(hdGift3.getGiftProp()).getInteger("signFlagBit"));
						if (JSONObject.parseObject(hdGift3.getGiftProp()).getInteger("signFlagBit").intValue() == 7) {
							hsresp.setSevenFlag("随机");
						} else {
							hsresp.setHdGiftCount(JSONObject.parseObject(hdGift3.getGiftProp()).getBigDecimal("count"));
						}
						if (JSONObject.parseObject(hdGift3.getGiftProp()).getInteger("signFlagBit").intValue() == 1) {
							hsresp.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
						} else {
							hsresp.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
						}
						resp.add(hsresp);
					}
					if (!hdSignInfo.getSignFlagBit().equals(HdSignFlagBit.INITIALIZE.getDescription())) {
						hdSignServiceClient.updateSignFlagBitByUserId(HdSignFlagBit.INITIALIZE.getDescription(),
								userConsumer.getId().intValue());
					}
					break;
				} else if (!checkWeekBetween(first, second) && signStatus) {
					for (HdGift hdGift3 : hdGift) {
						HdSignResponse hsresp = new HdSignResponse();
						hsresp.setHdGiftId(hdGift3.getId());
						hsresp.setHdGiftName(hdGift3.getGiftName());
						hsresp.setSignFlagBit(JSONObject.parseObject(hdGift3.getGiftProp()).getInteger("signFlagBit"));
						if (JSONObject.parseObject(hdGift3.getGiftProp()).getInteger("signFlagBit").intValue() == 7) {
							hsresp.setSevenFlag("随机");
						} else {
							hsresp.setHdGiftCount(JSONObject.parseObject(hdGift3.getGiftProp()).getBigDecimal("count"));
						}
						if (JSONObject.parseObject(hdGift3.getGiftProp()).getInteger("signFlagBit").intValue() == 1) {
							hsresp.setReceiveStatus(HdSignStatus.HAVE_RECEIVED.getIndex());
						} else {
							hsresp.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
						}
						resp.add(hsresp);
					}
					break;
				}
				hsr.setHdGiftId(hdGift2.getId());
				hsr.setHdGiftName(hdGift2.getGiftName());
				hsr.setSignFlagBit(JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit"));
				if (JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit").intValue() == 7) {
					hsr.setSevenFlag("随机");
				} else {
					hsr.setHdGiftCount(JSONObject.parseObject(hdGift2.getGiftProp()).getBigDecimal("count"));
				}
				switch (signFlagBit) {
				case "1":
					Integer one = JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit");
					if (one.intValue() <= 1) {
						hsr.setReceiveStatus(HdSignStatus.HAVE_RECEIVED.getIndex());
					} else if (one.intValue() == 2 && !signStatus) {
						hsr.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
					} else {
						hsr.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
					}
					break;
				case "2":
					Integer two = JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit");
					if (two.intValue() <= 2) {
						hsr.setReceiveStatus(HdSignStatus.HAVE_RECEIVED.getIndex());
					} else if (two.intValue() == 3 && !signStatus) {
						hsr.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
					} else {
						hsr.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
					}
					break;
				case "3":
					Integer three = JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit");
					if (three.intValue() <= 3) {
						hsr.setReceiveStatus(HdSignStatus.HAVE_RECEIVED.getIndex());
					} else if (three.intValue() == 4 && !signStatus) {
						hsr.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
					} else {
						hsr.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
					}
					break;
				case "4":
					Integer four = JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit");
					if (four.intValue() <= 4) {
						hsr.setReceiveStatus(HdSignStatus.HAVE_RECEIVED.getIndex());
					} else if (four.intValue() == 5 && !signStatus) {
						hsr.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
					} else {
						hsr.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
					}
					break;
				case "5":
					Integer five = JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit");
					if (five.intValue() <= 5) {
						hsr.setReceiveStatus(HdSignStatus.HAVE_RECEIVED.getIndex());
					} else if (five.intValue() == 6 && !signStatus) {
						hsr.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
					} else {
						hsr.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
					}
					break;
				case "6":
					Integer six = JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit");
					if (six.intValue() <= 6) {
						hsr.setReceiveStatus(HdSignStatus.HAVE_RECEIVED.getIndex());
					} else if (six.intValue() == 7 && !signStatus) {
						hsr.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
					} else {
						hsr.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
					}
					break;
				case "7":
					Integer seven = JSONObject.parseObject(hdGift2.getGiftProp()).getInteger("signFlagBit");
					if (seven.intValue() == 1 && !signStatus) {
						hsr.setReceiveStatus(HdSignStatus.UNCLAIMED.getIndex());
					} else {
						hsr.setReceiveStatus(HdSignStatus.UNABLE_TO_RECEIVE.getIndex());
					}
					break;
				default:
					break;
				}
			}
			resp.add(hsr);
		}
		jsonObject.put("signGift", resp);
	}

	public void saveSignInfo(UserConsumer userConsumer, ModelResult<List<HdGift>> result, JSONObject jsonObject,
			HdInfo hdInfo) {
		ModelResult<Integer> giftCount = hdSignServiceClient.saveSignInfo(userConsumer, result, hdInfo);
		if (giftCount.isSuccess() && null != giftCount.getModel()) {
			logger.info(logPrefix + "签到成功,用户id={}", userConsumer.getId());
		}
		jsonObject.put("giftCount", giftCount.getModel());
		jsonObject.put("giftRecScore", userWalletManager.getUserWalletRec(userConsumer.getId()).getAbleRecScore());
		jsonObject.put("result", "签到成功");
	}

	public void updateSignInfo(JSONObject jsonObject, UserConsumer userConsumer, ModelResult<List<HdGift>> result,
			HdSign hdSignInfo, HdInfo hdInfo) {
		ModelResult<Integer> giftCount = hdSignServiceClient.updateSignInfo(userConsumer, result, hdSignInfo, hdInfo);
		if (giftCount.isSuccess() && null != giftCount.getModel()) {
			logger.info(logPrefix + "签到成功,用户id={}", userConsumer.getId());
		}
		jsonObject.put("giftCount", giftCount.getModel());
		jsonObject.put("giftRecScore", userWalletManager.getUserWalletRec(userConsumer.getId()).getAbleRecScore());
		jsonObject.put("result", "签到成功");
	}

	/**
	 * 判断时间是不是今天
	 * 
	 * @param date
	 * @return 是返回true，不是返回false
	 */
	public static boolean isNow(Date date) {
		// 当前时间
		Date now = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		// 获取今天的日期
		String nowDay = sf.format(now);
		// 对比的时间
		String day = sf.format(date);
		return day.equals(nowDay);
	}

	/**
	 * 以second为主进行判断 以first为开始时间，second为结束时间 判断两个时间是否在通过一个周内。
	 *
	 * @param first
	 * @param second
	 */

	private static Boolean checkWeekBetween(LocalDateTime first, LocalDateTime second) {
		Period period = Period.between(first.toLocalDate(), second.toLocalDate());
		int years = period.getYears();
		if (years > 0) {
			return false;
		}
		int months = period.getMonths();
		if (months > 0) {
			return false;
		}
		int days = period.getDays();
		if (days == 0) {
			return true;
		}
		if (days > 7 || days < -7) {
			return false;
		}
		int firstDayOfWeek = first.getDayOfWeek().getValue();
		int secondDayOfWeek = second.getDayOfWeek().getValue();
		if (secondDayOfWeek == 1) {
			if (oneDay(firstDayOfWeek, secondDayOfWeek, days)) {
				return true;
			} else {
				return false;
			}
		}
		if (secondDayOfWeek == 7) {
			if (sevenDay(firstDayOfWeek, secondDayOfWeek, days)) {
				return true;
			} else {
				return false;
			}
		}
		if (otherDay(firstDayOfWeek, secondDayOfWeek, days)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * secondDayOfWeek 是所在星期的第一天 星期的第一天 数据处理
	 * 
	 * @return
	 */
	private static Boolean oneDay(int firstDayOfWeek, int secondDayOfWeek, int days) {
		if (days > 0) {
			return false;
		} else {
			if (secondDayOfWeek - days == firstDayOfWeek) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 星期的第7天的时候处理数据
	 * 
	 * @return
	 */
	private static Boolean sevenDay(int firstDayOfWeek, int secondDayOfWeek, int days) {
		if (firstDayOfWeek + days == secondDayOfWeek) {
			return true;
		}
		return false;
	}

	/**
	 * 其他天的数据处理
	 * 
	 * @return
	 */
	private static Boolean otherDay(int firstDayOfWeek, int secondDayOfWeek, int days) {
		if (days < 0) {
			if ((secondDayOfWeek - days) == firstDayOfWeek) {
				return true;
			}
		} else {
			if (firstDayOfWeek + days == secondDayOfWeek) {
				return true;
			}
		}
		return false;
	}



}
