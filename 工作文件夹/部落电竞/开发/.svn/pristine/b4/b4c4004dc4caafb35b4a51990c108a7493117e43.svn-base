<template>
  <!-- 已结束的方案加end_project -->
  <li class="expert_project" :class="{'end_project':false}" @click="goUrlPage('/followPlan/recommendPlanDetail')">
    <!-- right_tag 红单,wrong_tag 黑单 -->
    <span class="tag" :class="flag?'right_tag':'wrong_tag'"></span>
    <div class="user_info">
      <img src="" alt="">
      <p class="name">电竞打卡</p>
      <div class="tips">
        <span>近10中5</span>
        <span>近十单收益：1250%</span>
      </div>
    </div>
    <p class="match_info" v-if="recommendDetail!=null"><img :src="recommendDetail.videoGameId|gameLogo" alt="">{{recommendDetail.updateTime |dateFmt}} 截止</p>
    <ul class="match_table">
      <li class="title">
        <span>发布时间</span>
        <span>方案单价</span>
        <span>购买人数</span>
        <span>购买金额</span>
      </li>
      <li v-if="recommendDetail!=null">
        <span>{{recommendDetail.recommendAmount}}</span>
        <span>{{recommendDetail.expectedReturnRate}}</span>
        <span>{{recommendDetail.followCount}}</span>
        <span>{{recommendDetail.followAmount}}</span>
      </li>
    </ul>
    <div class="reason">
      <div class="title">推荐理由</div>
      <p class="txt">V5这只战队从进入LPL以来，成绩始终不够理想， 此前他们还有过在赛季中期临时采用前退役选手 OTTO担任中单的行为，俱乐部管理也不够职业。 本赛季8轮较量过后，他们始终难以获胜。</p>
    </div>
    <a class="guess_now">立即预测</a>
    <a class='pay_project_btn'>支付22元 查看<span>(30人已支付)</span></a>

    <!-- 投注弹窗 -->
    <!-- <bet-pop></bet-pop> -->
  </li>
</template>

<script>
import betPop from '../../components/betpops.vue';
import guessItem from '../../home/components/guessItem.vue';

export default {
  components: { betPop, guessItem },
  props: ['recommendDetail'],
  data() {
    return {};
  },
  methods: {
    goUrlPage(url) {
      this.$router.push({
        path: url,
        query: {}
      });
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../../assets/common/_base';
@import '../../../../assets/common/_mixin';

.expert_project {
  position: relative;
  margin: 2.1334vw 0;
  padding: 3.2vw;
  border-radius: 1.3334vw;
  background-color: #fff;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
}
.end_project {
  background-color: #912d32;
  .user_info,
  .follow_plan_btn {
    display: none;
  }
  .match_table {
    &,
    & span {
      color: #fff;
      border-color: #b7565c;
    }

    .title {
      background-color: #b7565c;
    }
  }
  .match_info {
    color: #fff;
  }
}
.tag {
  position: absolute;
  right: 0;
  top: 0;
  z-index: 10;
  width: 20vw;
  height: 19.2vw;
}
.right_tag {
  @include getBgImg('../../../../assets/images/followplan/right_tag.png');
}
.wrong_tag {
  @include getBgImg('../../../../assets/images/followplan/wrong_tag.png');
}
.user_info {
  position: relative;
  @extend .flex_hc;
  margin-bottom: 2.4vw;
  img {
    width: 7.4667vw;
    height: 7.4667vw;
    border: 0.2667vw solid #ff9da3;
    border-radius: 50%;
  }
  .name {
    margin-left: 2.1333vw;
    font-size: 3.7333vw;
    @include t_nowrap(26.6667vw);
    line-height: 1.2;
    color: #333;
    font-weight: bold;
  }
  .tips {
    @extend .flex_hc;
    @extend .g_v_mid;
    right: 0;
    span {
      margin-left: 1.3333vw;
      padding: 0.8vw 1.8667vw;
      font-size: 2.4vw;
      color: #fff;
      border-radius: 0.8vw;
      background-color: $color_main;
    }
  }
}
.match_info {
  @extend .flex_hc;
  font-size: 3.7333vw;
  line-height: 4vw;
  color: #333;
  img {
    width: 4.2667vw;
    height: 4.2667vw;
    margin-right: 2.1333vw;
    border-radius: 50%;
    background-color: #32191e;
  }
}
.match_table {
  // margin-top: 2vw;
  border: 0.2667vw solid #ddd;
  li {
    @extend .flex;
    span {
      flex: 1;
      -webkit-flex: 1;
      border-left: 0.2667vw solid #ddd;
      font-size: 3.4667vw;
      line-height: 8.8vw;
      text-align: center;
      color: #333;
      &:first-child {
        border: none;
      }
    }
  }
  .title {
    background-color: #f4f4f4;
  }
}

.reason {
  margin-top: 6.4vw;
  .title {
    font-size: 3.7333vw;
    line-height: 4.2667vw;
    color: #333;
    font-weight: bold;
    text-align: center;
  }
  .txt {
    margin-top: 2.1333vw;
    padding: 2.2667vw 5.3333vw 2.2667vw 3.2vw;
    font-size: 3.4667vw;
    line-height: 5.8667vw;
    color: #333;
    background-color: #f4f4f4;
  }
}

.guess_now {
  display: block;
  margin-top: 6.4vw;
  @include getBtn(auto, 10.6667vw, 3.7333vw, #fff, $color_main, 0.8vw);
}

.pay_project_btn {
  display: block;
  margin-top: 2.1333vw;
  @include getBtn(auto, 10.6667vw, 3.7333vw, #fff, $color_main, 0.8vw);
  span {
    margin-left: 2.6667vw;
    font-size: 3.2vw;
  }
}
</style>
