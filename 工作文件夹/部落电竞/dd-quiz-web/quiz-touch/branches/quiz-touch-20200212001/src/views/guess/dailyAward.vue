<template>
  <div class="ui_pop">
    <div class="Page">
      <a class='close' @click="close()"></a>
      <p class="txt">预测正确，获得星星</p>
      <p class="value">{{winPrize}}</p>
      <a class="know" @click="close()"></a>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: ["winPrize"],
  data() {
    return {};
  },

  methods: {
    close() {
      this.$emit("closeDailyAward");
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../assets/common/_base";
@import "../../assets/common/_mixin";

.Page {
  position: relative;
  @extend .flex_v_h;
  width: 100vw;
  height: 102.9333vw;
  @include getBgImg("../../assets/images/guess/congrats.gif");
  background-color: transparent;
}
.close {
  position: absolute;
  right: 5.3333vw;
  top: 3.2vw;
  width: 8vw;
  height: 8vw;
  border-radius: 50%;
  @include getBgImg("../../assets/images/guess/close.png");
}

.txt {
  margin-top: 44.8vw;
  font-size: 4.5333vw;
  color: #fff2f0;
}

.value {
  padding: 5.3333vw 0;
  font-size: 10.6667vw;
  font-weight: bold;
  color: #fff300;
}

.know {
  width: 42.4vw;
  height: 10.4vw;
  color: #fff;
}
</style>
