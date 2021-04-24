<template>

  <div class="god_card">
    <div class="god_info">
      <img :src="tenwinUserInfo.icon |getDefaultImg(globalConst.userDefaultIcon)" alt="" @click="goUrlPageWithUserId('/followPlan/masterHome',tenwinUserInfo.userId)">
      <div>
        <p class="name">{{tenwinUserInfo.nickName}}</p>
        <p class="rate" v-if="tenwinUserInfo.bet10WinNum">{{tenwinUserInfo.bet10WinNum}}</p>
      </div>
      <div class="options">
        <div>
          <p>{{tenwinUserInfo.bet10WinRate}}</p>
          <span>十场胜率</span>
        </div>
        <div>
          <p>{{tenwinUserInfo.bet10WinProfit}}</p>
          <span>十场收益</span>
        </div>
        <div :class="{follow_button:!tenwinUserInfo.followStatus,followed_button:tenwinUserInfo.followStatus} " @click="followUser(userId)">
          <p>关注</p>
          <span>{{tenwinUserInfo.fans}}</span>
        </div>
      </div>
    </div>
    <div class="intro">
      <div class="input">
        <p v-if="tenwinUserInfo.intro">{{tenwinUserInfo.intro}}</p>
        <!-- <input type="text" placeholder="请输入介绍" maxlength="20"> -->
        <span @click="toEdit" v-if="tenwinUserInfo.canEditIntro">编辑</span>
      </div>
    </div>
  </div>

</template>

<script>
import localStorage from '../../../../libs/storages/localStorage';

export default {
  components: {},
  props: [],
  data() {
    return {
      userId: null,
      tenwinUserInfo: {}
    };
  },
  mounted() {
    this.userId = this.$route.query.uid;
    console.log(this.userId);
    // this.getTenWinInfo();
  },
  methods: {
    goUpdatePage() {
      console.log('去编辑');
      let userId = this.userId;
      this.$router.push({
        path: '/userInfo',
        query: { id: userId }
      });
    },
    toEdit() {
      this.$router.push({
        path: '/writeInfo',
        query: {
          type: 2
        }
      });
    },
    follow(userId) {
      console.log('关注用户');
    },
    goUrlPage(url) {
      this.$router.push({
        path: url,
        query: {}
      });
    },
    getTenWinInfo() {
      let param = {};
      param.userId = this.userId;
      return this.$post('/api/planRecommend/getTenwinUserInfo', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            this.tenwinUserInfo = dataResponse.data;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    followUser(userId) {
      let val = {};
      val.userId = userId;
      let follow = !this.tenwinUserInfo.followStatus;
      if (follow) {
        val.followStatus = 1;
      } else {
        val.followStatus = 0;
      }
      console.log('关注参数', val);
      this.updateFollowStatus(val.userId, val.followStatus).then(res => {
        if (res == '200') {
          console.log('res', res);
          this.tenwinUserInfo.followStatus = follow;
          if (follow) {
            this.tenwinUserInfo.fans += 1;
          } else {
            this.tenwinUserInfo.fans -= 1;
          }
        }
      });
    },
    updateFollowStatus(userId, status) {
      let param = { type: status, usrId: userId };
      let message = '';
      if (status == 1) {
        message = '关注成功';
        localStorage.set('comment_follow_' + userId, true); //新增关注的usrId
      } else {
        message = '取消成功';
        localStorage.remove('comment_follow_' + userId); //删除取消关注的usrId
      }
      console.log('update-关注', param);
      return this.$post('/api/user/follow', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            this.$toast(message);
            return dataResponse.code;
          }
        })
        .catch(error => {
          this.$toast('网络异常，稍后再试');
          console.log(error);
          return dataResponse.code;
        });
    },
    goUrlPageWithUserId(url, userId) {
      if (this.$route.name == 'masterHome') {
        return;
      }
      this.$route.name;
      console.log('用户ID', userId);
      this.$router.push({
        path: url,
        query: { uid: userId }
      });
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../../assets/common/_base';
@import '../../../../assets/common/_mixin';

.god_card {
  padding: 0 4.2667vw;
  background-color: #fff;
}

.god_info {
  position: relative;
  @extend .flex_hc;
  padding: 3.7333vw 0;
  img {
    width: 10.4vw;
    height: 10.4vw;
    margin-right: 3.2vw;
    border: 0.2667vw solid #ff9da3;
    border-radius: 50%;
  }
  .name {
    @include t_nowrap(29.3333vw);
    font-size: 3.7333vw;
    color: #333;
  }
  .rate {
    display: inline-block;
    margin-top: 0.8vw;
    padding: 0.8vw 1.8667vw;
    font-size: 2.4vw;
    color: #fff;
    border-radius: 0.8vw;
    background-color: $color_main;
  }
}

.options {
  @extend .g_v_mid;
  right: 0;
  @extend .flex_hc;
  text-align: center;
  > div {
    margin-left: 2.1333vw;
  }
  p {
    font-size: 3.2vw;
    line-height: 3.7333vw;
    color: #333;
    font-weight: bold;
  }
  span {
    display: block;
    margin-top: 1.0667vw;
    padding: 0.8vw 1.8667vw;
    font-size: 2.4vw;
    color: #333;
    background-color: #f4f4f4;
    border-radius: 0.8vw;
  }
  .follow_button,
  .followed_button {
    padding: 1.8667vw 4vw;
    border-radius: 0.8vw;
    @include getBgLinear(bottom, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.05) 100%);
    background-color: $color_main;
    p,
    span {
      color: #fff;
      background-color: transparent;
    }
  }
  .followed_button {
    background-color: #999;
    background-image: none;
  }
}

.intro {
  border-top: 0.2667vw dashed #ddd;
  p {
    @include t_nowrap(69.3333vw);
    font-size: 3.4667vw;
    color: #666;
  }
  .input {
    @extend .flex_v_justify;
    height: 11.7333vw;
    // input {
    //   width: 66.6667vw;
    //   background-color: transparent;
    //   color: #666;
    // }
    // input::-webkit-input-placeholder {
    //   color: #666;
    // }
    span {
      padding: 1.6vw 4vw;
      font-size: 3.2vw;
      color: #fff;
      background-color: $color_main;
      border-radius: 0.8vw;
    }
  }
}
</style>
