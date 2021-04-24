
<template>
  <div :id="videoLiveInfo.scene+videoLiveInfo.videoId"></div>

</template>
<script>
import '../../libs/common/xgplayer';
import '../../libs/common/xgplayer-hls';
import '../../libs/common/xgplayer-flv';

export default {
  props: ['videoLiveInfo'],
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
    //  此处加延时是因为这里Vue自身bug ,
    //   this.videoLiveInfo 通过this获取父组件传进来的值，有延迟，因为父页面加了缓存，可能获取到上一个页面传进来的值
    setTimeout(() => {
      this.initParam();
      this.createPlayer();
    }, 100);
  },
  destroyed() {
    if (this.player != null) {
      let isDelDom = true; // 参数 isDelDom: true 删除内部DOM元素 | false 保留内部DOM元素
      this.player.destroy(isDelDom);
    }
  },
  activated() {},
  deactivated() {},
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
    initParam() {
      let _self = this;
      if (_self.videoLiveInfo.videoId == null || _self.videoLiveInfo.videoId == '') {
        _self.videoLiveInfo.scene = 'default_';
        _self.videoLiveInfo.videoId = this.getRadomVideoId();
      }
      // console.log(' this.videoLiveInfo.', this.videoLiveInfo);
      // console.log(this.videoLiveInfo.videoId);
      this.vedioConfig.id = this.videoLiveInfo.scene + this.videoLiveInfo.videoId;
      // console.log(this.vedioConfig.id);
      this.vedioConfig.url = this.videoLiveInfo.videoUrl;
      this.vedioConfig.poster = this.videoLiveInfo.poster;
    },
    createPlayer() {
      console.log('this.vedioConfig', this.vedioConfig);
      if (this.videoLiveInfo.videoType == 1) {
        this.player = new HlsJsPlayer(this.vedioConfig);
      } else if (this.videoLiveInfo.videoType == 2) {
        this.player = new FlvJsPlayer(this.vedioConfig);
      }
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
