<!--
 * @Author: your name
 * @Date: 2020-06-01 09:48:51
 * @LastEditTime: 2020-06-22 15:12:42
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch/src/components/user_info/index.vue
--> 
<template>
  <div class="user_info">
    <div v-on:click.stop="toAuthorDetail(issueUserId)">
      <div class="user_img ">
        <img :src="follower.userIcon|getDefaultImg(globalConst.userDefaultIcon)" alt="">
        <span class="god_player expert_player" v-if="follower.userType==1" >大神</span>
      </div>
      <div>
        <div class="name">{{follower.userNickName}}</div>
        <!-- <p class="tips">
          <span>{{follower.fans}}粉丝</span>
          <span>100万阅读</span>
        </p> -->
        <p class="expert_tips">
          <span v-if="follower.tenWinNum">{{follower.tenWinNum}}</span>
          <span v-if="follower.tenWinProfit">近十场收益：{{follower.tenWinProfit}}</span></p>
      </div>
    </div>
    <template v-if="type==1">
      <div class="follow_btn" v-on:click.stop="follow(issueUserId,1)" v-if="follower.followStatus==0  ">关注</div>
      <div class="followed_btn" v-on:click.stop="follow(issueUserId,0)" v-if="follower.followStatus==1 ">已关注</div>
    </template>
    <template v-else>
      <div class="follow_btn" @click="follow(follower.userId,1)" v-if="follower.followStatus==0&&type!=1">关注</div>
      <div class="followed_btn" @click="follow(follower.userId,0)" v-if="follower.followStatus==1&&type!=1">已关注</div>
    </template>
  </div>
</template>

<script>
export default {
  props: ['follower', 'type', 'issueUserId'],
  data() {
    return {};
  },
  methods: {
    follow(userId, followStatus) {
      let param = {};
      param.userId = userId;
      param.followStatus = followStatus;
      this.$emit('follow', param);
    },
    toAuthorDetail(userId) {
      this.$router.push({
        path: '/userCenter/userPublishArticle',
        query: {
          id: userId
        }
      });
    }
  },
  components: {}
};
</script>

<style lang='scss' scoped>
@import '../../assets/common/_base.scss';
@import '../../assets/common/_mixin.scss';
@import '../../assets/common/_var.scss';

.user_info {
  @extend .flex_v_justify;
  padding: 3.2vw 0;
  > div {
    @extend .flex_hc;
  }
}
.user_img {
  position: relative;
  width: 6.6667vw;
  height: 6.6667vw;
  margin-right: 2.1333vw;
  &.active::after {
    content: '';
    position: absolute;
    right: 0;
    top: 0;
    transform: translate(30%, -30%);
    width: 2.4vw;
    height: 2.4vw;
    border: 1px solid #fff;
    border-radius: 50%;
    background-color: #ff3b30;
  }
  img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
  .god_player,
  .expert_player {
    display: none;
    @extend .g_c_mid;
    bottom: -0.8vw;
    @include getBtn(15.7333vw, 4.5333vw, 3.2vw, #fff, #ff9da3, 34px);
    font-weight: bold;
  }
  .expert_player {
    background-color: $color_main;
  }
}
.name {
  @include t_nowrap(40vw);
  font-size: 3.4667vw;
  line-height: 1.2;
  font-weight: 500;
  color: #333;
}
.tips span {
  display: block;
  margin-top: 2.1333vw;
  padding-right: 10px;
  font-size: 3.7333vw;
  line-height: 4.2667vw;
  color: #999;
}

.expert_tips {
  display: none;
  @extend .flex_hc;
  margin-top: 2.1333vw;
  span {
    @include getBtn(auto, 4vw, 2.4vw, #fff, $color_main, 4px);
    margin-right: 1.3333vw;
    padding: 0 1.8667vw;
  }
}
</style>
