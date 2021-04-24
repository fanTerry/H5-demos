import Vue from 'vue';
import Vuex from 'vuex';
import user from './modules/user';
import homeIndex from './modules/homeIndex';
import login from './modules/login';
import mutations from './constStorage/mutations';
import getters from './constStorage/getters';
import bet from './modules/bet';

Vue.use(Vuex);
const state = {
    latitude: '', // 当前位置纬度
    longitude: '', // 当前位置经度
    searchAddress: null, // 搜索并选择的地址
    userCenterInfo: null, // 用户中心
    myExchangeTab: null,
    myGoodlistParam: null // 记录存放商品列表页面参数，返回使用
};

export default new Vuex.Store({
    modules: {
        user,
        homeIndex,
        login,        
        bet
    },
    mutations,
    getters
});
