'use strict';

import _ from 'lodash';
import Timeago from 'timeago.js';

let getCheck = {
    checkEmail(val) {
        var filter = /^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }
    },
    checkPhone(val) {
        var filter = /^1\d{10}$/;

        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }
    },
    checkIdCard(value) {
        if (value && (!(/\d{17}[\d|x]|\d{15}/).test(value) || (value.length !== 15 && value.length !== 18))) {
            return false;
        } else {
            return true;
        }

    },
    checkName(val) {
        var filter = /^[\u4e00-\u9fa5A-Za-z0-9-_]*$/;
        if (val.length < 4 || val.length > 14) {
            return false;
        }
        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }
    },
    checkId(val) {
        var filter = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/
        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }
    },
    checkTrueName(val) {
        var filter = /^[\u2E80-\u9FFF]+$/;
        if (val.length < 2 || val.length > 5) {
            return false;
        }
        if (filter.test(val)) {
            return true;
        } else {
            return false;
        }
    }
};

/**
 * 从文本中提取出@username 标记的用户名数组
 * @param {String} text 文本内容
 * @return {Array} 用户名数组
 */
const fetchUsers = (text) => {
    if (!text) {
        return [];
    }

    var ignoreRegexs = [
        /```.+?```/g, // 去除单行的 ```
        /^```[\s\S]+?^```/gm, // ``` 里面的是 pre 标签内容
        /`[\s\S]+?`/g, // 同一行中，`some code` 中内容也不该被解析
        /^.*/gm, // 4个空格也是 pre 标签，在这里 . 不会匹配换行
        /\b\S*?@[^\s]*?\..+?\b/g, // somebody@gmail.com 会被去除
        /\[@.+?\\]\(\/.+?\)/g // 已经被 link 的 username
    ];

    ignoreRegexs.forEach((ignoreRegex) => {
        text = text.replace(ignoreRegex, '');
    });

    var results = text.match(/@[a-z0-9\-_]+\b/igm);
    var names = [];
    if (results) {
        for (var i = 0, l = results.length; i < l; i++) {
            var s = results[i];
            // remove leading char @
            s = s.slice(1);
            names.push(s);
        }
    }
    names = _.uniq(names);
    return names;
};

/**
 * 根据文本内容，替换为数据库中的数据
 * @param {String} text 文本内容
 * @param {Function} callback 回调函数
 */
const linkUsers = (text) => {
    var users = fetchUsers(text);
    for (var i = 0, l = users.length; i < l; i++) {
        var name = users[i];
        text = text.replace(new RegExp('@' + name + '\\b(?!\\])', 'g'), '[@' + name + '](/user/' + name + ')');
    }
    return text;
};

/**
 *   对Date的扩展，将 Date 转化为指定格式的String
 *   月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 *   年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 *   例子：
 *   (new Date()).Format('yyyy-MM-dd hh:mm:ss.S') ==> 2006-07-02 08:09:04.423
 *   (new Date()).Format('yyyy-M-d h:m:s.S')      ==> 2006-7-2 8:9:4.18
 */
const fmtDate = (date, fmt) => { // author: meizz
    var o = {
        'M+': date.getMonth() + 1, // 月份
        'd+': date.getDate(), // 日
        'h+': date.getHours(), // 小时
        'm+': date.getMinutes(), // 分
        's+': date.getSeconds(), // 秒
        'q+': Math.floor((date.getMonth() + 3) / 3), // 季度
        'S': date.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp('(' + k + ')').test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
        }
    }
    return fmt;
};

/**
 * 调用Timeago库显示到现在的时间
 */
const MillisecondToDate = (time) => {
    var str = '';
    if (time !== null && time !== '') {
        let timeagoInstance = new Timeago();
        str = timeagoInstance.format(time, 'zh_CN');
    }
    return str;
};

/**
 * 格式化日期或时间
 * @param {string} time 需要格式化的时间
 * @param {bool} friendly 是否是fromNow
 */
exports.getLastTimeStr = (time, friendly) => {
    if (friendly) {
        return MillisecondToDate(time);
    } else {
        return fmtDate(new Date(time), 'yyyy-MM-dd hh:mm');
    }
};

/** 格式化时间
 *  @param {string} time 需要格式化的时间
 *  @param {bool} friendly 是否是fromNow
 */
exports.getQuizRecordTime = (time, friendly) => {
    if (friendly) {
        return MillisecondToDate(time);
    } else {
        return fmtDate(new Date(time), 'MM-dd hh:mm:ss');
    }
};

/**
 * 获取不同tab的信息
 * @param  {[type]}  tab     [tab分类]
 * @param  {[type]}  good    [是否是精华]
 * @param  {[type]}  top     [是否置顶]
 * @param  {Boolean} isClass [是否是样式]
 * @return {[type]}          [返回对应字符串]
 */
