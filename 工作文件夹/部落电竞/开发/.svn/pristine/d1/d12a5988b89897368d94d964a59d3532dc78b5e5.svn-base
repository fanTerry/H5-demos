<template>
  <div class="Page">
    <header class="mod_header">
      <nav-bar :pageTitle="'帮助中心'"></nav-bar>
    </header>
    <div class="main">
      <ul class="helpItems">
        <li @click="goUrlPage('/helpCenter/questionList',item.type)" v-for="(item,index) in questionList" :key="index">
          <img :src="item.imgUrl" alt="">
          <p>{{item.desc}}</p>
        </li>
        <!-- <li>
          <img src="../../assets/images/help/niudan.png">
          <p>扭蛋机</p>
        </li> -->
      </ul>
      <div class="title1">
        <span class="fs32 c333 fw500">热门问题</span>
      </div>
      <ul class="questionList">
        <li @click="goUrlPage('/helpCenter/questionList',item.type,item.sortNum)" v-for="(item,index) in hotQuestionList" :key="index">
          <a>{{item.questionName}}<i></i></a>
        </li>
      </ul>
      <p class="serviceTel">
        <!-- 客服电话：<a>400-9613-588</a> -->
      </p>
    </div>
    <footer class="mod_footer">
      <div class="footTab">
        <a @click="showWxFlag = true"><i class="info"></i>官方客服</a>
        <a @click="goUrlPage('/helpCenter/feedBackList')"><i class="note"></i>意见反馈</a>
      </div>
    </footer>

    <div class="ui_pop" v-if="showWxFlag">
      <div class="wx_qr_code">
        <!-- <p class="title">微信扫一扫联系客服</p> -->
        <!-- <img src="../../assets/images/help/wxchat.jpg" alt=""> -->
        <p class="wechat_id">赢加竞技客服QQ①：2943894994</p>
        <p class="wechat_id">赢加竞技客服QQ②：2732470865</p>
        <p class="wechat_id">赢加竞技客服QQ③：2125130860</p>
        <a @click="showWxFlag = false">取消</a>
      </div>
    </div>
  </div>
</template>

<script>
import navBar from '../../components/header/nav_bar/index.vue';

export default {
  components: {
    navBar
  },
  props: [],
  data() {
    return {
      pageTitle: '',
      questionList: [],
      hotQuestionList: [],
      showWxFlag: false
    };
  },
  mounted() {
    this.getHotQuestions();
  },
  methods: {
    goUrlPage(url, type, sortNum) {
      this.$router.push({
        path: url,
        query: {
          type: type,
          sortNum: sortNum - 1
        }
      });
    },
    getHotQuestions() {
      this.$get('../../../static/jsonData/helpCenterQuestion.json')
        .then(res => {
          console.log(res, 'json文本数据');
          // if (res.code == "200" && res.data) {
          const dataResponse = res;
          this.questionList = dataResponse.data.slice(0, dataResponse.data.length - 1);
          this.hotQuestionList = dataResponse.data[dataResponse.data.length - 1].questionList;
          console.log(this.hotQuestionList);
          // }
        })
        .catch(e => {
          console.log(e);
        });
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../assets/common/_mixin';
@import '../../assets/common/_base';

.Page {
  background-color: #fff;
}

.wx_qr_code {
  width: 72.8vw;
  padding-top: 6.1333vw;
  background-color: #fff;
  border-radius: 2.1333vw;
  color: #333;
  text-align: center;
  .title {
    font-size: 4.2667vw;
    line-height: 4.8vw;
  }
  img {
    margin-top: 4.8vw;
    width: 42.6667vw;
  }
  .wechat_id {
    padding-top: 2.6667vw;
    font-size: 4vw;
    line-height: 5.3333vw;
  }
  a {
    display: block;
    margin-top: 6.6667vw;
    line-height: 13.8667vw;
    font-size: 4.5333vw;
    @include getBorder(top, #ddd);
  }
}

.helpItems {
  padding: 1.3333vw 0 4vw;
  background: #fff;
  overflow: hidden;
}
.helpItems li {
  float: left;
  width: 33.3%;
  padding: 3.3333vw 0;
  text-align: center;
}
.helpItems li img {
  width: 11.2vw;
  height: 11.2vw;
}
.helpItems li p {
  line-height: 4.8vw;
  font-size: 3.4667vw;
  color: #333;
  margin-top: 1.7333vw;
}
.title1 {
  height: 11.3333vw;
  line-height: 11.3333vw;
  padding-left: 4vw;
}
.questionList {
  background: #fff;
  padding: 0 3.3333vw;
  -webkit-box-shadow: 0 0.04rem 0.2rem rgba(245, 245, 245, 0.1);
  box-shadow: 0 0.04rem 0.2rem rgba(245, 245, 245, 0.1);
}
.questionList li {
  border-bottom: 1px solid #e5e5e5;
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
  background: url(../../assets/images/help/icon_right.png);
  background-size: cover;
}
.questionList li a span {
  float: right;
  padding-right: 6.6667vw;
  font-size: 3.4667vw;
  color: #999;
}
.questionList li .answer {
  font-size: 3.7333vw;
  color: #999;
  line-height: 5.3333vw;
  padding: 4.6667vw;
  border-top: 1px solid #e5e5e5;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}
.questionList.detail li a i {
  -webkit-transform: rotate(90deg);
  -ms-transform: rotate(90deg);
  transform: rotate(90deg);
  -webkit-transition: all 0.3s ease-out;
  -o-transition: all 0.3s ease-out;
  transition: all 0.3s ease-out;
}
.questionList.detail li a i.active {
  -webkit-transform: rotate(-90deg);
  -ms-transform: rotate(-90deg);
  transform: rotate(-90deg);
}
.serviceTel {
  height: 10.6667vw;
  line-height: 10.6667vw;
  text-align: center;
  font-size: 4vw;
  color: #666;
}
.serviceTel a {
  color: #2c7ee0;
}

.footTab {
  display: -webkit-flex;
  display: flex;
  align-items: center;
  -webkit-align-items: center;
  // position: absolute;
  width: 100%;
  // left: 0;
  // bottom: 0;
  background: $color_btn;
}
.footTab a {
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -webkit-align-items: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-box-pack: center;
  -webkit-justify-content: center;
  -ms-flex-pack: center;
  justify-content: center;
  // float: left;
  // width: 50%;
  flex: 1;
  -webkit-flex: 1;
  height: 12vw;
  line-height: 12vw;
  text-align: center;
  font-size: 4vw;
  color: #fff;
  border-right: 1px solid #fff;
}
.footTab a:last-child {
  border: none;
}
.footTab a i {
  display: inline-block;
  margin-right: 1.3333vw;
}
.footTab a i.info {
  width: 4.1333vw;
  height: 4.1333vw;
  background: url(../../assets/images/help/icon_info.png) no-repeat;
  background-size: cover;
}
.footTab a i.note {
  width: 4vw;
  height: 4.2667vw;
  background: url(../../assets/images/help/icon_note.png) no-repeat;
  background-size: cover;
}
</style>
