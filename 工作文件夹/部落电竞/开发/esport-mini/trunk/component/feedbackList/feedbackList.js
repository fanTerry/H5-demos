// component/feedbackList/feedbackList.js
Component({
	/**
	 * 组件的属性列表
	 */
	properties: {
		myFeedbackList: Array,
		type: Number,
		userInfo:Object,
	},

	/**
	 * 组件的初始数据
	 */
	data: {

	},

	/**
	 * 组件的方法列表
	 */
	methods: {
		goToDetail: function (e) {
			var qid = e.currentTarget.dataset.id;
			console.log("qid", qid);
			//如果是自己的反馈，就不需要跳转
			if (this.data.type == 2) {
				return;
			}
			wx.navigateTo({
				url: '/pages/help/feedBackDetail/feedBackDetail?qid=' + qid,
			})
		},
	}
})
