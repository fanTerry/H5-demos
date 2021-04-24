<template>
  <div class="film_handle">
    <div class="comment_input">
      <span class="comment_btn"></span>
      <input type="text" :placeholder="placeholder" @focus="bindFocus($event)" @blur="bindBlur" ref="inputVal"
        v-model="contentValue" />
      <span class="clean" @click="contentValue = null"></span>
    </div>
    <div class="send_btn" v-if="sendBtnFlag" @click="toSaveComment(contentValue)">发送</div>
    <div class="feedback_item" v-if="!sendBtnFlag">
      <div :class="{'active':cmsDetail.upFlag}" @click="toOperateUp">
        <i class="good_icon"></i>
        <span>{{cmsDetail.ups}}</span>
      </div>
      <!-- <div>
            <i class="comment_icon"></i>
            <span>{{cmsDetail.comments}}</span>
          </div> -->
      <div :class="{'active':cmsDetail.favoritesFlag}" @click="toOperateCollect">
        <i class="collect_icon"></i>
        <span>{{cmsDetail.favorites}}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

export default {

  components: {},
  props: ["cmsDetail" ],
  data() {
    return {
      sendBtnFlag: false,
      contentValue: "",
      showComment: Boolean,
      placeholder: "说点什么..."
    };
  },
  computed: {
    ...mapGetters({
      getCommentInfo: "getCommentInfo", // 获取要发布的参数      
    })
  },
  methods: {
    ...mapActions(["setCommentInfo", "setCommentInfo"]),
    toSaveComment(contentValue){
      this.setCommentInfo({
        ...this.getCommentInfo,
        contentValue: contentValue,
      });
      this.$emit("toSaveComment",contentValue);

    },
    toOperateUp(){
      this.$emit("toOperateUp");

    },
    toOperateCollect(){
      this.$emit("toOperateCollect");

    },
    toSendBtnFlag(){
      console.log("55555");
      this.sendBtnFlag=true;
    },

    bindFocus(e) {
      console.log(e, "获取焦点");
      const originRect = e.target.getBoundingClientRect();
      console.log(originRect, "originRect");
      this.sendBtnFlag = true;
      // this.$refs.inputVal.focus();
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
    replyFocus(commentId, commentUsrName) {
      console.log(commentId, commentUsrName);
      var fatherCommentId = commentId; //要回复的评论
      var replyfathername = commentUsrName;
      this.replyFatherCommentId = commentId;
      this.placeholder = "回复" + replyfathername;
      //this.bindFocus();
      this.sendBtnFlag = true;
      this.$refs.inputVal.focus();
    },
    /**保存评论 */
    saveComment: function(e) {
      console.log(e, "保存");
      var _self = this;
      var fatherCommentId = this.replyFatherCommentId;
      var params = _self.getSaveCommentParam(_self);
      var curCmsDetail = _self.cmsDetail;
      console.log(params,996);      
      this.$post("/api/cmsComment/publish", params)
        .then(res => {
          console.log(res.data, "评论返回值");
          if (res.code == "200" && res.data) {
            this.$toast("评论成功", 2);
            if (!fatherCommentId) {
              //一级评论刷新评论列表,拿第一页渲染
              curCmsDetail.comments += 1;
              _self.contentValue = "";
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
              var myChildVal = "myChild" + fatherCommentId;
              var myChildHotVal = "myChildHot" + fatherCommentId;
              //console.log(this.$refs[myChildVal]);
              this.$refs[myChildVal][0].getData(res.data);
              if (this.$refs[myChildHotVal]) {
                this.$refs[myChildHotVal][0].getData(res.data);
              }
              _self.contentValue = "";
              _self.replyFatherCommentId = null;
              _self.placeholder = "说点什么...";
            }
          } else {
            this.$toast("评论失败,稍后重试", 2);
          }
          return null;
        })
        .catch(e => {
          console.log(e);
        });
    },
    /**封装发表评论需要的参数(包括评论和回复) */
    getSaveCommentParam: function(_self) {
      console.log(_self, "886");
      var fatherCommentId = _self.replyFatherCommentId;
      console.log(fatherCommentId, "7766");
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
      console.log(params, "评论的参数");
      return params;
    },


    /**点赞/取消赞 */
    operateUp: function() {
      var _self = this,
        type = 0,
        content = "点赞成功";
      var curCmsDetail = _self.cmsDetail;
      if (curCmsDetail.upFlag) {
        type = 0; //当前是赞-->则type是要取消赞
        if (curCmsDetail.ups > 0) {
          curCmsDetail.ups -= 1;
        }
        content = "取消点赞";
        localStorage.remove("detail_up_" + _self.id);
      } else {
        type = 1;
        curCmsDetail.ups += 1;
        localStorage.set("detail_up_" + _self.id, true);
      }
      curCmsDetail.upFlag = !curCmsDetail.upFlag;
      console.log(_self.cmsDetail);
      this.$post("/api/cmsContent/ups", { type: type, id: _self.id }).then(
        res => {
          console.log(res, "操作");
          if (res.code == "200" && res.data) {
            //操作成功
            this.$toast(content);
            _self.cmsDetail = curCmsDetail;
          }
        }
      );
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
      return this.$post("/api/cmsContent/favorites", param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            console.log("收藏成功");
          }
        })
        .catch(error => {
          this.$toast("网络异常，稍后再试");
          console.log(error);
          return dataResponse.code;
        });
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../../assets/common/_base.scss";
@import "../../../assets/common/_mixin";


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
