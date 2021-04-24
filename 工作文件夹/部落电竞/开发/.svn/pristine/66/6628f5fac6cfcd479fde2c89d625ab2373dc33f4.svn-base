<template>
  <div ref="homeDetailPage" class="Page homeDetail_Page" v-if="cmsDetail != null">
    <header class="mod_header">
      <navBar :pageTitle="type==3?'话题详情':type==2?'视频详情':'资讯详情'"></navBar>
      <figure class="figure_film" v-if="type==2">
        <!-- <video @click="playVideo($event,id)" :src="cmsDetail.content" :poster="cmsDetail.titleImg" controls></video> -->
        <xi-gua-video :videoUrl="cmsDetail.content" :poster="cmsDetail.titleImg" :videoId="cmsDetail.id" type='cmsdetail' scene='detail'></xi-gua-video>
        <div class="film_handle">
          <!-- <span class="close_barrage active"></span> -->
          <!-- <span class="open_barrage"></span> -->
          <div class="comment_input">
            <span class="comment_btn"></span>
            <input ref="inputVal" type="text" :placeholder="placeholder" @focus="bindFocus($event)" @blur="bindBlur" v-model="contentValue" />
            <span class="clean" @click="contentValue = null"></span>
          </div>
          <a class="send_btn" @click="saveComment">发送</a>
        </div>
      </figure>
    </header>
    <div class="main" id='mainId'>
      <!-- <scroll ref="scroll" :scrollbar="scrollbar" :pullUpLoad="pullUpLoad" :startY="0" @pullingUp="onPullingUp"> -->
      <mescroll ref="mescroll" :downLoadAuto="false" :isUseDown="false" @upCallback="onPullingUp" @mescrollInit="mescrollInit">
        <section class="mod_userInfo">
          <div class="user_info">
            <div>
              <img preview='0' :src="cmsDetail.authorIcon" alt />
              <div>
                <div class="name" v-if='!judeIsJuzi'>{{cmsDetail.authorName==null?'枫叶电竞':cmsDetail.authorName}}</div>
                <div class="name" else>橘子电竞</div>
                <p class="tips">
                  <span>{{cmsDetail.authorFans}}粉丝</span>
                  <span>{{cmsDetail.views}}阅读</span>
                  <span v-if="type==3" style="color: #818181;">{{cmsDetail.publishTimeStr}}</span>
                </p>
              </div>
            </div>
            <div :class="cmsDetail.followFlag?'followed_btn':'follow_btn'" @click="operateFollow">
              <span v-if="!cmsDetail.followFlag">+关注</span>
              <span v-else>已关注</span>
            </div>
          </div>
          <div class="infor_title" v-if="type!=3">
            <h2 v-html="cmsDetail.title"></h2>
            <p class="tips">
              <span v-if="cmsDetail.channelName !== null">{{cmsDetail.channelName}}</span>
              <span>{{cmsDetail.publishTimeStr}}</span>
            </p>
          </div>
          <div class="parag_message" v-if="type==1">
            <article v-html="cmsDetail.content">{{cmsDetail.content}}</article>
            <div class="qr_code">
              <img preview='1' src="https://rs.esportzoo.com/svn/esport-res/mini/images/icon/erweima.jpg" alt="">
            </div>
            <div class="c_black_50">本文禁止转载或摘编</div>
          </div>
          <div class="parag_message" v-if="type==3">
            <article v-html="cmsDetail.content">{{cmsDetail.content}}</article>
            <template v-if="cmsDetail.shortArticleList">
              <img preview='1' :src="item" v-for="(item,index) in cmsDetail.shortArticleList" :key="index+'img'"
                onerror="this.src='https://static.wanplus.com/data/default/banner.jpg?imageView2/1/w/308/h/208'" />
            </template>

          </div>
          <div class="feedback_item">
            <div :class="{'active':cmsDetail.upFlag}" @click="operateUp">
              <i class="good_icon"></i>
              <span>{{cmsDetail.ups}}</span>
            </div>

            <div>
              <button @click="commentFocus()"></button>
              <!--@click="bindFocus($event)"-->
              <i class="comment_icon"></i>
              <span>{{cmsDetail.comments}}</span>
            </div>
            <div :class="{'active':cmsDetail.favoritesFlag}" @click="operateCollect">
              <i class="collect_icon"></i>
              <span>{{cmsDetail.favorites}}</span>
            </div>
            <!-- <div>
              <i class="share_icon"></i>
              <span>65652</span>
            </div>
            <div>
              <i class="reward_icon"></i>
              <span>65652</span>
            </div> -->
          </div>
          <!-- <div class="game_items">
            <span class="bg_blue">LOL</span>
            <span class="bg_pink">CS</span>
            <span class="bg_orange">刺激战场</span>
          </div>-->
        </section>
        <section class="parag_comments">
          <h2>
            评论
            <span>#{{cmsDetail.comments}}</span>
          </h2>
          <template v-if="upTopCommentList.length>0">
            <div class="item" v-for="(dataItem,index) in upTopCommentList" :key="index+'cms1'">
              <article-discuss-item :ref="'myChildHot'+dataItem.commentId" :commentItem="dataItem" :hotFlag="1" @toReplyFocus="replyFocus"></article-discuss-item>
            </div>
            <div class="more_hot_comments">以上是热门评论</div>
          </template>
          <div class="item" v-for="(dataItem,index) in commentList" :key="index+'cms2'">
            <article-discuss-item :ref="'myChild'+dataItem.commentId" :commentItem="dataItem" :hotFlag="0" @toReplyFocus="replyFocus"></article-discuss-item>
          </div>
        </section>
        <!-- </scroll> -->
      </mescroll>
    </div>
    <footer class="mod_footer" v-if="type != 2">
      <div class="film_handle">
        <div class="comment_input">
          <span class="comment_btn"></span>
          <input type="text" :placeholder="placeholder" @focus="bindFocus($event)" @blur="bindBlur" ref="inputVal" v-model="contentValue" />
          <span class="clean" @click="contentValue = null"></span>
        </div>
        <div class="send_btn" v-show="sendBtnFlag" @click="saveComment">发送</div>
        <div class="feedback_item" v-show="!sendBtnFlag">
          <div :class="{'active':cmsDetail.upFlag}" @click="operateUp">
            <i class="good_icon"></i>
            <span>{{cmsDetail.ups}}</span>
          </div>
          <!-- <div>
            <i class="comment_icon"></i>
            <span>{{cmsDetail.comments}}</span>
          </div> -->
          <div :class="{'active':cmsDetail.favoritesFlag}" @click="operateCollect">
            <i class="collect_icon"></i>
            <span>{{cmsDetail.favorites}}</span>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script>
