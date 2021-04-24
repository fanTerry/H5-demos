<template>
    <div id="minirefresh" class="minirefresh-wrap">

      <div class="minirefresh-scroll">
          <slot></slot>

      </div>
    </div>
</template>


<script>
import MiniRefreshTools from "minirefresh";
import "minirefresh/dist/debug/minirefresh.css";
import "minirefresh/dist/debug/themes/applet/minirefresh.theme.applet.js";
import "minirefresh/dist/debug/themes/applet/minirefresh.theme.applet.css";

export default {
  data() {
    return {
      minirefresh: null,
   
    };
  },
  mounted() {
    var self = this;
    self.miniRefresh = new MiniRefresh({
      container: "#minirefresh",
      isScrollBar:false,
      down: {
        callback: self.downCallback,
         isWrapCssTranslate: true,
      },
      up: {
        isAuto: true,
        callback: self.upCallback
      }
    });
  },
  methods: {
    downCallback: function() {
       this.$emit("downCallback")
    },
    upCallback: function() {
      this.$emit("upCallback")
    },
    
  },
  
};
</script>

<style lang='scss' scoped>



</style>
