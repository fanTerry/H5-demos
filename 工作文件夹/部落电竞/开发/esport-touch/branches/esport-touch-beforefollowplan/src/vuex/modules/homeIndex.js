    const state = {
    	navigateListObj: {}
    }
    const getters = {
    	getNavigateList:state => state.navigateListObj
    }
    const mutations = {
    	setNavigateList(state, list) {
    		state.navigateListObj = list
    	}
    }
    const actions = {
    	setNavigateList({ commit }, list) {
    		commit('setNavigateList', list);
    	}
    }


    export default {
    	state,
    	getters,
    	actions,
    	mutations
    }