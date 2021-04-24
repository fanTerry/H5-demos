<template>
  <div class="ui_pop">
    <div class="pop_pay">
      <div class="order" id='yeePayBtn'>充值方式选择
        <a class="close" @click="close()"></a>
      </div>
      <div class="pay_method" v-for="(item,index) in toChargeWayList " @click="choose(item)" :key="index">
        <div>
          <img :src="item.payIcon" alt="">{{item.payName}}
          <span v-if='item.showBalance==1'>(余额:{{item.balance}})</span>
        </div>
        <i class="select_icon" :class="choosedPayWay.payIndex==item.payIndex?'selected':''"></i>
      </div>
      <a class="exchange_btn" @click="confirm()">{{buttonText}}</a>
      <div class="att_tips" v-for="(item,index) in toChargeWayList">
        <template v-if="item.payIndex==2"> 温馨提示:1元友宝余额价值与1星星价值一致</template>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ['price', 'chargeWayList'],
  data() {
    return {
      choosedPayWay: {},
      buttonText: '确认',
      notEnough: false,
      toChargePrice: 0, //组件拿的值,但会变化
      toChargeWayList: [], //组件拿的值,但会变化
      chargeListAliWx: [9.9, 49.9, 99.9, 199.9], //这4个档位：支付宝，微信支付
      chargeListYeeWx: [499.9, 999.9], //这2个档位：易宝支付,微信支付
      tempChargeWayList: [] //临时存放对应支付渠道index
    };
  },
  watch: {
    choosedPayWay(newData, oldData) {
      console.log(newData, oldData);
      if (newData.showBalance == 0) {
        this.buttonText = '确认';
        return;
      }
      if (parseFloat(newData.balance) < this.toChargePrice) {
        this.buttonText = '余额不足,去充值';
        this.notEnough = true;
      }
    }
  },
  mounted() {
    this.toChargePrice = parseFloat(this.price);
    this.toChargeWayList = this.chargeWayList;
    if (this.toChargeWayList) {
      /**
       * 9.9;49.9;99.9;199.9 这4个档位：支付宝，微信支付
        499.9;999.9 这2个档位：微信支付，易宝支付 
       */
      console.log(this.toChargeWayList, 'testtt');
      if (this.isInArray(this.chargeListAliWx, this.toChargePrice)) {
        this.tempChargeWayList = [];
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('Ali_H5_PAY'));
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('WXH5_PAY'));
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('YEEPAY_EBANK_PAY'));
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('UMS_H5_PAY'));
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('UMS_H5_ALI_PAY'));
        this.toChargeWayList = this.filterChargeIndexList(this.tempChargeWayList);
      }
      if (this.isInArray(this.chargeListYeeWx, this.toChargePrice)) {
        this.tempChargeWayList = [];
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('YEEPAY_EBANK_PAY'));
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('WXH5_PAY'));
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('UMS_H5_PAY'));
        this.tempChargeWayList.push(this.globalConst.payIndexMap.get('UMS_H5_ALI_PAY'));
        this.toChargeWayList = this.filterChargeIndexList(this.tempChargeWayList);
      }
      this.toChargeWayList.forEach(item => {
        console.log(item);
        if (parseFloat(item.showBalance) >= this.toChargePrice) {
          this.choosedPayWay = item;
          return;
        } else {
          this.choosedPayWay = this.toChargeWayList[0];
        }
      });
    }
  },
  methods: {
    close() {
      this.$emit('close');
    },
    choose(item) {
      this.choosedPayWay = item;
    },
    confirm() {
      if (this.choosedPayWay.payIndex == -1) {
        this.$toast('请先选择充值方式');
        return;
      }
      if (this.notEnough) {
        this.toCharge();
        return;
      }
      this.$emit('confirmCharge', this.choosedPayWay.payIndex, this.chargeWayBalance);
    },
    toCharge() {
      let chooseIndex = this.choosedPayWay.payIndex;
      if (chooseIndex == 2) {
        window.location.href = this.globalConst.uboxCharge;
      }
    },
    isInArray(arr, value) {
      for (var i = 0; i < arr.length; i++) {
        if (value === arr[i]) {
          return true;
        }
      }
      return false;
    },
    filterChargeIndexList(arr) {
      return this.toChargeWayList.filter(item => {
        return this.isInArray(arr, item.payIndex);
      });
    }
  },
  components: {}
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';
@import '../../../assets/common/_var';

.pop_pay {
  position: relative;
  width: 100%;
  margin: 0 20px;
  padding-bottom: 25px;
  border-radius: 8px;
  background-color: #fff;
  color: #b7b7b7;

  .close {
    @include getClose(20px, #000);
    @extend .g_v_mid;
    right: 0;
  }
  .order,
  .discount,
  .balance {
    @extend .flex_v_justify;
  }
  .order {
    position: relative;
    padding: 18px 0 14px 15px;
    font-size: 17px;
    color: #000;
    border-bottom: 1px solid #ddd;
  }
  .discount {
    margin: 0 15px;
    padding: 12px 0;
    font-size: 15px;
    border-bottom: 1px solid #ddd;
    .icon-xiangyou {
      font-size: 15px;
    }
    div {
      @extend .flex_hc;
    }
  }
  .balance {
    margin: 0 15px;
    padding: 12px 0;
    font-size: 14px;
    border-bottom: 1px solid #ddd;
  }
  .pay_num {
    padding: 10px 0 15px;
    font-size: 42px;
    text-align: center;
    color: #ee322b;
  }
  .pay_num span {
    font-size: 19px;
  }
  .pay_method {
    @extend .flex_v_justify;
    position: relative;
    margin: 0 15px 10px;
    height: 45px;
    font-size: 16px;
    color: #000;
    > div {
      @extend .flex_hc;
    }
    img {
      width: 26px;
      height: 26px;
      object-fit: cover;
      margin-right: 5px;
    }
    span {
      padding-left: 10px;
      font-size: 13px;
      color: #ee322b;
    }
    .select_icon {
      width: 20px;
      height: 20px;
      // @include getRadiusBorder(#999, all, 100%);
      border: 1px solid #999;
      border-radius: 50%;
    }
    .selected {
      @include getBgImg('../../../assets/images/store/select.png');
      border: none;
    }
  }
  .yb_icon {
    @include getBgImg('../../../assets/images/user_center/yb_coin.png');
  }
  .att_tips {
    padding: 4px 0 8px;
    text-align: center;
  }
  .pay_title {
    padding-top: 20px;
    font-size: 15px;
    text-align: center;
    color: #000;
  }
  .warn_tips {
    @extend .g_c_mid;
    bottom: 10px;
    color: $color_red;
    text-align: center;
  }
  .exchange_btn {
    display: table;
    margin: 0 auto 10px;
    padding: 0 30px;
    @include getBtn(auto, 40px, 17px, #fff, $color_main, 40px);
  }
}
</style>