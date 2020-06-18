import { asyncRoutes, constantRoutes } from '@/router'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
   /* console.log("-----------hasPermission:true")*/
    console.debug(route.meta.roles)
    return roles.some(role => route.meta.roles.includes(role))
  } else {
  /*  console.log("-----------hasPermission:false")*/

    return true
  }
}

/**
 * 通过递归过滤异步路由表
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {

  generateRoutes({ commit }, roles) {
    return new Promise(resolve => {
      let accessedRoutes
      // 这是原来的代码
      // if (roles.includes('admin')) {
      //   accessedRoutes = asyncRoutes || []
      // } else {
      //
      // }

   /*   console.log('generateRoutes--------')*/
      console.debug(asyncRoutes)
      // 将else中的代码拿出来，改为验证所有角色权限，根据角色的名字来动态生成要加载的菜单
      accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
      console.debug(accessedRoutes)
      commit('SET_ROUTES', accessedRoutes)
      resolve(accessedRoutes)
    })

  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
