/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const sysRouter = {
  path: '/sys',
  component: Layout,
  redirect: '/sys/menus',
  alwaysShow: true, // will always show the root menu
  name: 'SysSetting', // name必须和后台配置一致，不然匹配不到
  meta: { title: '系统设置', icon: 'el-icon-s-tools' },
  children: [
    {
      path: 'menus',
      component: () => import('@/views/sys/menus/index'),
      redirect: '/sys/menus/list',
      name: 'SysMenus',
      meta: { title: '菜单管理', icon: 'el-icon-menu' },
      children: [
        {
          path: 'list',
          hidden: true,
          component: () => import('@/views/sys/menus/list.vue'),
          name: 'SysMenuList',
          meta: { title: '菜单列表' }
        },
        {
          path: 'edit',
          hidden: true,
          component: () => import('@/views/sys/menus/form.vue'),
          name: 'SysMenuEdit',
          meta: { title: '编辑菜单' }
        },
        {
          path: 'add',
          hidden: true,
          component: () => import('@/views/sys/menus/form.vue'),
          name: 'SysMenuEdit',
          meta: { title: '添加菜单' }
        }
      ]
    },
    {
      path: 'roles',
      component: () => import('@/views/sys/roles/index'),
      redirect: '/sys/roles/list',
      name: 'SysRoles',
      meta: { title: '角色管理', icon: 'lock' },
      children: [
        {
          path: 'list',
          hidden: true,
          component: () => import('@/views/sys/roles/list.vue'),
          name: 'SysRoleList',
          meta: { title: '角色列表' }
        },
        {
          path: 'edit',
          hidden: true,
          component: () => import('@/views/sys/roles/form.vue'),
          name: 'SysRoleEdit',
          meta: { title: '编辑角色' }
        },
        {
          path: 'add',
          hidden: true,
          component: () => import('@/views/sys/roles/form.vue'),
          name: 'SysRoleEdit',
          meta: { title: '添加角色' }
        }
      ]
    },
    {
      path: 'users',
      component: () => import('@/views/sys/users/index'),
      redirect: '/sys/users/list',
      name: 'SysUsers',
      meta: { title: '用户管理', icon: 'user' },
      children: [
        {
          path: 'list',
          hidden: true,
          component: () => import('@/views/sys/users/list.vue'),
          name: 'SysUserList',
          meta: { title: '用户列表' }
        },
        {
          path: 'add',
          hidden: true,
          component: () => import('@/views/sys/users/form.vue'),
          name: 'SysUserEdit',
          meta: { title: '添加用户' }
        },
        {
          path: 'edit',
          hidden: true,
          component: () => import('@/views/sys/users/form.vue'),
          name: 'SysUserEdit',
          meta: { title: '编辑用户' }
        }
      ]
    },
    {
      path: 'icons',
      component: () => import('@/views/sys/icons/index'),
      name: 'SysIcons',
      meta: { title: '系统图标', icon: 'el-icon-picture', noCache: true }
    }
  ]
}
export default sysRouter
