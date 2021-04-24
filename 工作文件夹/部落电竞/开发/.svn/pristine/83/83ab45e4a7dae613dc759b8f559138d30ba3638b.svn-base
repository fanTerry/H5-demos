<template>
  <div class="Page">
    <header class="mod_header">
      <navBar pageTitle="我的消息"></navBar>
      <!-- <div class="tab">
        <div class="active">关注</div>
        <div @click="gotoTopic()">话题</div>
      </div> -->
    </header>

    <div class="main" id='mainId'>
      <mescroll ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit">
        <section class="mod_follow">
          <!-- <div class="my_follow_topic" v-if="this.userFollowTopics.length > 0">

            <h3>热门话题<a @click="gotoTopic()">更多<span></span></a></h3>
            <ul>
              <li v-for="(item,index) in userFollowTopics" :key="index" @click="gotoDetailTopic(item.id)">
                <div class="team " :class="{'active':item.newDatas}">
                  <img :src="item.iconUrl|getDefaultImg(globalConst.topicDefaultIcon)" alt="">
                  <p>{{item.name}}</p>
                </div>
              </li>
            </ul>
          </div> -->
          <!-- 文章列表组件 -->
          <articleTab :showType='showType' :videoType="2" :showCommon="1" :cmsContentList='cmsContentList'
            :topCmsContentList='topCmsContentList'>
          </articleTab>
        </section>
        <noData v-if="noData"> </noData>
        <loading v-if="loading"></loading>
      </mescroll>
    </div>
    <!-- <publishIcon></publishIcon> -->
    <!-- <footer class="mod_footer">
      <tabbar></tabbar>
    </footer> -->
  </div>
</template>


<script>
import navBar from "components/header/nav_bar/index.vue";
import topic from "components/user_follow/topic.vue";
import publishIcon from "components/user_follow/publishIcon.vue";
import Scroll from "components/common/scroll";
import localStorage from "../../../libs/storages/localStorage";
import fixScroll from "../../../libs/common/fix-scroll-ios";
import noData from "components/no_data/index.vue";
import loading from "components/common/loading";
import articleTab from "../../../components/follow/articleTab.vue";
import tabbar from "components/tabbar/index.vue";
import globalConst from "../../../globalConst";
import mescroll from "../../../components/common/mescroll.vue";

export default {
  components: {
    navBar,
    topic,
    Scroll,
    noData,
    loading,
    articleTab,
    publishIcon,
    tabbar,
    mescroll
  },
  data() {
    return {
      currTab: 1,
      userFollowTopics: [],
      showType: 100,
      noData: false,
      loading: false,
      showPublish: false,
      topCmsContentList: [],
      cmsContentList: [],
      scrollbar: { fade: true },
      pullDownRefresh: { threshold: 90, stop: 40, txt: "刷新成功" },
      pullUpLoad: {
        threshold: 10,
        txt: { more: "加载更多", noMore: "没有更多数据了" }
      },
      pageNo: 1,
      pageSize: 10,
      mescroll: null,
      hasNext: true
    };
  },
  created() {
    window.sessionStorage.selectedTab = 1;
  },
  mounted() {
    // wxApi.wxRegister(this.wxRegCallback())

    // var href = location.href.split('#')[0];
    // if (!window.sessionStorage.getItem("href")) {
    //   //将入口地址保存下来 ios分享会使用
    //   window.sessionStorage.setItem("href", href);
    // }

    // this.getMyFollowList();
    // this.getPageContenList();
    this.$wxApi.wxRegister({
      title: "枫叶电竞-发现",
      desc: "发现兴趣话题，找到志同道合的小伙伴~",
      imgUrl:
        "http://rs.esportzoo.com/svn/esport-res/mini/images/default/juzi_logo.jpg"
    });
  },
  methods: {
    downCallback() {
      console.log("下拉刷新");
      this.cmsContentList = [];
      this.pageNo = 1;
      let param = {};
      param.pageNo = this.pageNo;
      param.pageSize = 10;
      this.getPageContenList(param).then(data => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNext);
        });
      });
    },
    upCallback() {
      console.log("加载更多");
      this.loadMore();
    },
    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
    },
    gotoDetailTopic(topicId) {
      this.$router.push({
        path: "/detailTopic",
        query: {
          id: topicId
        }
      });
    },
    gotoTopic() {
      this.$router.push({
        path: "/topic",
        query: {}
      });
    },
    getPageContenList(param) {
      if (!param) {
        param = {};
        param.pageNo = 1;
        param.pageSize = 10;
      }
      console.log(param);
      return this.$post("/agency/userMsg/cmsMsg", param)
        .then(rsp => {
          const dataResponse = rsp;
          console.log(dataResponse.data.cmsContentList);
          if (dataResponse.code == "200") {
            this.currPageSize = dataResponse.data.dataList.length;
            this.hasNext = dataResponse.data.hasNext;
            if (dataResponse.data.dataList.length > 0) {
              this.cmsContentList = this.cmsContentList.concat(
                dataResponse.data.dataList
              );
            }
            if (this.cmsContentList.length == 0) {
              this.noData = true;
            } else {
              this.noData = false;
            }
            return this.cmsContentList;
          } else if (dataResponse.code == "9999") {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    toPublish(type) {
      this.showPublish = false;
      this.$router.push({
        name: "publishEssays",
        query: {
          type: type
        }
      });
    },
    /** 上拉加载*/
    loadMore() {
      this.pageNo = this.pageNo + 1;
      let param = {};
      param.pageNo = this.pageNo;
      param.pageSize = this.pageSize;
      this.getPageContenList(param).then(data => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNext);
        });
      });
    }
  },
  computed: {}
};
</script>


