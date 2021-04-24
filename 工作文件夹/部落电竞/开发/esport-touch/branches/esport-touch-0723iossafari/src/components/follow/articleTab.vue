<template>
  <!-- 全部，视频 -->
  <ul class="articleTabPage">
    <!-- 普通列表 -->
    <li class="article_tab " :class="{none:showType==110}" @click="toCmsDetail(item.aricleId,item.type)" v-for="(item,index) in cmsContentList" :key="index">
      <!-- //头像 -->
      <user-info :follower="item.followedUser" @follow="followUser" :type="1" :issueUserId="item.issueUserId"></user-info>
      <!-- 全部，文章 -->
      <!-- 分享和转发的动态 转发的动 态加上dynamic_repeat-->
      <div class="dynamic_share" v-if="item.type==1 || item.type==3 ">
        <!-- 评论专用 -->
        <span class="comment_dialog" v-if="showCommon==1 ||(showCommon== 2 && item.msgType==1)">
          <p class="comment_txt" v-if="item.msgType==1">{{item.currentComment}}</p>
          <span class="user_comment" v-if="item.beComment">
            <span class="user_name" @click="gotoUserHomepage(item.objectUserId,$event)">@{{item.objectUserName}}</span>:<span>{{item.beComment}}</span>
            <span class="separator_tag">//</span>
          </span>
          <span class="article_author" @click="gotoUserHomepage(item.cmsObjectUserId,$event)">@{{item.cmsObjectUserName}}：</span>
        </span>

        <div class="to_someone" v-html="item.detail">
          <!-- <span>#{{item.tag}}#</span> -->
          <!-- {{item.detail}} -->
        </div>

        <img class="adver_img" v-if="item.type==1" :imgurl="item.imageSrc" src='../../assets/images/common/default_img.png' alt="">
        <template v-if="item.type==3&&item.shortArticleList">
          <template v-if="item.shortArticleList.length==1">
            <img class="adver_img" v-for="(img,index) in item.shortArticleList" :key="index" :imgurl="img" src='../../assets/images/common/default_img.png' alt="">
          </template>
          <template v-else>
            <div class="nine_place_img">
              <img class="adver_img" v-for="(img,index) in item.shortArticleList" :key="index" :imgurl="img" src='../../assets/images/common/default_img.png' alt="">
            </div>
          </template>
        </template>
      </div>

      <!-- 视频 -->
      <div class="dynamic_share " v-if="item.type==2||item.type==5">
        <span class="comment_dialog" v-if="showCommon==1 ||(showCommon== 2 && item.msgType==1) ">
          <p class="comment_txt" v-if="item.msgType==1">{{item.currentComment}}</p>
          <span class="user_comment" v-if="item.beComment">
            <span class="user_name" @click="gotoUserHomepage(item.objectUserId,$event)">@{{item.objectUserName}}</span>:<span>{{item.beComment}}</span>
            <span class="separator_tag">//</span>
          </span>
          <span class="article_author" @click="gotoUserHomepage(item.cmsObjectUserId,$event)">@{{item.cmsObjectUserName}}：</span>
        </span>

        <div class="to_someone" v-html="item.detail">
          <!-- <span>#{{item.tag}}#</span> -->
          <!-- {{item.detail}} -->
        </div>
        <!-- <img class="adver_img" v-if="item.type==2" :src="item.titleImg" alt /> -->
        <!--  -->
        <!-- <video class="adver_video" mode="aspectFill" height="200px" v-if="item.type==2" :poster="item.titleImg"
          style="object-fit:fill" :class="'video-'+item.aricleId" @click="playVideo($event,item.aricleId)" controls
          webkit-playsinline="true" x-webkit-airplay="true" playsinline="true" preload="metadata"
          x5-video-player-type="h5" x5-video-orientation="h5" x5-video-player-fullscreen="true">
          <source :src="item.imageSrc">
        </video> -->
        <xi-gua-video v-if="videoType==1" class="adver_video" :videoUrl="item.imageSrc" :poster="item.titleImg" :videoId="item.aricleId" scene='usr_community'>
        </xi-gua-video>
        <xi-gua-video v-else-if="videoType==2" class="adver_video" :videoUrl="item.imageSrc" :poster="item.titleImg" :videoId="item.aricleId" :scene='item.videoSignId'>
        </xi-gua-video>
        <xi-gua-video v-else class="adver_video" :videoUrl="item.imageSrc" :poster="item.titleImg" :videoId="item.aricleId" scene='community'>
        </xi-gua-video>

      </div>

      <!-- 底部发布动态时间 点赞和评论 -->
      <div class="dynamic_num" v-if="!item.msgType">
        <span class="time">{{item.publishTimeStr}}</span>
        <div>
          <div class="item " :class="{'active':item.upFlag}">
            <i @click="operateUp($event,item,index)" class="good_icon"></i><span>{{item.commentNum}}</span>
          </div>
          <div class="item">
            <i class="comment_icon"></i><span>{{item.discussNum}}</span>
          </div>
        </div>
      </div>
      <!--我的消息页面 底部发布动态时间 点赞和评论 -->
      <div class="dynamic_num" v-else>
        <span class="time">{{item.publishTimeStr}}</span>
        <div>
          <div v-if="item.msgType==2 ||item.msgType==4" class="item" :class="{'active':item.upFlag}">
            <i @click="operateUp($event,item,index)" class="good_icon"></i><span>{{item.commentNum}}</span>
          </div>
          <div class="item" v-if="item.msgType==1 ||item.msgType==4">
            <i class="comment_icon"></i><span>{{item.discussNum}}</span>
          </div>
          <div class="item" @click="operateCollect($event,item)" v-if="item.msgType==3" :class="{'active':item.favoritesFlag}"><i class="iconfont collect_icon"></i>{{item.favorites}}</div>
        </div>
      </div>

    </li>
  </ul>
