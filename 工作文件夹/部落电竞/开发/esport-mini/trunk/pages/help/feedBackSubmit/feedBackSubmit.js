// pages/help/feedBackSubmit/feedBackSubmit.js
const app = getApp();
const api = require('../../../libs/http.js')
const baseUrl = api.baseUrl;

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
		questionType: "", //问题类型
		content: "", //要提交的内容
		phone: "", //手机号
		imgs: [], // 图片预览地址
		imgfiles: [], // 图片原文件，上传到后台的数据
		maxSize: 1, // 限制上传数量
		test: "",
		repeatClickFlag: false,
		type: 5 //暂时不删除,以防以后会上传视频
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		app.checkSessionAndLogin();

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
	toMyFeedback: function () {
		wx.navigateTo({
			url: "/pages/help/myFeedBack/myFeedBack",
		})
	},
	// 绑定输入框内容
	changeVal: function (e) {
		this.setData({
			content: e.detail.value
		})
	},
	bindBlur: function (e) {
		this.setData({
			content: e.detail.value
		})
	},
	// 绑定手机号
	checkPhone: function (e) {
		let str = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1}))+\d{8})$/
		if (str.test(e.detail.value)) {
			this.setData({
				phone: e.detail.value
			})
		} else {
			wx.showToast({
				title: '手机号格式不正确',
				icon: "none"
			})
		}
	},
	// 上传图片
	fileChange: function () {
		var that = this;
		wx.chooseImage({
			count: 1,
			sizeType: ['original', 'compressed'],
			sourceType: ['album', 'camera'],
			success(res) {
				var tempFilesSize = res.tempFiles[0].size;  //获取图片的大小，单位B                    
				if (tempFilesSize <= 30000000) {   //图片小于或者等于30M时 可以执行获取图片                        
					var tempFilePaths = res.tempFilePaths[0]; //获取图片                        
					that.data.imgs.push(tempFilePaths);   //添加到数组                        
					that.setData({
						imgs: that.data.imgs
					})
				} else {    //图片大于30M，弹出一个提示框                        
					wx.showToast({
						title: '上传图片不能大于30M!', icon: "none"
					})
				}
			}
		})
	},
	// 删除图片
	del: function (e) {
		var index = e.currentTarget.dataset.index;
		var that = this;
		this.data.imgs.splice(index, 1)
		this.setData({
			imgs: that.data.imgs
		})
	},
	submitFeedBack: function () {
		var that = this;
		if (that.data.content == "") {
			wx.showToast({
				title: '请输入要反馈的内容',  //标题                            
				icon: "none"
			})
			return;
		}
		if (that.data.phone == null) {
			wx.showToast({
				title: '输入号码格式不正确',  //标题   
				icon: "none"
			})
			return;
		}
		if (this.data.imgs.length == 0) {
			wx.showToast({
				title: '请上传图片',  //标题   
				icon: "none"
			})
			return;
		}
		if (that.data.repeatClickFlag) {
			//正在反馈,不能重复点击
			wx.showToast({
				title: '正在反馈中,请稍等~',  //标题                            
				icon: "none"
			})
			return;
		}
		that.data.repeatClickFlag = true;
		var sid = wx.getStorageSync('sid');
		console.log("sid", sid);
		var agentId = app.globalData.agentId;
		var clientType = app.globalData.clientType;
		var requestUrl = baseUrl + "/helpcenter/submitFeedback?sid=" + sid + "&agentId=" + agentId + "&clientType=" + clientType;
		console.log("requestUrl", requestUrl);
		console.log("99999999", this.data.imgs[0]);
		wx.uploadFile({
			url: requestUrl,
			filePath: that.data.imgs[0],
			name: 'files',
			formData: {
				"content": that.data.content,
				"phone": that.data.phone,
				"feedbackType": 3,
				"questionType": 5
			},
			success(res) {
				console.log("88888888", res);
				that.data.repeatClickFlag = false;
				if (res.data.code == "200") {

				}
			},
			fail(res) {
				console.log(res)
				return
			}
		})

		wx.showToast({
			title: '反馈成功',  //标题                            
		})
		setTimeout(() => {
			wx.navigateTo({
				url: '/pages/help/myFeedBack/myFeedBack',
			})
		}, 1500);

	},
})