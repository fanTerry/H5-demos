<template>
  <div class="Page allGuess">
    <header class="mod_header">
      <navBar :pageTitle="'枫叶电竞'">
      </navBar>
    </header>
    <div class="main" :class="{'ban_slide':getBetData.toCurBet}">
      <guess-item class="match_info" :matchInfo='matchInfo' matchType='1'></guess-item>
      <!-- 直播 -->
      <!-- <div class="live">
        <video src=""></video>
      </div> -->
      <!-- <div class="tab">
        <span>数据</span>
        <span>预测</span>
        <span>方案</span>
      </div> -->
      <ul class="guess_list">
        <li class="item" v-for="(item,index) in morePlayList" :key="index">
          <bet-Item class="bet" :guessEnd='true' :quizMathGame="item"></bet-Item>
        </li>
      </ul>

      <ul class="end_list" v-if='endingPlayList&&endingPlayList.length>0'>
        <p class="title">- 已完结赛果 -</p>
        <li class="item" v-for="(item,index) in endingPlayList" :key="index">
          <bet-Item class="bet" :guessEnd='false' :quizMathGame="item"></bet-Item>
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
import guessItem from "./home/components/guessItem.vue";
import betItem from "./components/betItem.vue";
import rechargePop from "../guess/recharge/recharge.vue";
import popAlert from "../../components/pop_up/pop_alert.vue";
import { mapGetters, mapActions } from "vuex";

