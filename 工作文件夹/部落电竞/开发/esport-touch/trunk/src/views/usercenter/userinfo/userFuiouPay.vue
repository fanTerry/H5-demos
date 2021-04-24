<template>
  <div class="Page userAuthPage">
    <div class="main"></div>
    <pop-Alert :showPop="showFuiouPop" @close="showFuiouPop=true" type="1">
      <p>支付未完成,请回到页面重新支付</p>
    </pop-Alert>
  </div>
</template>

<script>
import popAlert from '../../../components/pop_up/pop_alert.vue';

export default {
  components: {
    popAlert
  },
  data() {
    return {
      showFuiouPop: false,
      showFuiouAuthUrl: '',
      userId: null,
      chargeAmount: '',
      chargeWay: '',
      userId: null,
      buyerId: null
    };
  },
  mounted() {
    this.userId = this.$route.query.userId;
    this.chargeAmount = parseFloat(this.$route.query.chargeAmount);
    this.chargeWay = parseInt(this.$route.query.chargeWay);
    this.userId = parseInt(this.$route.query.userId);
    this.buyerId = parseInt(this.$route.query.buyerId);
    this.confirmCharge(this.chargeWay, this.chargeAmount);
  },
  methods: {
    /**充值 */
    confirmCharge: function(chooseChargeWay, chargeWayBalance) {
      if (!chargeWayBalance || !chooseChargeWay || !this.userId || !this.buyerId) {
        console.log(chargeWayBalance, chooseChargeWay, '充值传递的参数');
        this.$toast('充值参数异常');
        return;
      }
      let param = {
        chargeAmount: chargeWayBalance,
        chargeWay: chooseChargeWay,
        userId: this.userId,
        buyerId: this.buyerId
      };
      if (chooseChargeWay == this.globalConst.payIndexMap.get('FUIOU_ALI_PAY')) {
        this.doCharge(param);
      }
    },
    doCharge(param) {
      console.log(param, '充值传递的参数');
      this.$post('/api/h5charge/fuiou', param)
        .then(rsp => {
          const dataResponse = rsp;
          console.log(dataResponse, '充值返回数据');
          if (dataResponse.code == '200' && dataResponse.data.successFlag) {
            if (this.chargeWay == this.globalConst.payIndexMap.get('FUIOU_ALI_PAY')) {
              this.fuiouAlipayReady(this.fuiouToPay(dataResponse.data.prepayId));
            }
          } else if (dataResponse.code == '600') {
            this.$toast('余额不足');
          } else if (dataResponse.code == '602') {
            //风控提示
            this.$toast(dataResponse.message);
          } else {
            this.$toast('服务器繁忙,稍后再试~');
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    //授权支付
    fuiouAliPayAuth() {
      let param = {
        chargeAmount: this.chargeAmount,
        chargeWay: this.chargeWay
      };
      return this.$post('/api/oauth2/authorize/alipay', param)
        .then(rsp => {
          const dataResponse = rsp;
          console.log(dataResponse, '富友支付宝-h5充值方式返回');
          if (dataResponse.code == '200') {
            if (dataResponse.data && dataResponse.data.length > 50) {
              window.location.href = dataResponse.data;
            }
          } else {
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    fuiouAlipayReady(callback) {
      // 如果jsbridge已经注入则直接调用
      if (window.AlipayJSBridge) {
        callback && callback();
      } else {
        // 如果没有注入则监听注入的事件
        document.addEventListener('AlipayJSBridgeReady', callback, false);
      }
    },
    fuiouToPay(tradeNO) {
      let _self = this;
      console.log(window.AlipayJSBridge, 'window.AlipayJSBridge');
      console.log(window.AlipayJSBridge, 'in');
      if (window.AlipayJSBridge) {
        console.log('in  AlipayJSBridge');
      }
      AlipayJSBridge.call(
        'tradePay',
        {
          tradeNO: tradeNO
        },
        function(result) {
          let res = JSON.parse(JSON.stringify(result));
          if (res.resultCode == '6001') {
            _self.showFuiouPop = true;
          }
          console.log(res, '支付宝返回结果');
        }
      );
    }
  }
};
</script>

<style lang="scss">
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';
.userAuthPage {
  background-color: #fff !important;
}
</style>


<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';
.nav_bar {
  position: relative;
  @include t_nowrap(100%);
  padding: 0 40px;
  font-size: 18px !important;
  line-height: 44px;
  font-weight: normal;
  color: #333;
  text-align: center;
  .meet_problem {
    @extend .g_v_mid;
    right: 20px;
    color: #999;
    font-size: 13px;
  }
}
.main {
  padding: 0 4.2667vw;
}

.tips {
  padding: 4.2667vw 0;
  font-size: 3.4667vw;
  line-height: 5.8667vw;
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
