package com.esportzoo.esport.controller.open;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.open.KoiMallResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.util.open.KoiUtil;
import com.esportzoo.esport.vo.user.open.KoiH5UrlRequestVO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @Description 第三方商城兑换
* @Author zheng.lin
* @Date 2020/6/3 16:03 
*/
@Controller
@RequestMapping("/mall")
public class ThirdMallController extends BaseController {

    private transient final Logger logger = LoggerFactory.getLogger(ThirdMallController.class);

    /** 应用标识 */
    @Value("${partner.koi.mall.appKey}")
    private String appKey;
    /** 商户Id */
    @Value("${partner.koi.mall.mchId}")
    private String mchId;
    /** 密钥 */
    @Value("${partner.koi.mall.secret}")
    private String secret;
    /** 域名 */
    @Value("${partner.koi.mall.host}")
    private String host;
    /** 锦鲤通知地址 */
    @Value("${partner.koi.mall.notify.url}")
    private String notifyUrl;
    @Autowired
    private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;

    private static final String LOG_PREFIX = "请求/mall/koi接口，";

    @RequestMapping(value = "/koi", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "锦鲤商城H5地址接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "锦鲤商城H5地址POST", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<KoiMallResponse> koiMall(BaseRequest baseRequest, HttpServletRequest request) {
        logger.info("{}请求参数baseRequest={}",LOG_PREFIX, JSONObject.toJSONString(baseRequest));
        CommonResponse<KoiMallResponse> koiMallResponse = CommonResponse.newCommonResponse(ResponseConstant.RESP_SUCC_CODE, ResponseConstant.RESP_SUCC_MESG);
        try {
            //锦鲤商城开关
            String koiMallSwitch = sysConfigPropertyServiceClient.getConfigValueByKey(SysConfigPropertyKey.THIRD_MALL_KOI_SWITCH);
            if ("0".equals(koiMallSwitch)) {
                return CommonResponse.newCommonResponse(ResponseConstant.RESP_ERROR_CODE,"锦鲤商城暂未开通，敬请期待");
            }
            UserConsumer userConsumer = getLoginUsr(request);
            OkHttpClient client = new OkHttpClient();
            KoiUtil koiUtil = new KoiUtil(appKey, mchId, secret, host, client);
            if (StringUtils.isBlank(userConsumer.getOpenId())) {
                return CommonResponse.newCommonResponse(ResponseConstant.RESP_ERROR_CODE,"openId为空，请重新登陆后再试");
            }
            String memberName = StringUtils.isNotBlank(userConsumer.getTrueName()) ? userConsumer.getTrueName() : null;
            String identityNumber = StringUtils.isNotBlank(userConsumer.getCertNo()) ? userConsumer.getCertNo() : null;
            String mobile = StringUtils.isNotBlank(userConsumer.getPhone()) ? userConsumer.getPhone() : null;
            KoiH5UrlRequestVO requestVO = new KoiH5UrlRequestVO();
            requestVO.setMemberId(userConsumer.getOpenId());
            requestVO.setMemberName(memberName);
            requestVO.setIdentityNumber(identityNumber);
            requestVO.setMobile(mobile);
            requestVO.setNotifyUrl(notifyUrl);
            String logKey = RandomUtil.randomStringUpper(5);
            logger.info("[{}]-用户[{}]-openId[{}]请求锦鲤商城地址-参数：{}", logKey, userConsumer.getId(), userConsumer.getOpenId(), requestVO.toString());
            String result = koiUtil.postJson("/h5", requestVO);
            logger.info("[{}]-用户[{}]-openId[{}]请求锦鲤商城地址-结果：{}", logKey, userConsumer.getId(), userConsumer.getOpenId(), result);
            JSONObject resultJsonObj = JSON.parseObject(result);
            if (!String.valueOf(resultJsonObj.get("code")).equals("200")) {
                String msg = resultJsonObj.getString("msg");
                return CommonResponse.newCommonResponse(ResponseConstant.RESP_ERROR_CODE, msg);
            }
            String url = resultJsonObj.getJSONObject("data").getString("url");
            logger.info("{}锦鲤商城跳转地址={}",LOG_PREFIX, url);
            koiMallResponse.setData(new KoiMallResponse(url));
        } catch (Exception e) {
            logger.info("{}异常:", LOG_PREFIX,e);
            koiMallResponse.setCode(ResponseConstant.RESP_ERROR_CODE);
            koiMallResponse.setMessage(e.getMessage());
        }
        return koiMallResponse;
    }

    /**
     * 重定向跳转到锦鲤商城首页
     * */
    @RequestMapping("/koi/index")
    public ModelAndView koiIndex(BaseRequest baseRequest, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            CommonResponse<KoiMallResponse> koiIndex = koiMall(baseRequest, request);
            if (koiIndex.getCode().equals("200") && koiIndex.getData() != null){
                modelAndView.setView(new RedirectView(koiIndex.getData().getKoiMallUrl()));
                return modelAndView;
            }
            logger.info("重定向跳转到锦鲤商城首页失败：{}",JSON.toJSONString(koiIndex));
        }catch (Exception e){
            logger.error("重定向跳转到锦鲤商城首页异常：",e);
        }
        return null;
    }

}
