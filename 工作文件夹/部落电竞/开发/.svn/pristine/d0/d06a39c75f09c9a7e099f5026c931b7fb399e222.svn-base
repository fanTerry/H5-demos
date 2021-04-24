package com.esportzoo.esport.util;
import java.util.Date;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.esport.constant.WeChatUrlConstants;
import com.esportzoo.esport.domain.WxAccountUserInfo;
import com.esportzoo.esport.manager.wxaccount.WxAccountManager;
import com.esportzoo.esport.vo.user.WeixinUserVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeChatUtil {


    private static final Logger log = LoggerFactory.getLogger(WeChatUtil.class);


    //获取access_token的接口地址（GET） 限200（次/天）
    public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    //菜单创建（POST） 限100（次/天）
    public static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    //菜单查询(GET) 限
    public static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    //删除菜单
    public static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    //发送主动消息
    public static String WEIXIN_KEFU_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    //网页授权登录通过code换取token、openid、unionid
    public static String WEIXIN_OAUTH_GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    //扫码关注
    public static String WEIXIN_SCAN_TOKEN_URL ="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=";
    //扫码关注qrcodeUrl
    public static String WEIXIN_SCAN_QRCODEURL ="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";
    //基础用户信息查询接口查询用户信息
    public static String WEIXIN_BASE_GET_USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info?";

    public static String WEIXIN_DOWNLOAD_MULTIMEDIA_FILE_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";

    public static String scope = "snsapi_userinfo";

    public static String domainUrl = "beta-admin.esportzoo.com";

    public static String responseType = "code";

    @Autowired
    private WxAccountManager wechatManager;

    /**
     * 网页授权登录第一步,跳转授权地址请求地址
     * @param appid
     * @param backUrl  回调地址
     * @return
     *
     * 参考链接
     * scope为snsapi_userinfo 非静默方式授权
     * https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf0e81c3bee622d60&redirect_uri=http%3A%2F%2Fnba.bluewebgame.com%2Foauth_response.php&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect
     */
    public static String getWeixinOauth2AuthBaseUrl(String appid,String param,String backUrl,String baseUrl) {
        /*String baseUrl = request.getRequestURL().toString().replace("/wxlogin/toAuth", "");*/
        //String baseUrl = "https://beta-api.esportzoo.com";
        /*String baseUrl="http://7b6dsq.natappfree.cc";*/
        String format = "";
        if (StringUtils.isBlank(appid)) {
            log.info("getWeixinOauth2AuthBaseUrl:appid 不能为空！");
            throw new RuntimeException("微信授权登录appid为空!");
        }

       /* if (!backUrl.contains(domainUrl)) {
            log.info("重定向地址不是本站域名，不会回调微信登录！");
            return format;
        }*/
        try {
            backUrl=URLEncoder.encode(backUrl, "utf-8");
            log.info("baseurl [{}]",baseUrl);
            String urlEncode = URLEncoder.encode(baseUrl+"/wxlogin/baseAuth?backUrl="+backUrl, "UTF-8");
            format = String.format(WeChatUrlConstants.OAUTHCODE2, appid, urlEncode, responseType, scope,param);
            log.info("获取code链接:"+format);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return format;
    }


    /**
     * 网页授权登录第二步，通过code获取用户信息
     * @param code
     * @param appAppid
     * @param appAppSecret
     * @return WeixinUserVO
     */
    public static WeixinUserVO getUserAccessToken(String code, String appAppid, String appAppSecret) {

        WeixinUserVO  vo = new WeixinUserVO();
        String  accessTokenReqUrl = String.format(WeChatUrlConstants.WEIXIN_OAUTH_GET_TOKEN_URL,appAppid,appAppSecret,code);
        log.info("网页授权获取token请求链接:" + accessTokenReqUrl);
        String resultJosnStr = "";
        try{
            resultJosnStr = HttpUtil.getHttpContent(accessTokenReqUrl);
            log.info("网页授权获取token请求响应:"+resultJosnStr);
        }catch (Exception e) {
            vo.setErrcode("90001");
            vo.setErrormsg( "解析token jons格式错误");
            log.error("errcode 90001");
        }
        //转化成json格式
        JSONObject accessTokeJson = JSONObject.parseObject(resultJosnStr);
        String access_token = (String)accessTokeJson.get("access_token");
        if(StringUtils.isNotBlank(access_token)){
            vo.setAccess_token(access_token);
            vo.setOpenid((String)accessTokeJson.get("openid"));
            vo.setRefresh_token( accessTokeJson.get("refresh_token")+"");
            vo.setExpires_in(Integer.valueOf(accessTokeJson.get("expires_in")+""));
            vo.setUnionid((String)accessTokeJson.get("unionid"));
            log.info("通过code成功拿到accessToken，openid 【{}】",JSONObject.toJSONString(vo));
        }else{
            vo.setErrcode(accessTokeJson.get("errcode")+"");
            vo.setErrormsg( accessTokeJson.get("errmsg")+"");
            log.error("通过code请求用户信息错误 errcode:"+vo.getErrcode());
            log.error("通过code请求用户信息错误 errmsg:"+vo.getErrormsg());
        }
        return vo;
    }

    /**
     * 网页授权第四步，通过token获取用户详细信息,如果是静默授权方式不能调用此方法
     * @param access_token
     * @param openid
     * @return
     */
    public static WxAccountUserInfo getUserInfoByUserToken(String accessToken, String openid){
        WeixinUserVO  vo = new WeixinUserVO();
        WxAccountUserInfo wxAccountUserInfo = new WxAccountUserInfo();
        String  userinfoJosnUrl = String.format(WeChatUrlConstants.WEIXIN_OAUTH_GET_USER_URL,accessToken,openid);
        log.info("网页授权获取用户详情链接:" + userinfoJosnUrl);
        String userinfoJosnStr = "";
        try{
            userinfoJosnStr = HttpUtil.getHttpContent(userinfoJosnUrl);
            //log.info("网页授权获取用户响应:" + userinfoJosnStr);
        }catch (Exception e) {
            vo.setErrcode("90001");
            vo.setErrormsg( "解析userinfo jons格式错误");
            log.error("===getHttpContent1====:" + e);
        }
        //转化成json格式
        JSONObject userinfoJosn = JSONObject.parseObject(userinfoJosnStr);
        String name=userinfoJosn.get("nickname")+"";
        String headimgurl = userinfoJosn.get("headimgurl")+"";
        log.info("此次获取用户信息 【name:{},img:{}】", name,headimgurl);
        if(StringUtils.isNotBlank(name)){
            //去除微信表情
            String nickname = filterName(userinfoJosn.get("nickname")+"");
            if (StrUtil.isBlank(nickname)){
                Random random = new Random();
                vo.setNickname("esport_"+ (random.nextInt(9000)+1000));
            }else {
                vo.setNickname(nickname);
            }
            vo.setHeadimgurl(headimgurl);
            vo.setOpenid(userinfoJosn.get("openid")+"");
            vo.setUnionid((String)userinfoJosn.get("unionid"));
            wxAccountUserInfo.setCity((String) userinfoJosn.get("city"));
            wxAccountUserInfo.setSex(userinfoJosn.getInteger("sex"));
            wxAccountUserInfo.setCity(userinfoJosn.getString("city"));
            wxAccountUserInfo.setProvince(userinfoJosn.getString("province"));
            wxAccountUserInfo.setCountry(userinfoJosn.getString("country"));
        }else{
            vo.setErrcode(userinfoJosn.get("errcode")+"");
            vo.setErrormsg( userinfoJosn.getString("errormsg")+"");
        }
        BeanUtil.copyProperties(vo, wxAccountUserInfo, CopyOptions.create().ignoreNullValue());
        log.info("accesstoken 拿取到用户信息  【{}】",JSONObject.toJSONString(wxAccountUserInfo));
        return wxAccountUserInfo;
    }


    /**
     * 获取access_token(线下 请勿直接调用，除非使用测试公众号)
     * @param appid 凭证
     * @param appsecret 密钥
     * @return
     */
    public static String getAccessToken(String appid, String appsecret){
        String token = null;
        String requestUrl = ACCESS_TOKEN_URL.replaceAll("APPID", appid).replaceAll("APPSECRET", appsecret);
        String s = cn.hutool.http.HttpUtil.get(requestUrl);
        JSONObject jsonObject = JSONObject.parseObject(s);
        // 如果请求成功
        if (null != jsonObject) {
            token = jsonObject.getString("access_token");
            if(token == null){
                // 获取token失败
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return token;
    }

    /**
     * @param accessToken 公众号token
     * @return 获取jsapi ticket
     */
    public static String getJsTicket(String accessToken){
        String url = String.format(WeChatUrlConstants.WEIXIN_GET_JSAPI_TICKET_URL, accessToken);
        log.info("请求获取jsApiTicket连接 【{}】",url);
        String s = cn.hutool.http.HttpUtil.get(url);
        String jsapiTicket="";
        JSONObject json = JSONObject.parseObject(s);
        if (json != null) {
            jsapiTicket = json.getString("ticket");
            log.info("获取到ticket 【{}】",jsapiTicket);
        }else {
            // 获取ticket失败
            log.error("获取ticket失败 errcode:{} errmsg:{}", json.getInteger("errcode"), json.getString("errmsg"));
        }
        return jsapiTicket;
    }


    /**
     * 过滤昵称特殊表情
     */
    public static String filterName(String name) {
        if(name==null){
            return null;

        }
        if("".equals(name.trim())){
            return "";
        }

        Pattern patter = Pattern.compile("[a-zA-Z0-9\u4e00-\u9fa5]");
        Matcher match = patter.matcher(name);

        StringBuffer buffer = new StringBuffer();

        while (match.find()) {
            buffer.append(match.group());
        }

        return buffer.toString();
    }
    
    public static WxAccountUserInfo getScanUserInfoByUserToken(String accessToken, String openid){
        WeixinUserVO  vo = new WeixinUserVO();
        WxAccountUserInfo wxAccountUserInfo = new WxAccountUserInfo();
        String  userinfoJosnUrl = String.format(WeChatUrlConstants.WEIXIN_SCAN_URL,accessToken,openid);
        log.info("网页授权获取用户详情链接:" + userinfoJosnUrl);
        String userinfoJosnStr = "";
        try{
            userinfoJosnStr = HttpUtil.getHttpContent(userinfoJosnUrl);
            //log.info("网页授权获取用户响应:" + userinfoJosnStr);
        }catch (Exception e) {
            vo.setErrcode("90001");
            vo.setErrormsg( "解析userinfo jons格式错误");
            log.error("===getHttpContent1====:" + e);
        }
        //转化成json格式
        JSONObject userinfoJosn = JSONObject.parseObject(userinfoJosnStr);
        //去除微信表情
        String nickname = filterName(userinfoJosn.get("nickname")+"");
        String headimgurl = userinfoJosn.get("headimgurl")+"";
        if(StringUtils.isNotBlank(nickname)){
            vo.setNickname(nickname);
            vo.setHeadimgurl(headimgurl);
            vo.setOpenid(userinfoJosn.get("openid")+"");
            vo.setUnionid((String)userinfoJosn.get("unionid"));
            wxAccountUserInfo.setCity((String) userinfoJosn.get("city"));
            wxAccountUserInfo.setSex(userinfoJosn.getInteger("sex"));
            wxAccountUserInfo.setCity(userinfoJosn.getString("city"));
            wxAccountUserInfo.setProvince(userinfoJosn.getString("province"));
            wxAccountUserInfo.setCountry(userinfoJosn.getString("country"));
        }else{
            vo.setErrcode(userinfoJosn.get("errcode")+"");
            vo.setErrormsg( userinfoJosn.getString("errormsg")+"");
        }
        BeanUtil.copyProperties(vo, wxAccountUserInfo);
        log.info("accesstoken 拿取到用户信息  【{}】",JSONObject.toJSONString(wxAccountUserInfo));
        return wxAccountUserInfo;
    }
}