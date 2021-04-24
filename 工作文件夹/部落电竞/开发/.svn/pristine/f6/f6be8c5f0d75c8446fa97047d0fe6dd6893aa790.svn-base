<template>
  <div class="Page">
    <header class="mod_header">
      <navBar :pageTitle="'发布的文章'"></navBar>
      <!-- <expertUsercard :userId="userId" ></expertUsercard> -->
    </header>

    <div class="main" id='mainId'>
      <mescroll ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit"
        :isShowEmpty="false">
        <!-- <scroll ref="scroll" :scrollbar="scrollbar" :pullDownRefresh="pullDownRefresh" :pullUpLoad="pullUpLoad"
        :startY="0" @pullingDown="onPullingDown" @pullingUp="onPullingUp"> -->

        <section class="mod_expert">
          <!-- 文章列表组件 -->
          <articleTab :cmsContentList='cmsContentList' :videoType="1">
          </articleTab>
        </section>
        <noData v-if="noData"> </noData>
        <!-- </scroll> -->
      </mescroll>
    </div>

  </div>
</template>

<script>
import articleTab from "../../../components/follow/articleTab.vue";
import noData from "components/no_data/index";
import navBar from "../../../components/header/nav_bar/index";
import mescroll from "../../../components/common/mescroll.vue";
import globalConst from "../../../globalConst";
export default {
  data() {
    return {
      cmsContentList: [],
      hasNext: false, //是否有下一页
      noData: false,
      userId: Number,
      queryParam: {
        pageNo: 1,
        pageSize: 10
      },
      currPageSize: 10,
      mescroll: null,
      mescrollConfig: {
        warpId: "mainId", //设置置顶时，必须设置父容器ID
        hasToTop: true //默认不开启回到顶部项
      },
      lastUpdateTime: null
    };
  },
  created() {
    this.userId = this.$route.query.id;
    console.log("用户发布的短文userId", this.userId);
  },

  mounted() {
    // this.getPageData();
    this.$refs.mescroll.config = this.mescrollConfig;
  },

  activated() {
    // console.log("进来1");
    this.userId = this.$route.query.id;
    if (!this.$route.meta.isBack) {
      // console.log("进来2");
      let param = {};
      this.queryParam.pageNo = 1;
      param.pageNo = this.queryParam.pageNo;
      param.pageSize = this.queryParam.pageSize
      // param = this.queryParam;
      param.userId = this.$route.query.id;
      this.cmsContentList = [];
      this.lastUpdateTime = null;
      this.currPageSize = 10;
      this.getPageData(param).then(() => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNext);
        });
      });
    }
    this.$route.meta.isBack = false;
    console.log("this.$route.meta.isBack", this.$route.meta.isBack);

  },

  beforeRouteEnter(to, from, next) {
    // console.log("from.name", from.name);
    // 这个name是下一级页面的路由name
    if (from.name == "homeDetail" || from.name == "myRelease" || from.name == "myFollow") {
      // 设置为true说明你是返回到这个页面，而不是通过跳转从其他页面进入到这个页面
      to.meta.isBack = true;
    } else {

    }
    // console.log(" to.meta.isBack ", to.meta.isBack);
    next();
  },

  methods: {
    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
      // this.mescroll.setBounce(true);
    },

    downCallback() {
      let param = {};
      this.queryParam.pageNo = 1;
      // param = this.queryParam;
      param.pageNo = this.queryParam.pageNo;
      param.pageSize = this.queryParam.pageSize
      param.userId = this.$route.query.id;
      this.cmsContentList = [];
      this.currPageSize = 10;
      this.getPageData(param).then(() => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNext);
        });
      });
    },
    upCallback() {
      this.loadMore();
    },
    loadMore() {
      this.queryParam.pageNo += 1;
      let param = {};
      // param = this.queryParam;
      param.pageNo = this.queryParam.pageNo;
      param.pageSize = this.queryParam.pageSize
      param.userId = this.userId;
      this.getPageData(param).then(() => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNext);
        });
      });
    },

    /**获取分页资讯数据 */
    getPageData(param) {
      console.log("param", param);
      if (!param) {
        param = this.queryParam;
        param.userId = this.userId;
      }
      //查询第一页，不需要携带时间戳
      if (this.lastUpdateTime && param.pageNo != 1) {
        console.log("545454545");
        param.lastUpdateTime = this.lastUpdateTime + "";
      }
      console.log("查询参数", param);
      return this.$post("/api/userContent/getUserContent", param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            console.log("1=-----", dataResponse.data);
            this.currPageSize = dataResponse.data.dataList.length;
            this.hasNext = dataResponse.data.hasNext;
            if (dataResponse.data.dataList.length > 0) {
              this.cmsContentList = this.cmsContentList.concat(
                dataResponse.data.dataList
              );
            }
            if (this.cmsContentList.length == 0 && this.queryParam.pageNo == 1) {
              this.noData = true;
            } else {
              this.noData = false;
            }
            //更新最后一条数据的updateTime
            this.lastUpdateTime = this.cmsContentList[this.cmsContentList.length - 1].updateTime;
            return this.cmsContentList;
          } else if (dataResponse.code == "9999") {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  },
  components: {
    articleTab,
    mescroll,
    noData,
    navBar
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_mixin.scss";
@import "../../../assets/common/_base.scss";
@import "../../../assets/common/_var.scss";

.mod_expert {
  padding-top: 1px;
}
.expert_title {
  padding: 15px 12px 10px;
  font-size: 18px;
  font-weight: 500;
}
</style>
