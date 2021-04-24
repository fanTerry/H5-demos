<template>
  <div class='Page userCenterInfo_Page'>
    <header class="mod_header">
      <navBar pageTitle="个人设置"></navBar>
    </header>

    <div class="main">
      <section>
        <div class="item">
          <span>我的头像</span>
          <div class="flex_hc">
            <img :src="userInfo.icon|getDefaultImg(globalConst.userDefaultIcon)" alt="">
            <span class="link_icon"></span>
          </div>
          <vue-core-image-upload :class="['btn', 'btn-primary','user_input']" @imageuploaded="imageuploaded"
            @imagechanged="imagechanged" @imageuploading="imageuploading" @errorhandle="errorhandle"
            extensions="png,jpeg,jpg" compress="70" :url="uploadImgUrl">

          </vue-core-image-upload>
          <!-- <input class="user_input" type="file" @change="changeImage($event)" ref="avatar" accept="image/*"> -->
        </div>
        <div class="item" v-show="biz==1">
          <span>账户名</span>
          <div class="flex_hc">{{userInfo.account}}</div>
        </div>
        <div class="item" @click="goUpdatePage(1)">
          <span>昵称</span>
          <div class="flex_hc">{{userInfo.nickName}}<span class="link_icon"></span></div>
        </div>
        <div class="item" @click="goUpdatePage(2)">
          <span>个人简介</span>
          <div class="flex_hc">{{userInfo.intro?userInfo.intro:"未设置"}}<span class="link_icon"></span></div>
        </div>
      </section>

      <section>
        <div class="item" @click="goRealNameCheck()" v-if="!userInfo.trueName">
          <span>真实姓名</span>
          <div class="flex_hc">未设置<span class="link_icon"></span></div>
        </div>
        <div class="item" v-else>
          <span>真实姓名</span>
          <div class="flex_hc">{{userInfo.trueName}}</div>
        </div>
        <div class="item" @click="goRealNameCheck()" v-if="!userInfo.certNo">
          <span>身份证号</span>
          <div class="flex_hc">未设置<span class="link_icon"></span></div>
        </div>
        <div class="item" v-else>
          <span>身份证号</span>
          <div class="flex_hc">{{userInfo.certNo|subStr(12,'******')}}</div>
        </div>
        <div class="item" @click="goUrlPage('/uc/bindPhone')" v-if='!userInfo.phone'>
          <span>手机号</span>
          <div class="flex_hc">未绑定<span class="link_icon"></span>
          </div>
        </div>
        <div class="item" v-else>
          <span>手机号</span>
          <div class="flex_hc">{{userInfo.phone|subStr(7,'*****')}}</div>
        </div>
      </section>

      <section>
        <div class="item" @click="goUrlPage('/login/agreement')">
          <span>用户服务协议</span>
          <div class="flex_hc"><span class="link_icon"></span></div>
        </div>
        <div class="item" @click="goUrlPage('/login/pravicy')">
          <span>隐私政策</span>
          <div class="flex_hc"><span class="link_icon"></span></div>
        </div>
      </section>
      <section v-show="accountLoginFlag">
        <div class="item" @click="goUrlPage('/resetPassword?type=1')">
          <span>修改密码</span>
          <div class="flex_hc"><span class="link_icon"></span></div>
        </div>
      </section>
      <!--H5, IOS和安卓可以退出登录-->
      <a class="login_out" @click="logout()" v-if="accountLoginFlag">退出登录</a>
      <popAlert :showPop='showPop' @close='showPop=false' @confirm='confirmLogOut' type="2">
        <p>确定退出登录?</p>
      </popAlert>
      <loading v-if="isLoading" :text="'正在提交中...'"></loading>
    </div>
  </div>
</template>

