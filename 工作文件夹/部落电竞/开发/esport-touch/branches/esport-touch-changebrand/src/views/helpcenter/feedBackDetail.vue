<template>
  <div class="Page">
    <header class="mod_header">
      <nav-bar :pageTitle="'反馈详情'"></nav-bar>
    </header>
    <div class="main">
      <fan-kui-list ref="reply" :dataList="myFeedbackList" :type="2"></fan-kui-list>
    </div>
    <footer class="mod_footer">
      <div class="film_handle">
        <div class="comment_input">
          <span class="comment_btn"></span>
          <input type="text" :placeholder="placeholder" @focus="bindFocus($event)" @blur="bindBlur" ref="inputVal" v-model="contentValue" />
          <span class="clean" @click="contentValue = null"></span>
        </div>
        <div class="send_btn" @click="toSendReply(contentValue)">发送</div>
      </div>
    </footer>
  </div>
</template>

<script>
import navBar from '../../components/header/nav_bar/index.vue';
import fanKuiList from './components/fankui';

export default {
  components: {
    navBar,
    fanKuiList
  },
  props: [],
  data() {
    return {
      myFeedbackList: [],
      pageParam: {
        pageNo: 1,
        pageSize: 10
      },
      currPageSize: 10,
      quetionId: null,
      contentValue: '',
      placeholder: ''
    };
  },
  mounted() {
    this.quetionId = this.$route.query.qid;
    let _self = this;
    this.getMyReply(this.pageParam).then(() => {
      setTimeout(() => {
        var end = _self.myFeedbackList[this.myFeedbackList.length - 1];
        console.log('end', end);
        _self.$refs.reply.scrollViewMySend(end.id);
      }, 500);
    });
  },
  methods: {
    getMyReply(param) {
      if (param) {
        param = this.pageParam;
      }
      param.quetionId = this.quetionId;
      return this.$post('/api/helpcenter/detailFeedback', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            let dataList = dataResponse.data;
            this.currPageSize = dataList.length;
            if (dataList && dataList.length > 0) {
              dataList.forEach(element => {
                if (element.imageUrl) {
                  element.imgList = element.imageUrl.split(',');
                }
              });
            }
            this.myFeedbackList = this.myFeedbackList.concat(dataList);

            return dataList;
          } else if (dataResponse.code == '9999') {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          this.$toast('网络异常，稍后再试');
          console.log(error);
        });
    },
    bindFocus(event) {},
    bindBlur() {},
    toSendReply(contentValue) {
      if (!contentValue) {
        this.$toast('发送内容不能为空');
      }
      let param = {};
      param.quetionId = this.quetionId;
      param.content = contentValue;
      return this.$post('/api/helpcenter/userSendReply', param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == '200') {
            let myMessage = dataResponse.data;
            this.myFeedbackList.push(myMessage);
            this.contentValue = '';
            console.log('myMessage.id', myMessage.id);
            this.$nextTick(() => {
              this.$refs.reply.scrollViewMySend(myMessage.id);
            });
            return myMessage;
          } else if (dataResponse.code == '9999') {
            this.$toast(dataResponse.message);
          }
        })
        .catch(error => {
          this.$toast('网络异常，稍后再试');
          console.log(error);
        });
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../assets/common/_base.scss';
@import '../../assets/common/_mixin';

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
    @include getBgImg('../../assets/images/home/comment.png');
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
  background-color: #d13840;
  border-radius: 5px;
}
</style>
