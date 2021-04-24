<template>
  <div class='Page salesExtendPage'>
    <header class='mod_header'>
      <nav-bar :pageTitle='"销售推广"'></nav-bar>
    </header>
    <div class='main'>
      <div class="pull_news" @click="goUrlPage('salesExtend/pullNews')"></div>
      <div class="product_advertise" @click="goUrlPage('salesExtend/productAdver')"></div>
    </div>
    <footer class='mod_footer'>

    </footer>
  </div>
</template>

<script>
import navBar from '../../../components/header/nav_bar/index.vue';

export default {
  components: { navBar },
  props: [],
  data() {
    return {};
  },
  methods: {
    goUrlPage(parma) {
      this.$router.push({
        path: parma
      });
    }
  }
};
</script>

<style lang="scss">
.salesExtendPage {
  .back {
    &::before,
    &::after {
      background-color: #fff !important;
    }
  }
}
</style>


<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';

.mod_header {
  background-color: #3d3b51;
  .nav_bar {
    color: #fff;
  }
}

.main {
  @extend .flex_v_h;
  @include getBgImg('../../../assets/images/user_center/sales/sale_extend_bg.png');
}

.pull_news {
  width: 87.2vw;
  height: 36.5333vw;
  @include getBgImg('../../../assets/images/user_center/sales/pull_new.png');
}

.product_advertise {
  width: 88vw;
  height: 36.1333vw;
  margin-top: 6.6667vw;
  @include getBgImg('../../../assets/images/user_center/sales/product_advertise.png');
}
</style>
