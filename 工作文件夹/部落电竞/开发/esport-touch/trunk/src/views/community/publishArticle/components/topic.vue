<template>
  <div class="user_info" @click="selectTopic(value.name)">
    <div class="user_img " :class="{active:!value.attention}">
      <img :src="value.iconUrl|getDefaultImg(globalConst.topicDefaultIcon)" alt="">
    </div>
    <div class="des">
      <div class="name">{{value.name}}</div>
      <p class="tips" v-if="value.description">
        <span>{{value.description}}</span>
      </p>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: ["value"],
  data() {
    return {};
  },
  methods: {
    selectTopic(param) {
      this.$emit("selectTopic", param);
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../../../assets/common/_mixin.scss";
@import "../../../../assets/common/_base.scss";

.user_info {
  @extend .flex_hc;
  padding: 14px 0 14px 0;
  margin: 0 35px 0 15px;
  .user_img {
    @extend .flex_hc;
    flex: none;
    -webkit-flex: none;
    position: relative;
    width: 35px;
    height: 35px;
    margin-right: 10px;
    &.active::after {
      content: "";
      position: absolute;
      right: 0;
      top: 0;
      transform: translate(30%, -30%);
      width: 9px;
      height: 9px;
      border: 1px solid #fff;
      border-radius: 50%;
      background-color: #ff3b30;
    }
    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
    }
  }
  .name {
    @include t_nowrap(150px);
    line-height: 1.2;
    font-size: 13px;
    font-weight: bold;
    color: #000;
  }
  .tips {
    padding-top: 5px;
    span {
      @include line_clamp(2);
      padding-right: 10px;
      font-size: 11px;
      line-height: 13px;
      color: #818181;
    }
  }
  .des {
    @extend .flex;
    flex-direction: column;
    -webkit-flex-direction: column;
  }
}
</style>
