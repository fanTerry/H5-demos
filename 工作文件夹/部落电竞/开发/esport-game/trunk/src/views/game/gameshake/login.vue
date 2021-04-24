<template>
  <div></div>
</template>

<script>
export default {
  components: {},
  data() {
    return {};
  },
  computed: {},
  created() {},
  mounted() {
    let gameNo = this.$route.query.gameNo;
    console.log(gameNo);
    this.$post("/agency/v2/game/autoLogin", { gameNo: gameNo })
        .then(data => {
          console.log(data);
          if (data.code == '200') {
            window.location.href = data.data.gameUrl;
          } else {
            this.$toast('游戏登录异常');
          }
        })
        .catch(error => {
          console.log(error, "请求游戏登录接口异常");
        });
  },
  methods: {},
  watch: {}
};
</script>

<style lang='scss'>
</style>
