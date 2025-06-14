<!--模板部分：包含搜索框、搜索按钮、水平导航菜单以及用于显示路由视图的区域。搜索框绑定 keyword 数据，搜索按钮点击时触发 searchInfo 方法；
导航菜单根据 menuList 数据动态生成菜单项，且与路由关联；路由视图接收 keyword 作为属性传递。
脚本部分：定义组件名称为 Search，数据 menuList 包含一个电影菜单项信息，keyword 初始值为当前路由参数 kw 的值。created 钩子函数为空；
searchInfo 方法用于将当前路由路径加上 ?kw= 和输入的关键词，然后进行路由跳转；watch 监听 $route 变化，更新 keyword 为当前路由参数 kw 的值。-->
<template>
  <div>
    <div class="searchContainer">
      <el-input v-model="keyword" class="searchBar" placeholder="请输入电影名称"></el-input>
      <el-button id="searchBtn" type="primary" circle @click="searchInfo"><i class="iconfont icon-r-find" style="font-size: 24px;"></i></el-button>
    </div>
    <div>
      <el-menu :default-active="$route.path"
               class="nav-menu"
               mode="horizontal"
               :router="true"
               background-color="#47464a"
               text-color="#fff"
               active-text-color="#ffd04b">
        <el-menu-item :index="item.path" v-for="item in menuList" :key="item.id">
          <i :class="item.icon" style="font-size: 36px"> </i>
                <b style="font-size: 22px"> {{ item.name }}</b>
        </el-menu-item>
      </el-menu>
    </div>
    <div>
      <router-view :keyword="keyword"></router-view>
    </div>
  </div>


</template>

<script>
export default {
  name: "Search",
  data() {
    return {
      menuList: [
        {
          id: '1',
          name: '电影',
          icon: "iconfont icon-r-paper",
          path: '/search/searchMovie'
        }
        // ,
        // {
        //   id: '2',
        //   name: '影院',
        //   path: '/search/searchCinema'
        // }
      ],
      keyword: this.$route.query.kw
    }
  },
  created() {

  },
  methods:{
    searchInfo(){
      this.$router.push(this.$route.path + '?kw=' + this.keyword)
    }
  },
  watch:{
    '$route'(){
      this.keyword = this.$route.query.kw
    }
  }
}
</script>

<style scoped>
.el-header{
  padding: 0px;
}

.nav-menu{
  display: flex;
  justify-content: space-around;
  font-size: 16px;
}

.el-menu-item{
  font-size: 16px;
}

.el-menu-item:hover{
  color: #FFFFFF !important;
}

.searchContainer{
  padding: 30px 0;
  display: flex;
  background: #47464a;
  justify-content: center;
}

.searchBar{
  width: 630px;
}

.searchBar >>> input{
  padding-right: 0;
  border-radius: 50px;
  width: 630px;
  height: 50px;
}

#searchBtn{
  transform: translate(-100%, 0);
  width: 50px;
  height: 50px;
}
</style>
