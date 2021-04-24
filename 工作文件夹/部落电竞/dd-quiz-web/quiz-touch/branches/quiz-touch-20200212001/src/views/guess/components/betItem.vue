<template>
  <div>
    <h3>
      <span class="round">[{{quizMathGame.gameNumber|gameFightNum }}]</span> {{quizMathGame.subjectName}}<br>
      <!-- <span class="end_time" v-if="quizMathGame.deadLine">{{quizMathGame.deadLine}}截止</span> -->
    </h3>
    <div class="battle_team">
      <!-- 下单结束加上disabled -->
      <a v-for="(item,index) in quizMathGame.quizOptions" :key="index"
        :class="{select:getBetData.toCurBet == matchGameId+'_'+index}" @click="selectItem(item,index,$event)">
        <p>{{item.teamName}} {{item.name}}</p>
        <span> {{item.odds.toFixed(2)}}</span>
        <!-- 已经下注的选项-->

        <span class="selected"
          v-if='item.quizzedCount>0||(item.index == userSelectItem.index && userSelectItem.quizzedCount>0)'></span>
        <!-- 下单弹窗 -->
        <bet-pops ref="uiPop" v-if="getBetData.toCurBet==matchGameId+'_'+index" :selectItem="userSelectItem">
        </bet-pops>
      </a>
    </div>
  </div>
</template>

<script>
import betPops from "./betpops.vue";
import { mapGetters, mapActions } from "vuex";
import { gameFightNum } from "../../../filters";
export default {
  components: { betPops },
  props: ["quizMathGame"],
  filters: {
    gameFightNum(num) {
      return gameFightNum(num + "");
    }
  },
  data() {
    return {
      showProp: false,
      userSelectItem: Object,
      matchGameId: null //quiz_match_game表中的id
    };
  },
  computed: {
    ...mapGetters({
      getBetData: "getBetData"
    })
  },
  mounted() {
    this.matchGameId = this.quizMathGame.id;
  },
  watch: {
    quizMathGame(newVal, oldVal) {
      this.matchGameId = newVal.id;
      console.warn(newVal.id, "newVal.id");
    }
  },
  methods: {
    ...mapActions(["setBetData"]),
    selectItem(item, index, e) {
      //e.stopPropagation();
      console.log(item, "selectItem");
      console.log(this.quizMathGame, "this.quizMathGame");
      this.showProp = true;
      let toCurBet = this.matchGameId + "_" + index;
      this.userSelectItem = item;
      this.userSelectItem.matchGameId = this.matchGameId;
      this.userSelectItem.matchNo = this.quizMathGame.matchNo;
      this.userSelectItem.playNo = this.quizMathGame.playNo;
      this.userSelectItem.homeTeamName = this.quizMathGame.homeTeamName;
      this.userSelectItem.awayTeamName = this.quizMathGame.awayTeamName;
      this.userSelectItem.subjectName = this.quizMathGame.subjectName;

      this.setBetData({ toCurBet: toCurBet });
    },
    //刷新用户已猜状态
    reflushQuizzedCount() {
      this.userSelectItem.quizzedCount = +1;
    },
    //去充值
    toCharge() {
      this.$parent.toCharge();
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_base";
@import "../../../assets/common/_mixin";

h3 {
  @include line_clamp(2);
  font-size: 3.2vw;
  line-height: 1.4;
  color: #fedcd7;
  font-weight: normal;
  &:before {
    content: "";
    display: inline-block;
    vertical-align: top;
    width: 1.07vw;
    height: 3.73vw;
    margin-top: 0.1333vw;
    border-radius: 2px;
    background: linear-gradient(to bottom, #f7d64b, #c4480a);
    background: -webkit-linear-gradient(top, #f7d64b, #c4480a);
  }
  .round {
    padding-left: 1.3333vw;
    color: #feca14;
  }
}

.battle_team {
  padding-top: 0.5333vw;
  &::after {
    content: "";
    display: table;
    clear: both;
    overflow: hidden;
  }
  a {
    position: relative;
    float: left;
    width: 31%;
    margin-right: 3%;
    margin-top: 2.67vw;
    padding: 1.3333vw 0 2vw;
    text-align: center;
    @include getBgLinear(right bottom, #361a19, #251214);
    border-radius: 6px;
    @include getRadiusBorder(#463025, all, 6px);
    &:nth-child(3n) {
      margin-right: 0;
    }
    &.select {
      position: relative;
      @include getRadiusBorder(#bb3129, all, 6px);
      &::before {
        border-width: 4px;
      }
      &:after {
        content: "";
        position: absolute;
        right: -1.33vw;
        top: -1.33vw;
        width: 4.8vw;
        height: 4.8vw;
        @include getBgImg("../../../assets/images/guess/select.png");
        background-size: contain;
      }
    }

    &.disabled {
      background-color: #954735;
      opacity: 0.2;
    }
    p {
      @include t_nowrap(100%);
      padding: 0 0.6667vw;
      font-size: 3.2vw;
      line-height: 1.2;
      font-weight: bold;
      color: #fedcd7;
    }
    span {
      display: inline-block;
      padding-top: 0.9333vw;
      font-size: 2.6667vw;
      color: #fcb5ab;
    }
  }
}

.end_time {
  padding-left: 2.67vw;
  font-size: 3.2vw;
  color: #86564e;
}

.selected {
  position: absolute;
  right: 0;
  top: 0;
  z-index: 1;
  width: 9.3333vw;
  height: 8.2667vw;
  @include getBgImg("../../../assets/images/guess/guessed.png");
}
</style>
