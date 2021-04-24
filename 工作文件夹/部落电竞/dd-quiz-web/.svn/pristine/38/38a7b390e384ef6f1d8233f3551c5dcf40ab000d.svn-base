    const state = {
        betData: {
            toCurBet: null, // 当前点击的下单选项,格式:quiz_match_game表中id+"_"+选项index
            toCharge: null, // 当下单余额不足时,是否需要去充值(展示充值弹窗):true/false
            toReflushBalance: null // 下单是否需要刷新余额:true/false

        }
    };
    const getters = {
        getBetData: state => state.betData
    };
    const mutations = {
        setBetData(state, object) {
            state.betData = object;
        }
    };
    const actions = {
        setBetData({commit}, object) {
            commit('setBetData', object);
        }
    };

    export default {
        state,
        getters,
        actions,
        mutations
    };