exports.getTabInfo = (tab, good, top, isClass) => {
    let str = '';
    let className = '';
    if (top) {
        str = '置顶';
        className = 'top';
    } else if (good) {
        str = '精华';
        className = 'good';
    } else {
        switch (tab) {
            case 'share':
                str = '分享';
                className = 'share';
                break;
            case 'ask':
                str = '问答';
                className = 'ask';
                break;
            case 'job':
                str = '招聘';
                className = 'job';
                break;
            default:
                str = '暂无';
                className = 'default';
                break;
        }
    }
    return isClass ? className : str;
};

/**
 * 配置节流函数
 * @param  {[Function]}  fn     [要执行的函数]
 * @param  {[Number]}  delay    [延迟执行的毫秒数]
 * @param  {[Number]}  mustRun  [至少多久执行一次]
 * @return {[Function]}         [节流函数]
 */
exports.throttle = (fn, wait, mustRun) => {
    let timeout;
    let startTime = new Date();
    return function () {
        let context = this;
        let args = arguments;
        let curTime = new Date();

        clearTimeout(timeout);
        // 如果达到了规定的触发时间间隔，触发 handler
        if (curTime - startTime >= mustRun) {
            fn.apply(context, args);
            startTime = curTime;
            // 没达到触发间隔，重新设定定时器
        } else {
            timeout = setTimeout(fn, wait);
        }
    };
};

exports.linkUsers = linkUsers;
exports.fetchUsers = fetchUsers;
exports.getCheck = getCheck;
exports.fmtDate = fmtDate;
exports.MillisecondToDate = MillisecondToDate;



export function siblings(el) {
    let len = 0;
    let children = el.parentNode.childNodes;
    let result = new Array()
    for (let i = 0, len = children.length; i < len; i++) {
        if (children[i].nodeType === 1 && children[i] !== el) {
            result.push(children[i])
        }
    }
    return result;
}

export function hasClass(el, className) {
    let reg = new RegExp('(^|\\s)' + className + '(\\s|$)')
    return reg.test(el.className)
}
export function addClass(el, className) {
    if (hasClass(el, className)) {
        return
    }
    let newClass = el.className.split(' ')
    newClass.push(className)
    el.className = newClass.join(' ')
}

export function removeClass(el, className) {
    if (!hasClass(el, className)) {
        return
    }
    let reg = new RegExp('(^|\\s)' + className + '(\\s|$)', 'g')
    el.className = el.className.replace(reg, ' ')
}

export function getData(el, name, val) {
    let prefix = 'data-'
    if (val) {
        return el.setAttribute(prefix + name, val)
    }
    return el.getAttribute(prefix + name)
}

export function getRect(el) {
    if (el instanceof window.SVGElement) {
        let rect = el.getBoundingClientRect()
        return {
            top: rect.top,
            left: rect.left,
            width: rect.width,
            height: rect.height
        }
    } else {
        return {
            top: el.offsetTop,
            left: el.offsetLeft,
            width: el.offsetWidth,
            height: el.offsetHeight
        }
    }
}

export function add(array) {
    let sum = 0
    for (let item of array) {
        sum += item - 0
    }
    return sum
}

export function parseNullAndUndefine(str) {
    if (!str || str == "undefined" || str == "null") {
        return "33444";
    }
    return str;
}

export function isWeixin() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == "micromessenger") {
        return true;
    } else {
        return false;
    }
}

//是否为iPhone
export function isApple() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.indexOf("iphone") != -1) {
        return true;
    } else {
        return false;
    }
};

export function stopPropagation(event) {
    var e = arguments.callee.caller.arguments[0] || event;//这里是因为除了IE有event其他浏览器没有所以要做兼容
    if (window.event) {       //这是IE浏览器
        e.cancelBubble = true;
    } else if (e && e.stopPropagation) {     //这是其他浏览器
        e.stopPropagation();//阻止冒泡事件
    }
}

export function debounce(fn, delay) {  //防抖函数
    var timer; // 维护一个 timer
    return function () {
        var _this = this; // 取debounce执行作用域的this
        var args = arguments;
        if (timer) {
            clearTimeout(timer);
        }
        timer = setTimeout(function () {
            fn.apply(_this, args); // 用apply指向调用debounce的对象，相当于_this.fn(args);
        }, delay);
    };
}

export function throttle(fn, delay) { //时间戳实现节流函数
    var previous = 0;
    // 使用闭包返回一个函数并且用到闭包函数外面的变量previous
    return function () {
        var _this = this;
        var args = arguments;
        var now = new Date();
        if (now - previous > delay) {
            fn.apply(_this, args);
            previous = now;
        }
    }
}

export function getCookie(sName) {
    var aCookie = document.cookie.split("; ");
    for (var i = 0; i < aCookie.length; i++) {
        var aCrumb = aCookie[i].split("=");
        if (sName == aCrumb[0])
            return unescape(aCrumb[1]);
    }
    return null;
}


export function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 10000);
    var cval = getCookie(name);
    if (cval != null) {
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
    }

}

export function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}

export function getUrlCommonParam() {
    var agentId = getQueryString("agentId")
    var clientType = getQueryString("clientType")
    var biz = getQueryString("biz")
    return "agentId=" + agentId + "&biz=1" + biz + "&clientType=" + clientType;

}
