<template>
  <div class="Page">
    <header class="mod_header">
      <navBar pageTitle="实名认证"></navBar>
    </header>
    <div class="main">
      <section>
        <label for="real_name">真实姓名</label>

        <input v-model="userInfo.trueName" id="real_name" type="text" maxlength="5" placeholder="请输入真实姓名"
          @blur="scrollToTop">
      </section>

      <section>
        <label for="ID_num">身份证号</label>
        <input v-model="userInfo.certNo" id="ID_num" type="text" placeholder="请输入身份证号码" maxlength="18"
          @blur="scrollToTop">
      </section>
      <div style='background-color: #fff;margin-top: 10px;'>
        <phone-send-code ref='phoneSend' :time="60" btnTxt="保存" @doConfirm="toDoConfirm"
          @getSendPhoneCode="getSendPhoneCode">
        </phone-send-code>
      </div>
    </div>
  </div>
</template>

<script>
import navBar from "../../../components/header/nav_bar/index";
import phoneSendCode from "../../../components/user_info/phoneSendCode.vue";
import { getCheck } from "../../../libs/utils";
import { mapMutations } from "vuex";

export default {
  components: {
    navBar,
    phoneSendCode
  },
  data() {
    return {
      userInfo: Object
    };
  },
  created() {
    this.userInfo = this.$store.state.userCenterInfo;
    console.log(this.userInfo, "获取到的用户信息");
  },
  methods: {
    ...mapMutations(["USER_CENTER_INFO"]),
    scrollToTop: function() {
      setTimeout(function() {
        window.scrollTo(0, 0);
        console.log("回滚");
      }, 100);
    },

    //验证输入的手机号
    checkPhone(phone) {
      if (!/^1(3|4|5|6|7|8|9)\d{9}$/.test(phone)) {
        return false;
      }
      return true;
    },
    // 处理接收验证码
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
        smsType: 3
      };
      this.$post("/api/user/sendPhoneCode/", param)
        .then(dataResponse => {
          console.log(dataResponse, "实名认证");
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
    //处理提交绑定
    toDoConfirm(phone, phoneCode) {
      if (!getCheck.checkTrueName(this.userInfo.trueName)) {
        this.$toast("请输入真实姓名");
        return false;
      }
      if (!getCheck.checkId(this.userInfo.certNo)) {
        this.$toast("请填写正确身份证号");
        return false;
      }
      if (!this.checkPhone(phone)) {
        this.$toast("手机号码有误");
        return;
      }
      if (phoneCode == "") {
        this.$toast("验证码为空");
        return;
      }
      let param = {
        phone: phone,
        code: phoneCode,
        trueName: this.userInfo.trueName,
        certNo: this.userInfo.certNo
      };
      console.log(param);
      this.$post("/api/user/realNameCheck/", param)
        .then(rsp => {
          let response = rsp.data;
          if (rsp.code === "200") {
            this.USER_CENTER_INFO(this.userInfo);
            this.$router.go(-1);
          } else {
            this.$toast(rsp.message);
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
@import "../../../assets/common/_base";
@import "../../../assets/common/_mixin";
@import "../../../assets/common/_var";

section {
  position: relative;
  @extend .flex_hc;
  @include getBorder(bottom, rgba(0, 0, 0, 0.1));
  height: 44px;
  margin-top: 10px;
  padding: 0 20px;
  font-size: 14px;
  background-color: #fff;
  label {
    width: 65px;
    line-height: 44px;
  }
  input {
    flex: 1;
    -webkit-flex: 1;
    line-height: 44px;
    border: none;
  }
}
.personal_sign {
  align-items: flex-start;
  flex-direction: column;
  -webkit-flex-direction: column;
  height: initial;
  padding: 15px 20px;
  label {
    line-height: 1;
  }
  textarea {
    margin-top: 10px;
    width: 100%;
    padding-top: 5px;
    line-height: 16px;
    border: none;
    background-color: rgba(0, 0, 0, 0.05);
  }
}
.submit_btn {
  display: block;
  margin: 20px 20px 25px;
  padding: 15px;
  font-size: 18px;
  color: #fff;
  border-radius: 8px;
  text-align: center;
  background-color: $color_main;
}
</style>
