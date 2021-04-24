<template>
  <div>
    <section class="live_broadcast">
      <ul class="map_list">
        <li v-for="(item,index) in gameIdArray" :class="curGame==item?'current':''" @click='_selectGameId(item)'
          :key="index">第{{index+1}}局</li>
      </ul>
      <!-- 图片数据 -->
      <!-- <div class="live_pic">
        <div class="bp">
          BP数据
          <span></span>
        </div>
        <div class="score_con">
          <span>R: 16 -- inferno</span>
          <p class="score">2:0</p>
          <span class="copy"></span>
        </div>
        <div class="grid_list">
          <h3>
            <div class="name">
              <img src alt />W7M
            </div>
            <div class="blood">
              <span class="add_blood"></span>
            </div>
            <div>
              <span class="shop_cart"></span>
            </div>
            <div>
              <span class="suffer_injury"></span>
            </div>
            <div>
              <span class="money"></span>
            </div>
            <div>
              <span>AdR</span>
            </div>
          </h3>
          <div class="item">
            <div class="name">NAME 0001</div>
            <div class="blood">
              <div class="bar">
                <span class="bg_red" style="width:40%"></span>
              </div>
            </div>
            <div>
              <span class="gun_icon"></span>
            </div>
            <div>
              <span class="injury_icon"></span>
            </div>
            <div>
              <span>$4000</span>
            </div>
            <div>
              <span>95,71</span>
            </div>
          </div>
        </div>
      </div> -->
      <!-- 文字直播 -->
      <div class="live_txt">
        <div class="user_info" v-for="(item,index) in liveDataList" :key="index">
          <div class="user_img">
            <img src="https://rs.esportzoo.com/svn/esport-res/mini/images/avatar/cat.jpg" alt />
          </div>
          <div>
            <div class="flex_hc">
              <span class="name">直播君</span>
              <!-- <span class="time">26小时</span> -->
            </div>
            <p class="tips">
              {{item}}
              <!-- 比赛事件28'28"
              <span class="c_black">YM击败大龙</span>地方神王盖伦被我方
              <span class="c_red">神王诺手</span>击杀 -->
            </p>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script type="text/ecmascript-6">
const COMPONENT_NAME = "match-detail-live";
export default {
  name: COMPONENT_NAME,
  components: {},
  props: {
    matchId: Number,
    matchDetail: Object,
    socketUrl: String,
    gameIdArray: Array
  },
  data() {
    return {
      curUsrId: 0,
      liveDataList: [],
      timer: [],
      lockReconnect: false, //默认进来是断开链接的
      limit: 0, //重连次数
      curGame: "",
      ready: false
    };
  },
  computed: {},
  created() {},
  mounted() {
    var _self = this;
    console.log(_self.gameIdArray, "当前场次对应的局数");
    console.log(window.sessionStorage.userId, "window.sessionStorage.userId");
    if (_self.matchDetail.videogameId == 4) {
      _self.gameIdArray = [];
      this.$toast("暂不支持该游戏", 1);
      return;
    }
    if (_self.gameIdArray.length > 0) {
      _self.curGame = _self.gameIdArray[0];
    }
    console.log(_self.curGame, "_self.curGame");
    this.reflushData([]);
    this._getGameDataByStatus(_self.curGame, this);
  },
  methods: {
    reflushData(liveDataList) {
      this.liveDataList = liveDataList;
    },
    _selectGameId(game) {
      var _self = this;
      _self.curGame = game;
      _self._getGameDataByStatus(game, _self);
    },
    _getGameDataByStatus(game, _self) {
      var matchId = _self.matchDetail.matchId;
      if (_self.matchDetail.status == 2) {
        //已结束
        _self._getGameDataByGame(game);
      }
      if (_self.matchDetail.status == 1) {
        console.log("进行中");
        //进行中
        var gameIds = _self.gameIdArray;
        if (null != gameIds && gameIds.length > 1) {
          gameIds = gameIds.slice(0, gameIds.length - 1);
        }
        if (gameIds.length > 1 && gameIds.indexOf(game) > -1) {
          _self._getGameDataByGame(game);
        } else {
          //最后一局才需要连ws处理逻辑
          var initScene = _self.socketApi.getSceneMsg(
            2,
            21,
            17,
            "E_",
            matchId,
            window.sessionStorage.userId
          );
          _self.reflushData([]);
          _self._getGameDataByGame(game);
          setTimeout(function() {
            var myEventDetail = {
              // detail对象，提供给事件监听函数
              liveDataList: _self.liveDataList,
              curGame: _self.curGame
            };
            _self.$emit("onChangeData", myEventDetail);
            _self.socketApi.toSendSocketMessage(initScene);
          }, 1000);
        }
      }
    },
    _getGameDataByGame(game) {
      if (game == "") {
        return;
      }
      var _self = this;
      var matchId = _self.matchDetail.matchId;
      _self.liveDataList = [];
      // this.$get(this.globalConst.matchEventLiveJsonUrl + matchId + "_" + game)
      //   .then(res => {
      //     var result = res.split("_&_");
      //     result.forEach(function(value) {
      //       if (value == "") {
      //         return;
      //       }
      //       var dataValue = JSON.parse(value);
      //       var data = JSON.parse(dataValue.data);
      //       _self.socketApi.eventsLiveForHistory(_self, data);
      //     });
      //   })
      //   .catch(e => {
      //     console.log(e);
      //   });
      this.$post("/api/league/getJsonFile", {
        matchId: matchId,
        gameId: game,
        type: "events_live"
      })
        .then(res => {
          if (res.code == "200" && res.data) {
            if (res.data && res.data.fileContent) {
              var result = res.data.fileContent.split("_&_");
              result.forEach(function(value) {
                if (value == "") {
                  return;
                }
                var dataValue = JSON.parse(value);
                var data = JSON.parse(dataValue.data);
                _self.socketApi.eventsLiveForHistory(_self, data);
              });
            }
          } else {
          }
        })
        .catch(e => {
          console.log(e);
        });
    }
  },
  watch: {}
};
</script>
<style lang='scss' scoped>
@import "../../../assets/common/_base.scss";
@import "../../../assets/common/_mixin.scss";
@import "../../../assets/common/_var.scss";
@import "../../../assets/common/iconfont.css";

