<template>
  <div class="Page pkHomePage">
    <header class="mod_header">
      <page-title :pageTitle="'Win+'"></page-title>
      <div class="nav">
        <div class="nav_left">
          <img
            src="http://thirdwx.qlogo.cn/mmopen/vi_32/KpMCvItXghXRODkenZklAuoibDHiae6V7iadiaHLR89HdsPic3TproDEvNicRpjzfySpF8rTFSuicxibrP2dZh4CPQwZww/132"
            alt="">
          <div class="asset">
            <span class="gold_icon"></span>
            82000000
            <span class="recharge_icon"></span>
          </div>
        </div>
        <div class="nav_right">
          <i class="rank_icon"></i>
          <i class="gift_icon"></i>
          <i class="question_icon"></i>
        </div>
      </div>
    </header>
    <div class="main">
      <div class="banner">
        <banner-x class="banner_x" :adList='adList'></banner-x>
        <div class="swiper-container banner_y">
          <ul class="swiper-wrapper">
            <li class="swiper-slide" v-for="(item,index) in 3" :key="index">
              公告：王者挑战赛全新升级，一睹为快
            </li>
          </ul>
        </div>
      </div>
      <div class="pk">
        <ul class="tab">
          <li class="active">我要约架</li>
          <!-- 有约架信息加红点 -->
          <li class="att">我的约架<span>应战</span></li>
        </ul>
        <div class="content">
          <div class="pk_top">
            <span class="on_line">全新上线</span>
            <span class="off_line">敬请期待</span>
            <img src="" alt="">
          </div>
          <div class="pk_bottom">
            <div class="pond">
              <p>总奖池: 23312万</p>
            </div>
            <div class="num">
              <p>约架数: 182</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import pageTitle from "../../../components/header/nav_bar/index.vue";
import bannerX from "../../../components/header/banner/swiper.vue";

export default {
  components: { pageTitle, bannerX },
  props: [],
  data() {
    return {
      swiperY: null,
      adList: [
        {
          picUrl:
            "https://rs.esportzoo.com/upload/admin/picAd/开黑组队1_1577086288824.jpg"
        },
        {
          picUrl:
            "https://rs.esportzoo.com/upload/admin/picAd/20200312枫叶广场_1583995308439.jpg"
        }
      ]
    };
  },
  mounted() {
    this.$nextTick(() => {
      this.initSwiperY();
    });
  },
  methods: {
    initSwiperY() {
      this.swiperY = new Swiper(".banner_y", {
        pagination: ".swiper-pagination",
        loop: true,
        speed: 1000,
        autoplay: 3000,
        autoplayDisableOnInteraction: false,
        observer: true,
        direction: "vertical"
      });
    }
  }
};
</script>

<style lang="scss">
.pkHomePage {
  .back {
    display: none !important;
  }
  .nav_bar {
    color: #fff !important;
  }
  .swiper-pagination-fraction,
  .swiper-pagination-custom,
  .swiper-container-horizontal > .swiper-pagination-bullets {
    left: initial;
    right: 5.3333vw;
    width: initial;
  }
  .swiper-pagination-bullet {
    background: rgba(15, 30, 55, 1);
    opacity: 0.6;
  }
  .swiper-pagination-bullet-active {
    background: #ed4037;
  }
}
</style>


<style lang='scss' scoped>
@import "../../../assets/common/_base";
@import "../../../assets/common/_mixin";

.mod_header {
  background-color: #263e69;
}

.nav {
  @extend .flex_v_justify;
  padding: 2.6667vw 2.6667vw 1.8667vw;
}

