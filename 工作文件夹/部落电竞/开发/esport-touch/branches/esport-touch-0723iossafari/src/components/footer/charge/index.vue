<template>
  <div class="pay">
    <p>确认支付:
      <span class="num">￥{{price}}</span>
      <span class="coin">元</span>
    </p>
    <a class="pay_btn" @click="gotoCharge()">确认支付</a>
  </div>
</template>

<script>
export default {
  props:["price"],
  data() {
    return {};
  },
  methods: {
    gotoCharge(){
      this.$emit("gotoCharge")
    }
  },
  components: {
    
  }
};
</script>

<style lang='scss'>
@import "../../../assets/common/_base.scss";

.pay {
  position: relative;
  @extend .flex_hc;
  justify-content: flex-end;
  -webkit-justify-content: flex-end;
  padding: 5px 10px;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.5);
  p {
    @extend .flex_hc;
    color: #cfcfcf;
  }
  .num {
    font-size: 14px;
    margin-left: 10px;
    color: #ec9805;
  }
  .coin {
    font-size: 14px;
    color: #333;
  }
  .pay_btn {
    @extend .flex_v_h;
    width: 148px;
    height: 40px;
    margin-left: 10px;
    font-size: 17px;
    color: #fff;
    border-radius: 20px;
    background: linear-gradient(to right, #ffa200, #ff7b1c);
  }
}
</style>
