import Vue from 'vue'
// 全局过滤器
Vue.filter("parseNullAndUndefine", function (str) {
    if(!str || str=="undefined" || str=="null"){
        return "";
        }
        return str;
    });


  