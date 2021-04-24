<template>
  <div class="Page">
    <header class="mod_header">
      <nav-bar :pageTitle="'填写反馈'"></nav-bar>
    </header>
    <div class="main">
      <div class="title_tip">简单描述你的反馈<a @click="toMyFanKui()">我的反馈</a></div>
      <div class="cmt_textarea">
        <div class="textarea_wrap">
          <textarea v-model="submitParam.content" @focus="bindFocus($event)" @blur="scrollToTop" name="" id=""
            maxlength="200" placeholder="请输入10个字以上的问题描述以便我们提供更好的帮助" ref="focusTextarea"></textarea>
          <span>{{submitParam.content.length}}/200</span>
        </div>
      </div>
      <div class="cmt_images">
        <p class="label">上传图片能更好的帮助我们定位问题<span>{{imgs.length}}/{{maxSize}}</span></p>
        <div class="add_container">
          <div class="add_item" style="display:none">
            <img :src="test">
          </div>
          <template v-if='imgs.length>0'>
            <div class="add_item" v-for="(item, i) in imgs" :key='i'>
              <img :src="item">
              <span class="close" @click="del(i,3)"></span>
              <span class="edit">编辑</span>
              <input style="position:absolute;opacity:0;" type="file" @change="fileUpdate(i,$event)" accept="image/*">
            </div>
          </template>
          <div class="add_item" v-if="imgs.length<maxSize">
            <span class="add_icon"></span>
            <img src="" alt="" ref="img">
            <input style="position:absolute;opacity:0;" type="file" id='files' @change="fileChange($event)" ref="input"
              accept="image/*">
          </div>
        </div>
        <!-- <ul class="images">
          <li>
            <img
              src="https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=638833913,3134162988&fm=179&app=42&f=JPEG?w=121&h=140">
            <span href="javascript"></span>
          </li>
          <li>
            <a href="javascript:;" class="btn_add">
              <input style="position:absolute;opacity:0;" type="file" id='files' @change="fileChange($event)"
                ref="input" accept="image/*">
            </a>

          </li>
        </ul> -->
      </div>
      <div class="title_tip">联系电话</div>
      <div class="phone_input">
        <input type="tel" maxlength="11" placeholder="选填，便于我们联系你" v-model="submitParam.phone" @blur="scrollToTop">
      </div>
    </div>
    <footer class="mod_footer">
      <div class="cmt_btns">
        <a @click="submitFeedBack()" class="cmt_btn">提交</a>
        <!-- <a @click="cmtSuccess()" class="cmt_btn">提交</a> -->
      </div>
    </footer>
  </div>
</template>

