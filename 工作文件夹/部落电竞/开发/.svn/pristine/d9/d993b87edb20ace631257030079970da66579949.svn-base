<template>
  <div class="ui_pop">
    <div class="pay_project">
      <h3 class="title">方案购买<span class="close" @click=""></span></h3>
      <div class="con">
        <p class="prize">22.00</p>
        <p class="follow"><i class="active"></i>关注该专家</p>
        <p class="tips">您将支付22元购买电竞大咖的预测方案，请您确认</p>
      </div>
      <a class="confirm_btn">确认</a>
    </div>
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
@import '../../../../assets/common/_base';
@import '../../../../assets/common/_mixin';

.pay_project {
  width: 89.3333vw;
  border-radius: 1.3333vw;
  overflow: hidden;
}
.title {
  position: relative;
  @include getBtn(100%, 9.6vw, 4.2667vw, #333, #f4f4f4, 0);
  font-weight: bold;
  span {
    @extend .g_v_mid;
    right: 0;
    @include getClose(4.8vw, #999);
  }
}
.con {
  padding: 13.8667vw 0;
  background-color: #fff;
  text-align: center;
}

.prize {
  font-size: 12vw;
  line-height: 12.5333vw;
  font-weight: bold;
  color: $color_main;
}

.follow {
  @extend .flex_v_h;
  margin-top: 8.5333vw;
  font-size: 4vw;
  color: #333;
  i {
    width: 5.3333vw;
    height: 5.3333vw;
    margin-right: 2.1333vw;
    border: 1px solid $color_main;
    border-radius: 0.8vw;
    &.active {
      @include getBgImg('../../../../assets/images/followplan/select.png');
      background-size: 4.2667vw;
    }
  }
}
.tips {
  padding-top: 4.2667vw;
  font-size: 3.4667vw;
  line-height: 4vw;
  color: #999;
}
.confirm_btn {
  display: block;
  @include getBtn(100%, 9.6vw, 4vw, #fff, $color_main, 0);
  font-weight: bold;
}
</style>
