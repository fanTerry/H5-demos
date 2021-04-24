// modules/reportpage/mddata/md-data.js
var api = require('../../../libs/http')
const app = getApp()
var liveMatchDataSocketService = require('../../../service/SocketService')
Component({
  /**
   * 组件的属性列表
   */
  properties: {
    matchDetail: Object,
    socketUrl: String,
    gameIdArray:Array
  },

  /**
   * 组件的初始数据
   */
  data: {
    detailData: [],
    matchResData: [],
    curLiveDetail: Object,
    timer: [],
    socketUrl: '',//地址
    lockReconnect: false,//默认进来是断开链接的
    limit: 0,//重连次数
    scene: 'matchdata',//聊天场景
    ready:false
  },

  ready() {
    // 初始化文本数据
    console.log(this.properties.matchDetail, 'this.properties.matchDetail');
    this._getData();
    api._toSetReady(this,0.3)
  },

  /**
   * 组件的方法列表
   */
  methods: {
    _getData() {
      var _self = this, matchStatus = _self.properties.matchDetail.status;
      if (matchStatus != 2) {//0603修改:不是结束后的比赛都有数据
        _self._getMatchData(_self);
      }
      if (matchStatus == 2) {//结束后获取,比赛结束有赛果
        _self._getMatchResData(_self);
      }
      if (matchStatus == 1) {
        //_self._getMatchLiveData(_self);
        this._getGameByGameId();
      }
    },

    // 获取赛前赛事数据,这部分赛中的时候不展示
    _getMatchData(_self) {
      api._postAuth('/league/data/' + _self.properties.matchDetail.matchId).then(res => {
        console.log(res, '获取赛事数据');
        if (res.code == "200" && res.data) {
          _self.setData({
            detailData: res.data
          });
        }
      }).catch(e => {
        console.log(e)
      })
    },

    //获取赛果数据
    _getMatchResData(_self) {
      api._postAuth('/league/result/' + _self.properties.matchDetail.matchId).then(res => {
        console.log(res, '获取赛事数据的赛果');
        if (res.code == "200" && res.data) {
          _self.setData({
            matchResData: res.data
          });
        } else {
          //api._showToast('暂无赛后数据', 2);
          this._getGameByGameId();//数据库延迟没有数据从json数据里面取
        }
      }).catch(e => {
        console.log(e)
      })
    },

    //获取赛中数据
    _getMatchLiveData(_self) {
      // console.log(_self.properties.matchDetail.matchId, '本场是直播中的赛事');
      // console.log(_self.properties.socketUrl, 'socket地址');
    },

    reflushMatchResData(matchResData) {
      console.error(matchResData,'matchResData');
      this.setData({
        matchResData: matchResData
      });
    },

    reflushData(curLiveDetail) {
      this.setData({
        curLiveDetail: curLiveDetail
      });
    },

    _getGameByGameId() {
      var _self = this;
      liveMatchDataSocketService.matchesLiveForHistory(_self);
    }
  }

})
