import { constantRoutes, asyncRoutes, afterRoutes } from '@/router'

/**
 * 返回当前路由名称对应的菜单
 * @param menus 菜单列表
 * @param name  路由名称
 */
function filterMeun(menus, name) {
  if (name) {
    for (let i = 0; i < menus.length; i++) {
      const menu = menus[i]
      if (name === menu.name) {
        return menu
      }
    }
  }
  return null
}

/**
 * 通过后台请求的菜单列表递归过滤路由表
 * @param routes asyncRoutes
 * @param menus  接口返回的菜单
 */
export function filterAsyncRoutes(routes, menus) {
  const res = []
  routes.forEach(route => {
    const tmp = { ...route }
    const meun = filterMeun(menus, tmp.name)
    if (meun != null && meun.title) {
      tmp.hidden = meun.hidden !== 0
      // 显示的菜单替换后台设置的标题
      if (!tmp.hidden) {
        tmp.meta.title = meun.title
        tmp.sort = meun.sort
      }
      if (meun.icon) {
        tmp.meta.icon = meun.icon
      }
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, menus)
      }
      res.push(tmp)
    }
  })
  return res
}

/**
 * 对菜单进行排序
 */
function sortRouters(accessedRouters) {
  for (let i = 0; i < accessedRouters.length; i++) {
    const router = accessedRouters[i]
    if (router.children && router.children.length > 0) {
      router.children.sort(compare('sort'))
    }
  }
  accessedRouters.sort(compare('sort'))
}

/**
 * 升序比较函数
 */
function compare(p) {
  return (m, n) => {
    const a = m[p]
    const b = n[p]
    return a - b
  }
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
  generateRoutes({ commit }, menus) {
    return new Promise(resolve => {
      // 通过后台请求的菜单列表递归过滤路由表
      const roleAsyncRoutes = filterAsyncRoutes(asyncRoutes, menus)
      // 对可访问菜单进行排序
      sortRouters(roleAsyncRoutes)
      // 拼接尾部公共菜单
      const accessedRoutes = roleAsyncRoutes.concat(afterRoutes)
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
