// pages/hd/hd101/ruleslist/ruleslist.js
const app = getApp();
var api = require('../../../../../libs/http');
Component({

	/**
	 * 页面的初始数据
	 */
	data: {
		mark: 0,
		tablist: ["奖励榜单", "活动规则"],
		awardUserInfo: [],
		flagNum:1,
    giftList:[]
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
		// this.getAwardUserInfo()
    
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
	methods:{
		tabChange(event) {
			this.setData({
				mark:event.currentTarget.dataset.index
			})
      if (event.currentTarget.dataset.index=1){
        this.getGiftList();
      }
      
		},

		getAwardUserInfo() {
			let param = {};
			param.showUser = false;
			param.clientType=5;
			api._postAuth("/hd101/listUserGift", param)
				.then(rsp => {
					console.log(rsp, param);   



					const dataResponse = rsp;
					if (dataResponse.code == 200) {
						this.setData({awardUserInfo:dataResponse.data});
						console.log(this.data.awardUserInfo);
						// let str = [];
						// this.data.awardUserInfo.forEach((item, index) => {
						// 	if (item.showType == 0) {
						// 		str[index] = JSON.parse(item.giftProp);
						// 		item.subjectFristPrize = str[index].subjectFristPrize;
						// 	} else {
						// 		str[index] = null;
						// 		item.subjectFristPrize = str[index];
						// 	}
						// });
					}
				})
				.catch(error => {
					console.log(error);
				});
		},
    getGiftList() {
      api._postAuth("/subject/giftList").then(rsp => {
        const dataResponse = rsp;        
        if (dataResponse.code == "200") {            
            this.setData({
              giftList: dataResponse.data.giftList
            });
            console.log(999999+this.giftList);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },

	}
})