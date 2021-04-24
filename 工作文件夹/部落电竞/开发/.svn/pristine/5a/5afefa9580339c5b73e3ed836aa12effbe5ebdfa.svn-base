package com.esportzoo.esport.constant;


public class WeChatUrlConstants {

    //网页授权登录获取code接口地址（GET）无上限
    public final static String OAUTHCODE2 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s&connect_redirect=1#wechat_redirect";

    //网页授权登录通过code换取token、openid、unionid
    public static String WEIXIN_OAUTH_GET_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    //网页授权登录通过token获取用户信息,只有非静默授权方式才可以调用
    public static String WEIXIN_OAUTH_GET_USER_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
    //扫码关注
    public static String WEIXIN_SCAN_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s";
    //获取Js_tiket 建议缓存  有效期7200s
    public static  String WEIXIN_GET_JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
}