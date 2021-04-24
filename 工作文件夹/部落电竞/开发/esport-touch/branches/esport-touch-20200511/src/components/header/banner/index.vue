<template>
  <figure class="banner">
    <ul>
      <slide ref="slide" :loop="isLoop" :showDot="isAutoPlay" :interval="interval" :threshold="threshold" :speed="speed"
        @changePic="changePic">
        <li v-for="(item,index) in adList" :key="index" @click="see(item.location)">
          <img :src="item.picUrl" alt="" :style="styleObject">
        </li>
      </slide>
    </ul>
    <slot></slot>
    <!-- <figcaption class="title">{{slideText}}</figcaption> -->
  </figure>
</template>

<script>
import slide from "../../common/slide";
export default {
  components: {
    slide
  },
  props: ["adList", "noLoop", "styleObject"],
  data() {
    return {
      isAutoPlay: true,
      isLoop: true,
      isShowDot: true,
      speed: 400,
      threshold: 0.3,
      interval: 2000,
      slideText: "",
      list: []
    };
  },
  mounted() {},
  watch: {
    adList(newList, oldList) {
      if (newList.length > 0) {
        this.list = newList;
        this.slideText = this.list[0].title;
      }
    }
  },
  methods: {
    /** 轮播图切换 */
    changePic(index) {
      if (index != "undefined") {
        if (this.list.length > 1) {
          this.slideText = this.list[index].title;
        }
      }
    },

    update(log) {
      console.log(log);
      this.$refs.slide.update();
    },
    see(location) {
      console.log("000");
      console.log(location);
      if (location) {
        console.log("99999");
        window.location.href = location;
      }
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_mixin.scss";
.banner {
  padding: 0 12px;
  // @include getBorder(bottom, #ddd);
  img {
    width: 100%;
    object-fit: contain;
    border-radius: 5px;
  }
  .title {
    padding: 0 5px;
    font-size: 13px;
    line-height: 36px;
    color: #000;
    @include t_nowrap(100%);
  }
  li {
    position: relative;
    // &::after,
    &::before {
      content: "";
      display: block !important;
      position: absolute;
      left: 0;
      top: 0;
      z-index: 1;
      width: 100%;
      height: 100%;
      background: linear-gradient(
        to bottom,
        rgba(0, 0, 0, 0.4) 0%,
        rgba(0, 0, 0, 0) 40%,
        rgba(0, 0, 0, 0) 60%,
        rgba(0, 0, 0, 0.4) 100%
      );
      background: -webkit-linear-gradient(
        top,
        rgba(0, 0, 0, 0.4) 0%,
        rgba(0, 0, 0, 0) 40%,
        rgba(0, 0, 0, 0) 60%,
        rgba(0, 0, 0, 0.4) 100%
      );
    }
    // &:after {
    //   background: linear-gradient(
    //     to top,
    //     rgba(0, 0, 0, 0.4) 0%,
    //     rgba(0, 0, 0, 0) 40%
    //   );
    //   background: -webkit-linear-gradient(
    //     bottom,
    //     rgba(0, 0, 0, 0.4) 0%,
    //     rgba(0, 0, 0, 0) 40%
    //   );
    // }
  }
}
</style>
