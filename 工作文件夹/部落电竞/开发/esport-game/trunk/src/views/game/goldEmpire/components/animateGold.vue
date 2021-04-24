<template>
  <!-- 投注金币动画元素 -->
  <section class="animate_list">
    <div class="item" :class="['item'+index,{'active':animationFlag[index]},{'reflow':awardReflowList[index]}]" v-for="(item,index) in 22" :key="index">
      <img v-for="(item,index) in 5" :key="index" src="../../../../assets/images/game/goldempire/gold_coin.png" alt="">
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
@import '../../../../assets/common/_base';
.animate_list {
  @extend .g_c_mid;
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
        animation: reflow 0.6s ease-in forwards;
      }
      &:nth-child(2) {
        animation: reflow 0.7s ease-in forwards;
      }
      &:nth-child(3) {
        animation: reflow 0.8s ease-in forwards;
      }
      &:nth-child(4) {
        animation: reflow 0.9s ease-in forwards;
      }
      &:nth-child(5) {
        animation: reflow 1s ease-in forwards;
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
    top: -100vw;
    left: -24vw;
  }
  &.item2 img {
    top: -100vw;
    left: 30vw;
  }
  &.item21 img {
    top: -100vw;
    left: 5vw;
  }
  &.item5 img {
    top: -69vw;
    left: -32vw;
  }
  &.item6 img {
    top: -69vw;
    left: -12vw;
  }
  &.item7 img {
    top: -69vw;
    left: 22vw;
  }
  &.item8 img {
    top: -69vw;
    left: 42vw;
  }
  &.item9 img {
    top: -48vw;
    left: -31vw;
  }
  &.item10 img {
    top: -31vw;
    left: -31vw;
  }
  &.item11 img {
    top: -14vw;
    left: -31vw;
  }
  &.item12 img {
    top: -48vw;
    left: 41vw;
  }
  &.item13 img {
    top: -31vw;
    left: 41vw;
  }
  &.item14 img {
    top: -14vw;
    left: 41vw;
  }
  &.item15 img {
    top: -48vw;
    left: -12vw;
  }
  &.item16 img {
    top: -31vw;
    left: -12vw;
  }
  &.item17 img {
    top: -14vw;
    left: -12vw;
  }
  &.item18 img {
    top: -48vw;
    left: 23vw;
  }
  &.item19 img {
    top: -31vw;
    left: 23vw;
  }
  &.item20 img {
    top: -14vw;
    left: 23vw;
  }
}

@keyframes reflow {
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
      animation: move1 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move1 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move1 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move1 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move1 1s ease-in forwards;
    }
  }
}

@keyframes move1 {
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
    top: -100vw;
    left: -24vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item2.active {
  img {
    &:nth-child(1) {
      animation: move2 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move2 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move2 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move2 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move2 1s ease-in forwards;
    }
  }
}

@keyframes move2 {
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
    top: -100vw;
    left: 30vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item21.active {
  img {
    &:nth-child(1) {
      animation: move21 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move21 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move21 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move21 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move21 1s ease-in forwards;
    }
  }
}

@keyframes move21 {
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
    top: -100vw;
    left: 5vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item5.active {
  img {
    &:nth-child(1) {
      animation: move5 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move5 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move5 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move5 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move5 1s ease-in forwards;
    }
  }
}

@keyframes move5 {
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
    left: -32vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item6.active {
  img {
    &:nth-child(1) {
      animation: move6 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move6 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move6 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move6 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move6 1s ease-in forwards;
    }
  }
}

@keyframes move6 {
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
    left: -12vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item7.active {
  img {
    &:nth-child(1) {
      animation: move7 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move7 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move7 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move7 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move7 1s ease-in forwards;
    }
  }
}

@keyframes move7 {
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
    left: 22vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item8.active {
  img {
    &:nth-child(1) {
      animation: move8 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move8 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move8 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move8 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move8 1s ease-in forwards;
    }
  }
}

@keyframes move8 {
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

.item9.active {
  img {
    &:nth-child(1) {
      animation: move9 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move9 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move9 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move9 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move9 1s ease-in forwards;
    }
  }
}

@keyframes move9 {
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
    left: -31vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item10.active {
  img {
    &:nth-child(1) {
      animation: move10 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move10 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move10 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move10 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move10 1s ease-in forwards;
    }
  }
}

@keyframes move10 {
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
    left: -31vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item11.active {
  img {
    &:nth-child(1) {
      animation: move11 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move11 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move11 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move11 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move11 1s ease-in forwards;
    }
  }
}

@keyframes move11 {
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
    left: -31vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item12.active {
  img {
    &:nth-child(1) {
      animation: move12 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move12 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move12 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move12 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move12 1s ease-in forwards;
    }
  }
}

@keyframes move12 {
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
    left: 41vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item13.active {
  img {
    &:nth-child(1) {
      animation: move13 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move13 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move13 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move13 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move13 1s ease-in forwards;
    }
  }
}

@keyframes move13 {
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
    left: 41vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item14.active {
  img {
    &:nth-child(1) {
      animation: move14 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move14 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move14 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move14 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move14 1s ease-in forwards;
    }
  }
}

@keyframes move14 {
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
    left: 41vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item15.active {
  img {
    &:nth-child(1) {
      animation: move15 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move15 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move15 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move15 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move15 1s ease-in forwards;
    }
  }
}

@keyframes move15 {
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
    left: -12vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item16.active {
  img {
    &:nth-child(1) {
      animation: move16 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move16 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move16 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move16 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move16 1s ease-in forwards;
    }
  }
}

@keyframes move16 {
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
    left: -12vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item17.active {
  img {
    &:nth-child(1) {
      animation: move17 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move17 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move17 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move17 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move17 1s ease-in forwards;
    }
  }
}

@keyframes move17 {
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
    left: -12vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item18.active {
  img {
    &:nth-child(1) {
      animation: move18 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move18 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move18 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move18 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move18 1s ease-in forwards;
    }
  }
}

@keyframes move18 {
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
    left: 23vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item19.active {
  img {
    &:nth-child(1) {
      animation: move19 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move19 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move19 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move19 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move19 1s ease-in forwards;
    }
  }
}

@keyframes move19 {
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
    left: 23vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}

.item20.active {
  img {
    &:nth-child(1) {
      animation: move20 0.6s ease-in forwards;
    }
    &:nth-child(2) {
      animation: move20 0.7s ease-in forwards;
    }
    &:nth-child(3) {
      animation: move20 0.8s ease-in forwards;
    }
    &:nth-child(4) {
      animation: move20 0.9s ease-in forwards;
    }
    &:nth-child(5) {
      animation: move20 1s ease-in forwards;
    }
  }
}

@keyframes move20 {
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
    left: 23vw;
    transform: scale(1);
    -webkit-transform: scale(1);
  }
}
</style>
