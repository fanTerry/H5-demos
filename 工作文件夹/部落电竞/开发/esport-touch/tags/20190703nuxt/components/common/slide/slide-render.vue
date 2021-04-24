<template>
  <div class="slide-render-view">
    <div class="slide-wrapper">
      <div class="slide-content">
        <slide ref="slide" :adList="items" :autoPlay="isAutoPlay" :loop="isLoop" :showDot="isShowDot" :interval="interval" :threshold="threshold" :speed="speed">
          <div v-for="item in items">
            <a :href="item.linkUrl">
              <img :src="item.picUrl" >
            </a>
          </div>
        </slide>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
  import Slide from './slide.vue'
  const COMPONENT_NAME = 'slide-render'
  export default {
    name: COMPONENT_NAME,
    computed: {},
    props:{
      adList:Array,
    },
    data() {
      return {
        index: 1,
        turnToPrev: false,
        turnToNext: false,
        isAutoPlay: true,
        isLoop: true,
        isShowDot: true,
        speed: 400,
        threshold: 0.3,
        interval: 4000,
        items:[],
      }
    },
    methods: {
      updateAutoPlay(val) {
        this.isAutoPlay = val
      },
      updateInterval(val) {
        if (val) {
          this.interval = +val
        }
      },
      updateLoop(val) {
        this.isLoop = val
      },
      updateShowDot(val) {
        this.isShowDot = val
      },
      updateThreshold(val) {
        if (val) {
          this.threshold = +val
        }
      },
      updateSpeed(val) {
        if (val) {
          this.speed = +val
        }
      },
     
    },
    watch: {
      index() {
        this.$refs.slide.update()
      },
      adList(){
      
        for (const item of this.adList) {
          let picItem ={}
          picItem.picUrl = item.picUrl
          picItem.linkUrl = item.location
          picItem.title = item.title
          this.items.push(picItem)
        }

      }
    },
    components: {
      Slide
    }
  }
</script>

<style lang='less'>
  .slide-render-view{
    .slide-wrapper{
      position: relative;
      width: 714px;
      height: 459px;
      margin: 16px auto;
      padding-top: 40%;
      margin-bottom: 10px;
      overflow: hidden;
      background-color: #fff;
	    border-radius: 11px;
      .slide-content{
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        img{
          height: 100%;
        }

      }
    }
    .group{
      margin-bottom: 1rem;
      border: 1px solid rgba(0, 0, 0, .1);
      border-radius: 0.3rem;
      background: #fff;
      .item{
        height: 3.2rem;
        border-bottom: 1px solid rgba(0, 0, 0, .1);
        &.sub{
          font-size: 14px
        }
      }
      .item:last-child{
        border-bottom: none
      }
      .item:nth-child(even){
        background-color: rgba(0,0,0,0.04)
      }
    }
  }
  .free-option{
    .button-container{
      button{
        padding: 5px;
        border-radius: 5px;
        background-color: #fff;
        outline: none;
      }
      .active{
        background-color: #3b99fc;
        border: #fff 1px solid;
        color: #fff;
      }
      .change-button{
        background-color: #3b99fc;
        padding: 5px 10px;
        color: #fff;
      }
    }
  }
</style>
