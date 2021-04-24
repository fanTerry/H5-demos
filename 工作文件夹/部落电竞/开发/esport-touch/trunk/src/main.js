import Vue from 'vue';
import $ from 'webpack-zepto';
import VueRouter from 'vue-router';
import filters from './filters';
import routes from './router/routers';
import Alert from './libs/alert';
import './libs/rem';
import store from './vuex';
import FastClick from './libs/fastclick';
import 'vue-photo-preview/dist/skin.css';
import './assets/common/iconfont.css';
import './assets/common/main.scss';
import './assets/common/swiper';
import Swiper from 'swiper';
import 'jquery';
import toastRegistry from './components/common/toast/index';
import {
    post,
    get
} from './libs/request/http';
import wxApi from "./libs/weixinShare";
import axios from 'axios';
import VConsole from 'vconsole';
import globalConst from './globalConst';
import baseParamConfig from './baseParamConfig';
import * as socketApi from './libs/websocket/socketService';
import * as socketToolApi from './libs/websocket/socketServiceTool';
import * as socketGuess from './libs/websocket/socketGuess';
import wx from 'weixin-js-sdk';
import preview from 'vue-photo-preview';
import vueHashCalendar from 'vue-hash-calendar'
import 'vue-hash-calendar/lib/vue-hash-calendar.css'
Vue.use(vueHashCalendar)
Vue.prototype.socketApi = socketApi;
Vue.prototype.socketToolApi = socketToolApi;
Vue.prototype.socketGuess = socketGuess;
Vue.prototype.wx = wx
// 定义全局变量
Vue.prototype.$post = post;
Vue.prototype.$get = get;
Vue.prototype.$axios = axios;
Vue.prototype.globalConst = globalConst;
Vue.prototype.baseParamConfig = baseParamConfig;
Vue.prototype.$wxApi = wxApi;
Vue.use(VueRouter);
Vue.use(Alert);
Vue.use(toastRegistry);
let options = {
    fullscreenEl: true,
    history: true //点击返回键可以回退
};
Vue.use(preview, options);
Vue.use(preview);
console.log(process.env.NODE_ENV, 'process.env.NODE_ENV');
if (process.env.NODE_ENV === 'daily') {
    new VConsole();
}
//事件总线
var eventBus = {
    install(Vue, options) {
        Vue.prototype.$bus = new Vue()
    }
};
Vue.use(eventBus);

// 实例化Vue的filter
Object.keys(filters).forEach(k => Vue.filter(k, filters[k]));

// 实例化VueRouter
const router = new VueRouter({
    mode: 'history',
    routes
});
if ('addEventListener' in document) {
    document.addEventListener('DOMContentLoaded', function () {
        FastClick.attach(document.body);
    }, false);
}
if (window.localStorage.user) {
    store.dispatch('setUserInfo', JSON.parse(window.localStorage.user));
}

function getCookie(sName) {
    var aCookie = document.cookie.split("; ");
    for (var i = 0; i < aCookie.length; i++) {
        var aCrumb = aCookie[i].split("=");
        if (sName == aCrumb[0])
            return unescape(aCrumb[1]);
    }
    return null;
}

if (window.localStorage.token) {
    if (!getCookie("h5_login_cookie_sid")) {
        console.log("进来获取本地缓存的token");
        var token = window.localStorage.token
        var day = 1;
        var exp = new Date();
        var name = "h5_login_cookie_sid";
        exp.setTime(exp.getTime() + day * 24 * 60 * 60 * 1000);
        document.cookie = name + "=" + escape(token) + ";expires=" + exp.toGMTString();
    }

}
/** 在话题页测试返回 */
var firstUrl = window.location.href;
console.log("99获取到的url", firstUrl, window.sessionStorage.getItem("firstUrl"));
if (window.sessionStorage.getItem("firstUrl") == null) {
    window.sessionStorage.setItem("firstUrl", firstUrl);
}

