<template>
  <div class="Page bjnewsPage">
    <header class="mod_header">
      <navBar :pageTitle="'关注枫叶电竞公众号'"></navBar>
    </header>
    <div class="main">
      <div class="office">
        <img src="../../assets/images/guess/office_code.png" alt="">
        <p>长按图片识别二维码<br>关注公众号</p>
        <p class="office_txt">获取实时中奖通知和更多活动信息</p>
        <!-- <img class="hide_img" src="../../assets/images/guess/office_code.png" alt=""> -->
      </div>
    </div>
  </div>
</template>

<script>
import navBar from "../../components/header/nav_bar/index.vue";
export default {
  components: { navBar },
  props: [],
  data() {
    return {};
  },
  mounted() {
    let step1 = window.sessionStorage.getItem("step1");
    if (!step1) {
      window.sessionStorage.setItem("step1", true);
      window.location.reload();
    } else {
      let step2 = window.sessionStorage.getItem("step2");
      if (!step2) {
        window.sessionStorage.setItem("step2", true);
        window.location.reload();
      }
    }
  },
  methods: {}
};
</script>

<style lang="scss">
.bjnewsPage {
  background: #1f1212;
  .mod_header {
    background-color: #321919;
  }
  .nav_bar {
    color: #fedcd7 !important;
  }
  .back {
    &::before,
    &::after {
      background-color: #a26767 !important;
    }
  }
}
</style>


<style lang='scss' scoped>
@import "../../assets/common/_base";
@import "../../assets/common/_mixin";

.main {
  @extend .flex_v_h;
  flex-direction: column;
  -webkit-flex-direction: column;
}

.office {
  position: relative;
  margin-top: -13.3333vw;
  margin-left: -2.6667vw;
  padding: 50.6667vw 12vw 6.6667vw 15.7333vw;
  font-size: 4.5333vw;
  color: #fff2f0;
  text-align: center;
  @include getBgImg("../../assets/images/guess/office_bg.png");
  .close {
    position: absolute;
    top: 18.8vw;
    right: 0;
    z-index: 1;
    width: 8vw;
    height: 8vw;
    @include getBgImg("../../assets/images/guess/close.png");
    background-size: contain;
  }
  img {
    position: relative;
    z-index: 10;
    width: 57.3333vw;
    height: 57.3333vw;
    object-fit: cover;
    -webkit-touch-callout: inherit !important;
    // @include getBgImg("../../assets/images/guess/office_code.png");
    // background-size: contain;
    // background-position: top center;
  }
  p {
    margin-top: 4vw;
    line-height: 6.4vw;
  }
  .office_txt {
    font-size: 3.2vw;
    line-height: 1;
  }
  .hide_img {
    @extend .g_v_c_mid;
    width: 100%;
    height: 100%;
    object-fit: contain;
    opacity: 0;
    z-index: 0;
  }
}
</style>
