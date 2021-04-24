<template>
  <div class="Page orangeGamePage">
    <header class="mod_header">
      <!-- <nav-bar :pageTitle="'首页'" v-if="!adList.length"></nav-bar> -->
      <div class="tab">
        <div :class="{active:tabFlag == index}" @click="switchTab(index)" v-for="(item,index) in tabList" :key="index">
          {{item}}</div>
        <!-- <div>资讯</div>
        <div class="active">推荐</div>
        <div>视频</div> -->
      </div>
    </header>
    <div class="main" id='mainId'>
      <mescroll id="mescroll" :class="{'video_bg':tabFlag == 2}" ref="mescroll" :isShowEmpty="false" @downCallback="downCallback" @upCallback="upCallback" @mescrollInit="mescrollInit">
        <div class="scroll_wrapper">
          <section class="banner" v-if="adList.length >0">
            <banner ref="banner" :adList="adList" v-if="adList.length >0 && isKeep">
            </banner>
          </section>
          <nav-list v-if="tabFlag == 2" :dataList="channelList" @changeTab="relodDataByTab"></nav-list>
          <!-- <section class="hot_game" v-if="centerAdList.length>0">
            <h3 class="title">
              <span>热门游戏</span>
              <a @click="gotoAddress({path: '/community', query: {}})&&MtaH5.clickStat('hot_game_all')">去发现</a>
            </h3>
            <div class="con">
              <div class="list">
                <div class="item" v-for="(item,index) in centerAdList " :key="index">
                  <a :href="item.location" @click="toAddMtaH5(index)">
                    <img :src="item.picUrl" alt="">
                  </a>
                </div>
              </div>
            </div>
          </section> -->
          <section class="infor_list">
            <template v-for="(item,index) in newsList">
              <article class="item" v-if="item.type==1 " @click="toCmsDetail(item.aricleId,item.type)" :key="index">
                <div class="item_left">
                  <div class="title" v-html="item.detail"></div>
                  <div class="user_info">
                    <div class="flex_hc">
                      <img v-if='!judeIsJuzi()' class="user_img" :src="item.authorImg|getDefaultImg('https://rs.esportzoo.com/svn/esport-res/mini/images/default/winplus.png')" alt="">
                      <img v-else class="user_img" src="https://rs.esportzoo.com/svn/esport-res/mini/images/default/juzi.png">
                      <!-- <div> -->
                      <!-- <p class="user_name">{{item.tag}}</p> -->
                      <p class="user_name" v-if='!judeIsJuzi()'>赢加竞技</p>
                      <p class="user_name" v-else>橘子电竞</p>
                      <!-- <span class="publish_time">{{item.publishTimeStr}}</span> -->
                      <!-- </div> -->
                    </div>
                    <div class="visited">
                      <p :class="{'active':item.upFlag}">
                        <i class="good_icon"></i>
                        <span>{{item.ups}}</span>
                      </p>
                      <p :class="{'active':item.discussFlag}">
                        <i class="comment_icon"></i>
                        <span>{{item.discussNum}}</span>
                      </p>
                    </div>
                  </div>
                </div>
                <div>
                  <img class="item_img" :imgurl="item.imageSrc" src="../../assets/images/common/default_img.png" alt />
                </div>
              </article>
              <article class="item film_item" v-else-if="item.type==2" :key="index">
                <div class="film_con">
                  <div class="film_top" @click="toCmsDetail(item.aricleId,item.type)">
                    <div class="film_title" v-html="item.detail"></div>
                    <span class="play_nums">{{item.seeNum}}次播放</span>
                  </div>
                  <!-- <xi-gua-video ref="xiGuaVideo" :videoUrl="item.imageSrc" :poster="item.titleImg" :videoId="item.aricleId" scene="home"> -->
                  <xi-gua-video ref="xiGuaVideo" :videoUrl="item.imageSrc" :poster='require("../../assets/images/common/video_default_image.png")' :videoId="item.aricleId" scene="home">
                  </xi-gua-video>
                </div>
              </article>
            </template>
          </section>

        </div>
      </mescroll>
      <!-- 没有数据时展示 -->
      <no-data v-if="noData" :text="'暂无'+ tabList[tabFlag]" :imgUrl='require("../../assets/images/guess/no_data_icon.png")'>
      </no-data>
    </div>
    <footer class="mod_footer">
      <section class="AQ">
        备案/许可证编号： 琼ICP备20002708号-3
        <!-- <br><br>琼公网安备 46902302000297号 -->
      </section>
      <tabbar></tabbar>
    </footer>
    <loading v-show="isShowLoad"></loading>
    <!-- <pop-agreement></pop-agreement> -->
  </div>
