<template>
  <div id="mse"></div>

</template>
<script>
// import html2canvas from "html2canvas";
import "../../libs/common/xgplayer";
import "../../libs/common/xgplayer-hls";

export default {
  name: "HelloWorld",
  data() {
    return {
      msg: "Welcome to Your Vue.js App"
    };
  },
  mounted() {
    let player = new HlsJsPlayer({
      id: "mse",
      url:
        "http://al.hls.huya.com/backsrc/1279516578288-1279516578288-16798868545046839296-2559033280032-10057-A-0-1.m3u8",
      playsinline: true,
      whitelist: [""],
      "x5-video-player-type": "h5",
      "x5-video-player-fullscreen": "true",
      playbackRate: [null]
    });
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1,
h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
