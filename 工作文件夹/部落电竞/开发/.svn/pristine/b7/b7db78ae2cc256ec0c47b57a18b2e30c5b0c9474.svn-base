// modules/store/order/orderDetails.js
var api = require('../../../libs/http.js')

Page({

	/**
	 * 页面的初始数据
	 */
	data: {
    orderId: '',
    detail: Object,
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
    var _self = this;
    _self.setData({
      orderId: options.orderId
    })
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
    this.getOrderDetail();
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

  getOrderDetail: function () {
    var _self = this;
    api._postAuth('/shopOrder/orderdetail/' + _self.data.orderId).then(res => {
      _self.setData({
        detail: res.data
      })
      console.log(this.data.detail, "返回的订单详情数据")
    }).catch(e => {
      console.log(e)
    })
  },
  
  copyCommon: function(event) {
    var content = event.currentTarget.dataset.copycontent;
    console.log(content, "要复制的内容");
    wx.setClipboardData({
      data: content,
      success: function (res) {
        api._showToast('复制成功', 2);
        setTimeout(function(){
          wx.hideToast();
        },2000)
      }
    });
  }

})