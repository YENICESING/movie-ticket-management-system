<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import "./assets/css/global.css"
import '@/assets/font/font.css'
export default {
  name: 'App',
  data() {
    return {

    }
  }
};
</script>

<style>

#nprogress .bar{
  position: absolute !important;
  top: 60px !important;
  height: 3px !important;
}

</style>
/*#nprogress 作为 nprogress 库创建的进度条容器的 id，可以通过 CSS 对其内部元素（如 .bar）进行样式定制，以满足不同项目的设计需求。
同时，结合 JavaScript 代码在合适的时机启动和结束进度条，为用户提供良好的操作反馈。
这个组件是一个基础的 Vue 应用入口组件，主要负责提供路由视图的渲染位置，并进行了一些全局样式的定制。*/
