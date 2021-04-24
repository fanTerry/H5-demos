// modules/reportpage/mdexpert/md-expert.js
const app = getApp();
var api = require('../../../libs/http.js')
Component({
  /**
   * 组件的属性列表
   */
  properties: {

      matchId:Number,

  },

  /**
   * 组件的初始数据
   */
  data: {
      articleList:[],
      pageNo:0,
      pageSize:10,
      haseNext:true,
      noDataFlag:false,

  },
    lifetimes: {

        ready() {
            // 加载达人文章
            this.loadExpertArticle(this.properties.matchId);

        },
    },

  /**
   * 组件的方法列表
   */
  methods: {

      loadExpertArticle:function (matchId) {
          if (!this.data.haseNext) {
              return;
          }
          var that = this;
          var pageNo = that.data.pageNo+1;
          var pageSize = that.data.pageSize;

          var url = "/expert/articleList";
          api._postAuth(url,{
              pageNo:pageNo,
              pageSize:pageSize,
              matchId:matchId,
          }).then(res => {
              if(res.code=="200"){
              if (res.data.length>0){

                  var newList =  that.data.articleList.concat(res.data)
                  that.setData({
                      articleList:newList,
                      pageNo:pageNo,
                      pageSize:pageSize,
                  });

              }

              if (that.data.articleList.length==0){
                  that.setData({
                      noDataFlag:true,
                  });
              }

          }
      }).catch(e => { })
      }
  }
})
