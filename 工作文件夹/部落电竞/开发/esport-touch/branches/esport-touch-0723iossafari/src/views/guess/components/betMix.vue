<template>
  <div class="bet_mix">
    <div class="mask"></div>
    <div class="bet_info">
      <div class="bet_title">
        <div class="title_left">
          <i class="num">8</i>
          <span>投注单</span>
        </div>
        <div class="title_right">
          <span>1注 8串1</span>
          <span class="money">72.43</span>
        </div>
      </div>
      <div class="bet_list">
        <ul>
          <li v-for="(item,index) in 5" :key="index">
            <div>
              <p class="name">OMGing</p>
              <p class="vs">
                <span class="team">OMGing vs LGD Life Sport</span>
                <span class="title">全场比赛结果</span>
              </p>
            </div>
            <div class="flex_hc">
              <span class="odds">3.16</span>
              <a class='close'></a>
            </div>
          </li>
        </ul>
      </div>

      <bet-pop></bet-pop>
    </div>
  </div>
</template>

<script>
import betPop from './betpops.vue';

export default {
  components: { betPop },
  props: [],
  data() {
    return {};
  },
  methods: {}
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';
.bet_mix {
}

.bet_info {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.bet_title {
  @extend .flex_v_justify;
  height: 14.4vw;
  padding: 0 4.2667vw;
  background-color: #242330;
  color: rgba(255, 255, 255, 0.5);
  .item_left {
    font-size: 3.7333vw;
  }
  .item_right {
    font-size: 2.9333vw;
  }
  .num {
    @include getCircle(6.1333vw, #5f5a89, #fff);
    margin-right: 4.2667vw;
    border: 0.4vw solid #fff;
    box-sizing: content-box;
  }
  .money {
    padding-left: 3.3333vw;
    font-size: 4.2667vw;
    color: #609b8b;
  }
}

.bet_list {
  max-height: 61.3333vw;
  overflow: auto;
  -webkit-overflow-scrolling: touch;
  background-color: $color_item;
  li {
    @extend .flex_v_justify;
    height: 13.0667vw;
    margin-top: 0.5333vw;
    padding: 0 4.2667vw;
    background-color: #353447;
  }
  .name {
    font-size: 3.7333vw;
    line-height: 4.2667vw;
    color: #609b8b;
  }
  .vs {
    font-size: 2.6667vw;
    line-height: 3.2vw;
    color: rgba(255, 255, 255, 0.5);
    .team {
      @include t_nowrap(33.3333vw);
    }
    .title {
      margin-left: 8vw;
      width: 16vw;
      text-align: center;
    }
  }
  .odds {
    font-size: 3.4667vw;
    color: #609b8b;
  }
  .close {
    @include getClose(2.6667vw, #fff);
    position: relative;
    margin-left: 6.9333vw;
    border: 1px solid #fff;
    border-radius: 50%;
  }
}
</style>

