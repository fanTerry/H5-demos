<template>
  <div class="Page guessRules">
    <div class="main">
      <section>
        <div style="text-align:center">
          <h3>
            <span>平台公告</span>
          </h3>
        </div>
        <p>尊敬的客户您好！</p>
        <p>
          由于受到频繁的网络袭击安全事件，造成了枫叶平台部分赛事服务永久性关停；经过夜以继日的抢救与性能恢复，仍然无法恢复正常的服务。
          在诉诸司法手段追查网络攻击者的同时，我们只能遗憾的通知您，枫叶平台已于<span>2020年6月25日晚24点时</span>无限期停运，期间如果您有剩余的星星积分余额未进行商品兑换，
          可以联系我们的官方客服表明情况便于平台做后续的处理~
        </p>
      </section>
      <section>
        <h3>
          联系方式:
        </h3>
        <p>
          客服热线：0898-68911581（工作日9:00-18:00）
          <br />客服QQ号：3114156657
          <br />
        </p>
      </section>
    </div>
  </div>
</template>

<script>
import navBar from '../../components/header/nav_bar/index.vue';

export default {
  components: {
    navBar
  },
  props: [],
  data() {
    return {};
  },
  methods: {}
};
</script>

<style lang='scss' scoped>
@import '../../assets/common/_var';

.guessRules {
  background: $color_main;
}
.main {
  margin: 4.2667vw 4.2667vw;
  padding: 0 3.2vw;
  border-radius: 1.3333vw 1.3333vw 0 0;
  background-color: #fff;
  p {
    padding-top: 1.0667vw;
    font-size: 3.73vw;
    line-height: 5.8667vw;
    text-align: justify;
    word-break: break-all;
  }
}

section {
  margin-top: 4.2667vw;
  color: #333;
}

h3 {
  font-size: 4.2667vw;
  line-height: 4.8vw;
  span {
    color: #cf373f;
  }
}
span {
    color: #cf373f;
}
</style>
