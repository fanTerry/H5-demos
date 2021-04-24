const state = {
    userInfo: {
        nickName: '', //用户昵称
        icon: '', //用户头像
        userId: null, //用户id
        phone: null, //用户手机号
        yeYunPoints: null, // 椰云积分
        haiLePoints: null, // 嗨乐积分
        recScore: null, //推荐币余额
        giftRecScore: null, //赠送币余额
        ableRecScore: null, //可用余额(recScore+giftRecScore)
        exchangeStarNum: 0, //用户可兑换余额
    }
}
const getters = {
    getUserInfo: state => state.userInfo
}
const mutations = {
    setUserInfo(state, userInfo) {
        state.userInfo = userInfo
    }
}
const actions = {
    setUserInfo({ commit }, user) { commit('setUserInfo', user); }
}


export default {
    state,
    getters,
    actions,
    mutations
}
