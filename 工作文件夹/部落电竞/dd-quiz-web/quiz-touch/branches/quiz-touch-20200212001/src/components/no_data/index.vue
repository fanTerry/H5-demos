<template>
  <div class="no_data">
    <img :src="imgUrlData" alt="">
    <p>{{textData}}</p>
  </div>
</template>

<script>
export default {
  props: {
    imgUrl: String,
    text: String
  },
  mounted() {
    console.log(this.imgUrl,this.text);
    this.imgUrlData = this.imgUrl
      ? this.imgUrl
      : require('../../assets/images/no_data/no_data.png');
    this.textData = this.text ? this.text : "";
  },
  data() {
    return {
      imgUrlData: '',
      textData: ''
    };
  },
  methods: {},
  components: {}
};
</script>

<style lang='scss'>
.no_data {
  text-align: center;
  img {
    width: 193px;
  }
  p {
    padding-top: 20px;
    font-size: 14px;
    color: #999;
  }
}
</style>
