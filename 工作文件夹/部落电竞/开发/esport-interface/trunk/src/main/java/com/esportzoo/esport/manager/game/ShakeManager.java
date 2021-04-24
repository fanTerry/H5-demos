package com.esportzoo.esport.manager.game;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.annotation.EsportMethodLog;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.client.service.game.GameOrderServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.game.ShakePlayRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.game.ShakeGameOrderLogResponse;
import com.esportzoo.esport.connect.response.game.ShakeRoomDataResponse;
import com.esportzoo.esport.constants.CommonStatus;
import com.esportzoo.esport.constants.EsportPayway;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.game.GameOrderStatus;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserWalletRec;
import com.esportzoo.esport.domain.game.GameOrder;
import com.esportzoo.esport.domain.game.GamePlayItem;
import com.esportzoo.esport.manager.UserWalletManager;
import com.esportzoo.esport.service.exception.errorcode.WalletErrorTable;
import com.esportzoo.esport.service.game.GamePlayItemService;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.game.*;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 欢乐摇一摇
 *
 * @author: Haitao.Li
 *
 * @create: 2020-04-04 14:54
 **/
@Component
public class ShakeManager {
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String logPrefix = "欢乐摇一摇Mangager-";

	@Autowired
	GameOrderServiceClient gameOrderServiceClient;
	@Autowired
	GamePlayItemService gamePlayItemService;
	@Autowired
	UserWalletManager userWalletManager;
	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;

	@EsportMethodLog(paramLog = false)
	public CommonResponse<JSONObject> getUserWalletLog(UserConsumer userConsumer, int pageNo, int pageSize) {

		JSONObject jsonObject = new JSONObject();

		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}

			if (pageNo>50){
				return CommonResponse.withErrorResp("只能查看前500条记录");
			}
			DataPage<GameOrder> queryPage = new DataPage<>();
			queryPage.setPageNo(pageNo);
			queryPage.setPageSize(pageSize);
			GameOrderQueryVo queryVo = new GameOrderQueryVo();
			queryVo.setUserId(userConsumer.getId());
			queryVo.setNeedAttachAwardLevel(true);
			PageResult<GameOrder> pageResult = gameOrderServiceClient.queryPage(queryVo, queryPage);
			if (pageResult != null && pageResult.isSuccess()) {
				DataPage<GameOrder> dataPage = pageResult.getPage();
				List<GameOrder> dataList = dataPage.getDataList();
				List<ShakeGameOrderLogResponse> tempList = Lists.newArrayList();
				if (dataList.size() > 0) {
					for (GameOrder gameOrder : dataList) {
						ShakeGameOrderLogResponse orderLogResponse = new ShakeGameOrderLogResponse();
						orderLogResponse.setScore(gameOrder.getScore());
						orderLogResponse.setReturnScore(gameOrder.getReturnScore());
						orderLogResponse.setGameName(gameOrder.getGameName());
						BeanUtil.copyProperties(gameOrder, orderLogResponse);
						orderLogResponse.setSortDate(DateUtil.getDateStringByZdGs(orderLogResponse.getCreateTime(), "yyyy-MM"));
						if (gameOrder.getGameAwardLevel() != null ) {
							List<GameAwardLevelVo> gameAwardLevelVoList = gameOrder.getGameAwardLevel().getAwardLevelVos();
							orderLogResponse.setAwardLevelVoList(gameAwardLevelVoList);
						}
						//订单失败，钱包没做扣减，设置为0
						if (gameOrder.getStatus()== GameOrderStatus.ORDER_FAILED.getIndex()){
							orderLogResponse.setScore(new BigDecimal("0"));
						}
						tempList.add(orderLogResponse);
					}
				}
				/** 返回按日期切分的数据 */
				Map<String, List<ShakeGameOrderLogResponse>> listMap = tempList.stream()
						.collect(Collectors.groupingBy(ShakeGameOrderLogResponse::getSortDate));
				TreeMap<String, List<ShakeGameOrderLogResponse>> sortMap = MapUtil.sort(listMap);
				NavigableMap<String, List<ShakeGameOrderLogResponse>> dataMap = sortMap.descendingMap();
				jsonObject.put("hasNext", dataPage.isHasNext());
				jsonObject.put("listMap", dataMap);
				return CommonResponse.withSuccessResp(jsonObject);
			}

