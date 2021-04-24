import {
	RECORD_ADDRESS,
	CHOOSE_SEARCH_ADDRESS,
	USER_CENTER_INFO,
	MY_EXCHANGE_TAB,
	MY_GOOD_LIST_TAB
} from './mutation-types.js'

import {setStore, getStore} from '../../util/mUtils'

export default {
	// 记录当前经度纬度
	[RECORD_ADDRESS](state, {
		latitude,
		longitude
	}) {
		state.latitude = latitude;
		state.longitude = longitude;
	},

	//选择搜索的地址
	[CHOOSE_SEARCH_ADDRESS](state, place) {
		state.searchAddress = place;
	},

	[USER_CENTER_INFO](state, userCenterInfo) {
		state.userCenterInfo = userCenterInfo;
	},
	[MY_EXCHANGE_TAB](state, tabId) {
		state.myExchangeTab = tabId;
	},
	[MY_GOOD_LIST_TAB](state, param) {
		state.myGoodlistParam = param;
	},

}