<script>
import navBar from "../../../components/header/nav_bar/index";
import { mapMutations } from "vuex";
import VueCoreImageUpload from "vue-core-image-upload";
import loading from "components/common/loading";
import popAlert from "components/pop_up/pop_alert";
import { delCookie } from "../../../libs/utils";
export default {
  data() {
    return {
      userInfo: Object,
      isLoading: false,
      showPop: false,
      biz: 1,
      clientType: 6,
      uploadImgUrl: "",
      accountLoginFlag: false
    };
  },
  created() {
    console.log("用户信息1111", this.$store.state.userCenterInfo);
    if (!this.$store.state.userCenterInfo) {
      this.getUserInfo();
    } else {
      this.userInfo = this.$store.state.userCenterInfo;
    }
  },
  mounted() {
    this.biz = this.baseParamConfig.biz;
    this.clientType = this.baseParamConfig.clientType;
    console.log(this.biz, " 用户信息展示this.biz");
    this.uploadImgUrl =
      "/api/user/updateUserInfo?agentId=" +
      this.baseParamConfig.agentId +
      "&biz=" +
      this.biz +
      "&clientType=" +
      this.clientType;
    if (
      (this.biz == 1 && this.clientType == 6) ||
      this.clientType == 3 ||
      this.clientType == 4
    ) {
      this.accountLoginFlag = true;
    }
  },
  methods: {
    ...mapMutations(["USER_CENTER_INFO"]),
    imageuploaded(res) {
      console.log(res, "imageuploaded");
      if (res.code === "200") {
        console.log("----", res.data.icon);
        this.userInfo.icon = res.data.icon;
        this.USER_CENTER_INFO(this.userInfo);
        this.isLoading = false;
        this.$toast("修改成功");
      } else {
        this.isLoading = false;
        if (res.message) {
          this.$toast(res.message);
          return;
        }
        this.$toast("图片不可超过5M");
      }
    },
    imagechanged(res) {
      console.log("size", res.size);
    },
    imageuploading() {
      this.isLoading = true;
    },
    errorhandle() {
      this.isLoading = false;
    },
    changeImage($event) {
      let form = new FormData();
      //
      form.append("files", this.$refs.avatar.files[0]);
      console.log(this.$refs.avatar.files[0]);
      return this.$axios
        .post("/api/user/updateUserInfo", form)
        .then(rsp => {
          let response = rsp.data;
          console.log("返货", response.code);
          if (response.code === "200") {
            console.log("返货", response);
            this.userInfo = response;
            this.USER_CENTER_INFO(this.userInfo);
          } else {
            this.$toast(response.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    goUrlPage(path) {
      this.$router.push({
        path: path,
        query: {}
      });
    },
    goUpdatePage(type, param) {
      this.$router.push({
        path: "/writeInfo",
        query: {
          type: type
        }
      });
    },
    goRealNameCheck() {
      this.$router.push({
        path: "/uc/userAuth",
        query: {}
      });
    },
    logout() {
      this.showPop = true;
    },
    confirmLogOut() {
      this.showPop = false;
      this.$post("/api/phoneloginout")
        .then(dataResponse => {
          if (dataResponse.code != "200") {
            this.$toast("系统异常");
            return;
          }
          window.localStorage.removeItem("token");
          //删除H5cookie
          delCookie("h5_login_cookie_sid");

          this.$toast("退出成功");
          this.$router.push({
            name: "home"
          });
        })
        .catch(error => {
          console.log(error);
        });
    },
    getUserInfo() {
      let param = {};
      this.$post("/api/usercenter/ucIndexdata", param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            console.log(dataResponse, "个人设置");
            this.userInfo = dataResponse.data;
            this.$wxApi.wxRegister({
              title: "动动电竞-（" + this.userInfo.nickName + "）个人中心",
              desc: "游戏爱好者聚集地",
              imgUrl:
                "http://rs.esportzoo.com/svn/esport-res/mini/images/default/juzi_logo.jpg"
            });
            this.USER_CENTER_INFO(dataResponse.data);
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  components: {
    navBar,
    "vue-core-image-upload": VueCoreImageUpload,
    loading,
    popAlert
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_base";
@import "../../../assets/common/_mixin";

section {
  margin-top: 10px;
}

.item {
  position: relative;
  @extend .flex_v_justify;
  @include getBorder(bottom, rgba(0, 0, 0, 0.1));
  height: 44px;
  padding: 0 20px;
  font-size: 14px;
  background-color: #fff;
  img {
    width: 30px;
    height: 30px;
    border-radius: 50%;
    object-fit: cover;
  }
  .user_input {
    position: absolute;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0;
  }
  > div {
    color: rgba(0, 0, 0, 0.4);
  }
  .link_icon {
    width: 20px;
    height: 20px;
    margin-left: 5px;
    @include getArrow(10px, rgba(0, 0, 0, 0.3), right);
  }
  .personal_sign {
    @include t_nowrap(180px);
    line-height: 1.2;
  }
}

.login_out {
  display: block;
  margin-top: 8px;
  line-height: 48px;
  font-size: 14px;
  color: #333;
  text-align: center;
  background-color: #fff;
}
</style>
