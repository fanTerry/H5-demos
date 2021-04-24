<template>
  <div class='Page'>
    <header class='mod_header'>

    </header>
    <mescroll ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit" :isShowEmpty="false">

      <div class='main' id="mainId">
        <!-- banner图 -->

        <section class="banner" v-if="adList.length >0">
          <banner ref="banner" :adList="adList">
          </banner>
        </section>
        <!-- tab选项卡 -->
        <figure class="mod_tab">
          <div class="active">大神跟单</div>
          <div @click="toFollowPlan()">专家方案</div>
        </figure>
        <!-- 大神跟单和专家方案 -->
        <section class="mod_expert">
          <div class="title">
            <span>大神跟单</span>
            <span class="percent">(十单胜率)</span>
            <a class="my_god">我的大神</a>
          </div>
          <div class="god_list">
            <ul>
              <li class="item" @click="goToGodPage(item.userId)" v-for="(item,index) in planRecommendInfo.tenWinList" :key="index">
                <img :src="item.icon" alt="">
                <p class="name">{{item.nickName}}</p>
                <p class="percent_num">{{item.tenWinRate}}</p>
              </li>
            </ul>
          </div>
        </section>
        <!-- 排行榜tab -->
        <section class="mod_rank">
          <ul class="tab">
            <li @click="changeBnad(item.type)" :class="{active:planRecommendInfo.currBandType==item.type}" v-for='(item,index) in planRecommendInfo.bandTypeArr' :key="index">{{item.name}}</li>
          </ul>
          <!-- 大神跟单 -->
          <ul class="orders">
            <follow-order :planRecommend="item" v-for="(item,index) in this.planRecommendInfo.dataList" :key="index"></follow-order>
          </ul>
          <!-- 专家方案 -->
        </section>
        <no-data v-if="noData" :text="'暂无数据'" :imgUrl='require("../../../assets/images/guess/no_data_icon.png")'>
        </no-data>
      </div>
    </mescroll>
    <footer class='mod_footer'>
      <tabbar></tabbar>
    </footer>
  </div>
</template>

<script>
import tabbar from '../../../components/tabbar/index.vue';
import banner from '../../../components/header/banner/swiper.vue';
import followOrder from './components/followOrder.vue';
import mescroll from '../../../components/common/mescroll.vue';
import noData from '../../../components/no_data/index';

