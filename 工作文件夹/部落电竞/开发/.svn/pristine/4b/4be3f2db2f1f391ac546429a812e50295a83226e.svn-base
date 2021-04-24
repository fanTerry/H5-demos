package com.esportzoo.esport.domain;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.esport.constants.user.SexType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiajing.he
 * @date 2020/2/21 15:59
 */
@Data
public class QqConnectUserInfo implements Serializable {

    private static final long serialVersionUID = 7896901145946768971L;
    /**
     *用户在QQ空间的昵称
     */
    private String nickName;

    /**
     *性别。 如果获取不到则默认返回"男"
     */
    private Integer gender;

    /**
     * 省份
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 出生年份
     */
    private String year;


    /**
     * 大小为40×40像素的QQ头像URL。
     */
    private String icon;

    /**
     *返回码
     */
    private Integer ret;

    /**
     *如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
     */
    private String msg;

    /**
     * 唯一标识
     */
    private String openId;

    private QqConnectUserInfo() {
    }

    public QqConnectUserInfo(JSONObject json) {
        this.init(json);
    }

    private void init(JSONObject json){
        if (json != null) {
            try {
                this.ret = json.getInteger("ret");
                if (0 != this.ret) {
                    this.msg = json.getString("msg");
                } else {
                    this.msg = "";
                    this.nickName = json.getString("nickname");
                    this.gender = json.getInteger("gender_type").intValue()==SexType.BOY.getIndex()? SexType.BOY.getIndex():SexType.GIRL.getIndex();
                    /*大小为100*100像素的QQ头像URL,没有则用 40*40 */
                    this.icon = StrUtil.isNotBlank(json.getString("figureurl_qq_2"))?json.getString("figureurl_qq_2"):json.getString("figureurl_qq_1");
                    this.city=json.getString("city");
                    this.province=json.getString("province");
                    this.year=json.getString("year");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
