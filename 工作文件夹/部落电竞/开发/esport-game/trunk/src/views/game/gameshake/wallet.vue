<!--
 * @Author: haitao.li
 * @Date: 2020-04-04 16:10:23
 * @LastEditTime: 2020-04-09 15:27:34
 * @LastEditors: Please set LastEditors
 * @Description: 欢乐摇一摇钱包流水弹窗
 * @FilePath: /quiz-touch/src/views/game/shake/wallet.vue
 -->
<template>
  <div class='ui_pop'>
    <div class="wallet">
      <h3>游戏记录</h3>
      <a class="close" @click="closePop()"></a>
      <div class="container" @scroll="onScroll()">
        <div class="record_list">
          <div class="record">
            <!-- <p class="title">{{item.time}}</p> -->
            <ul class="list">
              <li class="item" v-for="(gameOrder,index) in dataList" :key="index">
                <div>
                  <div class="top">
                    <div class="txt">
                      <div class="flex_hc">
                        <span>掉落: </span>
                        <p class="flex_hc" v-for="(award,index) in gameOrder.awardList " :key="index">
                          <img :src="award.icon | gameFruits" alt="">x{{award.num}}
                        </p>
                      </div>
                    </div>
                    <span class="num">+{{gameOrder.returnAmt}}</span>
                  </div>
                  <div class="bottom">
                    <span>{{gameOrder.createDate}}</span>
                    <span>消耗-{{gameOrder.betAmt}}</span>
                  </div>
                </div>
              </li>
              <p class="att_tips" v-if="!hasNext">暂无更多数据</p>
            </ul>
          </div>
          <p class="no_data" v-if="hasGameRecord">暂无游戏记录</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      hasGameRecord: false,
      dataMap: [],
      dataList: null,
      pageParam: {
        page: 1,
        limit: 10
      },
      hasNext: true
    };
  },
  mounted() {
    this.getPageData();
  },
  methods: {
    closePop() {
      this.$emit('closeWalletPop');
    },

    /**
     * @description: 获取每一页的数据
     * @param {分页数据}
     * @return:
     */
    getPageData() {
      // if (!param) {
      let param = new FormData();
      param.append('page', this.pageParam.page);
      param.append('limit', this.pageParam.limit);
      // }
      console.log('分页参数', param);
      return this.$post('/agency/api/gameOrder/orderList', param)
        .then(rsp => {
          const dataResponse = rsp.data;
          if (rsp.code == '200') {
            if (this.pageParam.page > dataResponse.pages) {
              this.hasNext = false;
              return;
            }
            if (this.pageParam.page == 1 && dataResponse.records.length == 0) {
              //用户没有任何游戏记录
              this.hasGameRecord = true;
            }
            if (this.dataList) {
              this.dataList = this.dataList.concat(dataResponse.records);
            } else if (dataResponse.records) {
              this.dataList = dataResponse.records;
            }
          } else {
            this.$toast(dataResponse.message, 1.5);
          }
        })
        .catch(error => {
          this.$toast('请求异常', 1.5);
          console.log(error);
        });
    },
    /**
     * @description: 将数据转化为按日期分割的条目
     * @param {array}
     * @return:
     */

    /**
     * 监控上拉加载翻页
     */
    onScroll() {
      //滚动内容容器的高度
      let outerHeight = document.querySelector('.container').clientHeight;
      //滚动内容高度
      let innerHeight = document.querySelector('.record_list').scrollHeight;
      //滚动条距离顶部的大小
      let scrollTop = document.querySelector('.container').scrollTop;

      console.log(innerHeight, outerHeight, scrollTop);
      if (innerHeight <= outerHeight + scrollTop + 100) {
        console.log('44444');
        //加载更多操作
        if (!this.hasNext) {
          //没有下一页
          if (this.pageParam.page >= 1) {
            // this.$toast("数据到底啦", 1.5);
          }
          return;
        }
        console.log('加载下一页', this.pageParam);
        this.pageParam.page += 1;
        this.getPageData();
      }
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_mixin';
@import '../../../assets/common/_base';

.wallet {
  position: relative;
  width: 80.2667vw;
  height: 117.2vw;
  @include getBgImg('../../../assets/images/game/gameshake/pop_common.png');
}

.close {
  position: absolute;
  right: 2.1333vw;
  top: 1.0667vw;
  width: 16vw;
  height: 16vw;
}

h3 {
  padding-top: 7vw;
  font-size: 6.9333vw;
  color: #ff6c24;
  text-align: center;
}

.container {
  height: 80.8vw;
  margin: 6.1333vw 8vw 0;
  overflow-y: auto;
  // -webkit-overflow-scrolling: touch;
}

.record_list {
  @extend .flex_v_h;
  flex-direction: column;
  -webkit-flex-direction: column;
  // height: 100%;
  .no_data {
    font-size: 4vw;
    color: #df6744;
  }
}

.record {
  @extend .flex_hc;
  width: 100%;
  height: 100%;
  flex-direction: column;
  -webkit-flex-direction: column;
  .title {
    display: block;
    line-height: 6.9333vw;
    font-size: 5.3333vw;
    font-weight: bold;
    text-align: center;
    color: #aa661b;
  }
  ul {
    width: 100%;
    height: auto;
    overflow: auto;
  }
  .att_tips {
    font-size: 4vw;
    color: #cfa863;
    text-align: center;
  }
}

.item {
  @extend .flex_v_h;
  flex-direction: column;
  -webkit-flex-direction: column;
  width: 100%;
  margin: 1.3333vw 0;
  padding: 0.8vw;
  @include getRadiusBorder(#ceb583, all, 30px);
  &:last-child {
    margin-bottom: 0;
  }
  > div {
    width: 100%;
    padding: 1.6vw 2.6667vw;
    background-color: #f1dd94;
    border-radius: 10px;
  }
  .top,
  .bottom {
    @extend .flex_v_justify;
    width: 100%;
  }
}

.top {
  color: #aa661b;
  .txt {
    font-size: 4.2667vw;
    font-weight: bold;
    > div {
      flex-wrap: wrap;
      -webkit-flex-wrap: wrap;
    }
  }
  .num {
    font-size: 4.5333vw;
    font-weight: bold;
    color: #ec5a13;
  }
  img {
    width: 5.3333vw;
    height: 5.3333vw;
    object-fit: contain;
  }
}

.bottom {
  padding-top: 1.0667vw;
  font-size: 3.7333vw;
  color: #cfa863;
}
</style>
