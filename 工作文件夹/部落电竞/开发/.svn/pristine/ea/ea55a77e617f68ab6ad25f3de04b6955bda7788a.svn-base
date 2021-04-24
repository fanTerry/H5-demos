<template>
  <div class='Page' v-if="showFlag">
    <header class='mod_header'>
      <div class="tab" :class="{bg_change:showTab == 2}">
        <span :class="{active:showTab == 1}" @click="showTab = 1">开奖记录</span>
        <span :class="{active:showTab == 2}" @click="showTab = 2">开奖趋势</span>
      </div>
    </header>
    <div class='main'>
      <!-- 开奖记录 -->
      <div class="mod_vote" v-if="showTab == 1">
        <div class="title">
          <div class="project">
            <span>项目</span>
          </div>
          <div class="sex">
            <span>男性</span>
            <span>女性</span>
          </div>
          <div class="party">
            <span>男元老</span>
            <span>女元老</span>
            <span>男民主</span>
            <span>女民主</span>
          </div>
          <div class="award">
            <span>回报</span>
          </div>
        </div>
        <ul class="vote_list">
          <li class="item" v-for="(item,index) in recordListData" :key="index">
            <div class="project">
              <span>{{item.issuse}}</span>
            </div>
            <div class="sex">
              <span class="male" :class="{active:item.res[0] == 1}"></span>
              <span class="female" :class="{active:item.res[0] == 2}"></span>
            </div>
            <div class="party">
              <span :class="{active:item.res[1] == 5}"></span>
              <span :class="{active:item.res[1] == 7}"></span>
              <span :class="{active:item.res[1] == 6}"></span>
              <span :class="{active:item.res[1] == 8}"></span>
            </div>
            <div class="award">
              <img :src="item.res[2]|goldEmpireAward" alt="">
            </div>
          </li>
        </ul>
        <span class="line"></span>
      </div>
      <!-- 开奖趋势 -->
      <div class="mod_tendency" v-if="showTab == 2">
        <div class="title"><span>今日最佳投资项目</span></div>
        <ul class="list">
          <li class="item" v-for="(item,index) in lotteryRecordData" :key="index">
            <span class="name">{{item.name}}</span>
            <p class="bar"><span :style="{width:item.frequency/30*100+'%'}"></span>{{item.frequency}}</p>
          </li>
        </ul>
      </div>
      <a class='pull_up' @click="showPage(false)"></a>
    </div>
    <footer class='mod_footer'>

    </footer>
  </div>
</template>

