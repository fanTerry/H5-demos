<template>
  <div class="location_address">
    <i class="location_icon"></i>
    <div>
      <p><span class="name">{{address.receiverName}}</span><span class="phone_num">{{address.receiverPhone}}</span></p>
      <p class="address">{{address.receiverProvince}} {{address.receiverCity}} {{address.receiverDistrict}} {{address.receiverAddress}}</p>
    </div>
    <span class="link_icon" v-if="showLinkIcon"></span>
  </div>
</template>

<script>
export default {
   props: {
    address: Object,
    showLinkIcon: Boolean,
  },
  data() {
    return {};
  },
  methods: {},
  components: {}
};
</script>

<style lang='scss' scoped>
@import "../../assets/common/_base";
@import "../../assets/common/_mixin";
.location_address {
  position: relative;
  @extend .flex_hc;
  justify-content: flex-start !important;
  -webkit-justify-content: flex-start !important;
  padding: 17px 0;
  .location_icon {
    width: 22px;
    height: 22px;
    margin-right: 8px;
    @include getBgImg("../../assets/images/store/location_icon.png");
  }
  .name {
    font-size: 15px;
    @include t_nowrap(100px);
    line-height: 1.2;
  }
  .phone_num {
    color: #999;
    padding-left: 12px;
  }
  p {
    @extend .flex_hc;
    color: #333;
    font-size: 12px;
  }
  .address {
    display: block;
    padding-top: 5px;
    @include t_nowrap(240px);
    line-height: 1.2;
  }
  .link_icon {
    position: absolute;
    right: 0;
    width: 20px;
    height: 20px;
    @include getArrow(12px, #999, right);
  }
}
</style>
