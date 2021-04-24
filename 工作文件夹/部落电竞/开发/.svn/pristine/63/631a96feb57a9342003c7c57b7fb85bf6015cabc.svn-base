// modules/mypage/myrecharge/my-recharge.js
var api = require('../../../libs/http.js')
var strUtil = require('../../../libs/strUtil.js')
const app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    myBalance: "0",
    chargeAmoumtList: [],
    selectedAmount: '0.00',
    returnUrl: '',
    returnUrlEncodeFlag: 0,
    userInfo: null
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var _self = this;
    _self.setData({
      returnUrl: options.returnUrl,
      returnUrlEncodeFlag: options.returnUrlEncodeFlag,
      userInfo: app.getGlobalUserInfo()
    })
    console.log(app.getGlobalUserInfo())
    this.getChargePageData();
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

  getChargePageData: function () {
    api._postAuth("/charge/pageData").then(res => {
      console.log(res, "得到充值页所需数据")
      if (res.code == "200") {
        var _self = this
        _self.setData({
          myBalance: res.data.ableRecScore,
          chargeAmoumtList: res.data.chargeAmountList
        })
      } else {
        api._showToast(res.message, 2, "none")
      }
    }).catch(e => { })
  },

  selectChargeAmount: function (e) {
    var _self = this
    _self.setData({
      selectedAmount: e.currentTarget.dataset.amount
    });
  },

  goCharge: function () {
    var _self = this
    var chargeAmount = _self.data.selectedAmount
    if (chargeAmount == "0.00") {
      api._showToast("请先选择充值金额", 2, "none")
      return
    }
    this.dialog = this.selectComponent("#dialog");
    this.dialog.show();
  },

  handleConfirmDialog: function (e) {
    console.log("支付回调");
    var payIndex = e.detail.choosedValue
    this.confirmCharge(payIndex);
  },

  confirmCharge: function (payIndex) {
    var _self = this
    console.log('点击确认充值');
    var chargeAmount = _self.data.selectedAmount
    if (chargeAmount == "0.00") {
      api._showToast("请先选择充值金额", 2, "none")
      return
    }

    api._postAuth("/minicharge/toCharge", {
      chargeAmount: _self.data.selectedAmount,
      chargeWay:payIndex
    }).then(res => {
      console.log(res, "后端充值接口响应");
      if (res.code == "200") {
        if (payIndex == 1) {
          console.log("=====");
          //调用微信支付
          this.weixinPay(res);
        } else {
          //其他支付，可以直接成功
          this.selectComponent("#dialog").close()
          var updateMyBalance =  parseFloat(_self.data.myBalance)+parseFloat(chargeAmount)
          this.setData({
            selectedAmount: '0.00',
            myBalance: updateMyBalance.toFixed(2),
          })
          api._showToast("充值成功", 2, "none")
          _self.payReturn(_self);
        }
      } else {
        api._showToast(res.message, 2, "none")
        this.selectComponent("#dialog").close()
      }
    }).catch(e => { })

  },

  /**
   * 调用微信支付
   */
  weixinPay(res) {
    var _self = this
    wx.requestPayment({
      'timeStamp': res.data.timeStamp,
      'nonceStr': res.data.nonceStr,
      'package': "prepay_id=" + res.data.prepayId,
      'signType': "MD5",
      'paySign': res.data.paySign,
      'success': function (res) {
        console.log(res, "充值成功调用")
        if (res.errMsg == "requestPayment:ok") { // 调用支付成功
          api._showToast("充值成功", 2, "success")
         _self.payReturn(_self);
        } else if (res.errMsg == 'requestPayment:cancel') {
          conole.log('用户取消了支付');
        }
      },
      'fail': function (res) {
        console.log(res, "唤起微信支付页失败调用")
        api._showToast("取消支付", 2, "none")
      },
      'complete': function (res) {
        //console.log(res, "充值成功或失败都会调用")
      }
    })


  },
  payReturn(_self){
    var returnUrl = _self.data.returnUrl;
    if (_self.data.returnUrlEncodeFlag == 1) {
      returnUrl = strUtil.base64decode(returnUrl)
    } else if (_self.data.returnUrlEncodeFlag == 2) {
      var pages = getCurrentPages();
      var currPage = pages[pages.length - 1]; // 当前页
      var prevPage = pages[pages.length - 2]; // 上一个页面
      console.log(prevPage, 'prevPage');
      // 如果存在上一页 expertdetailpage
      if (prevPage && prevPage.route.indexOf("goodsDetai") > -1) {
        // 可以修改上一页的数据
        prevPage.selectComponent("#goods_exchange").selectComponent("#dialog").show();
        // 返回上一页
        wx.navigateBack();
        return;
      } else if (prevPage && prevPage.route.indexOf("expertdetailpage") > -1) {
        // 可以修改上一页的数据
        prevPage.selectComponent("#dialog").show();
        // 返回上一页
        wx.navigateBack();
      }
    }
    wx.redirectTo({
      url: returnUrl,
      fail: function () {
        wx.switchTab({
          url: returnUrl
        })
      }
    })
  }

})