.live_broadcast {
  color: #666;
  .map_list {
    @extend .flex;
    padding: 12px 36px;
    background-color: #fff;
  }
  li {
    @extend .flex_v_h;
    width: 70px;
    height: 24px;
    border-radius: 24px;
    color: #666;
    background-color: #f5f6f7;
    border: 1px solid #dbdcdd;
    &.current {
      color: $color_main;
      border: 1px solid $color_main;
    }
  }
  .live_pic {
    position: relative;
    padding: 40px 0 10px;
    border-radius: 8px;
    background-color: #fff;
  }
  .bp {
    @extend .flex_hc;
    position: absolute;
    right: 10px;
    top: 10px;
    span {
      width: 12px;
      height: 12px;
      @include getArrow(8px, #666666, right);
    }
  }
  .score_con {
    position: relative;
    @extend .flex_v_justify;
    padding: 0 5px;
    .copy {
      width: 12px;
      height: 12px;
      background-color: #666;
    }
    .score {
      @extend .g_v_c_mid;
      font-size: 21px;
      letter-spacing: 10px;
      color: #000000;
    }
  }
  .grid_list {
    margin-top: 10px;
    padding-bottom: 5px;
    background-color: rgba($color: #625e57, $alpha: 0.5);
    img {
      width: 18px;
      height: 18px;
      margin-right: 5px;
      object-fit: cover;
    }
    h3,
    .item {
      @extend .flex;
      height: 38px;
      > div {
        @extend .flex_v_h;
        flex: 1;
        -webkit-flex: 1;
      }
      .name {
        flex: none;
        width: 80px;
        text-align: center;
        @include t_nowrap(80px);
        line-height: 1.2;
        @media (max-width: 320px) {
          width: 50px;
        }
      }
      .blood {
        flex: none;
        width: 76px;
      }
    }
    h3 {
      background-color: #f0f5fa;
    }
    .item {
      height: 26px;
      margin-top: 5px;
      background-color: rgba(96, 126, 153, 0.4);
      .name {
        display: block;
        line-height: 26px;
      }
    }
    .bar {
      width: 80%;
      height: 10px;
      border-radius: 10px;
      background-color: #fff;
      overflow: hidden;
      span {
        display: block;
        height: 100%;
      }
    }
    .add_blood,
    .shop_cart,
    .suffer_injury,
    .money,
    .gun_icon,
    .injury_icon {
      width: 13px;
      height: 13px;
    }
    .add_blood {
      @include getBgImg("../../../assets/images/match/add_blood.png");
    }
    .shop_cart {
      @include getBgImg("../../../assets/images/match/shop_cart.png");
    }
    .suffer_injury {
      @include getBgImg("../../../assets/images/match/suffer_injury.png");
    }
    .money {
      @include getBgImg("../../../assets/images/match/money.png");
    }
    .gun_icon {
      @include getBgImg("../../../assets/images/match/gun_icon.png");
    }
    .injury_icon {
      @include getBgImg("../../../assets/images/match/injury_icon.png");
    }
  }
  .live_txt {
    border-radius: 8px;
    background-color: #f5f6f7;
    .user_info {
      @extend .flex;
      align-items: flex-start;
      -webkit-align-items: flex-start;
      padding: 10px 45px 10px 10px;
    }
    .user_img {
      position: relative;
      flex: none;
      width: 35px;
      height: 35px;
      margin-right: 10px;
      img {
        width: 100%;
        height: 100%;
        border-radius: 50%;
      }
    }
    .time {
      padding-left: 5px;
      font-size: 12px;
      color: #a3a3a3;
    }
    .name {
      @include t_nowrap(150px);
      line-height: 1.2;
      font-size: 13px;
      font-weight: 500;
      color: #000;
    }
    .tips {
      position: relative;
      margin-top: 5px;
      padding: 5px 10px;
      line-height: 25px;
      border-radius: 8px;
      background-color: #fff;
      span {
        font-weight: 500;
      }
      &:after {
        content: "";
        position: absolute;
        left: 0;
        top: 10px;
        transform: translateX(-100%);
        -webkit-transform: translateX(-100%);
        @include getTriangle(8px, #fff, left);
      }
    }
  }
}
</style>


