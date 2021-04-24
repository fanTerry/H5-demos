<template>
  <div class="Page comment_pop">
    <header class="mod_header">
      <h3><span class="close" @click="closeComment"></span>{{repliesnNum}}条回复</h3>
    </header>
    <div class="main" id='mainId'>
      <mescroll ref="mescroll" :downLoadAuto="false" :isUseDown="false" @upCallback="onPullingUp"
        @mescrollInit="mescrollInit">

        <!-- <template v-for="(dataItem,index) in commentList" > -->

        <article-discuss-item v-for="(dataItem,index) in commentList" :showType='1' :key="index+'cms1'"
          @refreshComment="refreshComment" :ref="'myChildHot'+dataItem.commentId" :commentItem="dataItem" :hotFlag="0"
          @toReplyFocus="replyFocus" @toDelComment="toDelComment">
        </article-discuss-item>
        <!-- </template> -->
        <div class="replys">
          <div class="title">全部评论</div>
          <!-- <template v-for="(dataItem,index) in subCommentList" > -->
          <article-discuss-item v-for="(dataSubItem,index) in subCommentList" :showType='2' :key="index+'cms2'"
            @refreshComment="refreshComment" :ref="'myChild'+dataSubItem.commentId" :commentItem="dataSubItem"
            :hotFlag="0" @toReplyFocus="replyFocus" @toDelComment="toDelComment">
          </article-discuss-item>
          <!-- </template> -->
        </div>

      </mescroll>
    </div>
    <footer class="mod_footer">
      <!-- <comment-footer :cmsDetail="commentList" ref="inputValFocus" @toSaveComment="saveComment"></comment-footer> -->
      <div class="film_handle">
        <div class="comment_input">
          <span class="comment_btn"></span>
          <input type="text" :placeholder="placeholder" @focus="bindFocus($event)" @blur="bindBlur" ref="inputVal"
            v-model="contentValue" />
          <span class="clean" @click="contentValue = null"></span>
        </div>
        <div class="send_btn" @click="saveComment(contentValue)">发送</div>
        <!-- <div class="feedback_item" v-show="!sendBtnFlag">
          <div :class="{'active':cmsDetail.upFlag}" @click="operateUp">
            <i class="good_icon"></i>
            <span>{{cmsDetail.ups}}</span>
          </div>
          <div>
            <i class="comment_icon"></i>
            <span>{{cmsDetail.comments}}</span>
          </div>
          <div :class="{'active':cmsDetail.favoritesFlag}" @click="operateCollect">
            <i class="collect_icon"></i>
            <span>{{cmsDetail.favorites}}</span>
          </div>
        </div> -->
      </div>
    </footer>
  </div>
</template>

<script>
import articleDiscussItem from "../../../components/article/article-discuss-item.vue";
import commentFooter from "../components/commentfooter.vue";
import mescroll from "../../../components/common/mescroll.vue";
import sessionStorage from "../../../libs/storages/sessionStorage";
import { mapGetters, mapActions } from "vuex";

