<template>
  <div class='ui_pop'>
    <div class="rules">
      <h2>规则说明</h2>
      <a class="close" @click="closePop()"></a>
      <div class="content">
        <h3>游戏说明</h3>
        <p>相传在遥远的蒙达鲁克硫斯草原上有一颗神奇的宝树，无需踏遍千山万水就可尝尽各色水果，水果可以补充人体所需的各类维生素，让你力大无穷，轻轻摇一摇就会获得水果，因此不要吝啬自己的力气，快来尽情摇晃吧！</p>
        <h3>游戏步骤</h3>
        <p>1、选择默认金额挡位;</p>
        <p>2、点击PLAY按钮摇动宝树;</p>
        <p>3、宝树下落奖品进行奖励结算;</p>
        <p>4、奖励结算详见奖励表;</p>
        <p>5、您也可点击托管开启自动摇晃模式;</p>
        <h3>游戏奖励表</h3>
        <p>奖励值=使用的摇晃力量*奖励倍数;</p>
        <ul>
          <li class="title">欢乐摇一摇奖励表</li>
          <li>
            <span>组合</span>
            <span>奖励</span>
            <span>奖励倍数</span>
          </li>
          <li>
            <span>1</span>
            <span><img src="../../../assets/images/game/gameshake/yezi.png" alt=""></span>
            <span>0.1</span>
          </li>
          <li>
            <span>2</span>
            <span><img src="../../../assets/images/game/gameshake/apple.png" alt=""></span>
            <span>0.3</span>
          </li>
          <li>
            <span>3</span>
            <span><img src="../../../assets/images/game/gameshake/taozi.png" alt=""></span>
            <span>0.6</span>
          </li>
          <li>
            <span>4</span>
            <span><img src="../../../assets/images/game/gameshake/putao.png" alt=""></span>
            <span>1</span>
          </li>
          <li>
            <span>5</span>
            <span><img src="../../../assets/images/game/gameshake/caomei.png" alt=""></span>
            <span>2</span>
          </li>
          <li>
            <span>6</span>
            <span><img src="../../../assets/images/game/gameshake/yingtao.png" alt=""></span>
            <span>3</span>
          </li>
          <li>
            <span>7</span>
            <span><img src="../../../assets/images/game/gameshake/hamigua.png" alt=""></span>
            <span>15</span>
          </li>
          <li>
            <span>8</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*2
            </span>
            <span>0.6</span>
          </li>
          <li>
            <span>9</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*3
            </span>
            <span>0.9</span>
          </li>
          <li>
            <span>10</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
            </span>
            <span>1.2</span>
          </li>
          <li>
            <span>11</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
              <img src="../../../assets/images/game/gameshake/taozi.png" alt="">*1
            </span>
            <span>1.8</span>
          </li>
          <li>
            <span>12</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
              <img src="../../../assets/images/game/gameshake/taozi.png" alt="">*2
            </span>
            <span>2.4</span>
          </li>
          <li>
            <span>13</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
              <img src="../../../assets/images/game/gameshake/taozi.png" alt="">*3
            </span>
            <span>3</span>
          </li>
          <li>
            <span>14</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
              <img src="../../../assets/images/game/gameshake/taozi.png" alt="">*3
              <img src="../../../assets/images/game/gameshake/putao.png" alt="">*1
            </span>
            <span>4</span>
          </li>
          <li>
            <span>15</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
              <img src="../../../assets/images/game/gameshake/taozi.png" alt="">*3
              <img src="../../../assets/images/game/gameshake/putao.png" alt="">*2
            </span>
            <span>5</span>
          </li>
          <li>
            <span>16</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
              <img src="../../../assets/images/game/gameshake/taozi.png" alt="">*3
              <img src="../../../assets/images/game/gameshake/putao.png" alt="">*2
              <img src="../../../assets/images/game/gameshake/caomei.png" alt="">*1
            </span>
            <span>7</span>
          </li>
          <li>
            <span>17</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
              <img src="../../../assets/images/game/gameshake/taozi.png" alt="">*3
              <img src="../../../assets/images/game/gameshake/putao.png" alt="">*2
              <img src="../../../assets/images/game/gameshake/caomei.png" alt="">*1
              <img src="../../../assets/images/game/gameshake/yingtao.png" alt="">*1
            </span>
            <span>10</span>
          </li>
          <li>
            <span>18</span>
            <span>
              <img src="../../../assets/images/game/gameshake/apple.png" alt="">*4
              <img src="../../../assets/images/game/gameshake/taozi.png" alt="">*3
              <img src="../../../assets/images/game/gameshake/putao.png" alt="">*2
              <img src="../../../assets/images/game/gameshake/caomei.png" alt="">*1
              <img src="../../../assets/images/game/gameshake/yingtao.png" alt="">*1
              <img src="../../../assets/images/game/gameshake/hamigua.png" alt="">*1
            </span>
            <span>25</span>
          </li>
          <li>
            <span>19</span>
            <span>
              <img class="plane" src="../../../assets/images/game/gameshake/feiji.png" alt="">
            </span>
            <span>100</span>
          </li>
        </ul>
        <h3>客服电话</h3>
        <p>如有问题可联系 客服微信号：jzdj08 微信公众号：枫叶电竞服务号</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  components: {},
  props: [],
  data() {
    return {};
  },
  methods: {
    closePop() {
      this.$emit('closeRulesPop');
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_mixin';
@import '../../../assets/common/_base';

.rules {
  position: relative;
  width: 80.2667vw;
  height: 117.2vw;
  padding: 0 9.3333vw;
  @include getBgImg('../../../assets/images/game/gameshake/pop_common.png');
}

.close {
  position: absolute;
  right: 2.1333vw;
  top: 1.0667vw;
  width: 16vw;
  height: 16vw;
}

h2 {
  padding-top: 7vw;
  font-size: 6.9333vw;
  color: #ff6c24;
  text-align: center;
}

.content {
  width: 100%;
  height: 82vw;
  margin-top: 6vw;
  text-align: justify;
  overflow: auto;
  -webkit-overflow-scrolling: touch;
}

h3 {
  padding: 2.4vw 0 1.3333vw;
  font-size: 4vw;
  color: #df6744;
}

ul {
  width: 99%;
  margin-top: 4vw;
  border-left: 1px solid #aa661b;
}

li {
  @extend .flex;
  font-size: 4vw;
  text-align: center;
  color: #aa661b;
  span {
    @extend .flex_v_h;
    flex-wrap: wrap;
    -webkit-flex-wrap: wrap;
    border-width: 0 1px 1px 0;
    border-color: #aa661b;
    border-style: solid;
    padding: 1.3333vw 0;
    &:first-child {
      flex: 1;
      -webkit-flex: 1;
    }
    &:nth-child(2) {
      flex: 2.5;
      -webkit-flex: 2.5;
    }
    &:last-child {
      flex: 1.5;
      -webkit-flex: 1.5;
    }
  }
  img {
    width: 4vw;
    height: 4vw;
  }
  .plane {
    width: 8vw;
    height: 8vw;
  }
}

.title {
  display: block;
  padding: 1.3333vw 0;
  font-size: 3.7333vw;
  font-weight: bold;
  color: #df6744;
  border: 1px solid #aa661b;
  border-left: 0;
  text-align: center;
  background-color: rgba(80, 78, 77, 0.1);
}

p {
  font-size: 3.7333vw;
  line-height: 4.8vw;
  color: #aa661b;
}
</style>
