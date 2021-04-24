<template>
  <div class="swiper-container banner_swiper">
    <ul class="swiper-wrapper">
      <li class="swiper-slide" v-for="(item,index) in adList " :key="index">
        <slot :data='item'></slot>
        <img v-if="item.picUrl" :src="item.picUrl" alt="">
      </li>
    </ul>
    <div class="swiper-pagination"></div>
  </div>
</template>

<script>
import Swiper from 'swiper';
export default {
  components: {},
  props: ['adList'],
  data() {
    return {
      commonSwiper: null
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initSwiper();
    });
  },
  methods: {
    initSwiper() {
      this.commonSwiper = new Swiper('.banner_swiper', {
        pagination: {
          el: '.swiper-pagination'
        },
        loop: true,
        speed: 1000,
        autoplay: {
          delay: 3000,
          disableOnInteraction: true
        },
        touchAngle: 10, //手势滑动距离触发切换
        observer: true,
        observeParents: true,
        on: {
          tap: event => {
            const realIndex = this.commonSwiper.realIndex;
            console.log(this, event.currentTarget, event);
            if (this.adList[realIndex].locationType == 4) {
              // this.player.play();
              // this.commonSwiper.autoplay.stop();
            } else {
              // this.player.pause();
              this.goUrlPage(this.adList[realIndex].location, this.adList[realIndex].locationType);
            }
          },
          slideChangeTransitionStart: () => {
            // console.log(this.commonSwiper);
            // const realIndex = this.commonSwiper.realIndex;
            // if (this.adList[realIndex].locationType == 4) {
            //   this.player.play();
            //   this.commonSwiper.autoplay.stop();
            // } else {
            //   this.player.pause();
            //   this.autoplay.start();
            // }
          }
        }
      });
    },
    goUrlPage(location, locationType) {
      console.log(location, locationType, '跳转信息');
      //0：不跳转；1：功能弹窗；2：跳转url
      if (locationType == 2) {
        window.location.href = location;
        // window.open(location, '_self');
      } else if (locationType == 1) {
        //跳转相应的弹窗
        this.$emit('toPop', location);
      } else {
        return;
      }
    }
  }
};
</script>

<style lang="scss">
@import '../../../assets/common/_base';

.banner_swiper {
  .swiper-pagination {
    @extend .flex_hc;
    width: auto;
    left: auto;
    right: 2.1333vw;
  }
  .swiper-pagination-bullet {
    width: 1.6vw;
    height: 1.6vw;
    background: rgba(#fff, 0.5);
  }
  .swiper-pagination-bullet-active {
    background-color: #fff;
  }
  #mse {
    width: 100% !important;
    height: 45.3333vw !important;
    object-fit: cover;
    pointer-events: none;
  }
}
</style>


<style lang='scss' scoped>
ul,
li {
  width: 100%;
  height: 100%;
}
img,
video,
#mse {
  width: 100%;
  height: 45.3333vw;
  object-fit: cover;
  pointer-events: none;
}
</style>

