<template>
  <div class="Page myFkPage">
    <header class="mod_header">
      <nav-bar :pageTitle="'我的反馈'"></nav-bar>
    </header>
    <div class="main" id="mainId">
      <mescroll ref="mescroll" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit">
        <fan-kui-list :dataList="myFeedbackList" :type="1"></fan-kui-list>
      </mescroll>
    </div>

    <footer class="mod_footer">

    </footer>
  </div>
</template>

<script>
import navBar from "../../components/header/nav_bar/index.vue";
import fanKuiList from "./components/fankui.vue";
import mescroll from "../../components/common/mescroll.vue";
export default {
  components: {
    navBar,
    fanKuiList,
    mescroll
  },
  props: [],
  data() {
    return {
      myFeedbackList: [],
      pageParam: {
        pageNo: 1,
        pageSize: 10
      },
      currPageSize: 10,
      hasNextPage: true
    };
  },
  mounted() {
    // this.getMyFeedBack();
  },
  methods: {
    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
    },
    downCallback() {
      this.myFeedbackList = [];
      this.getMyFeedBack(this.pageParam).then(data => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNextPage);
        });
      });
    },
    upCallback() {
      this.pageParam.pageNo = this.pageParam.pageNo + 1;
      this.getMyFeedBack(this.pageParam).then(data => {
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize, this.hasNextPage);
        });
      });
    },
    getMyFeedBack(param) {
      if (param) {
        param = this.pageParam;
      }
      return this.$post("/api/helpcenter/myFeedbackList", param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            let dataList = dataResponse.data.dataList;
            this.currPageSize = dataList.length;
            if (dataList && dataList.length > 0) {
              dataList.forEach(element => {
                if (element.imageUrl) {
                  element.imgList = element.imageUrl.split(",");
                }
              });
            }
            this.hasNextPage = dataResponse.data.hasNext;
            this.myFeedbackList = this.myFeedbackList.concat(dataList);
            return dataList;
          } else if (dataResponse.code == "9999") {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          this.$toast("网络异常，稍后再试");
          console.log(error);
        });
    }
  }
};
</script>

<style lang="scss">
.myFkPage {
  .cmt_list.detail li {
    padding-bottom: 0 !important;
  }
}
</style>

<style lang='scss' scoped>
</style>
