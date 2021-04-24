var api = require('../../../libs/http.js')
var WxParse = require('../../../wxParse/wxParse.js');
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    swiperOpt: {
      indColor: "#fff",
      indActColor: "#fea21b", //#075fe7
    },
    bannerList: [],
    goodsId: null,
    popChangeFlag:false,
    showPopUp:false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    this.setData({
      goodsId: options.goodsId
    })
    this.getGoodsDetail();
  },

  getGoodsDetail() {
    api._post("/shop/detail/" + this.data.goodsId)
      .then(res => {
        console.log(res);
        var bannerList = [];
        if ((res.code = "200")) {
          this.goodsDetail = res.data;
          if (res.data.imageList) {
            res.data.imageList.forEach(item => {
              var temp = {};
              temp.location = "";
              temp.picUrl = item;
              bannerList.push(temp);
            });
            console.log(bannerList, "this.bannerList");
            // if (this.$refs.banner) {
            //   this.$refs.banner.update("更新");
            // }
          }
          WxParse.wxParse('goodsDesc', 'html', res.data.description, this, 10);
          WxParse.wxParse('goodsPurchaseNote', 'html', res.data.purchaseNote, this, 10);
          WxParse.wxParse('goodsServiceNote', 'html', res.data.serviceNote, this, 10);

          this.setData({
            goodsDetail: res.data,
            bannerList: bannerList
          });
        }
      })
      .catch(error => {
        console.log(error);
      });
  },
  showPop() {
    console.log('sss');
   // this.popChangeFlag = true;
    this.setData({
      popChangeFlag:true
    });
    this.selectComponent("#goods_exchange").setData({
      step1Flag:true
    });
    // if (this.$refs.popSon) {
    //   this.$refs.popSon.step1Flag = true;
    //   this.$refs.popSon.getUserAddress();
    // }
  },
  contactCustomerService: function () {
    this.setData({
      showPopUp: true
    })
  },

  closePopUp: function () {
    this.setData({
      showPopUp: false
    })
  },

   /**
   * 收集表单ID，用于发送模板消息
   */
  formSubmit:function(event){
    console.log("000000");
    console.log(event.detail.formId);
    this.showPop();
    var param = {}
    param.formId = event.detail.formId
    param.sourceType = "商城兑换" //收集商城模板消息ID
    param.userId = app.getGlobalUserId()
    api._postAuth("/wxapp/saveFormid", param).then(res => {
			if (res.code == "200") {
				console.log("添加完成");
			} else {
				console.log(res.message);
			}
		}).catch(e => { })


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

  }
})