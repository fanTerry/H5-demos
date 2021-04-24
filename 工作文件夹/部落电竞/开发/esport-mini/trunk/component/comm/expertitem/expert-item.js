// component/comm/expertitem/expert-item.js
var api = require('../../../libs/http.js')
Component({
  /**
   * 组件的属性列表
   */
  properties: {
      followUseInfo:Object,
  },

  /**
   * 组件的初始数据
   */
  data: {
      followStaus:Number,
      fans:Number,
  },


    ready(){
      this.setData({
          fans:this.data.followUseInfo.fans,
          followStaus:this.data.followUseInfo.followStatus,
      })
    },

  /**
   * 组件的方法列表
   */
  methods: {

      //取消关注
      cancelFollow:function (e) {

          api._showToast("取消关注",1);
          var useInfo = this.data.followUseInfo
          console.log("组件",useInfo);
          useInfo.followStaus = 0;
          this.setData({
              followStaus:0,
              fans:this.data.fans-1,
          })
          // this.sendData(useInfo.userId);
          this.followApi(e.currentTarget.dataset.usrid,this.data.followStaus)
      },

      follow:function (e) {

          api._showToast("关注成功",1);
          var useInfo = this.data.followUseInfo
          useInfo.followStaus = 1;
          this.setData({
              followStaus:1,
              fans:this.data.fans+1,
          })
          this.followApi(e.currentTarget.dataset.usrid,this.data.followStaus)

      },

      followApi:function (usrId,type) {
          console.log("用户ID", usrId);
          console.log("关注状态", type);
          var url = "/user/follow";
          api._postAuth(url,{
              type: type,
              usrId: usrId,
              noShowLoading:1,
          }).then(res =>{
              console.log("关注接口",res);
          if(res.code=="200"){

          }else {
              api._showToast(res.message,1.5);
          }

      }).catch(e => { })
      }



  }
})
