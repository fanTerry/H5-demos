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
  left: 62.4vw;
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
        animation: reflowRobot3 0.6s ease-in forwards;
      }
      &:nth-child(2) {
        animation: reflowRobot3 0.7s ease-in forwards;
      }
      &:nth-child(3) {
        animation: reflowRobot3 0.8s ease-in forwards;
      }
      &:nth-child(4) {
        animation: reflowRobot3 0.9s ease-in forwards;
      }
      &:nth-child(5) {
        animation: reflowRobot3 1s ease-in forwards;
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
    left: -45vw;
  }
  &.item2 img {
    top: -93vw;
    left: 13vw;
  }
  &.item21 img {
    top: -93vw;
    left: -16vw;
  }
  &.item5 img {
    top: -69vw;
    left: -53vw;
  }
  &.item6 img {
    top: -69vw;
    left: -33vw;
  }
  &.item7 img {
    top: -69vw;
    left: 0vw;
  }
  &.item8 img {
    top: -69vw;
    left: 21vw;
  }
  &.item9 img {
    top: -48vw;
    left: -52vw;
  }
  &.item10 img {
    top: -31vw;
    left: -52vw;
  }
  &.item11 img {
    top: -14vw;
    left: -52vw;
  }
  &.item12 img {
    top: -48vw;
    left: 20vw;
  }
  &.item13 img {
    top: -31vw;
    left: 20vw;
  }
  &.item14 img {
    top: -14vw;
    left: 20vw;
  }
  &.item15 img {
    top: -48vw;
    left: -33vw;
  }
  &.item16 img {
    top: -31vw;
    left: -33vw;
  }
  &.item17 img {
    top: -14vw;
    left: -33vw;
  }
  &.item18 img {
    top: -48vw;
    left: 1vw;
  }
  &.item19 img {
    top: -31vw;
    left: 1vw;
  }
  &.item20 img {
    top: -14vw;
    left: 1vw;
  }
}

@keyframes reflowRobot3 {
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
      animation: move1Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move1Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move1Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move1Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move1Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move1Robot3 {
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
    left: -45vw;
    top: -93vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item2.active {
  img {
    &:nth-child(1) {
      animation: move2Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move2Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move2Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move2Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move2Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move2Robot3 {
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
    left: 13vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item21.active {
  img {
    &:nth-child(1) {
      animation: move2Robot31 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move2Robot31 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move2Robot31 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move2Robot31 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move2Robot31 1s ease-in forwards;
    }
  }
}

@keyframes move2Robot31 {
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
    left: -16vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item5.active {
  img {
    &:nth-child(1) {
      animation: move5Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move5Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move5Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move5Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move5Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move5Robot3 {
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
    left: -53vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item6.active {
  img {
    &:nth-child(1) {
      animation: move6Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move6Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move6Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move6Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move6Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move6Robot3 {
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
    left: -33vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item7.active {
  img {
    &:nth-child(1) {
      animation: move7Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move7Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move7Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move7Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move7Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move7Robot3 {
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
    left: 0vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item8.active {
  img {
    &:nth-child(1) {
      animation: move8Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move8Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move8Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move8Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move8Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move8Robot3 {
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
    left: 21vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item9.active {
  img {
    &:nth-child(1) {
      animation: move9Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move9Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move9Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move9Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move9Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move9Robot3 {
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
    left: -52vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item10.active {
  img {
    &:nth-child(1) {
      animation: move10Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move10Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move10Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move10Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move10Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move10Robot3 {
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
    left: -52vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item11.active {
  img {
    &:nth-child(1) {
      animation: move11Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move11Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move11Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move11Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move11Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move11Robot3 {
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
    left: -52vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item12.active {
  img {
    &:nth-child(1) {
      animation: move12Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move12Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move12Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move12Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move12Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move12Robot3 {
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
    left: 20vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item13.active {
  img {
    &:nth-child(1) {
      animation: move13Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move13Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move13Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move13Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move13Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move13Robot3 {
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
    left: 20vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item14.active {
  img {
    &:nth-child(1) {
      animation: move14Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move14Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move14Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move14Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move14Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move14Robot3 {
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
    left: 20vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item15.active {
  img {
    &:nth-child(1) {
      animation: move15Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move15Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move15Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move15Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move15Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move15Robot3 {
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
    left: -33vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item16.active {
  img {
    &:nth-child(1) {
      animation: move16Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move16Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move16Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move16Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move16Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move16Robot3 {
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
    left: -33vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item17.active {
  img {
    &:nth-child(1) {
      animation: move17Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move17Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move17Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move17Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move17Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move17Robot3 {
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
    left: -33vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item18.active {
  img {
    &:nth-child(1) {
      animation: move18Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move18Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move18Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move18Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move18Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move18Robot3 {
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
    left: 1vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item19.active {
  img {
    &:nth-child(1) {
      animation: move19Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move19Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move19Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move19Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move19Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move19Robot3 {
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
    left: 1vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item20.active {
  img {
    &:nth-child(1) {
      animation: move20Robot3 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move20Robot3 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move20Robot3 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move20Robot3 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move20Robot3 1s ease-in forwards;
    }
  }
}

@keyframes move20Robot3 {
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
    left: 1vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}
</style>