</template>

<script>
import navBar from '../../components/header/nav_bar/index.vue';
import tabbar from '../../components/tabbar/index.vue';
import navList from '../../components/header/nav_list/index.vue';
import userHeader from '../../components/header/user_header/index';
import banner from '../../components/header/banner/swiper.vue';
import Scroll from 'components/common/scroll';
import loading from 'components/common/loading';
import fixScroll from '../../libs/common/fix-scroll-ios';
import xiGuaVideo from '../../components/common/video.vue';
import mescroll from '../../components/common/mescroll.vue';
import popAgreement from '../../components/pop_up/pop_agreement.vue';
import noData from '../../components/no_data/index.vue';

export default {
  components: {
    tabbar,
    userHeader,
    navList,
    banner,
    Scroll,
    loading,
    xiGuaVideo,
    mescroll,
    navBar,
    popAgreement,
    noData
  },
  data() {
    return {
      pageNo: 1,
      pageSize: 10,
      currPageSize: 10,
      clientType: 6, //H5客户端
      newsList: [],
      noDataFlag: false,
      contentType: 1,
      videoTypeIndex: 2, //默认加载全部视频
      adList: [],
      centerAdList: [],
      channelList: [],
      isShowLoad: false,
      currVideo: 0,
      tabList: ['资讯', '推荐', '视频'],
      tabFlag: null,
      noData: false,
      mescroll: null,
      mescrollConfig: {
        warpId: 'mainId', //设置置顶时，必须设置父容器ID
        hasToTop: true //默认不开启回到顶部项
      },
      autoPlayFlag: false, //开启页面视频自动播放标识
      isKeep: false
    };
  },
  created() {
    window.sessionStorage.selectedTab = this.videoTypeIndex;
  },
  mounted() {
    this.doQuiz();
    this.$refs.mescroll.config = this.mescrollConfig;
    $('.loading_wrapper').hide();
    if (window.sessionStorage.topTab) {
      this.contentType = window.sessionStorage.topTab;
      this.tabFlag = window.sessionStorage.topTab;
    } else {
      window.sessionStorage.topTab = this.contentType;
      this.tabFlag = 1;
    }
    // this.getPageData();
    this.setHeader();
    // fixScroll.tofix();
    if (this.$route.query.clientType == 3) {
      //安卓app记录信息
      this.setAndroidId();
    }
    // if (
    //   this.$route.query.clientType == 3 ||
    //   this.$route.query.clientType == 4
    // ) {
    if (this.autoPlayFlag) {
      // 监听滚动条变化
      this.$nextTick(() => {
        this.mutedVideo();
        document.querySelector('#mescroll').addEventListener('scroll', this.handleMescrollY, true);
      });
    }
    // }
  },
  activated() {
    //keepAlive组件激活才会触发的钩子，用于重新获取数据进而保存更新的缓存
    this.$wxApi.wxRegister({
      title: '赢加竞技-电竞玩家平台',
      desc: '全球最新游戏资讯，精彩赛事直播，电竞玩家聚集地。',
      imgUrl: 'http://rs.esportzoo.com/svn/esport-res/mini/images/default/juzi_logo.jpg'
    });
    this.isKeep = true;
  },
  deactivated() {
    this.isKeep = false;
  },
  methods: {
    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
      this.mescroll.setBounce(true); //允许iOS回弹,相当于配置up的isBounce为true
    },
    downCallback() {
      let param = {};
      param.pageNo = 1;
      param.pageSize = this.pageSize;
      param.contentType = this.contentType;
      param.videoTypeIndex = this.videoTypeIndex;
      this.newsList = [];
      this.currPageSize = 10;
      this.getPageData(param).then(() => {
        // this.$refs.scroll.forceUpdate();
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize);
          $('.loading_wrapper').hide();
        });
      });
    },
    upCallback() {
      console.log('加载更多');
      this.loadMore();
    },
    refresh(loaded) {
      console.log('you are refresh');
      this.setHeader();
      this.getPageData().then(() => {
        this.$refs.scroll.forceUpdate();
      });
    },
    loadMore() {
      console.log('you are loadMore');
      this.pageNo += 1;
      let param = {};
      param.pageNo = this.pageNo;
      param.pageSize = this.pageSize;
      param.contentType = this.contentType;
      param.videoTypeIndex = this.videoTypeIndex;
      this.getPageData(param).then(() => {
        // this.$refs.scroll.forceUpdate();
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize);
        });
      });
    },
    /**获取菜单栏 */
    setHeader() {
      let param = {};
      param.contentType = this.contentType;
      // param.clientType = this.clientType;
      this.$post('/api/indexData', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            if (this.tabFlag == 2) {
              if (dataResponse.data.channelList.length > 0) {
                this.channelList = dataResponse.data.channelList;
              }
            } else {
              if (dataResponse.data.centerAdList.length > 0) {
                this.adList = dataResponse.data.centerAdList;
              }
              if (dataResponse.data.adList.length > 0) {
                this.adList = this.adList.concat(dataResponse.data.adList);
              }
            }
            // this.slideText =  this.adList[0].title;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    /**获取分页资讯数据 */
    getPageData(param) {
      if (!param) {
        param = {};
        param.pageNo = 1;
        param.pageSize = 10;
        param.contentType = this.contentType;
        param.videoTypeIndex = this.videoTypeIndex;
      }
      console.log('分页参数', param);
      return this.$post('/api/newlist', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            console.log(dataResponse.data, 'getPageData---请求成功');
            this.currPageSize = dataResponse.data.length;
            if (dataResponse.data.length > 0) {
              this.noData = false;
              let list = dataResponse.data;
              list.forEach(element => {
                if (element.type == 2) {
                  element.imgShow = true;
                }
              });
              this.newsList = this.newsList.concat(list);
              //   this.$nextTick(() => {
              //         this.videHandle();
              // });
            } else {
              if (this.newsList.length <= 0) {
                this.noData = true;
              }
            }
            return this.newsList;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },

    /** 切换顶部tab */
    switchTab(index) {
      if (index == this.tabFlag) {
        return;
      }

      window.sessionStorage.topTab = index;
      if (index == 2) {
        window.sessionStorage.selectedTab = index;
        this.videoTypeIndex = index;
      }
      this.contentType = index;
      this.tabFlag = index;
      this.adList = [];
      this.channelList = [];
      this.isShowLoad = true;

      this.newsList = [];
      this.currPageSize = 10;
      this.pageNo = 1;
      let param = {};
      param.pageNo = this.pageNo;
      param.pageSize = this.pageSize;
      param.contentType = this.contentType;
      param.videoTypeIndex = this.videoTypeIndex;
      this.setHeader();
      this.getPageData(param).then(res => {
        this.isShowLoad = false;
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize);
          $('.upwarp-nodata').hide();
        });
      });
    },

    /**切换tab数据 */
    relodDataByTab(tabId) {
      console.log('tabId---', tabId);
      // window.sessionStorage.selectedTab = tabId;

      this.videoTypeIndex = tabId;
      this.isShowLoad = true;
      this.newsList = [];
      this.currPageSize = 10;
      this.pageNo = 1;
      let param = {};
      param.pageNo = this.pageNo;
      param.pageSize = this.pageSize;
      param.contentType = this.contentType;
      param.videoTypeIndex = this.videoTypeIndex;
      this.getPageData(param).then(res => {
        this.isShowLoad = false;
        this.$nextTick(() => {
          this.mescroll.endSuccess(this.currPageSize);
          /**隐藏底部暂无更多数据 */
          $('.upwarp-nodata').hide();
        });
      });
    },
    /** 跳转详情页 */
    toCmsDetail(id, type) {
      this.$bus.$emit('videoPauseByAll', this.id);
      this.$router.push({
        name: 'homeDetail',
        params: {
          id: id
        },
        query: {
          type: type
        }
      });
    },
    /**视频点击播放 */
    playVideo(event, id) {
      let video = event.currentTarget;
      console.log('-------', video.paused);
      if (video.paused) {
        // console.log("播放-",id);

        let lastVideo = this.currVideo;
        console.log('上一个视频', lastVideo);
        if (this.currVideo === 0) {
          this.currVideo = id;
        } else {
          /**暂停上一个视频 */
          console.log('暂停上一个视频', lastVideo);

          //  $(".video-"+lastVideo).pause()
          let lastVideoEl = document.querySelector('.video-' + lastVideo);
          console.log('暂停上一个视频------', lastVideoEl);
          if (lastVideoEl) {
            lastVideoEl.pause();
          }
          this.currVideo = id;
        }
        video.play();
      } else {
        // console.log("暂停",id);
        video.pause();
        // video.currentTime = 0;
      }
    },
    playVideoImage(event, item) {
      item.imgShow = false;
      this.$nextTick(() => {
        let curr = document.querySelector('.video-' + item.aricleId);
        let lastVideo = this.currVideo;
        console.log('2222', curr);
        if (curr.paused) {
          curr.play();
          if (this.currVideo === 0) {
          } else {
            /**如果有上一个视频，直接暂停上一个再播放 */
            let lastVideoEl = document.querySelector('.video-' + lastVideo);
            if (lastVideoEl) {
              lastVideoEl.pause();
            }
          }
          this.currVideo = item.aricleId;
        } else {
          curr.pause();
        }
      });
    },
    updateView() {
      return this.$post('/api/newlist', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            console.log(dataResponse.data, 'getPageData---请求成功');
            this.currPageSize = dataResponse.data.length;
            if (dataResponse.data.length > 0) {
              this.newsList = this.newsList.concat(dataResponse.data);
            }
            return this.newsList;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    gotoAddress(path) {
      this.$router.push(path);
    },
    setAndroidId() {
      let _self = this;
      document.addEventListener('plusready', function() {
        var mainActivity = plus.android.runtimeMainActivity();
        var Settings = plus.android.importClass('android.provider.Settings');
        var androidId = Settings.Secure.getString(mainActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!androidId) {
          _self.$toast('获取不到ANDROID_ID', 4);
        }
        var flag = window.localStorage.appInfoFlag || false;
        if (!flag) {
          setTimeout(() => {
            _self
              .$post('/api/appClient/addUserClientInfo', {
                deviceId: androidId
              })
              .then(rsp => {
                console.log(rsp);
                if ((rsp.code = '200')) {
                  window.localStorage.appInfoFlag = true;
                }
              })
              .catch(error => {
                console.log(error);
              });
          }, 2 * 1000);
        }
      });
    },
    toAddMtaH5(index) {
      var keyStr = {
        '0': 'hot_game_first',
        '1': 'hot_game_second',
        '2': 'hot_game_third'
      };
      MtaH5.clickStat(keyStr[index]);
    },
    // 监听当视频滑到中间位置时，播放该视频
    handleMescrollY() {
      // console.log(document.getElementById("mescroll").scrollTop);
      this.mutedVideo();
      let clientHeight = document.documentElement.clientHeight;
      let inforList = document.getElementsByClassName('infor_list')[0];
      let topArr = [];
      let heightArr = [];
      for (var i = 0; i < this.newsList.length; i++) {
        topArr[i] = parseInt(inforList.childNodes[i].getBoundingClientRect().top);
        heightArr[i] = parseInt(inforList.childNodes[i].clientHeight);
      }
      for (var j = 0; j < this.newsList.length; j++) {
        let value = parseInt((clientHeight - heightArr[j]) / 2);
        if (topArr[j] - value <= 40 && topArr[j] - value >= -20) {
          this.$refs.xiGuaVideo[j].player.play();
          console.log(document.getElementsByTagName('video'));
          document.getElementsByTagName('video')[j].play();
        }
      }
      // console.log(topArr[0]);
    },

    // 把所有视频都静音
    mutedVideo() {
      let video = document.getElementsByTagName('video');
      for (let k = 0; k < video.length; k++) {
        video[k].muted = true;
        video[k].setAttribute({ 'webkit-playsinline': 'true' });
      }
      console.log(video);
    },
    doQuiz() {
      let url = window.location.href;
      if (url.indexOf('orange.yddzjj66.com') > -1) {
        this.$router.push({
          path: '/guess/home',
          query: {}
        });
      }
    },
    judeIsJuzi() {
      if (location.toString().indexOf('.esportzoo.com') > -1) {
        return true;
      } else {
        return false;
      }
    }
  }
};
</script>

