<template>
  <div class="Page allGuess">
    <header class="mod_header">
      <navBar :pageTitle='123'>
        <div class="battle_team" v-if="morePlayList.length>0">
          <div class="flex_hc">
            <img :src="morePlayList[0].homeTeamLogo|getDefaultImg(globalConst.dDmatchTeamDefaultIcon)" alt="">
            <span class="name">{{morePlayList[0].homeTeamName}}</span>
          </div>
          <span class="vs">vs</span>
          <div class="flex_hc">
            <span class="name">{{morePlayList[0].awayTeamName}}</span>
            <img :src="morePlayList[0].awayTeamLogo|getDefaultImg(globalConst.dDmatchTeamDefaultIcon)" alt="">
          </div>
        </div>
      </navBar>
      <!-- 刷新赔率进度条 -->
      <!-- <div class="progress">
        <p class="bar"></p>
      </div> -->
    </header>
    <div class="main" :class="{'ban_slide':getBetData.toCurBet}">
      <ul>
        <li v-for="(item,index) in morePlayList" :key="index">
          <bet-Item class="bet_item" :quizMathGame="item"></bet-Item>
        </li>
      </ul>
    </div>
    <recharge-pop v-if="showRechargePop" @closeRechargePop="closeRechargePop" @getUserWallet="getUserWallet"
      @openPopAlert="openPopAlert">
    </recharge-pop>
    <pop-Alert :showPop="showPop" @close="showPop=false" @confirm="confirm" btnTxt1="已取消" btnTxt2="已完成支付" type="2">
      <p>请确认微信支付是否已完成</p>
    </pop-Alert>
  </div>
</template>

<script>
import navBar from "../../components/header/nav_bar/index";
import betItem from "./components/betItem.vue";
import rechargePop from "../guess/recharge/recharge.vue";
import popAlert from "../../components/pop_up/pop_alert.vue";
// import * as socketGuess from '../../libs/websocket/socketGuess.js';
import { mapGetters, mapActions } from "vuex";

