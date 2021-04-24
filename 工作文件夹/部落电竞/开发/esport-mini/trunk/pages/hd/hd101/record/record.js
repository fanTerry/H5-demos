// pages/hd/hd101/record/record.js
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		mark: 0,
		tablist: ["营收记录", "提现记录"],
		requestParam: {
			recordType: 0,
			pageSize: 10,
			pageNo: 1
		},
		recordList: [],
		currPageSize: 0,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
    this.getRecordList();
	},

	/**
	 * 生命周期函数--监听页面初次渲染完成
	 */
	onReady: function () {

	},

	/**
	 * 生命周期函数--监听页面显示
	 */
	onShow: function () {

	},

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () {

	},

	/**
	 * 生命周期函数--监听页面卸载
	 */
	onUnload: function () {

	},

	/**
	 * 页面相关事件处理函数--监听用户下拉动作
	 */
	onPullDownRefresh: function () {

	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {

	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	},

	changeTab(index) {
		console.log(index);
		this.mark = index;
		this.requestParam.recordType = index;
		this.requestParam.pageNo = 1;
		this.recordList = [];
		this.getRecordList();
	},
	getRecordList(param) {
		if (!param) {
			param = this.requestParam;
		}
		return this.$post("/api/hd101/incomeRecord", param)
			.then(rsp => {
				console.log(rsp);
				const dataResponse = rsp;
				if (dataResponse.code == "200") {
					if (dataResponse.data.recordList) {
						this.currPageSize = dataResponse.data.recordList.length;
						this.recordList = this.recordList.concat(
							dataResponse.data.recordList
						);
					}
				}
			})
			.catch(error => {
				console.log(error);
			});
	},
	onPullingUp() {
		console.log("you are onPullingUp");
		if (this._isDestroyed) {
			return;
		}
		if (this.currPageSize < this.requestParam.pageSize) {
			console.log("currPageSize", this.currPageSize);
			this.$refs.scroll.forceUpdate();
		} else {
			this.loadMore();
		}
	},

	/** 上拉加载*/
	loadMore() {
		this.requestParam.pageNo += 1;
		let param = {};
		param.pageNo = this.requestParam.pageNo;
		param.pageSize = this.requestParam.pageSize;
		param.recordType = this.requestParam.recordType;

		this.getRecordList(param).then(data => {
			this.$refs.scroll.forceUpdate();
		});
	}
})