			logger.error(logPrefix + "用户：【{}】，分页查询异常信息 【{}】", userConsumer.getId(), pageResult.getErrorMsg());
		} catch (Exception e) {
			logger.error(logPrefix + "用户：【{}】，分页查询异常信息 【{}】", userConsumer.getId(), e.getMessage());
			e.printStackTrace();
		}
		return CommonResponse.withErrorResp("查询数据异常");

	}

	@EsportMethodLog
	public List<GamePlayItem> getPlayItemsByCondition(String roomNo) {
		GamePlayItemQueryVo vo = new GamePlayItemQueryVo();
		vo.setRoomNo(roomNo);
		vo.setStatus(CommonStatus.EFFECTIVE.getIndex());
		ModelResult<List<GamePlayItem>> modelResult = gamePlayItemService.queryListByCondition(vo);
		if (modelResult != null && modelResult.isSuccess()) {
			return modelResult.getModel();
		} else {
			return new ArrayList<GamePlayItem>();
		}
	}

	@EsportMethodLog
	public CommonResponse<ShakeRoomDataResponse> getRoomData(Long userId, String roomNo, BaseRequest baseRequest) {
		try {
			List<GamePlayItem> playItems = getPlayItemsByCondition(roomNo);
			if (playItems.isEmpty()) {
				return CommonResponse.withErrorResp("摇晃力量列表为空");
			}
			//排序 按照档位金额降序
			playItems = playItems.stream().sorted(Comparator.comparing(GamePlayItem::getItemScore).reversed()).collect(Collectors.toList());

			ShakeRoomDataResponse shakeRoomDataResponse = new ShakeRoomDataResponse();
			shakeRoomDataResponse.setPlayItems(playItems);
			//判断用户默认摇晃力量
			UserWalletRec wallet = userWalletManager.getUserWalletRec(userId);
			GamePlayItem defaultGamePlayItem = getDefaultPlayItem(playItems, wallet.getAbleRecScore(), baseRequest);
			shakeRoomDataResponse.setDefaultPlayItem(defaultGamePlayItem);
			return CommonResponse.withSuccessResp(shakeRoomDataResponse);
		} catch (Exception e) {
			return CommonResponse.withErrorResp("获取房间数据异常");
		}

	}

	/**计算用户默认力量等级*/
	private GamePlayItem getDefaultPlayItem(List<GamePlayItem> playItemList, BigDecimal balance, BaseRequest baseRequest) {
		playItemList = playItemList.stream().sorted(Comparator.comparing(GamePlayItem::getItemScore)).collect(Collectors.toList());
		GamePlayItem res = playItemList.get(0);
		try {
			// 生成用户余额区间
			String balanceStr = sysConfigPropertyServiceClient
					.getValueByCondition(SysConfigPropertyKey.GAME_SHAKE_PLAY_NUM_LIST_AREA, baseRequest.getClientType(), baseRequest.getAgentId());
			if (StringUtils.isBlank(balanceStr)) {
				balanceStr = "0,10000,50000,500000";
			}
			List<Integer> balanceList = Arrays.asList(balanceStr.split(",")).stream().map(s -> (Integer.parseInt(s))).collect(Collectors.toList());
			List<List<Integer>> balanceAreaList = new ArrayList<>();
			for (int i = 0; i < balanceList.size(); i++) {
				List<Integer> temp = new ArrayList<>();
				if (i <= balanceList.size() - 2) {
					temp.add(balanceList.get(i));
					temp.add(balanceList.get(i + 1));
					balanceAreaList.add(temp);
				}
			}
			// 余额对应的区间,取出投注列表
			for (int i = 0; i < balanceAreaList.size(); i++) {
				int min = balanceAreaList.get(i).get(0);
				int max = balanceAreaList.get(i).get(1);
				if (balance.intValue() > min && balance.intValue() <= max) {
					res = playItemList.get(i);
					break;
				} else if (balance.intValue() > balanceAreaList.get(balanceAreaList.size() - 1).get(1)) {
					res = playItemList.get(playItemList.size() - 1);
					break;
				}
			}
			return res;
		} catch (Exception e) {
			logger.error("{}计算用户默认等级异常，参数：{}", logPrefix, e);
			return res;
		}
	}


	public CommonResponse<ShakePlayResponse> startPlay(HttpServletRequest request, ShakePlayRequest shakePlayRequest, UserConsumer userConsumer) {//渠道改为用户注册时渠道
		//组装参数
		SubmitGameOrderParam orderParam = new SubmitGameOrderParam();

		orderParam.setUserId(userConsumer.getId());
		orderParam.setUserAccount(userConsumer.getAccount());

		orderParam.setRoomNo(shakePlayRequest.getRoomNo());
		orderParam.setPlayItemId(shakePlayRequest.getGamePlayItemId());
		orderParam.setAutoPlay(shakePlayRequest.getAutoPlay());

		String clientIp = RequestUtil.getClientIp(request);
		orderParam.setOperIp(clientIp);
		orderParam.setBizSystem(shakePlayRequest.getBiz());
		orderParam.setClientType(shakePlayRequest.getClientType());
		orderParam.setChannelNo(userConsumer.getChannelNo().intValue());
		orderParam.setPayType(EsportPayway.REC_PAY.getIndex());

		try {
			ModelResult<ShakePlayResponse> gameOrderModelResult = gameOrderServiceClient.saveShakeOrder(orderParam);
			if (gameOrderModelResult.isSuccess()) {
				return CommonResponse.withSuccessResp(gameOrderModelResult.getModel());
			} else {
				ShakePlayResponse response = new ShakePlayResponse();
				if (WalletErrorTable.REC_NOT_SUFFICIENT_FUNDS.code.equals(gameOrderModelResult.getErrorCode())) {
					response.setBalanceEnough(Boolean.FALSE);
					return CommonResponse.withSuccessResp(response);
				} else {
					return CommonResponse.withErrorResp(gameOrderModelResult.getErrorMsg());
				}
			}
		} catch (Exception e) {
			logger.error("{}确定摇一摇,调用接口异常，参数：{}", logPrefix, JSON.toJSONString(orderParam), e);
			return CommonResponse.withErrorResp("PLAY GAME ERROR");
		}
	}

}
