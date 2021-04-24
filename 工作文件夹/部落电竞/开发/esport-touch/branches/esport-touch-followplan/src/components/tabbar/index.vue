<template>
  <section class="tabbar">
    <ul>
      <li @click="gotoAddress({path: '/home', query: {}})" :class="{cur:($route.path.indexOf('/guess')==-1&&($route.path.indexOf('/home')!== -1 || $route.path=='/')) }">
        <i class="home"></i>
        <span>首页</span>
      </li>
      <!-- <li @click="gotoAddress({path: '/match', query: {}})"
        :class="{cur:$route.path.indexOf('match')!== -1 || $route.path.indexOf('matchTool')!== -1 || $route.path.indexOf('expert')!== -1}">
        <i class="match"></i>
        <span>赛事</span>
      </li> -->
      <li @click="gotoAddress({path: '/community', query: {}})"
        :class="{cur:$route.path.indexOf('community')!== -1 || $route.path.indexOf('matchTool')!== -1 || $route.path.indexOf('myFollow')!== -1}">
        <i class="community"></i>
        <span>发现</span>
      </li>
      <!-- <li v-if='showFlag' @click="gotoAddress({path: '/store', query: {}})"
        :class="{cur:$route.path.indexOf('store')!== -1}">
        <i class="store"></i>
        <span>商城</span>
      </li> -->
      <li v-if='showGuess||inviteCodeType' @click="gotoAddress({path: '/guess/home', query: {}})" :class="{cur:$route.path.indexOf('/guess/home')!== -1}">
        <i class="guess"></i>
        <span>预测</span>
      </li>
      <!-- <li @click="gotoAddress({path: '/expert', query: {}})" :class="{cur:$route.path.indexOf('expert')!== -1}">
        <i class="expert"></i>
        <span>专家</span>
      </li> -->
      <li v-if='showGuess||inviteCodeType' @click="gotoAddress({path: '/followPlan', query: {}})" :class="{cur:$route.path.indexOf('followPlan')!== -1}">
        <i class="follow_plan"></i>
        <span>大神</span>
      </li>
      <li @click="gotoAddress({path: '/userCenter', query: {}})" :class="{cur:$route.path.indexOf('userCenter')!== -1}">
        <i class="user_center"></i>
        <span>我的</span>
        <p class="tips_num" v-if="messageNum">{{messageNum > 99?'···':messageNum}}</p>
      </li>
    </ul>
  </section>
</template>

<script>
export default {
  props: [],
  data() {
    return {
      showFlag: false,
      messageNum: 0,
      inviteCodeType: false,
      showGuess: false
    };
  },
  mounted() {
    this.queryInviteCode();
    if (this.$route.query.agentId == 1004) {
      this.showFlag = false;
    }
    //只有H5账号密码登录才能显示预测
    var clientType = this.$route.query.clientType;
    var biz = this.$route.query.biz;
    if (clientType == 6 && biz == 1) {
      this.showFlag = true;
    }
    if (!this.$route.meta.keepAlive) {
      this.getMessageNum();
    }
    this.judgeShowGuess();
  },
  activated() {
    this.getMessageNum();
  },
  methods: {
    judgeShowGuess() {
      let clientType = this.$route.query.clientType;
      let agentId = this.$route.query.agentId;
      if (clientType == 3 && (agentId == '200001' || agentId == '200002' || agentId == '200003' || agentId == '200004' || agentId == '200005')) {
        this.showGuess = true;
      }
    },
    queryInviteCode() {
      this.$post('/api/user/queryInviteCode')
        .then(rsp => {
          console.log(dataResponse, '结果');
          const dataResponse = rsp;
          if (dataResponse.code == '200' && dataResponse.data) {
            this.inviteCodeType = true;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },

    gotoAddress(path) {
      this.$router.push(path);
    },
    //获取未读相关信息数量
    getMessageNum() {
      let param = {};
      this.$post('/api/usercenter/ucIndexdata', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            console.log('个人中心首页--setHeader---请求成功');
            this.messageNum = parseInt(dataResponse.data.messageNum);
            console.log(this.messageNum);
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  components: {}
};
</script>

<style lang='scss' scoped>
@import '../../assets/common/_base.scss';
@import '../../assets/common/_mixin.scss';
.tabbar {
  color: #999;
  background-color: #fff;
  ul {
    @extend .flex_hc;
    justify-content: space-around;
    -webkit-justify-content: space-around;
    padding: 4px 0;
  }
  li {
    position: relative;
    flex: 1;
    -webkit-flex: 1;
  }
  i {
    display: block;
    width: 26px;
    height: 26px;
    margin: 0 auto;
  }
  span {
    display: block;
    margin-top: 3px;
    font-size: 10px;
    line-height: 12px;
    text-align: center;
  }
  .tips_num {
    @extend .flex_v_h;
    position: absolute;
    left: 50%;
    top: 0;
    transform: translateX(1px);
    -webkit-transform: translateX(1px);
    width: 14px;
    height: 14px;
    font-size: 8px;
    color: #fff;
    background-color: #ff0000;
    border: 1px solid #fff;
    border-radius: 50%;
  }
}

.cur {
  color: $color_main;

  .home {
    @include getBgImg('../../assets/images/home/home_cur.png');
    background-size: 100% auto;
  }
  .match {
    @include getBgImg('../../assets/images/match/match_cur.png');
    background-size: 100% auto;
  }
  .follow {
    @include getBgImg('../../assets/images/follow/follow_cur.png');
    background-size: 100% auto;
  }
  .community {
    @include getBgImg('../../assets/images/community/community_cur.png');
    background-size: 100% auto;
  }
  // .store {
  //   @include getBgImg("../../assets/images/store/store_cur.png");
  //   background-size: 100% auto;
  // }
  .guess {
    @include getBgImg('../../assets/images/guess/guess_cur.png');
    background-size: 100% auto;
  }
  .expert {
    @include getBgImg('../../assets/images/expert/expert_cur.png');
    background-size: 100% auto;
  }
  .follow_plan {
    @include getBgImg('../../assets/images/followplan/follow_plan_cur.png');
    background-size: 100% auto;
  }
  .user_center {
    @include getBgImg('../../assets/images/user_center/user_center_cur.png');
    background-size: 100% auto;
  }
}

.home {
  @include getBgImg('../../assets/images/home/home.png');
  background-size: 100% auto;
}
.match {
  @include getBgImg('../../assets/images/match/match.png');
  background-size: 100% auto;
}
.follow {
  @include getBgImg('../../assets/images/follow/follow.png');
  background-size: 100% auto;
}
.community {
  @include getBgImg('../../assets/images/community/community.png');
  background-size: 100% auto;
}
// .store {
//   @include getBgImg("../../assets/images/store/store.png");
//   background-size: 100% auto;
// }
.guess {
  @include getBgImg('../../assets/images/guess/guess.png');
  background-size: 100% auto;
}
.expert {
  @include getBgImg('../../assets/images/expert/expert.png');
  background-size: 100% auto;
}
.follow_plan {
  @include getBgImg('../../assets/images/followplan/follow_plan.png');
  background-size: 100% auto;
}
.user_center {
  @include getBgImg('../../assets/images/user_center/user_center.png');
  background-size: 100% auto;
}
</style>