<style lang='scss'>
.follow_Page {
  .back {
    display: none;
  }
  .nav_bar {
    color: #ff7e00 !important;
    font-weight: bold;
  }
}
</style>

<style lang='scss' scoped>
@import "../../../assets/common/_base.scss";
@import "../../../assets/common/_mixin.scss";
@import "../../../assets/common/_var.scss";
@import "../../../assets/common/iconfont.css";

.no_data {
  @extend .g_v_c_mid;
}

.icon-tianxie {
  @extend .g_v_mid;
  right: 10px;
  font-size: 20px;
  color: #818181;
}

.mod_header {
  .tab {
    @extend .flex;
    background-color: #fff;
    div {
      position: relative;
      flex: 1;
      -webkit-flex: 1;
      padding: 10px 0;
      font-size: 15px;
      color: #999;
      text-align: center;
      &.active {
        color: #333;
        &::after {
          content: "";
          @extend .g_c_mid;
          bottom: 0;
          width: 20px;
          height: 3px;
          background-color: $color_main;
          border-radius: 2px;
        }
      }
    }
  }
}

.main {
  position: relative;
  .nav_list {
    margin-bottom: 7px;
  }
}

.mod_follow {
  .my_follow_topic {
    padding: 0 10px;
    background-color: #fff;
    ul {
      @extend .flex;
      flex-wrap: wrap;
      -webkit-flex-wrap: wrap;
    }
    li {
      position: relative;
      width: 21.7%;
      padding-top: 21.7%;
      margin-right: 4.4%;
      margin-bottom: 14px;
      box-shadow: 0 0 4px rgba(#000000, 0.1);
      &:nth-child(4n) {
        margin-right: 0;
      }
    }
  }
  .team {
    @extend .g_v_c_mid;
    @extend .flex_v_h;
    flex-direction: column;
    -webkit-flex-direction: column;
    width: 100%;
    height: 100%;
    text-align: center;
    &.active::after {
      content: "";
      position: absolute;
      right: 3px;
      top: 3px;
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background-color: $color_main;
    }
    img {
      width: 65%;
      height: 65%;
      object-fit: contain;
    }
    p {
      @include t_nowrap(100%);
      padding-top: 5px;
      font-size: 12px;
      color: #333;
    }
  }
  h3 {
    @extend .flex_v_justify;
    padding: 12px 0 18px;
    font-size: 14px;
    font-weight: normal;
    color: #000;
    a {
      @extend .flex_hc;
      font-size: 12px;
      color: #999;
      span {
        position: relative;
        width: 14px;
        height: 14px;
        @include getArrow(7px, #999, right);
      }
    }
  }
}

.sidebar {
  position: fixed;
  z-index: 99;
  bottom: 95px;
  right: 15px;
  color: #c2c2c2;
  text-align: center;
  .icon-sousuo {
    font-size: 12px;
    color: currentColor;
  }
  a {
    display: block;
    padding-top: 8px;
    color: currentColor;
  }
}
</style>

