<template>
  <div class="Page userCenter_Page">
    <div class="main">
      <header class="mod_header">
        <span class="msg_icon" @click="goUrlPage('/guess/message')" :class="{'active':true}"></span>
        <span class="set_icon" @click="goUrlPage('/userInfo')"></span>
      </header>
      <section class="user_top">
        <div class="user_info">
          <div class="user_img" @click="goUrlPage('/userInfo')">
            <img :src="userInfo.icon|getDefaultImg(globalConst.userDefaultIcon)" alt="">
          </div>
          <div>
            <div class="name">{{userInfo.nickName?userInfo.nickName:'枫叶电竞欢迎您'}}</div>
            <p class="tips">简介:{{userInfo.intro?userInfo.intro:'电竞爱好者集聚地'}}</p>
          </div>
          <!-- 会员标志 -->
          <!-- <div class="vip_player" v-if="userInfo.nickName">
            <span></span>会员
          </div> -->
          <!-- 登录注册按钮 -->
          <template v-if="!userInfo">
            <router-link to="/login">
              <div class="login_btn">注册/登录</div>
            </router-link>
          </template>
        </div>
      </section>
      <!-- 枫叶余额 -->
      <section class="banlance">
        <p>星星余额</p>
        <div>
          <span class="num">{{getVuexUserInfo.recScore}}</span>
          <a @click="popUps.showRechargePop = true">星星获取</a>
        </div>
      </section>
      <!-- 我的服务 -->
      <section class="my_service">
        <h3 class="title">我的服务</h3>
        <div class="swiper-container swiper_service">
          <ul class="swiper-wrapper">
            <li class="list swiper-slide">
              <div class="item" @click="goUrlPage('/guess/record')">
                <img src="../../assets/images/user_center/guess_record_icon.png" alt="">
                <p>预测记录</p>
              </div>
              <div class="item" @click="goUrlPage('/wallet')">
                <img src="../../assets/images/user_center/wallet_record_icon.png" alt="">
                <p>流水记录</p>
              </div>
              <div class="item">
                <img src="../../assets/images/user_center/my_plan_icon.png" alt="">
                <p>我的方案</p>
              </div>
              <div class="item">
                <img src="../../assets/images/user_center/follow_record_icon.png" alt="">
                <p>我的跟单</p>
              </div>
              <div class="item" @click="goUrlPage('/userCenter/myMessage')">
                <img src="../../assets/images/user_center/my_message_icon.png" alt="">
                <p>我的消息</p>
                <span class="message_num" v-if="userInfo.messageNum > 0">{{userInfo.messageNum>99?'99+':userInfo.messageNum}}</span>
              </div>
              <div class="item" @click="goUrlPage('/userCenter/myFollow')">
                <img src="../../assets/images/user_center/my_follow_icon.png" alt="">
                <p>我的关注</p>
              </div>
              <div class="item" @click="goUrlPage('/userCenter/myFans')">
                <img src="../../assets/images/user_center/my_fans_icon.png" alt="">
                <p>我的粉丝</p>
              </div>
              <div class="item" @click="goUrlMyPage('/userCenter/myRelease')">
                <img src="../../assets/images/user_center/my_community_icon.png" alt="">
                <p>我的社区</p>
              </div>
            </li>
            <li class="list swiper-slide">
              <div class="item" v-if="userInfo.saleAuth" @click="goUrlPage('/userCenter/salesManage')">
                <img src="../../assets/images/user_center/sales_icon.png" alt="">
                <p>销售管理</p>
              </div>
              <div class="item" @click="goUrlPage('/helpcenter')">
                <img src="../../assets/images/user_center/help_icon.png" alt="">
                <p>帮助中心</p>
              </div>
            </li>
          </ul>
          <div class="swiper-pagination"></div>
        </div>
      </section>
      <section class="exchange_store">
        <h3 class="title">兑换商城</h3>
        <div class="exchange">
          <p>可兑换星星：<span>{{getVuexUserInfo.exchangeStarNum}}</span></p>
          <p>
            椰子分:<span>{{getVuexUserInfo.yeYunPoints}}</span>
            <a class="btn" @click="popUps.showExchangeType = true">兑换</a></p>
        </div>
        <section class="store">
          <div class="store_item" @click="goToYeYunPage()">
            <img src="http://rs.esportzoo.com/upload/admin/picAd/出口引导-2_1587269184424.png" alt="">
            <span class="name">椰子商城</span>
            <!-- 跑马灯 -->
            <section class="mod_award" v-show="prizeList != ''">
              <!-- <i class="horn_icon"></i> -->
              <div class="winner_scroll swiper-container">
                <ul class="swiper-wrapper">
                  <li class="swiper-slide" v-for="(item,index) in prizeList" :key="index">
                    <template v-if='item.type==1'>
                      <span>恭喜</span>
                      <span class="winner_name">{{item.userName}}</span>
                      <span>测预正确获得</span>
                      <span class="award_value">{{item.winPrizeAmount}}</span>
                      <span>星星!</span>
                    </template>
                    <template v-if='item.type==2'>
                      <span>恭喜</span>
                      <span class="winner_name">{{item.userName}}</span>
                      <span>成功兑换</span>
                      <span class="award_value">{{item.goodsName}}</span>
                    </template>
                    <template v-if='item.type==3'>
                      <span class="notice" @click="toLocation(item.noticeUrl)">{{item.noticeDesc}}</span>
                    </template>
                  </li>
                </ul>
              </div>
              <!-- <i class="horn_close" @click="closeHorn()"></i> -->
            </section>
          </div>
        </section>
      </section>

      <!-- <section class="business_column">
        <ul>
          <li @click="goUrlPage('/store')">
            商城<span class="link_icon"></span>
          </li>
          <li @click="goUrlPage('/myExchange')">
            我的订单<span class="link_icon"></span>
          </li>
          <li @click="goUrlPage('/uc/bindPhone')" v-if='!bindPhoneFlag'>
            绑定手机号<span class="link_icon"></span>
          </li>
          <li @click="goUrlPage('/userCenter/userPublishArticle')">
            我的发布<span class="link_icon"></span>
          </li>
          <li @click="goUrlPage('/userArticle')">
            付费文章<span class="link_icon"></span>
          </li>
          <li @click="goExpertApplyPage('/expertApply')" v-if="userInfo.hasExpertApply">
            专家申请<span class="link_icon"></span>
          </li>
        </ul>
      </section> -->

      <!-- <section class="AQ">
        备案/许可证编号：琼ICP备19001792号-1<br><br>琼公网安备 46902302000297号
      </section> -->

      <!-- <section class="module_section">
        <h3>账号安全</h3>
        <div class="bind_phoneNum">
          <span class="firewall_icon"></span>
          <div>
            <p class="bind">你绑定得手机是 151*****456 吗？</p>
            <p class="phone">手机换号可能导致无法正常使用程序</p>
            <p class="confirm_tips">请确认是否已经换号</p>
          </div>
        </div>
        <div class="confirm_btn">
          <a>立即确认</a>
        </div>
      </section> -->

      <!-- <section class="module_section">
        <h3>签到领红包</h3>
        <p class="vip_tips">
          您有一个
          <span>会员</span>福利带领
        </p>
        <div class="gift">
          <div>
            <img
              src="../../assets/images/user_center/gold_coin.png"
              alt=""
            > 1000预测币
          </div>
          <div>
            <img
              src="../../assets/images/user_center/gift.png"
              alt=""
            > 小积分赚豪礼
          </div>
        </div>
        <div class="confirm_btn">
          <a>立即打开</a>
        </div>
      </section> -->

    </div>

    <exchange-pop v-if="popUps.showExchangeType" @closeExchangePop='popUps.showExchangeType = false' :exchangeYeYunScore='exchangeScore' :exchangeRatio='exchangeRatio'></exchange-pop>
    <recharge-pop v-if="popUps.showRechargePop" @closeRechargePop="popUps.showRechargePop = false" @getUserWallet="getUserWallet()">
    </recharge-pop>
    <footer class="mod_footer">
      <tabbar></tabbar>
    </footer>
  </div>
