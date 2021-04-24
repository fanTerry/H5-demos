const state = {
    starNum: {}, // 用户星星
    yeYunPoints: {} // 椰云积分
};
const getters = {
    getstarNum: state => state.starNum,
    getYeYunPoints: state => state.yeYunPoints
};
const mutations = {
    setstarNum(state, Number) {
        state.starNum = Number;
    },
    setYeYunPoints(state, Number) {
        state.yeYunPoints = Number;
    }
};
const actions = {
    setstarNum({ commit }, Number) {
        commit('setstarNum', Number);
    },
    setYeYunPoints({ commit }, Number) {
        commit('setYeYunPoints', Number);
    }
};

export default {
    state,
    getters,
    actions,
    mutations
};