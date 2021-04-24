import Vue from 'vue'
import VueLazyload from 'vue-lazyload'

Vue.use(VueLazyload, {
  preLoad: 1.3, //预加载的宽高比 
  error: 'static/favicon.ico', //图片加载失败时使用的图片源 
  loading: 'static/favicon.ico', //图片加载的路径 
  attempt: 1 //尝试加载次数 
})
