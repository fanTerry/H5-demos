<template>
  <!-- 投注金币动画元素 -->
  <section class="animate_list ">
    <div class="item" :class="['item'+index,{'active':animationFlag[index]},{'reflow':awardReflowList[index]}]" v-for="(item,index) in 22" :key="index">
      <img v-for="(item,index) in 5" :key="index" src="../../../../../assets/images/game/goldempire/gold_coin.png" alt="">
    </div>
  </section>
</template>

<script>
export default {
  components: {},
  props: ['animationFlag', 'awardReflowList'],
  data() {
    return {};
  },
  methods: {}
};
</script>

<style lang='scss' scoped>
@import '../../../../../assets/common/_base';
.animate_list {
  position: absolute;
  left: 20.4vw;
  top: 101.4vw;
  width: 18.1333vw;
  height: 18.1333vw;
}
.item {
  @extend .g_v_c_mid;
  width: 100%;
  height: 100%;
  &.reflow {
    img {
      &:nth-child(1) {
        animation: reflowRobot2 0.6s ease-in forwards;
      }
      &:nth-child(2) {
        animation: reflowRobot2 0.7s ease-in forwards;
      }
      &:nth-child(3) {
        animation: reflowRobot2 0.8s ease-in forwards;
      }
      &:nth-child(4) {
        animation: reflowRobot2 0.9s ease-in forwards;
      }
      &:nth-child(5) {
        animation: reflowRobot2 1s ease-in forwards;
      }
    }
  }
  img {
    position: absolute;
    left: 5vw;
    top: 5vw;
    transform: scale(0);
    -webkit-transform: scale(0);
  }
  &.item1 img {
    top: -93vw;
    left: -3vw;
  }
  &.item2 img {
    top: -93vw;
    left: 65vw;
  }
  &.item21 img {
    top: -93vw;
    left: 26vw;
  }
  &.item5 img {
    top: -69vw;
    left: -11vw;
  }
  &.item6 img {
    top: -69vw;
    left: 9vw;
  }
  &.item7 img {
    top: -69vw;
    left: 42vw;
  }
  &.item8 img {
    top: -69vw;
    left: 63vw;
  }
  &.item9 img {
    top: -48vw;
    left: -10vw;
  }
  &.item10 img {
    top: -31vw;
    left: -10vw;
  }
  &.item11 img {
    top: -14vw;
    left: -10vw;
  }
  &.item12 img {
    top: -48vw;
    left: 62vw;
  }
  &.item13 img {
    top: -31vw;
    left: 62vw;
  }
  &.item14 img {
    top: -14vw;
    left: 62vw;
  }
  &.item15 img {
    top: -48vw;
    left: 9vw;
  }
  &.item16 img {
    top: -31vw;
    left: 9vw;
  }
  &.item17 img {
    top: -14vw;
    left: 9vw;
  }
  &.item18 img {
    top: -48vw;
    left: 43vw;
  }
  &.item19 img {
    top: -31vw;
    left: 43vw;
  }
  &.item20 img {
    top: -14vw;
    left: 43vw;
  }
}

@keyframes reflowRobot2 {
  0% {
    opacity: 1;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    opacity: 1;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    opacity: 1;
    left: 5vw;
    top: 5vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
  100% {
    opacity: 0;
    left: 5vw;
    top: 5vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item1.active {
  img {
    &:nth-child(1) {
      animation: move1Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move1Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move1Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move1Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move1Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move1Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    left: -3vw;
    top: -93vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item2.active {
  img {
    &:nth-child(1) {
      animation: move2Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move2Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move2Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move2Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move2Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move2Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -93vw;
    left: 65vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item21.active {
  img {
    &:nth-child(1) {
      animation: move2Robot21 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move2Robot21 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move2Robot21 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move2Robot21 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move2Robot21 1s ease-in forwards;
    }
  }
}

@keyframes move2Robot21 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -93vw;
    left: 26vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item5.active {
  img {
    &:nth-child(1) {
      animation: move5Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move5Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move5Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move5Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move5Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move5Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -69vw;
    left: -11vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item6.active {
  img {
    &:nth-child(1) {
      animation: move6Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move6Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move6Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move6Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move6Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move6Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -69vw;
    left: 9vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item7.active {
  img {
    &:nth-child(1) {
      animation: move7Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move7Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move7Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move7Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move7Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move7Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -69vw;
    left: 42vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item8.active {
  img {
    &:nth-child(1) {
      animation: move8Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move8Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move8Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move8Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move8Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move8Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -69vw;
    left: 63vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item9.active {
  img {
    &:nth-child(1) {
      animation: move9Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move9Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move9Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move9Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move9Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move9Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -48vw;
    left: -10vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item10.active {
  img {
    &:nth-child(1) {
      animation: move10Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move10Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move10Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move10Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move10Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move10Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -31vw;
    left: -10vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item11.active {
  img {
    &:nth-child(1) {
      animation: move11Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move11Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move11Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move11Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move11Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move11Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -14vw;
    left: -10vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item12.active {
  img {
    &:nth-child(1) {
      animation: move12Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move12Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move12Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move12Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move12Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move12Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -48vw;
    left: 62vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item13.active {
  img {
    &:nth-child(1) {
      animation: move13Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move13Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move13Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move13Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move13Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move13Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -31vw;
    left: 62vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item14.active {
  img {
    &:nth-child(1) {
      animation: move14Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move14Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move14Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move14Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move14Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move14Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -14vw;
    left: 62vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item15.active {
  img {
    &:nth-child(1) {
      animation: move15Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move15Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move15Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move15Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move15Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move15Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -48vw;
    left: 9vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item16.active {
  img {
    &:nth-child(1) {
      animation: move16Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move16Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move16Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move16Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move16Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move16Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -31vw;
    left: 9vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item17.active {
  img {
    &:nth-child(1) {
      animation: move17Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move17Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move17Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move17Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move17Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move17Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -14vw;
    left: 9vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item18.active {
  img {
    &:nth-child(1) {
      animation: move18Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move18Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move18Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move18Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move18Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move18Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -48vw;
    left: 43vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item19.active {
  img {
    &:nth-child(1) {
      animation: move19Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move19Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move19Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move19Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move19Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move19Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -31vw;
    left: 43vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item20.active {
  img {
    &:nth-child(1) {
      animation: move20Robot2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move20Robot2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move20Robot2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move20Robot2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move20Robot2 1s ease-in forwards;
    }
  }
}

@keyframes move20Robot2 {
  0% {
    left: 5vw;
    top: 5vw;
    transform: scale(0.2);
    -webkit-transform: scale(0.2);
  }
  30%,
  60% {
    left: 5vw;
    top: 5vw;
    transform: scale(1.2);
    -webkit-transform: scale(1.2);
  }
  99% {
    top: -14vw;
    left: 43vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}
</style>
