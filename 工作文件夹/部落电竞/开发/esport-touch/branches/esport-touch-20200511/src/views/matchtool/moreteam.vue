<template>
  <div class="Page" :style="'background-image: url('+(require('assets/images/matchtool/king_glory_bg.png'))+')'">
    <header class="mod_header">
      <nav-bar :pageTitle="'更多队伍'"></nav-bar>
    </header>
    <div class="main">
      <team-list ref="teamlist" :showDeleteIcon="teamRoomInfo.creator" @deletePlayer='deletePlayer'
        :teamList="teamRoomInfo.teamInfo" :userTeamId="teamId" :matchId="matchId" @applySuccess="applySuccess"
        @applyFailed="applyFailed">
      </team-list>
    </div>

    <pops :popTitle="popTitle" v-show="showPop" @cancel='showPop = false' @confirm="applyQuiz"></pops>
    <pops :popTitle="popTitle" :hideCancel="true" v-show="showApplyPop" @confirm="showApplyPop = false"></pops>
  </div>
</template>

<script>
import navBar from "../../components/header/nav_bar/index.vue";
import teamList from "./components/teamlist.vue";
import pops from "./components/pops.vue";

export default {
  components: { teamList, navBar, pops },
  props: [],
  data() {
    return {
      showPop: false,
      showApplyPop: false,
      popTitle: "",
      matchId: Number,
      teamId: Number,
      userInfo: {},
      teamRoomInfo: {},
      deleteUserId: Number,
      deleteTeamId: Number
    };
  },
  mounted() {
    this.matchId = this.$route.query.matchId;
    this.getMatchRoomTeamInfo();
  },
  methods: {
    deletePlayer(deleteUserId, teamId) {
      this.showPop = true;
      this.deleteUserId = deleteUserId;
      this.deleteTeamId = teamId;
      this.popTitle = "您确定要移除该名参赛用户吗?";
    },

    applyQuiz() {
      this.showPop = false;
      let param = {};
      param.teamId = this.deleteTeamId;
      param.userApplyId = this.deleteUserId;
      param.matchId = this.matchId;
      console.log(param);
      return this.$post("/api/matchtool/matchRoom/quiz", param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            if (this.deleteUserId == this.userInfo.userId) {
              //清除自己的头像
              this.$refs.teamlist.currPosition = "";
              this.clearUserAvatarByCreator();
            } else {
              this.clearUserAvatar();
            }
            console.log(dataResponse);
          } else if (dataResponse.code == "4444") {
            this.$toast(dataResponse.message);
          } else {
            this.$toast("退赛异常");
          }
        })
        .catch(error => {
          console.log(error);
        });
    },
    applySuccess(teamId, teanNum) {
      this.teamRoomInfo.join = true;
      this.clearUserAvatarByCreator();
      this.teamId = teamId;
      this.showApplyPop = true;
      this.popTitle = "恭喜您已报名成功";
    },
    applyFailed() {
      this.teamRoomInfo.join = false;
      this.clearUserAvatar();
    },
    //删除掉剔除的用户头像
    clearUserAvatar() {
      for (let index = 0; index < this.teamRoomInfo.teamInfo.length; index++) {
        const team = this.teamRoomInfo.teamInfo[index];
        if (team.teamId == this.deleteTeamId) {
          if (team.playerInfo != null) {
            for (var i = 0; i < team.playerInfo.length; i++) {
              let player = team.playerInfo[i];
              if (player && player.userId == this.deleteUserId) {
                console.log("下标", i);
                // team.playerInfo[i] = null
                team.playerInfo.splice(i, 1);
              }
            }
          }
        }
      }
    },
    //删除掉用户自己的头像
    clearUserAvatarByCreator() {
      for (let index = 0; index < this.teamRoomInfo.teamInfo.length; index++) {
        const team = this.teamRoomInfo.teamInfo[index];
        if (team.teamId == this.teamId) {
          if (team.playerInfo != null) {
            for (var i = 0; i < team.playerInfo.length; i++) {
              if (
                team.playerInfo[i] &&
                team.playerInfo[i].userId == this.userInfo.userId
              ) {
                console.log("下标", i);
                //  team.playerInfo[i] = null
                team.playerInfo.splice(i, 1);
              }
            }
          }
        }
      }
    },
    getMatchRoomTeamInfo() {
      //测试使用
      // this.matchId = 44456;
      let param = {};
      param.pageType = 1;
      return this.$post("/api/matchtool/matchRoom/" + this.matchId, param)
        .then(rsp => {
          const dataResponse = rsp;
          if (dataResponse.code == "200") {
            this.teamRoomInfo = dataResponse.data.teamRoomInfo;
            if (this.teamRoomInfo.teamInfo.length > 0) {
              this.teamRoomInfo.teamInfo = this.teamRoomInfo.teamInfo;
            }
            this.teamId = this.teamRoomInfo.userTeamId;
            this.userInfo = dataResponse.data.userInfo;
            this.$refs.teamlist.userInfo = this.userInfo;
          }
        })
        .catch(error => {
          console.log(error);
        });
    }
  }
};
</script>

<style lang='scss' scoped>
@import "../../assets/common/_mixin";
@import "../../assets/common/_base";

.Page {
  background-size: cover;
  background-position: center -118px;
  background-repeat: no-repeat;
  @media only screen and (min-device-width: 375px) and (min-device-height: 812px) and (-webkit-device-pixel-ratio: 3),
    only screen and (device-width: 414px) and (device-height: 896px) and (-webkit-device-pixel-ratio: 2) {
    background-size: 100% 1000px;
  }
}

.main {
  padding-top: 4vw;
}
</style>
