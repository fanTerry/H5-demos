package com.esportzoo.esport.controller.hd;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.connect.request.hd.Hd104Request;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.hd.Hd104Response;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.hd.constants.HdCode;
import com.esportzoo.esport.hd.constants.HdEnable;
import com.esportzoo.esport.hd.constants.HdGiftType;
import com.esportzoo.esport.hd.entity.HdInfo;
import com.esportzoo.esport.hd.entity.HdUserGift;
import com.esportzoo.esport.hd.entity.HdUserShare;
import com.esportzoo.esport.hd.gift.HdUserGiftServiceClient;
import com.esportzoo.esport.hd.info.HdInfoServiceClient;
import com.esportzoo.esport.hd.share.HdUserShareServiceClient;
import com.esportzoo.esport.hd.vo.HdUserGiftQueryVo;
import com.esportzoo.esport.hd.vo.HdUserShareVo;
import com.esportzoo.esport.manager.hd.Hd104Manager;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.service.exception.errorcode.GameErrorTable;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @description: 活动相关
 *
 * @author: Haitao.Li
 *
 * @create: 2020-03-18 15:44
 **/
@Controller
@RequestMapping("share")
public class Hd104Controller extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String logPrefix = "104分享活动活动相关接口-";

	@Autowired
	Hd104Manager hd104Manager;
	@Autowired
	private HdInfoServiceClient hdInfoServiceClient;
	@Autowired
	private HdUserShareServiceClient hdUserShareServiceClient;

	@Autowired
	private HdUserGiftServiceClient hdUserGiftServiceClient;

	@Autowired
	RedisClient redisClient;

	public static final String SHARECODE_KEY = "esport_sharecode_key_";


	@Autowired
	@Qualifier("taskExecutor")
	protected ThreadPoolTaskExecutor taskExecutor;

	@RequestMapping(value = "/queryShareCode", method = RequestMethod.POST)
	@ApiOperation(value = "获取104活动分享码", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取104活动分享码", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse queryShareCode(HttpServletRequest request, Hd104Request hdRequest) {
		Hd104Response hdResponse = new Hd104Response();
		UserConsumer userConsumer = getLoginUsr(request);
		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}

			if (!redisClient.setNX(SHARECODE_KEY + userConsumer.getId(), "1", 3)) {
				logger.info("queryShareCode获取分享码,重复提交，3秒内只能提交一次,参数：{}");
				throw new BusinessException(GameErrorTable.submit_duplicate.code, GameErrorTable.submit_duplicate.msg);
				//return CommonResponse.withErrorResp("重复提交");
			}

			//校验活动是否有效
			ModelResult<HdInfo> modelResult = hdInfoServiceClient.queryByCode(HdCode.HD_104.getIndex());
			if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
				HdInfo hdInfo = modelResult.getModel();
				Date joinDate = new Date();
				if (hdInfo.getStatus() == HdEnable.TRUE.getIndex() && (hdInfo.getStartTime().compareTo(joinDate) <= 0
						&& hdInfo.getEndTime().compareTo(joinDate) > 0)) {

					//当用户是携带邀请码进入的，记录分享链接点击流水
					hdRequest.setHdId(hdInfo.getId());
					//查看用户是否为当前邀请码过来注册的用户
					//String shareCode = userConsumer.getFeature("shareCode");
					//if (StringUtils.isNotEmpty(hdRequest.getShareCode()) && StringUtils.isNotEmpty(shareCode) && shareCode
					//		.equals(hdRequest.getShareCode())) {
					hd104Manager.addClickShareLinkLog(hdRequest, userConsumer);
					//}
					//产生用户自己唯一的活动分享码
					String userShareCode = hd104Manager.getShareCode(userConsumer, hdRequest);
					if (StringUtils.isNotEmpty(userShareCode)) {
						hdResponse.setShareCode(userShareCode);
						logger.info(logPrefix + "用户：【{}】，活动104分享码 【{}】", userConsumer.getId(), userShareCode);
					}
				}

			}

			return CommonResponse.withSuccessResp(hdResponse);

		} catch (Exception e) {
			logger.error("用户查询分享码发生异常，用户id:{},异常信息:{}", userConsumer.getId(), e.getMessage());
			return CommonResponse.withErrorResp("获取分享码失败");
		}
	}


	@RequestMapping(value = "/getSumByHdSend", method = RequestMethod.POST)
	@ApiOperation(value = "获取104活动累积获赠的星星数", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取104活动累积获赠的星星数", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse getSumByHdSend(HttpServletRequest request, Hd104Request hdRequest) {

		Hd104Response hdResponse = new Hd104Response();
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		try {

			hdResponse.setSumStar(0L);
			ModelResult<HdInfo> modelResult = hdInfoServiceClient.queryByCode(HdCode.HD_104.getIndex());
			HdInfo hdInfo = modelResult.getModel();
			HdUserShareVo vo = new HdUserShareVo();
			vo.setHdId(hdInfo.getId());
			vo.setUserId(userConsumer.getId().intValue());
			ModelResult<List<HdUserShare>> queryByCondition = hdUserShareServiceClient.queryByCondition(vo, null);
			if (queryByCondition != null && queryByCondition.isSuccess() && queryByCondition.getModel() != null) {
				List<HdUserShare> hdUserShareList = queryByCondition.getModel();
				if (hdUserShareList.size() == 1) {
					HdUserShare hdUserShare = hdUserShareList.get(0);
					BigDecimal sumStar = hdUserShare.getFeature("sumStar", BigDecimal.class);
					if (sumStar != null) {
						hdResponse.setSumStar(sumStar.longValue());
					}
				} else {
					logger.warn(logPrefix + "用户：【{}】，单个用户查询到多条分享二维码记录 ，【{}】", userConsumer.getId(), JSON.toJSONString(hdUserShareList));
				}
			}
		} catch (Exception e) {
			logger.error(logPrefix + "用户：【{}】，查询用户分享活动累积的星星数出现异常，异常信息:{}", userConsumer.getId(), e.getMessage());
			e.printStackTrace();
		}
		hdResponse.setNickName(userConsumer.getNickName());
		return CommonResponse.withSuccessResp(hdResponse);
	}

	@RequestMapping(value = "/geHeplUserList", method = RequestMethod.POST)
	@ApiOperation(value = "获取助力用户列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取助力用户列表", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<DataPage<Hd104Response>> geHeplUserList(HttpServletRequest request, Hd104Request hdRequest) {

		DataPage<Hd104Response> resPage = null;
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		try {

			resPage = new DataPage<>();
			resPage.setPageNo(hdRequest.getPageNo());
			resPage.setPageSize(hdRequest.getPageSize());
			//获取用户的分享码
			ModelResult<HdInfo> modelResult = hdInfoServiceClient.queryByCode(HdCode.HD_104.getIndex());
			HdInfo hdInfo = modelResult.getModel();
			hdRequest.setHdId(hdInfo.getId());
			String shareCode = hd104Manager.getShareCode(userConsumer, hdRequest);

			//分页查询助力流水
			DataPage<HdUserGift> dataPage = new DataPage<>();
			dataPage.setPageSize(hdRequest.getPageSize());
			dataPage.setPageNo(hdRequest.getPageNo());
			//dataPage.setOrderBy("create_time desc");
			HdUserGiftQueryVo queryVo = new HdUserGiftQueryVo();
			queryVo.setOuterSerialNo(shareCode);
			List<HdGiftType> giftTypes = Lists.newArrayList();
			giftTypes.add(HdGiftType.XINGXING);
			PageResult<HdUserGift> giftPageResult = hdUserGiftServiceClient.pageQueryHdUserGift(dataPage, queryVo, giftTypes);
			if (giftPageResult != null && giftPageResult.isSuccess()) {
				List<Hd104Response> hd104ResponseList = Lists.newArrayList();
				List<HdUserGift> hdUserGiftList = giftPageResult.getPage().getDataList();
				for (HdUserGift hdUserGift : hdUserGiftList) {
					Hd104Response hd104Response = new Hd104Response();
					Integer userIdHelp = hdUserGift.getFeature("userIdHelp", Integer.class);
					ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient
							.queryConsumerById(userIdHelp.longValue(), new UserConsumerQueryOption());
					UserConsumer helpUser = userConsumerModelResult.getModel();
					if (helpUser == null) {
						logger.error(logPrefix + "查询助力活动用户列表，助力用户：【{}】不存在", userIdHelp);
						continue;
					}

					hd104Response.setAcceptStar(100L);
					hd104Response.setHelpStar(hdUserGift.getAmount().longValue());
					hd104Response.setIcon(helpUser.getIcon());
					hd104Response.setNickName(helpUser.getNickName());
					hd104ResponseList.add(hd104Response);

				}

				resPage.setDataList(hd104ResponseList);
			}
			BeanUtil.copyProperties(giftPageResult.getPage(), resPage, "dataList");
		} catch (Exception e) {
			logger.error(logPrefix + "获取用户助力列表出现异常，用户ID=【{}】，请求参数param =【{}】", userConsumer.getId(), JSON.toJSONString(hdRequest));
			e.printStackTrace();
			return CommonResponse.withErrorResp("数据异常,请稍后重试");
		}

		return CommonResponse.withSuccessResp(resPage);
	}


	@RequestMapping(value = "/downloadFile")
	public String downloads(HttpServletResponse response) throws Exception {
		String path = "/mfs/ShareFile/res/esport/upload/";
		String fileName = "orange.mobileconfig";
		//String fileName = "Orange.mobileconfig";
		//1、设置response 响应头
		response.reset();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));

		File file = new File(path, fileName);
		//2、 读取文件--输入流
		InputStream input = new FileInputStream(file);
		//3、 写出文件--输出流
		OutputStream out = response.getOutputStream();
		byte[] buff = new byte[1024];
		int index = 0;
		//4、执行 写出操作
		while ((index = input.read(buff)) != -1) {
			out.write(buff, 0, index);
			out.flush();
		}
		out.close();
		input.close();
		return null;
	}


}
