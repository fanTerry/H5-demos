<template>
  <div class="ui_pop" v-if="getLoginData.loginShowType">
    <!-- 微信登录 -->
    <div class="login" v-if="getLoginData.codeType==1">
      <a class="close" @click="loginClosePop"></a>
      <p class="login_title">登录微信</p>
      <img class="login_img" src="../../../assets/images/pop/big_star.png" alt />
      <p class="login_att">领取星星，参与竞猜</p>
      <p class="login_tips">稀有皮肤、话费、Q币海量奖品等你拿</p>
      <a class="login_btn" @click="wxCodeLogin">立即登录</a>
    </div>

    <!-- 二维码扫一扫 -->
    <div class="qrCode" v-if="getLoginData.codeType==2">
      <a class="close" @click="loginClosePop"></a>
      <img class="qrCode_img" v-bind:src="qrcodeUrl" alt />
      <p class="qrCode_tips">
        微信
        <span>扫一扫</span>关注
        <br />橘子电竞快速登陆
      </p>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import localStorage from "../../../libs/storages/localStorage";
import cookie from "../../../libs/common/cookie";
import { mapGetters, mapActions } from "vuex";
export default {
  components: {},
  props: [],
  data() {
    return {
      // iconImg: null,
      sceneStr: null,
      qrcodeUrl: null,
      websock: null,
      wx_account_login_cookie_sid: null,
      //缓存用户信息
      wxUserInfo:{
        nickName:null,
        icon:null,
        userId:null
      }
    };
  },
  computed: {
    ...mapGetters({
      getLoginData: "getLoginData", // 获取登录状态
      getUserData: "getUserData"
    })
  },
  methods: {
    ...mapActions(["setLoginData", "setUserData"]), // 通过vuex控制用户登录状态和用户星星、椰云分
    //用户登录
    wxCodeLogin() {
      return this.$post("/api/wxlogin/scanLogin")
        .then(rsp => {
          const dataResponse = rsp;
          this.qrcodeUrl = dataResponse.qrcodeUrl;
          this.sceneStr = dataResponse.sceneStr;
          this.initWebSocket();
        })
        .catch(error => {
          console.log(error);
        });
    },
    //初始化
    initWebSocket() {
      if ("WebSocket" in window) {
        console.log("开始连接websocket");
        const wsuri = "ws://quiz.esportzoo.com/websocket"; //ws地址
        this.websock = new WebSocket(wsuri);
        this.websock.onopen = this.websocketonopen; //连接WebSocket
        this.websock.onerror = this.websocketonerror;
        this.websock.onmessage = this.websocketonmessage;
        this.websock.onclose = this.websocketclose;
        console.log("开始连接22websocket");
      } else {
        alert("当前浏览器无法获取二维码，请更换浏览器");
      }
    },
    websocketonopen() {
      console.log("WebSocket连接成功");
      this.websock.send(this.sceneStr);
      this.setLoginData({
        ...this.getLoginData,
        loginShowType: true, // 控制登录弹窗
        codeType: 2
      });
    },
    websocketonerror(e) {
      //错误
      console.log("WebSocket连接发生错误");
      this.initWebSocket();
    },
    //接收数据
    websocketonmessage(e) {
      this.userInfo = JSON.parse(e.data);
      this.wx_account_login_cookie_sid = this.userInfo.sId;
      cookie.setCookie(
        "wx_account_login_cookie_sid",
        this.wx_account_login_cookie_sid,
        1
      );
      this.wxUserInfo.nickName=this.userInfo.nickName;
      this.wxUserInfo.icon=this.userInfo.icon;
      this.wxUserInfo.userId=this.userInfo.id;
      //只缓存部分用户信息
      localStorage.set("user", this.wxUserInfo);
      //用户成功登录后改登录标志位true
      this.setLoginData({
        loginState: true,
        loginType: 2,
        loginShowType: false,
        codeType: 0
      });
      this.setUserData({
        ...this.getUserData,
        nickName: this.userInfo.nickName, // 用户昵称
        icon: this.userInfo.icon // 用户头像
      });
      this.$parent.dataRefresh();
      this.websock.onclose(); //关闭WebSocket
    },
    websocketclose() {
      console.log("WebSocket连接关闭"); //关闭
    },
    //关闭弹窗
    loginClosePop() {
      this.setLoginData({
        ...this.getLoginData,
        loginShowType: false, // 控制登录弹窗
        codeType: 0
      });
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

.login {
  width: 403px;
  height: 536px;
  text-align: center;
  @include getBgImg("../../../assets/images/pop/login_wx_bg.png");
  .close {
    top: 40px;
    right: 30px;
  }
}

.login_title {
  padding-top: 72px;
  font-size: 24px;
  color: #ffdcd7;
}

.login_img {
  width: 153px;
  margin-top: 55px;
}

.login_att {
  padding-top: 55px;
  font-size: 24px;
  color: #ffdcd7;
}

.login_tips {
  padding-top: 19px;
  font-size: 14px;
  color: #ffbea3;
}

.login_btn {
  display: inline-block;
  width: 318px;
  height: 48px;
  margin-top: 26px;
  line-height: 48px;
  font-size: 16px;
  color: #ffdcd7;
  @include getBgImg("../../../assets/images/home/guess_now.png");
}

.qrCode {
  width: 647px;
  height: 300px;
  @include getBgImg("../../../assets/images/pop/qrCode_bg.png");
  background-size: 100% 100%;
  .close {
    right: 30px;
    top: 33px;
  }
}

.qrCode_img {
  @extend .g_v_mid;
  left: 72px;
  width: 200px;
  height: 200px;
}

.qrCode_tips {
  @extend .g_v_mid;
  right: 80px;
  font-size: 30px;
  line-height: 50px;
  color: #fff;
  text-align: center;
  span {
    color: #27c5d3;
  }
}

.confirm_pay,
.orderDetails,
.recharge,
.prize,
.qrCodePay,
.payStatus {
  background: linear-gradient(to right bottom, #34201d, #2a181c);
  background: -webkit-linear-gradient(left top, #34201d, #2a181c);
  background: -ms-linear-gradient(left top, #34201d, #2a181c);
  box-shadow: 0px 0px 15px 0px rgba(10, 2, 4, 0.5);
}
</style>
