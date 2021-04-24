<!--
 * @Author: your name
 * @Date: 2020-07-24 10:53:58
 * @LastEditTime: 2020-07-24 15:36:44
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: /esport-touch/src/components/common/videoAliPlayer.vue
--> 
  <!-- <div id="app"> -->
  <template v-if="show">
  <vue-aliplayer-v2 :source="videoUrl" ref="VueAliplayerV2" :options="options" />
</template>
  <!-- <p class="remove-text" v-else>播放器已销毁!</p>
    <div class="player-btns">
      <span @click="play()">播放</span>
      <span @click="pause()">暂停</span>
      <span @click="replay()">重播</span>
      <span @click="getCurrentTime()">播放时刻</span>
      <span @click="show = !show">{{ show ? '销毁' : '重载' }}</span>
      <span @click="options.isLive = !options.isLive">{{ options.isLive ? '切换普通模式' : '切换直播模式' }}</span>
      <span @click="getStatus()">获取播放器状态</span>
    </div>
    <div class="source-box">
      <span class="source-label">选择播放源(支持动态切换):</span>
      <select v-model="source" name="source" id="source">
        <option value="//player.alicdn.com/video/aliyunmedia.mp4">播放源1</option>
        <option value="//yunqivedio.alicdn.com/user-upload/nXPDX8AASx.mp4">播放源2</option>
        <option value="//tbm-auth.alicdn.com/e7qHgLdugbzzKh2eW0J/kXTgBkjvNXcERYxh2PA@@hd_hq.mp4?auth_key=1584519814-0-0-fc98b2738f331ff015f7bf5c62394888">播放源3</option>
        <option value="//ivi.bupt.edu.cn/hls/cctv1.m3u8">直播播放源4</option>
      </select>
    </div>
    <div class="source-box">
      <span class="source-label">输入播放源(支持动态切换):</span>
      <input class="source-input" type="text" v-model="source">
    </div> -->
  <!-- </div> -->

<script>
// import VueAliplayer from 'vue-aliplayer';
import VueAliplayerV2 from 'vue-aliplayer-v2';

export default {
  components: {
    'vue-aliplayer-v2': VueAliplayerV2.Player
  },
  props: ['videoUrl'],
  data() {
    return {
      options: {
        // source: '//player.alicdn.com/video/aliyunmedia.mp4'
        height: '53.3333vw',
        // videoHeight: '200px',
        isLive: true,
        autoplay: true,
        useH5Prism: true,
        playsinline: true,
        // x5_type: 'h5',
        // x5_fullscreen: false,
        // x5_orientation: landscape,
        useHlsPluginForSafari: true, //Safari浏览器可以启用Hls插件播放，Safari 11除外。
        enableStashBufferForFlv: true //H5播放flv时，设置是否启用播放缓存，只在直播下起作用。
      },
      show: true
      // source:videoUrl,
    };
  },
  mounted() {},
  methods: {
    play() {
      this.$refs.VueAliplayerV2.play();
    },

    pause() {
      this.$refs.VueAliplayerV2.pause();
    },

    replay() {
      this.$refs.VueAliplayerV2.replay();
    },

    getCurrentTime() {
      this.$refs.VueAliplayerV2.getCurrentTime();
    }
  }
};
</script>


<style lang="scss" >
@import '../../assets/common/_base';

.prism-big-play-btn {
  position: absolute !important;
  left: 50% !important;
  top: 50% !important;
  transform: translate(-50%, -50%) !important;
  -webkit-transform: translate(-50%, -50%) !important;
}
</style>
