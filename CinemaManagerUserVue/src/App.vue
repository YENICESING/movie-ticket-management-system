<!--
 * @Description: 
 * @Author: 
 * @Date: 2024-02-24 18:08:34
 这个根组件 App.vue 为整个 Vue 应用提供了基本的结构框架，引入了全局样式和字体样式，并通过 <router-view> 组件实现了路由视图的动态渲染。
 后续可以根据项目需求进一步完善组件的功能和样式。
分享如何在组件中使用路由导航守卫？如何在 Vue Router 中配置动态路由？如何在 Vue 项目中进行状态管理？
-->
<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
import './assets/css/global.css'
import '@/assets/font/font.css'
export default {
  name: 'app',
}
</script>

<style>

</style>
