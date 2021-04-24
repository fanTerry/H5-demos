<template>
  <div class='Page'>
    <header class='mod_header'>
      <span>投资详情</span>
    </header>
    <div class='main'>
      <div class="mod_vote">
        <div class="left">
          <div class="time"><span>{{issuse}}年最佳选举</span></div>
          <div class="txt" v-if="awardGrades.length == 3">{{awardGrades[0].name}} 深受国民喜欢选举票数强势上涨</div>
          <div class="txt" v-else>无人当选</div>
        </div>
        <div class="right">
          <div class="the_king">
            <img :src="awardGrades[0].grade|goldEmpireAward" v-if='awardGrades[0]' alt="">
          </div>
          <div class="vote_list">
            <div class="vote_item" v-for="(item,index) in awardGrades" :key="index">
              <img :src="item.grade|goldEmpireAward" alt="">
              <p class="name">
                <i class="male_icon" v-if="item.grade == 1"></i>
                <i class="female_icon" v-if="item.grade == 2"></i>
                {{item.grade == 1?'男性当选':item.grade == 2?'女性当选':item.name}}
              </p>
            </div>
          </div>
        </div>
      </div>
      <!-- 选举详情 -->
      <div class="detail">
        <p class="title">选举详情</p>
        <div class="tab">
          <p class="award">回报 <span>{{totaleAward}}</span></p>
          <p class="invest">总投资 <span>{{totaleInvest}}</span></p>
        </div>
        <div class="detail_list">
          <ul>
            <li>
              <span>项目</span>
              <span>投资</span>
              <span>回报</span>
            </li>
            <li v-for="(item,index) in investOrders" :key="index">
              <span>{{item.strformat}}</span>
              <span>{{item.betAmt}}</span>
              <span>{{item.returnAmt}}</span>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <footer class='mod_footer'>
      <div class="return_home">
        <a @click="goUrlPage('investRecord')"><span>选举记录</span></a>
        <a @click="goUrlPage('/game/goldEmpire')"><span>返回王宫</span></a>
      </div>
    </footer>
  </div>
</template>

<script>
import mescroll from '../../../components/common/mescroll.vue';

