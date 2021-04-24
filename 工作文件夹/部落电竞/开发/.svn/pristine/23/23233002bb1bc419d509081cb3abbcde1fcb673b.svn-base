<template>
  <div class="Page orderListPage">
    <header class="mod_header">
      <navBar :pageTitle="pageName"></navBar>
      <navList :dataList="goodTab" @changeTab="relodDataByTab"></navList>
    </header>
    <div class="main" id='mainId' :class="{flex_v_h:shopOrderList.length==0}">
      <!-- <scroll v-if="shopOrderList.length>0" ref="scroll" :scrollbar="scrollbar" :pullUpLoad="pullUpLoad" :startY="0"
        @pullingUp="onPullingUp"> -->

      <mescroll ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit"
        v-show="shopOrderList.length>0">
        <goodsList :goodList="shopOrderList" :type='3' :pageType="pageType"></goodsList>
      </mescroll>
      <!-- </scroll> -->
      <noData v-show="shopOrderList.length==0"> </noData>
    </div>
  </div>

</template>

<script>
import navBar from "../../../components/header/nav_bar";
import navList from "../../../components/header/nav_list";
import goodsList from "../../../components/store/goodsList";
import noData from "components/no_data/index";
// import Scroll from "components/common/scroll";
import mescroll from "../../../components/common/mescroll.vue";
import { mapMutations } from "vuex";

export default {
  components: {
    navBar,
    navList,
    goodsList,
    noData,
    // Scroll
    mescroll
  },
  data() {
    return {
      shopOrderList: [],
      goodTab: [
        {
          contentType: 0,
          name: "全部购买"
        },
        // {
        //    contentType: 1,
        //   name: "待支付"
        // },
        {
          contentType: 5,
          name: "待发货"
        },
        {
          contentType: 6,
          name: "已完成"
        }
      ],
      pageType: 3,
      pageName: "我的订单",
      tabFlag: true,
      goodParam: {
        pageNo: 1,
        pageSize: 10,
        shopOrderStatus: 0
      },
      currPageSize: 10,
      mescroll: null,
      mescrollConfig: {
        warpId: "mainId", //设置置顶时，必须设置父容器ID
        hasToTop: true //默认不开启回到顶部项
      }
    };
  },
  created() {
    if (
      this.$route.params.shopOrderStatus ||
      this.$route.params.shopOrderStatus == 0
    ) {
      this.goodParam.shopOrderStatus = this.$route.params.shopOrderStatus;
    } else if (this.$store.state.myExchangeTab) {
      this.goodParam.shopOrderStatus = this.$store.state.myExchangeTab;
    } else {
      this.goodParam.shopOrderStatus = 0;
    }
    this.MY_EXCHANGE_TAB(this.goodParam.shopOrderStatus);
    // this.goodTab.forEach(item => {
    //   if(item.contentType==this.goodParam.shopOrderStatus){
    //     this.pageName =item.name
    //   }
    // });

    // this.pageName =

    window.sessionStorage.selectedTab = this.goodParam.shopOrderStatus;
  },
  mounted() {
    // this.getGoodsData();
    // this.$refs.mescroll.config = this.mescrollConfig;
  },
  destroyed() {
    //  this.MY_EXCHANGE_TAB(null)
  },
  methods: {
    ...mapMutations(["MY_EXCHANGE_TAB"]),
    relodDataByTab(tabId) {
      window.sessionStorage.selectedTab = tabId;
      this.MY_EXCHANGE_TAB(tabId);
      this.goodParam.pageNo = 1;
      this.goodParam.pageSize = 10;
      this.goodParam.shopOrderStatus = tabId;
      this.shopOrderList = [];
      this.pageType = tabId;
      let param = this.goodParam;
      param.clientType = this.clientType;
      this.downCallback();
      // this.getGoodsData(param);
    },

    getGoodsData(param) {
      if (!param) {
        param = this.goodParam;
      }
      console.log("切换tab", param);
      return this.$post("/api/shop/shopOrder", param)
        .then(rsp => {
          console.log(rsp);
          const dataResponse = rsp.data;
          if (rsp.code == "200") {
            console.log("getGoodsData---请求成功", dataResponse);
            this.currPageSize = dataResponse.shopOrderList.length;
            this.shopOrderList = this.shopOrderList.concat(
              dataResponse.shopOrderList
            );
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
      // this.mescroll.setBounce(true);
    },
    downCallback() {
      console.log("下拉刷新");
      let param = {};
      this.shopOrderList = [];

      param = this.goodParam;
      this.getGoodsData(param).then(() => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize);
        });
      });
    },
    upCallback() {
      console.log("上拉加载更多6666");
      this.loadMore();
    },

    /** 上拉加载*/
    loadMore() {
      this.goodParam.pageNo += 1;
      let param = {};
      param.pageNo = this.goodParam.pageNo;
      param.pageSize = this.goodParam.pageSize;
      param.shopOrderStatus = this.goodParam.shopOrderStatus;
      param.contentType = this.pageType;
      this.getGoodsData(param).then(() => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize);
        });
      });
    }
  }
};
</script>

<style lang="scss">
@import "../../../assets/common/_base";
@import "../../../assets/common/_mixin";
@import "../../../assets/common/_var";

.orderListPage {
  .nav_list {
    ul {
      padding-left: 10px !important;
    }
  }
  .goods_list {
    margin-top: 8px;
    padding-bottom: 10px;
    background-color: #fff;
    h3 {
      line-height: 35px;
      font-size: 13px;
      color: #999;
      font-weight: normal;
      text-align: center;
    }
    .goods_item {
      position: relative;
      @extend .flex_v_justify;
      padding: 10px 10px 0;
    }
    .goods_img {
      position: relative;
      width: 50%;
      padding-top: 25%;
      margin-right: 15px;
      img {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        border-radius: 2px;
      }
    }
    .goods_title {
      @extend .flex_v_justify;
      font-size: 14px;
      color: #333;
      font-weight: 500;
    }
    .goods_info {
      flex: 1;
      -webkit-flex: 1;
    }

    .goods_grade {
      display: inline-block;
      margin-top: 5px;
      padding: 3px 5px;
      font-size: 11px;
      color: $color_main;
      @include getRadiusBorder($color_main, all, 4px);
    }
    .goods_detail {
      @extend .flex_v_justify;
      padding-top: 18px;
      color: #ccc;
      span {
        @extend .flex_hc;
      }
    }
    .rank_first,
    .rank_second,
    .rank_third {
      position: absolute;
      right: 10px;
      top: 10px;
    }
    .tag {
      font-size: 13px;
      color: $color_main;
    }
    .num {
      padding-right: 5px;
    }
    .mark {
      color: $color_main;
      font-size: 14px;
    }
  }
}
</style>
<style lang='scss' scoped>
</style>
