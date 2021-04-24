const state = {
    loginType: {},
    loginShowType: {},
    codeType: {}
};
const getters = {
    getLoginType: state => state.loginType,
    getLoginShowType: state => state.loginShowType,
    getCodeType: state => state.codeType

};
const mutations = {
    setLoginType(state, object) {
        state.loginType = object;
    },
    setLoginShowType(state, object) {
        state.loginShowType = object;
    },
    setCodeType(state, object) {
        state.codeType = object;
    }    
};
const actions = {
    setLoginType({
        commit
    }, object) {
        commit('setLoginType', object);
    },
    setLoginShowType({
        commit
    }, object) {
        commit('setLoginShowType', object);
    },
    setCodeType({
        commit
    }, object) {
        commit('setCodeType', object);
    }
};

export default {
    state,
    getters,
    actions,
    mutations
};