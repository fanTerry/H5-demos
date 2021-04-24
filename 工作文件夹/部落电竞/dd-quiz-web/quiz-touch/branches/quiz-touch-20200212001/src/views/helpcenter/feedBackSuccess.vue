<template>
  <div class="Page">
    <header class="mod-header">
      <nav-bar :pageTitle="'反馈成功'"></nav-bar>
    </header>
    <div class="main">
      <div class="cmt_succ">
        <i></i>
        <h3>感谢您的反馈</h3>
        <p>每一个声音我们都用心倾听</p>
      </div>
    </div>
    <footer class="mod_footer">
      <div class="cmt_btns" @click="toHelpCenter()">
        <a href="javascript:;" class="cmt_btn">回到帮助中心</a>
      </div>
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
  methods: {
    toHelpCenter() {
      this.$router.push({
        path: "/helpCenter"
      });
    }
  }
};
</script>

<style lang='scss' scoped>
.cmt_succ {
  text-align: center;
  margin-top: 10vw;
}
.cmt_succ i {
  display: inline-block;
  width: 15.4667vw;
  height: 15.7333vw;
  background: url("../../assets/images/help/icon_succ.png") no-repeat;
  background-size: cover;
}
.cmt_succ h3 {
  margin-top: 2.6667vw;
  font-weight: normal;
  font-size: 4.5333vw;
  color: #666;
}
.cmt_succ p {
  margin-top: 2.9333vw;
  font-size: 3.7333vw;
  color: #999;
}
.cmt_btns {
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
}
.cmt_btns .cmt_btn {
  -webkit-box-flex: 1;
  -webkit-flex: 1;
  -ms-flex: 1;
  flex: 1;
  height: 12vw;
  line-height: 12vw;
  text-align: center;
  font-size: 4.5333vw;
  color: #fff;
  background-image: -webkit-linear-gradient(left, #e69e59, #e47b32);
  border: 0 solid transparent;
  -webkit-border-image: -webkit-linear-gradient(#eebb8b, #eca370) 30 30;
  border-image: -webkit-linear-gradient(#eebb8b, #eca370) 30 30;
  border-top-width: 0.1333vw;
}
</style>