<script>
import navBar from "../../components/header/nav_bar/index.vue";
import { getCheck } from "../../libs/utils";
export default {
  components: {
    navBar
  },
  props: [],
  data() {
    return {
      submitParam: {
        questionType: "", //问题类型
        content: "", //要提交的内容
        phone: "" //手机号
      },
      imgs: [], // 图片预览地址
      imgfiles: [], // 图片原文件，上传到后台的数据
      maxSize: 3, // 限制上传数量
      test: "",
      repeatClickFlag: false,
      type: 5 //暂时不删除,以防以后会上传视频
    };
  },
  mounted() {
    this.submitParam.questionType = this.$route.query.questionType || 0;
  },
  methods: {
    toMyFanKui() {
      this.$router.push({
        path: "/helpCenter/myFeedBack",
        query: ""
      });
    },
    bindFocus(e) {
      this.$refs.focusTextarea.focus();
    },
    scrollToTop: function() {
      setTimeout(function() {
        window.scrollTo(0, 0);
      }, 100);
    },
    fileUpdate(index, event) {
      //修改图片
      var _this = this;
      var event = event || window.event;
      var file = event.target.files;
      var leng = file.length;
      for (var i = 0; i < leng; i++) {
        if (this.judgeFileSize(file[i])) {
          return;
        }
        var reader = new FileReader(); // 使用 FileReader 来获取图片路径及预览效果
        _this.imgfiles[index] = file[i];
        reader.readAsDataURL(file[i]);
        reader.onload = function(e) {
          _this.imgs[index] = e.target.result; // base 64 图片地址形成预览
          _this.test = e.target.result;
        };
      }
    },
    fileChange() {
      console.log("fileChange");
      // 添加图片
      var _this = this;
      var event = event || window.event;
      var file = event.target.files;
      var leng = file.length;
      for (var i = 0; i < leng; i++) {
        if (this.judgeFileSize(file[i])) {
          return;
        }
        var reader = new FileReader(); // 使用 FileReader 来获取图片路径及预览效果
        _this.imgfiles.push(file[i]);
        reader.readAsDataURL(file[i]);
        reader.onload = function(e) {
          _this.imgs.push(e.target.result); // base 64 图片地址形成预览
        };
      }
    },
    del(i, type) {
      // 根据下标删除
      this.imgfiles.splice(i, 1);
      this.imgs.splice(i, 1);
      console.log(this.imgs);
      console.log(this.imgfiles);
      if (type == 5) {
        this.showVideo = false;
      }
    },
    judgeFileSize(file) {
      var limitSize = this.maxPicSize * 1024 * 1024;
      if (this.type == 5) {
        limitSize = this.maxVideoSize * 1024 * 1024;
      }
      const isLt5M = file.size > limitSize;
      console.log(file.size);
      if (isLt5M && this.type == 3) {
        this.$toast("上传图片大小不能超过" + this.maxPicSize + "MB!");
        return true;
      }
      if (isLt5M && this.type == 5) {
        this.$toast("上传视频大小不能超过" + this.maxVideoSize + "MB!");
        return true;
      }
    },
    submitFeedBack() {
      let _self = this;
      if (_self.submitParam.content == "") {
        _self.$toast("请输入要反馈的内容");
        return;
      }
      if (_self.submitParam.content.length > _self.maxContentLength) {
        _self.$toast("反馈的内容在" + _self.maxContentLength + "字以内");
        return;
      }
      if (
        _self.submitParam.phone &&
        !getCheck.checkPhone(_self.submitParam.phone)
      ) {
        _self.$toast("输入号码格式不正确");
        return;
      }
      if (_self.repeatClickFlag) {
        //正在反馈,不能重复点击
        _self.$toast("正在反馈中,请稍等~");
        return;
      }
      let form = new FormData();
      form.append("content", _self.submitParam.content);
      form.append("phone", _self.submitParam.phone);
      for (var i = 0; i < _self.imgfiles.length; i++) {
        var filePath = _self.imgfiles[i].name;
        var fileFormat = filePath.split(".")[1].toLowerCase();
        form.append("files", _self.imgfiles[i]);
      }
      form.append("isForm", true);
      form.append("questionType", _self.submitParam.questionType);
      _self.repeatClickFlag = true;
      _self
        .$post("/api/helpcenter/submitFeedback", form)
        .then(res => {
          _self.repeatClickFlag = false;
          if (res.code == "200") {
            console.log(res);
            _self.$toast("反馈成功");
            _self.$router.push({
              name: "helpcenter"
            });
          } else {
            _self.$toast(res.message);
          }
        })
        .catch(error => {
          _self.repeatClickFlag = false;
          console.log(error);
        });
    },
    cmtSuccess() {
      this.$router.push({
        path: "/helpCenter/feedBackSuccess"
      });
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../assets/common/_base";
@import "../../assets/common/_mixin";
@import "../../assets/common/_var";
.title_tip {
  position: relative;
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -webkit-align-items: center;
  -ms-flex-align: center;
  align-items: center;
  height: 10.6667vw;
  line-height: 10.6667vw;
  padding: 0 4vw;
  font-size: 3.4667vw;
  color: #666;
}
.title_tip.h90 {
  height: 12vw;
  line-height: 12vw;
}
.title_tip a {
  position: absolute;
  top: 50%;
  right: 4vw;
  margin-top: -2.9333vw;
  color: #e47b32;
  border: 0.2667vw solid #e47b32;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  border-radius: 5.3333vw;
  line-height: 5.8667vw;
  height: 5.8667vw;
  padding: 0 1.8667vw;
}
.cmt_textarea {
  position: relative;
  overflow: hidden;
  border-top: 0.1333vw solid #e1e1e1;
  border-bottom: 0.1333vw solid #e1e1e1;
}
.cmt_images {
  background: #fff;
  margin-top: 2.6667vw;
  padding: 2.6667vw 4vw;
  border-top: 0.1333vw solid #e1e1e1;
  border-bottom: 0.1333vw solid #e1e1e1;
}
.cmt_images .label {
  margin-bottom: 3.3333vw;
  font-size: 3.4667vw;
  color: #666;
}
.cmt_images .label span {
  float: right;
  color: #999;
}

.cmt_images .images li span {
  position: absolute;
  right: -3.4667vw;
  top: -3.4667vw;
  width: 8vw;
  height: 8vw;
  background: url("../../assets/images/help/icon_close.png") no-repeat;
  background-size: 4.2667vw 4.2667vw;
  background-position: center;
}
.phone_input {
  border-top: 0.1333vw solid #e1e1e1;
  border-bottom: 0.1333vw solid #e1e1e1;
}
.phone_input input {
  display: block;
  width: 100%;
  height: 11.4667vw;
  padding-left: 4vw;
  font-size: 3.7333vw;
  color: #333;
  background-color: #fff;
  -webkit-appearance: none;
  -webkit-border-radius: 0;
  border: none;
  outline: none;
}
.phone_input input::-webkit-input-placeholder {
  font-size: 3.7333vw;
  color: #e1e1e1;
}
.textarea_wrap {
  padding: 3.3333vw 4vw;
  background: #fff;
  position: relative;
}
.textarea_wrap textarea {
  width: 100%;
  height: 44vw;
  line-height: 1.2;
  font-size: 3.7333vw;
  color: #333;
  -webkit-appearance: none;
  -webkit-border-radius: 0;
  border: none;
  outline: none;
  resize: none;
}
.textarea_wrap textarea::-webkit-input-placeholder {
  font-size: 3.7333vw;
  color: #e1e1e1;
}
.textarea_wrap span {
  position: absolute;
  right: 4.6667vw;
  bottom: 2.1333vw;
  font-size: 3.4667vw;
  color: #999;
}
.cmt_btns {
  display: -webkit-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  display: flex;
  // position: absolute;
  // left: 0;
  // bottom: 0;
  width: 100%;
}
.cmt_btns .cmt_btn {
  -webkit-box-flex: 1;
  -webkit-flex: 1;
  -ms-flex: 1;
  flex: 1;
  height: 12vw;
  line-height: 12vw;
  text-align: center;
  font-size: 4.5333vw;
  color: #fff;
  background-image: -webkit-linear-gradient(left, #e69e59, #e47b32);
  border: 0 solid transparent;
  -webkit-border-image: -webkit-linear-gradient(#eebb8b, #eca370) 30 30;
  border-image: -webkit-linear-gradient(#eebb8b, #eca370) 30 30;
  border-top-width: 0.1333vw;
}
.btn_add {
  display: block;
  width: 100%;
  height: 100%;
  background: url("../../assets/images/help/icon_add.png") no-repeat;
  background-size: cover;
}
.btn_add input {
  width: 100%;
  height: 100%;
  -webkit-appearance: none;
  -webkit-border-radius: 0;
  border: none;
  outline: none;
  opacity: 0;
}
.add_container {
  @extend .flex_hc;
  flex-wrap: wrap;
  -webkit-flex-wrap: wrap;
}

.add_item {
  position: relative;
  width: 27.4%;
  padding-top: 26.9%;
  margin: 0 0 10px 10px;
  border: 1px dashed #ddd;
  border-radius: 8px;
  input {
    @extend .g_v_c_mid;
    width: 100%;
    height: 100%;
    opacity: 0;
  }
  img,
  video {
    @extend .g_v_c_mid;
    width: 102%;
    height: 102%;
    border-radius: 8px;
    object-fit: cover;
  }
  .close {
    @include getClose(12px, #fff);
    right: 3px;
    top: 3px;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
  }
  .edit {
    position: absolute;
    bottom: 3px;
    left: 3px;
    z-index: 100;
    padding: 0 5px;
    font-size: 13px;
    line-height: 18px;
    color: #fff;
    border-radius: 5px;
    background-color: rgba(0, 0, 0, 0.7);
  }
}
.add_icon {
  @extend .g_v_c_mid;
  width: 24px;
  height: 24px;
  &::before {
    content: "";
    @extend .g_v_mid;
    left: 0;
    width: 100%;
    height: 3px;
    border-radius: 5px;
    background-color: #ddd;
  }
  &::after {
    content: "";
    @extend .g_c_mid;
    top: 0;
    width: 3px;
    height: 100%;
    border-radius: 5px;
    background-color: #ddd;
  }
}
</style>
