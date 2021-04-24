package com.esportzoo.esport.controller.app;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.GetLocationUtil;
import com.esportzoo.esport.connect.request.app.AppClientInfo;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.ClientLog;
import com.esportzoo.esport.service.common.ClientLogService;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.ClientLogQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName AppClientController
 * @Description
 * @Author jiajing.he
 * @Date 2019/10/16  15:15
 * @Version 1.0
 **/
@RequestMapping("appClient")
@Controller
@Api(value = "用户移动端相关接口", tags={"用户移动端相关接口"})
public class AppClientController extends BaseController {

    @Autowired
    private ClientLogService clientLogService;
    private Logger logger= LoggerFactory.getLogger(AppClientController.class);

    private final String prefix="用户移动端相关接口_";

    @RequestMapping(value = "/addUserClientInfo", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "记录用户移动端信息", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
    @ApiResponse(code = 200, message = "记录用户移动端信息", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse addUserClientInfo(HttpServletRequest request, AppClientInfo info){
        try {
            String header = request.getHeader("User-Agent");
            Pattern pattern = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
            Matcher matcher = pattern.matcher(header);
            String phone = null;
            if (matcher.find()) {
                phone = matcher.group(1).trim();
            }else{
                phone = "未知型号";
            }
            String clientIp = RequestUtil.getClientIp(request);
            Map location = GetLocationUtil.getTaoBaoLocation(clientIp);
            String deviceId = info.getDeviceId();
            ClientLogQueryVo param = new ClientLogQueryVo();
            param.setDeviceId(deviceId);
            ModelResult<List<ClientLog>> queryList = clientLogService.queryList(param);
            List<ClientLog> model = queryList.getModel();
            if (!queryList.isSuccess() || null==model){
                logger.info(prefix+"查询列表出现异常 param【{}】 异常【{}】", JSONObject.toJSONString(info),queryList.getErrorMsg());
                return CommonResponse.withErrorResp("服务器异常！");
            }
            if (CollectionUtil.isNotEmpty(model)){
                return CommonResponse.withSuccessResp("记录存在");
            }
            ClientLog clientLog = new ClientLog();
            clientLog.setDeviceStyle(phone);
            clientLog.setIpAddress(clientIp);
            clientLog.setCity((String) location.get("city"));
            clientLog.setProvince((String) location.get("region"));
            clientLog.setOsType(getOsVersion(header));
            BeanUtil.copyProperties(info, clientLog, CopyOptions.create().ignoreNullValue());
            clientLog.setChannelNo(info.getAgentId().intValue());
            ModelResult<Long> longModelResult = clientLogService.addAndGetPrimaryKey(clientLog);
            Long aLong = longModelResult.getModel();
            if (!longModelResult.isSuccess() || null==aLong){
                logger.info(prefix+"增加用户移动端信息出现异常 param【{}】 异常【{}】", JSONObject.toJSONString(info),queryList.getErrorMsg());
                return CommonResponse.withErrorResp("服务器异常！");
            }
            return CommonResponse.withSuccessResp("记录成功");
        } catch (Exception e) {
            logger.info(prefix+"添加客户端信息异常 param【{}】 异常【{}】", JSONObject.toJSONString(info),e.getMessage(),e);
        }
        return CommonResponse.withErrorResp("服务器异常！");
    }

    /**
     * @param userAgent 获取操作系统版本
     * @return
     */
    public static String getOsVersion(String userAgent) {
        String osVersion = "";
        if(StrUtil.isBlank(userAgent)) {
            return osVersion;
        }
        String[] strArr = userAgent.substring(userAgent.indexOf("(")+1,
                userAgent.indexOf(")")).split(";");
        if(null == strArr || strArr.length == 0) {
            return osVersion;
        }

        osVersion = strArr[1];
        return osVersion;
    }
}
