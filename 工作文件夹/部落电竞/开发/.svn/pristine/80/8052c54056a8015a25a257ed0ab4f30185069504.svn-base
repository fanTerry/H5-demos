<template>
  <div class="Page guessRecord">
    <header class="mod_header">
      <navBar :pageTitle="'预测记录'"></navBar>
      <ul>
        <li :class="{active:tabFlag == index}" @click="queryRecord(index)" v-for="(item,index) in tabList" :key="index">
          {{item}}</li>
      </ul>
    </header>
    <div class="main" id="mainId">
      <mescroll  ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit">
        <ul class="record_list">
          <li class="record_item" v-for="(item,index) in recordList" :key="index" @click="show(index)">
            <div class="title">
              <span>
                <!-- 预测内容 主队、客队-->
                <span class="team">
                  <img :src="item.videoGameId|gameLogo" alt />
                  <span v-if="item.showMatch">{{item.homeTeamName}} VS {{item.awayTeamName}}</span>
                  <span v-else>{{item.followUserNickName}}的预测跟单</span>
                </span>
                <!--局数、玩法 -->
                <p class="bet_title" v-if="item.showMatch">{{item.matchNo| gameFightNum(true)}} {{item.playName}}</p>
                <p class="bet_title" v-else># 跟单内容待开奖后公开 #</p>
                <i class="show_icon" :class="{active:showDetails == index}"></i>
              </span>
            </div>
            <div class="bet_info">
              <!-- 预测时间 预测金额 -->
              <div>{{item.createTime | getQuizRecordTime}} 投{{item.cost}}星星</div>
              <span class="bingo_wait" v-if="item.winStatus==2 && item.quizOrderStatus!=6 ">
                <i v-if="item.planType==3">+{{(sub(item.prize,add(item.winIncomeUser,item.winIncomeSys)))|formatAwardTtotal}}星星</i>
                <i v-else>+{{item.prize.toFixed(2)}}星星</i>
                派奖中
              </span>
              <span class="bingo" v-else-if="item.winStatus==2 && item.quizOrderStatus==6 ">
                <i v-if="item.planType==3">+{{(sub(item.prize,add(item.winIncomeUser,item.winIncomeSys)))|formatAwardTtotal}}星星</i>
                <i v-else>+{{item.prize.toFixed(2)}}星星</i>
                已派奖
              </span>
              <span class="waiting" v-else-if="item.winStatus==0 && item.status==4">等待开奖</span>
              <span class="failed" v-else-if="item.winStatus==0 && item.status!=4">{{item.status |betPlan}}</span>
              <span class="failed" v-else>{{item.winStatus |winStatus}}</span>
            </div>
            <div class="details" v-if="showDetails === index">
              <p class="match_info" v-if="item.showMatch">
                {{item.homeTeamName}} VS
                {{item.awayTeamName}} （{{item.startTime | getQuizRecordTime}}）
              </p>
              <p v-if="item.showMatch">下&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;单：{{item.option}} {{item.betSp}}</p>
              <p v-if="item.answer">
                赛&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;果：
                <span>{{item.answer}}</span>
              </p>
              <p v-if="item.winAward">
                中奖金额：
                <span>{{item.winAward.toFixed(2)}}</span>
              </p>
              <p v-if=" item.winIncomeUser && item.feeRate && item.commissionRate">
                佣金&nbsp;&nbsp;&nbsp;&nbsp;率：
                <span>{{add(item.feeRate , item.commissionRate)}}%</span>
              </p>
              <p v-if="item.winIncomeUser">
                支付佣金：
                <span>{{(add(item.winIncomeUser,item.winIncomeSys)).toFixed(2)}}</span>
              </p>
              <p>
                方案编号：
                <span>{{item.planNo}}</span>
              </p>
              <p>
                方案状态：
                <span v-if="item.rejectDesc">{{item.rejectDesc}}</span>
                <span v-else>{{item.status |betPlan}}</span>
              </p>
            </div>
            <div class="recommend_btn" v-if="item.recommendType" @click="doRecommend($event,item.orderId)">
              <a>推荐此单，赚{{recommendRule.commissionRate}}收益</a>
            </div>
          </li>
        </ul>
        <div class="ui_pop" v-if="recomObj.recSuccess" @click="recomObj.recSuccess=false">
          <div class="pop_recommend">
            <h3 :class="{'failed':!recomObj.recSuccess}">
              <template>{{recomObj.betTips}}</template>
              <span v-if="null!=recomObj.remainderTimes">今日剩余发单次数：{{recomObj.remainderTimes}}次</span>
            </h3>
            <div class="confirm_btn" v-if="recomObj.recSuccess">
              <a @click="toRecommendDetail">查看详情</a>
            </div>
          </div>
        </div>        
      </mescroll>
      <!-- 没有数据时展示 -->
      <noData v-if="noData" :imgUrl="imgUrl" :text="'暂无预测记录'"></noData>      
    </div>
  </div>