import localStorage from '../../../libs/storages/localStorage';
import articleDiscussItem from '../../../components/article/article-discuss-item';
import Scroll from 'components/common/scroll';
import { getData } from '../../../libs/dom';
import navBar from '../../../components/header/nav_bar/index';
import xiGuaVideo from '../../../components/common/video.vue';
import mescroll from 'components/common/mescroll.vue';
export default {
  components: {
    articleDiscussItem,
    Scroll,
    mescroll,
    navBar,
    xiGuaVideo
  },
  data() {
    return {
      type: '', //资讯类型:1:长文,2:视频
      cmsDetail: null,
      pageNo: 1,
      pageSize: 10,
      totalPages: 1,
      id: null,
      commentList: [],
      upTopCommentList: [],
      scrollbar: { fade: true },
      pullDownRefresh: { threshold: 90, stop: 40, txt: '刷新成功' },
      pullUpLoad: {
        threshold: 10,
        txt: { more: '加载更多', noMore: '到底啦~' }
      },
      sendBtnFlag: false,
      contentValue: '',
      replyFatherCommentId: null,
      placeholder: '说点什么...',
      isJuzi:false
    };
  },
  computed: {},
  created() {},
  mounted() {
    if (this.$route.query && this.$route.query.type) {
      this.type = this.$route.query.type;
    }
    this.id = this.$route.params.id;
    this.getCmsDetail();
    this.getCommentPage(this);
    this.upTopComment();
    console.log(this.$route.params.id, 'this.id');
  },
  methods: {
    commentFocus() {
      this.$refs.inputVal.focus();
    },
    /**收藏 */
    operateCollect() {
      let param = {};
      this.cmsDetail.favoritesFlag = !this.cmsDetail.favoritesFlag;
      if (this.cmsDetail.favoritesFlag) {
        this.cmsDetail.favorites += 1;
        param.status = 1;
      } else {
        if (this.cmsDetail.favorites > 0) {
          this.cmsDetail.favorites -= 1;
        }
        param.status = 0;
      }
      param.id = this.cmsDetail.id;
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
    replyFocus(commentId, commentUsrName) {
      console.log(commentId, commentUsrName);
      var fatherCommentId = commentId; //要回复的评论
      var replyfathername = commentUsrName;
      this.replyFatherCommentId = commentId;
      this.placeholder = '回复' + replyfathername;
      //this.bindFocus();
      this.sendBtnFlag = true;
      this.$refs.inputVal.focus();
    },
    bindFocus(e) {
      console.log(e, '获取焦点');
      const originRect = e.target.getBoundingClientRect();
      console.log(originRect, 'originRect');
      // let isFirst = false;
      // setTimeout(() => {
      //   let rect = e.target.getBoundingClientRect();
      //   if (isFirst) {
      //     if (rect.top - originRect.top < 0) {
      //       // todo 设置body的height
      //       document.body.style.height =
      //         window.innerHeight - (originRect.top - rect.top) + "px";
      //       e.target.scrollIntoView(false);
      //       return;
      //     }
      //   }
      //   e.target.scrollIntoView(false);
      //   setTimeout(() => {
      //     rect = e.target.getBoundingClientRect();
      //     // 某些机型下，得到的top为负值，直接使用window.innerHeight
      //     if (rect.top > 0) {
      //       document.body.style.height = window.innerHeight + "px";
      //     } else {
      //       document.body.style.height =
      //         window.innerHeight - (originRect.top - rect.top) + "px";
      //     }
      //     e.target.scrollIntoView(false);
      //   }, 100);

      //   isFirst = false;
      // }, 100);

      this.sendBtnFlag = true;
      this.$refs.inputVal.focus();
      if (this.type == 2) {
        setTimeout(() => {
          // window.scrollTo(0, 0);
          document.body.scrollTop = 0;
        }, 100);
      } else {
        setTimeout(() => {
          document.body.scrollTop = document.body.offsetHeight;
        }, 100);
      }
    },
    bindBlur() {
      setTimeout(function() {
        window.scrollTo(0, 0);
      }, 100);
    },
    /**封装发表评论需要的参数(包括评论和回复) */
    getSaveCommentParam: function(_self) {
      var fatherCommentId = _self.replyFatherCommentId;
      var content = _self.contentValue;
      var params = {
        commentLevel: 1, //评论层级
        contentId: _self.id,
        contentTypeId: _self.cmsDetail.typeId,
        comment: content,
        noShowLoading: true
      };
      if (fatherCommentId) {
        params.commentParentId = fatherCommentId;
        params.commentRootId = fatherCommentId;
        params.commentLevel = 2;
      }
      console.log(params, '评论的参数');
      return params;
    },
    /**保存评论 */
    saveComment: function(e) {
      console.log(e, '保存');
      var _self = this;
      var fatherCommentId = this.replyFatherCommentId;
      var params = _self.getSaveCommentParam(_self);
      var curCmsDetail = _self.cmsDetail;
      this.$post('/api/cmsComment/publish', params)
        .then(res => {
          console.log(res.data.commentId, '评论返回值');
          if (res.code == '200' && res.data) {
            this.$toast('评论成功', 2);
            if (!fatherCommentId) {
              //一级评论刷新评论列表,拿第一页渲染
              curCmsDetail.comments += 1;
              _self.contentValue = '';
              _self.replyFatherCommentId = null;
              _self.commentList = [];
              _self.pageNo = 1;
              _self.cmsDetail = curCmsDetail;
              _self.getCommentPage(_self).then(res => {
                _self.mescroll.endSuccess(_self.pageSize, true);
              });
            } else {
              // console.log(fatherCommentId, "二级评论怎么处理");
              // console.log(this.$refs);
              var myChildVal = 'myChild' + fatherCommentId;
              var myChildHotVal = 'myChildHot' + fatherCommentId;
              //console.log(this.$refs[myChildVal]);
              this.$refs[myChildVal][0].getData(res.data);
              if (this.$refs[myChildHotVal]) {
                this.$refs[myChildHotVal][0].getData(res.data);
              }
              _self.contentValue = '';
              _self.replyFatherCommentId = null;
              _self.placeholder = '说点什么...';
            }
          } else {
            this.$toast('评论失败,稍后重试', 2);
          }
          return null;
        })
        .catch(e => {
          console.log(e);
        });
    },
    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
    },
    onPullingUp() {
      // if (this.pageNo > this.totalPages) {
      //   this.$refs.scroll.forceUpdate();
      // } else {
      //   this.getCommentPage(this).then(res => {
      //     this.$refs.scroll.forceUpdate();
      //   });
      // }
      if (this.pageNo != 1 && this.pageNo > this.totalPages) {
        this.mescroll.endSuccess(this.pageSize, false);
      } else {
        this.getCommentPage(this).then(res => {
          this.mescroll.endSuccess(this.pageSize, true);
        });
      }
    },
    getCmsDetail() {
      this.$post('/api/cmsContent/detail/nologin/' + this.id)
        .then(dataResponse => {
          console.log(dataResponse, 'dataResponse');
          if (dataResponse.code == '200') {
            this.cmsDetail = dataResponse.data;
            if (this.type == 2 && this.cmsDetail.content.indexOf('esportzoo') == -1) {
              this.cmsDetail.content = window.location.host + this.cmsDetail.content;
              console.log('视频地址', this.cmsDetail.content);
            }
            // console.log(window.location.host);
            //当标题为空的时候，填充这个
            let titleShare = '枫叶电竞欢迎你';
            if (this.cmsDetail.typeId == 5 && this.cmsDetail.title) {
              titleShare = this.cmsDetail.title.replace(/(<\/?a.*?>.*<\/a>)|(<\/?span.*?>)/g, '');
            } else if (this.cmsDetail.typeId == 3 && this.cmsDetail.content) {
              console.log(this.cmsDetail.content);
              //去掉a标签，去掉回车换行
              titleShare = this.cmsDetail.content
                .replace(/(<\/?a.*?>.*<\/a>)|(<\/?span.*?>)/g, '')
                .replace(/[\r\n]/g, '')
                .substring(0, 20);
              console.log(titleShare);
            } else if (this.cmsDetail.title) {
              titleShare = this.cmsDetail.title;
            }
            this.$wxApi.wxRegister({
              title: titleShare,
              desc: '枫叶电竞，全球最新游戏资讯，精彩赛事直播，电竞玩家聚集地。',
              imgUrl: this.cmsDetail.titleImg || 'https://rs.esportzoo.com/svn/esport-res/mini/images/default/juzi_logo.jpg'
            });
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    getCommentPage(_self) {
      var pageNo = _self.pageNo;
      var pageSize = _self.pageSize;
      if (pageNo != 1 && pageNo > _self.totalPages) {
        return;
      }
      let param = {
        pageNo: pageNo,
        pageSize: pageSize,
        contentId: _self.id //当前资讯id
      };
      return this.$post('/api/cmsComment/list/nologin', param)
        .then(res => {
          if (res.code == '200') {
            var resData = res.data.commentList;
            _self.totalPages = res.data.totalPages;
            if (_self.pageNo == 1) {
              _self.commentList = resData;
            } else {
              _self.commentList = _self.commentList.concat(resData);
            }
            _self.pageNo = _self.pageNo + 1;
          }
        })
        .catch(e => {
          console.error(e);
        });
    },
    /**关注/取消关注用户 */
    operateFollow: function() {
      var _self = this,
        type = 0,
        content = '关注成功';
      var toFollowUsrId = _self.cmsDetail.authorId;
      // var curUsrId = app.getGlobalUserInfo().usrId;
      // console.log(app.getGlobalUserInfo(), toFollowUsrId)
      // if (curUsrId === toFollowUsrId) {
      //   this.$toast("您不能关注自己", 3);
      //   return;
      // }
      var curCmsDetail = _self.cmsDetail;
      if (curCmsDetail.followFlag) {
        type = 0; //当前是关注-->则type是要取消关注
        content = '取消关注';
      } else {
        type = 1;
      }
      let param = {
        type: type,
        usrId: toFollowUsrId
      };
      this.$post('/api/user/follow', param)
        .then(res => {
          if (res.code == '200' && res.data) {
            //操作成功
            this.$toast(content, 2);
            console.log(content);
            if (curCmsDetail.followFlag) {
              localStorage.remove('detail_follow_' + _self.cmsDetail.authorId); //删除取消关注的usrId
              if (curCmsDetail.authorFans > 0) {
                curCmsDetail.authorFans -= 1;
              }
            } else {
              localStorage.set('detail_follow_' + _self.cmsDetail.authorId, true); //新增关注的usrId
              curCmsDetail.authorFans += 1;
            }
             this.$bus.$emit("detailFollow", curCmsDetail.followFlag);
            curCmsDetail.followFlag = !curCmsDetail.followFlag;
            _self.cmsDetail = curCmsDetail;
          }
        })
        .catch(e => {});
    },
    /**点赞/取消赞 */
    operateUp: function() {
      var _self = this,
        type = 0,
        content = '点赞成功';
      var curCmsDetail = _self.cmsDetail;
      if (curCmsDetail.upFlag) {
        type = 0; //当前是赞-->则type是要取消赞
        if (curCmsDetail.ups > 0) {
          curCmsDetail.ups -= 1;
        }
        content = '取消点赞';
        localStorage.remove('detail_up_' + _self.id);
      } else {
        type = 1;
        curCmsDetail.ups += 1;
        localStorage.set('detail_up_' + _self.id, true);
      }
      curCmsDetail.upFlag = !curCmsDetail.upFlag;
      console.log(_self.cmsDetail);
      this.$post('/api/cmsContent/ups', { type: type, id: _self.id }).then(res => {
        console.log(res, '操作');
        if (res.code == '200' && res.data) {
          //操作成功
          this.$toast(content);
          _self.cmsDetail = curCmsDetail;
        }
      });
    },
    /**热门评论 */
    upTopComment: function() {
      var _self = this;
      this.$post('/api/cmsComment/upTopComment/nologin', {
        contentId: _self.id
      })
        .then(res => {
          console.log(res, '置顶评论');
          if (res.code == '200' && res.data) {
            _self.upTopCommentList = res.data;
          } else {
            this.$toast('查询热门评论失败,稍后重试', 2000);
          }
          return null;
        })
        .catch(e => {
          console.log(e);
        });
    },
    /**视频点击播放 */
    playVideo(event, id) {
      let video = event.currentTarget;
      if (video.paused) {
        // console.log("播放-",id);
        video.play();
        if (this.currVideo === 0) {
          this.currVideo = id;
        } else {
          /**暂停上一个视频 */
          //  console.log("暂停上一个视频",this.currVideo);
          document.querySelector('.video-' + this.currVideo).pause();
          this.currVideo = id;
        }
      } else {
        // console.log("暂停",id);
        video.pause();
      }
    },
    judeIsJuzi(){
      if(location.toString().indexOf(".esportzoo.com") > -1){
        this.isJuzi=true;
      }
    }
  },
  watch: {}
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_base.scss';
@import '../../../assets/common/_mixin.scss';
@import '../../../assets/common/_var.scss';

a {
  color: #679dfa;
  text-decoration: underline;
}
.mod_header {
  .figure_film {
    position: relative;
    // font-size: 0;
  }
  video {
    width: 100%;
    height: 56vw;
    object-fit: cover;
  }
  .more {
    position: absolute;
    right: 5px;
    top: 5px;
    width: 40px;
    height: 40px;
    @include getBgImg('../../../assets/images/home/more.png');
  }
  .send_btn {
    width: 48px;
    height: 24px;
    font-size: 13px;
    color: #fff;
    border-radius: 24px;
    text-decoration: none;
  }
  .film_handle {
    @extend .flex_hc;
    padding: 5px 10px;
    background-color: #040303;
  }
  .comment_input {
    height: 24px;
    background-color: #3c3c3c;
    border-radius: 24px;
    input {
      color: #fff;
      font-size: 13px;
    }
    .comment_btn {
      width: 16px;
      height: 16px;
    }
  }
  .close_barrage,
  .open_barrage {
    width: 17px;
    height: 17px;
    margin: 0 10px;
  }
  .close_barrage {
    @include getBgImg('../../../assets/images/home/ba_close.png');
    &.active {
      @include getBgImg('../../../assets/images/home/ba_close_active.png');
    }
  }
  .open_barrage {
    @include getBgImg('../../../assets/images/home/ba_open.png');
    &.active {
      @include getBgImg('../../../assets/images/home/ba_open_active.png');
    }
  }
}

.feedback_item {
  @extend .flex_h_avg;
  padding: 15px 0;
  font-size: 14px;
  color: #818181;
  text-align: center;
  .active {
    color: $color_main !important;
    .good_icon {
      @include getBgImg('../../../assets/images/home/good_active.png');
    }
    .comment_icon {
      @include getBgImg('../../../assets/images/home/comment_active.png');
    }
    .collect_icon {
      @include getBgImg('../../../assets/images/home/collect_active.png');
    }
    .share_icon {
      @include getBgImg('../../../assets/images/home/share_active.png');
    }
    .reward_icon {
      @include getBgImg('../../../assets/images/home/reward_active.png');
    }
  }
  > div {
    position: relative;
    color: #818181;
  }
  button {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
  }
  i {
    display: block;
    margin: 0 auto;
    width: 30px;
    height: 30px;
  }
  span {
    display: block;
    padding-top: 4px;
  }
}

.film_handle {
  @extend .flex_v_justify;
  padding: 10px;
  .feedback_item {
    padding: 0;
    font-size: 14px;
    > div {
      padding: 0 8px;
      color: #818181;
    }
    i {
      width: 26px;
      height: 26px;
    }
  }
}

.comment_input {
  flex: 1;
  -webkit-flex: 1;
  position: relative;
  height: 38px;
  margin-right: 10px;
  background-color: #f0efee;
  border-radius: 5px;
  input {
    width: 100%;
    height: 100%;
    padding: 0 35px;
    font-size: 16px;
    color: #333;
    border: none;
  }
  input::-webkit-input-placeholder {
    color: #aeaeae;
  }
  .comment_btn {
    @extend .g_v_mid;
    left: 10px;

    width: 20px;
    height: 20px;
    @include getBgImg('../../../assets/images/home/comment.png');
  }
  .clean {
    @extend .g_v_mid;
    right: 10px;
    @include getClose(9px, #999);
    border: 1px solid #999;
    border-radius: 50%;
  }
}

.send_btn {
  @extend .flex_v_h;
  width: 58px;
  height: 38px;
  font-size: 17px;
  color: #fff;
  background-color: #ff7e00;
  border-radius: 5px;
}

.main {
  section {
    padding: 0 10px;
    margin-bottom: 10px;
    background-color: #fff;
  }
  .infor_title {
    position: relative;
    h2 {
      font-size: 18px;
      line-height: 25px;
      color: #000;
      @include line_clamp(2);
    }
    .tips span {
      margin-top: 6px;
      padding-right: 35px;
      color: #818181;
    }
  }

  .game_items {
    padding: 8px 0;
    font-size: 0;
    @include getBorder(top, #ddd);
    span {
      display: inline-block;
      padding: 4px 8px;
      margin-right: 8px;
      color: #fff;
      font-size: 10px;
      border-radius: 4px;
    }
  }
}

.mod_footer {
  background-color: #fff;
}
</style>


// 覆盖articleDiscussItem组件里面的样式
<style lang='scss'>
@import '../../../assets/common/_base.scss';
@import '../../../assets/common/_mixin.scss';
@import '../../../assets/common/_var.scss';
.homeDetail_Page {
  .user_info {
    @extend .flex_v_justify;
    padding: 15px 0 12px 0;
    > div {
      @extend .flex_hc;
    }
    img {
      width: 30px;
      height: 30px;
      margin-right: 10px;
      border-radius: 50%;
    }
    .name {
      font-size: 13px;
      padding-bottom: 5px;
      font-weight: bold;
      color: #333;
    }
    .tips {
      font-size: 0;
      span {
        padding-right: 10px;
        font-size: 10px;
        color: #818181;
      }
    }
  }
  .parag_message {
    padding-bottom: 20px;
    font-size: 16px;
    article {
      line-height: 24px;
    }
    p {
      padding-bottom: 15px;
      font-size: 16px;
      line-height: 24px;
      color: #000;
    }
    img {
      width: 100%;
      margin-top: 10px;
      border-radius: 6px;
    }
    .qr_code {
      margin: 0 auto;
      padding-bottom: 10px;
      text-align: center;
      img {
        width: 150px;
        height: 150px;
        object-fit: cover;
        border-radius: 0;
        background: url('https://rs.esportzoo.com/svn/esport-res/mini/images/icon/erweima.jpg') no-repeat center;
        background-size: contain;
      }
    }
  }

  .parag_comments {
    padding-bottom: 20px !important;
    h2 {
      padding-top: 20px;
      padding-bottom: 5px;
      font-size: 14px;
      color: #000;
      span {
        padding-left: 10px;
        font-size: 10px;
        color: $black_50;
      }
    }
    .user_info {
      padding-top: 12px;
      padding-bottom: 10px;
    }
    .con {
      padding: 0 20px 0 38px;
      .txt {
        color: #3b3b3b;
        font-size: 16px;
        line-height: 20px;
      }
    }
    .feedback_item {
      @extend .flex_hc;
      padding: 14px 0 16px;
      > div {
        @extend .flex_hc;
        position: relative;
        margin-right: 24px;
        color: #818181;
      }
      .active {
        color: $color_main;
        .good_icon {
          @include getBgImg('../../../assets/images/home/good_active.png');
        }
      }
      button {
        position: absolute;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
      }
      i {
        width: 26px;
        height: 26px;
      }
      span {
        padding: 0;
        font-size: 14px;
      }
    }
    .follow_comment {
      padding: 10px 28px 10px 15px;
      background-color: #efefef;
      border-radius: 5px;
      p {
        padding-bottom: 8px;
        font-size: 16px;
        line-height: 22px;
        color: #333;
        &:last-child {
          padding-bottom: 0;
        }
        span {
          color: #999;
        }
      }
      .toggle_txt {
        @extend .flex_v_h;
        position: relative;
        font-size: 14px;
        color: #999;
        text-align: center;
        &::after {
          content: '';
          display: block;
          width: 5px;
          height: 5px;
          margin-left: 10px;
          border: solid #999;
          border-width: 0 1px 1px 0;
          transform: rotate(45deg);
          -webkit-transform: rotate(45deg);
          transform-origin: center right;
          -webkit-transform-origin: center right;
        }
        &.up {
          &::after {
            transform: rotate(225deg);
            -webkit-transform: rotate(225deg);
          }
        }
      }
    }
    .more_hot_comments {
      @extend .flex_hc;
      color: $color_main;
      &::after,
      &::before {
        content: '';
        flex: 1;
        height: 1px;
        background-color: #ddd;
        transform: scaleY(0.5);
      }
    }
  }
}
</style>
