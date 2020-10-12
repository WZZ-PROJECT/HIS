import Vue from 'vue'
import Router from 'vue-router'
import ScheduleIndex  from '@/views/admin/schedule'
import RedirectIndex from '@/views/redirect/index'
import LoginIndex from '@/views/login/index'
import LoginAuth from '@/views/login/auth-redirect'
import Error404 from '@/views/error-page/404'
import Error401 from '@/views/error-page/401'
import DashBoardIndex from '@/views/dashboard/index'
import PharmacyIndex from '@/views/pharmacy/index'
import PharmacyHerbal from '@/views/pharmacy/herbal'
import TechnologyIndex from '@/views/technology/index'
import OutpatientIndex from '@/views/outpatient/index'
import MaintainMould from '@/views/outpatient/maintain/mould'
import MaintainInspection from '@/views/outpatient/maintain/inspection'
import DrugModel from '@/views/outpatient/maintain/drugModel'
import HerDrugModel from '@/views/outpatient/maintain/herDrugModel'
import Freguse from '@/views/outpatient/maintain/frequse'
import RegistWorkIndex from '@/views/registWork/index'
import CommonSchedule from '@/views/common/schedule'
import DayReport from '@/views/registWork/dayReport'
import DayReportHis from '@/views/registWork/dayReportHis'
import RolePermission from '@/views/permission/role'
import FinanceCheck from '@/views/finance/check'
import DeptWorkLoad from '@/views/finance/deptWorkload'
import PerWorkLoad from '@/views/finance/perWorkload'
import Monitor from '@/views/cloud/monitor'
import Zipkin from '@/views/cloud/zipkin'
import Swagger from '@/views/cloud/swagger'
import Hystrix from '@/views/cloud/hystrix'
import Kibana from '@/views/cloud/kibana'