</template>

<script>
import userInfo from '../../components/user_info/index.vue';
import xiGuaVideo from '../../components/common/video.vue';
import sessionStorage from '../../libs/storages/sessionStorage';
import localStorage from '../../libs/storages/localStorage';

export default {
  components: {
    userInfo,
    xiGuaVideo
  },
  props: ['topCmsContentList', 'cmsContentList', 'showType', 'videoType', 'showCommon'],
  data() {
    return {};
  },
  mounted() {
    // console.log('7776' + this.cmsContentList);
    this.$bus.$on('detailFollow', data => {
      this.synFolowHandle(data);
    });
  },
  methods: {
    /**
     * 同步详情页关注状态
     */
    synFolowHandle(data) {
      console.log('关注变更', data);
      this.cmsContentList.forEach(cmsDetail => {
        if (cmsDetail.issueUserId == data.usrId) {
          cmsDetail.followedUser.followStatus = data.type;
        }
      });
      this.topCmsContentList.forEach(cmsDetail => {
        if (cmsDetail.issueUserId == data.usrId) {
          cmsDetail.followedUser.followStatus = data.type;
        }
      });
    },
    toCmsDetail: function(id, type) {
      setTimeout(() => {
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
      }, 0.3 * 1000);
    },

    gotoUserHomepage(userId, e) {
      e.stopPropagation();
      this.$router.push({
        path: '/userCenter/userPublishArticle',
        query: {
          id: userId
        }
      });
    },
    /**点赞/取消赞 */
    operateUp: function(e, cmsDetail, index) {
      e.stopPropagation();
      var _self = this,
        type = 0,
        content = '点赞成功';

      if (cmsDetail.upFlag) {
        type = 0; //当前是赞-->则type是要取消赞
        cmsDetail.commentNum -= 1;
        content = '取消点赞';
        // localStorage.remove("detail_up_" + _self.id);
      } else {
        type = 1;
        cmsDetail.commentNum += 1;
        // localStorage.set("detail_up_" + _self.id, true);
      }
      cmsDetail.upFlag = !cmsDetail.upFlag;
      console.log(cmsDetail);
      this.$post('/api/cmsContent/ups', {
        type: type,
        id: cmsDetail.aricleId
      }).then(res => {
        console.log(res, '操作');
        if (res.code == '200' && res.data) {
          //操作成功
          this.$toast(content);
        } else if (res.code == '5555') {
          console.log('用户未登录');
          this.$router.push({
            name: 'login',
            query: ''
          });
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
    operateCollect(e, cmsDetail) {
      e.stopPropagation();
      let param = {};
      cmsDetail.favoritesFlag = !cmsDetail.favoritesFlag;
      if (cmsDetail.favoritesFlag) {
        cmsDetail.favorites += 1;
        param.status = 1;
      } else {
        if (cmsDetail.favorites > 0) {
          cmsDetail.favorites -= 1;
        }
        param.status = 0;
      }
      param.id = cmsDetail.aricleId;
      return this.$post('/api/cmsContent/favorites', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            console.log('收藏成功');
          }
        })
        .catch(error => {
          this.$toast('网络异常，稍后再试');
          console.log(error);
          return dataResponse.code;
        });
    },
    /**
     * 关注或取关
     */
    followUser(val) {
      console.log('关注参数', val);
      this.updateFollowStatus(val.userId, val.followStatus).then(res => {
        if (res == '200') {
          console.log('res', res);
          this.cmsContentList.forEach(element => {
            if (element.issueUserId == val.userId) {
              // console.log("找到对应的用户");
              element.followedUser.followStatus = val.followStatus;
            }
          });
        }
      });
    },
    updateFollowStatus(userId, status) {
      let param = { type: status, usrId: userId };
      let message = '';
      if (status == 1) {
        message = '关注成功';
        localStorage.set('comment_follow_' + userId, true); //新增关注的usrId
      } else {
        message = '取消成功';
        localStorage.remove('comment_follow_' + userId); //删除取消关注的usrId
      }
      console.log('update-关注', param);
      return this.$post('/api/user/follow', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            this.$toast(message);
            return dataResponse.code;
          }
        })
        .catch(error => {
          this.$toast('网络异常，稍后再试');
          console.log(error);
          return dataResponse.code;
        });
    }
  }
};
</script>


