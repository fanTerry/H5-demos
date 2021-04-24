<template>
  <div class='Page godPage'>
    <header class='mod_header'>
      <nav-bar :pageTitle="'我的方案'"></nav-bar>
    </header>
    <div class='main'>
      <!-- 大神信息 -->
      <master-info></master-info>
      <!-- 10场红单情况 -->
      <ul class="bills">
        <li class="right" v-for="(item,index) in 9" :key='index'>红</li>
        <li class="wrong">黑</li>
      </ul>
      <!-- 投单列表 -->
      <ul class="projects">
        <expert-project-item></expert-project-item>
      </ul>
    </div>
    <footer class='mod_footer'>

    </footer>
  </div>
</template>

<script>
import navBar from '../../../../components/header/nav_bar/index.vue';
import masterInfo from '../../followplan/components/masterInfo.vue';
import expertProjectItem from '../../expert/components/expertProjectItem.vue';

export default {
  components: { navBar, masterInfo, expertProjectItem },
  props: [],
  data() {
    return {};
  },
  methods: {}
};
</script>

<style lang="scss">
.godPage {
  .user_info {
    display: none !important;
  }
}
</style>


<style lang='scss' scoped>
@import '../../../../assets/common/_base';
@import '../../../../assets/common/_mixin';

.Page {
  @include getBgLinear(bottom, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.05) 100%);
  background-color: $color_main;
}

.bills {
  @extend .flex_hc;
  margin-top: 2.1333vw;
  padding: 3.2vw 2.9333vw;
  background-color: #fff;
  li {
    @extend .flex_v_h;
    width: 6.6667vw;
    height: 6.6667vw;
    margin: 0 1.3333vw;
    border-radius: 50%;
    font-size: 3.2vw;
    color: #fff;
  }
  .right {
    background-color: $color_main;
  }
  .wrong {
    background-color: #333;
  }
}

.projects {
  margin: 0 4.2667vw;
}
</style>
