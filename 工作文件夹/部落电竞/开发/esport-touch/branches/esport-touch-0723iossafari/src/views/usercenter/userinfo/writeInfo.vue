<template>
  <div class="Page">
    <header class="mod_header">
      <navBar pageTitle="修改个人信息"></navBar>
    </header>
    <div class="main">
      <!-- <section>
        <label for="account">账户名</label>
        <input type="text" id="account" placeholder="4-14位汉字、字母和数字的组合">
      </section> -->
      <section v-if="type ==1">
        <label for="nick">昵称:</label>

        <input type="text" id="nick" v-model="userInfo.nickName" placeholder="4-14位汉字、字母和数字的组合" minlength="4" maxlength="14" @blur="scrollToTop">
      </section>

      <section class="personal_sign" v-if="type ==2">
        <label for="p_sign">个人简介</label>

        <textarea v-model="userInfo.intro" name="" id="p_sign" cols="10" rows="10" placeholder="不能超过50个字符" maxlength="50" @blur="scrollToTop"></textarea>
      </section>

      <section v-if="type ==3">
        <label for="real_name">真实姓名</label>

        <input v-model="userInfo.trueName" id="real_name" type="text" maxlength="5" placeholder="请输入真实姓名" @blur="scrollToTop">
      </section>

      <section v-if="type ==4">
        <label for="ID_num">身份证号</label>
        <input v-model="userInfo.certNo" id="ID_num" type="text" placeholder="请输入身份证号码" maxlength="18" @blur="scrollToTop">
      </section>

      <a class="submit_btn" @click="submitData()">保存</a>
    </div>
  </div>
</template>

<script>
import navBar from '../../../components/header/nav_bar/index';
import { getCheck } from '../../../libs/utils';
import { mapMutations } from 'vuex';
export default {
  data() {
    return {
      type: Number,
      userInfo: Object
    };
  },
  created() {
    this.type = this.$route.query.type;
    this.userInfo = this.$store.state.userCenterInfo;
    console.log('writeInfo', this.userInfo);
    if (this.userInfo == null) {
      this.getUserInfo().then(data => {
        this.userInfo = this.$store.state.userCenterInfo;
      });
    }
  },
  methods: {
    ...mapMutations(['USER_CENTER_INFO']),
    submitData() {
      let form = new FormData();

      if (this.type == 1 && (!getCheck.checkName(this.userInfo.nickName) || this.userInfo.nickName.length > 14)) {
        this.$toast('请输入4-14位汉字、字母和数字的组合');
        return false;
      }
      if (this.type == 2 && this.userInfo.intro.length > 50) {
        this.$toast('简介不可超过50个字符');
        return false;
      }
      if (this.type == 3 && !getCheck.checkTrueName(this.userInfo.trueName)) {
        this.$toast('请输入真实姓名');
        return false;
      }
      if (this.type == 4 && !getCheck.checkIdCard(this.userInfo.certNo)) {
        this.$toast('请填写正确身份证号');
        return false;
      }
      if (this.type == 1) {
        form.append('nickName', this.userInfo.nickName);
      } else if (this.type == 2) {
        form.append('intro', this.userInfo.intro);
      } else if (this.type == 3) {
        form.append('trueName', this.userInfo.trueName);
      } else if (this.type == 4) {
        form.append('certNo', this.userInfo.certNo);
      }
      return this.$post('/api/user/updateUserInfo', form)
        .then(rsp => {
          let response = rsp.data;
          if (rsp.code === '200') {
            this.USER_CENTER_INFO(this.userInfo);
            this.$router.go(-1);
          } else {
            this.$toast(rsp.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    scrollToTop: function() {
      setTimeout(function() {
        window.scrollTo(0, 0);
        console.log('回滚');
      }, 100);
    },
    getUserInfo() {
      let param = {};
      return this.$post('/api/usercenter/ucIndexdata', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            console.log(dataResponse, '个人设置');
            this.userInfo = dataResponse.data;
            this.$wxApi.wxRegister({
              title: '赢加竞技-（' + this.userInfo.nickName + '）个人中心',
              desc: '游戏爱好者聚集地',
              imgUrl: 'http://rs.esportzoo.com/svn/esport-res/mini/images/default/juzi_logo.jpg'
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
    navBar
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';
@import '../../../assets/common/_var';

.Page {
  background-color: #fff;
}

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
  background-color: $color_btn;
}
</style>