//统一加渠道号&登录中间验证，页面需要登录而没有登录的情况直接跳转登录 from 上一个路由,to现在的路由
router.beforeEach((to, from, next) => {
    if (to.query.debug && to.query.debug == 1) {
        new VConsole();
    }
    if (to.meta.title) {
        document.title = to.meta.title
    }
    //有cookie在加loginFlag判断的原因是避免服务端redis缓存失效
    //在全局配置中添加的原因是避免已经登录的状态下多次调用第三方登录接口
    var path = to.path;
    var agentId = to.query.agentId ? to.query.agentId : baseParamConfig.agentId;
    var biz = to.query.biz ? to.query.biz : baseParamConfig.biz;
    var inviteCode = to.query.inviteCode ? to.query.inviteCode : baseParamConfig.inviteCode;
    var ADTAG = baseParamConfig.ADTAGTable[agentId] ? baseParamConfig.ADTAGTable[agentId] : baseParamConfig.ADTAG;
    var loginflag = to.query.loginflag
    if (loginflag) {
        baseParamConfig.setLoginFlag(loginflag);
    }
    var clientType = to.query.clientType ? to.query.clientType : baseParamConfig.clientType;
    biz = biz && Array.isArray(biz) ? biz[0] : biz;
    agentId = agentId && Array.isArray(agentId) ? agentId[0] : agentId;
    clientType = clientType && Array.isArray(clientType) ? clientType[0] : clientType;
    inviteCode = inviteCode && Array.isArray(inviteCode) ? inviteCode[0] : inviteCode;
    baseParamConfig.setClientType(clientType);
    console.log("to.fullPath=" + to.fullPath + ",agentId=" + agentId + ",biz=" + biz + ",loginflag=" + loginflag + ",clientType=" + clientType + ",ADTAG=" + ADTAG);
    if (biz && biz == 2) { //友宝业务系统
        var cookieSid = null;
        var aCookie = document.cookie.split("; ");
        for (var i = 0; i < aCookie.length; i++) {
            var aCrumb = aCookie[i].split("=");
            if ("ubox_login_cookie_sid" == aCrumb[0]) {
                cookieSid = unescape(aCrumb[1]);
            }
        }
        console.log(cookieSid, "beforeEach，友宝进入获取登录cookie");
        if (!cookieSid || cookieSid == "" || cookieSid == '""') {
            console.log("无cookie值，进入友宝登录");
            window.location.href = "/api/third/login?agentId=" + agentId + "&biz=" + biz + "&redirect=" + encodeURIComponent(window.location.href);
            return;
        } else {
            if (!baseParamConfig.loginFlag) {
                console.log("有cookie,但没有登录标记，进入友宝登录");
                window.location.href = "/api/third/login?agentId=" + agentId + "&biz=" + biz + "&redirect=" + encodeURIComponent(window.location.href);
                return;
            }
        }
    }
    if (clientType == 7) { //微信公众号进入
        var cookieSid = null;
        var aCookie = document.cookie.split("; ");
        for (var i = 0; i < aCookie.length; i++) {
            var aCrumb = aCookie[i].split("=");
            if ("wx_account_login_cookie_sid" == aCrumb[0]) {
                cookieSid = unescape(aCrumb[1]);
            }
        }
        console.log(cookieSid, "beforeEach，微信公众号进入获取登录cookie");
        if (!cookieSid || cookieSid == "" || cookieSid == '""') {
            console.log("无cookie值，进入微信公众号登录");
            window.location.href = "/api/wxlogin/toAuth?agentId=" + agentId + "&biz=" + biz + "&clientType=7" + "&backUrl=" + encodeURIComponent(window.location.href);
            return;
        }
    }
    window.sessionStorage.setItem("redirectUrl", encodeURIComponent(to.fullPath));
    globalConst.entryUrl = to.fullPath
    window.sessionStorage.setItem("entryUrl", to.fullPath);
    //to.query.ADTAG 会导致在微信中不会自动带上参数
    if (to.query.agentId && to.query.biz && to.query.clientType && to.query.ADTAG && (to.query.ADTAG === ADTAG)) {
        baseParamConfig.setAgentId(agentId);
        baseParamConfig.setBiz(biz);
        baseParamConfig.setClientType(clientType);
        baseParamConfig.setADTAG(ADTAG);
        if (to.query.inviteCode) {
            baseParamConfig.setInviteCode(inviteCode);
        }
        next();
        return;
    }
    if (agentId || biz || clientType) {
        baseParamConfig.setAgentId(agentId);
        baseParamConfig.setBiz(biz);
        baseParamConfig.setClientType(clientType);
        baseParamConfig.setADTAG(ADTAG);
        let toQuery = JSON.parse(JSON.stringify(to.query));
        let toParams = JSON.parse(JSON.stringify(to.params)) || {};
        console.log(toParams, 'toparams');
        toQuery.agentId = agentId;
        toQuery.biz = biz;
        toQuery.clientType = clientType;
        toQuery.ADTAG = ADTAG;
        if (inviteCode) {
            baseParamConfig.setInviteCode(inviteCode);
            toQuery.inviteCode = inviteCode;
        }
        globalConst.entryUrl = to.fullPath
        window.sessionStorage.setItem("entryUrl", to.fullPath);
        if (JSON.stringify(toParams) != '{}') {
            console.log('toParams不为空');
            next({
                name: to.name,
                params: toParams,
                query: toQuery
            })
        } else {
            next({
                path: to.path,
                query: toQuery
            })
        }
    } else {
        globalConst.entryUrl = to.fullPath
        window.sessionStorage.setItem("entryUrl", to.fullPath);
        next();
    }
});



//以下代码未用上,暂时不删
router.onError((error) => {
    console.log("cuowu", error);
    const pattern = /Loading chunk (\d)+ failed/g;
    const isChunkLoadFailed = error.message.match(pattern);
    const targetPath = router.history.pending.fullPath;
    if (isChunkLoadFailed) {
        router.replace(targetPath);
    }
});

const rootVueObj = new Vue({
    router,
    store
}).$mount('#app');

export default rootVueObj;