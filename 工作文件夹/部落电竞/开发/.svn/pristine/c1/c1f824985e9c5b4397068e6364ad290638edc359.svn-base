<template>
  <div class="test">
    <router-link to="/testCity">←返回test页面</router-link>
    <!-- <div>获取vuex中的值：{{this.$store.state.city}}</div> -->
    <!-- <div>获取vuex中的值：{{this.city}}</div> -->

    <!-- Getter -->
    <!-- <div>获取vuex中的值：{{this.$store.getters.currentCity}}</div> -->
    <div>
      
      <img v-lazy="imgObj">
    </div>

    <div>
      <swiper :options="swiperOption">
        <swiper-slide> <div><img v-lazy="imgObj"></div> 1</swiper-slide>
        <swiper-slide> <div><img v-lazy="imgObj"></div> 2</swiper-slide>
        <swiper-slide> <div><img v-lazy="imgObj"></div> 3</swiper-slide>
        <swiper-slide> <div><img v-lazy="imgObj"></div> 4</swiper-slide>
        
        <swiper-slide>I'm Slide 5</swiper-slide>
        <swiper-slide>I'm Slide 6</swiper-slide>
        <swiper-slide>I'm Slide 7</swiper-slide>
        <div class="swiper-pagination"  slot="pagination"></div>
      </swiper>
    </div>
  </div>
</template>


<script>
  import Vue from 'vue'
  import VueToast from 'vue-toast-notification';
  import 'vue-toast-notification/dist/index.css';
  Vue.use(VueToast);

  import global_ from "../components/Global";
  import 'swiper/dist/css/swiper.css'
  import { swiper, swiperSlide } from 'vue-awesome-swiper'
  export default {
    name: "Test",
    components: {
      swiper,
      swiperSlide
    },
    data() {
      return {
        message: "hello world",
        imgObj:{
          src:'http://5b0988e595225.cdn.sohucs.com/images/20180112/dc9d117c580441eeb8a59fc77b4bef18.jpeg',
          error:'http://ku.90sjimg.com/element_origin_min_pic/00/96/00/1456f2f3374fd63.jpg',
          loading:'https://d2v9y0dukr6mq2.cloudfront.net/video/thumbnail/2T0t-6V/loading-screen-bar-simple-hue-4k-clean-retro-style-loading-text-with-progress-bar-with-color-hue-shift_n1ozc7tx__F0000.png'
        },
        swiperOption:{
          //这里配置的参数参考官网API设置，这里的pagination就是下图中的官方配置
          pagination: {
            el: '.swiper-pagination',
          }
        }
      };
    },
    created() {
      Vue.$toast.open({
        message: 'message string',
        type: 'default',
        duration:1500,
        position: 'top'
      });
    },
    mounted() {
      this.getIndexData();
    },
    watch: {},
    computed: {
    },
    methods: {

      getIndexData(){
        this.$http.get(global_.api+"/indexData",{
          params:{
            id:1233,
          }
        }).then(res => {
          this.message = res.data.channelList;

        }).catch(error => {console.log(error)});
      }

    }
  };



</script>

