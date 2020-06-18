import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui' // 支持
import 'element-ui/lib/theme-chalk/index.css' // 样式
import api from './http/index'

Vue.config.productionTip = false
// 使用ElementUI
Vue.use(ElementUI)
Vue.use(api)
new Vue({
  el: '#app',
  router,
  render: h => h(App)
})
