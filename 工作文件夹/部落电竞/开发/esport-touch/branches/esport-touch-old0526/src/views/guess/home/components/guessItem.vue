<template>
  <li class="guess_item guessItemPage">
    <div class="header">
      <div class="title">
        <div class="game_logo">
          <img :src="matchInfo.videogameId|gameLogo" alt="">
        </div>
        <!-- <span v-if="matchInfo.matchStatus==0">[未开始]</span> -->

        <span v-if="matchInfo.matchStatus==2">[比赛结束]</span>
        <span v-if="matchInfo.matchStatus==3">[比赛取消]</span>
        <p>{{matchInfo.startTime|formatByDay}}&nbsp;&nbsp;{{matchInfo.leagueName}} BO{{matchInfo.gameNumbers}}</p>

      </div>
      <div class="more_guess" @click="goUrlPage(matchInfo.matchId,matchInfo.quizMathGame.playNo)">
        <span>更多玩法</span>
        <i class="link_icon"></i>
      </div>
    </div>
    <div class="con">
      <div class="battle_info" @click="goUrlPage(matchInfo.matchId,matchInfo.quizMathGame.playNo)">
        <img :src="matchInfo.homeTeamLogo|getDefaultImg(globalConst.dDmatchTeamDefaultIcon)" alt="">
        <div>
          <span class="before" v-if="matchInfo.matchStatus == 0">{{matchInfo.startTime|formatByTime}} </span>
          <span class="live" v-if="matchInfo.matchStatus == 1">LIVE</span>
          <span class="end"
            v-if="matchInfo.matchStatus == 2"><i>{{matchInfo.homeScore}}</i>:<i>{{matchInfo.awayScore}}</i></span>
          <!-- <p class="join_num" v-if="matchInfo.betUsers">{{matchInfo.betUsers}}人参加</p> -->
        </div>
        <img :src="matchInfo.awayTeamLogo|getDefaultImg(globalConst.dDmatchTeamDefaultIcon)" alt="">
      </div>
    </div>
    <bet-item class="bet_item" :quizMathGame="matchInfo.quizMathGame"></bet-item>
  </li>

</template>

<script>
import betItem from "../../components/betItem.vue";
import { fmtDate } from "../../../../libs/utils";
export default {
  components: { betItem },
  props: ["type", "matchInfo"],
  filters: {
    formatByDay(time) {
      return fmtDate(new Date(time), "MM-dd");
    },
    formatByTime(time) {
      return fmtDate(new Date(time), "hh:mm");
    }
  },
  data() {
    return {};
  },
  methods: {
    goUrlPage(matchId, playNo) {
      this.$router.push({
        path: "/guess/allGuess",
        query: { matchId: matchId, playNo: playNo }
      });
    },
    //去充值
    toCharge() {
      this.$emit("toCharge");
    }
  }
};
</script>

<style lang="scss">
@import "../../../../assets/common/_base";

.guessItemPage {
  .battle_team {
    @extend .flex_hc;
    > a {
      flex: 1;
      -webkit-flex: 1;
      &:first-child {
        margin-right: 2.67vw;
      }
      &:last-child {
        margin-left: 2.67vw;
      }
    }
  }
}
</style>


<style lang='scss' scoped>
@import "../../../../assets/common/_base";
@import "../../../../assets/common/_mixin";

.guess_item {
  margin-top: 2.6667vw;
  @include getBgImg("../../../../assets/images/guess/guess_item_bg.png");
  background-size: 100% 100%;
}

.header {
  @extend .flex_v_justify;
  height: 8.53vw;
  padding: 0 2.67vw;
  .title {
    @extend .flex_hc;
    flex: 1 auto;
    -webkit-flex: 1 auto;
    font-size: 0;
    color: #fedcd7;
    white-space: nowrap;
    overflow: hidden;
    p {
      display: inline-block;
      vertical-align: middle;
      @include t_nowrap(70%);
      font-size: 3.73vw;
      line-height: 1.2;
    }
    span {
      display: inline-block;
      vertical-align: middle;
      flex: none;
      -webkit-flex: none;
      padding-right: 2.67vw;
      font-size: 3.2vw;
      color: #a86459;
    }
  }
  .game_logo {
    @extend .flex_v_h;
    width: 6.6667vw;
    height: 6.6667vw;
    margin-right: 2.6667vw;
    background-color: #32191e;
    border-radius: 50%;
    img {
      height: 100%;
      object-fit: contain;
    }
  }
  .more_guess {
    flex: none;
    -webkit-flex: none;
    width: 18vw;
    font-size: 0;
    color: #d6a638;
    white-space: nowrap;
    text-align: right;
    span {
      font-size: 3.2vw;
      vertical-align: middle;
    }
  }
  .link_icon {
    display: inline-block;
    vertical-align: middle;
    width: 3.2vw;
    height: 3.2vw;
    margin-left: 1.07vw;
    @include getBgImg("../../../../assets/images/guess/link_icon.png");
    background-size: 100% 100%;
  }
}

.con {
  padding: 0 2.67vw;
}

.battle_info {
  @extend .flex_v_justify;
  width: 56vw;
  margin: 0 auto;
  padding-top: 3.7333vw;
  text-align: center;
  img {
    width: 9.33vw;
    height: 9.33vw;
    border-radius: 50%;
    object-fit: cover;
  }
}

.before,
.live {
  @extend .flex_v_h;
  width: 14.93vw;
  height: 4.8vw;
  margin: 0 auto;
  font-size: 3.2vw;
  border-radius: 4.8vw;
  color: #fff;
}

.before {
  @include getRadiusBorder(#4f2f2c, all, 4.8vw);
  background-color: #3a1e20;
}

.live {
  background: linear-gradient(to right, #bc3129, #902522);
  background: -webkit-linear-gradient(to right, #bc3129, #902522);
}

.end {
  font-size: 4.8vw;
  color: #fedcd7;
  i {
    padding: 0 2.67vw;
  }
}

.join_num {
  padding-top: 1.33vw;
  font-size: 2.9333vw;
  color: #714038;
}

.bet_item {
  margin-top: 5.0667vw;
  padding: 0 2.67vw 3.2vw;
}
</style>
