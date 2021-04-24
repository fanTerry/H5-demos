<!--需要上拉下拉的模板页面 for 前端开发-->
<template>
  <div class="Page">
    <div class="main">
      <div class="scroll-list-wrap">
        <scroll
          ref="scroll"
          :scrollbar="scrollbar"
          :pullDownRefresh="pullDownRefresh"
          :pullUpLoad="pullUpLoad"
          :startY="0"
          @pullingDown="onPullingDown"
          @pullingUp="onPullingUp"
        >
          <div class="scroll_wrapper">
            <!--需要滚动的内容-->
          </div>
        </scroll>
      </div>
    </div>
    <loading></loading>
    <!--loading 组件-->
  </div>
</template>

<script>
import Scroll from "components/common/scroll";
import loading from "components/common/loading";
export default {
  components: {
    Scroll,
    loading
  },
  data() {
    return {
      scrollbar: {
        fade: true
      },
      pullDownRefresh: {
        threshold: 90,
        stop: 40,
        txt: "刷新成功"
      },
      pullUpLoad: {
        threshold: 10,
        txt: {
          more: "加载更多",
          noMore: "没有更多数据了"
        }
      }
    };
  },
  computed: {},
  created() {},
  mounted() {
    $(".loading_wrapper").hide();
  },
  methods: {
    onPullingDown() {
      console.log("you are onPullingDown");
      if (this._isDestroyed) {
        return;
      }
      this.refresh(true);
    },
    onPullingUp() {
      console.log("you are onPullingUp");
      if (this._isDestroyed) {
        return;
      }
      if (this.counter >= this.pageEnd) {
        this.$refs.scroll.forceUpdate();
      } else {
        this.loadMore();
      }
    },
    refresh(loaded) {
      console.log("you are refresh");
    },
    loadMore() {
      console.log("you are loadMore");
    }
  },
  watch: {}
};
</script>

<style lang='scss'>
</style>
