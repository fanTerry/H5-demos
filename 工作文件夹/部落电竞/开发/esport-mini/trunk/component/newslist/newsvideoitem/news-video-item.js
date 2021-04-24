// component/newslist/newsvideoitem/news-video-item.js
var api = require('../../../libs/http.js')
const app = getApp();
Component({
  /**
   * 组件的属性列表
   */
  properties: {
      newsData: Object,

  },


  /**
   * 组件的初始数据
   */
  data: {
      tab_image:"block",
      platform:Number,
      curr_id: ''
  },

  attached() { },
  ready() {
      const res = wx.getSystemInfoSync()
      var platform=1;
      if (res.platform == "android"){
          platform =1
      }else  {
          platform =2
      }
      this.setData({
          platform:platform
      })
    console.log("platform",this.data.platform);
  },
  detached(){
        // app.event.off('afterUpSuccess', this.afterUpSuccess)
    },
  /**
   * 组件的方法列表
   */
  methods: {
      videoPlay(e) {
        this.setData({
          curr_id: e.currentTarget.dataset.id,
        })
        console.log("视频播放事件触发");
        var videoContextPrev = wx.createVideoContext('video' + this.properties.newsData.aricleId, this)
        videoContextPrev.play();
        this.updateVideoView();
      },
      videoErrorCallback:function (e) {
          console.log("视频错误信息：",e.detail.errMsg);
      },

      puaseVideo:function () {
          var videoContextPrev = wx.createVideoContext('video' + this.properties.newsData.aricleId ,this)
          videoContextPrev.pause();
      },

      bindPlay:function () {
          this.setData({
              tab_image:"none",
          })
          this.updateVideoView();
          console.log("视频播放事件触发");
          var videoContextPrev = wx.createVideoContext('video' + this.properties.newsData.aricleId ,this)
          videoContextPrev.play();


      },

      pauseOtherPlay:function () {
          console.log("暂停其他播放视频");
          var videoContextPrev = wx.createVideoContext('video' + this.properties.newsData.aricleId ,this)
          // 所有要带到主页面的数据，都装在eventDetail里面
          var eventDetail = {
              aricleId:this.properties.newsData.aricleId,
          }
          // 触发事件的选项 bubbles是否冒泡，composed是否可穿越组件边界，capturePhase 是否有捕获阶段
          var eventOption = {
              composed: true,
          }

          this.triggerEvent('myplay', eventDetail, eventOption)

      },

      afterUpSuccess:function (aricleId) {
          console.log("页面通讯");
          console.log(aricleId);
          console.log(this.properties)
          if (this.properties.newsData.aricleId ==aricleId ) {
              console.log(aricleId);
              console.log(   this.properties.newsData.discussNum+1)
          }
      },
    endVideo: function () {
      this.setData({
        curr_id:''
      })
    },

      updateVideoView() {

         var video  =  this.data.newsData;
          api._postAuth("/cmsContent/addViews", {
              id: video.aricleId,
              noShowLoading:true,
          }).then(res => {
              if (res.code == "200") {
                  video.seeNum = res.data;
                  /*更新阅读次数*/
                  this.setData({
                      newsData: video,
                  });
              }
          }).catch(e => {

          })




      }


  }

})
