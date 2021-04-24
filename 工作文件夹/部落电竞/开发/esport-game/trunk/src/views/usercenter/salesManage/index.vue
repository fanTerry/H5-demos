<template>
  <div class='Page'>
    <header class='mod_header'>
      <nav-bar :pageTitle="'销售管理'"></nav-bar>
      <!-- <div class="users">
        <div>
          <img src="" alt="">
          <span class="user_name">苍穹之鲸鱼</span>
        </div>
        <div>
          <a class="bind_card" :class="{'active':true}">绑定个人结算银行卡</a>
          <i class="service_icon"></i>
        </div>
      </div> -->
    </header>

    <div class='main' id="mainId">
      <mescroll ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit" :isShowEmpty="false">
        <!-- banner图 -->
        <!-- <banner></banner> -->
        <!-- <div class="tab">
          <div>今日数据</div>
          <div class="active">历史数据</div>
          <div>我的结算</div>
        </div> -->
        <div class="title_list">
          <div class="item">
            <p class="txt">注册</p>
            <p class="num">{{sumData.userCount?sumData.userCount:0}}</p>
          </div>
          <div class="item">
            <p class="txt">充值</p>
            <p class="num">{{sumData.scoreSum?sumData.scoreSum:0}} +
              {{sumData.lossProfitAddScoreSum?sumData.lossProfitAddScoreSum:0 }}(RG) </p>
          </div>
          <div class="item">
            <p class="txt">流水</p>
            <p class="num">{{sumData.betAmount?sumData.betAmount:0}}</p>
          </div>
        </div>
        <div class="info">
          <div class="title">
            <span class="phone">手机号码</span>
            <span class="register " :class="{'active':riFlag}" @click="sortByType(1)">注册<i></i></span>
            <span class="recharge " :class="{'active':reFlag}" @click="sortByType(2)">充值<i></i></span>
            <span class="bet " :class="{'active':betFlag}" @click="sortByType(3)">投注<i></i></span>
          </div>
          <ul class="list">
            <li v-for="(item,index) in saleDataList" :key="index">
              <span class="phone">{{item.phone}}</span>
              <span class="register ">{{item.registerTime | dateFmt('yyyy/MM/dd/hh:mm')}}</span>
              <span class="recharge ">{{item.scoreSum | formatMoney(2,"") }}</span>
              <span class="bet ">{{item.betAmount ?item.betAmount:0 | formatMoney(2,"") }}</span>
            </li>
          </ul>
        </div>
        <div class="tips" v-if="!hasNext">
          <!-- <p>更新时间：2020/4/23/11:10</p> -->
          <p>每10分钟更新统计数据，请手动刷新页面获取实时数据</p>
        </div>
      </mescroll>
    </div>
    <footer class='mod_footer'>

    </footer>
  </div>
</template>

<script>
import navBar from '../../../components/header/nav_bar/index.vue';
import mescroll from '../../../components/common/mescroll.vue';
import banner from '../../../components/header/banner/swiper.vue';

