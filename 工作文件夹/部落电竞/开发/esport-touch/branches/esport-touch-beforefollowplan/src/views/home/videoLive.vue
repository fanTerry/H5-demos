<!--
 * @Author: your name
 * @Date: 2019-11-27 14:34:37
 * @LastEditTime: 2020-06-12 19:03:01
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch/src/views/home/video.vue
--> 
<template>
  <div id="mse"></div>

</template>
<script>
// import html2canvas from "html2canvas";
import '../../libs/common/xgplayer';
import '../../libs/common/xgplayer-hls';
import '../../libs/common/xgplayer-flv';

export default {
  name: 'HelloWorld',
  data() {
    return {
      msg: 'Welcome to Your Vue.js App'
    };
  },
  mounted() {
    let player = new HlsJsPlayer({
      id: "mse",
      url:
        "https://80fae9315da0e64f409ea52b7fd0b121.v.apcdns.net/zijian.hls.video.qq.com/01C5CB49011BAA066E066DB764E9E0F964D4F54A733B5D68701A0974B57C03230E695F000770EBAF2BCB33659F4B502CEFB211963D2E68F3A512D92FB777F79829B4F5E5679742DA4D3A1AE58FEA23EA1F8055091A49254B4E4C94165403303FBBFD36BF2A583898C9D7277460FC8A6A/1153018201.m3u8?cdncode=%2f18907E7BE0798990%2f&time=1591959592&cdn=zijian&sdtfrom=v27222212&platform=10201&butype=22&scheduleflag=1&HLSP2P=1&buname=qqlive&vkey=01C5CB49011BAA066E066DB764E9E0F964D4F54A733B5D68701A0974B57C03230E695F000770EBAF2BCB33659F4B502CEFB211963D2E68F3A512D92FB777F79829B4F5E5679742DA4D3A1AE58FEA23EA1F8055091A49254B4E4C94165403303FBBFD36BF2A583898C9D7277460FC8A6A",
      playsinline: true,
      whitelist: [""],
      "x5-video-player-type": "h5",
      "x5-video-player-fullscreen": "true",
      playbackRate: [null]
    });
    // let player = new FlvJsPlayer({
    //   id: 'mse',
    //   url:
    //     'https://al.flv.huya.com/src/1456953785-1456953785-6257568858358415360-2914031026-10057-A-0-1.flv?wsSecret=29399c7a4f41d9f028090416e0b0a367&wsTime=5ee2e6e1&seqid=1591928557013&ctype=huya_tars&fs=bgct&u=0&t=100&sv=2005111540',
    //   playsinline: true,
    //   whitelist: [''],
    //   'x5-video-player-type': 'h5',
    //   'x5-video-player-fullscreen': 'true',
    //   playbackRate: [null],
    //   autoplay: true,
    //   fluid: true
    // });
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