</template>

<script>
import tabbar from '../../components/tabbar/index.vue';
import navBar from '../../components/header/nav_bar/index';
import sessionStorage from '../../libs/storages/sessionStorage';
import { mapGetters, mapActions, mapMutations } from 'vuex';
import Swiper from 'swiper';
import exchangePop from '../guess/pointsExchange.vue';
import rechargePop from '../guess/recharge/recharge.vue';

export default {
  components: {
    tabbar,
    navBar,
    exchangePop,
    rechargePop
  },
  data() {
    return {
      serviceSwiper: Object,
      userInfo: Object,
      clientType: null,
      // bindPhoneFlag: false //默认未绑定
      popUps: {
        showExchangeType: false,
        showRechargePop: false
      },
      prizeList: [], //跑马灯中奖信息
      exchangeScore: 0, //椰云分每日额度
      exchangeRatio: null //椰子分兑换比例
    };
  },
  computed: {
    ...mapGetters({
      getVuexUserInfo: 'getUserInfo'
    })
  },
  mounted() {
    this.getUserInfo();
    this.dataRefresh(); //刷新用户钱包
    // this.checkBindPhone();
    this.clientType = this.$route.query.clientType;
    this.$nextTick(() => {
      this.swiperService();
    });
    this.prizeCarousel().then(() => {
      this.$nextTick(() => {
        if (this.prizeList != '') {
          this.initAwardSwiper();
        }
      });
    });
  },
  methods: {
    ...mapActions(['setUserInfo']),
    /**测试用 */
    ...mapMutations(['USER_CENTER_INFO']),
    getUserInfo() {
      let param = {};
      this.$post('/api/usercenter/ucIndexdata', param)
        .then(rsp => {
          const dataResponse = rsp;
          this.isLoading = false;
          if (dataResponse.code == '200') {
            console.log('个人中心首页--setHeader---请求成功');
            this.userInfo = dataResponse.data;
            this.$wxApi.wxRegister({
              title: '枫叶电竞-（' + this.userInfo.nickName + '）个人中心',
              desc: '游戏爱好者聚集地',
              imgUrl: 'http://rs.esportzoo.com/svn/esport-res/mini/images/default/juzi_logo.jpg'
            });
            this.USER_CENTER_INFO(dataResponse.data);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    goUrlPage(url) {
      this.$router.push({
        path: url,
        query: {
          id: this.userInfo.userId
        }
      });
    },
    goExpertApplyPage(url) {
      this.$router.push({
        path: url,
        query: {}
      });
    },
    goUrlMyPage(url) {
      sessionStorage.set('tabIndex', 0);
      this.$router.push({
        path: url,
        query: {}
      });
    },
    // swiper组件初始化
    swiperService() {
      this.serviceSwiper = new Swiper('.swiper_service', {
        pagination: '.swiper-pagination',
        loop: true,
        speed: 1000,
        autoplayDisableOnInteraction: false,
        observer: true,
        observeParents: true
      });
    },

    // 跳转到椰云积分商城
    goToYeYunPage() {
      this.$post('/api/yeYunUser/getYeYunIndex')
        .then(rsp => {
          const dataResponse = rsp;
          let yeYunUrl = dataResponse.message;
          console.log(yeYunUrl);
          window.location.href = yeYunUrl;
        })
        .catch(error => {
          console.log(error);
        });
    },
    //查询设置用户钱包
    getUserWallet() {
      return this.$post('/api/starNum/queryStarNum')
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            console.log(dataResponse.data.ableRecScore, '获取用户的星星');
            // this.ableRecScore = dataResponse.data.ableRecScore;
            this.setUserInfo({
              ...this.getVuexUserInfo,
              recScore: dataResponse.data.ableRecScore
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },

    //查询椰云积分
    queryYeYunPoints() {
      return this.$post('/api/yeYunUser/yeYunUserInfo')
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            console.log(dataResponse.data, '获得用户椰云积分');
            this.exchangeScore = dataResponse.data.exchangeScore;
            this.exchangeRatio = dataResponse.data.exchangeRatio;
            this.setUserInfo({
              ...this.getVuexUserInfo,
              yeYunPoints: dataResponse.data.score
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    //查询用户可兑换余额
    getExchangeStarNum() {
      this.$post('/api/starNum/queryExchangeScore')
        .then(rsp => {
          console.log(rsp, 'starNum/queryExchangeScore');
          if (rsp.code == '200') {
            if (rsp.data >= 0) {
              // this.exchangeStarNum = rsp.data;
              this.setUserInfo({
                ...this.getVuexUserInfo,
                exchangeStarNum: rsp.data
              });
            }
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    dataRefresh() {
      this.queryYeYunPoints();
      this.getExchangeStarNum();
      this.getUserWallet();
    },
    // userWalletRefresh() {
    //   this.queryYeYunPoints();
    //   this.getUserWallet();
    // },

    // 跑马灯数据请求
    prizeCarousel() {
      return this.$post('/api/quiz/record/broadcastList')
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            // console.log(dataResponse.data, "顶部获奖跑马灯");
            this.prizeList = dataResponse.data;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    initAwardSwiper() {
      this.swiper = new Swiper('.winner_scroll', {
        loop: true,
        autoplay: 2000,
        autoplayDisableOnInteraction: false,
        direction: 'vertical',
        observer: true,
        observeParents: true
      });
    }

    //判断是否已经绑定手机
    // checkBindPhone() {
    //   console.log("in checkBindPhone");
    //   this.$post("/api/user/checkBindingPhone")
    //     .then(dataResponse => {
    //       console.log(dataResponse, "checkBindPhone");
    //       if (dataResponse.code == 200) {
    //         this.bindPhoneFlag = dataResponse.data;
    //       } else {
    //         this.$toast(dataResponse.message);
    //       }
    //     })
    //     .catch(error => {
    //       console.log(error);
    //     });
    // }
  }
};
</script>

<style lang="scss">
.userCenter_Page {
  .swiper-pagination {
    bottom: 5px;
  }
  .swiper-pagination-bullet {
    background-color: #d73a42;
  }
}
</style>


<style lang='scss' scoped>
@import '../../assets/common/_base.scss';
@import '../../assets/common/_mixin.scss';
@import '../../assets/common/_var.scss';

.mod_header {
  position: relative;
  min-height: 7.7333vw;
  background-color: transparent;
  .msg_icon,
  .set_icon {
    position: absolute;
    bottom: -2.9333vw;
    z-index: 10;
    width: 5.8667vw;
    height: 5.8667vw;
  }
}

.msg_icon {
  right: 15.4667vw;
  @include getBgImg('../../assets/images/user_center/msg_icon.png');
  background-size: contain;
  &.active {
    &::before {
      content: '';
      position: absolute;
      right: 0;
      top: 0;
      width: 2.4vw;
      height: 2.4vw;
      background-color: #feff00;
      border-radius: 50%;
    }
  }
}

.set_icon {
  right: 4.2667vw;
  @include getBgImg('../../assets/images/user_center/set_icon.png');
  background-size: contain;
}

.mod_footer {
  background-color: #fff;
}

.main {
  padding-bottom: 5.3333vw;

  .user_info {
    position: relative;
    @extend .flex_hc;
    padding: 0 4.2667vw;
    .name {
      @include t_nowrap(40vw);
      line-height: 1.2;
      font-size: 4.2667vw;
      padding-bottom: 2.6667vw;
      font-weight: bold;
      color: #fff;
    }
    .tips {
      @include t_nowrap(40vw);
      line-height: 1.2;
      color: rgba(255, 255, 255, 0.5);
    }
  }
  .user_img {
    position: relative;
    flex: none;
    width: 15.7333vw;
    height: 15.7333vw;
    margin-right: 3.7333vw;
    img {
      width: 100%;
      height: 100%;
      border: 2px solid #fff;
      border-radius: 50%;
      object-fit: cover;
    }
  }
}
.vip_player {
  @extend .flex_hc;
  position: absolute;
  right: 5.3333vw;
  top: 2.6667vw;
  font-size: 3.7333vw;
  color: #333;
  span {
    width: 6.4vw;
    height: 6.4vw;
    margin-right: 1.3333vw;
    @include getBgImg('../../assets/images/user_center/super_vip.png');
  }
}
.login_btn {
  @extend .g_v_mid;
  right: 2.6667vw;
  padding: 2.6667vw;
  font-size: 3.7333vw;
  border-radius: 9.0667vw;
  color: $color_main;
  background-color: #fff;
}

.business_column {
  margin-top: 2.1333vw;
  li {
    @extend .flex_v_justify;
    height: 12.8vw;
    padding: 0 4.5333vw;
    font-size: 4vw;
    color: #333;
    span {
      display: block;
      width: 5.3333vw;
      height: 5.3333vw;
      @include getArrow(2.6667vw, #bbb, right);
    }
  }
  .wallet_icon {
    @include getBgImg('../../assets/images/user_center/wallet_icon.png');
  }
  .expert_icon {
    @include getBgImg('../../assets/images/user_center/expert_icon.png');
  }
  .recharge_icon {
    @include getBgImg('../../assets/images/user_center/recharge_icon.png');
  }
  .payment_icon {
    @include getBgImg('../../assets/images/user_center/payment_icon.png');
  }
  .my_order_icon {
    @include getBgImg('../../assets/images/user_center/my_order_icon.png');
  }
  .service_icon {
    @include getBgImg('../../assets/images/user_center/service_icon.png');
  }
}

.banlance {
  margin: 4.2667vw 4.2667vw 0;
  padding: 4vw 4.2667vw;
  background-color: #fff;
  border-radius: 1.3333vw;
  p {
    font-size: 3.2vw;
    color: #333;
  }
  div {
    @extend .flex_v_justify;
    margin-top: 1.3333vw;
  }
  .num {
    font-size: 9.3333vw;
    font-weight: bold;
    color: #d33940;
  }
  a {
    padding: 2.1333vw;
    font-size: 3.2vw;
    border-radius: 0.8vw;
    color: #fff;
    background-color: #d13840;
  }
}

.title {
  padding-top: 5.3333vw;
  font-size: 4.2667vw;
  line-height: 4.8vw;
  color: #fff;
}

.swiper_service {
  margin-top: 3.2vw;
  background-color: #fff;
  border-radius: 1.3333vw;
}

.my_service {
  margin: 0 4.2667vw;
  .list {
    @extend .flex_hc;
    flex-wrap: wrap;
    -webkit-flex-wrap: wrap;
    padding: 2.6667vw 1.3333vw;
    p {
      margin-top: 2.1333vw;
      font-size: 3.2vw;
      line-height: 3.7333vw;
      color: #333;
    }
  }
  .item {
    position: relative;
    width: 25%;
    padding: 1.6vw 0;
    text-align: center;
  }
  img {
    width: 10.1333vw;
    height: 10.1333vw;
  }
  .message_num {
    position: absolute;
    right: 4.6667vw;
    top: 1.3vw;
    @extend .flex_v_h;
    width: 4.2667vw;
    height: 4.2667vw;
    font-size: 2.1333vw;
    color: #feff00;
    background-color: #d63941;
    border: 1px solid #fff;
    border-radius: 50%;
  }
}

.exchange_store {
  margin: 0 4.2667vw;
  .exchange {
    @extend .flex_v_justify;
    margin-top: 2.1333vw;
    padding: 0 1.0667vw;
    height: 7.4667vw;
    background-color: #d0383f;
    @include getRadiusBorder(#ff9da3, all, 6px);
  }
  p {
    @extend .flex_hc;
    font-size: 3.2vw;
    color: #ff9da3;
    span {
      color: #fff;
    }
  }
  .btn {
    margin-left: 3.2vw;
    padding: 1.0667vw;
    font-size: 2.4vw;
    border-radius: 6px;
    color: #d13840;
    background-color: #fff;
  }
  .store {
    margin-top: 2.1333vw;
    padding: 2.1333vw;
    border-radius: 1.3333vw;
    background-color: #fff;
  }
  .store_item {
    position: relative;
    margin-bottom: 2.1333vw;
    &:last-child {
      margin-bottom: 0;
    }
    img {
      width: 100%;
      height: 25.6vw;
      border-radius: 0.8vw;
      object-fit: cover;
    }
    .name {
      position: absolute;
      top: 1.3333vw;
      right: 0;
      @extend .flex_v_h;
      width: 13.3333vw;
      height: 4.5333vw;
      font-size: 2.4vw;
      border-radius: 0.8vw 0 0 0.8vw;
      color: #fff;
      background-color: #d13840;
    }
  }
}

.mod_award {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  @extend .flex_hc;
  border-radius: 0 0 6px 6px;
  background-color: rgba(0, 0, 0, 0.6);
}

.winner_scroll {
  flex: 1;
  -webkit-flex: 1;
  height: 6.4vw;
  overflow: hidden;
  li {
    @extend .flex_v_h;
    padding: 0 2.6667vw 0 2.2667vw;
    color: #fff;
    span {
      font-size: 3.2vw;
      line-height: 1.2;
    }
    .winner_name {
      @include t_nowrap(20vw);
      padding: 0 1.8667vw;
      color: #feff00;
    }
    .award_value {
      @include t_nowrap(30.3333vw);
      padding: 0 1.8667vw;
      color: #feff00;
    }
  }
  .notice {
    @include t_nowrap(100%);
  }
}

.fans_column {
  @extend .flex_v_justify;
  margin: 2.1333vw 3.2vw 0;
  padding: 4vw 6.1333vw;
  color: #666;
  border-radius: 6px;
  box-shadow: 0 0 5px rgba(#000, 0.1);
  > div {
    text-align: center;
  }
  .num {
    display: block;
    margin: 0 auto 2.1333vw;
    font-size: 4.5333vw;
    line-height: 3.4667vw;
    color: #333;
  }
  .thumbs-icon,
  .follow-icon,
  .fans-icon,
  .message-icon {
    width: 5.3333vw;
    height: 5.3333vw;
    margin-right: 1px;
  }
  .thumbs-icon {
    @include getBgImg('../../assets/images/user_center/thumbs_icon.png');
  }
  .message-icon {
    @include getBgImg('../../assets/images/user_center/message_icon.png');
  }
  .follow-icon {
    @include getBgImg('../../assets/images/user_center/follow_icon.png');
  }
  .fans-icon {
    @include getBgImg('../../assets/images/user_center/fans_icon.png');
  }
}

.module_section {
  padding: 0 2.6667vw;
  h3 {
    padding-top: 2.6667vw;
    color: #a3a3a3;
  }
  .bind_phoneNum {
    @extend .flex_hc;
    padding: 4vw 0;
  }
  .firewall_icon {
    width: 18.6667vw;
    height: 18.6667vw;
    margin-right: 2.6667vw;
    @include getBgImg('../../assets/images/user_center/firewall_icon.png');
  }
  .bind {
    font-size: 4.2667vw;
    color: #3e3e3e;
  }
  .phone {
    padding-top: 2.6667vw;
    font-size: 3.4667vw;
    color: #8a8a8a;
  }
  .confirm_tips {
    padding-top: 2.6667vw;
    color: #a3a3a3;
  }
  .confirm_btn {
    @include getBorder(top, rgba(#000, 0.1));
    padding: 2.6667vw 0;
    a {
      @extend .flex_v_h;
      width: 29.3333vw;
      height: 10.6667vw;
      margin: 0 auto;
      font-size: 3.7333vw;
      border-radius: 20px;
      color: #fff;
      background-color: $color_main;
    }
  }
  .vip_tips {
    padding-top: 4vw;
    font-size: 3.7333vw;
    span {
      padding: 0 0.8vw;
      color: #ec9805;
    }
  }
  .gift {
    @extend .flex_hc;
    justify-content: space-around;
    text-align: center;
    padding: 2.6667vw 0 5.3333vw;
    img {
      display: block;
      width: 18.6667vw;
      height: 18.6667vw;
      margin: 0 auto 1.3333vw;
    }
  }
}

.AQ {
  // text-align: center;
  // margin-top: 30px;
  // font-size: 15px;
  // color: #999;
}
</style>
