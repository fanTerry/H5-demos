<template>
  <div class='Page'>
    <header class='mod_header'>
      <nav-bar :pageTitle="'消息中心'"></nav-bar>
    </header>
    <div class='main'>
      <div class="match_tab">
        <span class="active">公告</span>
        <span>全部</span>
        <span>消息</span>
      </div>
      <!-- 置顶公告栏，只在全部里显示 -->
      <section class="message up_notice">
        <div class="con">
          <h3>公告栏</h3>
          <div class="info">
            <p class="title">
              <span>未成年人禁止注册公告</span>
              <span>2020-05-14 21:22:00</span>
            </p>
            <p class="txt">枫叶电竞将于5月18日针对用户年龄进行核查，针对未成年用户将采取禁止预测等功能限制，望周知～</p>
          </div>
        </div>
      </section>
      <!-- 公告信息加上notice -->
      <section class="message ">
        <p class="date">2020-04-14 21:22:00</p>
        <div class="con">
          <h3 class="active">消息通知</h3>
          <div class="info">
            <p class="title">
              <span>恭喜你中奖</span>
            </p>
            <p class="txt">枫叶电竞将于5月18日针对用户年龄进行核查，针对未成年用户将采取禁止预测等功能限制，望周知～</p>
          </div>
        </div>
      </section>
    </div>
    <footer class='mod_footer'>

    </footer>
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
  methods: {}
};
</script>

<style lang='scss' scoped>
@import "../../assets/common/_mixin";
@import "../../assets/common/_base";

.main {
  @include getBgLinear(bottom, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.05) 100%);
  background-color: $color_main;
}

.match_tab {
  position: sticky;
  position: -webkit-sticky;
  top: 0;
  z-index: 1;
  @extend .flex_v_justify;
  margin: 0 22.6667vw;
  overflow: hidden;
  span {
    display: inline-block;
    padding: 4.5333vw 0;
    font-size: 3.7333vw;
    color: rgba(255, 255, 255, 0.5);
    border-radius: 3px;
    &.active {
      position: relative;
      color: #fff;
      &::after {
        content: "";
        @extend .g_c_mid;
        bottom: 1.0667vw;
        width: 4vw;
        height: 1.0667vw;
        border-radius: 0.8vw;
        background-color: #fff;
      }
    }
  }
}

.date {
  font-size: 3.7333vw;
  line-height: 4.2667vw;
  text-align: center;
  color: #fff;
}

.message {
  padding: 0 4.2667vw;
  .con {
    margin: 2.1333vw 0;
    background-color: #fff;
    border-radius: 1.3333vw;
  }
  h3 {
    position: relative;
    padding-left: 9.6vw;
    font-size: 3.7333vw;
    line-height: 9.3333vw;
    font-weight: normal;
    @include getBorder(bottom, #ddd);
    &::before {
      content: "";
      @extend .g_v_mid;
      left: 3.2vw;
      width: 4.2667vw;
      height: 4.2667vw;
      @include getBgImg("../../assets/images/guess/message_icon.png");
      background-size: contain;
    }
    &.active::after {
      content: "";
      @extend .g_v_mid;
      right: 3.2vw;
      width: 2.4vw;
      height: 2.4vw;
      border-radius: 50%;
      background-color: #d63941;
    }
  }
  .info {
    padding: 3.2vw;
  }
  .title {
    @extend .flex_v_justify;
    span {
      font-size: 3.7333vw;
      line-height: 4.2667vw;
      font-weight: bold;
      color: #333;
    }
  }
  .txt {
    padding-top: 2.1333vw;
    font-size: 3.2vw;
    line-height: 3.7333vw;
    color: #999;
    text-align: justify;
  }
}

.notice {
  h3 {
    &::before {
      @include getBgImg("../../assets/images/guess/notice_att_icon.png");
      background-size: contain;
    }
  }
}

.up_notice {
  .con {
    position: relative;
    &::before,
    &::after {
      content: "";
      position: absolute;
      top: -2.2667vw;
      width: 2.4vw;
      height: 5.3333vw;
      @include getBgImg("../../assets/images/guess/notice_icon.png");
    }
    &:before {
      left: 6.1333vw;
    }
    &:after {
      right: 6.1333vw;
    }
  }
  h3 {
    padding-left: 0;
    font-size: 5.3333vw;
    line-height: 10.6667vw;
    text-align: center;
    font-weight: bold;
    color: #d63941;
    &::before {
      display: none;
    }
  }
  .info {
    padding: 5.8667vw 3.2vw;
  }
}
</style>
