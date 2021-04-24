<!--
 * @Author: your name
 * @Date: 2019-09-11 11:22:49
 * @LastEditTime: 2020-04-21 18:00:48
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch/src/components/hot_recommend/index.vue
 -->
<template>
  <div class="hot_recommend">
    <div class="user_info">
      <div v-if="type!=1">
        <div class="user_img" @click="toExpertHome(article.userId)">
          <img :src="article.authorAvatar" alt="">
        </div>
        <div>
          <div class="name">
            <!-- <i class="fine">精</i> -->
            <span class="txt">{{article.authorName}}</span>
            <!-- <span class="record">近10中0</span> -->
          </div>
          <p class="tips">
            <!-- 推荐买点:
            <span>胜负让分</span>
            <span>大小分</span>
            <span>十二杀</span> -->
          </p>
        </div>
      </div>
      <div>
        <!-- <p class="return_rate">66%<br>近期回报率</p> -->
        <!-- 黑单,红单 -->
        <!-- <i class="wrong">黑</i> -->
        <!-- <i class="true">红</i> -->
      </div>
    </div>
    <div class="match_con" @click="goDetail(article.id,article.userId)">
      <div class="match_name">
        <div>
          <img class="logo" v-if="article.videogameId==1"
            src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562671724161&di=e1f0a7a5c8ecb42e4f9007eda15e2b91&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F031096455ea5aca000001cbb0ee8e30.jpg"
            alt="">
          <img class="logo" v-if="article.videogameId==4"
            src="https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1562671781275&di=2b41fd4370a413a504b28101c8704de5&imgtype=0&src=http%3A%2F%2Fp2.so.qhmsg.com%2Ft013599c658b4887137.png"
            alt="">

          <span class="match_title">{{article.title}}</span>
        </div>
      </div>
      <div class="match_team" v-for="(item ,index) in article.quizMatchResultList" :key="index">
        <div>
          <div class="left">{{item.homeTeamName}}<img :src="item.homeTeamLogo" alt=""></div>
          <span class="vs">VS</span>
          <div class="right"><img :src="item.awayTeamLogo" alt="">{{item.awayTeamName}}</div>
        </div>
        <!-- <div class="refund">不中全退</div> -->
      </div>
    </div>
    <div class="release_time">
      <p>{{article.publishTime}}发布 <span class="break_icon">|</span> <i class="iconfont icon-icon_yulan"></i>
        {{article.views}}</p>
      <div class="ticket_cost">
        <template v-if="article.isFree==0 && article.price>0">
          <span v-if="article.hasPayed ">已支付</span>
          <span v-else-if="payFlag && article.id==expertData.payedArticle">已支付</span>
          <template v-else>
            <span class="star_coin"></span> {{article.price+"星星"}}
          </template>
        </template>
        <span v-else>免费</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {
  props: ["article", "type", "userId"],
  data() {
    return {
      articlrQueryParam: {
        pageNo: 1,
        pageSize: 10,
        showAuthPage: true
        // returnUrl: api.getCurrentPageUrlWithArgs()
      },
      payFlag: false,
    };
  },
  computed: {
    ...mapGetters({
      expertData: "getExpertData",
    })
  },
  activated() {
    //只针对需要支付的文章，返回需要刷新支付状态
    if (this.expertData.payedArticle == this.article.id && this.article.isFree == 0) {
      console.log("更新文章支付状态");
      console.log("article.id", this.article.id);
      //查看当前用户是否已经支付该文章
      this.getArticleData();
    }

  },
  methods: {
    toExpertHome(id) {
      this.$router.push({
        path: "/expertHome",
        query: {
          id: id
        }
      });
    },   
    goDetail(articleId, userId) {
      console.log(userId);
      this.$router.push({
        path: "/articleDetail",
        query: {
          aId: articleId,
          uId: userId
        }
      });
    },
    getArticleData(param) {
      if (!param) {
        param = this.articlrQueryParam;
      }
      param.userId = this.userId;
      console.log("分页参数", param);
      return this.$post("/api/article/detail/new/" + this.article.id, param)
        .then(rsp => {
          const dataResponse = rsp;
          console.log(dataResponse, "获取文章详情new");
          if (dataResponse.code == "200") {
            //文章查看状态
            this.payFlag = dataResponse.data.canView;
          } else if (dataResponse.code == "555") {
            //用户未登录
            console.log("用户未登录");
            // window.location.href =
            //   "/login?agentId=" +
            //   this.baseParamConfig.agentId +
            //   "&biz=" +
            //   this.baseParamConfig.biz;
            // console.log("用户未登录1");
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
  },
}
</script>


<style lang='scss' scoped>
@import "../../assets/common/_mixin.scss";
@import "../../assets/common/_base.scss";
@import "../../assets/common/_var.scss";
@import "../../assets/common/iconfont.css";

.hot_recommend {
  margin-bottom: 8px;
  padding: 0 12px;
  background-color: #fff;
  .user_info {
    @extend .flex_v_justify;
    padding: 12px 0;
    > div {
      @extend .flex_hc;
    }
    .name {
      @extend .flex_hc;
      max-width: initial;
      .txt {
        @include t_nowrap(120px);
        line-height: 1.2;
        font-size: 16px;
        font-weight: bold;
        color: #333;
      }
    }
  }
  .user_img {
    position: relative;
    width: 50px;
    height: 50px;
    margin-right: 10px;
    img {
      width: 100%;
      height: 100%;
      border-radius: 50%;
    }
  }

  .record {
    font-size: 10px;
    line-height: 14px;
    padding: 0 8px;
    border-radius: 12px;
    color: #fff;
    background-color: #d60c1f;
  }
  .tips {
    margin-top: 5px;
    font-size: 12px;
    color: #a3a3a3;
    @media (max-width: 320px) {
      font-size: 10px;
    }
    span {
      padding-left: 5px;
      color: $color_main;
    }
  }
  .return_rate {
    text-align: right;
    line-height: 16px;
    color: #d60c1f;
    @media (max-width: 320px) {
      font-size: 10px;
    }
  }
  .fine {
    font-size: 12px;
    line-height: 14px;
    padding: 0 1px;
    margin-right: 2px;
    color: #fff;
    background-color: #1086fd;
  }
  .wrong,
  .true {
    display: block;
    width: 24px;
    line-height: 24px;
    font-size: 12px;
    color: #fff;
    border-radius: 50%;
    text-align: center;
  }
  .wrong {
    background-color: #999;
  }
  .true {
    background-color: #d60c1f;
  }
  .match_con {
    padding: 12px 12px 24px;
    border-radius: 6px;
    background-color: #f8f9fe;
    > div {
      @extend .flex_v_justify;
    }
  }
  .match_title {
    @include t_nowrap(180px);
    font-size: 17px;
    line-height: 1.2;
    color: #333;
  }
  .match_name {
    padding-bottom: 12px;
    > div {
      @extend .flex_hc;
      color: #575757;
    }
  }
  .star_coin,
  .logo {
    width: 14px;
    height: 14px;
    margin-right: 2px;
    border-radius: 50%;
    object-fit: cover;
  }
  .logo {
    width: 23px;
    height: 23px;
    margin-right: 10px;
  }
  .match_team {
    margin-top: 10px;
    font-size: 14px;
    color: #333;
    > div {
      @extend .flex_hc;
      margin: 0 auto;
    }
    .left,
    .right {
      @extend .flex_hc;
      width: 80px;
    }
    .left {
      justify-content: flex-end;
      -webkit-justify-content: flex-end;
    }
    .right {
      justify-content: flex-start;
      -webkit-justify-content: flex-start;
    }
    img {
      width: 20px;
      height: 20px;
      margin: 0 10px;
      object-fit: cover;
    }
    .vs {
      padding: 0 25px;
      font-size: 14px;
      color: #c8c8c8;
    }
  }
  .refund {
    line-height: 14px;
    padding: 0 5px;
    font-size: 10px;
    border-radius: 14px;
    color: #fff;
    background-color: #ec9805;
  }
  .release_time {
    @extend .flex_v_justify;
    padding-left: 10px;
    font-size: 11px;
    height: 33px;
    color: #cfcfcf;
  }
  .ticket_cost {
    @extend .flex_hc;
    font-size: 12px;
    color: $color_main;
  }
  .iconfont {
    font-size: 12px;
    margin-right: 5px;
  }
  .break_icon {
    padding: 0 10px;
  }
  .no_recommend {
    margin: 10px 5px 0;
    padding: 10px 0;
    border-radius: 8px;
    background-color: #f8f9fe;
    text-align: center;
    p {
      line-height: 15px;
      color: #a3a3a3;
    }
  }
  .follow_btn {
    @extend .flex_v_h;
    width: 70px;
    height: 24px;
    font-size: 13px;
    margin: 10px auto;
    border-radius: 24px;
    color: #fff;
    background-color: $color_main;
  }
}
</style>