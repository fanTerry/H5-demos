<template>
  <div class="Page userAuthPage" @click="closeShowRulesPop($event)">
    <header class="mod_header">
      <navBar pageTitle="授权支付"></navBar>
    </header>
    <div class="main">
      <div class="tips">
        温馨提示：
        <br />授权成功,3秒后自动返回...
      </div>
      <div class="exchange_btn" @click="goback()">返回</div>
    </div>
  </div>
</template>

<script>
import navBar from '../../../components/header/nav_bar/index.vue';
import phoneSendCode from '../../../components/user_info/phoneSendCode.vue';
import { getCheck } from '../../../libs/utils';
import { mapMutations } from 'vuex';

export default {
  components: {
    navBar,
    phoneSendCode
  },
  data() {
    return {
      userInfo: {
        trueName: '',
        certNo: ''
      },
      showPopRules: false
    };
  },
  created() {},
  mounted() {
    setTimeout(() => {
      this.$router.go(-2);
    }, 3 * 1000);
  },
  methods: {
    goback() {
      this.$router.go(-2);
    }
  }
};
</script>

<style lang="scss">
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';
.userAuthPage {
  .mod_header {
    background-color: $color_item !important;
  }
  .nav_bar {
    color: #fff !important;
    .back {
      &::before,
      &::after {
        background-color: #fff !important;
      }
    }
  }
  .phone_num,
  .ver_code {
    height: 12vw !important;
    margin: 0 !important;
    font-size: 3.7333vw !important;
    input {
      color: #fff !important;
    }
    ::-webkit-input-placeholder {
      color: rgba(255, 255, 255, 0.2) !important;
    }
  }
  .phone_num {
    @include getBorder(bottom, #504d70);
  }
  .confirm_btn {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 91.4667vw;
    margin: 1.3333vw 4.2667vw !important;
    border-radius: 0.5333vw !important;
    @media only screen and (min-device-width: 375px) and (min-device-height: 812px) and (-webkit-device-pixel-ratio: 3),
      only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) {
      margin-bottom: constant(safe-area-inset-bottom) !important;
      margin-bottom: env(safe-area-inset-bottom) !important;
    }
  }
}
</style>


<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';

.main {
  padding: 0 4.2667vw;
}

.tips {
  padding: 4.2667vw 0;
  font-size: 3.4667vw;
  line-height: 5.8667vw;
  color: #fff;
}

.exchange_btn {
  display: block;
  margin-top: 3.4667vw;
  line-height: 9.6vw;
  font-size: 4vw;
  color: #fff;
  font-weight: bold;
  text-align: center;
  background-color: $color_btn;
  &.disabled {
    opacity: 0.3;
  }
}
</style>
