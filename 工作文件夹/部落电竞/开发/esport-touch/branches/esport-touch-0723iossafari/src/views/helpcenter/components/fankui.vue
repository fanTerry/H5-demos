<template>
  <ul class="cmt_list detail">
    <li v-for="(item ,index) in dataList " :key="index" @click="goToDetail(item.id)" :id="'sendMessage_'+item.id">
      <div class="cmt_user" v-if="type==2">
        <div class="flex_hc">
          <span class="name">{{userInfo.nickName}}</span>
          <b></b>
          <span class="class">{{item.feedbackType==1?'用户问题':item.feedbackType==2?"客服回复":"用户回复"}}</span>
        </div>
        <span class="date">{{item.createTime | dateFmt}}</span>
      </div>
      <div class="cmt_cnt">
        {{item.content}}
      </div>
      <div class="cmt_att">

        <span class="img" v-for="(imageUrl, imgIndex) in item.imgList" :key="imgIndex">
          <img :src="imageUrl">
        </span>
      </div>
      <!-- <div class="cmt_att error" v-if="type==1"> -->
      <!--有图，加载失败-->
      <!-- <span class="img"><img src=""></span>
      </div> -->
      <div class="cmt_type" v-if="type==1"><span class="orange">{{item.questionTypeName}}</span></div>
      <!-- 用户的个人反馈才有cmt_time -->
      <div class="cmt_time" v-if="type==1">

        <span class="date">{{item.createTime | dateFmt}} &nbsp;</span>
        <a class="reply"><span class="num">{{item.replyNum}}</span></a>

      </div>
    </li>
  </ul>
</template>

<script>
export default {
  components: {},
  props: ['dataList', 'type'],
  data() {
    return {
      userInfo: {}
    };
  },
  mounted() {
    // this.userInfo = this.$store.state.user.userInfo;
    // this.userInfo = JSON.parse(window.localStorage.user);
    if (this.$store.state.userCenterInfo) {
      this.userInfo = this.$store.state.userCenterInfo;
    } else if (this.$store.state.user.userInfo) {
      this.userInfo = this.$store.state.user.userInfo;
    }
  },
  methods: {
    goToDetail(id) {
      console.log('id', id);
      if (this.type == 2) {
        return;
      }
      this.$router.push({
        path: '/helpcenter/feedBackDetail',
        query: { qid: id }
      });
    },
    scrollViewMySend(id) {
      const currel = document.getElementById('sendMessage_' + id);
      if (currel) {
        currel.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base';

.cmt_list li {
  background: #fff;
  padding: 4.5333vw 4vw 0;
  margin-bottom: 2.6667vw;
}
.cmt_list li:last-child {
  margin-bottom: 0;
}
.cmt_list .cmt_cnt {
  line-height: 4.8vw;
  margin-bottom: 4vw;
  font-size: 3.7333vw;
  color: #666;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}
.cmt_list .cmt_time {
  margin-top: 5.0667vw;
  padding: 2vw 0.8vw 1.6vw 0;
  font-size: 3.4667vw;
  line-height: 1.2;
  color: #999;
  border-top: 1px solid #eaeaea;
}
.cmt_list .cmt_time .reply {
  float: right;
  padding-left: 3.7333vw;
  color: #999;
  background: url('../../../assets/images/help/icon_comment.png') no-repeat left 0.5333vw;
  background-size: 3.7333vw 3.4667vw;
}
.cmt_list .cmt_time .reply .num {
  margin-left: 1.3333vw;
}
.cmt_list .cmt_att {
  margin-top: 2.4vw;
  font-size: 0;
}
.cmt_list .cmt_att .img {
  display: inline-block;
  width: 24vw;
  height: 24vw;
  margin: 0 1.3333vw 1.3333vw 0;
  overflow: hidden;
  text-align: center;
  background: #d0d0d0;
}
.cmt_list .cmt_att .img img {
  width: auto;
  display: inline-block;
  height: auto;
  min-width: 24vw;
  max-height: 24vw;
}
.cmt_list .cmt_att.error {
  margin-top: -1.3333vw;
  margin-bottom: -2.4vw;
}
.cmt_list .cmt_att.error .img {
  width: 8.1333vw;
  height: 5.7333vw;
  background: transparent;
}
.cmt_list .cmt_att.error .img img {
  min-width: 8.1333vw;
  max-height: 5.7333vw;
}
.cmt_list .cmt_user {
  display: flex;
  display: -webkit-flex;
  align-items: center;
  -webkit-align-items: center;
  justify-content: space-between;
  -webkit-justify-content: space-between;
  height: 5.3333vw;
  line-height: 5.3333vw;
  margin-bottom: 4vw;
  font-size: 3.4667vw;
  color: #999;
}
.cmt_list .cmt_user .name {
  color: #333;
  vertical-align: middle;
}
.cmt_list .cmt_user b {
  display: inline-block;
  width: 1px;
  height: 4.1333vw;
  margin: 0 1.0667vw 0 1.0667vw;
  background: #999;
}
.cmt_list .cmt_user .date {
  float: right;
}
.cmt_list.detail li {
  padding-bottom: 4vw;
}
.cmt_list.detail .cmt_cnt {
  margin-bottom: 4vw;
  -webkit-line-clamp: inherit;
}
.cmt_type {
  margin-top: 2.4vw;
}
.cmt_type span {
  font-size: 3.2vw;
  height: 4.5333vw;
  line-height: 4.5333vw;
  padding: 0 0.5333vw;
  margin-right: 1.3333vw;
  border: 0.2667vw solid transparent;
  border-radius: $border_radius;
}
.cmt_type span.orange {
  border-color: $color_btn;
  color: $color_btn;
}
</style>
