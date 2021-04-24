// component/newslist/newsimageitem/news-image-item.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    newsData: Object,
    selectedTag: Number
  },

  /**
   * 组件的初始数据
   */
  data: {
    errorImg:''
  },

  /**
   * 组件的方法列表
   */
  methods: {

    gotoDetail: function() {
      var _self = this;
      var toUrl = '/modules/articlepage/article-page?type=1&id=' + _self.data.newsData.aricleId;
      if (_self.data.newsData.type == 2) {
        toUrl = '/modules/articlepage/article-page?type=3&id=' + _self.data.newsData.aricleId;
      }
      wx.navigateTo({
        url: toUrl
      })
    },
    errorFunction: function(e) {
      console.log('加载图片异常');
      this.setData({
        errorImg:'https://rs.esportzoo.com/svn/esport-res/mini/images/default/default_video_img.png'
      });
    },
  }
})