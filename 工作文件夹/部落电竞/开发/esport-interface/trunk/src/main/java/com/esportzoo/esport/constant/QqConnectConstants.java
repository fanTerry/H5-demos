package com.esportzoo.esport.constant;

/**
 * @author jiajing.he
 * @date 2020/2/21 16:14
 */
public class QqConnectConstants {

    /*获取用户信息的重要接口  详情 {@link https://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token}*/
    public final static String baseURL = "https://graph.qq.com/";

    public final static String getUserInfoURL = "https://graph.qq.com/user/get_user_info";

    public final static String accessTokenURL = "https://graph.qq.com/oauth2.0/token";

    public final static String authorizeURL = "https://graph.qq.com/oauth2.0/authorize";

    public final static String getOpenIDURL = "https://graph.qq.com/oauth2.0/me";

    public final static String toqqLoginURL = "https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=%s&redirect_uri=%s&state=%s";

    public final static String appqqLoginURL = "https://graph.qq.com/oauth2.0/authorize?response_type=token&client_id=%s&redirect_uri=%s&state=%s";
}