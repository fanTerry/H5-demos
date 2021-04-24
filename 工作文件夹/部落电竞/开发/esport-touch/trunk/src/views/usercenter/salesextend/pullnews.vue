<!--
 * @Author: your name
 * @Date: 2020-07-31 17:11:05
 * @LastEditTime: 2020-08-05 17:17:53
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch/src/views/usercenter/salesextend/pullnews.vue
--> 
<template>
  <div class='Page pullNewsPage'>
    <header class='mod_header'>
      <nav-bar :pageTitle='"专属拉新海报"'></nav-bar>
    </header>
    <div class='main'>
      <ul class="list">
        <!-- <li class="item" v-for="(item,index) in adList" :key="index">
          <img :src="item.picUrl" alt="">
          <a @click="screenshotsImg">点击下载</a> -->

        <li class="item" v-for="(item,index) in adList" :key="index">
          <div class="poster" :style="'background-image:url(' + item.picUrl+')'">
            <img :src="qrCode" alt="">
            <p class="tips">财运好不好，全在这一秒</p>
          </div>
          <a @click="handlePic(item.picUrl)">点击下载</a>
        </li>
      </ul>
    </div>

    <div id="ui_pop" class="ui_pop" v-show="showDialog" :style="'background-image:url(' +  selectedPic+')'">
      <div class="qr_code">
        <img :src="qrCode" alt="">
        <p class="tips">财运好不好，全在这一秒</p>
      </div>
    </div>

    <div class="ui_pop screen_shot"  v-show="screemDialog"  >
      <div class="con">
        <a class="close"></a>
        <img :src="screemImageUrl" alt="">
      </div>
    </div>

  </div>
</template>

<script>
import navBar from '../../../components/header/nav_bar/index.vue';
import domtoimage from 'dom-to-image';
import html2canvas from 'html2canvas';

export default {
  components: { navBar },
  props: [],
  data() {
    return {
      adList: [],
      qrCode: null,
      showDialog:false,
      screemDialog:false,
      selectedPic:null,
      screemImageUrl:null,
    };
  },
  mounted() {
    this.getSalePic();
  },
  methods: {
    handlePic(picUrl){
      this.showDialog = true;
      this.selectedPic = picUrl;
      this.canvasToImg();

    },
    getSalePic() {
      let param = {};
      param.promotionType = 13;
      this.$post('/api/quizSale/salePromotion', param)
        .then(rsp => {
          if (rsp.code == 200) {
            this.adList = rsp.data.adList;
            this.qrCode = rsp.data.qrCode;
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    canvasToImg() {
      let _self = this;
      setTimeout(() => {
        html2canvas(document.getElementById('ui_pop'), {
          useCORS: true ,//（图片跨域相关）
          allowTaint: true //允许跨域（图片跨域相关）
          // taintTest: true //是否在渲染前测试图片
          // width: document.getElementById("my-node").clientWidth, //显示的canvas窗口的宽度
          // height: document.getElementById("my-node").clientHeight //显示的canvas窗口的高度
          // windowWidth: document.body.scrollWidth, // 获取X轴方向滚动条中的内容
          // windowHeight: document.body.scrollHeight, // 获取Y轴方向滚动条中的内容
          // x: 0 // 页面在X轴上没有滚动，故设为0
          // y: window.pageYOffset // 页面在垂直方向的滚动距离
        }).then(function(canvas) {
          _self.screemImageUrl = canvas.toDataURL('image/png');
         _self.showDialog = false;
         _self.screemDialog = true;
          console.log(_self.showDialog);
        });
      }, 1000);
    },
    screenshotsImg() {
      // this.dialogShow = true;
      let self = this;
      this.$nextTick(() => {
        //ios第一次截图会失败，所以这里执行两次，暂时不知道什么原因
        var node = document.getElementById('ui_pop');
        domtoimage
          .toPng(node)
          .then(function(dataUrl) {
            // self.imageUrl = dataUrl;
            // self.shareFlag = false;
            console.log(self.screemImageUrl, dataUrl);
          })
          .catch(function(error) {
            console.error('oops, something went wrong!', error);
          });

        setTimeout(() => {
          var node = document.getElementById('my-node');
          console.log(node);
          domtoimage
            .toPng(node)
            .then(function(dataUrl) {
              self.screemImageUrl = dataUrl;
              self.shareFlag = false;
              console.log(self.screemImageUrl, dataUrl);
              this.showDialog = false;
            })
            .catch(function(error) {
              console.error('oops, something went wrong!', error);
            });
        }, 1000);
      });
    }
  }
};
</script>

<style lang="scss">
.pullNewsPage {
  .back {
    &::before,
    &::after {
      background-color: #fff !important;
    }
  }
}
</style>


<style lang='scss' scoped>
@import '../../../assets/common/_base';
@import '../../../assets/common/_mixin';

.mod_header {
  background-color: #3d3b51;
  .nav_bar {
    color: #fff;
  }
}

.list {
  @extend .flex_v_justify;
  flex-wrap: wrap;
  -webkit-flex-wrap: wrap;
  padding: 4.2667vw 10vw 0;
}

.item {
  margin-bottom: 4.2667vw;
  a {
    display: block;
    @include getBtn(21.3333vw, 7.7333vw, 3.2vw, #fff, $color_item, 0.5333vw);
    margin-top: 3.2vw;
  }
}

.poster {
  position: relative;
  width: 21.3333vw;
  height: 37.8667vw;
  text-align: center;
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  img {
    width: 13.3333vw;
    margin-top: 14.6667vw;
    object-fit: contain;
    clip-path: polygon(15% 5%, 95% 5%, 95% 85%, 85% 95%, 5% 95%, 5% 15%);
    -webkit-clip-path: polygon(15% 5%, 95% 5%, 95% 85%, 85% 95%, 5% 95%, 5% 15%);
  }
  .tips {
    padding-top: 2.4vw;
    font-size: 0.8vw;
    color: #fff;
    text-align: center;
  }
}

.ui_pop {
  // display: none;
  background-repeat: no-repeat;
  background-position: center;
  background-size: cover;
  // &.active{
  //   display: block;
  // }
}

.qr_code {
  @extend .g_c_mid;
  bottom: 17.0667vw;
  margin: 0 auto;
  img {
    width: 39.7333vw;
    object-fit: contain;
    clip-path: polygon(15% 5%, 95% 5%, 95% 85%, 85% 95%, 5% 95%, 5% 15%);
    -webkit-clip-path: polygon(15% 5%, 95% 5%, 95% 85%, 85% 95%, 5% 95%, 5% 15%);
  }
  .tips {
    padding-top: 2.4vw;
    font-size: 3.4667vw;
    color: #fff;
    text-align: center;
  }
}

.screen_shot {
  .con{
    position: relative;
  }
  .close{
    position: absolute;
    right: 20px;
    top: 20px;
    width: 40px;
    height: 40px;
    @include getBgImg('../../../assets/images/guess/close.png');
  }
  img {
    width: 80%;
  }
}
</style>
