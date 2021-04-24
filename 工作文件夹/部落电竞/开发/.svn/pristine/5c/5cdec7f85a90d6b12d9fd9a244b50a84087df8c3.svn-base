// modules/reportpage/mdcharts/md-charts.js
//图文直播
var api = require('../../../libs/http')
const app = getApp()
var liveSocketService = require('../../../service/SocketService')
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    matchId: Number,
    matchStatus: Number,
    matchDetail: Object,
    socketUrl: String,
    gameIdArray: Array
  },

  /**
   * 组件的初始数据
   */
  data: {
    tableTrArr: ['Rd', 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15],
    curUsrId: 0,
    matchId: 0,
    //liveDataList: ['5分20秒 貂蝉击杀了亚瑟', '3分50秒 G2摧毁IG防御塔'],
    liveDataList: [],
    timer: [],
    socketUrl: '',//socket地址
    lockReconnect: false,//默认进来是断开链接的
    limit: 0,//重连次数
    curGame: '',
    ready:false
  },

  ready() {
    var _self = this;
    console.log(_self.properties.gameIdArray, '当前场次对应的局数');
    if (_self.properties.matchDetail.videogameId == 4) {
      _self.setData({
        gameIdArray: []
      });
      api._showToast('暂不支持该游戏', 1)
      return;
    }
    var curGame = '';
    if (_self.properties.gameIdArray.length > 0) {
      curGame = _self.properties.gameIdArray[0];
    }
    _self.setData({
      matchId: _self.properties.matchDetail.matchId,
      socketUrl: _self.properties.socketUrl,
      curUsrId: app.getGlobalUserInfo().usrId,
      curGame: curGame
    });
    this.reflushData([]);
    this._getGameDataByStatus(curGame, this);
    api._toSetReady(_self,0.3)
  },

  /**
   * 组件的方法列表
   */
  methods: {
    reflushData(liveDataList) {
      console.log(liveDataList, '清空数据');
      this.setData({
        liveDataList: liveDataList
      });
    },
    _selectGameId(e) {
      var _self = this;
      var game = e.currentTarget.dataset.game;
      _self.setData({
        curGame: game
      })
      _self._getGameDataByStatus(game, _self);
    },
    _getGameDataByStatus(game, _self) {
      var matchId = _self.properties.matchDetail.matchId;
      if (_self.properties.matchStatus == 2) {//已结束
        _self._getGameDataByGame(game);
      }
      if (_self.properties.matchStatus == 1) {//进行中
        var gameIds = _self.properties.gameIdArray;
        if (null != gameIds && gameIds.length > 1) {
          gameIds = gameIds.slice(0, gameIds.length - 1);
        }
        if (gameIds.length > 1 && gameIds.indexOf(game) > -1) {
          _self._getGameDataByGame(game);
        } else {
          //最后一局才需要连ws处理逻辑
          var initScene = liveSocketService.getSceneMsg(2, 21, 17, 'E_', matchId);
          _self.setData({
            liveDataList: []
          });
          _self._getGameDataByGame(game);
          setTimeout(function () {
            var myEventDetail = {// detail对象，提供给事件监听函数
              liveDataList: _self.data.liveDataList,
              curGame:_self.data.curGame
            }
            _self.triggerEvent('myevent', myEventDetail)
            liveSocketService.toSendSocketMessage(initScene);
          }, 5000);
        }
      }
    },
    _getGameDataByGame(game) {
      if (game == '') {
        return;
      }
      var _self = this;
      var matchId = _self.properties.matchDetail.matchId;
      _self.setData({
        liveDataList: []
      });
      api._get(api.matchEventLiveJsonUrl + matchId + "_" + game).then(res => {
        var result = res.split('_&_');
        result.forEach(function (value) {
          if (value == '') {
            return;
          }
          var dataValue = JSON.parse(value);
          var data = JSON.parse(dataValue.data);
          liveSocketService.eventsLiveForHistory(_self, data);
        })

      }).catch(e => { console.log(e) });
    }
  }
})
