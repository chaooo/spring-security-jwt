import store from '@/store'

function checkPermission(el, binding) {
  const { value } = binding
  // const roles = store.getters && store.getters.roles
  // if (value && value instanceof Array) {
  //   if (value.length > 0) {
  //     const permissionRoles = value

  //     const hasPermission = roles.some(role => {
  //       return permissionRoles.includes(role)
  //     })

  //     if (!hasPermission) {
  //       el.parentNode && el.parentNode.removeChild(el)
  //     }
  //   }
  // } else {
  //   throw new Error(`need roles! Like v-permission="['admin','editor']"`)
  // }
  if (value && value.length > 0) {
    const permissions = store.getters && store.getters.permissions
    const permissionButton = value

    const hasPermission = permissions.length > 0 && permissions.some(permission => {
      return permission === permissionButton
    })
    if (!hasPermission) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    console.error(`need permissions! Like v-permission="SysMenuList:edit"`)
    return false
  }
}

export default {
  inserted(el, binding) {
    checkPermission(el, binding)
  },
  update(el, binding) {
    checkPermission(el, binding)
  }
}
