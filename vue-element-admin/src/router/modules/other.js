/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const otherRouter = {
  path: '/other',
  component: Layout,
  redirect: '/other/icons',
  alwaysShow: true, // will always show the root menu
  name: 'Other', // name必须和后台配置一致，不然匹配不到
  meta: { title: '其他菜单', icon: 'el-icon-s-tools' },
  children: [
    {
      path: 'icons',
      component: () => import('@/views/sys/icons/index'),
      name: 'SysIcons',
      meta: { title: '系统图标', icon: 'el-icon-picture', noCache: true }
    }
  ]
}
export default otherRouter
