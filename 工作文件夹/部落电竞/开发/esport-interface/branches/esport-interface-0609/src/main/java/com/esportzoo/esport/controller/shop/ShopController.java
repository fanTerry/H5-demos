package com.esportzoo.esport.controller.shop;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.util.MathUtil;
import com.esportzoo.esport.client.service.common.ClientAdPicServiceClient;
import com.esportzoo.esport.client.service.shop.ShopGoodsCategoryServiceClient;
import com.esportzoo.esport.client.service.shop.ShopGoodsServiceClient;
import com.esportzoo.esport.client.service.shop.ShopOrderDetailServiceClient;
import com.esportzoo.esport.client.service.shop.ShopOrderServiceClient;
import com.esportzoo.esport.connect.request.ShopRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.goods.GoodsDetailResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.AdPicPlaceType;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.shop.ShopGoodsLabel;
import com.esportzoo.esport.constants.shop.ShopGoodsStatus;
import com.esportzoo.esport.constants.shop.ShopOrderStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.*;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.manager.CommonManager;
import com.esportzoo.esport.manager.ShopManager;
import com.esportzoo.esport.vo.shop.ShopGoodsInfo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 商城接口
 *
 * @author: Haitao.Li
 *
 * @create: 2019-07-25 14:31
 **/

