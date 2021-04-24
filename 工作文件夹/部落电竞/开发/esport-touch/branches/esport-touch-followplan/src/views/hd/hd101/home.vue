<template>
  <div class="Page">
    <div class="main">
      <!-- 答题海报 -->
      <section class="mod_poster">
        <!-- 选中加active -->
        <a class="start_answer_btn" :class="{'active':show}" @click="joinHdSubject()"></a>
        <!-- href="/hd/hd101/answering"  -->
      </section>
      <!-- 中奖模块 -->
      <section class="mod_award">
        <div class="winner_scroll swiper-container">
          <ul class="swiper-wrapper">
            <li v-for="(item,index) in giftInfo.giftWinnerList" :key="index" class="swiper-slide">

              <span>恭喜</span>
              <span class="winner_name">{{item.userName}}</span>
              <span v-if="item.showType==0">喜中 {{item.giftNum}}星星 {{item.giftName}}</span>
              <span v-else-if="item.showType==1">营收 {{item.giftNum}}元 </span>

            </li>
          </ul>
        </div>
        <div class="award_prize">
          <p class="title">{{giftInfo.hdGiftName}}</p>
          <p>数量：<span class="num">{{giftInfo.giftRemainder}}</span>个</p>
          <p>时间：{{giftInfo.playTime}}</p>
        </div>
      </section>
    </div>
    <footer class="mod_footer">
      <tabs></tabs>
    </footer>
    <!-- 弹窗集合组件 -->
    <pops ref="propRef" @joinHdSubject="joinHdSubject"></pops>
  </div>
</template>

<script>
import tabs from "./components/tabs";
import pops from "./components/pops";
import Swiper from "swiper";
import localStorage from "../../../libs/storages/localStorage";
import wxApi from "../../../libs/weixinShare";

export default {
  components: { tabs, pops },
  props: [],
  data() {
    return {
      show: false,
      giftInfo: {},
      subject: {},
      shareCode: null,
      swiper: null
    };
  },
  mounted() {
    let shareCode = this.$route.query.shareCode;
    if (shareCode) {
      this.shareCode = shareCode;
      localStorage.set("shareCode", shareCode);
    }
    this.getGift().then(() => {
      this.$nextTick(() => {
        this.initSwiper();
      });
    });
    this.wxShare();
  },
  methods: {
    getGift() {
      let param = {};
      console.log(this.shareCode);
      if (this.shareCode) {
        param.shareCode = this.shareCode;
      }
      return this.$post("/api/subject/home", param)
        .then(rsp => {
          console.log(rsp);
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            this.giftInfo = dataResponse.data;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    initSwiper() {
      this.swiper = new Swiper(".swiper-container", {
        loop: true,
        autoplay: 1000,
        autoplayDisableOnInteraction: false,
        // autoplay: {
        //   delay: 3000,
        //   disableOnInteraction: false
        // },
        direction: "vertical"
      });
    },
    joinHdSubject(apiParam) {
      apiParam = apiParam ? apiParam : {};
      console.log(apiParam, "00000");
      let _self = this;
      this.show = true;
      setTimeout(() => {
        _self.show = false;
      }, 200);

      this.$post("/api/subject/joinSubject", apiParam)
        .then(rsp => {
          console.log(rsp);
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            let subject = dataResponse.data;
            //跳转答题页
            let queryParam = {
              hdUserLogId: subject.hdUserLogId,
              subjectId: subject.subjectId,
              subjectLogId: subject.subjectLogId
            };
            if (this.shareCode) {
              queryParam.shareCode = this.shareCode;
            }
            this.$router.push({
              name: "hd101Answering",
              query: queryParam
            });
          } else if (dataResponse.code == "1607") {
            this.$refs.propRef.openDialog(4);
          } else if (dataResponse.code == "4444") {
            this.$toast("请勿频繁重复点击");
          } else if (dataResponse.code == "2111") {
            this.$refs.propRef.openDialog(2);
            console.log("支付1毛挑战");
          } else {
            this.$toast("参加答题异常，请稍后再试");
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    wxShare() {
      let option = {
        title: "一战到底，答题赢大奖", // 分享标题, 请自行替换
        desc: "在这里要么暴露智商，要么赢取大奖，你敢来挑战吗？", // 分享描述
        imgUrl:
          "https://rs.esportzoo.com/svn/esport-res/mini/images/icon/hongbao.png" // 分享图标, 请自行替换，需要绝对路径
      };
      console.log(option);
      wxApi.wxRegister(option);
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_mixin";
@import "../../../assets/common/_base";

.Page {
  background-color: rgb(255, 231, 131);
}

.mod_poster {
  position: relative;
  padding-bottom: 93.3%;
  @include getBgImg("../../../assets/images/hd/hd101/poster.png");
}

.start_answer_btn {
  @extend .g_c_mid;
  bottom: -5.6vw;
  width: 87.5vw;
  padding-bottom: 19.7vw;
  @include getBgImg("../../../assets/images/hd/hd101/start_answer_btn.png");
  &.active {
    @include getBgImg(
      "../../../assets/images/hd/hd101/start_answer_active.png"
    );
  }
  &::after {
    content: "最先连对20道题即可领取";
    position: absolute;
    left: 21.1vw;
    top: -2.4vw;
    font-size: 3.6vw;
    color: #333;
    white-space: nowrap;
  }
}

.mod_award {
  width: 94.4%;
  height: 39.2vw;
  margin: 9.6vw auto 3.7vw;
  padding-top: 2vw;
  @include getBgImg("../../../assets/images/hd/hd101/award_bg.png");
}
.winner_scroll {
  height: 10.2vw;
  overflow: hidden;
  li {
    @extend .flex_v_h;
    color: #fff;
    span {
      font-size: 4vw;
      line-height: 10.7vw;
    }
    .winner_name {
      @include t_nowrap(110px);
      padding: 0 7px;
      color: #fcff00;
    }
  }
}

.award_prize {
  padding: 3.2vw 0 0 33.3vw;
  p {
    padding-bottom: 1.3vw;
    font-size: 3.2vw;
    color: #999;
  }
  .title {
    padding-bottom: 2.7vw;
    font-size: 5.6vw;
    color: #ffea00;
    @include t_nowrap(100%);
  }

  .num {
    padding-right: 3px;
    color: #ffea00;
  }
}
</style>
