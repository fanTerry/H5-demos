    const state = {
        guessData: {
            isFirstGuess:null, //是否为首次竞猜下单
        }
    };
    const getters = {
        getGuessData: state => state.guessData
    };
    const mutations = {
        setGuessData(state, object) {
            state.guessData = object;
        }
    };
    const actions = {
        setGuessData({commit}, object) {
            commit('setGuessData', object);
        }
    };

    export default {
        state,
        getters,
        actions,
        mutations
    };
