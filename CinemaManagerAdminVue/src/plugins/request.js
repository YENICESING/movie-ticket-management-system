import Vue from 'vue';
import axios from "axios.js";

// Full config:  https://github.com/axios/axios#request-config
// axios.defaults.baseURL = process.env.baseURL || process.env.apiUrl || '';
// axios.defaults.headers.common['Authorization'] = AUTH_TOKEN;
// axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
//配置 axios 实例
let config = {
  // baseURL: process.env.baseURL || process.env.apiUrl || ""
  // timeout: 60 * 1000, // Timeout
  // withCredentials: true, // Check cross-site Access-Control
};

const _axios = axios.create(config);
//请求拦截器
_axios.interceptors.request.use(
  function(config) {
    // Do something before request is sent
    return config;
  },
  function(error) {
    // Do something with request error
    return Promise.reject(error);
  }
);
// 响应拦截器
// Add a response interceptor
_axios.interceptors.response.use(
  function(response) {
    // Do something with response data
    return response;
  },
  function(error) {
    // Do something with response error
    return Promise.reject(error);
  }
);
/*定义一个名为 Plugin 的对象，其 install 方法是 Vue 插件的安装函数。
Vue.axios = _axios：将自定义的 axios 实例挂载到 Vue 构造函数上，这样可以通过 Vue.axios 全局访问。
window.axios = _axios：将自定义的 axios 实例挂载到 window 对象上，这样在全局作用域中可以通过 window.axios 访问。
Object.defineProperties：将自定义的 axios 实例挂载到 Vue.prototype 上，这样在 Vue 组件中可以通过 this.axios 或 this.$axios 访问。
Vue.use(Plugin)：使用 Vue.use 方法安装插件，使插件生效。*/
Plugin.install = function(Vue, options) {
  Vue.axios = _axios;
  window.axios = _axios;
  Object.defineProperties(Vue.prototype, {
    axios: {
      get() {
        return _axios;
      }
    },
    $axios: {
      get() {
        return _axios;
      }
    },
  });
};

Vue.use(Plugin)
//导出插件
export default Plugin;
