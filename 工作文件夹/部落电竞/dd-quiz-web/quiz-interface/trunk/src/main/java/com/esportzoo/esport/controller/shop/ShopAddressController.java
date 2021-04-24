package com.esportzoo.esport.controller.shop;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.shop.ShopAddressServiceClient;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.shop.ShopAddressDefaulted;
import com.esportzoo.esport.constants.shop.ShopAddressStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.ShopAddress;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.goods.ShopAddressManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商城地址接口
 * 
 * @author wujing
 */
@Controller
@RequestMapping("shopAddress")
@Api(value = "商城地址相关接口", tags = { "商城地址相关接口" })
public class ShopAddressController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ShopAddressManager shopAddressManager;

	@Autowired
	@Qualifier("shopAddressServiceClient")
	ShopAddressServiceClient shopAddressServiceClient;

	@ApiOperation(value = "获取用户默认地址接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/default", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<ShopAddress> getGoodsDetailById(HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("根据id获取shopGoods详情,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			ShopAddress shopAddress = shopAddressManager.queryDefaultAddress(userConsumer.getId());
			return CommonResponse.withSuccessResp(shopAddress);
		} catch (Exception e) {
			logger.info("根据id获取shopGoods详情，发生异常，exception={}",  e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}




	@RequestMapping(value = "/listAddress", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询收货地址", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询收货地址", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> listAddress(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		try {
			ShopAddress shopAddress = new ShopAddress();
			shopAddress.setUserId(userConsumer.getId());
			shopAddress.setStatus((short) ShopAddressStatus.VALID.getIndex());
			ModelResult<List<ShopAddress>> modelResult = shopAddressServiceClient.queryList(shopAddress);
			if (modelResult!=null && modelResult.isSuccess()) {
				List<ShopAddress> shopAddressList = modelResult.getModel();
				List<ShopAddress> addressList = shopAddressList.stream().sorted(Comparator.comparing(ShopAddress::getDefaulted).reversed())
						.collect(Collectors.toList());
				jsonObject.put("shopAddressList",addressList);
			}
		} catch (Exception e) {
			logger.info("查询用户地址列表出现异常，用户id：{},异常信息:{}", userConsumer.getId(), e.getMessage());
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE,ResponseConstant.SYSTEM_ERROR_MESG);
		}

		return CommonResponse.withSuccessResp(jsonObject);

	}

	@RequestMapping(value = "/queryAddressById", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询地址详情", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询地址详情", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> queryAddressById(HttpServletRequest request,@ApiParam(required = true, name = "地址ID") Long addressById) {
		JSONObject jsonObject = new JSONObject();

		if (addressById==null){
			return CommonResponse.withErrorResp("地址ID为空");
		}
		try {
			ModelResult<ShopAddress> modelResult = shopAddressServiceClient.queryById(addressById);
			if (modelResult!=null && modelResult.isSuccess()) {
				ShopAddress shopAddress = modelResult.getModel();
				jsonObject.put("shopAddress",shopAddress);
			}
		} catch (Exception e) {
			logger.info("查询用户地址出现异常，地址id：{},异常信息:{}", addressById, e.getMessage());
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE,ResponseConstant.SYSTEM_ERROR_MESG);
		}

		return CommonResponse.withSuccessResp(jsonObject);

	}

	@RequestMapping(value = "/saveAddress", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "保存用户收货地址", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "保存用户收货地址", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> saveAddress(HttpServletRequest request, ShopAddress shopAddress) {
		JSONObject jsonObject = new JSONObject();
		String message = "保存失败";
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		if (shopAddress==null){
			return CommonResponse.withErrorResp("未填写地址信息，不可保存");
		}
		try {
			shopAddress.setUserId(userConsumer.getId());
			shopAddress.setAccount(userConsumer.getAccount());
			if (StringUtils.isNotEmpty(shopAddress.getReceiverProvince())){
				String receiverCity = shopAddress.getReceiverProvince();
				String[] strings = receiverCity.split(" ");
				for (int i = 0; i < strings.length; i++) {
					if (i==0){
						shopAddress.setReceiverProvince(strings[i]);
					}
					if (i==1){
						shopAddress.setReceiverCity(strings[i]);
					}
					if (i==2){
						shopAddress.setReceiverDistrict(strings[i]);
					}
				}
			}
			if (shopAddress.getId()==null){
				//保存地址
				ShopAddress queryVo = new ShopAddress();
				queryVo.setUserId(userConsumer.getId());
				queryVo.setStatus((short) ShopAddressStatus.VALID.getIndex());
				ModelResult<List<ShopAddress>> queryList = shopAddressServiceClient.queryList(queryVo);
				if (queryList!=null && queryList.isSuccess() && queryList.getModel().size()==0){
					//首次添加设置默认
					shopAddress.setDefaulted((short) ShopAddressDefaulted.DEFAULT.getIndex());
				}

				shopAddress.setStatus((short) ShopAddressStatus.VALID.getIndex());
				Calendar instance = Calendar.getInstance();
				shopAddress.setCreateTime(instance);
				shopAddress.setUpdateTime(instance);
				ModelResult<Long> modelResult = shopAddressServiceClient.save(shopAddress);
				if (modelResult!=null && modelResult.isSuccess() && modelResult.getModel()!=null) {
					if (shopAddress.getDefaulted()== ShopAddressDefaulted.DEFAULT.getIndex()){
						shopAddressServiceClient.updateDefaultAddress(modelResult.getModel());
					}

					jsonObject.put("addressId", modelResult.getModel());
					return CommonResponse.withSuccessResp(jsonObject);
				}
			}else {
				//更新地址
				ModelResult<ShopAddress> modelResult = shopAddressServiceClient.queryById(shopAddress.getId());
				if (modelResult!=null && modelResult.isSuccess() && modelResult.getModel()!=null) {
					ShopAddress address = modelResult.getModel();
					address.setReceiverName(shopAddress.getReceiverName());
					address.setDefaulted(shopAddress.getDefaulted());
					address.setReceiverPhone(shopAddress.getReceiverPhone());
					address.setReceiverAddress(shopAddress.getReceiverAddress());
					address.setReceiverCity(shopAddress.getReceiverCity());
					address.setReceiverProvince(shopAddress.getReceiverProvince());
					address.setReceiverDistrict(shopAddress.getReceiverDistrict());
					address.setUpdateTime(Calendar.getInstance());
					ModelResult<Integer> result = shopAddressServiceClient.update(address);
					if (result!=null && result.isSuccess() && result.getModel()!=null) {
						if (address.getDefaulted()== ShopAddressDefaulted.DEFAULT.getIndex()){
							shopAddressServiceClient.updateDefaultAddress(address.getId());
						}
						jsonObject.put("addressId", shopAddress.getId());
						return CommonResponse.withSuccessResp(jsonObject);
					}
				}

			}
		} catch (Exception e) {
			logger.info("保存用户地址列表出现异常，用户id：{},异常信息:{}", userConsumer.getId(), e.getMessage());
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE,ResponseConstant.SYSTEM_ERROR_MESG);
		}

		return CommonResponse.withErrorResp(message);

	}

	@RequestMapping(value = "/deleteAddress", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "删除用户收货地址", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "删除用户收货地址", response = CommonResponse.class)
	public @ResponseBody CommonResponse<JSONObject> saveAddress(HttpServletRequest request,@ApiParam(required = true, name = "地址ID") Long addresId) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer==null){
				return CommonResponse.withErrorResp("用户未登录");
			}
			if (addresId==null){
				return CommonResponse.withErrorResp("addresId为空");
			}
			ModelResult<ShopAddress> modelResult = shopAddressServiceClient.queryById(addresId);
			if (modelResult==null || !modelResult.isSuccess()) {
				return CommonResponse.withErrorResp("删除失败，收货地址不存在");
			}


			ShopAddress shopAddress = modelResult.getModel();

			if (!shopAddress.getUserId().equals(userConsumer.getId())){
				return CommonResponse.withErrorResp("收货地址不属于当前用户，不可删除");
			}

			//删除的是默认地址
			if (shopAddress.getDefaulted()==ShopAddressDefaulted.DEFAULT.getIndex()){
				ShopAddress address = new ShopAddress();
				address.setUserId(userConsumer.getId());
				address.setStatus((short) ShopAddressStatus.VALID.getIndex());
				ModelResult<List<ShopAddress>> queryList = shopAddressServiceClient.queryList(address);
				List<ShopAddress> addressList = queryList.getModel();
				//当有多个地址时，设置设置第二地址为默认地址
				if (addressList.size()>1){
					addressList = addressList.stream().sorted(Comparator.comparing(ShopAddress::getDefaulted).reversed())
							.collect(Collectors.toList());
					ShopAddress defaultAddress = addressList.get(1);
					shopAddressServiceClient.updateDefaultAddress(defaultAddress.getId());
				}

			}
			shopAddress.setStatus((short)ShopAddressStatus.INVALID.getIndex());
			ModelResult<Integer> result = shopAddressServiceClient.update(shopAddress);
			if (!result.isSuccess()){
				logger.error("删除收货地址异常，ID={},异常信息={}",addresId,result.getErrorMsg());
				return CommonResponse.withErrorResp("删除异常");
			}
		} catch (Exception e) {
			logger.error("删除收货地址异常，ID={},异常信息={}",addresId,e.getMessage());
			e.printStackTrace();
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE,ResponseConstant.SYSTEM_ERROR_MESG);
		}

		return CommonResponse.withSuccessResp("删除成功");
	}
}
