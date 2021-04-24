<template>
  <div class="goods_list">
    <slot></slot>
    <ul v-if="type===1">
      <!-- <h3>7月18日 周日</h3> -->
      <li class="goods_item" v-for="item in goodList" @click="gotoDetail(item.id)">
        <div class="goods_img">
          <i class="no_goods_icon" v-if="item.stock==0"></i>
          <img src="../../assets/images/common/default_img.png" :imgurl='item.image' alt="">
        </div>
        <div class="goods_info">
          <i class="rank_first"></i>
          <div class="goods_title">
            <span class="goods_name">{{item.name}}</span>
            <!-- <span class="tag">待发货</span> -->
          </div>

          <!-- <div class="goods_grade">王者</div> -->
          <div class="goods_detail">
            <span class="mark">
              <!--<i class="star_coin"></i>-->{{item.payScore|formatMoney}}
            </span>
            <p class="discount_price" v-if="item.referencePrice&&item.referencePrice>item.payScore">
              {{item.referencePrice|formatMoney}}</p>
            <p class="discount_price" v-else><br></p>
          </div>
          <div class="buy_num">
            <!-- <span> 合计：<i class="star_coin"></i>￥ 55500  </span> -->
            <span class="num">{{item.purchases?item.purchases:"0"}}</span>人购买
          </div>
        </div>
      </li>
    </ul>

    <ul v-if="type===2">
      <!-- <h3>7月18日 周日</h3> -->
      <li class="goods_item" v-for="(item,index) in goodList" @click="gotoDetail(item.id)">
        <div class="goods_img">
          <i class="no_goods_icon" v-if="item.stock==0"></i>
          <img src="../../assets/images/common/default_img.png" :imgurl='item.image' alt="">
        </div>
        <div class="goods_info">
          <i v-if="!band"
            :class="{rank_first:pageType==3&&index==0,rank_second:pageType==3&&index==1,rank_third:pageType==3&&index==2}">
          </i>
          <div>
            <div class="goods_title">
              <span class="goods_name">{{item.name}}</span>
              <!-- <span class="tag">待发货</span> -->
            </div>
            <div class="buy_num">
              <!-- <span> 合计：<i class="star_coin"></i>￥ 55500  </span> -->
              <span class="num">{{item.purchases?item.purchases:"0"}}</span>人购买
            </div>
            <i :class="{'goods_recommend':item.label==1,'goods_new':item.label==2,'goods_discount':item.label==3}"
              v-if="item.label>0">{{item.labelName}}</i>
          </div>
          <div class="goods_detail">
            <span class="mark">
              <!--<i class="star_coin"></i>-->{{item.payScore|formatMoney}}
            </span>
            <p class="discount_price" v-if="item.referencePrice&&item.referencePrice>item.payScore">
              {{item.referencePrice|formatMoney}}</p>
            <p class="discount_price" v-else><br></p>
          </div>
        </div>
      </li>
    </ul>

    <!-- 兑换商品 -->
    <ul v-if="type===3">
      <!-- <h3>7月18日 周日</h3> -->
      <li class="goods_item" v-for="(item,index) in goodList" @click="gotoExchangeDetail(item.orderId)">
        <div class="goods_img">
          <img src="../../assets/images/common/default_img.png" :imgurl='item.goodImg' alt="">
        </div>
        <div class="goods_info">
          <!-- <i :class="{rank_first:pageType==3&&index==0,rank_second:pageType==3&&index==1,rank_third:pageType==3&&index==2}"></i> -->
          <div class="goods_title">
            <span class="goods_name">{{item.goodsName}}</span>
            <span class="tag">{{item.orderStausDec}}</span>
          </div>
          <!-- <div class="goods_grade" v-if="item.labelName">{{item.labelName}}</div> -->
          <!-- <p class="discount_price" >￥{{item.referencePrice}}</p> -->
          <div class="goods_detail">
            <!-- <span class="mark">{{item.payScore}}</span> -->
            <span>
              <span>{{item.quantity}}件商品 合计：
                <!--<i class="star_coin"></i>--> {{item.scoreTotal|formatMoney}} </span>
              <!-- <span> <span class="num">{{item.purchases?item.purchases:"0"}}</span>人购买  </span> -->
            </span>
          </div>
        </div>
      </li>
    </ul>

  </div>
</template>

<script>
export default {
  props: ["goodList", "type", "pageType", "band"],
  data() {
    return {};
  },
  methods: {
    /**商品详情页 */
    gotoDetail(id) {
      this.$router.push({
        name: "goodsDetail",
        params: {
          goodsId: id
        }
      });
    },
    /**购买订单详情页 */
    gotoExchangeDetail(id) {
      this.$router.push({
        name: "orderDetails",
        params: {
          orderId: id
        }
      });
    }
  },
  components: {}
};
</script>

<style lang='scss' scoped>
@import "../../assets/common/_mixin";
@import "../../assets/common/_var";

.star_coin {
  display: block;
  width: 14px;
  height: 14px;
  margin-right: 3px;
  @include getBgImg("../../assets/images/user_center/star_coin.png");
}

.goods_info {
  padding-left: 8px;
}

.goods_img {
  position: relative;
  // @include getRadiusBorder(#eee, all, 4px);
  background-color: #eee;
  img {
    object-fit: contain;
  }
  .no_goods_icon {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
    width: 51px;
    height: 51px;
    @include getBgImg("../../assets/images/store/no_goods.png");
  }
}

.goods_discount,
.goods_new,
.goods_recommend {
  display: block;
  width: 50px;
  height: 16px;
  margin-top: 4px;
  padding-right: 8px;
  font-size: 11px;
  line-height: 16px;
  text-align: right;
  color: #fff;
}

.discount_price {
  padding-left: 5px;
  font-size: 13px;
  color: #999;
  text-decoration: line-through;
}

.buy_num {
  padding-top: 4px;
  font-size: 13px;
  color: #ccc;
}

.mark {
  color: $color_main;
  font-size: 20px;
}

.goods_discount {
  @include getBgImg("../../assets/images/store/goods_discount.png");
}
.goods_new {
  @include getBgImg("../../assets/images/store/goods_new.png");
}
.goods_recommend {
  @include getBgImg("../../assets/images/store/goods_recommend.png");
}

.goods_grade {
  display: block;
  margin-top: 5px;
  padding: 3px 5px;
  font-size: 11px;
  color: $color_main;
  @include getRadiusBorder($color_main, all, 4px);
}
.goods_name {
  @include t_nowrap(180px);
  line-height: 1.2;
  @media screen and (max-width: 320px) {
    @include t_nowrap(160px);
    line-height: 1.2;
  }
}
.rank_first,
.rank_second,
.rank_third {
  width: 23px;
  height: 23px;
}
.rank_first {
  @include getBgImg("../../assets/images/store/rank_first.png");
}
.rank_second {
  @include getBgImg("../../assets/images/store/rank_second.png");
}
.rank_third {
  @include getBgImg("../../assets/images/store/rank_third.png");
}
</style>
