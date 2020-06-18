import axios from 'axios'
import qs from 'qs' // 引入JSON转字符串工具
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'


// 解决415错误
// 请求超时 请求头
/*axios.defaults.timeout = 10000
axios.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';*/

// create an axios instance
const service = axios.create({

  // 访问的父路径，访问的开头
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // 把这个前面的注释取消，不然，后台获取的sessionid每次都改变
   withCredentials: true, // send cookies when cross-domain requests

  timeout: 5000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent
    console.debug("request请求的ajax拦截器-utils/request")
    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
      // config.headers['Authorization'] = getToken()
    }

    // 如果是post提交，将json数据转为字符串

    console.debug('-----utils/request')
    console.debug(config.data)
    if(config.method  === 'post'){
      config.data = qs.stringify(config.data);
    }
    // 设置数据提交方式为字符串
    config.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=UTF-8'

    return config
  },
  error => {
    console.debug(config)
    console.debug("request请求的错误-utils/request")

    // do something with request error
    console.debug(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data

    console.debug(res)
    console.debug("response响应的ajax拦截器-utils/request")
    // if the custom code is not 20000, it is judged as an error.
    if (res.code === 20001) {

      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        // to re-login
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
      }
      return Promise.reject(new Error(res.message || 'Error'))
    } else {
      return res
    }
  },
  error => {
    console.debug("ajax操作出错-utils/request")

    console.debug('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
