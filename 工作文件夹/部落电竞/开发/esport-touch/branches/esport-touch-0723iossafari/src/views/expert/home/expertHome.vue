<template>
  <div class="Page">
    <header class="mod_header">
      <navBar :pageTitle="'专家主页'"></navBar>
      <expertUsercard :userId="userId"></expertUsercard>
    </header>

    <div class="main">
      <mescroll ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit">
        <!-- <section class="mod_expert">
                <h2 class="expert_title">
                    <i class="zj_icon"></i>战绩走势
                    <ul>
                        <li>全部时间</li>
                        <li class="active">近7日</li>
                        <li>近30日</li>
                    </ul>
                </h2>
                <div class="expert_con expert_grid">
                    <div class="grid">
                        <p>500</p>
                        <p>总推荐数</p>
                    </div>
                    <div class="grid">
                        <div>
                            <p class="c_red">57%</p>
                            <p>命中率</p>
                        </div>
                        <div>
                            <p class="c_red">5</p>
                            <p>连红</p>
                        </div>
                    </div>
                    <div class="grid">
                        <div>
                            <p class="c_red">57%</p>
                            <p>命中率</p>
                        </div>
                        <div>
                            <p class="c_red">5</p>
                            <p>连红</p>
                        </div>
                    </div>
                </div>
                <div class="expert_con expert_forecast">
                    <ul>
                        <li>方案预测</li>
                        <li class="active">专栏</li>
                        <li>历史预测</li>
                    </ul>
                </div>
            </section> -->

        <section class="mod_expert">
          <h2 class="expert_title">
            <i class="onSale_icon"></i>在售方案
            <!-- <i class="history_icon"></i>历史方案 -->
          </h2>
          <!-- 暂无在售方案 -->
          <div class="hot_recommend" v-if="noData">
            <div class="no_recommend">
              <p>暂无在售方案</p>
              <p>关注专家可第一时间收到新发布方案通知</p>
            </div>
            <!-- <a class="follow_btn" v-if="!expertInfo.followFlag" @click="updateFollowStatus(expertInfo.userId,1)">+ 关注</a>
            <a class="followed_btn"  v-else @click="updateFollowStatus(expertInfo.userId,0)">- 已关注</a> -->
          </div>
          <hotRecommend :article="item" :type="1" v-for="(item,index) in articleList " :key="index">
          </hotRecommend>
        </section>

        <!-- <section class="mod_expert">
                <h2 class="expert_title"> -->
        <!-- 推荐选项胜负 -->
        <!-- <i class="history_icon"></i>历史方案 -->
        <!-- </h2>
                <div class="expert_con expert_match">
                    <div class="item">
                        <div class="left">
                            <img src="https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2094543967,385852855&fm=179&app=42&f=PNG?w=121&h=140" alt="">
                            <p>WE</p>
                        </div>
                        <div class="center">
                            <p class="name">2019 KPL青春赛-小组赛 拷贝</p>
                            <p class="vs">VS</p>
                            <p>5月29日08:00 [BO3]</p>
                        </div>
                        <div class="right">
                            <img src="https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2094543967,385852855&fm=179&app=42&f=PNG?w=121&h=140" alt="">
                            <p>WE</p>
                        </div>
                    </div>
                </div>
            </section> -->

        <!-- <section class="mod_expert">
                <h2 class="expert_title">
                    推荐理由
                </h2> -->
        <!-- 付费后去掉no_per展开文章内容 -->
        <!-- <div class="expert_con expert_reason no_per">
                    <h2>【官方】 电竞吧 专家评到用户指南</h2>
                    <p class="time">官方 2019-01-09 17：01：22</p>
                    
                    <p class="txt">1.专家文本文 本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本. 2.专家文本文 本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本专家文本文本文本.</p>
                    <div class="expert_mask">
                        <div class="lock"></div>
                        <p>请支付后查看推荐结果和理由</p>
                    </div>
                </div>
            </section> -->

        <!-- <section class="mod_expert ">
                <div class="expert_con expert_agreement">
                    <p>
                        <span>推荐编号</span>
                        <span class="num">201905063511594</span>
                    </p>
                    <p>
                        <span>发布时间</span>
                        <span class="num">201905063511594</span>
                    </p>
                    <p>
                        <span>支付金币</span>
                        <span class="num">
                            <span>38</span>个</span>
                    </p>
                    <p>
                        <span>查看人数</span>
                        <span class="num">201905063511594</span>
                    </p>
                    <div class="agreement">
                        <span class="active"></span>我已阅读同意
                        <a>《专家解读和推荐服务协议》</a>
                    </div>
                </div>
            </section> -->
        <!-- </scroll> -->
      </mescroll>
    </div>

    <!-- <footer class="mod_footer">
            <pay></pay>
        </footer> -->
  </div>
