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
  left: 2.4vw;
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
        animation: reflowRobot1 0.6s ease-in forwards;
      }
      &:nth-child(2) {
        animation: reflowRobot1 0.7s ease-in forwards;
      }
      &:nth-child(3) {
        animation: reflowRobot1 0.8s ease-in forwards;
      }
      &:nth-child(4) {
        animation: reflowRobot1 0.9s ease-in forwards;
      }
      &:nth-child(5) {
        animation: reflowRobot1 1s ease-in forwards;
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
    left: 15vw;
  }
  &.item2 img {
    top: -93vw;
    left: 73vw;
  }
  &.item21 img {
    top: -93vw;
    left: 44vw;
  }
  &.item5 img {
    top: -69vw;
    left: 7vw;
  }
  &.item6 img {
    top: -69vw;
    left: 27vw;
  }
  &.item7 img {
    top: -69vw;
    left: 60vw;
  }
  &.item8 img {
    top: -69vw;
    left: 81vw;
  }
  &.item9 img {
    top: -48vw;
    left: 8vw;
  }
  &.item10 img {
    top: -31vw;
    left: 8vw;
  }
  &.item11 img {
    top: -14vw;
    left: 8vw;
  }
  &.item12 img {
    top: -48vw;
    left: 80vw;
  }
  &.item13 img {
    top: -31vw;
    left: 80vw;
  }
  &.item14 img {
    top: -14vw;
    left: 80vw;
  }
  &.item15 img {
    top: -48vw;
    left: 27vw;
  }
  &.item16 img {
    top: -31vw;
    left: 27vw;
  }
  &.item17 img {
    top: -14vw;
    left: 27vw;
  }
  &.item18 img {
    top: -48vw;
    left: 61vw;
  }
  &.item19 img {
    top: -31vw;
    left: 61vw;
  }
  &.item20 img {
    top: -14vw;
    left: 61vw;
  }
}

@keyframes reflowRobot1 {
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
      animation: move1Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move1Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move1Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move1Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move1Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move1Robot1 {
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
    left: 15vw;
    top: -93vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item2.active {
  img {
    &:nth-child(1) {
      animation: move2Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move2Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move2Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move2Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move2Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move2Robot1 {
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
    left: 73vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item21.active {
  img {
    &:nth-child(1) {
      animation: move2Robot11 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move2Robot11 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move2Robot11 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move2Robot11 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move2Robot11 1s ease-in forwards;
    }
  }
}

@keyframes move2Robot11 {
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
    left: 44vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item5.active {
  img {
    &:nth-child(1) {
      animation: move5Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move5Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move5Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move5Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move5Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move5Robot1 {
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
    left: 7vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item6.active {
  img {
    &:nth-child(1) {
      animation: move6Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move6Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move6Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move6Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move6Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move6Robot1 {
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
    left: 27vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item7.active {
  img {
    &:nth-child(1) {
      animation: move7Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move7Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move7Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move7Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move7Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move7Robot1 {
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
    left: 60vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item8.active {
  img {
    &:nth-child(1) {
      animation: move8Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move8Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move8Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move8Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move8Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move8Robot1 {
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
    left: 81vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item9.active {
  img {
    &:nth-child(1) {
      animation: move9Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move9Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move9Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move9Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move9Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move9Robot1 {
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
    left: 8vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item10.active {
  img {
    &:nth-child(1) {
      animation: move10Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move10Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move10Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move10Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move10Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move10Robot1 {
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
    left: 8vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item11.active {
  img {
    &:nth-child(1) {
      animation: move11Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move11Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move11Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move11Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move11Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move11Robot1 {
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
    left: 8vw;

    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item12.active {
  img {
    &:nth-child(1) {
      animation: move12Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move12Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move12Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move12Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move12Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move12Robot1 {
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
    left: 80vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item13.active {
  img {
    &:nth-child(1) {
      animation: move13Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move13Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move13Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move13Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move13Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move13Robot1 {
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
    left: 80vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item14.active {
  img {
    &:nth-child(1) {
      animation: move14Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move14Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move14Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move14Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move14Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move14Robot1 {
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
    left: 80vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item15.active {
  img {
    &:nth-child(1) {
      animation: move15Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move15Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move15Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move15Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move15Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move15Robot1 {
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
    left: 27vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item16.active {
  img {
    &:nth-child(1) {
      animation: move16Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move16Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move16Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move16Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move16Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move16Robot1 {
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
    left: 27vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item17.active {
  img {
    &:nth-child(1) {
      animation: move17Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move17Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move17Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move17Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move17Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move17Robot1 {
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
    left: 27vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item18.active {
  img {
    &:nth-child(1) {
      animation: move18Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move18Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move18Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move18Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move18Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move18Robot1 {
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
    left: 61vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item19.active {
  img {
    &:nth-child(1) {
      animation: move19Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move19Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move19Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move19Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move19Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move19Robot1 {
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
    left: 61vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item20.active {
  img {
    &:nth-child(1) {
      animation: move20Robot1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move20Robot1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move20Robot1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move20Robot1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move20Robot1 1s ease-in forwards;
    }
  }
}

@keyframes move20Robot1 {
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
    left: 61vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}
</style>
