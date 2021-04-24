    const state = {
        shakeData: {
            toReflushBalance: null, // 下单是否需要刷新余额:true/false
        }
    };
    const getters = {
        getShakeData: state => state.shakeData
    };
    const mutations = {
        setShakeData(state, object) {
            state.shakeData = object;
        }
    };
    const actions = {
        setShakeData({commit}, object) {
            commit('setShakeData', object);
        }
    };

    export default {
        state,
        getters,
        actions,
        mutations
    };