export default {
  components: { mescroll },
  props: [],
  data() {
    return {
      investOrders: [], //本期选举详情
      awardGrades: [], //本期开奖奖及
      totaleInvest: 0,
      totaleAward: 0,
      issuse: '' //期号
    };
  },
  mounted() {
    this.issuse = this.$route.query.issuse;
    this.orderDetail(this.issuse);
  },
  methods: {
    orderDetail(param) {
      let form = new FormData();
      form.append('issuse', param);
      this.$post('/agency/api/gameOrder/getOrdersByIssuse', form)
        .then(res => {
          this.investOrders = res.data.orders;
          this.awardGrades = res.data.grades;
          for (let index = 0; index < this.investOrders.length; index++) {
            this.totaleInvest += this.investOrders[index].betAmt;
            this.totaleAward += this.investOrders[index].returnAmt;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    goUrlPage(url) {
      this.$router.push({
        path: url
      });
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';

.main {
  padding-bottom: 5.3333vw;
  @include getBgImg('../../../assets/images/game/goldempire/record_bg.png');
  background-size: 100% 100%;
}

.mod_header {
  height: 11.4667vw;
  padding-top: 2.4vw;
  @include getBgImg('../../../assets/images/game/goldempire/title_bg.png');
  background-position: top center;
  text-align: center;
  span {
    font-size: 4vw;
    font-weight: bold;
    background: linear-gradient(
      to bottom,
      rgba(171, 136, 82, 1) 0%,
      rgba(200, 173, 118, 1) 29.78515625%,
      rgba(246, 234, 181, 1) 48.974609375%,
      rgba(252, 239, 202, 1) 79.00390625%,
      rgba(226, 204, 145, 1) 100%
    );
    background: -webkit-linear-gradient(
      top,
      rgba(171, 136, 82, 1) 0%,
      rgba(200, 173, 118, 1) 29.78515625%,
      rgba(246, 234, 181, 1) 48.974609375%,
      rgba(252, 239, 202, 1) 79.00390625%,
      rgba(226, 204, 145, 1) 100%
    );
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}

.mod_vote {
  @extend .flex_v_justify;
  padding: 4.4vw 3.7333vw 0;
  .time {
    width: 38.8vw;
    height: 15.0667vw;
    padding-top: 5.3333vw;
    @include getBgImg('../../../assets/images/game/goldempire/detail_time.png');
    background-size: contain;
    text-align: center;
    span {
      font-size: 3.4667vw;
      font-weight: bold;
      background: linear-gradient(to bottom, rgba(247, 228, 173, 1) 0%, rgba(255, 253, 232, 1) 100%);
      background: -webkit-linear-gradient(top, rgba(247, 228, 173, 1) 0%, rgba(255, 253, 232, 1) 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }
  .txt {
    width: 38.4vw;
    height: 33.0667vw;
    margin: 0 auto;
    padding: 2.4vw 3.2vw 0;
    font-size: 3.2vw;
    line-height: 5.8667vw;
    color: #e1ecf4;
    @include getBgImg('../../../assets/images/game/goldempire/detail_txt.png');
  }
  .the_king {
    @extend .flex_v_h;
    width: 28.5333vw;
    height: 27.6vw;
    margin: 0 auto;
    padding-right: 1.3333vw;
    @include getBgImg('../../../assets/images/game/goldempire/vote_king.png');
    img {
      width: 17.3333vw;
      height: 17.3333vw;
      object-fit: contain;
    }
  }
}

.right {
  width: 51.7333vw;
  height: 46.9333vw;
  border: 1px solid #324152;
  border-radius: 1.3333vw;
}

.vote_list {
  @extend .flex_v_h;
  padding-top: 1.0667vw;
}
.vote_item {
  position: relative;
  width: 12.8vw;
  height: 12.8vw;
  border-radius: 50%;
  text-align: center;
  &:first-child {
    background-color: #27a5bf;
    .name {
      background-color: #113b44;
    }
  }
  &:nth-child(2) {
    margin: 0 2.4vw;
    background-color: #5bc184;
    .name {
      background-color: #143e27;
    }
  }
  &:last-child {
    width: 16vw;
    height: 16vw;
    background-color: #36c3e1;
    .name {
      bottom: 2.1333vw;
      width: 17.7333vw;
      background-color: #154351;
    }
  }
  img {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }
}
.name {
  @extend .g_c_mid;
  @extend .flex_v_h;
  bottom: 0.5333vw;
  width: 14.9333vw;
  height: 4.2667vw;
  font-size: 3.2vw;
  border-radius: 4.8vw;
  color: #fff;
  white-space: nowrap;
}

.male_icon {
  width: 3.4667vw;
  height: 3.4667vw;
  @include getBgImg('../../../assets/images/game/goldempire/male_icon.png');
  background-size: contain;
}

.female_icon {
  width: 2.4vw;
  height: 3.4667vw;
  @include getBgImg('../../../assets/images/game/goldempire/female_icon.png');
  background-size: contain;
}

.detail {
  padding-top: 9.3333vw;
  .title {
    position: relative;
    width: 90.9333vw;
    margin: 0 auto;
    font-size: 4vw;
    font-weight: bold;
    text-align: center;
    background: linear-gradient(to bottom, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
    background: -webkit-linear-gradient(top, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    &::before,
    &::after {
      content: '';
      @extend .g_v_mid;
      width: 24vw;
      height: 4vw;
      @include getBgImg('../../../assets/images/game/goldempire/detail_title_bg.png');
    }
    &::before {
      left: 0;
    }
    &::after {
      right: 0;
      transform: translateY(-50%) rotate(180deg);
      -webkit-transform: translateY(-50%) rotate(180deg);
      @include getBgImg('../../../assets/images/game/goldempire/detail_title_bg.png');
    }
  }
  .tab {
    position: relative;
    @include getBtn(71.8667vw, 7.7333vw, 0, #fff, #245a6a, 7.7333vw);
    @extend .flex_hc;
    margin: 3.7333vw auto 0;
    border: 1px solid#378184;
    &::before {
      content: '';
      @extend .g_v_c_mid;
      width: 1px;
      height: 4.5333vw;
      background-color: #356e7f;
    }
    p {
      flex: 1;
      -webkit-flex: 1;
      font-size: 3.2vw;
    }
  }
  .award {
    span {
      color: #ffe56d;
    }
  }
  .invest {
    span {
      color: #4eb5ff;
    }
  }
}

.detail_list {
  padding-top: 5.3333vw;
  margin: 0 1.0667vw;
  li {
    @extend .flex;
    height: 11.2vw;
    margin-bottom: 0.2667vw;
    font-size: 3.2vw;
    color: #e1ecf4;
    background-color: #31465d;
    &:nth-child(2n) {
      background-color: #2a3b50;
    }
    &:first-child {
      span {
        font-size: 4vw;
        font-weight: bold;
        background: linear-gradient(to bottom, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
        background: -webkit-linear-gradient(top, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }
    }
    span {
      flex: 1;
      @extend .flex_v_h;
    }
  }
}

.return_home {
  @extend .flex_v_h;
  height: 16.2667vw;
  @include getBgImg('../../../assets/images/game/goldempire/footer_bg.png');
  a {
    width: 41.6vw;
    height: 12.6667vw;
    margin: 0 4vw;
    padding: 3.4667vw 0 0 9.3333vw;
    @include getBgImg('../../../assets/images/game/goldempire/return_btn.png');
    text-align: center;
  }
  span {
    font-size: 4.8vw;
    font-weight: bold;
    background: linear-gradient(to bottom, rgba(207, 185, 150, 1) 0%, rgba(255, 251, 241, 1) 78.564453125%);
    background: -webkit-linear-gradient(top, rgba(207, 185, 150, 1) 0%, rgba(255, 251, 241, 1) 78.564453125%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}
</style>
