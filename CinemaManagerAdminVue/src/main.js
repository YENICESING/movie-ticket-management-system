import Vue from 'vue'
import App from './App.vue'
import './plugins/request.js'
import './plugins/element.js'
import router from './router'
import './assets/css/fonts/iconfont.css'
import store from './store'
import {Message} from 'element-ui'
import './assets/js/permission'
import global_variable from "@/components/global_variable";
// import echarts from 'echarts'
// Vue.prototype.$echarts = echarts
Vue.prototype.global = global_variable
axios.defaults.baseURL = 'http://127.0.0.1:9231/'
axios.interceptors.request.use(config => {
  config.headers.Token = window.sessionStorage.getItem('token')
  return config
})

// 状态码错误信息
const codeMessage = {
  401: '用户没有权限，请登录',
  403: '用户没有权限，请联系管理员',
  404: '访问的资源不存在',
  406: '请求的格式不可得',
  410: '请求的资源被永久删除，且不会再得到的',
  422: '当创建一个对象时，发生一个验证错误',
  500: '服务器发生错误，请检查服务器',
  502: '网关错误',
  503: '服务不可用，服务器暂时过载或维护',
  504: '网关超时'
};

axios.interceptors.response.use(response => {
  return response
}, error => {
  if(error){
    if(!error.response){
      return console.log('Error', error.message);
    }
    //获取状态码
    const status = error.response.status;
    const errortext = codeMessage[status] || error.response.statusText;

    //提示错误信息
    Message.error(errortext)

    // 错误状态处理
    if (status === 401) {
      router.push('/login')
    } else if (status === 403) {
      router.push('/login')
    } else if (status >= 404 && status < 422) {
      router.push('/404')
    }
  }
  return Promise.reject(error)
})

Vue.config.productionTip = false
/*创建一个新的 Vue 实例，将路由配置 router、Vuex 仓库 store 注入到实例中，并使用 render 函数渲染根组件 App*/
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
/*这段代码完成了 Vue 项目的初始化工作，包括导入必要的模块和资源、配置全局变量和请求拦截器、处理响应错误以及创建并挂载 Vue 实例，为整个应用的运行奠定了基础。
同时，通过对请求和响应的拦截处理，实现了统一的身份验证和错误处理机制，提高了代码的可维护性和用户体验。
 */
Vue.use(ElementUI, {
  size: 'medium',
  zIndex: 2000
})
