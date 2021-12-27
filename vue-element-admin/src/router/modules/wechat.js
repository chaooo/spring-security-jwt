/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const wechatRouter = {
  path: '/wechat',
  component: Layout,
  redirect: '/wechat/icons',
  alwaysShow: true, // will always show the root menu
  name: 'WeChat', // name必须和后台配置一致，不然匹配不到
  meta: { title: '微信后台', icon: 'el-icon-s-tools' },
  children: [
    {
      path: 'icons',
      component: () => import('@/views/sys/icons/index'),
      name: 'SysIcons',
      meta: { title: '系统图标', icon: 'el-icon-picture', noCache: true }
    }
  ]
}
export default wechatRouter