export default {
  components: {
    articleDiscussItem,
    commentFooter,
    mescroll
  },
  props: ["contentId", "contentTypeId"],
  data() {
    return {
      queryParam: {
        pageNo: 1,
        pageSize: 10,
        contentId: Number, //文章id
        cmsCommentId: Number //一级评论id
      },
      commentList: [], // 一级评论list
      subCommentList: [], //二级以上评论list
      repliesnNum: 0,
      totalPages: 1,
      sendBtnFlag: false,
      contentValue: "",
      showComment: Boolean,
      placeholder: "说点什么...",
      mescroll: null,
      mescrollConfig: {
        warpId: "mainId", //设置置顶时，必须设置父容器ID
        hasToTop: true //默认不开启回到顶部项
      }
    };
  },
  computed: {},

  mounted() {
    this.queryParam.contentId = sessionStorage.get("contentId");
    this.queryParam.cmsCommentId = sessionStorage.get("commentId");
    // let param = {};
    // param = this.queryParam;
    // // this.placeholder = "回复" + this.beCommentUsrName;
    // console.log(param, "进入评论也99");
    this.getPageData(this);
    // this.$refs.mescroll.config = this.mescrollConfig;
   
  },

  methods: {

    toDelComment(commentId,replies) {
    //当在详细页删除评论时
     console.log(commentId, replies,"POP");
    var parentCommentId= this.queryParam.cmsCommentId;
    this.$emit("toDelComment", commentId,replies,parentCommentId);      
    },

    refreshComment(e) {
      console.log(e, 1122);
      this.commentList = [];
      this.subCommentList = [];
      this.queryParam.pageNo = 1;
      if (e == 2) {       
        let param = {};       
        param = this.queryParam;
        console.log(param, "进入评论也2");
        this.getPageData(param);
      }
    },

    bindBlur() {
      setTimeout(function() {
        window.scrollTo(0, 0);
      }, 100);
    },

    /**封装发表评论需要的参数(包括评论和回复) */
    getSaveCommentParam: function(_self, contentValue) {
      var fatherCommentId = _self.replyFatherCommentId;
      console.log(_self.replyFatherCommentId, "fatherCommentId");
      var params = {
        commentLevel: 2, //评论层级
        contentId: this.contentId,
        contentTypeId: this.contentTypeId,
        comment: contentValue,
        commentRootId: this.queryParam.cmsCommentId,
        noShowLoading: true
      };
      if (fatherCommentId) {
        params.commentParentId = fatherCommentId;
      } else {
        params.commentParentId = this.queryParam.cmsCommentId;
      }
      console.log(params, "评论的参数");
      return params;
    },

    /**保存评论 */
    saveComment: function(contentValue) {
      if (contentValue == null || contentValue == "") {
        return;
      }
      console.log(contentValue, "保存");
      var _self = this;
      console.log(_self, "_self");
      var params = _self.getSaveCommentParam(_self, contentValue);
      this.$post("/api/cmsComment/publish", params)
        .then(res => {
          console.log(res.data, "评论返回值");
          if (res.code == "200" && res.data) {
            this.$toast("评论成功", 2);
            _self.contentValue = "";
            // this.placeholder= "说点什么...";
            _self.commentList = [];
            _self.subCommentList = [];
            _self.queryParam.pageNo = 1;
            this.$parent.refreshComment(1);
            _self.getPageData().then(res => {
              _self.mescroll.endSuccess(_self.pageSize, true);
            });
          } else {
            this.$toast("评论失败,稍后重试", 2);
          }
          return null;
        })
        .catch(e => {
          console.log(e);
        });
    },

    bindFocus(e) {
      console.log(e, "获取焦点");
      const originRect = e.target.getBoundingClientRect();
      console.log(originRect, "originRect");
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

    closeComment() {
      this.commentList = [];
      this.subCommentList = [];
      this.queryParam.pageNo = 1;
      this.$parent.closeDetailedComment();
    },

    replyFocus(commentId, commentUsrName) {
      console.log(commentId, commentUsrName, 4444);
      var fatherCommentId = commentId; //要回复的评论
      var replyfathername = commentUsrName;
      this.replyFatherCommentId = commentId;

      this.placeholder = "回复" + replyfathername;
      //this.bindFocus();
      this.$refs.inputVal.focus();
    },

    mescrollInit(mescroll) {
      this.mescroll = mescroll; // 如果this.mescroll对象没有使用到,则mescrollInit可以不用配置
    },
    onPullingUp() {
      console.log(this.totalPages, "this.totalPages");
      if (
        this.queryParam.pageNo != 1 &&
        this.queryParam.pageNo > this.totalPages
      ) {
        this.mescroll.endSuccess(this.queryParam.pageSize, false);
      } else {
        this.getPageData(this).then(res => {
          this.mescroll.endSuccess(this.queryParam.pageSize, true);
        });
      }
    },

    /**获取分页资讯数据 */
    getPageData(_self) {
      var pageNo = this.queryParam.pageNo;
      var pageSize = this.queryParam.pageSize;
      if (pageNo != 1 && pageNo > this.totalPages) {
        return;
      }
      let param = {
        pageNo: pageNo,
        pageSize: pageSize,
        contentId: sessionStorage.get("contentId"),
        cmsCommentId: sessionStorage.get("commentId")
      };

      console.log("分页参数", param);
      return this.$post("/api/cmsComment/list", param)
        .then(rsp => {
          const dataResponse = rsp;
          console.log(dataResponse, "返回的参数");
          if (dataResponse.code == "200") {
            this.totalPages = dataResponse.data.totalPages;
            if (dataResponse.data.commentList != null) {
              this.commentList = dataResponse.data.commentList;
              console.log(this.commentList, "commentList");
              this.repliesnNum = this.commentList[0].replies;
              console.log(this.repliesnNum, "this.repliesnNum");
            }
            if (dataResponse.data.subCommentList != null) {
              this.subCommentList = this.subCommentList.concat(
                dataResponse.data.subCommentList
              );
              console.warn(this.subCommentList, "this.subCommentList");
            }
            this.queryParam.pageNo = this.queryParam.pageNo + 1;
          } else {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  }
};
</script>

<style lang="scss" >
.comment_pop {
  .user_info {
    padding-left: 12px;
  }
}
</style>


<style lang='scss' scoped>
@import "../../../assets/common/_mixin";
@import "../../../assets/common/_base";

.comment_pop {
  position: fixed;
  left: 0;
  top: 0;
  z-index: 1000;
  width: 100%;
  height: 100%;
  background-color: #fff;
  border-radius: 10px 10px 0px 0px;
}
h3 {
  position: relative;
  height: 40px;
  line-height: 40px;
  font-size: 16px;
  color: #333;
  text-align: center;
  font-weight: normal;
  @include getBorder(bottom, #eee);
}
.close {
  @extend .g_v_mid;
  left: 8px;
  @include getClose(13px, #000);
}
.replys {
  @include getBorder(top, #eee);
}

.title {
  padding: 19px 0 15px 12px;
  font-size: 13px;
  color: #333;
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
    @include getBgImg("../../../assets/images/home/comment.png");
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
</style>
