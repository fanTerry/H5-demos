<template>
  <div class="ui_pop" v-if="showSignType">

    <!-- 每日登陆领奖 -->
    <div class="prize">
      <a class="close" @click="closePop"></a>
      <h2>每日领取<span>您已连续签到 {{signNum}} 天</span></h2>
      <ul class="prize_list">
        <li v-for="(item,index) in signGiftList " :key="index"
          :class="{'recevied':item.receiveStatus==0,'disabled':item.receiveStatus>1}">
          <p>{{item.hdGiftName.substring(0,3)}}</p>
          <img :src="require('../../../assets/images/pop/starday'+index+'.png')" alt />
          <!-- 不能领取加disabled置灰态 -->
          <a class="prize_get_btn" v-if="item.receiveStatus>1"
            @click="showMessage('暂不可领取')">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a>
          <a class="prize_get_btn" v-else-if="item.receiveStatus ==0"
            @click="showMessage('已领取')">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a>

          <a class="prize_get_btn" v-else
            @click="getPrize(index)">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a>

        </li>
      </ul>
      <div class="pop_tips" v-if="signTips">
        <p v-if="!randomFlag">获得</p>
        <p v-else>随机获得</p>
        <span>{{signAwardNum}}星星</span>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import QRCode from "qrcodejs2";
//import { mapGetters, mapActions } from "vuex";
export default {
  components: { QRCode },
  props: ["showSignType"],

  data() {
    return {
      signGiftList: [], //签到礼品
      signTips: false, //领取提示弹窗
      signAwardNum: 0, //随机获得的星星数量
      randomFlag: false ,//显示随机的弹出提示
      signNum:0
    };
  },
  methods: {
    closePop() {
      this.$emit("closePop");
    },
    //去签到
    getPrize(index) {
      return this.$post("/api/hdsign/signIn")
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            console.log("签到");
            let that = this;
            this.signGiftList[index].receiveStatus = 0;
            if (this.signGiftList[index].sevenFlag) {
              this.randomFlag = true;
            }
            this.signTips = true;

            this.signAwardNum = dataResponse.data.giftCount;
            setTimeout(() => {
              that.signTips = false;
            }, 1500);
            //刷新用户余额
            this.$emit("updateSign", dataResponse.data.giftRecScore);
          } else {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    //弹窗提示
    showMessage(msg) {
      this.$toast(msg);
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_base";
@import "../../../assets/common/_mixin";

.ui_pop > div {
  position: relative;
}

.close {
  position: absolute;
  right: 15px;
  top: 15px;
  width: 20px;
  height: 20px;
}

.pop_tips {
  @extend .g_v_c_mid;
  width: 215px;
  padding: 20px 0;
  text-align: center;
  p {
    font-size: 14px;
    color: #fff;
  }
  span {
    display: block;
    padding-top: 20px;
    font-size: 20px;
    color: #f6b70d;
  }
}

.prize {
  width: 648px;
  padding: 30px 30px 42px;
  h2 {
    position: relative;
    padding-left: 22px;
    font-size: 24px;
    color: #fff6ea;
    &::before {
      content: "";
      position: absolute;
      top: 1px;
      left: 0;
      width: 7px;
      height: 24px;
      background: linear-gradient(
        to bottom,
        rgba(246, 215, 75, 1),
        rgba(197, 69, 4, 1)
      );
      background: -ms-linear-gradient(
        top,
        rgba(246, 215, 75, 1),
        rgba(197, 69, 4, 1)
      );
      background: -webkit-linear-gradient(
        top,
        rgba(246, 215, 75, 1),
        rgba(197, 69, 4, 1)
      );
    }
    span {
      padding-left: 20px;
      font-size: 14px;
      color: #ffdcd7;
    }
  }
  .prize_list {
    padding-left: 8px;
    overflow: hidden;
  }
  li {
    float: left;
    width: 142px;
    height: 164px;
    padding-bottom: 18px;
    margin: 40px 3px 0 0;
    @include getBgImg("../../../assets/images/pop/sign_bg.png");
    box-shadow: 0px 6px 8px 0px rgba(0, 0, 0, 0.03);
    text-align: center;
    &:first-child {
      img {
        height: 50px;
        margin: 22px auto;
      }
    }
    &:nth-child(4) {
      margin-right: 0;
    }
    &:last-child {
      img {
        height: 44px;
        margin: 25px auto 26px;
      }
    }
    &.disabled {
      @include getBgImg("../../../assets/images/pop/sign_disabled.png");
      img,
      .prize_get_btn,
      p {
        opacity: 0.5;
      }
    }
    &.recevied {
      position: relative;
      @include getBgImg("../../../assets/images/pop/sign_disabled.png");
      &::after {
        content: "";
        @extend .g_c_mid;
        top: 23px;
        width: 96px;
        height: 96px;
        @include getBgImg("../../../assets/images/home/recevied.png");
      }
      img,
      .prize_get_btn,
      p {
        opacity: 0.5;
      }
    }
    p {
      height: 22px;
      font-size: 12px;
      line-height: 22px;
      color: #fedcd7;
    }
    img {
      display: block;
      height: 31px;
      margin: 32px auto;
    }
  }
  .prize_get_btn {
    display: inline-block;
    width: 94px;
    height: 30px;
    line-height: 26px;
    font-size: 20px;
    font-weight: bold;
    color: #f6b70d;
    border: 2px solid currentColor;
    border-radius: 14px;
  }
}

.confirm_pay,
.orderDetails,
.recharge,
.prize,
.qrCodePay,
.payStatus,
.pointsExchange,
.pop_tips {
  background: linear-gradient(to right bottom, #34201d, #2a181c);
  background: -webkit-linear-gradient(left top, #34201d, #2a181c);
  background: -ms-linear-gradient(left top, #34201d, #2a181c);
  box-shadow: 0px 0px 15px 0px rgba(10, 2, 4, 0.5);
}
</style>
