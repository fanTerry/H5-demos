// modules/expertpage/expertpage/expert-page.js
var api = require('../../../libs/http.js')
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: "",//达人id，对应user表的id
    expertBaseInfo: Object, //达人基础信息
    articleList: [],//达人文章列表
    pageNo: 1,
    pageSize: 15,
    totalPages: 1,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      id: options.id,
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
    this.getExpertBaseData()
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
    this.getArticleList()
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },

  /**达人基础数据 */
  getExpertBaseData: function () {
    var _self = this;
    api._postAuth('/recExpert/baseInfo/' + _self.data.id, { showAuthPage: true, returnUrl: api.getCurrentPageUrlWithArgs()}).then(res => {
      _self.setData({
        expertBaseInfo: res.data 
      })
      _self.getArticleList()
      return null;
    }).catch(e => {
      console.log(e)
    })
  },

  /**获取达人文章列表 */
  getArticleList: function () {
    var _self = this;
    var pageNo = _self.data.pageNo;
    var pageSize = _self.data.pageSize;
    if (pageNo > _self.data.totalPages) {
      console.log("当前已是最后一页了,pageNo=" + pageNo + ",pageSize=" + pageSize);
      return;
    }
    api._postAuth("/recExpert/articleList", {
      pageNo: pageNo,
      pageSize: pageSize,
      userId: _self.data.expertBaseInfo.userId
    }).then(res => {
      if (res.code == "200") {
        var resData = res.data.articleList;
        if (resData.length > 0) {
          resData = _self.data.articleList.concat(resData);
          _self.setData({
            articleList: resData,
            pageNo: pageNo + 1,
            totalPages: res.data.totalPages
          });
        }
      }
      return null;
    }).catch(e => { })
  },

  /**关注/取消关注用户 */
  operateFollow: function () {
    var _self = this, type = 0, content = "";
    var toFollowUsrId = _self.data.expertBaseInfo.userId;
    var curUsrId = app.getGlobalUserInfo().usrId;
    if (curUsrId === toFollowUsrId) {
      api._showToast("您不能对自己进行关注或取消关注操作", 3);
      return;
    }
    var previousFollowFlag = _self.data.expertBaseInfo.followFlag;
    var previousBaseInfo = _self.data.expertBaseInfo 
    if (previousFollowFlag) {
      type = 0;//当前是关注-->则type是要取消关注
      content = "取消关注成功"
    } else {
      type = 1;
      content = "关注成功";
    }
    api._postAuth('/user/follow', { type: type, usrId: toFollowUsrId, noShowLoading: true }).then(res => {
      if (res.code == '200' && res.data) {//操作成功
        api._showToast(content, 3);
        previousBaseInfo.followFlag = !previousFollowFlag
        _self.setData({
          expertBaseInfo: previousBaseInfo 
        })
      }
    });
  },


})