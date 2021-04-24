// component/expert/expertbanner/expert-banner.js
Component({
  /**
   * 组件的属性列表
   */
  properties: {
      adList:Array,
  },

  /**
   * 组件的初始数据
   */
  data: {

  },

  /**
   * 组件的方法列表
   */
  methods: {
      onSwiperChange: function (evt) {


      },

      goDetailPage: function (e) {
          var curIndex = e.currentTarget.dataset.index;
          var swiperArr = this.properties.adList;
          console.log("轮播图跳转")
          wx.navigateTo({
              url: swiperArr[curIndex].location
          });
      },

  }
})
