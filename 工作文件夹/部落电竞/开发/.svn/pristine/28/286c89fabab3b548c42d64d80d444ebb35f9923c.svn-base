<template>
  <div id="mainId">
    <ul class="match_list">
      <li class="match_item" v-for="(item,index) in matchInfoList" :key="index" @click="toJoinMatch(item.id)">
        <div class="match_img">
          <img v-bind:src="item.logoUrl" alt />
          <!-- 有四种颜色的tag -->
          <span class="tag tag_purple" v-if="item.status== 0">已报名</span>
          <span class="tag tag_red" v-else-if="item.status== 4">比赛中</span>
          <span class="tag tag_green" v-else-if="item.status== 1">报名中</span>
          <span class="tag tag_grey" v-else>{{item.status|matchStatus}}</span>
        </div>
        <div class="match_info">
          <p class="title">{{item.name}}</p>
          <p class="times">{{item|matchTime()}}</p>
          <p class="stage" v-if="item.status!=0 && item.status!=3 && item.status!=4 &&item.status!=5">
            已报名队伍：<span>{{item.joinTeamNum}}</span>/{{item.teamNum}}</p>
          <p class="champion" v-else-if="item.join==false && item.status==5">冠军：{{item.championName}}</p>
          <p class="stage" v-else>{{item|matchMsg()}}</p>
        </div>
        <div v-if="item.status == 1" class="apply_btn">报名中</div>
        <div class="team_logo">
          <span class="entry_icon" v-if="item.join==true"></span>
          <span class="court_icon" v-if="item.creator==true"></span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  components: {},
  props: ['matchInfoList'],
  data() {
    return {};
  },
  mounted() {},
  computed: {},
  methods: {
    toJoinMatch(matchId) {
      console.log(matchId, '获得的比赛ID');
      this.$router.push({
        path: '/matchtool/room',
        query: {
          matchId: matchId
        }
      });
    }
  }
};
</script>

<style lang='scss' scoped>
@import '../../../assets/common/_mixin';
@import '../../../assets/common/_base';

.match_item {
  position: relative;
  @extend .flex_hc;
  margin: 2.1333vw 4.2667vw 0;
  padding: 0 4.5333vw;
  background-color: #fff;
  border-radius: 5px;
}

.match_img {
  position: relative;
  width: 16vw;
  height: 18.6667vw;
  padding-top: 0.8vw;
  img {
    width: 16vw;
    height: 16vw;
    border-radius: 50%;
  }
  .tag {
    @extend .g_c_mid;
    top: 13.3333vw;
    width: 14.6667vw;
    line-height: 4.8vw;
    font-size: 2.6667vw;
    font-weight: 600;
    color: #fff;
    text-align: center;
    border-radius: 2.4vw;
  }
}

.tag_purple {
  @include getBgLinear(right, #6950fb, #b83af3);
}

.tag_grey {
  @include getBgLinear(right, #b4b4b4, #cacaca);
}

.tag_red {
  @include getBgLinear(right, #f54b64, #f78361);
}

.tag_green {
  @include getBgLinear(right, #8aca00, #bee700);
}

.apply_btn {
  @extend .g_v_mid;
  right: 7.2vw;
  @include getBtn(14.6667vw, 10.6667vw, 3.2vw, #fff, $color_main, 0.8vw);
}

.match_info {
  @extend .flex_vc;
  flex-direction: column;
  -webkit-flex-direction: column;
  flex: 1;
  -webkit-flex: 1;
  height: 25.0667vw;
  margin-left: 3.7333vw;
  .title {
    @include t_nowrap(42.6667vw);
    font-size: 4.8vw;
    line-height: 5.3333vw;
    font-weight: 600;
    color: #333;
  }
  .times {
    margin-top: 2.1333vw;
    font-size: 3.4667vw;
    color: #999;
  }
  .stage {
    margin-top: 2.1333vw;
    font-size: 3.7333vw;
    line-height: 4.2667vw;
    color: #999;
    span {
      color: #ff7e00;
    }
  }
  .champion {
    margin-top: 2.1333vw;
    font-size: 3.7333vw;
    line-height: 4.2667vw;
    color: #fe5049;
  }
}

.team_logo {
  position: absolute;
  top: 4vw;
  right: 2.6667vw;
  @extend .flex_hc;
  .court_icon,
  .entry_icon {
    width: 4.5333vw;
    height: 4.5333vw;
    margin-left: 1.0667vw;
  }
  .court_icon {
    @include getBgImg('../../../assets/images/matchtool/court_icon.png');
    background-size: 100% 100%;
  }
  .entry_icon {
    @include getBgImg('../../../assets/images/matchtool/entry_icon.png');
    background-size: 100% 100%;
  }
}
</style>