//定时任务
var spSchdule = null;
export default {
  components: {
    navBar,
    betItem,
    rechargePop,
    popAlert
  },

  props: [],
  data() {
    return {
      matchId: null,
      morePlayList: [],
      playNo: null,
      showRechargePop: false, //支付弹窗
      showPop: false,
      spUpdatTime: 5,//赔率更新时间 秒单位
      
      stopRepeat:false,
    };
  },
  computed: {
    ...mapGetters({
      getBetData: "getBetData",
      userInfo: "getUserInfo"
    })
  },
  watch: {
    getBetData(val) {
      //当用户点击投注弹出，先暂停刷新
      if (val.toCurBet) {
        console.log("清楚");
        clearInterval(spSchdule);
      } else {
        console.log("开始刷新");
        // this.runSpSchedule();
      }
      console.log("getBetData", val);
      if (val.toReflushBalance) {
        //下单完刷新余额
        this.getUserWallet();
      }
    }
  },
  activated() {
    //定时刷新当前页面赛事赔率
    // clearInterval(spSchdule);
    // this.runSpSchedule();
    this.intoWebsocketRoom();

    this.getUserWallet();
    if (this.$route.meta.isBack) {
      this.matchId = this.$route.query.matchId;
      this.playNo = this.$route.query.playNo;
      this.morePlayList = [];
      this.getMorePlayByMatchId();
    }
    this.$route.meta.isBack = true;
  },
  destroyed() {
    clearInterval(spSchdule);
  },
  deactivated() {
    clearInterval(spSchdule);
  },
  beforeRouteEnter(to, from, next) {
    if (from.name == "recharge") {
      // 这个name是下一级页面的路由name
      to.meta.isBack = false; // 设置为true说明你是返回到这个页面，而不是通过跳转从其他页面进入到这个页面
    }
    next();
  },
  mounted() {
    this.matchId = this.$route.query.matchId;
    this.playNo = this.$route.query.playNo;
    this.getMorePlayByMatchId();
    this.webSocketConfig()
  },
  methods: {
    ...mapActions(["setUserInfo"]),

    //进入赛事场景
    intoWebsocketRoom() {
      let initScene = this.socketGuess.getGameRoomSceneMsg(this.userInfo.userId, this.matchId);
      console.log("websock", this.socketGuess.isConneted);
      if (this.socketGuess.isConneted) {
         this.socketGuess.socketObeject.initScene = initScene;
        this.socketGuess.toSendSocketMessage(initScene);
      } else {
        //刷新当前页面，则重新初始化websocket
        this.openAndinitWebSocket();
      }

    },
    webSocketConfig() {
      //监听websocket推送
      this.$bus.$on("webMessage", data => {
        this.handleUpdateData(data)
      });
      //断线重连重连
      this.$bus.$on("reconnect", data => {
        this.openAndinitWebSocket();
      });

    },
    //处理webSocket推送的数据 
    handleUpdateData(data) {
      if (data.execType == 12) {
        console.log("连接成功");
        return
      }
      let matchResponse = JSON.parse(data.playload);
      if (data.execType == 19) { // 赛事状态
        if (data.add) {
          //说明有新增玩法，直接刷新整个页面
          if (this.matchId == data.matchId) {
            this.getMorePlayByMatchId();
          }
          
        }
        this.morePlayList.forEach(matchInfo => {
          if (data.matchType == 'match_game') {
            if (matchResponse.data.length > 0) {
              matchResponse.data.forEach(quizGame => {
                if (matchInfo.id == quizGame.matchGameId) {
                  matchInfo.status = quizGame.status
                  matchInfo.suspended = quizGame.suspended
                  // matchInfo.quizMathGame.visible = matchResponse.data.visible
                }

              });
            }

          }
        });

      }

      if (data.execType == 18) { // 赔率数据
        this.morePlayList.forEach(matchInfo => {
          if (matchInfo.matchId == matchResponse.matchId) {
            if (matchResponse.data.length > 0) {

              matchResponse.data.forEach(quizGame => {
                if (matchInfo.id == quizGame.matchGameId) {
                  matchInfo.quizOptions.forEach((option, k) => {
                    option.limit = quizGame.odds[k].limit;
                    option.odds = quizGame.odds[k].odds;
                  });
                }

              });
            }

          }
        });

      }

    },
    /** 打开websocket*/
    openAndinitWebSocket() {
      console.log("获取websocket");
      if (this.socketGuess.stopRepeat) {
        return
      }
      this.socketGuess.stopRepeat = true
      return this.$post("/api/user/getUserWebsocket")
        .then(rsp => {
          const dataResponse = rsp;
          console.log(dataResponse);
          this.socketGuess.socketObeject.socketUrl = dataResponse.data.socketUrl;
          this.socketGuess.socketObeject.userId = dataResponse.data.userId
          // var initScene = this.socketGuess.getGameRoomSceneMsg(this.socketGuess.socketObeject.userId);
          // this.socketGuess.socketObeject.initScene = initScene;
          this.socketGuess.initWebSocket(this.socketGuess.socketObeject, this);
          this.socketGuess.stopRepeat = false
        })
        .catch(error => {
          console.log(error);
        });
    },
    getMorePlayByMatchId() {
      let param = {};
      param.matchId = this.matchId;
      param.playNo = this.playNo;
      param.distinctIndex = true;
      return this.$post("/api/quiz/match/gameList", param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            console.log("更多玩法");
            let dataList = dataResponse.data;
            let tempList = [];
            dataList.forEach(element => {
              element.forEach(match => {
                tempList.push(match);
              });
            });
            this.morePlayList = tempList;
            // this.morePlayList = dataResponse.data;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    //查询设置用户钱包
    getUserWallet() {
      return this.$post("/api/starNum/queryStarNum")
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            console.log(dataResponse.data.ableRecScore, "获取用户的星星");
            this.setUserInfo({
              ...this.userInfo,
              recScore: dataResponse.data.recScore,
              giftRecScore: dataResponse.data.giftRecScore,
              ableRecScore: dataResponse.data.ableRecScore,
              nickName: dataResponse.data.nickName,
              icon: dataResponse.data.icon
            });
            console.log(this.userInfo);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    toCharge() {
      console.log(9955);
      this.showRechargePop = true;
    },

    /**关闭支付弹窗 */
    closeRechargePop() {
      this.showRechargePop = false;
    },
    openPopAlert() {
      this.showPop = true;
    },
    /**h5支付后 */
    confirm() {
      this.showPop = false;
      this.getUserWallet();
    },
    //启动赔率刷新
    runSpSchedule() {
      let _this = this;
      spSchdule = setInterval(() => {
        if (_this.morePlayList) {
          // let updateSp = this.$route.query.updateSp;
          // if (updateSp == 1) {
          this.getMorePlayByMatchId();
          // } else {
          //   let currSpList = "";
          //   for (let index = 0; index < _this.morePlayList.length; index++) {
          //     let gameList = _this.morePlayList[index];
          //     currSpList = currSpList + gameList.id + ",";
          //   }
          //   console.log("currSpList", currSpList);
          //   if (currSpList) {
          //     _this.updateMatchSp(currSpList);
          //   }

          // }

        }
      }, _this.spUpdatTime * 1000);
    },
    /**根据gameId更新赔率 */
    updateMatchSp(currSpList) {
      let param = {};
      param.matchGameIds = currSpList;
      return this.$post("/api/quiz/match/updateSp", param)
        .then(rsp => {
          let _this = this;
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            let quizMathGameList = dataResponse.data;
            if (quizMathGameList.length > 0) {
              let tempList = [];
              quizMathGameList.forEach(quizMathGame => {
                _this.morePlayList.forEach(game => {
                  if (quizMathGame.id === game.id) {
                    if (!game.quizOptions || game.quizOptions.length == 0) {
                      return;
                    }
                    // console.log(game);
                    for (let i = 0; i < game.quizOptions.length; i++) {
                      let quizzedCount = game.quizOptions[i].quizzedCount;
                      game.quizOptions[i] = quizMathGame.quizOptions[i];
                      game.quizOptions[i].quizzedCount = quizzedCount;
                    }
                    let tempGame = {};
                    Object.assign(tempGame, game);
                    tempList.push(tempGame);
                  }
                });
              });
              this.morePlayList = tempList;
            }
            console.log("更新赔率");
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  }
};
</script>

<style lang="scss">
.allGuess {
  background-color: #1f1212;
  .back {
    height: 10.67vw !important;
    &::before,
    &::after {
      background-color: #a26767 !important;
    }
  }
  .bet_item h3 {
    font-size: 3.7333vw;
    &::before {
      margin-top: 0.6667vw;
    }
  }
}
</style>


<style lang='scss' scoped>
@import "../../assets/common/_base";
@import "../../assets/common/_mixin";

.nav_bar {
  color: transparent !important;
  background-color: #321919 !important;
  line-height: 10.67vw !important;
}
.main {
  padding-bottom: 8vw;
}

.ban_slide {
  overflow: hidden;
}

.battle_team {
  @extend .g_v_c_mid;
  @extend .flex_v_justify;
  width: 74.67vw;
  height: 100%;
  padding: 0 6.4vw;
  @include getBgImg("../../assets/images/guess/guess_title_bg.png");
  img {
    width: 7.2vw;
    height: 7.2vw;
    border-radius: 50%;
  }
  .name {
    margin: 0 1.07vw;
    font-size: 3.73vw;
    color: #fedcd7;
    @include t_nowrap(18.67vw);
  }
  .vs {
    @extend .g_v_c_mid;
    font-size: 4.8vw;
    font-weight: bold;
    font-style: italic;
    color: #5b3732;
  }
}

.bet_item {
  margin: 2.6667vw 2.67vw 0;
  padding: 4vw 2.67vw;
  background: linear-gradient(to right bottom, #482522, #32191e);
  background: -webkit-linear-gradient(left top, #482522, #32191e);
}

.progress {
  position: absolute;
  bottom: -0.8vw;
  left: 0;
  z-index: 10;
  width: 100%;
  height: 0.8vw;
  .bar {
    height: 100%;
    @include getBgLinear(right, #5d3517, #b93029);
    transform: translateX(-100%);
    -webkit-transform: translateX(-100%);
    animation: barMove 5s linear infinite;
  }
}

@keyframes barMove {
  from {
    transform: translateX(-100%);
    -webkit-transform: translateX(-100%);
  }
  to {
    transform: translateX(0);
    -webkit-transform: translateX(0);
  }
}

@-webkit-keyframes barMove {
  from {
    transform: translateX(-100%);
    -webkit-transform: translateX(-100%);
  }
  to {
    transform: translateX(0);
    -webkit-transform: translateX(0);
  }
}
</style>
