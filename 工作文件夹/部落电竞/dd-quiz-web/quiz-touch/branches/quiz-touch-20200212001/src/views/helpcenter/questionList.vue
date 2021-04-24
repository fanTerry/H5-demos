<template>
  <div class="Page">
    <header class="mod_header">
      <nav-bar :pageTitle="dataList.desc"></nav-bar>
    </header>
    <div class="main">
      <ul class="questionList detail">
        <li @click="toggle(index)" v-for="(item,index) in dataList.questionList" :key="index">
          <a :class="{'active':listFlag == index}">{{item.questionName}}<i></i></a>
          <div class="answer" v-if="listFlag == index">
            <p v-for="(item,index) in item.questionAnswer" :key="index">{{item}}</p>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import navBar from "../../components/header/nav_bar/index.vue";

export default {
  components: { navBar },
  props: [],
  data() {
    return {
      dataList: [],
      listFlag: -1
    };
  },
  created() {
    this.type = this.$route.query.type;
    this.listFlag = this.$route.query.sortNum;
  },
  mounted() {
    this.getHotQuestions(this.type).then(() => {
      if (document.querySelector(".active")) {
        document.querySelector(".active").scrollIntoView();
      }
    });
  },
  methods: {
    toggle(index) {
      if (index == this.listFlag) {
        this.listFlag = -1;
      } else {
        this.listFlag = index;
      }
    },
    getHotQuestions(index) {
      return this.$get("../../../static/jsonData/helpCenterQuestion.json")
        .then(res => {
          console.log(res, "json文本数据");
          const dataResponse = res;
          this.dataList = dataResponse.data[index];
          console.log(this.dataList);
        })
        .catch(e => {
          console.log(e);
        });
    }
  }
};
</script>

<style lang='scss' scoped>
.questionList {
  background: #fff;
  padding: 0 3.3333vw;
  -webkit-box-shadow: 0 0.5333vw 2.6667vw rgba(245, 245, 245, 0.1);
  box-shadow: 0 0.5333vw 2.6667vw rgba(245, 245, 245, 0.1);
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
  background: url("../../assets/images/help//icon_right.png");
  background-size: cover;
}
.questionList li a span {
  float: right;
  padding-right: 6.6667vw;
  font-size: 3.4667vw;
  color: #999;
}
.questionList li .answer {
  padding: 4.6667vw;
  border-top: 0.1333vw solid #e5e5e5;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  p {
    font-size: 3.7333vw;
    color: #999;
    line-height: 5.3333vw;
  }
}
.questionList.detail li a i {
  -webkit-transform: rotate(90deg);
  -ms-transform: rotate(90deg);
  transform: rotate(90deg);
  -webkit-transition: all 0.3s ease-out;
  -o-transition: all 0.3s ease-out;
  transition: all 0.3s ease-out;
}
.questionList.detail li a.active i {
  -webkit-transform: rotate(-90deg);
  -ms-transform: rotate(-90deg);
  transform: rotate(-90deg);
}
</style>