<style lang='scss' scoped>
@import '../../assets/common/_base';
@import '../../assets/common/_mixin';
@import '../../assets/common/_var';
.articleTabPage {
  margin-top: 3.2vw;
  .user_info {
    .tips {
      display: none;
    }
  }
}

.article_tab {
  position: relative;
  margin: 0 4.2667vw 3.2vw;
  padding: 0 3.2vw;
  line-height: 1;
  border-radius: 5px;
  color: #fff;
  background-color: $color_item;
  box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
  // .dynamic_share {
  // }
  .nine_place_img {
    @extend .flex_hc;
    flex-wrap: wrap;
    -webkit-flex-wrap: wrap;
    padding-top: 0.6667vw;
    img {
      width: 26.6667vw;
      height: 26.6667vw;
      margin-right: 2.5333vw;
      object-fit: cover;
      border-radius: 8px;
      &:nth-child(3n) {
        margin-right: 0;
      }
    }
  }

  .comment_txt {
    font-size: 4.2667vw;
    line-height: 5.6vw;
    padding-bottom: 2.6667vw;
    margin-bottom: 2.6667vw;
    @include getBorder(bottom, #ddd);
  }

  .user_comment {
    @extend .flex_hc;
    flex-wrap: wrap;
    -webkit-flex-wrap: wrap;
  }

  .to_someone,
  .user_comment,
  .article_author {
    display: inline;
    font-size: 4vw;
    line-height: 1.2;
    color: #fff;
  }

  .separator_tag {
    font-size: 18px;
    color: #333;
  }

  .to_someone {
    display: initial;
    word-break: break-all;
    span {
      padding-right: 4px;
      color: #0f87ff;
    }
  }

  .user_name,
  .article_author {
    color: #0f87ff;
  }

  .adver_img,
  .adver_video {
    display: block;
    max-width: 100%;
    max-height: 53.3333vw;
    object-fit: contain;
    margin-top: 2.6667vw;
    border-radius: 6px;
    background: rgba(53, 52, 71, 0.5);
  }

  .adver_title {
    @include t_nowrap(100%);
    line-height: 1.2;
    padding-top: 10px;
    font-size: 13px;
  }

  .dynamic_num {
    @extend .flex_v_justify;
    height: 11.7333vw;
    font-size: 3.7333vw;
    color: #fff;
    .item {
      padding-right: 2.1333vw;
      &:last-child {
        padding-right: 0;
      }
      span {
        color: rgba(255, 255, 255, 0.5);
      }
    }
    i {
      display: block;
      width: 4.2667vw;
      height: 4.2667vw;
      margin-right: 1.0667vw;
    }
    div {
      @extend .flex_hc;
    }
    .active {
      span {
        color: $color_yellow;
      }
      .good_icon {
        @include getBgImg('../../assets/images/home/good_active.png');
        background-size: contain;
      }
      .comment_icon {
        @include getBgImg('../../assets/images/home/comment_active.png');
        background-size: contain;
      }
      .collect_icon {
        @include getBgImg('../../assets/images/home/collect_active.png');
        background-size: contain;
      }
    }
  }

  .dynamic_repeat {
    background-color: #efefef;
  }
}
</style>