.nav_left {
  @extend .flex_hc;
  img {
    width: 8.2667vw;
    height: 8.2667vw;
  }
  .asset {
    position: relative;
    margin-left: 5.7333vw;
    padding: 0 7.2vw 0 6.1333vw;
    line-height: 6.9333vw;
    font-size: 3.7333vw;
    color: #fff;
    background-color: #0d1e39;
  }
  .gold_icon {
    @extend .g_v_mid;
    left: 0;
    transform: translate(-50%, -50%);
    -webkit-transform: translate(-50%, -50%);
    width: 7.2vw;
    height: 7.2vw;
    @include getBgImg("../../../assets/images/pk/home/gold_icon.png");
    background-size: 100% auto;
  }
  .recharge_icon {
    @extend .g_v_mid;
    right: 0;
    transform: translate(50%, -50%);
    -webkit-transform: translate(50%, -50%);
    width: 7.7333vw;
    height: 7.7333vw;
    @include getBgImg("../../../assets/images/pk/home/recharge_icon.png");
  }
}

.nav_right {
  @extend .flex_hc;
  i {
    width: 5.3333vw;
    height: 5.3333vw;
    margin-right: 6.6667vw;
    &:last-child {
      margin-right: 0;
    }
  }
  .rank_icon {
    @include getBgImg("../../../assets/images/pk/home/rank_icon.png");
  }
  .gift_icon {
    @include getBgImg("../../../assets/images/pk/home/gift_icon.png");
  }
  .question_icon {
    @include getBgImg("../../../assets/images/pk/home/question_icon.png");
  }
}

.main {
  background-color: #11223c;
}

.banner {
  margin: 2.6667vw;
  background-color: #192f51;
  border-radius: 0 0 4px 4px;
}

.banner_x {
  height: 36.5333vw;
}

.banner_y {
  position: relative;
  height: 7.7333vw;
  &::before {
    content: "";
    width: 3.7333vw;
    height: 3.7333vw;
    @extend .g_v_mid;
    left: 2.9333vw;
    @include getBgImg("../../../assets/images/pk/home/voice.png");
  }
  li {
    @include t_nowrap(100%);
    padding-left: 8vw;
    line-height: 7.7333vw;
    font-size: 3.4667vw;
    color: #79b8ff;
  }
}

.pk {
  padding: 0 2.6667vw 5.3333vw;
  background-color: #192f51;
}

.tab {
  @extend .flex;
  li {
    position: relative;
    flex: 1;
    -webkit-flex: 1;
    font-size: 4vw;
    line-height: 9.8667vw;
    color: #79b8ff;
    text-align: center;
    &.active {
      color: #fff;
      background-color: #2d63d3;
    }
    &.att {
      &::before {
        content: "";
        position: absolute;
        top: 2px;
        right: 2px;
        width: 1.3333vw;
        height: 1.3333vw;
        background-color: #ed4037;
        transform: rotate(45deg);
        -webkit-transform: rotate(45deg);
      }
    }
    span {
      @extend .g_v_mid;
      margin-left: 1.3333vw;
      padding: 0.5333vw 1.6vw;
      font-size: 2.4vw;
      color: #fff;
      border-radius: 0.5333vw;
      background-color: #ed4037;
    }
  }
}

.content {
  margin-top: 2.6667vw;
}

.pk_top {
  position: relative;
  .on_line,
  .off_line {
    position: absolute;
    top: 2vw;
    left: 5.0667vw;
    padding: 0 2.2667vw;
    line-height: 4.2667vw;
    font-size: 2.6667vw;
    color: #fff;
    border-radius: 0.5333vw;
  }
  .on_line {
    background-color: #ed4037;
  }
  .off_line {
    background-color: #252525;
  }
  img {
    height: 32vw;
    object-fit: contain;
  }
}

.pk_bottom {
  @extend .flex_hc;
  height: 7.2vw;
  border-radius: 0 0 4px 4px;
  background-color: #4b70cc;
  & > div {
    flex: 1;
    -webkit-flex: 1;
    @extend .flex_hc;
    padding-left: 5.0667vw;
    font-size: 3.2vw;
    color: #fff;
  }
}

.pond,
.num {
  &::before {
    content: "";
    width: 4vw;
    height: 4vw;
    margin-right: 1.3333vw;
  }
}

.pond {
  &::before {
    @include getBgImg("../../../assets/images/pk/home/gold_icon.png");
    background-size: 100% auto;
  }
}

.num {
  &::before {
    @include getBgImg("../../../assets/images/pk/home/users_icon.png");
    background-size: 100% auto;
  }
}
</style>
