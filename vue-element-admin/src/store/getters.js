const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  username: state => state.user.username,
  userId: state => state.user.userId,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  roleDesc: state => state.user.roleDesc,
  fullName: state => state.user.fullName,
  phone: state => state.user.phone,
  permission_routes: state => state.permission.routes,
  errorLogs: state => state.errorLog.logs
}
export default getters
