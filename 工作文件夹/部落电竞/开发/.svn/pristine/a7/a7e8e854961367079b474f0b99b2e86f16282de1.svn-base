const app = getApp();
var api = require('../../libs/http.js')
var strUtil = require('../../libs/strUtil')
Page({
    data: {
        //判断小程序的API，回调，参数，组件等是否在当前版本可用。
        canIUse: wx.canIUse('button.open-type.getUserInfo')
    },
  onLoad: function (options) {
      var _self = this;
      _self.setData({
        returnUrl: options.returnUrl
      })
    },
    bindGetUserInfo: function (e) {
      var _self = this;
      console.log(e);
      if (e.detail.userInfo) { //用户按了允许授权按钮,插入登录的用户的相关信息到数据库
          wx.login({
            success: function (loginRes) {
              if (loginRes) {
                //获取用户信息
                wx.getUserInfo({
                  withCredentials: true,//非必填  默认为true
                  success: function (infoRes) {
                    console.log(infoRes, '>>>');
                    //请求服务端的登录接口
                    api._post('/login', {
                      code: loginRes.code,//临时登录凭证
                      rawData: infoRes.rawData,//用户非敏感信息
                      signature: infoRes.signature,//签名
                      encrypteData: infoRes.encryptedData,//用户敏感信息
                      iv: infoRes.iv//解密算法的向量
                    }).then(res => {
                        if(res.code==200){
                          app.globalData.userInfo = res.data;
                          wx.setStorageSync('userInfo',JSON.stringify(res.data));
                          wx.setStorageSync('sid',res.data.sid);
                        }else{
                          api._showToast("登录异常,稍后重试", 2);
                        }
                       // wx.navigateBack();
                      var returnUrl = strUtil.base64decode(_self.data.returnUrl);
                      console.log(returnUrl,'授权需要回调的地址');
                      wx.redirectTo({
                        url: returnUrl,
                        fail: function () {
                          console.log('跳tab页');
                          wx.switchTab({
                            url: returnUrl
                          })
                        }
                      })
                    }).catch(e => {
                      console.log(e)
                    })
                  }
                });
              } else {
      
              }
            }
          });
      } else {
          //用户按了拒绝按钮
          wx.showModal({
              title:'提示',
              content:'您点击了拒绝授权，为了更好的体验,请授权之后再进入',
              showCancel:false,
              confirmText:'返回授权',
              success:function(res){
                  if (res.confirm) {
                      console.log('用户点击了“返回授权”')
                  } 
              }
          })
      }
  }
})