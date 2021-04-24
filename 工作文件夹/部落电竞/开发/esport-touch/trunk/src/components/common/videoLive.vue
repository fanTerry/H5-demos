<!--
 * @Author: your name
 * @Date: 2020-06-12 16:06:56
 * @LastEditTime: 2020-07-24 09:39:11
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch-followplan/src/components/common/videoLive.vue
--> 

<template>
  <div :id="videoLiveInfo.scene+videoLiveInfo.videoId"></div>

</template>
<script>
import '../../libs/common/xgplayer';
import '../../libs/common/xgplayer-hls';
import '../../libs/common/xgplayer-flv';

export default {
  props: ['videoLiveInfo','videoId','videoUrl'],
  data() {
    return {
      player: null,
      vedioConfig: {
        id: '',
        url: '',
        poster: '',
        playsinline: true,
        whitelist: [''],
        'x5-video-player-type': 'h5',
        'x5-video-player-fullscreen': 'true',
        playbackRate: [null],
        autoplay: true,
        fluid: true
      }
    };
  },
  mounted() {
    // let _self = this;
    // if (_self.videoLiveInfo.videoId == null || _self.videoLiveInfo.videoId == '') {
    //   _self.videoLiveInfo.scene = '_default';
    //   _self.videoLiveInfo.videoId = this.getRadomVideoId();
    // }
     this.createPlayer();
   
  },
  destroyed() {
    if (this.player != null) {
      let isDelDom = true; // 参数 isDelDom: true 删除内部DOM元素 | false 保留内部DOM元素
      player.destroy(isDelDom);
    }
  },
  methods: {
    puase() {
      if (this.player != null) {
        this.player.pause();
      }
    },
    play() {
      if (this.player != null) {
        this.player.play();
      }
    },
    createPlayer() {
      // console.log("---222222-this.videoLiveInfo-22222222-",this.videoLiveInfo);
      // console.log("=====2222=====",this.videoId);
      // console.log(this.videoLiveInfo.videoId);
      console.log(this.videoLiveInfo.scene + this.videoLiveInfo.videoId);
      this.vedioConfig.id = this.videoLiveInfo.scene + this.videoLiveInfo.videoId;
      this.vedioConfig.url = this.videoLiveInfo.videoUrl;
      this.vedioConfig.poster = this.videoLiveInfo.poster;
      if (this.videoLiveInfo.videoType == 1) {
        this.player = new HlsJsPlayer(this.vedioConfig);
      } else if (this.videoLiveInfo.videoType == 2) {
        this.player = new FlvJsPlayer(this.vedioConfig);
      }
      console.log("this.vedioConfig",this.vedioConfig);
    },
    getRadomVideoId() {
      let id = Math.round(Math.random() * 10);
      console.log('随机视频ID', id);
      return id;
    }
  }
};
</script>
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
