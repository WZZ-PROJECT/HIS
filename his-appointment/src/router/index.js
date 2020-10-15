import Vue from 'vue'
import Router from 'vue-router'
const index = () => import('@/views/index')
const appointment = () => import('@/views/appointment')
const third = () => import('@/views/third')
const fourth = () => import('@/views/fourth')
const five = () => import('@/views/five')
const patientPage = () => import('@/views/patientPage')
const patientIntroduce = () => import('@/views/patientIntroduce')
const result = () => import('@/views/result')
const center = () => import('@/views/center/index')

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [{
      path: '/',
      name: 'index',
      component: index
    },
    {
      path: '/appointment',
      component: appointment,
      children: [
        {
          name: 'third',
          path: 'third',
          component: third,
          meta: {
            keep: true,
            title: '济南市慈光',
            hideBack: true
          }
        },
        {
          name: "five",
          path: 'five',
          component: five,
          meta: {
            title: '请确认预约信息'
          }
        },

        {
          name: 'patientPage',
          path: 'patientPage',
          component: patientPage,
          meta: {
            title: '医生主页'
          }
        },
        {
          name: 'patientIntroduce',
          path: 'patientIntroduce',
          component: patientIntroduce,
          meta: {
            title: '医生介绍'
          }
        },

        {
          name: 'result',
          path: 'result',
          component: result,
          meta: {
            title: '搜索'
          }
        },
        {
          name: 'fourth',
          path: 'fourth',
          component: fourth,
          meta: {
            title: '医生主页'
          }
        },
        {
          path: '/',
          redirect: '/appointment/third'
        },
      ]
    },
    {
      path: '/center',
      component: center,
      children: [
        {
          path: '/',
          component: () => import('@/views/center/center'),
          meta: {
            title: '个人中心',
            hideBack: true
          }
        },
        {
          path: 'orders',
          component: () => import('@/views/center/orders'),
          meta: {
            title: '预约记录',
          }
        },
        {
          path: 'doctors',
          component: () => import('@/views/center/doctors'),
          meta: {
            title: '关注医生',
          }
        },
        {
          path: 'history',
          component: () => import('@/views/center/history'),
          meta: {
            title: '历史病历',
          }
        },
        {
          path: 'records',
          component: () => import('@/views/center/records'),
          meta: {
            title: '缴费记录',
          }
        },
        {
          path: 'prestore',
          component: () => import('@/views/center/prestore'),
          meta: {
            title: '预存费用',
          }
        },
      ]
    },
    {
      path: '*',
      redirect: '/appointment/third'
    }
  ]
})
