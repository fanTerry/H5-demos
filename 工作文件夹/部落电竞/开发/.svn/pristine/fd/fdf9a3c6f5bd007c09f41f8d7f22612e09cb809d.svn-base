<template>
  <div class="ui_pop" v-if="showSignPop">
    <div class='Page'>
      <a class='close' @click="closePop"></a>
      <p class="att_tips">本周您已连续签到 <span>{{signNum}}</span> 天</p>
      <ul class="prize_list">
        <!-- 不能领取加类名disabled -->
        <li v-for="(item,index) in signGiftList " :key="index"
          :class="{'recevied':item.receiveStatus==0,'disabled':item.receiveStatus>1}" @click="showMessage(index)">
          <p>{{item.hdGiftName.substring(0,3)}}</p>
          <img :src="require('assets/images/guess/starday0.png')" alt />
          <!-- <a class="prize_get_btn" v-if="item.receiveStatus>1"
            @click="showMessage('暂不可领取')">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a>
          <a class="prize_get_btn" v-else-if="item.receiveStatus ==0"
            @click="showMessage('已领取')">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a> -->

          <a class="prize_get_btn">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a>

        </li>
      </ul>
      <a class='get_award' :class="{'received':receivedFlag}" @click="getPrize()">领取奖励</a>
      <div class="pop_tips" v-if="signTips">
        <p v-if="!randomFlag">获得</p>
        <p v-else>随机获得</p>
        <span>{{signAwardNum}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import navBar from "../../components/header/nav_bar/index";
// import Vue from "vue";
import { mapGetters, mapActions } from "vuex";
export default {
  components: { navBar },
  props: [],

  data() {
    return {
      signGiftList: [], //签到礼品
      signTips: false,
      signAwardNum: 0, //随机获得的星星数量
      randomFlag: false, //显示随机的弹出提示
      signNum: 0,
      dayFlag: null,
      receivedFlag: true,
      showSignPop: false
    };
  },
  mounted() {
    //屏蔽app处理
    if (this.$route.query.clientType == 3) {
      this.$router.push({
        path: "/home",
        query: {}
      });
    }
    this.openSignPop().then(() => {
      this.showSignPop = true;
      this.verifySign();
    });
    // 阻止ios系统下弹窗拉起，底部仍会滑动的问题
    let u = navigator.userAgent;
    let isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if (isiOS) {
      console.log("isios");
      let signPop = document.getElementsByClassName("ui_pop");
      if (signPop instanceof Array) {
        signPop = signPop[0];
      }
      console.log("isios1", document.getElementsByClassName("ui_pop"));
      signPop.ontouchmove = function (e) {
        console.log("isios2");
        e.preventDefault();
      };
    }
  },
  methods: {
    //签到领取
    openSignPop() {
      return this.$post("/api/hdsign/queryHdGiftInfo")
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            this.signGiftList = dataResponse.data.signGift;
            this.signNum = dataResponse.data.signNum;
            console.log(this.signGiftList);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    getPrize() {
      return this.$post("/api/hdsign/signIn")
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            console.log("签到");
            let that = this;
            for (let i = 0; i < this.signGiftList.length; i++) {
              if (this.signGiftList[i].receiveStatus == 1) {
                if (this.signGiftList[i].sevenFlag) {
                  this.randomFlag = true;
                }
                this.signGiftList[i].receiveStatus = 0;
                this.receivedFlag = true;
                this.dayFlag = i;
              }
            }
            // this.signGiftList[index].receiveStatus = 0;
            // if (this.signGiftList[index].sevenFlag) {
            //   this.randomFlag = true;
            // }
            this.signTips = true;
            this.signAwardNum = dataResponse.data.giftCount;
            setTimeout(() => {
              that.signTips = false;
            }, 1500);
            //刷新用户余额
            this.$emit("updateWallet", dataResponse.data.giftRecScore);
            this.signNum++;
          } else {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    // 验证是否有可签到的状态
    verifySign() {
      for (let i = 0; i < this.signGiftList.length; i++) {
        if (this.signGiftList[i].receiveStatus == 1) {
          this.receivedFlag = false;
        }
      }
    },
    //弹窗提示
    showMessage(index) {
      console.log(this.signGiftList[index]);
      if (this.signGiftList[index].receiveStatus == 0) {
        this.$toast("已领取");
      } else if (this.signGiftList[index].receiveStatus == 1) {
        this.$toast("请点击按钮领取签到奖励");
      } else if (this.signGiftList[index].receiveStatus > 1) {
        this.$toast("暂不可领取");
      }
    },
    closePop() {
      if (this.dayFlag != null) {
        if (this.dayFlag < 5) {
          this.$toast(
            "明日可领取" +
            this.signGiftList[this.dayFlag + 1].hdGiftCount +
            "星星"
          );
        } else if (this.dayFlag == 5) {
          this.$toast("明日可领取随机数量星星");
        } else if (this.dayFlag == 6) {
          this.$toast("明日可领取" + this.signGiftList[0].hdGiftCount + "星星");
        }
      }
      this.$emit("closeSignPop");
    }
  }
};
</script>



<style lang='scss' scoped>
@import "../../assets/common/_base";
@import "../../assets/common/_mixin";

.Page {
  position: relative;
  width: 95.2vw;
  height: 135.4667vw;
  margin-top: -6.6667vw;
  padding-top: 37.0667vw;
  @include getBgImg("../../assets/images/guess/sign_page_bg.png");
  background-size: 100% auto;
  background-color: transparent;
}

.close {
  position: absolute;
  right: 5.3333vw;
  top: 14vw;
  width: 8vw;
  height: 8vw;
  @include getBgImg("../../assets/images/guess/close.png");
  background-size: 100% auto;
}

.att_tips {
  padding-bottom: 0.9333vw;
  font-size: 3.7333vw;
  color: #fedcd7;
  text-align: center;
  span {
    padding: 0 0.8vw;
    font-size: 4.5333vw;
    font-weight: bold;
    color: #ffe035;
  }
}

.pop_tips {
  @extend .g_v_c_mid;
  @extend .flex_v_h;
  flex-direction: column;
  -webkit-flex-direction: column;
  width: 56vw;
  height: 37.3333vw;
  text-align: center;
  background-color: #391a1c;
  border-radius: 1.3333vw;
  p {
    font-size: 3.73vw;
    color: #f58079;
  }
  span {
    padding-top: 5.33vw;
    font-size: 5.33vw;
    font-weight: bold;
    color: #fff;
  }
}
.prize_list {
  @extend .flex_v_justify;
  margin: 0 14.6667vw 0 13.8667vw;
  flex-wrap: wrap;
  -webkit-flex-wrap: wrap;
}
li {
  width: 20.8vw;
  margin-top: 2vw;
  padding-top: 1.4667vw;
  @include getBgImg("../../assets/images/guess/sign_bg.png");
  text-align: center;
  border-radius: 4px;
  overflow: hidden;
  &:last-child {
    position: relative;
    width: 100%;
    @include getBgImg("../../assets/images/guess/random_gift_bg.png");
    img {
      opacity: 0;
    }
    &::before {
      content: "";
      @extend .g_v_c_mid;
      width: 16.2667vw;
      height: 16.2667vw;
      @include getBgImg("../../assets/images/guess/random_gift.png");
      background-size: contain;
    }
    &.disabled,
    &.recevied {
      @include getBgImg("../../assets/images/guess/random_gift_disabled.png");
    }
  }
  &.disabled,
  &.recevied {
    @include getBgImg("../../assets/images/guess/sign_disabled.png");
    p,
    a {
      color: #de3b02;
      background-color: #50030a;
    }
  }
  &.recevied {
    position: relative;
    &::after {
      content: "";
      @extend .g_c_mid;
      top: 5.8667vw;
      width: 19.4667vw;
      height: 11.2vw;
      @include getBgImg("../../assets/images/guess/recevied.png");
    }
  }
  p {
    margin: 0 2.6667vw;
    padding: 0.5333vw 0;
    font-size: 2.6667vw;
    border-radius: 1.8667vw;
    color: #f63c0a;
    background-color: #ffe035;
  }
  img {
    display: block;
    height: 6.4vw;
    margin: 3.2vw auto;
    object-fit: contain;
  }
}
.prize_get_btn {
  display: block;
  line-height: 5.3333vw;
  font-size: 4vw;
  font-weight: bold;
  color: #f63c0a;
  background-color: #ffe035;
}

.get_award {
  display: block;
  width: 65.0667vw;
  margin: 2.9333vw auto 0;
  line-height: 9.3333vw;
  font-size: 5.3333vw;
  color: #d1341c;
  font-weight: bold;
  text-align: center;
  background: rgba(255, 224, 53, 1);
  box-shadow: 3px 3px 0 0 #ffa423, 0px 6px 7px 0px rgba(0, 0, 0, 0.35);
  border-radius: 9.3333vw;
  &.received {
    opacity: 0.3;
  }
}
</style>
