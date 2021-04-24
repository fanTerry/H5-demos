<template>
  <h2 class="nav_bar">
    <div class="back" @click="goBackHome" v-if="type"></div>
    <div class="back" @click="goBack" v-else></div>
    {{pageTitle}}
    <slot></slot>
    <!-- 注册页面用到 遇到问题 -->
    <!-- <a class="meet_problem">遇到问题?</a> -->
  </h2>
</template>

<script>
export default {
  props: ["pageTitle", "type"],
  data() {
    return {};
  },
  methods: {
    goBack() {
      this.$router.go(-1);
      if (window.history.length <= 1) {
        this.$router.push("/guess/home");
      }
    },
    goBackHome() {
      this.$router.push("/guess/home");
    }
  },

  components: {}
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_mixin";
@import "../../../assets/common/_base";
@import "../../../assets/common/_var";
.nav_bar {
  position: relative;
  padding: 0 40px;
  font-size: 19px !important;
  line-height: 44px;
  font-weight: normal;
  color: #333;
  text-align: center;
  @include t_nowrap(100%);
  .meet_problem {
    @extend .g_v_mid;
    right: 20px;
    color: #999;
    font-size: 13px;
  }
}
// 回退按钮
.back {
  position: absolute;
  left: 0;
  top: 0;
  z-index: 1;
  width: 44px;
  height: 44px;
  @include getArrow(12px, #525252, left);
  &:before,
  &:after {
    width: 2px;
  }
}
</style>
