package com.esportzoo.esport.manager.hd;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.hd.constants.HdEnable;
import com.esportzoo.esport.hd.entity.HdGift;
import com.esportzoo.esport.hd.entity.HdInfo;
import com.esportzoo.esport.hd.gift.HdUserLogServiceClient;
import com.esportzoo.esport.hd.info.HdInfoServiceClient;

@Component
public class HdCommonManager {
	private static final Logger logger = LoggerFactory.getLogger(HdCommonManager.class);
	@Autowired
	private HdInfoServiceClient hdInfoServiceClient;
	@Autowired
	private HdUserLogServiceClient hdUserLogServiceClient;

	/** 判断活动是否有效 true:有效；false:无效 */
	public boolean judgeHdIfValid(Integer hdCode) {
		if (hdCode == null)
			return false;
		HdInfo hdInfo = hdInfoServiceClient.queryByCode(hdCode).getModel();
		if (hdInfo == null)
			return false;

		if (hdInfo.getStatus().intValue() != HdEnable.TRUE.getIndex())
			return false;

		Date currentTime = new Date();
		if (currentTime.before(hdInfo.getStartTime()) || currentTime.after(hdInfo.getEndTime()))
			return false;
		return true;
	}

	/**
	 * 获取活动基础信息 判断活动是否为空 是否有效 是否在活动有效期
	 * 
	 * @param hdCode
	 *            活动码
	 * @return
	 */
	public CommonResponse<HdInfo> getHdBaseInfo(Integer hdCode) {
		CommonResponse<HdInfo> response = new CommonResponse<>();
		try {
			if (hdCode == null) {
				return CommonResponse.withResp(ResponseConstant.RESP_PARAM_ERROR_CODE, ResponseConstant.RESP_PARAM_ERROR_MESG);
			}
			ModelResult<HdInfo> modelResult = hdInfoServiceClient.queryWithGiftByCode(hdCode);
			HdInfo hdInfo = modelResult.getModel();
			if (hdInfo == null) {
				return CommonResponse.withResp(ResponseConstant.HD_IS_NULL_CODE, ResponseConstant.HD_IS_NULL_MESG);
			}
			if (hdInfo.getStatus().intValue() != HdEnable.TRUE.getIndex()) {
				return CommonResponse.withResp(ResponseConstant.HD_NOT_ENABLE_ERROR_CODE, ResponseConstant.HD_NOT_ENABLE_ERROR_MESG);
			}
			Calendar hdStartTime = Calendar.getInstance();
			hdStartTime.setTime(hdInfo.getStartTime());
			Calendar hdEndTime = Calendar.getInstance();
			hdEndTime.setTime(hdInfo.getEndTime());
			Calendar currentTime = Calendar.getInstance();
			if (currentTime.before(hdStartTime)) {
				return CommonResponse.withResp(ResponseConstant.HD_NOT_START_ERROR_CODE, ResponseConstant.HD_NOT_START_ERROR_MESG);
			}
			if (currentTime.after(hdEndTime)) {
				return CommonResponse.withResp(ResponseConstant.HD_HAS_END_ERROR_CODE, ResponseConstant.HD_HAS_END_ERROR_MESG);
			}
			return CommonResponse.withSuccessResp(hdInfo);
		} catch (Exception e) {
			logger.info("查询活动信息接口异常：{}", e.getMessage(), e);
		}
		return response;
	}

	/** 查询活动礼品 */
	public List<HdGift> queryHdGift(Integer hdCode) {
		return hdInfoServiceClient.queryWithGiftByCode(hdCode).getModel().getGiftList();
	}

	/** 根据活动code,查询活动信息 */
	public HdInfo queryHdInfo(Integer hdCode) {
		return hdInfoServiceClient.queryByCode(hdCode).getModel();
	}
}
