<template>
  <section class="nav_top" v-if="num">
    <div class="left">
      <div class="user_img">
        <img src="http://res.doo8.com/memberphoto/565/tn_1550558670125_160_160.jpg" alt="">
      </div>
      <div class="search_con">
        <input type="text" placeholder="">
        <i class="search"></i>
      </div>
    </div>
    <div class="right">
      <div class="message"><span class="att">6</span></div>
      <div class="comment"></div>
    </div>
  </section>

  <section class="nav_top" v-else>
    <div class="left">
      <div class="user_img">
        <img src="http://res.doo8.com/memberphoto/565/tn_1550558670125_160_160.jpg" alt="">
      </div>
      <ul class="match_list">
        <li class="item active">CSGO</li>
        <li class="item">CSGO</li>
        <li class="item">CSGO<span class="att">6</span></li>
        <li class="item">CSGO</li>
      </ul>
    </div>
  </section>
</template>

<script>
export default {
  props: {
    num: Boolean
  },
  data() {
    return {};
  },
  methods: {
    //   on(){
    //     this.$emit("change",false)
    //   }
  },
  components: {},
  mounted() {
    console.log(this.prop);
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_base.scss";
@import "../../../assets/common/_mixin.scss";
.nav_top {
  position: relative;
  @extend .flex_v_justify;
  padding: 15px 10px 10px;
  .user_img {
    width: 24px;
    height: 24px;
    margin-right: 12px;
    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
    }
  }
  .left,
  .right {
    @extend .flex_hc;
  }
  .left {
    flex: 1;
    -webkit-flex: 1;
  }
  .match_list {
    position: absolute;
    left: 46px;
    bottom: 0;
    @extend .flex_hc;
    padding-left: 5px;
    .item {
      position: relative;
      margin-right: 30px;
      padding-bottom: 16px;
      color: #ffed29;
      font-size: 13px;
      font-weight: 500;
      &.active {
        color: #fff;
        background: linear-gradient(
          to top,
          rgba(255, 255, 255, 1) -30%,
          rgba(255, 255, 255, 0) 60%
        );
        &::after {
          content: "";
          position: absolute;
          left: 0;
          bottom: 0;
          width: 100%;
          height: 2px;
          background-color: #fff;
        }
      }
    }
  }
  .search_con {
    position: relative;
    flex: 1;
    -webkit-flex: 1;
    margin-right: 20px;
  }
  input {
    width: 100%;
    height: 24px;
    padding-left: 30px;
    border-radius: 24px;
    background-color: #fff;
    border: none;
  }
  .search {
    @extend .g_v_mid;
    left: 7px;
    width: 16px;
    height: 16px;
    @include getBgImg("../../../assets/images/home/search.png");
  }
  .message,
  .comment {
    position: relative;
    width: 22px;
    height: 22px;
  }
  .message {
    @include getBgImg("../../../assets/images/home/message.png");
  }
  .att {
    @extend .flex_v_h;

    position: absolute;
    right: 0;
    top: 0;
    transform: translate(50%, -30%);
    -webkit-transform: translate(50%, -30%);

    @include getCircle(14px, #ff0000, #fff);
    font-size: 11px;
  }
  .comment {
    margin-left: 20px;
    @include getBgImg("../../../assets/images/home/comment.png");
  }
}
</style>