</template>

<script>
import navBar from '../../components/header/nav_bar/index.vue';
import noData from '../../components/no_data/index.vue';
import mescroll from '../../components/common/mescroll.vue';
import { accAdd, accSub } from '../../libs/math';
import { getQuizRecordTime } from '../../libs/utils';
export default {
  components: { navBar, noData, mescroll },
  props: [],
  filters: {
    getQuizRecordTime(time) {
      return getQuizRecordTime(time, false);
    }
  },
  data() {
    return {
      noData: false,
      tabList: ['待开奖', '全部', '已中奖'],
      imgUrl: require('../../assets/images/guess/no_data_icon.png'),
      tabFlag: 1,
      showDetails: null, //下标
      type: 1,
      recordList: [], //用户预测记录
      currPageSize: 10,
      recommendRule: {},
      recordQueryParam: {
        pageNo: 1,
        pageSize: 10,
        winStatus: null //初始加载全部的预测记录
      },
      hasNext: false, //是否有下一页     
      //推荐
      recomObj: {
        recSuccess: false, //是否推荐成功
        planRecommendId: null, //推荐单id
        planId: null, //方案id
        userId: null,
        remainderTimes: null, //剩余推单次数
        betTips: ''
      }
    };
  },

  mounted() {
    let test = '{"code":4007, "msg": "方案已取消,用户限额原因"}';
    console.log(JSON.parse(test)['msg']);
    //屏蔽app处理
    if (this.$route.query.clientType == 3) {
      // this.$router.push({
      //   path: "/home",
      //   query: {}
      // });
    }
    // this.quizzesPopRecord(this.recordQueryParam);
    this.getRecommendRule();
    this.$wxApi.wxRegister({
      title: '王者KPL预测',
      desc: '免费领预测星星，预测赢百万好礼~',
      imgUrl: 'http://rs.esportzoo.com/svn/esport-res/ddquiz/images/logo/dd_logo.png'
    });
  },
  methods: {
    goUrlPage(url) {
      this.$router.push({
        path: url,
        query: {}
      });
    },
    add(s1, s2) {
      return accAdd(s1, s2);
    },
    sub(a1, a2) {
      return accSub(a1, a2);
    },
    show(index) {
      if (this.showDetails == index) {
        this.showDetails = null;
      } else {
        this.showDetails = index;
      }
    },

    downCallback() {
      this.recordList = [];
      this.recordQueryParam.pageNo = 1;
      this.currPageSize = 10;
      this.quizzesPopRecord(this.recordQueryParam).then(() => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNext);
        });
      });
    },

    upCallback() {
      this.loadMore();
    },
    loadMore() {
      this.recordQueryParam.pageNo += 1;
      let param = {};
      param.pageNo = this.recordQueryParam.pageNo;
      param.pageSize = this.recordQueryParam.pageSize;
      param.winStatus = this.recordQueryParam.winStatus;
      this.quizzesPopRecord(this.recordQueryParam).then(() => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNext);
        });
      });
    },

    mescrollInit(mescroll) {
      this.mescroll = mescroll;
    },   
    queryRecord(index) {
      this.tabFlag = index;
      this.recordList = [];
      if (index == 1) {
        index = null;
      }
      this.currPageSize = 10;
      this.recordQueryParam.pageNo = 1;
      this.recordQueryParam.pageSize = 10;
      this.recordQueryParam.winStatus = index;
      this.quizzesPopRecord(this.recordQueryParam);
    },

    //查询预测记录
    quizzesPopRecord(param) {
      return this.$post('/api/quiz/record/recordPage', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            this.hasNext = dataResponse.data.hasNext;
            if (dataResponse.data.dataList.length > 0) {
              this.currPageSize = dataResponse.data.dataList.length;
              this.recordList = this.recordList.concat(dataResponse.data.dataList);
            }
            if (this.recordList.length == 0) {
              console.log("无数据");
              this.noData = true;
            } else {
              this.noData = false;
            }
            return this.recordList;
          }
        })
        .catch(error => {
          console.log(error, '查询预测记录失败');
        });
    },
    doRecommend(e, orderId) {
      e.stopPropagation();
      this.$post('/api/followplan/submitPlanRecommend', { orderId: orderId })
        .then(res => {
          console.log(orderId, res, '推单接口');
          if (res.code == 200) {
            this.recomObj.recSuccess = true;
            this.recomObj.planRecommendId = res.data.planRecommendId;
            this.recomObj.planId = res.data.planId;
            this.recomObj.userId = res.data.userId;
            this.recomObj.remainderTimes = res.data.remainderTimes;
            this.recomObj.betTips = '方案推荐成功';
          } else {
            this.$toast(res.message, 4);
          }
        })
        .catch(error => {
          console.error(error, '推单接口,请求接口异常');
        });
    },
    toRecommendDetail() {
      this.$router.push({
        path: '/followPlan/recommendPlanDetail',
        query: {
          id: this.recomObj.planRecommendId,
          planId: this.recomObj.planId,
          uid: this.recomObj.userId
        }
      });
    },
    //取系统配置的跟单参数
    getRecommendRule() {
      return this.$post('/api/planRecommend/getRecommendRule').then(rsp => {
        const dataResponse = rsp;
        if (dataResponse.code == 200) {
          this.recommendRule = rsp.data;
        }
      });
    }
  }
};
</script>
<style lang='scss'>
.guessRecord {
  .nav_bar {
    color: #fff !important;
    .back {
      &::before,
      &::after {
        background-color: #fff !important;
      }
    }
  }
}
</style>

