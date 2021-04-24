var api = require('../../../libs/http.js')
const app = getApp()
Component({

  /**组件的属性列表*/
  properties: {
    commentItem: Object,
    hotFlag: Number
  },

  /**组件的初始数据*/
  data: {
    showMoreFlag: false,
    showLen: 4,
    dataItem: Object,//页面渲染传进来的对象,操作这个
    subCommentList: [],
    subCommentListLen: 0//当前评论子评论总条数
  },

  ready() {
    console.log(this.properties.hotFlag);
    var pageDataItem = this.properties.commentItem;//当前页的数据
    var storeUp = wx.getStorageSync('comment_up_' + pageDataItem.commentId);
    var storeFollow = wx.getStorageSync('comment_follow_' + pageDataItem.usrId);
    if (storeUp) pageDataItem.upFlag = true;
    if (storeFollow) pageDataItem.followFlag = true;
    this.reflushData(pageDataItem);
  },

  /** 组件的方法列表,内部方法建议以下划线开头*/
  methods: {

    /**关注/取消关注用户 */
    _toFollow: function () {
      var _self = this, item = this.data.dataItem, type = 0, content = "关注成功";
      var toFollowUsrId = item.usrId;
      var curUsrId = app.getGlobalUserInfo().usrId;
      if (curUsrId === toFollowUsrId) {
        api._showToast("您不能关注自己", 3);
        return;
      }
      var storeFollow = wx.getStorageSync('comment_follow_' + item.usrId);//获取全部关注的usrId
      console.error(storeFollow, 'storeFollow')
      if (item.followFlag) {
        type = 0;//当前是关注-->则type是要取消关注
        item.ups -= 1;
        content = "取消关注"
        wx.removeStorageSync('comment_follow_' + item.usrId);//删除取消关注的usrId 
      } else {
        type = 1;
        item.ups += 1;
        wx.setStorageSync('comment_follow_' + item.usrId, true);//新增关注的usrId
      }
      item.followFlag = !item.followFlag;
      api._postAuth('/user/follow', { type: type, usrId: toFollowUsrId, noShowLoading: true }).then(res => {
        console.log(res, '操作')
        if (res.code == '200' && res.data) {//操作成功
          api._showToast(content, 2);
          _self.setData({
            dataItem: item
          })
        }
      });
    },
    /**点击评论的回复 */
    _toReply(e) {
      console.log(e)
      var replyfathercommentid = e.currentTarget.dataset.replyfathercommentid;
      var replyfathername = e.currentTarget.dataset.replyfathername;
      console.log(replyfathercommentid)
      this.triggerEvent('myevent', { replyfathercommentid: replyfathercommentid, replyfathername: replyfathername });
    },

    /**赞或者取消赞 */
    _toUp: function () {
      var _self = this, item = this.data.dataItem, type = 0, content = "点赞成功";
      var storeUp = wx.getStorageSync('comment_up_' + item.commentId);//获取全部点赞的comment_id
      console.error(storeUp, 'storeUp')
      if (item.upFlag) {
        type = 0;//当前是赞-->则type是要取消赞
        item.ups -= 1;
        content = "取消点赞"
        wx.removeStorageSync('comment_up_' + item.commentId);//删除取消赞的comment_id 
      } else {
        type = 1;
        item.ups += 1;
        wx.setStorageSync('comment_up_' + item.commentId, true);//新增赞的comment_id
      }
      item.upFlag = !item.upFlag;
      api._postAuth('/cmsComment/ups', { type: type, id: item.commentId, noShowLoading: true }).then(res => {
        console.log(res, '操作')
        if (res.code == '200' && res.data) {//操作成功
          api._showToast(content, 2);
          _self.setData({
            dataItem: item
          })
        }
      });


    },

    /**收起或者查看更多 */
    _showOrHideMore(e) {
      var _self = this;
      var nowFlag = !this.data.showMoreFlag, subCommentList = JSON.parse(JSON.stringify(this.data.subCommentList));
      this.setData({
        showMoreFlag: nowFlag
      })
      if (nowFlag) {//true:展开更多,false 收起
        this.setData({
          subCommentList: this.data.dataItem.subCommentList//所有子评论记录
        })
      } else {
        this.setData({
          subCommentList: subCommentList.splice(0, _self.data.showLen)
        })

      }
    },

    /**刷新组件的数据 */
    getData(data) {
      console.log(data, "刷新数据")
      data.upFlag = this.data.dataItem.upFlag;
      data.followFlag = this.data.dataItem.followFlag;
      this.reflushData(data);
    },

    /**刷新当前组件数据 */
    reflushData(data) {
      var subCommentListCp = JSON.parse(JSON.stringify(data.subCommentList));
      var subCommentListLen = 0;
      if (data && data.subCommentList) {
        subCommentListLen = data.subCommentList.length;
      }
      this.setData({
        dataItem: data,
        subCommentList: data.subCommentList,
        subCommentListLen: subCommentListLen
      })
      if (subCommentListLen >= this.data.showLen) {
        this.setData({
          subCommentList: subCommentListCp.splice(0, this.data.showLen)
        })
      }
    }
  }
})