</template>

<script>
import hotRecommend from "../../../components/hot_recommend/index";
import pay from "../../../components/footer/pay/index";
import Scroll from "../../../components/common/scroll";
import navBar from "../../../components/header/nav_bar/index";
import expertUsercard from "../../../components/expert/user_card";
import mescroll from "../../../components/common/mescroll";

export default {
  data() {
    return {
      articleList: [],
      userId: Number,
      expertInfo: Object,
      noData: false,
      articlrQueryParam: {
        pageNo: 1,
        pageSize: 10,
        showAuthPage: true
        // returnUrl: api.getCurrentPageUrlWithArgs()
      },
      currPageSize: 0,
      scrollbar: { fade: true },
      pullDownRefresh: { threshold: 90, stop: 40, txt: "刷新成功" },
      pullUpLoad: {
        threshold: 10,
        txt: { more: "加载更多", noMore: "没有更多数据了" }
      }
    };
  },
  created() {
    this.userId = this.$route.query.id;
    console.log("userId", this.$route.query.id);
  },
  mounted() {
    // this.setExpertInfo();
    // this.getPageData();
  },
  methods: {
    downCallback() {
      console.log("下拉刷新");
      this.articleList = [];
      this.articlrQueryParam.pageNo = 1
      this.getPageData().then(() => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize);
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
    /**获取分页资讯数据 */
    getPageData(param) {
      if (!param) {
        param = this.articlrQueryParam;
      }
      param.userId = this.userId;
      console.log("分页参数", param);
      return this.$post("/api/recExpert/articleList/nologin", param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            console.log(
              dataResponse.data.articleList,
              "getPageData---请求成功"
            );
            this.currPageSize = dataResponse.data.articleList.length;
            if (dataResponse.data.articleList.length > 0) {
              this.articleList = this.articleList.concat(
                dataResponse.data.articleList
              );
            } else {
              if (this.pageNo == 1) {
                this.noData = true;
              }
            }
            return this.articleList;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    onPullingDown() {
      console.log("you are onPullingDown");
      if (this._isDestroyed) {
        return;
      }
      this.articleList = [];
      this.getPageData().then(() => {
        this.$refs.scroll.forceUpdate();
      });
    },
    onPullingUp() {
      console.log("you are onPullingUp");
      if (this._isDestroyed) {
        return;
      }

      if (this.currPageSize < this.articlrQueryParam.pageSize) {
        console.log("currPageSize", this.currPageSize);
        this.$refs.scroll.forceUpdate();
      } else {
        this.loadMore();
      }
    },
    /** 上拉加载*/
    loadMore() {
      this.articlrQueryParam.pageNo = this.articlrQueryParam.pageNo + 1;
      this.getPageData().then(data => {
        // this.$refs.scroll.forceUpdate();
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize);
        });
      });
    },
    goBack() {
      this.$router.back(-1);
    }
  },
  components: {
    hotRecommend,
    pay,
    Scroll,
    navBar,
    expertUsercard,
    mescroll
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_mixin.scss";
@import "../../../assets/common/_base.scss";
@import "../../../assets/common/_var.scss";

.main {
  padding-bottom: 40px;
}

.mod_expert {
  padding-top: 1px;
}

.expert_title {
  padding: 15px 12px 10px;
  font-size: 18px;
  font-weight: bold;
}

.expert_con {
  margin: 0 5px;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
}

.expert_grid {
  @extend .flex_hc;
  margin-bottom: 10px;
  text-align: center;
  .grid {
    flex: 1;
    -webkit-flex: 1;
    color: #818181;
    > div {
      padding: 4px 0;
      border-width: 0 0 1px 1px;
      border-style: solid;
      border-color: rgba(0, 0, 0, 0.1);
      &:last-child {
        border-bottom: none;
      }
    }
  }
  p {
    line-height: 16px;
  }
}

.expert_forecast {
  ul {
    @extend .flex;
    margin: 10px 35px;
    border-radius: 12px;
    overflow: hidden;
    background-color: #f5f6f7;
    font-weight: normal;
  }
  li {
    flex: 1;
    -webkit-flex: 1;
    height: 24px;
    @extend .flex_v_h;
    color: #a3a3a3;
    border: 1px solid rgba(0, 0, 0, 0.1);
    &:first-child {
      border-right: none;
      border-radius: 12px 0 0 12px;
    }
    &:nth-child(2) {
      border-right: none;
      border-left: none;
    }
    &:last-child {
      border-left: none;
      border-radius: 0 12px 12px 0;
    }
    &.active {
      background-color: #fea21b;
      color: #fff;
      border: none;
    }
  }
}

.onSale_icon,
.history_icon,
.zj_icon {
  width: 15px;
  height: 15px;
  margin-right: 1px;
}

.onSale_icon {
  @include getBgImg("../../../assets/images/expert/onSale.png");
}

.history_icon {
  @include getBgImg("../../../assets/images/expert/history.png");
}

.expert_match {
  .item {
    @extend .flex_v_justify;
    height: 84px;
    @include getBorder(bottom, rgba(0, 0, 0, 0.1));
    text-align: center;
    &:last-child {
      background: initial;
    }
    > div {
      flex: 1;
      -webkit-flex: 1;
      white-space: nowrap;
    }
    img {
      width: 38px;
      height: 38px;
      margin-bottom: 5px;
      border-radius: 50%;
      object-fit: cover;
    }
    .vs {
      padding: 10px 0;
      color: #444;
    }
    .left,
    .right {
      font-size: 14px;
    }
    .center {
      color: #999;
    }
  }
}

.expert_reason {
  padding: 15px;
  &.no_per {
    position: relative;
    height: 145px;
    .expert_mask {
      display: block;
    }
  }
  .time {
    padding-top: 15px;
    color: #aeaeae;
  }
  .txt {
    padding-top: 10px;
    padding-bottom: 100px;
    line-height: 18px;
    color: #4a4a4a;
  }
  .expert_mask {
    display: none;
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 145px;
    color: #979797;
    background-color: rgba(255, 255, 255, 0.95);
    text-align: center;
  }
  .lock {
    width: 50px;
    height: 50px;
    margin: 20px auto 10px;
    @include getBgImg("../../../assets/images/expert/lock.png");
  }
}

.expert_agreement {
  padding: 15px 20px 10px;
  margin-top: 10px;
  p {
    @extend .flex_v_justify;
    padding-bottom: 15px;
    color: #a3a3a3;
  }
  .num {
    color: #333;
    span {
      color: $color_main;
    }
  }
  .agreement {
    @extend .flex_v_h;
    span {
      width: 16px;
      height: 16px;
      margin-right: 5px;
      border: 1px solid rgba(0, 0, 0, 0.5);
      border-radius: 50%;
      &.active {
        @include getBgImg("../../../assets/images/expert/agree.png");
        border: none;
      }
    }
    a {
      color: #679dfa;
    }
  }
}

.mod_footer {
  background-color: #fff;
}
</style>
