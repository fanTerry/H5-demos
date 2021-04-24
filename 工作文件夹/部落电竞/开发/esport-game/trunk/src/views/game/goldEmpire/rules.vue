<template>
  <div class='Page' v-if="showFlag">
    <header class='mod_header'>
      <span>规则说明</span>
    </header>
    <div class='main'>
      <h3 class="title">规则说明标题</h3>
      <p class='txt'>规则说明规则说明规则说明规则说明规则说明规则说明规则说明</p>
      <p class='txt'>规则说明规则说明规<span>则说明规</span>则说明规则说明规则说明规则说明规则说明规则说明规则说明规则说明规则说明规则说明规则说明规则说明</p>
      <h3 class="title">规则说明</h3>

      <a class='pull_up' @click="showPage(false)"></a>
    </div>
    <footer class='mod_footer'>

    </footer>
  </div>
</template>

<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      showFlag: false
    };
  },
  mounted() {},
  methods: {
    showPage(parma) {
      this.showFlag = parma;
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';

.Page {
  position: fixed;
  left: 0;
  top: 0;
  z-index: 999;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
}

.mod_header {
  height: 11.4667vw;
  padding-top: 2.4vw;
  @include getBgImg('../../../assets/images/game/goldempire/title_bg.png');
  background-position: top center;
  text-align: center;
  span {
    font-size: 4vw;
    font-weight: bold;
    background: linear-gradient(
      to bottom,
      rgba(171, 136, 82, 1) 0%,
      rgba(200, 173, 118, 1) 29.78515625%,
      rgba(246, 234, 181, 1) 48.974609375%,
      rgba(252, 239, 202, 1) 79.00390625%,
      rgba(226, 204, 145, 1) 100%
    );
    background: -webkit-linear-gradient(
      top,
      rgba(171, 136, 82, 1) 0%,
      rgba(200, 173, 118, 1) 29.78515625%,
      rgba(246, 234, 181, 1) 48.974609375%,
      rgba(252, 239, 202, 1) 79.00390625%,
      rgba(226, 204, 145, 1) 100%
    );
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }
}

.main {
  position: relative;
  flex: none;
  -webkit-flex: none;
  overflow: initial;
  min-height: 146.6667vw;
  padding: 0 1.0667vw;
  @include getBgImg('../../../assets/images/game/goldempire/record_bg.png');
  background-size: 100% 100%;
}

.title {
  position: relative;
  width: 90.9333vw;
  margin: 0 auto;
  padding: 3.7333vw 0;
  font-size: 4vw;
  font-weight: bold;
  text-align: center;
  background: linear-gradient(to bottom, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
  background: -webkit-linear-gradient(top, rgba(255, 253, 232, 1) 0%, rgba(255, 253, 232, 1) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  &::before,
  &::after {
    content: '';
    @extend .g_v_mid;
    width: 24vw;
    height: 4vw;
    @include getBgImg('../../../assets/images/game/goldempire/detail_title_bg.png');
  }
  &::before {
    left: 0;
  }
  &::after {
    right: 0;
    transform: translateY(-50%) rotate(180deg);
    -webkit-transform: translateY(-50%) rotate(180deg);
    @include getBgImg('../../../assets/images/game/goldempire/detail_title_bg.png');
  }
}

.txt {
  padding: 0 4vw 4vw;
  font-size: 3.4667vw;
  line-height: 4vw;
  color: #8eabc9;
  span {
    color: #deba3e;
  }
}

.pull_up {
  position: absolute;
  left: 50%;
  bottom: 0;
  transform: translate(-50%, 50%);
  -webkit-transform: translate(-50%, 50%);
  width: 12.2667vw;
  height: 12.6667vw;
  @include getBgImg('../../../assets/images/game/goldempire/pull_up.png');
}
</style>
