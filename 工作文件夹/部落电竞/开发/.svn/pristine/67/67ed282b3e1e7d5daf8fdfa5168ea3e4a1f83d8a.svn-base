<template>
  <div class="">

    <nuxt-link  to="/home/csgo" class="list-wrap" v-for="(article,index) in articleList" :key="index" >
      <div class="list-img" >
        <img :src="article.imageSrc" alt="">
      </div>
      <div class="list-text">
        <p class="text-tag">#{{article.tag | parseNullAndUndefine}}</p>
        <p class="text-word">{{article.detail}}</p>
        <p class="text-num">

          <span><i class="num-icon icon-yanjing"></i><i class="num-number">{{article.ups}}</i></span>
          <span><i class="num-icon icon-pinglun"></i><i class="num-number">{{article.discussNum}}</i></span>
        </p>
      </div>
    </nuxt-link>

  </div>
</template>
<script>
  export default {
    props: {
      articleList:Array,
    },
    created () {
     
    },
    methods: {

    },
    data () {
      return {

      }
    },
    mounted(){
      
    }
  }
</script>
<style lang="less" scoped>
.list-wrap{
  border-top:1px solid #dddddd;
  display:flex;
  width: 718px;
	height: 226px;
  margin: 0 auto;
  padding: 20px 0;
}
.list-img{
  img{
    width: 285px;
    height: 183px;
  }
  margin-right: 26px;
}
.list-text{
  font-family: SourceHanSansSC-Regular;
  flex:1;
  .text-tag{
  	font-size: 18px;
  	color: #a3a3a3;
  }
  .text-word{
    font-size: 26px;
    height: 94px;
    margin:18px 0 10px;
  	line-height: 32px;
  	color: #000000;
  }
  .text-num{
    span{
      margin-right:28px;
    }
    .num-icon{
      font-size:32px;
    }
    .num-number{
      font-size: 24px;
      display: inline-block;
      transform: translateY(-3px);
    }
    i{
      color: #a3a3a3;
    }
  }
}
</style>
