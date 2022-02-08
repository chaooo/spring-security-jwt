import store from '@/store'

/**
 * @param {String} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export default function checkPermission(value) {
  // if (value && value instanceof Array && value.length > 0) {
  //   const roles = store.getters && store.getters.roles
  //   const permissionRoles = value

  //   const hasPermission = roles.some(role => {
  //     return permissionRoles.includes(role)
  //   })
  //   return hasPermission
  // } else {
  //   console.error(`need roles! Like v-permission="['admin','editor']"`)
  //   return false
  // }
  if (value && value.length > 0) {
    const permissions = store.getters && store.getters.permissions
    const permissionButton = value

    const hasPermission = permissions.length > 0 && permissions.some(permission => {
      return permission === permissionButton
    })
    return hasPermission
  } else {
    console.error(`need permissions! Like v-permission="SysMenuList:edit"`)
    return false
  }
}
