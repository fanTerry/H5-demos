// modules/mypage/mywallet/my-wallet.js
const app = getApp();
var api = require('../../../libs/http.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
      walletLogData:[],
      balance:Number,
      pageNo:1,
      pageSize:10,
      hasNextPage:false ,// 默认没下一页
      listMap:[],
      noDataFlag:false,
      chargeFlag:false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var _self = this;
    this.getWalletPage(0,_self.data.pageNo,_self.data.pageSize);
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
      /*type==1 不查询钱包余额*/
      var _self = this;
      if (!_self.data.hasNextPage) {
          console.log("没有下一页")
          return;
      }
       var pageNo = _self.data.pageNo+1 ;
      this.getWalletPage(1,pageNo,_self.data.pageSize);

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

    getWalletPage:function( type,pageNo,pageSize){

        var _self = this;
        var url = "/usercenter/getWalletLog";
        api._postAuth(url,{
            pageNo:pageNo,
            pageSize:pageSize,
            type:type,
        }).then(res =>{
            if(res.code=="200"){

            // var walletLogList =  res.data.walletLogList;
            var balance =  res.data.balance;
            console.log(res.data)
            _self.setData({
                balance:balance,
                chargeFlag: res.data.chargeFlag
            })
            _self.setData({
                hasNextPage:res.data.hasNextPage,
                pageNo:pageNo,
            })

          /*  if (walletLogList.length>0) {
                // walletLogList = _self.data.walletLogData.concat(walletLogList)
                _self.setData({
                    // walletLogData:walletLogList,
                    pageNo:pageNo,
                })
            }*/


            var obj  =  res.data.listMap;
            var listMap = []
           for (var key in obj){
            var el = {
                time :key,
                dataList:obj[key]
            }
            listMap.push(el)

           }
           var  selfMap  = _self.data.listMap
           if (selfMap.length>0) {

               var index = selfMap.length - 1
               if (selfMap[index].time===listMap[0].time) {
                   selfMap[index].dataList =   selfMap[index].dataList.concat(listMap[0].dataList)
                   listMap.shift();
               }

           }
            for (var i = 0; i <listMap.length ; i++) {
                selfMap.push(listMap[i]);
            }
            _self.setData({
                listMap:selfMap,
            })
           // console.log("显示数组",_self.data.listMap);
            if (_self.data.listMap.length==0){
                this.setData({
                    noDataFlag:true,
                })
            }

        }



        });
    },



})