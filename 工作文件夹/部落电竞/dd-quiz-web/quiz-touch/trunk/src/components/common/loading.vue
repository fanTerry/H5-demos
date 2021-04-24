<!--
 * @Author: your name
 * @Date: 2020-02-07 11:41:46
 * @LastEditTime: 2020-04-05 17:11:29
 * @LastEditors: your name
 * @Description: In User Settings Edit
 * @FilePath: /quiz-touch/src/components/common/loading.vue
 -->
<template>
 <div class="loading_wrapper">
    <div class="vue-simple-spinner"></div>
    <div v-if="!notext" class="vue-simple-spinner-text" >{{text?text:"Loading..."}}</div>
     <div v-else class="vue-simple-spinner-text" ></div>
</div>
</template>
<script type="text/ecmascript-6">
  const COMPONENT_NAME = 'loading'
  export default {
    props:["text","notext"],
    name: COMPONENT_NAME,
  }
</script>
<style lang="scss" scoped>
.loading_wrapper {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 9999999;
}
  .vue-simple-spinner {
   margin: 0px auto; 
   border-radius: 100%; 
   border-color: rgb(33, 150, 243) rgb(238, 238, 238) rgb(238, 238, 238); 
   border-style: solid; 
   border-width: 3px; 
   border-image: none 100% / 1 / 0 stretch; 
   width: 32px; 
   height: 32px; 
   animation: 0.8s linear 0s infinite normal none running vue-simple-spinner-spin;
  }
  .vue-simple-spinner-text{
    margin-top: 4px; 
    color: rgb(85, 85, 85); 
    font-size: 13px; 
    text-align: center;
  }
  @keyframes vue-simple-spinner-spin {
    0% { transform: rotate(0deg); }
    100% { transform: rotate(360deg); }
  }
</style>