export default {
  components: { navBar, mescroll, banner },
  props: [],

  data() {
    return {
      requestParam: {
        pageNo: 1,
        pageSize: 30,
        totalPages: 0,
        sortType: 1,
        sortFlag: true
      },
      currPageSize: 10,
      loadMore: true,
      saleDataList: [],
      reFlag: true,
      riFlag: true,
      betFlag: true,
      hasNext: true,
      sumData: {}
    };
  },
  mounted() {
    this.getPersonSumData();
  },
  methods: {
    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
    },
    downCallback() {
      this.saleDataList = [];
      this.requestParam.pageNo = 1;
      this.getIndexMatchData().then(data => {
        this.mescroll.endSuccess(this.currPageSize, this.loadMore);
      });
    },
    upCallback() {
      this.requestParam.pageNo += 1;
      this.getIndexMatchData().then(data => {
        this.mescroll.endSuccess(this.currPageSize, this.loadMore);
      });
    },
    getIndexMatchData() {
      let param = {};
      param = this.requestParam;
      return this.$post('/agency/quizSale/personData', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            this.saleDataList = this.saleDataList.concat(dataResponse.data.dataList);
            this.hasNext = dataResponse.data.hasNext;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },

    getPersonSumData() {
      let param = {};
      param = this.requestParam;
      return this.$post('/agency/quizSale/personDataSum', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            this.sumData = dataResponse.data;
          } else if (dataResponse.code == '3333') {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },

    getSum() {
      let param = {};
      param = this.requestParam;
      return this.$post('/agency/quizSale/personData', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            this.saleDataList = this.saleDataList.concat(dataResponse.data.dataList);
            this.hasNext = dataResponse.data.hasNext;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },

    sortByType(type) {
      if (this.saleDataList.length == 0) {
        return;
      }
      if (type == 1) {
        this.riFlag = !this.riFlag;
        this.requestParam.sortFlag = this.riFlag;
      } else if (type == 2) {
        this.reFlag = !this.reFlag;
        this.requestParam.sortFlag = this.reFlag;
      } else if (type == 3) {
        this.betFlag = !this.betFlag;
        this.requestParam.sortFlag = this.betFlag;
      }
      this.requestParam.sortType = type;
      this.requestParam.pageNo = 1;
      this.requestParam.pageSize = 30;
      this.saleDataList = [];
      this.getIndexMatchData();
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_mixin';
@import '../../../assets/common/_base';
.Page {
  // background-color: $color_main;
}

.tab {
  @extend .flex_v_h;
  background-color: $color_main;
  div {
    position: relative;
    margin-top: -2.9333vw;
    padding: 9.3333vw 8vw 5.3333vw;
    font-size: 3.7333vw;
    line-height: 4.2667vw;
    color: rgba(255, 255, 255, 0.5);
    font-weight: bold;
    text-align: center;
    &.active {
      color: #fff;
      &::after {
        content: '';
        @extend .g_c_mid;
        bottom: 2.1333vw;
        width: 4vw;
        height: 1.0667vw;
        background-color: #fff;
        border-radius: 5px;
      }
    }
  }
}

.users {
  @extend .flex_v_justify;
  height: 10.6667vw;
  padding: 0 4.2667vw;
  @include getBgLinear(bottom, transparent 0%, rgba(0, 0, 0, 0.05) 100%);
  background-color: $color_main;
  > div {
    @extend .flex_hc;
  }
  img {
    width: 7.4667vw;
    height: 7.4667vw;
    margin-right: 2.1333vw;
    border-radius: 50%;
    border: 1px solid #ff9da3;
  }
  .user_name {
    font-size: 3.7333vw;
    color: #fff;
  }
  .bind_card {
    position: relative;
    padding: 0 2.2667vw;
    @include getBtn(auto, 6.4vw, 3.2vw, #d73a42, #fff, 0.8vw);
    font-weight: bold;
    &.active::before {
      content: '';
      position: absolute;
      right: -1.0667vw;
      top: -1.0667vw;
      width: 3.2vw;
      height: 3.2vw;
      border: 0.5333vw solid #fff;
      border-radius: 50%;
      background-color: $color_main;
    }
  }
  .service_icon {
    width: 8.6667vw;
    height: 6.4vw;
    margin-left: 3.2vw;
    @include getBgImg('../../../assets/images/user_center/sales/service_icon.png');
    background-size: contain;
  }
}

.title_list {
  @extend .flex_v_h;
  height: 22.1333vw;
  .item {
    position: relative;
    flex: 1;
    -webkit-flex: 1;
    text-align: center;
  }
  .txt {
    @extend .flex_v_h;
    font-size: 3.4667vw;
    line-height: 4.2667vw;
    color: #333;
  }
  .num {
    margin-top: 2.6667vw;
    font-size: 5.6vw;
    line-height: 6.1333vw;
    font-weight: bold;
    color: $color_main;
  }
  .register_icon {
    @include getBgImg('../../../assets/images/user_center/sales/register_icon.png');
  }
  .recharge_icon {
    @include getBgImg('../../../assets/images/user_center/sales/recharge_icon.png');
  }
  .value_icon {
    @include getBgImg('../../../assets/images/user_center/sales/value_icon.png');
  }
}

.main {
  @extend .flex;
  flex-direction: column;
  -webkit-flex-direction: column;
  background-color: #fff;
}

.info {
  @extend .flex;
  flex-direction: column;
  -webkit-flex-direction: column;
  flex: 1;
  -webkit-flex: 1;
  background-color: #fff;
  border-radius: 1.3333vw 1.3333vw 0 0;
  overflow: hidden;
  .title {
    @extend .flex_hc;
    font-size: 3.4667vw;
    line-height: 4vw;
    padding: 4.6667vw 4.2667vw;
    font-weight: bold;
    color: #fff;
    background-color: #ff9da3;
  }
  .phone {
    width: 29.3333vw;
    text-align: left;
  }
  .register {
    width: 28vw;
    text-align: center;
  }
  .recharge {
    width: 21.3333vw;
    text-align: right;
  }
  .bet {
    width: 21.3333vw;
    text-align: right;
  }
  .register,
  .recharge,
  .bet {
    i {
      position: relative;
      top: 1.6vw;
      margin-left: 1.3333vw;
      @include getTriangle(1.8667vw, #fff, down);
    }
    &.active {
      i {
        top: -0.5333vw;
        transform: rotate(180deg);
      }
    }
  }
}

.list {
  flex: 1;
  -webkit-flex: 1;
  overflow: auto;
  li {
    @extend .flex_hc;
    height: 8vw;
    padding: 0 4.2667vw;
    font-size: 3.2vw;
    color: #333;
    &:nth-child(2n) {
      background-color: rgba(215, 58, 66, 0.05);
    }
  }
}

.tips {
  flex: none;
  -webkit-flex: none;
  padding: 2.6667vw 0 3.2vw 0;
  text-align: center;
  p {
    line-height: 5.8667vw;
    font-size: 3.2vw;
    color: #999;
  }
}
</style>