<style lang='scss' scoped>
@import '../../assets/common/_base';
@import '../../assets/common/_mixin';
.mod_header {
  background-color: $color_item !important;
  ul {
    @extend .flex_v_justify;
    padding: 0 22.6667vw;
    overflow-x: auto;
    white-space: nowrap;
    -webkit-overflow-scrolling: touch;
    background-color: $color_main;
  }
  li {
    padding: 4.5333vw 0;
    font-size: 3.7333vw;
    font-weight: bold;
    color: rgba(255, 255, 255, 0.5);
    border-radius: 3px;
    &.active {
      position: relative;
      color: #fff;
      &::after {
        content: '';
        @extend .g_c_mid;
        bottom: 1.0667vw;
        width: 4vw;
        height: 1.0667vw;
        border-radius: 0.8vw;
        background-color: #fff;
      }
    }
  }
}

.record_list {
  padding: 0 4.2667vw;
}

.record_item {
  position: relative;
  margin-bottom: 2.67vw;
  background-color: $color_item;
  border-radius: $border_radius;
  overflow: hidden;
}
.title {
  padding: 3.2vw 10.67vw 0 3.2vw;
  line-height: 4.5333vw;
}
.team {
  @extend .flex_hc;
  font-size: 3.73vw;
  line-height: 4.2667vw;
  color: #fff;
  img {
    width: 3.7333vw;
    height: 3.7333vw;
    margin-right: 2.1333vw;
    border-radius: 50%;
    background-color: #32191e;
  }
}
.bet_title {
  padding-top: 1.0667vw;
  font-size: 3.7333vw;
  line-height: 4.53vw;
  font-weight: bold;
  color: $color_yellow;
}
.show_icon {
  position: absolute;
  right: 3.6vw;
  top: 4.4vw;
  width: 3.2vw;
  height: 3.2vw;
  transform: rotate(180deg);
  -webkit-transform: rotate(180deg);
  @include getArrow(2.1333vw, #999, down);
  &.active {
    transform: rotate(0deg);
    -webkit-transform: rotate(0deg);
  }
}

.bet_info {
  @extend .flex_v_justify;
  height: 3.7334vw;
  padding: 3.2vw 0 3.2vw 3.2vw;
  color: rgba(255, 255, 255, 0.5);
  box-sizing: content-box;
  > div {
    font-size: 2.9333vw;
    line-height: 3.4667vw;
  }
}

.recommend_btn {
  padding: 2vw 3.2vw;
  border-top: 1px solid #504d70;
  a {
    display: block;
    @include getBtn(auto, 10.6667vw, 4vw, #fff, $color_btn, $border_radius);
  }
}

.waiting,
.failed,
.bingo,
.bingo_wait {
  padding: 0.8vw 2.1333vw;
  font-size: 2.9333vw;
  border-radius: 0.5333vw 0 0 0.5333vw;
}

.waiting {
  color: #fff;
  background-color: $color_btn;
}
.failed {
  color: #fff;
  background-color: #aaa;
}
.bingo {
  color: #fff;
  background-color: #c47b87;
  i {
    padding-right: 0.8vw;
  }
}

.bingo_wait {
  color: #fff;
  background-color: #c85958;
}

.details {
  position: relative;
  padding: 3.2vw;
  background-color: #2a2939;
  &::after {
    content: '';
    position: absolute;
    top: -3.7333vw;
    left: 8.4vw;
    @include getTriangle(2vw, #2a2939, up);
  }
  p {
    padding-top: 2.4vw;
    font-size: 2.9333vw;
    color: #fff;
  }
  .match_info {
    padding-top: 0;
    font-size: 3.7333vw;
    line-height: 4.2667vw;
  }
}

.mod_footer {
  background-color: #fff;
}

.return_home {
  display: block;
  width: 35.47vw;
  margin: 2.13vw auto;
  border-radius: 9.07vw;
  line-height: 9.07vw;
  font-size: 4vw;
  color: #fff;
  text-align: center;
  background-color: #d53941;
  border-radius: 0.8vw;
}

.no_data_icon {
  display: inline-block;
  width: 9.07vw;
  height: 9.07vw;
  @include getBgImg('../../assets/images/guess/no_data_icon.png');
  background-size: 100% 100%;
}

.pop_recommend {
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  background-color: #fff;
  h3 {
    @extend .flex_v_justify;
    height: 9.6vw;
    padding: 0 4.2667vw;
    font-size: 4vw;
    color: #fff;
    background-color: $color_btn;
    span {
      font-weight: normal;
      color: rgba(255, 255, 255, 0.5);
    }
    &.failed {
      background-color: #999;
    }
  }
}

.confirm_btn {
  @extend .flex_v_h;
  padding: 3.2vw;
  font-size: 3.73vw;
  font-weight: bold;
  border-radius: 0.8vw;
  color: #fff;
  text-align: center;
  a {
    flex: 1;
    -webkit-flex: 1;
    @include getBtn(44.6667vw, 10.6667vw, 4vw, #fff, #999, 0.8vw);
    &:last-child {
      background-color: #c05554;
    }
  }
}
</style>