@Controller
@RequestMapping("shop")
@Api(value = "商城相关接口", tags = { "商城相关接口" })
public class ShopController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ShopManager shopManager;

	@Autowired
	@Qualifier("shopGoodsCategoryServiceClient")
	private ShopGoodsCategoryServiceClient shopGoodsCategoryServiceClient;

	@Autowired
	@Qualifier("clientAdPicServiceClient")
	private ClientAdPicServiceClient clientAdPicServiceClient;

	@Autowired
	private CommonManager commonManager;

	@Autowired
	@Qualifier("shopGoodsServiceClient")
	private ShopGoodsServiceClient shopGoodsServiceClient;

	@Autowired
	@Qualifier("shopOrderDetailServiceClient")
	ShopOrderDetailServiceClient shopOrderDetailServiceClient;

	@Autowired
	@Qualifier("shopOrderServiceClient")
	ShopOrderServiceClient shopOrderServiceClient;

	@Value("${shop.image.host}")
	private String shopImageHost;

	@Autowired
	CachedManager cachedManager;

	public static final String logPrefix = "商城相关接口：";



	@RequestMapping(value = "/indexData", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "首页商品目录和广告数据接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "首页商品目录和广告数据接口", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> index(HttpServletRequest request, ShopRequest shopRequest) {
		JSONObject jsonObject = new JSONObject();


		if(shopRequest.getClientType()== ClientType.WXXCY.getIndex()){
			/** 小程序商城开关 */
			SysConfigProperty sysConfigPropertyByKey = getSysConfigByKey(SysConfigPropertyKey.WECHAT_MINI_SHOP_SWITCH,shopRequest.getClientType(),0L);
			if (sysConfigPropertyByKey == null || StringUtils.isEmpty(sysConfigPropertyByKey.getValue())) {
				logger.info(logPrefix + "请配置小程序商城开关,请求参数param 【{}】", JSON.toJSONString(shopRequest));
				return CommonResponse.withErrorResp("请配置小程序商城开关");
			}
			String miniSwitch = sysConfigPropertyByKey.getValue();
			if ("0".equals(miniSwitch)) {
				jsonObject.put("miniSwitch", Convert.toInt(miniSwitch));
				return CommonResponse.withSuccessResp(jsonObject);
			}
			jsonObject.put("miniSwitch", Convert.toInt(miniSwitch));
		}

		if (shopRequest.getClientType()==null){
			return CommonResponse.withResp(ResponseConstant.RESP_ERROR_CODE,ResponseConstant.PARAM_NOT_NULL);
		}
		try {
			ModelResult<List<ShopGoodsCategory>> modelResult = shopGoodsCategoryServiceClient.queryTopCategorys();
			if (modelResult!=null && modelResult.isSuccess() && modelResult.getModel()!=null) {
				List<ShopGoodsCategory> categories = modelResult.getModel();
				List<ShopGoodsCategory> categoryList = categories.stream().sorted(Comparator.comparing(ShopGoodsCategory::getSort)).collect(Collectors.toList());
				Iterator<ShopGoodsCategory> iterator = categoryList.iterator();
				while (iterator.hasNext()) {
					ShopGoodsCategory item = iterator.next();
					if (item.getId().equals(0L)) {
						iterator.remove();
						continue;
					}else {
						if (StringUtils.isNotEmpty(item.getImage())){
							List<String> goodsImageList = shopManager.getGoodsImageList(item.getImage());
							item.setImage(goodsImageList.size()>0?goodsImageList.get(0):"");
						}
					}

				}
				jsonObject.put("categoryList",categoryList);
			}

			List<ClientAdPic> adPic = commonManager.getClientAdPic(AdPicPlaceType.SHOP_BANNER.getIndex(), shopRequest.getClientType());
			jsonObject.put("adList", adPic);
		} catch (Exception e) {
			logger.error("查询商城首页目录和广告数据出错,{}",e.getMessage());
			e.printStackTrace();
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE,ResponseConstant.SYSTEM_ERROR_MESG);
		}

		return CommonResponse.withSuccessResp(jsonObject);

	}

	@RequestMapping(value = "/getGooods", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "获取商品列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "获取商品列表", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> getGooods(HttpServletRequest request, ShopRequest shopRequest) {
		JSONObject jsonObject = new JSONObject();
		try {
			DataPage<ShopGoods> dataPage = new DataPage<>();
			dataPage.setPageNo(shopRequest.getPageNo());
			dataPage.setPageSize(shopRequest.getPageSize());

			ShopGoods  shopGoodsQueryVo = new ShopGoods();
			BeanUtil.copyProperties(shopRequest,shopGoodsQueryVo);

			/*商品分类*/
			if (shopRequest.getContentType()!=null && shopRequest.getContentType()>0){
				shopGoodsQueryVo.setCategoryId(shopRequest.getContentType().longValue());
			}

			/*榜单类型*/
			if (shopRequest.getGoodsQueryType()==null){
				shopRequest.setGoodsQueryType(0);
			}
			shopGoodsQueryVo.setStatus(ShopGoodsStatus.ABLE_EXCHANGE.getIndex());

			PageResult<ShopGoods> shopGoodsPageResult = shopGoodsServiceClient.queryListOrderBy(shopGoodsQueryVo,shopRequest.getGoodsQueryType(),dataPage);
			if (shopGoodsPageResult!=null && shopGoodsPageResult.isSuccess() ) {
				List<ShopGoods> shopGoods = shopGoodsPageResult.getPage().getDataList();
				for (ShopGoods shopGood : shopGoods) {
					if (shopGood.getLabel()!=null && shopGood.getLabel()>0){
						shopGood.setLabelName(ShopGoodsLabel.valueOf(shopGood.getLabel()).getDescription());
					}
					shopGood.setPayScore(shopGood.getPayScore());
					List<String> goodsImageList = shopManager.getGoodsImageList(shopGood.getImage());
					shopGood.setImage(goodsImageList.size()>0?goodsImageList.get(0):"");
					//添加购买人数
					shopGood.setPurchases(shopGood.getPurchases()==null?0:shopGood.getPurchases());
					//当前的购买人数+虚增的购买人数
					shopGood.setPurchases(shopGood.getPurchases()+cachedManager.getGoodPurchase(shopGood.getId(),shopGood.getPublishTime()));

				}
				if (shopRequest.getGoodsQueryType()==3){
					shopGoods = shopGoods.stream().sorted(Comparator.comparing(ShopGoods::getPurchases).reversed())
							.collect(Collectors.toList());
				}
				jsonObject.put("shopGoodList", shopGoods);

			}
		} catch (Exception e) {
			logger.error("查询商城首页商品数据出错,{}",e.getMessage());
			e.printStackTrace();
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE,ResponseConstant.SYSTEM_ERROR_MESG);
		}
		return CommonResponse.withSuccessResp(jsonObject);
	}

	@ApiOperation(value = "获取商品详情接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/detail/{goodsId}", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<GoodsDetailResponse> getGoodsDetailById(@ApiParam(required = true, name = "商品id") @PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
		try {
			/*UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据id获取shopGoods详情,未获取到登录用户信息，goodsId={}", goodsId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}*/
			/*ModelResult<ShopGoods> modelResult = shopGoodsServiceClient.queryById(goodsId);
			if (modelResult == null || !modelResult.isSuccess() || null == modelResult.getModel()) {
				logger.info("根据id获取shopGoods详情，调用接口错误modelResult==null，goodsId={}", goodsId);
				return CommonResponse.withErrorResp("查询商品详情异常");
			}*/
			ModelResult<ShopGoodsInfo> modelResult = shopGoodsServiceClient.queryGoodsInfoById(goodsId);
			if (null == modelResult || !modelResult.isSuccess() || null == modelResult.getModel()) {
				logger.info("根据id获取shopGoods详情，调用接口错误shopGoodsInfo==null，goodsId={}", goodsId);
				return CommonResponse.withErrorResp("查询商品详情异常");
			}
			if (modelResult.getModel().getStatus().intValue() != ShopGoodsStatus.ABLE_EXCHANGE.getIndex()) {
				return CommonResponse.withErrorResp("商品已下架");
			}
			GoodsDetailResponse resp = shopManager.toConvertResponse(modelResult.getModel());
			if (resp.getPurchases()==null){
				resp.setPurchases(0L);
			}
			resp.setPurchases(resp.getPurchases()+cachedManager.getGoodPurchase(resp.getId(),resp.getPublishTime()));
			return CommonResponse.withSuccessResp(resp);
		} catch (Exception e) {
			logger.info("根据id获取shopGoods详情，发生异常，goodsId={}，exception={}", goodsId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}


	@RequestMapping(value = "/shopOrder", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询兑换订单", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询兑换订单", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> shopOrder(HttpServletRequest request, ShopRequest shopRequest) {
		JSONObject jsonObject = new JSONObject();
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		if (shopRequest.getShopOrderStatus()==null){
			return CommonResponse.withResp(ResponseConstant.RESP_ERROR_CODE,ResponseConstant.PARAM_NOT_NULL);
		}
		try {

			DataPage<ShopOrderDetail> dataPage  = new DataPage<ShopOrderDetail>();
			dataPage.setPageSize(shopRequest.getPageSize());
			dataPage.setPageNo(shopRequest.getPageNo());
			List<Integer> statusList = Lists.newArrayList();

			if (shopRequest.getShopOrderStatus()==0){
				/*查询全部兑换订单*/
				statusList.add(ShopOrderStatus.WAIT_DELIVERY.getIndex());
				statusList.add(ShopOrderStatus.EXCHANGE_COMPLETED.getIndex());
				statusList.add(ShopOrderStatus.WAIT_AUDIT.getIndex());
			}else {
				statusList.add(shopRequest.getShopOrderStatus());
			}

			PageResult<ShopOrderDetail> pageResult = shopOrderServiceClient
					.queryOrderDetail(userConsumer.getId(), statusList, dataPage);
			if (pageResult!=null && pageResult.isSuccess() && pageResult.getPage()!=null && pageResult.getPage().getDataList()!=null) {
				List<ShopOrderDetailVo> list = Lists.newArrayList();
				List<ShopOrderDetail> shopOrderList = pageResult.getPage().getDataList();
				for (ShopOrderDetail shopOrderDetail : shopOrderList) {
					ShopOrderDetailVo detailVo = new ShopOrderDetailVo();
					BeanUtil.copyProperties(shopOrderDetail,detailVo);

					ModelResult<ShopOrder> orderModelResult = shopOrderServiceClient.queryById(shopOrderDetail.getOrderId());
					if (orderModelResult!=null && orderModelResult.isSuccess() && orderModelResult.getModel()!=null) {
						ShopOrder order = orderModelResult.getModel();
						ShopOrderStatus shopOrderStatus = ShopOrderStatus.valueOf(order.getStatus());
						detailVo.setOrderStausDec(shopOrderStatus.getDescription());
					}
					ModelResult<ShopGoods> modelResult = shopGoodsServiceClient.queryById(shopOrderDetail.getGoodsId());
					if (modelResult.getModel()==null|| !modelResult.isSuccess()){
						logger.warn("没有查询到对应的商品，商品ID={}",shopOrderDetail.getGoodsId());
						continue;
					}
					ShopGoods shopGoods = modelResult.getModel();
					//货币比例
					detailVo.setScoreTotal(detailVo.getScoreTotal());

					if (StringUtils.isNotEmpty( shopGoods.getImage())){
						String[] imgArray = shopGoods.getImage().split(",");
						detailVo.setGoodImg(shopImageHost+imgArray[0]);
					}

					list.add(detailVo);

				}

				jsonObject.put("shopOrderList", list);
			}
		} catch (Exception e) {
			logger.info("查询用户订单出现异常，用户id：{},异常信息:{}", userConsumer.getId(), e.getMessage(),e);
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE,ResponseConstant.SYSTEM_ERROR_MESG);

		}

		return CommonResponse.withSuccessResp(jsonObject);

	}



}
