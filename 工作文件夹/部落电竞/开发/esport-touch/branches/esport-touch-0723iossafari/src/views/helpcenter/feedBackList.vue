<template>
  <div class="Page">
    <header class="mod_header">
      <nav-bar :pageTitle="'意见反馈'"></nav-bar>
    </header>
    <div class="main">
      <div class="title_tip h90"><i class="icon_ask"></i>请选择问题场景<a @click="toMyFanKui()">我的反馈</a></div>
      <ul class="questionList">
        <li @click="toSubmit(item.index)" v-for="(item,index) in questionTypes" :key="index">
          <a href="javascript:;">{{item.description}}<span>{{item.outDescription}}</span><i></i></a>
        </li>
      </ul>
    </div>

    <footer class="mod_footer">

    </footer>
  </div>
</template>

<script>
import navBar from '../../components/header/nav_bar/index.vue';

export default {
  components: { navBar },
  props: [],
  data() {
    return {
      questionTypes: []
    };
  },
  mounted() {
    this.$post('/api/helpcenter/feedbackTypeList')
      .then(res => {
        if (res.code == '200') {
          console.log(res);
          this.questionTypes = res.data.questionTypes;
        } else {
          this.$toast(res.message);
        }
      })
      .catch(error => {
        console.log(error);
      });
  },
  methods: {
    getQuestionList() {},
    toSubmit(questionType) {
      this.$router.push({
        path: '/helpCenter/feedBackSubmit',
        query: {
          questionType: questionType
        }
      });
    },
    toMyFanKui() {
      this.$router.push({
        path: '/helpCenter/myFeedBack',
        query: ''
      });
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../assets/common/_base';

.Page {
  background-color: #fff;
}

.title_tip {
  position: relative;
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -webkit-align-items: center;
  -ms-flex-align: center;
  align-items: center;
  height: 10.6667vw;
  line-height: 10.6667vw;
  padding: 0 4vw;
  font-size: 3.4667vw;
  color: #666;
}
.title_tip.h90 {
  height: 12vw;
  line-height: 12vw;
}
.title_tip a {
  position: absolute;
  top: 50%;
  right: 4vw;
  margin-top: -2.9333vw;
  color: $color_btn;
  border: 0.2667vw solid $color_btn;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  border-radius: 5.3333vw;
  line-height: 5.8667vw;
  height: 5.8667vw;
  padding: 0 1.8667vw;
}
.questionList {
  background: #fff;
  padding: 0 3.3333vw;
  -webkit-box-shadow: 0 0.04rem 0.2rem rgba(245, 245, 245, 0.1);
  box-shadow: 0 0.04rem 0.2rem rgba(245, 245, 245, 0.1);
}
.questionList li {
  border-bottom: 0.1333vw solid #e5e5e5;
}
.questionList li:last-child {
  border: none;
}
.questionList li a {
  position: relative;
  display: block;
  height: 13.3333vw;
  line-height: 13.3333vw;
  padding-left: 1.3333vw;
  font-size: 4vw;
  color: #333;
  outline: none;
}
.questionList li a i {
  position: absolute;
  top: 4.8vw;
  right: 0.6667vw;
  width: 2vw;
  height: 3.7333vw;
  background: url('../../assets/images/help/icon_right.png');
  background-size: cover;
}
.questionList li a span {
  float: right;
  padding-right: 6.6667vw;
  font-size: 3.4667vw;
  line-height: inherit;
  color: #999;
}
.questionList li .answer {
  font-size: 3.7333vw;
  color: #999;
  line-height: 5.3333vw;
  padding: 4.6667vw;
  border-top: 0.1333vw solid #e5e5e5;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}
</style>
