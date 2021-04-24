<template>
  <div class="god_card">
    <div class="god_info">
      <img src="" alt="">
      <div>
        <p class="name">一二三四五六七八九十</p>
        <p class="rate">近10中5</p>
      </div>
      <div class="options">
        <div>
          <p>168%</p>
          <span>十场胜率</span>
        </div>
        <div>
          <p>168%</p>
          <span>十场胜率</span>
        </div>
        <div class="follow_button">
          <p>关注</p>
          <span>28</span>
        </div>
      </div>
    </div>
    <p class="intro">专业电竞选手20年，历经沧桑终成摆渡人</p>
  </div>
</template>

<script>
export default {
  components: {},
  props: [],
  data() {
    return {};
  },
  methods: {}
};
</script>

<style lang='scss' scoped>
@import '../../../../../assets/common/_base';
@import '../../../../../assets/common/_mixin';

.god_card {
  padding: 0 4.2667vw;
  background-color: #fff;
}

.god_info {
  position: relative;
  @extend .flex_hc;
  padding: 3.7333vw 0;
  img {
    width: 10.4vw;
    height: 10.4vw;
    margin-right: 3.2vw;
    border: 0.2667vw solid #ff9da3;
    border-radius: 50%;
  }
  .name {
    @include t_nowrap(29.3333vw);
    font-size: 3.7333vw;
    color: #333;
  }
  .rate {
    display: inline-block;
    margin-top: 0.8vw;
    padding: 0.8vw 1.8667vw;
    font-size: 2.4vw;
    color: #fff;
    border-radius: 0.8vw;
    background-color: $color_main;
  }
}

.options {
  @extend .g_v_mid;
  right: 0;
  @extend .flex_hc;
  text-align: center;
  > div {
    margin-left: 2.1333vw;
  }
  p {
    font-size: 3.2vw;
    line-height: 3.7333vw;
    color: #333;
    font-weight: bold;
  }
  span {
    display: block;
    margin-top: 1.0667vw;
    padding: 0.8vw 1.8667vw;
    font-size: 2.4vw;
    color: #333;
    background-color: #f4f4f4;
    border-radius: 0.8vw;
  }
  .follow_button {
    padding: 1.8667vw 4vw;
    border-radius: 0.8vw;
    @include getBgLinear(bottom, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.05) 100%);
    background-color: $color_main;
    p,
    span {
      color: #fff;
      background-color: transparent;
    }
  }
}

.intro {
  padding: 3.7333vw 0;
  font-size: 3.4667vw;
  line-height: 4vw;
  color: #666;
  border-top: 0.2667vw dashed #ddd;
}
</style>
