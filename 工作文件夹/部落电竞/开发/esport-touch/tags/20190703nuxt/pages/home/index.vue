<template>
  <section>
    <div class="nav">
      <ul>
        <li v-for='(item, index) in tabs' :class="{active:item.contentType == num}" :key="index"
              @click="tab(item.contentType)" :data-index='index'>
            <span v-if="item">{{item.name}}</span>
        </li>
      </ul>
    </div>
      <div class="contents">
        <!-- <recommend /> -->
        <!-- <videos v-if="num==2"/>
        <legends v-if="num==3"/>
        <kings v-if="num==4"/>
        <survival v-if="num==7"/>
        <dota v-if="num==8"/>
        <csgo v-if="num==10"/> -->
          <section>
            <SlideRender :ad-list="adList"/>
        </section>
      </div>

    <List :article-list='articleList' />
			
    <Footer />
  </section>
</template>

<script>
import recommend from "./recommend"
import videos from "./video"
import legends from "./legends"
import kings from "./kings"
import survival from "./survival"
import dota from "./dota"
import csgo from "./csgo"
import Footer from "~/components/common/footer/index"
import List from "~/components/home/list.vue"
import SlideRender from "~/components/common/slide/slide-render"
import Video from "~/components/home/video.vue"

export default {
  components: {
    recommend,
    videos,
    legends,
    kings,
    survival,
    dota,
    csgo,
    Footer,
    List,
    Video,
    SlideRender,
  },
  data() {
      return {
        num: 1,
        tabs: [],
        adList:[],
        newListParam:{
          pageNo:1,
          pageSize:10,
          contentType:1,
        },
        articleList: [],
      };
  },
  methods:{
    tab(index){
      console.log(index);
      this.num=index
    },
    downCallback(){
      console.log("下拉刷新");
    }

  },
  mounted() {

    //获取菜单栏
    let res = this.$axios.$post('/indexData').then((res)=> {
        console.log(res);
        if (res.code='200') {
          this.tabs = res.data.channelList;
          this.adList = res.data.adList;
        } else {
          console.info(res);
        }
      }).catch(function(error) {
        console.log(error);
      });

    //获取资讯
      let param ={} 
      param.pageNo =  this.newListParam.pageNo;
      param.pageSize =  this.newListParam.pageSize;
      param.contentType =  this.newListParam.contentType;
      this.$axios.$post('/newlist',this.$qs.stringify(param)).then((res) =>{

        if (res.code=='200') {
          this.articleList = res.data;
        } else {
          console.log(res);
        }
      }).catch(function(error) {
        console.log(error);
      });

  },
}
 



</script>

<style lang="less" scoped>
.nav{
  width: 100%;
  overflow: auto;
}
ul {
  display: flex;
  flex-wrap: nowrap;
  flex-shrink: 0;
  width: 450px;
  li {
    display: inline-block;
    color: #818181;
    padding: 23px;
    border-bottom: 1px solid #ddd;
    span {
      display: flex;
      justify-content: center;
      align-items: center;
      position: relative;
      display: block;
      font-size: 26px;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
    }
    &:last-child {
      margin-right: 0;
    }
    &.active {
      color: #ff7e00;
      border-bottom: 1px solid #ff7e00;
    }
  }
}
</style>
