const app = getApp();
const api = require('../../../libs/http.js')
const baseUrl = api.baseUrl;
Page({

  /**
   * 页面的初始数据
   */
  data: {
      tempFilePaths:[],
      showNeedFlag:Number,
      nickName:String,

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

        this.setData({
            nickName:options.nickName,
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

   /**表单提交事件 */
  formSubmit(e) {
      console.log('form发生了submit事件，携带数据为：', e.detail.value)

      var that = this;

       var expertName = e.detail.value.expertName
       if (expertName==''){
           api._showToast("达人号名称不能为空",1);
           that.setData({
               showNeedFlag:1,
           })
           return
       }

       var introduction = e.detail.value.introduction
       if (introduction==''){
           api._showToast("达人简介不能为空",1);
           that.setData({
               showNeedFlag:2,
           })
           return
       }

       var reason = e.detail.value.reason
       if (reason==''){
           api._showToast("申请理由不能为空",1);
           that.setData({
               showNeedFlag:3,
           })
           return
       }

       var realName = e.detail.value.realName
       if (realName==''){
           api._showToast("运营者姓名不能为空",1);
           that.setData({
               showNeedFlag:4,
           })
           return
       }
       var cardNo = e.detail.value.cardNo
       if (cardNo==''){
           api._showToast("身份证号码不能为空",1);
           that.setData({
               showNeedFlag:5,
           })
           return
       }
       if (that.data.tempFilePaths.length == 0) {
           api._showToast("缺少身份证正面照",1);
           return
       }
       if (expertName != '' && introduction != '' && reason != ''
          && realName != '' && cardNo != '' && that.data.tempFilePaths.length > 0) {
          if ( !this.isCardNo(cardNo)) {
              api._showToast("请填写正确格式的身份证",1);
              that.setData({
                  showNeedFlag:5,
              })
          }else if (expertName.length>8) {
              api._showToast("达人号名称不超过8个字符",1);
              that.setData({
                  showNeedFlag:1,
              })
          }else if (reason.length>100){
              api._showToast("申请理由不超过100个字符",1);
              that.setData({
                  showNeedFlag:3,
              })

          } else if (introduction.length>100) {
              api._showToast("达人简介不超过100个字符",1);
              that.setData({
                  showNeedFlag:2,
              })
          }else {
              var param = {
                  expertName:expertName,
                  introduction:introduction,
                  reason:reason,
                  realName:realName,
                  cardNo:cardNo,
              }
              this.submitData(param);


          }



      } else {
          api._showToast("请填写完整信息",1);
      }

    },

    /**提交数据 */
   submitData:function(param){

       var that = this;
       var url  = baseUrl +"/expert/apply";

       var usrInfo = app.getGlobalUserInfo();
       if (!usrInfo){
           api._showToast("用户未登录",1);
       }
        param.userId = usrInfo.usrId
        wx.showLoading({
            title: '正在提交申请'
        });

       console.log(usrInfo)
       wx.uploadFile({
           url: url,
           filePath: that.data.tempFilePaths[0],
           name: 'file',
           formData: param,
           success(res) {
               wx.hideLoading();
               const data = JSON.parse(res.data)
               if (data.code=='200') {
                   // api._showToast("提交成功，请等待审核",1);
                   that.submitSuccess();

               }else{
                   console.log(data)
                   api._showToast(data.message,1);
               }


           },
       })

   },

    /**选取图片 */
  chooseImg: function (e) {
        var that = this;
        wx.chooseImage({
            count: 1,
            sizeType: ['original'],
            sourceType: ['album', 'camera'],
            success: function (res) {
                var tempFilesSize = res.tempFiles[0].size;  //获取图片的大小，单位B
                if(tempFilesSize <= 5000000){   //图片小于或者等于2M时 可以执行获取图片
                    var tempFilePaths = res.tempFilePaths[0]; //获取图片
                    that.setData({
                        tempFilePaths: res.tempFilePaths
                    });
                }else {
                    api._showToast("不能超过5M",1);
                }

                console.log(res.tempFilePaths);
            }
        })
    },


    previewImage:function () {
        wx.previewImage({
            current: 'https://rs.esportzoo.com/svn/esport-res/mini/images/imagefile.jpg', // 当前显示图片的http链接
            urls: ['https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1557726538856&di=5e1d5684f1dc2c7d423a04163cadb57a&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180928%2F497ed60f31e944a6a2d003403314ee9d.png'] // 需要预览的图片http链接列表
        })
    },

    /**身份证号码校验 */
    isCardNo: function (card) {
        //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
        var reg = /(^\d{15}$)|(^\d{17}(\d|X|x)$)/;
        if (reg.test(card) === false) {
            return false;
        }
        return true;
    },

    submitSuccess:function () {

        wx.showToast({
            title: '请等待审核',
            icon: 'success',
            mask:true,
            duration: 1500,
        })
        setTimeout(function () {
            wx.switchTab({
                url: '/pages/my/my'
            });
        },1500);

    }

})