import Vue from 'vue'
import Router from 'vue-router'
import Login from '../views/Login'
import Home from '../views/Home'
import NotFound from '../views/404'
import Cookies from "js-cookie"

Vue.use(Router)

/*const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}*/


const router =  new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    {
      path: '/error/404',
      name: 'notFound',
      component: NotFound
    },
    {
      path: '/',
      redirect: '/login'
    }
  ]
})

router.beforeEach((to,from,next) => {
  // 登录成功后，会把用户信息保存在会话
  // 存在时间为会话生命周期，页面关闭即失效
  let user = sessionStorage.getItem('user')
  // 如果是登录
  if (to.path == '/login') {
    if (user) {
      // 会话里存在用户信息,代表已经登录，跳转到首页
      next({ path: '/home'})
    } else {
      // 放行
      next()
    }
  } else {
    // 不是登录，用户信息不存在，去登录
    if (!user) {
      next({ path: '/login'})
    } else {
      // 存在用户信息，放行
      next()
    }
  }
})
export default router
