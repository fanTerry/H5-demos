<template>
  <div class="list">
    <div v-for='(item,index) in 2' :key="index">
      <div class="item" v-for="(item,index) in gradeList" :key="index">
        <div>
          <img :src="item.grade|goldEmpireAward" alt="">
          <p class="txt">
            <i class="male_icon" v-if="item.grade == 1"></i>
            <i class="female_icon" v-if="item.grade == 2"></i>
            {{item.name}}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      gradeList: [
        { name: '男性当选', grade: 1 },
        { name: '女性当选', grade: 2 },
        { name: '男元老派', grade: 5 },
        { name: '男民主派', grade: 6 },
        { name: '女元老派', grade: 7 },
        { name: '女民主派', grade: 8 },
        { name: '查理曼大帝', grade: 9 },
        { name: '凯撒大帝', grade: 10 },
        { name: '屋大维', grade: 11 },
        { name: '玛丽', grade: 12 },
        { name: '叶卡捷琳娜', grade: 13 },
        { name: '伊莎贝拉', grade: 14 },
        { name: '奥托大帝', grade: 15 },
        { name: '查士丁尼大帝', grade: 16 },
        { name: '君士坦丁一世', grade: 17 },
        { name: '玛格丽特', grade: 18 },
        { name: '维多利亚', grade: 19 },
        { name: '伊丽莎白', grade: 20 }
      ]
    };
  },
  methods: {}
};
</script>

<style lang='scss' scoped>
@import '../../../../assets/common/_base';
@import '../../../../assets/common/_mixin';
.list {
  @extend .flex_hc;
  display: inline-flex;
  display: -webkit-inline-flex;
}

.list > div {
  @extend .flex_hc;
}

.item {
  position: relative;
  width: 18.1333vw;
  height: 15.0667vw;
  margin: 0 3.6vw;
  text-align: center;
  > div {
    position: relative;
    z-index: 1;
    border-radius: 1.3333vw;
    background: linear-gradient(to bottom, #60c892, #47af7c);
    background: -webkit-linear-gradient(top, #60c892, #47af7c);
    filter: blur(2px);
    -webkit-filter: blur(2px);
  }
  img {
    height: 15.0667vw;
  }
}
.txt {
  @extend .g_c_mid;
  @extend .flex_hc;
  bottom: 0.5333vw;
  padding: 0.6667vw 0.8vw;
  font-size: 3.2vw;
  border-radius: 4.8vw;
  color: #fff;
  background-color: rgba(0, 0, 0, 0.7);
  white-space: nowrap;
}
</style>
