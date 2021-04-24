// component/userinfobar/userinfo-bar.js
var api = require('../../libs/http.js');
const app = getApp();
Component({
    /**
     * 组件的属性列表
     */
    properties: {
        showType: Number,
        gamesList:Array,
        selectedTag:Boolean,
    },

    /**
     * 组件的初始数据
     */
    data: {
        usrInfo: {},

        hasUsrInfo:false
    },
    attached() {



    },
    ready() {
	   var usrInfo = app.getGlobalUserInfo();
       if (!usrInfo) {
           console.log("无法获取到用户信息")
       } else {
           this.setData({
               usrInfo: usrInfo,
               hasUsrInfo: true
           });
       }

        console.log("查看数据",this.properties.gamesList);
    },
    /**
     * 组件的方法列表
     */
    methods: {

        /**赛事首页切换tag */
        changeTag:function (e) {
            var curIndex = e.currentTarget.dataset.index;
            var arry = this.properties.gamesList;
            console.log("当前发送id",arry[curIndex].videogameId)
            for (var i = 0; i <arry.length ; i++) {
                if (i==curIndex) {
                    arry[i].selected = true;
                }else {
                    arry[i].selected = false;
                }
            }
            this.setData({
                gamesList:arry,
            })

            // 所有要带到主页面的数据，都装在eventDetail里面
            var eventDetail = {
                videogameId:arry[curIndex].videogameId,
            }
            // 触发事件的选项 bubbles是否冒泡，composed是否可穿越组件边界，capturePhase 是否有捕获阶段
            var eventOption = {
                composed: true,
            }
            this.triggerEvent('mymatch', eventDetail, eventOption)
        },

        /**刷新当前组件数据 */
        reflushData(usrInfo) {
            if (!usrInfo) {
                console.log("无法获取到用户信息")
            } else {
                this.setData({
                    usrInfo: usrInfo,
                    hasUsrInfo: true
                });
            }
        },
    }


})
