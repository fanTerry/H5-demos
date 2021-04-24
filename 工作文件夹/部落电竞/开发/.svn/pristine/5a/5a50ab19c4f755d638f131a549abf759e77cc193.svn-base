// component/expert/expertheader/expert-header.js
var api = require('../../../libs/http')
const app = getApp()
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    expertBaseInfo: Object
  },

  /**
   * 组件的初始数据
   */
  data: {
    expertBaseInfo:Object
  },

  ready(){
    this.setData({
      expertBaseInfo: this.properties.expertBaseInfo
    })
  },
  /**
   * 组件的方法列表
   */
  methods: {
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
          previousBaseInfo.fans =   previousBaseInfo.fans -1
      } else {
        type = 1;
        content = "关注成功";
          previousBaseInfo.fans =   previousBaseInfo.fans +1
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


  }
})