<style lang="scss">
@import '../../assets/common/_base';

.orangeGamePage {
  .back {
    display: none;
  }
  .nav_bar {
    color: #ff7e00 !important;
    font-weight: bold;
  }
  .nav_list {
    margin-top: 2.1334vw;
    height: 6.4vw !important;
    li {
      margin-right: 4vw !important;
      padding: 1.3334vw 1.6vw !important;
    }
    .active {
      border-radius: $border_radius;
      color: #fff !important;
      background-color: #666695;
      &::after {
        display: none;
      }
    }
  }
  .swiper-pagination {
    @extend .flex_hc;
    width: auto;
    left: auto;
    right: 2.1333vw;
  }
  .swiper-pagination-bullet {
    width: 1.6vw;
    height: 1.6vw;
    background-color: rgba(#fff, 0.5);
  }
  .swiper-pagination-bullet-active {
    background-color: #fff;
  }
  .xgplayer-skin-default {
    padding-top: 50.5% !important;
  }
}
</style>


<style lang='scss' scoped>
@import '../../assets/common/_base.scss';
@import '../../assets/common/_mixin.scss';
@import '../../assets/common/iconfont.css';

.mod_header {
  background-color: $color_main;
  .tab {
    @extend .flex_v_h;
    padding: 0 9.3333vw;
    @include getBgLinear(bottom, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.05) 100%);
    background-color: $color_item;
    div {
      position: relative;
      padding: 2.1333vw 8vw 4.2667vw;
      font-size: 3.7333vw;
      line-height: 4.2667vw;
      color: rgba(255, 255, 255, 0.5);
      font-weight: bold;
      text-align: center;
      &.active {
        color: #fff;
        &::after {
          content: '';
          @extend .g_c_mid;
          bottom: 2.1333vw;
          width: 4vw;
          height: 1.0667vw;
          background-color: #fff;
          border-radius: 5px;
        }
      }
    }
  }
}

