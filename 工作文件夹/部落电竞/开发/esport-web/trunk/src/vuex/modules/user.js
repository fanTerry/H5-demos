const state = {
    userData: {
        // userInfo: null, // 用户信息        
        yeYunPoints: null, // 椰云积分
        nickName: '', // 用户昵称
        icon: '', // 用户头像
        userId: null, // 用户id
        phone: null, // 用户手机号
        recScore: null, // 推荐币余额
        giftRecScore: null, // 赠送币余额        
        starNum: null // 用户星星(recScore+giftRecScore)
    }
};
const getters = {
    getUserData: state => state.userData
};
const mutations = {
    setUserData(state, object) {
        state.userData = object;
    }
};
const actions = {
    setUserData({
        commit
    }, object) {
        commit('setUserData', object);
    }
};

export default {
    state,
    getters,
    actions,
    mutations
};