export default {
  components: {
    navBar,
    betItem,
    rechargePop,
    popAlert,
    guessItem
  },

  props: [],
  data() {
    return {
      matchId: null,
      morePlayList: [], //赛中，未开赛
      endingPlayList: [], // 完结场
      playNo: null,
      showRechargePop: false, //支付弹窗
      showPop: false,
      spUpdatTime: 5, //赔率更新时间 秒单位
      matchInfo: Object
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
      // if (val.toCurBet) {

      // }
      console.log("getBetData", val);
      if (val.toReflushBalance) {
        //下单完刷新余额
        this.getUserWallet();
      }
    }
  },
  activated() {
   
    this.getUserWallet();
    if (this.$route.meta.isBack) {
      this.matchId = this.$route.query.matchId;
      this.queryQuizMatch(this.matchId);
      this.getMorePlayByMatchId();
      this.getEndingPlayByMatchId(2);
    }
    this.$route.meta.isBack = true;
     this.intoWebsocketRoom();
  },
  destroyed() {
    //关闭websocket
    this.socketGuess.websocketclose();
  },
  deactivated() { },
  beforeRouteEnter(to, from, next) {
    if (from.name == "recharge") {
      // 这个name是下一级页面的路由name
      to.meta.isBack = false; // 设置为true说明你是返回到这个页面，而不是通过跳转从其他页面进入到这个页面
    }
    next();
  },
  created() {
    this.matchId = this.$route.query.matchId;
    this.queryQuizMatch(this.$route.query.matchId);
  },
  mounted() {
    //初始化赛事监听
    this.webSocketConfig();
    this.matchId = this.$route.query.matchId;
    this.getMorePlayByMatchId(); //赛中，未开赛
    this.getEndingPlayByMatchId(); //完结
  },
  methods: {
    ...mapActions(["setUserInfo"]),
    //进入websocket赛事玩法列表房间
    intoWebsocketRoom() {
      console.log("this.userInfo.userId", this.userInfo.userId);
      console.log("this.matchId",this.matchId);
      let initScene = this.socketGuess.getGameRoomSceneMsg(this.userInfo.userId,this.matchId);
      this.socketGuess.socketObeject.initScene = initScene;
      console.log("websock", this.socketGuess.isConneted);
      if (this.socketGuess.isConneted) {
        this.socketGuess.socketObeject.initScene = initScene;
        console.log("initScene—进入场景参数", initScene);
        this.socketGuess.toSendSocketMessage(initScene);
      } else {
        //刷新当前页面，则重新初始化websocket
        console.log("准备wsSocket");
        this.openAndinitWebSocket();
      }
    },
    //赛事推送监听
    webSocketConfig() {
      //监听websocket推送
      this.$bus.$on("webMessage", data => {
        this.handleUpdateData(data);
      });
      //监听断线重连
      // this.$bus.$on("reconnect", data => {
      //   this.openAndinitWebSocket();
      // });
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
    //获取赛事对阵信息
    queryQuizMatch(matchId) {
      let param = {};
      param.matchId = matchId;
      return this.$post("/api/quiz/match/queryQuizMatch", param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            this.matchInfo = dataResponse.data;
          }
        })
        .catch(error => {
          console.log(error, "异常");
        });
    },
    /**
     *  前端玩法排序规则
     */
    sortArrayList(dataList) {
      if (dataList.length > 1) {
        dataList.sort(function (a, b) {
          if (a.gameNumber === b.gameNumber) {
            return a.playOrder - b.playOrder;
          }
          return a.gameNumber - b.gameNumber;
        })
      }

    },

    /**
     *  处理webSocket推送的数据
     */
    handleUpdateData(data) {
      if (data.execType == 12) {
        console.log("ws连接成功");
        return;
      }
      let _this = this;
      let matchResponse = JSON.parse(data.playload);
      if (data.execType == 19) {

        if (matchResponse.refresh && matchResponse.refresh == 'add') {
          //说明有新增玩法，把数据填入，再重新排序
          console.log("add");
          if (matchResponse.data && matchResponse.data.length > 0) {
            let tem = matchResponse.data.filter(function (game) {
              //防止出现重复数据
              for (const quizGame of _this.morePlayList) {
                if (quizGame.id == game.id) {
                  return false
                }
              }
              return true;
            })

            this.morePlayList = this.morePlayList.concat(tem);
            this.sortArrayList(this.morePlayList);
          }
          return
        }

        let delArray = [];
        this.morePlayList.forEach((matchInfo, q) => {
          if (data.matchType == "match_game") {
            if (matchResponse.data.length > 0) {
              matchResponse.data.forEach(quizGame => {
                if (matchInfo.id == quizGame.matchGameId) {
                  matchInfo.status = quizGame.status;
                  matchInfo.suspended = quizGame.suspended;
                  matchInfo.visible = quizGame.visible;
                  matchInfo.awardStatus = quizGame.awardStatus;
                  matchInfo.awardResult = quizGame.awardResult;
                  if (  (quizGame.awardStatus && quizGame.awardStatus != 0 )
                  || quizGame.awardResult != null 
                  || quizGame.visible == 0) {
                    //完结赛事，删除并移动到完结列表
                    delArray.push(q) //保存要移动数据的下标
                  }
                  //update赔率
                  matchInfo.quizOptions.forEach((option, k) => {
                    option.limit = quizGame.odds[k].limit;
                    option.odds = quizGame.odds[k].odds;
                  });
                }
              });
            }
          }
        });
        if (delArray.length == 0) {
          return
        }
        console.log("delArray", delArray);
        let endingObejectArray = [];
        if (delArray.length > 0) {
          for (const i in delArray) {
            // let element = {};
            let index = delArray[i] //获取删除元素的下标
            let gameObject = this.morePlayList[index];
            if (gameObject.visible == 0) {
              //隐藏类型直接删掉，不需要移动
            } else {
              // Object.assign(gameObject, element)
              endingObejectArray.push(gameObject);
            }
            this.morePlayList.splice(index, 1);
          }
        }
        if (endingObejectArray.length == 0) {
          return
        }
        console.log("endingObejectArray", endingObejectArray);
        if (endingObejectArray.length > 0) {
          
          this.endingPlayList = this.endingPlayList.concat(endingObejectArray.filter(function (v) {
            //防止出现重复数据
            for (const quizGame of _this.endingPlayList) {
              if (v.id == quizGame.id) {
                return false;
              }
            }
            return true
          }));
          this.sortArrayList(this.endingPlayList);
        }


      }

      // if (data.execType == 18) {
      //   // 赔率数据
      //   this.morePlayList.forEach(matchInfo => {
      //     if (matchInfo.matchId == matchResponse.matchId) {
      //       if (matchResponse.data.length > 0) {
      //         matchResponse.data.forEach(quizGame => {
      //           if (matchInfo.id == quizGame.matchGameId) {
      //             matchInfo.quizOptions.forEach((option, k) => {
      //               option.limit = quizGame.odds[k].limit;
      //               option.odds = quizGame.odds[k].odds;
      //             });
      //           }
      //         });
      //       }
      //     }
      //   });
      // }
    },

    /** 
     * 打开websocket
     * 
     * */
    openAndinitWebSocket() {
      console.log("获取websocket");
      if (this.socketGuess.stopRepeat) {
        return;
      }
      this.socketGuess.stopRepeat = true;
      return this.$post("/api/user/getUserWebsocket")
        .then(rsp => {
          const dataResponse = rsp;
          console.log(dataResponse);
          this.socketGuess.socketObeject.socketUrl =
            dataResponse.data.socketUrl;
          this.socketGuess.socketObeject.userId = dataResponse.data.userId;
          // var initScene = this.socketGuess.getGameRoomSceneMsg(this.socketGuess.socketObeject.userId);
          // this.socketGuess.socketObeject.initScene = initScene;
          console.log(this.socketGuess.socketObeject);
          this.socketGuess.initWebSocket(this.socketGuess.socketObeject, this);
          this.socketGuess.stopRepeat = false;
        })
        .catch(error => {
          console.log(error);
        });
    },
    //获取赛中，未开赛
    getMorePlayByMatchId() {
      let param = {};
      param.matchId = this.matchId;
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
            this.sortArrayList(this.morePlayList);
            // this.morePlayList = dataResponse.data;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    //获取完结赛果
    getEndingPlayByMatchId() {
      let param = {};
      param.matchId = this.matchId;
      param.distinctIndex = true;
      param.finished = true;
      console.log(param);
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
            this.endingPlayList = tempList;
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
  background-color: #d73a42;
  .back {
    height: 10.67vw !important;
    &::before,
    &::after {
      background-color: #333 !important;
    }
  }
  .end_list {
    .title {
      padding: 3.2vw 0 1.0667vw;
      font-size: 3.2vw;
      color: #fff;
      text-align: center;
    }
    .bet {
      color: #fff;
      background-color: #912d32;
    }
  }
  .match_info {
    border-radius: 0 !important;
    .more_options {
      display: none !important;
    }
    .header {
      .title {
        justify-content: center;
        -webkit-justify-content: center;
      }
      &::before {
        display: none;
      }
    }
  }
}
</style>


<style lang='scss' scoped>
@import "../../assets/common/_base";
@import "../../assets/common/_mixin";

.mod_header {
  @include getRadiusBorder(#ddd, bottom, 0);
}

.nav_bar {
  line-height: 44px !important;
}
.main {
  padding-bottom: 8vw;
}

.ban_slide {
  overflow: hidden;
}

.live {
  video {
    width: 100%;
  }
}

.tab {
  position: sticky;
  position: -webkit-sticky;
  top: 0;
  z-index: 1;
  @extend .flex_v_justify;
  margin: 0 22.6667vw;
  overflow: hidden;
  background-color: #d73a42;
  span {
    display: inline-block;
    padding: 3.4667vw 0;
    font-size: 3.7333vw;
    color: rgba(255, 255, 255, 0.5);
    border-radius: 3px;
    &.active {
      position: relative;
      color: #fff;
      &::after {
        content: "";
        @extend .g_c_mid;
        bottom: 1.0667vw;
        width: 4vw;
        height: 1.0667vw;
        border-radius: 0.8vw;
        background-color: #fff;
      }
    }
  }
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

.item {
  margin-top: 2.1333vw;
}

.bet {
  margin: 0 4.2667vw;
  padding: 3.2vw;
  color: #333;
  background-color: #fff;
  border-radius: 1.3333vw;
}
</style>