#mescroll {
  background-color: $color_main;
  &.video_bg .main {
    @include getBgLinear(bottom, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 0.05) 100%);
    background-color: $color_main;
  }
}

.main {
  .banner {
    margin: 3.2vw 4.2667vw;
    padding: 1.0667vw;
    border-radius: $border_radius;
    background-color: $color_item;
    box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  }
}

.hot_game {
  padding: 20px 12px 16px;
  .title {
    @extend .flex_v_justify;
    span {
      font-size: 19px;
      font-weight: bold;
    }
    a {
      font-size: 15px;
      color: $color_main;
      font-weight: normal;
    }
  }
  .con {
    height: 110px;
    margin-top: 8px;
    overflow: hidden;
    .list {
      height: calc(100% + 6px);
      white-space: nowrap;
      overflow: auto;
      -webkit-overflow-scrolling: touch;
    }
    .item {
      display: inline-block;
      margin-right: 8px;
      &:last-child {
        margin-right: 0;
      }
    }
    img {
      width: 250px;
      height: 110px;
      border-radius: 4px;
      object-fit: cover;
    }
  }
}

.infor_list .item {
  @extend .flex_hc;
  margin: 2.1334vw 4.2667vw;
  padding: 3.2vw;
  background-color: $color_item;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  border-radius: $border_radius;
  &.film_item {
    padding: 1.0667vw;
    margin: 2.1334vw 4.2667vw;
  }
  .item_img {
    width: 24vw;
    height: 19.2vw;
    object-fit: cover;
    border-radius: $border_radius;
    background-color: rgba(53, 52, 71, 0.5);
    @media (max-width: 320px) {
      width: 115px;
    }
  }
  .item_left {
    @extend .flex_justify;
    flex-direction: column;
    -webkit-flex-direction: column;
    flex: 1;
    -webkit-flex: 1;
    padding: 1.0667vw 0;
    margin-right: 14px;
  }
  .title {
    font-size: 4vw;
    line-height: 1.2;
    color: #fff;
    text-align: justify;
    font-weight: bold;
    @include line_clamp(2);
  }
  .visited {
    @extend .flex;
    p {
      @extend .flex_hc;
      margin-right: 2.6667vw;
      color: #999;
      font-size: 3.4667vw;
      &:last-child {
        margin-right: 0;
      }
      &.active {
        color: $color_yellow;
        .good_icon {
          @include getBgImg('../../assets/images/home/good_active.png');
        }
        .comment_icon {
          @include getBgImg('../../assets/images/home/comment_active.png');
        }
      }
    }
  }
  .good_icon,
  .comment_icon {
    width: 4.2667vw;
    height: 4.2667vw;
    margin-right: 1.3333vw;
  }
  .good_icon {
  }
  .film_con {
    width: 100%;
    span {
      font-size: 11px;
      color: #a3a3a3;
    }
    .play_nums {
      flex: none;
      -webkit-flex: none;
      font-size: 3.7334vw;
      color: rgba(255, 255, 255, 0.5);
    }
  }
  .film_title {
    font-size: 4vw;
    line-height: 1.2;
    color: #fff;
    font-weight: bold;
    @include t_nowrap(69.3333vw);
  }
  .film_top {
    @extend .flex_v_justify;
    padding: 1.3333vw 2.1333vw 2.6667vw;
  }
}

.user_info {
  @extend .flex_v_justify;
  margin-top: 2.9333vw;
  color: #fff;
  .user_img {
    width: 4.8vw;
    height: 4.8vw;
    margin-right: 1.0667vw;
    border-radius: 4px;
  }
  .user_name {
    @include t_nowrap(100%);
    padding-right: 10px;
    font-size: 3.4667vw;
  }
  .publish_time {
    font-size: 14px !important;
  }
}

.AQ {
  text-align: center;
  margin-top: 10px;
  font-size: 15px;
  color: #999;
  margin-bottom: 5px;
}
</style>
