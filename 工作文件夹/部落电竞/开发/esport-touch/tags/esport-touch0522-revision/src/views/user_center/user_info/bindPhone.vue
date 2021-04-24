<template>
  <div class="Page">
    <header class="mod_header">
      <navBar pageTitle="绑定手机号"></navBar>
    </header>
    <div class="main">
      <phone-send-code ref='phoneSend' :time="60" :btnTxt="'确认绑定'" @doConfirm="toBindPhone"
        @getSendPhoneCode="getSendPhoneCode">
      </phone-send-code>
      <p class="att_tips">点击上面的“确认绑定”按钮，即表示您同意</p>
      <p class="rules">枫叶电竞软件许可及服务协议，枫叶电竞版权保护说明，用户私隐政策</p>
    </div>
  </div>
</template>

<script>
import navBar from "../../../components/header/nav_bar/index";
import phoneSendCode from "../../../components/user_info/phoneSendCode.vue";
import { mapMutations } from "vuex";
export default {
  components: {
    navBar,
    phoneSendCode
  },
  data() {
    return {};
  },
  methods: {
    ...mapMutations(["USER_CENTER_INFO"]),
    //获取验证码
    getSendPhoneCode(phone) {
      if (phone == "") {
        this.$toast("请先输入手机号");
        return;
      }
      if (!this.checkPhone(phone)) {
        this.$toast("手机号码有误，请重填");
        return;
      }
      let param = {
        phone: phone,
        smsType: 2
      };
      this.$post("/api/user/sendPhoneCode/", param)
        .then(dataResponse => {
          console.log(dataResponse, "bindingPhoneGetCode");
          if (dataResponse.code == 200 && dataResponse.data) {
            this.$toast("发送成功,请注意查收~");
          } else {
            this.$toast(dataResponse.message);
            this.$refs.phoneSend.setClickFlag(false);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    //验证输入的手机号
    checkPhone(phone) {
      if (!/^1(3|4|5|6|7|8|9)\d{9}$/.test(phone)) {
        return false;
      }
      return true;
    },
    //绑定手机号
    toBindPhone(phone, phoneCode) {
      if (phone == "") {
        this.$toast("手机号为空");
        return;
      }
      if (phoneCode == "") {
        this.$toast("验证码为空");
        return;
      }
      let param = {
        phone: phone,
        code: phoneCode
      };
      this.$post("/api/usercenter/bindingPhone/", param)
        .then(dataResponse => {
          if (dataResponse.code != "200") {
            this.$toast(dataResponse.message);
          } else {
            this.USER_CENTER_INFO({
              ...this.$store.state.userCenterInfo,
              phone: phone
            });
            //跳个人中心
            this.$router.replace({
              name: "userInfo",
              params: {}
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_mixin";
@import "../../../assets/common/_base";
@import "../../../assets/common/_var";

.mod_header {
  h2 {
    position: relative;
  }
  .meet_problem {
    @extend .g_v_mid;
    right: 20px;
    color: #999;
    font-size: 13px;
  }
}

.main {
  padding-top: 25px;
  .disabled {
    opacity: 0.5;
  }
  .att_tips {
    padding-top: 10px;
    padding-left: 20px;
    color: #7c7c7c;
  }
  .rules {
    padding: 20px 20px 0;
    line-height: 18px;
    color: #494949;
  }
}
</style>
