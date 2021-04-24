// modules/store/my_exchange/my_exchange.js
var api = require('../../../libs/http.js')
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
    showPopUp: false, 
    shopOrderStatusList: [],
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {

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
    this.getShopOrderStatausList();
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
	
	goToAddressManage:function(){
		wx.navigateTo({
			url: '/pages/store/address/manage',
		})
	},

  goToOrderList: function (event) {
    var orderStatus = event.currentTarget.dataset.orderstatus;
    wx.navigateTo({
      url: '/pages/store/my_exchange/goodsList?orderStatus=' + orderStatus
    })
  },

  getShopOrderStatausList: function() {
    api._get('/shopOrder/orderstatus').then(res => {
      if (res.code != "200") {
        api._showToast('系统异常', 2);
      } else {
        this.setData({
          shopOrderStatusList: res.data,
        });
      }
    }).catch(e => {
      console.log(e)
    })
  },

  contactCustomerService: function() {
    this.setData({
      showPopUp: true
    })
  },

  closePopUp: function() {
    this.setData({
      showPopUp: false
    })
  }

})