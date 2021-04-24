<template>
  <div class="no_data">
    <div>
      <img :src="imgUrlData" alt="">
      <p>{{textData}}</p>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    imgUrl: String,
    text: String
  },
  mounted() {
    console.log(this.imgUrl, this.text);
    this.imgUrlData = this.imgUrl
      ? this.imgUrl
      : require("../../assets/images/guess/no_data_icon.png");
    this.textData = this.text ? this.text : "";
  },
  data() {
    return {
      imgUrlData: "",
      textData: ""
    };
  },
  methods: {},
  components: {}
};
</script>

<style lang='scss' scoped>
@import "../../assets/common/_base";

.no_data {
  @extend .flex_v_h;
  height: 100%;
  min-height: inherit;
  text-align: center;
  > div {
    margin-top: -5.3333vw;
  }
  img {
    width: 9.07vw;
  }
  p {
    padding-top: 20px;
    font-size: 14px;
    color: #fedcd7;
  }
}
</style>
