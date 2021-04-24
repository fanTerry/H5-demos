<template>
  <div id='mainId' class="main">
    <mescroll ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit" :isShowEmpty="false">
      <!-- 跟单版块 -->
      <section class="mod_follow_plan" v-if="type==2">
        <follow-plan-item :recommendDetail="item" :orderType="false" v-for="(item,index) in this.planRecommendInfo.dataList" :key="index"></follow-plan-item>
      </section>

      <!-- 专家方案版块 -->
      <section class="mod_expert_project" v-if="type==3">
        <expert-project-item></expert-project-item>
      </section>

      <no-data v-if="noData" :text="'暂无数据'"> </no-data>
    </mescroll>
  </div>
</template>

<script>
import followPlanItem from '../followplan/components/followPlanItem.vue';
import expertProjectItem from '../expert/components/expertProjectItem.vue';
import mescroll from '../../../components/common/mescroll.vue';
import noData from '../../../components/no_data/index.vue';

export default {
  components: { followPlanItem, expertProjectItem, mescroll, noData },
  props: ['type', 'matchId'],
  data() {
    return {
      mescroll: null,
      noData: false,
      planRecommendInfo: {
        tenWinList: [],
        loadMore: true,
        currPageSize: 10,
        dataList: [],
        requestParam: {
          pageNo: 1,
          pageSize: 10,
          followQueryType: 3 //赛事下的推荐单
        }
      }
    };
  },
  mounted() {},
  methods: {
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
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.planRecommendInfo.currPageSize, this.planRecommendInfo.loadMore);
        });
      });
    },
    upCallback() {
      console.log('上拉加载更多');
      this.planRecommendInfo.requestParam.pageNo += 1;
      this.pageData().then(data => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.planRecommendInfo.currPageSize, this.planRecommendInfo.loadMore);
        });
      });
    },
    //分页数据查询
    pageData() {
      let param = {};
      param = this.planRecommendInfo.requestParam;
      param.matchId = this.matchId;
      console.log(param);
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
    }
  }
};
</script>

<style lang='scss' scoped>
.mod_follow_plan,
.mod_expert_project {
  margin: 0 4.2667vw;
}

.mescroll {
  height: 100% !important;
}
.no_data {
  margin-top: 53.3333vw;
}
</style>
