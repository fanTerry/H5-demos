/*
 * @Author: your name
 * @Date: 2020-04-21 14:49:02
 * @LastEditTime: 2020-04-21 14:52:04
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch/src/vuex/modules/expert.js
 */
const state = {
    expertData: {
        payedArticle: null, // 存储文章支付完成的ID，用于返回后页面的文章支付状态的刷新
    }
};
const getters = {
    getExpertData: state => state.expertData
};
const mutations = {
    setExpertData(state, object) {
        state.expertData = object;
    }
};
const actions = {
    setExpertData({commit}, object) {
        commit('setExpertData', object);
    }
};

export default {
    state,
    getters,
    actions,
    mutations
};