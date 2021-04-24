// modules/expertpage/expertdetailpage/expert-detail-page.js
var api = require('../../../libs/http.js')
var strUtil = require('../../../libs/strUtil.js')
var WxParse = require('../../../wxParse/wxParse.js');
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    expertUsrId: '',
    articleId: '',
    expertBaseInfo: Object,
    articleInfo: Object,
    payInfo: Object,
    showPayDialog: false,
    agree: true,
    choosedPayWay: [
      // { index: 1, name: '电竞钱包支付', amount: '1.00', unit:'星星' },
      // { index: 2, name: '友宝钱包支付', amount: '2.00', unit: '友宝余额'},
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    var _self = this;
    _self.setData({
      expertUsrId: options.expertUsrId,
      articleId: options.articleId
    })
    api._postAuth('/recExpert/baseInfo/' + options.expertUsrId, {
      showAuthPage: true,
      returnUrl: api.getCurrentPageUrlWithArgs()
    }).then(res => {
      _self.setData({
        expertBaseInfo: res.data
      })
      return null;
    }).catch(e => {
      console.log(e)
    })
    this.getArticelInfo(this, options.articleId);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
    var _self = this;
    api._postAuth('/recExpert/baseInfo/' + _self.data.expertUsrId).then(res => {
      _self.setData({
        expertBaseInfo: res.data
      })
      return null;
    }).catch(e => {
      console.log(e)
    })
    this.getArticelInfo(_self, _self.data.articleId);
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  },

  //达人文章支付
  payArticle: function(choosedPayWay) {
    wx.showLoading({
      title: '支付中'
    });
    var _self = this;
    var articleId = _self.data.articleId;
    var expertUserId = _self.data.expertUsrId;
    var param = {
      noShowLoading: true,
      choosedPayWay: choosedPayWay
    };
    api._postAuth('/article/payArticle/' + articleId, param).then(res => {
      console.log(res, '支付响应');
      wx.hideLoading();
      if (res.code == '200' && res.data != null && res.data.successFlag) {
        if (choosedPayWay == 1) {//小程序支付
          this.weixinPay(res.data,_self);
          return;
        }
        this.dialog.close();
        api._showToast("支付成功", 2, 'success')
        this.setData({
          payInfo: res.data,
          showPayDialog: false
        })
        this.getArticelInfo(this, articleId);
      } else {
        api._showToast(res.message, 2)
      }
      return null;
    }).catch(e => {
      wx.hideLoading();
      api._showToast('支付异常', 2);
      console.log(e)
    })
  },

  closePayDialog: function() {
    this.setData({
      showPayDialog: false,
    })
  },

  getArticelInfo: function(_self, articelId) {
    var param = {
      noShowLoading: true
    };
    api._postAuth('/article/detail/' + articelId, param).then(res => {
      _self.setData({
        articleInfo: res.data
      })
      if (res.data.content) {
        WxParse.wxParse('wxViewPoint', 'html', res.data.content.viewPoint, this, 10);
      }
      return null;
    }).catch(e => {
      console.log(e)
    })
  },
  goToPay: function(e) {
    var _self = this;
    if (!_self.data.agree) {
      api._showToast("请先勾选同意协议", 2)
      return
    }
    this.dialog = this.selectComponent("#dialog");
    this.dialog.show()
  },

  handleConfirmDialog: function (e) {
    this.payArticle(e.detail.choosedValue);
  },

  agree: function() {
    var agree = !this.data.agree;
    this.setData({
      agree: agree,
    })
  },
  checkagreePage: function() {
    wx.navigateTo({
      url: '/modules/expertpage/expertagreepage/expert-agree-page'
    })
  },
  getPayWay: function() {
    var _self = this;
    var param = {
      articleId: _self.data.articleId
    };
    api._postAuth('/article/choosePayWay/', param).then(res => {
      _self.setData({
        choosedPayWay: res.data
      })
      console.log(this.data.choosedPayWay, "返回的可选支付方式")
    }).catch(e => {
      console.log(e)
    })
  },
  weixinPay: function (result,_self) {
    var _self = this
    console.log(result, result);
    if (!result.requestParams) {
      api._showToast("参数异常", 2)
      return;
    }
    wx.requestPayment({
      'timeStamp': result.requestParams.timeStamp,
      'nonceStr': result.requestParams.nonceStr,
      'package': "prepay_id=" + result.prepayId,
      'signType': "MD5",
      'paySign': result.requestParams.paySign,
      'success': function (res) {
        if (res.errMsg == "requestPayment:ok") { // 调用支付成功
          setTimeout(() => {
            _self.dialog.close();
            api._showToast("支付成功", 2, 'success')
            _self.getArticelInfo(_self, _self.data.articleId);
          }, 1000);
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
  }
})