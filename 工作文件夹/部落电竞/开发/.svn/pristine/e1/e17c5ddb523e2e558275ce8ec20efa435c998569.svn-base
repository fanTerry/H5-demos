<template>
  <div class="ui_pop" v-if="showSignPop">
    <div class='Page'>
      <h3>每日签到<a class='close' @click="closePop"></a></h3>
      <p class="att_tips">本周您已连续签到 <span>{{signNum}}</span> 天</p>
      <ul class="prize_list">
        <!-- 不能领取加类名disabled -->
        <li v-for="(item,index) in signGiftList " :key="index" :class="{'recevied':item.receiveStatus==0,'disabled':item.receiveStatus>1}" @click="showMessage(index)">
          <p class="day">{{item.hdGiftName.substring(0,3)}}</p>
          <p v-if="item.receiveStatus!=0" class="num">{{item.hdGiftCount?item.hdGiftCount:''}}</p>
          <!-- <a class="prize_get_btn" v-if="item.receiveStatus>1"
            @click="showMessage('暂不可领取')">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a>
          <a class="prize_get_btn" v-else-if="item.receiveStatus ==0"
            @click="showMessage('已领取')">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a> -->

          <a class="prize_get_btn" v-if="item.receiveStatus==0">{{item.hdGiftCount?item.hdGiftCount:item.sevenFlag}}</a>

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
import navBar from '../../components/header/nav_bar/index';
// import Vue from "vue";
import { mapGetters, mapActions } from 'vuex';
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
      // this.$router.push({
      //   path: "/home",
      //   query: {}
      // });
    }
    this.openSignPop().then(() => {
      this.showSignPop = true;
      this.verifySign();
    });
    // 阻止ios系统下弹窗拉起，底部仍会滑动的问题
    let u = navigator.userAgent;
    let isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
    if (isiOS) {
      console.log('isios');
      let signPop = document.getElementsByClassName('ui_pop');
      if (signPop instanceof Array) {
        signPop = signPop[0];
      }
      console.log('isios1', document.getElementsByClassName('ui_pop'));
      signPop.ontouchmove = function(e) {
        console.log('isios2');
        e.preventDefault();
      };
    }
  },
  methods: {
    //签到领取
    openSignPop() {
      return this.$post('/api/hdsign/queryHdGiftInfo')
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
      return this.$post('/api/hdsign/signIn')
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            console.log('签到');
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
            this.$emit('updateWallet', dataResponse.data.giftRecScore);
            this.signNum = parseInt(this.signNum) + 1;
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
        this.$toast('已领取');
      } else if (this.signGiftList[index].receiveStatus == 1) {
        this.$toast('请点击按钮领取签到奖励');
      } else if (this.signGiftList[index].receiveStatus > 1) {
        this.$toast('暂不可领取');
      }
    },
    closePop() {
      if (this.dayFlag != null) {
        if (this.dayFlag < 5) {
          this.$toast('明日可领取' + this.signGiftList[this.dayFlag + 1].hdGiftCount + '星星');
        } else if (this.dayFlag == 5) {
          this.$toast('明日可领取随机数量星星');
        } else if (this.dayFlag == 6) {
          this.$toast('明日可领取' + this.signGiftList[0].hdGiftCount + '星星');
        }
      }
      this.showSignPop = false;
      this.$emit('closeSignPop');
    }
  }
};
</script>



<style lang='scss' scoped>
@import '../../assets/common/_base';
@import '../../assets/common/_mixin';

.Page {
  position: relative;
  width: 89.3333vw;
  height: 81.6vw;
  border-radius: $border_radius;
  background-color: $color_item;
  overflow: hidden;
}

h3 {
  position: relative;
  @include getBtn(auto, 9.6vw, 4.2667vw, #fff, #353447, 0);
  @include getBorder(bottom, #565279);
}

.close {
  @extend .g_v_mid;
  right: 0.5333vw;
  width: 9.6vw;
  height: 9.6vw;
  @include getBgImg('../../assets/images/guess/sign_close.png');
  background-size: 4.2667vw;
}

.att_tips {
  padding: 4.2667vw 0;
  font-size: 3.7333vw;
  color: #fff;
  text-align: center;
  span {
    padding: 0 0.8vw;
    font-size: 4.5333vw;
    font-weight: bold;
    color: $color_yellow;
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
  background-color: $color_item;
  border-radius: 1.3333vw;
  p {
    font-size: 3.73vw;
    color: #fff;
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
  margin: 0 4.2667vw;
  flex-wrap: wrap;
  -webkit-flex-wrap: wrap;
}
li {
  position: relative;
  width: 18.1333vw;
  height: 20.8vw;
  margin-bottom: 2.6667vw;
  border-radius: $border_radius;
  color: #c85958;
  @include getBgImg('../../assets/images/guess/sign_bg.png');
  overflow: hidden;
  &:last-child {
    position: relative;
    width: 39.0667vw;
    @include getBgImg('../../assets/images/guess/random_gift_bg.png');
    img {
      opacity: 0;
    }
    &::before {
      content: '';
      @extend .g_v_c_mid;
      width: 11.4667vw;
      height: 11.4667vw;
      @include getBgImg('../../assets/images/guess/random_gift.png');
      background-size: contain;
    }
    &.disabled,
    &.recevied {
      @include getBgImg('../../assets/images/guess/random_gift_disabled.png');
    }
  }
  &.disabled,
  &.recevied {
    @include getBgImg('../../assets/images/guess/sign_disabled.png');
    .day {
      color: rgba(255, 255, 255, 0.5);
    }
    .num {
      color: #fff;
    }
  }
  &.recevied {
    position: relative;
    &::after {
      content: '';
      @extend .g_c_mid;
      top: 5.8667vw;
      width: 8vw;
      height: 8vw;
      @include getBgImg('../../assets/images/guess/recevied.png');
    }
  }
  .day {
    display: inline-block;
    padding: 1.3333vw 3.3333vw 1.3333vw 1.3333vw;
    font-size: 2.6667vw;
    color: #fff;
  }
  .num {
    padding-top: 3.2vw;
    font-size: 5.3333vw;
    font-weight: bold;
    color: #c85958;
    text-align: center;
  }
  img {
    display: block;
    height: 6.4vw;
    margin: 1.8667vw auto 2.4vw;
    object-fit: contain;
  }
}
.prize_get_btn {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  line-height: 5.3333vw;
  font-size: 2.6667vw;
  font-weight: bold;
  text-align: center;
  color: #fff;
  background-color: #353447;
}

.get_award {
  display: block;
  margin-top: 1.8667vw;
  line-height: 10.4vw;
  font-size: 4vw;
  color: #fff;
  font-weight: bold;
  text-align: center;
  background: $color_btn;
  &.received {
    opacity: 0.3;
  }
}
</style>
