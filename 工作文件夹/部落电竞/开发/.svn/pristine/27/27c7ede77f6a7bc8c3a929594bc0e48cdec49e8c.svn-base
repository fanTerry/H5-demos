// pages/help/myFeedBack/myFeedBack.js

const app = getApp();
const api = require('../../../libs/http');
const baseUrl = api.baseUrl;

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		myFeedbackList: [],
		pageParam: {
			pageNo: 1,
			pageSize: 10
		},
		currPageSize: 10,
		hasNextPage: true,
		showType: 1,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		// this.getMyFeedBack(this.data.pageParam);
		console.log("onLoad----进入页面刷新");
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

		console.log("onShow----进入页面刷新");
		var pageParam = {
			pageNo: 1,
			pageSize: 10
		};
		this.setData({
			myFeedbackList: [],
			pageParam: pageParam,
			hasNextPage: true,
		})
		this.getMyFeedBack(this.data.pageParam);
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
		var pageParam = {
			pageNo: 1,
			pageSize: 10
		};

		this.setData({
			myFeedbackList: [],
			pageParam: pageParam,
		})
		this.getMyFeedBack(pageParam);
	},

	/**
	 * 页面上拉触底事件的处理函数
	 */
	onReachBottom: function () {
		var pageParam = {
			pageNo: this.data.pageParam.pageNo + 1,
			pageSize: 10
		};
		this.setData({
			pageParam: pageParam
		})
		this.getMyFeedBack(this.data.pageParam);
	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	},
	getMyFeedBack(param) {
		if (param) {
			param = {
				pageNo: 1,
				pageSize: 10
			}
		}
		var that = this;
		if (!that.data.hasNextPage) {
			return;
		}
		console.log("请求翻页数据",param);
		api._postAuth("/helpcenter/myFeedbackList", param)
			.then(rsp => {
				const dataResponse = rsp;
				if (dataResponse.code == "200") {
					let dataList = dataResponse.data.dataList;
					dataList = this.data.myFeedbackList.concat(dataList);
					this.setData({
						currPageSize: dataResponse.data.dataList.length,
						hasNextPage: dataResponse.data.hasNext,
						myFeedbackList: dataList,
						pageParam: param
					})
					console.log("myFeedbackList", this.data.myFeedbackList);
					return;
				} else if (dataResponse.code == "9999") {
					wx.showToast({
						title: dataResponse.message,  //标题                            
						icon: "none"
					})
				}
			})
			.catch(error => {
				wx.showToast({
					title: "网络异常，稍后再试",  //标题                            
					icon: "none"
				})
				console.log(error);
			});
	}
})