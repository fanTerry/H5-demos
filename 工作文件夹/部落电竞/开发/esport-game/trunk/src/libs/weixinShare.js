/*
 * @Author: your name
 * @Date: 2019-10-11 18:38:45
 * @LastEditTime: 2020-04-27 17:26:00
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch/src/libs/weixinShare.js
 */
/**
 * 微信js-sdk
 * 参考文档：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141115
 */
// import wx from 'weixin-js-sdk'
let wx = require('weixin-js-sdk');
import Axios from 'axios'
import {isWeixin} from '../libs/utils';
const wxApi = {
    /**
     * [wxRegister 微信Api初始化]
     * @param  {Function} option [分享参数]
     */
    wxRegister(option) {

        //非微信环境不做处理
        if (!isWeixin()) {
            console.log("非微信浏览器");
            return;
        }
        if(!option.link){//没有传入分享地址的情况
            let url = window.location.protocol + "//" + window.location.host + window.sessionStorage.getItem("entryUrl")
            // if (navigator.userAgent.toLowerCase().match(/iphone|ipad/i)) {
            //     url = window.location.protocol + "//" + window.location.host + window.sessionStorage.getItem("entryUrl")
            // } else {
            //     url = location.href.split('#')[0];
            // }
            option.link = url; // 分享链接，根据自身项目决定是否需要split
        }
        console.log(option, '需要分享的参数信息');
        Axios.post('/agency/wxlogin/jsCheck', {
            reqUrl: location.href.split('#')[0]
        }, {
            timeout: 5000,
            withCredentials: true
        }).then((res) => {
            // let data = JSON.parse(res.data.data) // PS: 这里根据你接口的返回值来使用
            let data = res.data;
            console.log(res.data,'配置分享接口返回数据');
            wx.config({
                debug: false, // 开启调试模式
                appId: data.appId, // 必填，公众号的唯一标识
                timestamp: data.timestamp, // 必填，生成签名的时间戳
                nonceStr: data.nonceStr, // 必填，生成签名的随机串
                signature: data.signature, // 必填，签名，见附录1
                jsApiList: ["updateAppMessageShareData", "updateTimelineShareData"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            })
            wx.ready((res) => {
                console.log("option", option);
                this.ShareAppMessage(option)
                this.ShareTimeline(option)
            })
        }).catch((error) => {
            console.log(error)
        })
       
    },

    /**
     * [ShareTimeline 微信分享到朋友圈]
     * @param {[type]} option [分享信息]
     * @param {[type]} success [成功回调]
     */
    ShareTimeline(option) {
        wx.updateTimelineShareData({
            title: option.title, // 分享标题
            link: option.link, // 分享链接
            imgUrl: option.imgUrl, // 分享图标
            success: function () {
                // 设置成功后执行的函数
                console.log("分享到朋友圈设置成功后执行函数");
            },
        })
    },

    /**
     * [ShareAppMessage 微信分享给朋友]
     * @param {[type]} option [分享信息]
     * @param {[type]} success [成功回调]
     */
    ShareAppMessage(option) {
        console.log("分享给朋友");
        wx.updateAppMessageShareData({
            title: option.title, // 分享标题
            desc: option.desc, // 分享描述
            link: option.link, // 分享链接
            imgUrl: option.imgUrl, // 分享图标
            success: function () {
                // 设置成功后执行的函数
                console.log("分享给朋友设置成功后执行函数");
            },
        })
    }
}
export default wxApi
