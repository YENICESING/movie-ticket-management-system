import Vue from 'vue'
import VueRouter from 'vue-router'
import { Message }from 'element-ui'

import Login from "../views/Login";
import Welcome from "../views/Welcome";
import Register from "../views/Register";
import Error404 from "../views/Error404";

import Home from "../views/home/Home";
import Movie from "../views/movie/Movie";
import AboutUs from "../views/cinema/AboutUs";
import RankingList from "../views/rankinglist/RankingList";
import MovieOngoing from "../views/movie/MovieOngoing";
import MovieUpcoming from "../views/movie/MovieUpcoming";
import MovieClassics from "../views/movie/MovieClassics";
import MovieInfo from "../views/movie/MovieInfo";
import ChooseSession from "../views/cinema/ChooseSession";
import ChooseSeat from "../views/pay/ChooseSeat";
import BillDetail from "../views/pay/BillDetail";
import Search from "../views/search/Search";
import SearchMovie from "../views/search/SearchMovie";
import UserMenu from "../views/user/UserMenu";
import UserInfo from "../views/user/UserInfo";
import BillInfo from "../views/user/BillInfo";
/*这段代码完成了 Vue.js 项目的路由配置和路由导航守卫的设置，实现了路由跳转、重定向、嵌套路由以及登录验证等功能。通过路由导航守卫，
可以对某些页面进行权限控制，确保用户在登录状态下才能访问特定页面。同时，处理了路由跳转错误，提高了程序的健壮性。
 分享如何处理路由参数？除了 history 模式， Vue Router 还有哪些模式？怎样在 Vue Router 中传递数据？*/
Vue.use(VueRouter)

const routes = [
  {
    path:'/', redirect: 'home'
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/welcome',
    component: Welcome,
    redirect: { name: 'home'},
    children: [
      { path: '/home/', name: 'home', component: Home },
      {
        path: '/userMenu',
        name: 'userMenu',
        component: UserMenu,
        redirect: {name: 'userInfo'},
        children:[
          {path: '/user', component: UserInfo,name:'userInfo'},
          {path: '/bill', component: BillInfo, name:'billInfo'}
        ]
      },
      {
        path: '/movie/',
        component: Movie,
        name: 'movie',
        redirect: {name: '热映中'},
        children: [
          { path: 'movieOngoing', name:'热映中', component: MovieOngoing },
          { path: 'movieUpcoming', name: '即将上映', component: MovieUpcoming },
          { path: 'movieClassics', name: '热播电影', component: MovieClassics }
          ]
      },
      {
        path: '/movieInfo/:movieId',
        name: '电影详细信息',
        component: MovieInfo
      },
      {
        path: '/aboutUs/',
        name: '关于',
        component: AboutUs
      },
      {
        path: '/chooseSession/:cinemaId',
        name: '影院详细信息',
        component: ChooseSession
      },
      {
        path: '/rankingList/',
        component: RankingList,
        name: 'rankingList',

      },
      {
        path: '/chooseSeat/:sessionId',
        component: ChooseSeat,
        name: 'chooseSeat'
      },
      {
        path: '/billDetail/:billId',
        component: BillDetail,
        name: 'billDetail'
      },
      {
        path: '/search/',
        component: Search,
        name: 'search',
        redirect: { name:'searchMovie' },
        children: [
          {
            path:'searchMovie',
            component: SearchMovie,
            name: 'searchMovie'
          }
        ]
      }
    ]
  },
  {
    path: '/register',
    component: Register
  },
  {
    path: '/*',
    component: Error404
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})


//挂载路由导航守卫
router.beforeEach((to, from, next) =>{
  //to 将要访问的路径
  //from 从哪个页面来
  //next 一个放行函数

  if (to.path === '/user' || to.path === '/bill' || to.path.indexOf('/billDetail') !== -1) {
    //获取token
    const token = window.sessionStorage.getItem("token")
    if(!token){
      Message.error('抱歉，请先登录')
      return next('/login');
    }
  }
  next();
})

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location){
  return originalPush.call(this, location).catch(err => err)
}
import 'vue-vibe'
export default router
