<template>
  <!-- 跑马灯 -->
  <section class="mod_award">
    <div class="winner_scroll swiper-container">
      <ul class="swiper-wrapper">
        <li class="swiper-slide" v-for="(item,index) in prizeList" :key="index">
          <template v-if='item.type==1'>
            <span>恭喜</span>
            <span class="winner_name">{{item.userName}}</span>
            <span>测预正确获得</span>
            <span class="award_value">{{item.winPrizeAmount}}</span>
            <span>星星!</span>
          </template>
          <template v-if='item.type==2'>
            <span>恭喜</span>
            <span class="winner_name">{{item.userName}}</span>
            <span>成功兑换</span>
            <span class="award_value">{{item.goodsName}}</span>
          </template>
          <template v-if='item.type==3'>
            <span class="notice" @click="toLocation(item.noticeUrl)">{{item.noticeDesc}}</span>
          </template>
        </li>
      </ul>
    </div>
  </section>
</template>

<script>
export default {
  components: {},
  props: ['prizeList'],
  data() {
    return {
      brodcastSwiper: null
    };
  },
  mounted() {
    this.$nextTick(() => {
      // console.log(newValue.length, 111111111111111111111);
      this.initAwardSwiper();
    });
  },
  methods: {
    initAwardSwiper() {
      this.brodcastSwiper = new Swiper('.winner_scroll', {
        loop: true,
        speed: 1000,
        autoplay: 3000,
        autoplayDisableOnInteraction: false,
        direction: 'vertical',
        observer: true,
        observeParents: true
      });
    },
    toLocation(href) {
      if (href != '') {
        window.location.href = href;
      }
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../../assets/common/_base';
@import '../../../../assets/common/_mixin';

.mod_award {
  @extend .flex_hc;
  margin: 1.8667vw 4.2667vw 0;
  background-color: #fff;
  border-radius: 0.8vw;
}
.winner_scroll {
  flex: 1;
  -webkit-flex: 1;
  height: 8vw;
  overflow: hidden;
  li {
    @extend .flex_v_h;
    padding: 0 2.6667vw 0 2.2667vw;
    color: #333;
    span {
      font-size: 3.7333vw;
      line-height: 1.2;
    }
    .winner_name {
      @include t_nowrap(20vw);
      padding: 0 1.8667vw;
      font-weight: bold;
      color: $color_main;
    }
    .award_value {
      @include t_nowrap(30.3333vw);
      padding: 0 1.8667vw;
      font-weight: bold;
      color: $color_main;
    }
  }
  .notice {
    @include t_nowrap(100%);
  }
}
</style>
