// modules/store/list/goodsList.js
var api = require('../../../libs/http.js')
Page({

	/**
	 * 页面的初始数据
	 */
	data: {
    goodType: 4,
		myClass:'goodsList_Page',
    orderList: [],
    orderStatus: '',
    pageNo: 1,
    pageSize: 10, 
    hasMoreData: false,
    subMenuList: [],
    noDataFlag: false
	},

	/**
	 * 生命周期函数--监听页面加载
	 */
	onLoad: function (options) {
    this.setData({
      orderStatus: options.orderStatus,
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
    this.setData({
      pageNo: 1,
    })
    this.getOrderList();
    this.getShopOrderStatausList();
	},

	/**
	 * 生命周期函数--监听页面隐藏
	 */
	onHide: function () {
    	this.setData({
     		orderList: [],
    	})
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
    if (this.data.hasMoreData) {
      this.getOrderList();
    } else {
      console.log("没有更多数据了");
    }
	},

	/**
	 * 用户点击右上角分享
	 */
	onShareAppMessage: function () {

	},

  getOrderList: function() {
    var _self = this;
    var param = {
      pageNo: _self.data.pageNo,
      pageSize: _self.data.pageSize,
      shopOrderStatus: _self.data.orderStatus
    }
    console.log(param, "查询参数");
    api._postAuth('/shop/shopOrder', param).then(res => {
      console.log(res, "查询响应");
      if (res.code != "200") {
        api._showToast('系统异常', 2);
      } else {
        if (res.data.shopOrderList) {
          var orderList1 = _self.data.orderList.concat(res.data.shopOrderList);
          _self.setData({
            orderList: orderList1,
           });
          if (res.data.shopOrderList.length >= _self.data.pageSize) {
            _self.setData({
              hasMoreData: true,
              pageNo: _self.data.pageNo + 1,
            });
          }
        } else {
          if (_self.data.pageNo >= 2) {
            _self.setData({
              hasMoreData: false,
            });
          }
        }
      }

      if (_self.data.orderList.length <= 0) {
        _self.setData({
          noDataFlag: true,
        })
      }

     }).catch(e => {
      console.log(e)
    })
  },

  switchTab: function(e) {
    console.log("切换栏目：" + e.detail.name + "  " + e.detail.tag);
    if (this.data.orderStatus == e.detail.tag) {
      return;
    }
    this.setData({
      orderList: [],
      orderStatus: e.detail.tag,
      pageNo: 1,
      hasMoreData: false,
      noDataFlag: false,
    })
    this.getOrderList();
  },

  getShopOrderStatausList: function () {
    api._get('/shopOrder/orderstatus').then(res => {
      if (res.code != "200") {
        api._showToast('系统异常', 2);
      } else {
        this.setData({
          subMenuList: res.data,
        });
      }
    }).catch(e => {
      console.log(e)
    })
  },


})