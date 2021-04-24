var api = require('../../libs/http.js')
var commentService = require('../../service/CommentService')
var WxParse = require('../../wxParse/wxParse.js');
const app = getApp()
Page({
  /**页面的初始数据*/
  data: {
    id: "",//当前文章id
    type: "",//资讯类型:1:长文,2:短文,3:视频
    cmsDetail: Object,//当前资讯详情
    placeholder: "说点什么...",
    contentValue: '',//评论内容
    commentList: [],//文章评论列表
    upTopCommentList: [],//置顶评论
    pageNo: 1,
    pageSize: 10,
    totalPages: 1,
    ready: false,
    showLineInput: true, //显示单行文本框
    playFlag: false,
    platform:1,
    tab_image: "block"
  },

  /**生命周期函数--监听页面加载 */
  onLoad: function (options) {
    this.setData({
      id: options.id,
      type: options.type
    })
    this.reflushData(this, true)
    const res = wx.getSystemInfoSync()
    var platform = 1;
    if (res.platform == "android") {
      platform = 1
    } else {
      platform = 2
    }
    this.setData({
      platform: platform
    })
  },

  onShow: function () {
    this.reflushData(this, false);
  },


  /**热门评论 */
  upTopComment: function () {
    var _self = this;
    api._postAuth('/cmsComment/upTopComment', { contentId: _self.data.id }).then(res => {
      console.log(res, '置顶评论');
      if (res.code == "200" && res.data) {
        _self.setData({
          upTopCommentList: res.data
        })
      } else {
        api._showToast("查询热门评论失败,稍后重试", 2);
      }
      return null;
    }).catch(e => {
      console.log(e)
    })
  },

  /**点赞/取消赞 */
  operateUp: function () {
    var _self = this, type = 0, content = "点赞成功";
    var curCmsDetail = _self.data.cmsDetail;
    if (curCmsDetail.upFlag) {
      type = 0;//当前是赞-->则type是要取消赞
      curCmsDetail.ups -= 1;
      content = "取消点赞";
      wx.removeStorageSync('detail_up_' + _self.data.id);
    } else {
      type = 1;
      curCmsDetail.ups += 1;
      wx.setStorageSync('detail_up_' + _self.data.id, true);
    }
    curCmsDetail.upFlag = !curCmsDetail.upFlag;
    console.log(_self.data.cmsDetail);
    api._postAuth('/cmsContent/ups', { type: type, id: _self.data.id, noShowLoading: true }).then(res => {
      console.log(res, '操作')
      if (res.code == '200' && res.data) {//操作成功
        api._showToast(content, 2);
        _self.setData({
          cmsDetail: curCmsDetail
        })
      }
    });
  },

  /**关注/取消关注用户 */
  operateFollow: function () {
    var _self = this, type = 0, content = "关注成功";
    var toFollowUsrId = _self.data.cmsDetail.authorId;
    var curUsrId = app.getGlobalUserInfo().usrId;
    console.log(app.getGlobalUserInfo(), toFollowUsrId)
    if (curUsrId === toFollowUsrId) {
      api._showToast("您不能关注自己", 3);
      return;
    }
    var curCmsDetail = _self.data.cmsDetail;
    if (curCmsDetail.followFlag) {
      type = 0;//当前是关注-->则type是要取消关注
      content = "取消关注"
      wx.removeStorageSync('detail_follow_' + _self.data.cmsDetail.authorId);//删除取消关注的usrId 
    } else {
      type = 1;
      wx.setStorageSync('detail_follow_' + _self.data.cmsDetail.authorId, true);//新增关注的usrId
    }
    curCmsDetail.followFlag = !curCmsDetail.followFlag;
    console.log(_self.data.cmsDetail);
    api._postAuth('/user/follow', { type: type, usrId: toFollowUsrId, noShowLoading: true }).then(res => {
      console.log(res, '操作')
      if (res.code == '200' && res.data) {//操作成功
        api._showToast(content, 2);
        _self.setData({
          cmsDetail: curCmsDetail
        })
      }
    });
  },

  /**收藏/取消收藏 */
  operateCollect: function () {
    var _self = this, type = 0, content = "收藏成功";
    var curCmsDetail = _self.data.cmsDetail;
    if (curCmsDetail.favoritesFlag) {
      type = 0;//当前是收藏-->则type是要取消收藏
      curCmsDetail.favorites -= 1;
      content = "取消收藏";
      wx.removeStorageSync('detail_favorites_' + _self.data.id);
    } else {
      type = 1;
      curCmsDetail.favorites += 1;
      wx.setStorageSync('detail_favorites_' + _self.data.id, true);
    }
    curCmsDetail.favoritesFlag = !curCmsDetail.favoritesFlag;
    console.log(_self.data.cmsDetail);
    api._postAuth('/cmsContent/favorites', { status: type, id: _self.data.id, noShowLoading: true }).then(res => {
      if (res.code == '200' && res.data) {//操作成功
        api._showToast(content, 2);
        _self.setData({
          cmsDetail: curCmsDetail
        })
      }
    });
  },

  /**获取评论列表 */
  getCommentsList: function () {
    commentService.getCommentPage(this);
  },

  /**激活回复:拉起输入框并设置评论的对象 */
  replyFocus: function (e) {
    var fatherCommentId = e.detail.replyfathercommentid;//要回复的评论
    var replyfathername = e.detail.replyfathername
    this.setData({
      replyFatherCommentId: fatherCommentId,
      commentFocus: true,
      showLineInput: false,
      placeholder: '回复' + replyfathername
    });
  },

  /**保存评论 */
  saveComment: function (e) {
    console.log(e, '保存');
    var _self = this;
    // 获取评论回复的对象
    var fatherCommentId = e.currentTarget.dataset.replyfathercommentid;
    var params = _self.getSaveCommentParam(_self, e);
    var curCmsDetail = _self.data.cmsDetail;
    api._postAuth('/cmsComment/publish', params).then(res => {
      console.log(res.data.commentId, '评论返回值');
      if (res.code == "200" && res.data) {
        api._showToast("评论成功", 2, 'success');
        if (!fatherCommentId) {//一级评论刷新评论列表,拿第一页渲染
          curCmsDetail.comments += 1;
          _self.setData({
            contentValue: "",//清空评论内容
            replyFatherCommentId: null,//清空评论对象
            commentList: [],
            showLineInput: true,
            pageNo: 1,
            cmsDetail: curCmsDetail
          });
          commentService.getCommentPage(_self);
        } else {
          _self.setData({
            contentValue: "",//清空评论内容
            replyFatherCommentId: null,//清空评论对象
            placeholder: "说点什么...",
            showLineInput: true,
          });
          console.log(fatherCommentId, '二级评论怎么处理');
          _self.selectComponent("#comment_" + fatherCommentId).getData(res.data);
          if (_self.selectComponent("#hot_comment_" + fatherCommentId)){
            _self.selectComponent("#hot_comment_" + fatherCommentId).getData(res.data);
          }
        }
      } else {
        api._showToast("评论失败,稍后重试", 2);
      }
      return null;
    }).catch(e => {
      console.log(e)
    })
  },

  /**底部加载数据 */
  onReachBottom: function () {
    commentService.getCommentPage(this);
  },

  /**封装发表评论需要的参数(包括评论和回复) */
  getSaveCommentParam: function (_self, e) {
    var fatherCommentId = e.currentTarget.dataset.replyfathercommentid;
    var content = _self.data.contentValue;
    var params = {
      commentLevel: 1,//评论层级
      contentId: _self.data.id,
      contentTypeId: _self.data.cmsDetail.typeId,
      comment: content,
      noShowLoading: true
    }
    if (fatherCommentId) {
      params.commentParentId = fatherCommentId;
      params.commentRootId = fatherCommentId;
      params.commentLevel = 2;
    }
    return params;
  },

  /**刷新数据 */
  reflushData: function (_self, flag) {
    api._postAuth('/cmsContent/detail/' + _self.data.id, { showAuthPage: flag, returnUrl: api.getCurrentPageUrlWithArgs() }).then(res => {
      var storeUp = wx.getStorageSync('detail_up_' + _self.data.id);
      var storeFollow = wx.getStorageSync('detail_follow_' + res.data.authorId);
      var storeFav = wx.getStorageSync('detail_favorites_' + _self.data.id);
      if (storeUp && res.data.upFlag) res.data.upFlag = true;
      if (storeFollow && res.data.followFlag) res.data.followFlag = true;
      if (storeFav && res.data.followFlag) res.data.followFlag = true;
      _self.setData({
        cmsDetail: res.data,
        ready: true
      })
      if (_self.data.type != 3) {
        WxParse.wxParse('article', 'html', res.data.content, this, 10);
      }
      console.log(res, '详情页数据')
    }).catch(e => {
      console.log(e)
    })
    if (flag) {
      _self.getCommentsList();
    }
    _self.upTopComment();
  },

  bindDTextAreaFocus: function (e) {
    this.setData({
      commentFocus: true,
      showLineInput: false
    });
  },

  bindDTextAreaBlur: function (e) {
    var contentValue = e.detail.value;
    if (contentValue == '') {
      this.setData({
        showLineInput: true
      });
    }
    this.setData({
      commentFocus: false,
      contentValue: contentValue
    });
  },

  /**绑定输入内容 */
  contentInput: function (e) {
    this.setData({
      contentValue: e.detail.value
    })
  },

  videoPlay(e) {
    console.log("视频播放事件触发");
    this.setData({
      playFlag: true,
      tab_image: "none"
    });
    var videoContextPrev = wx.createVideoContext('myVideo', this)
    videoContextPrev.play();
  },
  videoErrorCallback: function (e) {
    console.log("视频错误信息：", e.detail.errMsg);
  },

  puaseVideo: function () {
    var videoContextPrev = wx.createVideoContext('myVideo', this)
    videoContextPrev.pause();
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
})