<script>
export default {
  components: {},
  props: ['lotteryRecordData', 'recordListData'],
  data() {
    return {
      showFlag: false,
      showTab: 1
    };
  },
  mounted() {},
  methods: {
    showPage(parma) {
      this.showFlag = parma;
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';

.Page {
  position: fixed;
  left: 0;
  top: 0;
  z-index: 999;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
}

.tab {
  @extend .flex_vc;
  height: 11.7333vw;
  @include getBgImg('../../../assets/images/game/goldempire/vote_title_bg.png');
  text-align: center;
  &.bg_change {
    @include getBgImg('../../../assets/images/game/goldempire/bg_change_title.png');
  }
  span {
    padding: 2.4vw 3.3333vw;
    font-size: 4vw;
    font-weight: bold;
    background: linear-gradient(0deg, rgba(137, 182, 219, 1) 0%, rgba(179, 221, 255, 1) 85.3271484375%, rgba(120, 174, 218, 1) 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    &.active {
      background: linear-gradient(
        0deg,
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
}

.main {
  position: relative;
  flex: none;
  -webkit-flex: none;
  overflow: initial;
  min-height: 146.6667vw;
  padding: 0 1.0667vw;
  @include getBgImg('../../../assets/images/game/goldempire/record_bg.png');
  background-size: 100% 100%;
}

.project {
  width: 10.6667vw;
}
.sex {
  width: 21.3333vw;
}
.party {
  flex: 1;
  -webkit-flex: 1;
}
.award {
  @extend .flex_vc;
  width: 10.6667vw;
}

.project,
.sex,
.party,
.award {
  @extend .flex;
  span {
    flex: 1;
    -webkit-flex: 1;
    @extend .flex_v_h;
    color: #e1ecf4;
  }
}
.mod_vote {
  position: relative;
  &::before,
  &::after,
  .line {
    content: '';
    position: absolute;
    top: 0;
    transform: scaleX(0.5);
    -webkit-transform: scaleX(0.5);
    width: 1px;
    height: 100%;
    background-color: rgba(255, 255, 255, 0.2);
  }
  &::before {
    left: 10.6667vw;
  }
  &::after {
    left: 32vw;
  }
  .line {
    right: 10.6667vw;
  }
  .title {
    @extend .flex_hc;
    height: 8vw;
    span {
      background: linear-gradient(to bottom, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
      background: -webkit-linear-gradient(top, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }
}

.vote_list {
  li {
    @extend .flex;
    height: 9.8667vw;
    margin-top: 0.2667vw;
    background-color: #283b50;
    &:nth-child(2n) {
      background-color: #2f475f;
    }
  }
  .sex span {
    &.active {
      @include getBgImg('../../../assets/images/game/goldempire/sex_icon.png');
      background-size: 5.7333vw;
    }
  }
  .party span {
    &.active {
      @include getBgImg('../../../assets/images/game/goldempire/party_icon.png');
      background-size: 5.7333vw;
    }
  }
  .award {
    img {
      width: 8.2667vw;
      height: 9.0667vw;
      object-fit: contain;
    }
  }
}

.mod_tendency {
  .title {
    position: relative;
    background-color: #2f475e;
    text-align: center;
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
    span {
      position: relative;
      height: 8vw;
      font-size: 4vw;
      line-height: 8vw;
      font-weight: bold;
      text-align: center;
      background: linear-gradient(to bottom, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
      background: -webkit-linear-gradient(top, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }
  }
  .list {
    position: relative;
    &::before {
      content: '';
      position: absolute;
      left: 29.6vw;
      top: 0;
      transform: scaleX(0.5);
      -webkit-transform: scaleX(0.5);
      width: 1px;
      height: 100%;
      background-color: rgba(255, 255, 255, 0.2);
    }
  }
  .item {
    @extend .flex_hc;
    height: 9.8667vw;
    margin-top: 0.2667vw;
    background-color: #2a3b50;
    &:nth-child(2n) {
      background-color: #2f475f;
    }
    .name {
      width: 29.6vw;
      padding-left: 2.1333vw;
      font-size: 3.2vw;
      color: #e1ecf4;
    }
    &:nth-child(1),
    &:nth-child(2),
    &:nth-child(3) {
      .bar {
        color: #2580d2;
        span {
          background-color: #2580d2;
        }
      }
    }
    &:nth-child(4),
    &:nth-child(5),
    &:nth-child(6) {
      .bar {
        color: #39aa3c;
        span {
          background-color: #39aa3c;
        }
      }
    }
    &:nth-child(7),
    &:nth-child(8),
    &:nth-child(9) {
      .bar {
        color: #c2b13f;
        span {
          background-color: #c2b13f;
        }
      }
    }
    &:nth-child(10),
    &:nth-child(11),
    &:nth-child(12) {
      .bar {
        color: #c2423f;
        span {
          background-color: #c2423f;
        }
      }
    }
  }
  .bar {
    flex: 1;
    -webkit-flex: 1;
    @extend .flex_hc;
    padding-left: 1.3333vw;
    font-size: 4vw;
    color: #fff;
    span {
      height: 4vw;
      margin-right: 1.3333vw;
      border-radius: 5.3333vw;
    }
  }
}

.pull_up {
  position: absolute;
  left: 50%;
  bottom: 0;
  transform: translate(-50%, 50%);
  -webkit-transform: translate(-50%, 50%);
  width: 12.2667vw;
  height: 12.6667vw;
  @include getBgImg('../../../assets/images/game/goldempire/pull_up.png');
}
</style>
