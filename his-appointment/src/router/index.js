import Vue from 'vue'
import Router from 'vue-router'
import index from'@/views/index';
const appointment = () => import('@/views/appointment')
const notice = () => import('@/views/notice')
const third = () => import('@/views/third')
const fourth = () => import('@/views/fourth')
const five = () => import('@/views/five')
const patientPage = () => import('@/views/patientPage')
const patientIntroduce = () => import('@/views/patientIntroduce')
const deptDetail = () => import('@/views/deptDetail')
const result = () => import('@/views/result')
const center = () => import('@/views/center/index');
const centerIndex  = () => import('@/views/center/center');
const orders = () => import('@/views/center/orders');
const doctors = () => import('@/views/center/doctors');
const history = () => import('@/views/center/history');
const records = () => import('@/views/center/records');
const edit = () => import('@/views/center/edit');
const prestore = () => import('@/views/center/prestore');
const user = () => import('@/views/center/user');
const login = () => import('@/views/login');
const information = () => import('@/views/information')

Vue.use(Router)

const originalPush = Router.prototype.push
Router.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject) return originalPush.call(this, location, onResolve, onReject)
  return originalPush.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: index,
      children: [
        {
          path: 'appointment',
          component: appointment,
          children: [
            {
              path: 'notice',
              component: notice,
              meta: {
                hideLayout: true
              }
            },
            {
              name: 'third',
              path: 'third',
              component: third,
              meta: {
                keep: true,
                title: localStorage.APP_FROM === "applet" ? '' : '济南槐荫慈光诊所',
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
              name: 'deptDetail',
              path: 'deptDetail',
              component: deptDetail,
              meta: {
                keep: true,
                title: '科室介绍'
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
            {
              path: '*',
              redirect: '/appointment/third'
            },
          ]
        },
        {
          path: 'center',
          component: center,
          children: [{
              path: '/',
              component: centerIndex,
              meta: {
                title: '个人中心',
                hideBack: true
              }
            },
            {
              path: 'orders',
              component: orders,
              meta: {
                title: '预约记录',
              }
            },
            {
              path: 'doctors',
              component: doctors,
              meta: {
                title: '关注医生',
              }
            },
            {
              path: 'history',
              component: history,
              meta: {
                title: '历史病历',
              }
            },
            {
              path: 'records',
              component: records,
              meta: {
                title: '缴费记录',
              }
            },
            {
              path: 'prestore',
              component: prestore,
              meta: {
                title: '预存费用',
              }
            },
            {
              path: 'user',
              component: user,
              meta: {
                title: '用户信息',
              }
            },
            {
              path: 'edit',
              component: edit,
              meta: {
                title: '编辑个人信息',
              }
            },
            // {
            //   path: '*',
            //   redirect: '/'
            // },
          ]
        },
        {
          path: '/',
          redirect: '/appointment/notice'
        }
      ]
    },
    {
      path: '/information',
      name: 'information',
      component: information,
      meta: {
        keep: true,
        title: '动态',
        hideBack: true
      }
    },
    {
      path: '/login',
      component: login,
      meta: {
        title: '绑定个人信息',
      }
    },
    {
      path: '*',
      redirect: '/appointment/third'
    }
  ]
})