import packagePlanIndex  from '@/views/admin/packagePlan'
import CommonPackagePlan from '@/views/common/packagePlan'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'
/* 管理员有关的路由 */
import adminRouter from './admin/admin'
/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: RedirectIndex
      }
    ]
  },
  {
    path: '/login',
    component:  LoginIndex,
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: LoginAuth,
    hidden: true
  },
  // {
  //   path: '/404',
  //   component: Error404,
  //   hidden: true
  // },
  {
    path: '/401',
    component: Error401,
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: 'index',
        component: DashBoardIndex,
        name: 'index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  }
  /** when your routing map is too long, you can split it into small modules **/
  // 404 page must be placed at the end !!!

]
/* 异步挂载的路由 */
export const asyncRoutes = [
  {
    path: '/schedule',
    component: Layout,
    redirect: '/schedule/index',
    name: 'schedule',
    meta: {
      title: '医生排班管理',
      icon: 'people',
      roles:[1,7]
    },
    children: [
      {
        path: 'index',
        component: ScheduleIndex,
        name: 'scheduleIndex',
        meta: { title: '医生排班管理', icon: 'dashboard', noCache: true }
      }
    ]
  },

  {
    path: '/packagePlan',
    component: Layout,
    redirect: '/schedule/index',
    name: 'packagePlan',
    meta: {
      title: '套餐排班管理',
      icon: 'people',
      roles:[1,7]
    },
    children: [
      {
        path: 'index',
        component: packagePlanIndex,
        name: 'packagePlanIndex',
        meta: { title: '套餐排班管理', icon: 'dashboard', noCache: true }
      }
    ]
  },

  {
    path: '/pharmacy',
    component: Layout,
    redirect: '/pharmacy/index',
    meta: {
      title: '药房工作台',
      icon: 'tree-table',
      roles:[4,7]
    },
    children: [
      {
        path: 'index',
        component: PharmacyIndex,
        name: 'pharmacy',
        meta: {
          title: '成药药房工作台',
          noCache: true,
          roles:[4,7]
        },
      },
      {
        path: 'herbal',
        component: PharmacyHerbal,
        name: 'herbal',
        meta: {
          title: '草药药房工作台',
          noCache: true,
          roles:[4,7]
        },
      },
    ]
  },
  {
    path: '/technology',
    component: Layout,
    redirect: '/technology/index',
    children: [
      {
        path: 'index',
        component:TechnologyIndex,
        name: 'technology',
        meta: {
          title: '门诊医技工作台',
          icon: 'skill',
          noCache: true,
          roles:[3,7]
        }
      }
    ]
  },
  {
    path: '/outpatient',
    component: Layout,
    redirect: '/outpatient/index',
    roles:[2],
    children: [
      {
        path: 'index',
        component: OutpatientIndex,
        name: 'outpatient',
        meta: {
          title: '门诊医生工作台',
          icon: 'peoples',
          noCache: true,
          hidden: true,
          roles:[2,7]
        },
      }
    ]
  },
  {
    path: '/outpatient',
    component: Layout,
    redirect: '/outpatient/maintain/mould',
    name: 'mould',
    meta: {
      title: '模板&常用项',
      icon: 'nested',
      roles:[2,7]
    },
    children: [
      {
        path: '/maintain/mould',
        component: MaintainMould,
        name: 'mould',
        meta: { title: '病历模板管理',noCache: true }
      },
      {
        path: '/maintain/inspection',
        component: MaintainInspection,
        name: 'inspection',
        meta: { title: '非药品模板管理',noCache: true }
      },
      {
        path: '/maintain/drugModel',
        component: DrugModel,
        name: 'drugModel',
        meta: { title: '成药药品模板管理', noCache: true },
      },
      {
        path: '/maintain/herDrugModel',
        component: HerDrugModel,
        name: 'herDrugModel',
        meta: { title: '草药药品模板管理', noCache: true }
      },
      {
        path: '/maintain/frequse',
        component: Freguse,
        name: 'frequse',
        meta: { title: '常用项管理', noCache: true }
      },
    ]
  },
  {
    path: '/registWork',
    component: Layout,
    redirect: '/registWork/index',
    name: 'pay',
    meta: {
      title: '门诊收费挂号',
      icon: 'money',
      roles: [5,7]
    },
    children: [
      {
        path: 'index',
        component: RegistWorkIndex,
        name: 'registWork',
        meta: {
          title: '门诊挂号收费',
          noCache: true,
          roles: [5,7]
        },
      },
    ]
  },

  {
    path: '/schedule',
    component: Layout,
    redirect: '/schedule',
    children: [
      {
        path: 'schedule',
        component: CommonSchedule,
        name: 'commonSchedule',
        meta: {
          title: '医生排班表',
          icon: 'table',
          noCache: true
        },
      },
    ]
  },


  {
    path: '/packagePlan',
    component: Layout,
    redirect: '/schedule',
    children: [
      {
        path: 'schedule',
        component: CommonPackagePlan,
        name: 'CommonPackagePlan',
        meta: {
          title: '套餐排班表',
          icon: 'table',
          noCache: true
        },
      },
    ]
  },

  {
    path: '/dayReport',
    component: Layout,
    redirect: '/registWork/dayReport',
    name: 'dayReport',
    meta: {
      title: '门诊日结',
      icon: 'documentation',
      roles: [5,7]
    },
    children: [
      {
        path: 'dayReport',
        component: DayReport,
        name: 'dayReport',
        meta: {
          title: '门诊日结',
          noCache: true,
          roles: [5,7]
        },
      },
      {
        path: 'dayReportHis',
        component: DayReportHis,
        name: 'dayReportHis',
        meta: {
          title: '历史日结',
          noCache: true,
          roles: [5,7]
        },
      },
    ]
  },
  {
    path: '/permission',
    component: Layout,
    redirect: '/permission/role',
    alwaysShow: true, // will always show the root menu
    name: 'Permission',
    meta: {
      title: '用户管理',
      icon: 'lock',
      roles: [1,7] // you can set roles in root nav
    },
    children: [
      {
        path: 'role',
        component:RolePermission,
        name: 'RolePermission',
        meta: {
          title: '角色权限管理',
        }
      }
    ]
  },
  adminRouter,

  {
    path: '/finance',
    component: Layout,
    redirect: '/finance/check',
    name: 'finance',
    meta: {
      title: '门诊财务',
      icon: 'money'
    },
    children: [
      {
        path: 'check',
        component: FinanceCheck,
        name: 'check',
        meta: {
          title: '门诊日结核对',
          noCache: true,
          roles:[1,6,7]
        },
      },
      {
        path: 'deptWorkload',
        component: DeptWorkLoad,
        name: 'deptWorkload',
        meta: {
          title: '科室工作量统计',
          noCache: true,
        },
      },
      {
        path: 'perWorkload',
        component: PerWorkLoad,
        name: 'perWorkload',
        meta: {
          title: '个人工作量统计',
          noCache: true,
        },
      },
    ]
  },
  {
    path: '/cloud',
    component: Layout,
    redirect: '/cloud/monitor',
    name: 'cloud',
    meta: {
      title: '服务监控',
      icon: 'eye-open'
    },
    children: [
      {
        path: 'monitor',
        component: Monitor,
        name: 'monitor',
        meta: {
          title: 'monitor',
          noCache: true,
          roles:[1,7]
        },
      },
      {
        path: 'zipkin',
        component: Zipkin,
        name: 'zipkin',
        meta: {
          title: 'zipkin',
          noCache: true,
          roles:[1,7]
        },
      },
      {
        path: 'swagger',
        component: Swagger,
        name: 'swagger',
        meta: {
          title: 'swagger',
          noCache: true,
          roles:[1,7]
        },
      },
      {
        path: 'hystrix',
        component: Hystrix,
        name: 'hystrix',
        meta: {
          title: 'hystrix',
          noCache: true,
          roles:[1,7]
        },
      },
      {
        path: 'kibana',
        component: Kibana,
        name: 'kibana',
        meta: {
          title: 'kibana',
          noCache: true,
          roles:[1,7]
        },
      },
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]
const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
