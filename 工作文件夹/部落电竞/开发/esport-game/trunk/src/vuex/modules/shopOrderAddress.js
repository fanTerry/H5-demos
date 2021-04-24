    const state = {
        addressObj: {}
    }
    const getters = {
        getUserAddress: state => state.addressObj
    }
    const mutations = {
        setUserAddress(state, object) {
            state.addressObj = object
        }
    }
    const actions = {
        setUserAddress({
            commit
        }, object) {
            commit('setUserAddress', object);
        }
    }


    export default {
        state,
        getters,
        actions,
        mutations
    }