export default {
  components: { tabbar, banner, followOrder, mescroll, noData },
  props: [],
  data() {
    return {
      adList: [],
      noData: false,
      planRecommendInfo: {
        tenWinList: [],
        loadMore: true,
        currPageSize: 10,
        dataList: [],
        requestParam: {
          pageNo: 1,
          pageSize: 10,
          followQueryType: 4 //4金额、5人气、6、命中 2、我关注
        },
        currBandType: 4,
        bandTypeArr: [
          {
            type: 4,
            name: '金额'
          },
          {
            type: 5,
            name: '人气'
          },
          {
            type: 6,
            name: '命中'
          },
          {
            type: 2,
            name: '我关注的'
          }
        ]
      }
    };
  },
  mounted() {
    this.setHeader();
    this.getTenWinBand();
  },
  methods: {
    /**获取菜单栏 */
    setHeader() {
      let param = {};
      param.contentType = this.contentType;
      // param.clientType = this.clientType;
      console.log('切换tab', param);
      this.$post('/api/indexData', param)
        .then(rsp => {
          console.log(rsp);
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            console.log('setHeader---请求成功');
            this.adList = dataResponse.data.adList;
            this.centerAdList = dataResponse.data.centerAdList;
            // this.slideText =  this.adList[0].title;
            this.channelList = dataResponse.data.channelList;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    goToGodPage(userId) {
      console.log('跳转大神主页', userId);
    },
    //近10场胜率榜单
    getTenWinBand() {
      return this.$post('/api/planRecommend/getTenWinBand')
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            let dataList = dataResponse.data;
            if (dataList.length > 0) {
              this.planRecommendInfo.tenWinList = dataList;
            }
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
      // this.mescroll.setBounce(true)//允许iOS回弹,相当于配置up的isBounce为true
    },
    downCallback() {
      console.log('下拉刷新');
      // this.refresh(true);
      // this.mescroll.setPageNum(2);
      this.planRecommendInfo.dataList = [];
      this.planRecommendInfo.requestParam.pageNo = 1;
      this.pageData().then(data => {
        this.mescroll.endSuccess(this.planRecommendInfo.currPageSize, this.planRecommendInfo.loadMore);
      });
    },
    upCallback() {
      console.log('上拉加载更多');
      this.planRecommendInfo.requestParam.pageNo += 1;
      this.pageData().then(data => {
        this.mescroll.endSuccess(this.planRecommendInfo.currPageSize, this.planRecommendInfo.loadMore);
      });
    },
    //分页数据查询
    pageData() {
      let param = {};
      param = this.planRecommendInfo.requestParam;
      return this.$post('/api/planRecommend/pageData', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == 200) {
            let dataList = dataResponse.data.dataList;
            if (dataList) {
              this.planRecommendInfo.currPageSize = dataList.length;
            }
            this.planRecommendInfo.loadMore = dataResponse.data.hasNext;
            this.planRecommendInfo.dataList = this.planRecommendInfo.dataList.concat(dataList);
            this.planRecommendInfo.requestParam.pageNo = dataResponse.data.pageNo;
          }
          if (this.planRecommendInfo.dataList.length > 0) {
            this.noData = false;
          } else {
            this.noData = true;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    changeBnad(bandType) {
      if (bandType == this.planRecommendInfo.currBandType) {
        return;
      }
      this.planRecommendInfo.currBandType = bandType;
      this.planRecommendInfo.requestParam.pageNo = 1;
      this.pageData();
    },

    toFollowPlan() {}
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';

.main {
  background: linear-gradient(to bottom, #fff 0%, #fff 42.1333vw, $color_main 42.1333vw, $color_main 100%);
  background: -webkit-linear-gradient(top, #fff 0%, #fff 42.1333vw, $color_main 42.1333vw, $color_main 100%);
}

.banner {
  margin: 3.2vw 4.2667vw 0;
  padding: 1.0667vw;
  border-radius: 1.3333vw;
  background-color: $color_main;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  > div {
    height: 47.2vw;
  }
}

.mod_tab {
  @extend .flex_v_h;
  div {
    padding: 4.2667vw 8vw;
    font-size: 3.7333vw;
    line-height: 4.2667vw;
    color: rgba(255, 255, 255, 0.5);
    font-weight: bold;
    &.active {
      position: relative;
      color: #fff;
      &::after {
        content: '';
        @extend .g_c_mid;
        bottom: 2.1333vw;
        width: 4vw;
        height: 1.0667vw;
        background-color: #fff;
        border-radius: 0.6667vw;
      }
    }
  }
}

.mod_expert {
  margin: 2.9333vw 4.2667vw 0;
  .title {
    position: relative;
    font-size: 4.2667vw;
    line-height: 4.8vw;
    color: #fff;
    font-weight: bold;
  }
  .percent {
    padding-left: 1.0667vw;
    font-size: 2.6667vw;
    color: rgba(255, 255, 255, 0.5);
  }
  .my_god {
    @extend .g_v_mid;
    right: 0;
    padding: 1.6vw;
    font-size: 3.2vw;
    border-radius: 0.8vw;
    color: $color_main;
    background-color: #fff;
  }
  .god_list {
    height: 28vw;
    overflow: hidden;
    ul {
      @extend .flex_hc;
      flex-wrap: nowrap;
      -webkit-flex-wrap: nowrap;
      height: calc(100% + 6px);
      overflow: auto;
      -webkit-overflow-scrolling: touch;
    }
    .item {
      padding: 4.2667vw 5.0667vw 4.2667vw 3.2vw;
      text-align: center;
      img {
        width: 10.4vw;
        height: 10.4vw;
        border: 0.2667vw solid #ff9da3;
        border-radius: 50%;
      }
      p {
        font-size: 2.6667vw;
        line-height: 3.2vw;
        white-space: nowrap;
      }
    }
    .name {
      margin-top: 2.1333vw;
      color: #fff;
    }
    .percent_num {
      margin-top: 0.5334vw;
      font-weight: bold;
      color: #feff00;
    }
  }
}

.mod_rank {
  .tab {
    @extend .flex_v_justify;
    margin: 0 4.2667vw;
    li {
      padding: 1.3334vw 3.4667vw;
      color: rgba(255, 255, 255, 0.5);
      &.active {
        border-radius: 1.0667vw;
        color: $color_main !important;
        background-color: #fff;
      }
    }
  }
  .orders {
    margin: 0 4.2667vw;
  }
}
</style>
