<!--
 * @Author: your name
 * @Date: 2020-07-23 11:41:47
 * @LastEditTime: 2020-07-23 11:49:05
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch/src/views/guess/share.vue
--> 
<template>
  <div class='Page'>
    <div class='main'>
      <div class="qr_code">
        <img :src="qrCode" alt="">
        <p class="tips">财运好不好，全在这一秒</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      qrCode: ''
    };
  },
  created() {
    this.qrCode = this.$route.query.qrCode;
  },
  methods: {}
};
</script>

<style lang='scss' scoped>
@import '../../assets/common/_base';
@import '../../assets/common/_mixin';

.main {
  @include getBgImg('../../assets/images/guess/share_bg.png');
  background-position: top center;
}

.qr_code {
  @extend .g_c_mid;
  bottom: 17.0667vw;
  margin: 0 auto;
  img {
    width: 39.7333vw;
    object-fit: contain;
    clip-path: polygon(15% 5%, 95% 5%, 95% 85%, 85% 95%, 5% 95%, 5% 15%);
    -webkit-clip-path: polygon(15% 5%, 95% 5%, 95% 85%, 85% 95%, 5% 95%, 5% 15%);
  }
}

.tips {
  padding-top: 2.4vw;
  font-size: 3.4667vw;
  color: #fff;
  text-align: center;
}
</style>
