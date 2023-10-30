import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/Main.vue'
import jwtDecode from 'jwt-decode'
import store from '@/store'
import { Message } from 'element-ui'
Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'main',
        component: HomeView,
        redirect: '/home',
        children: [
            {
                path: 'home',
                name: 'home',
                meta: { title: '首页', icon: 'el-icon-s-home' },
                component: () => import('../views/Home/Home.vue')
            },
            //用户管理
            {
                path: 'user',
                name: 'user',
                component: {
                    render: h => h('router-view')
                },
                meta: { title: '用户管理', icon: 'el-icon-user-solid' },
                children: [
                    {
                        path: 'ulist',
                        name: 'userlist',
                        meta: { title: '用户列表' },
                        component: () => import('../views/UserManage/User.vue')
                    },
                ]
            },
            //数据管理
            {
                path: 'data',
                name: 'data',
                component: {
                    render: h => h('router-view')
                },
                meta: { title: '数据管理', icon: 'el-icon-s-data' },
                children: [
                    {
                        path: 'dlist',
                        name: 'dataList',
                        meta: { title: '收录列表' },
                        component: () => import('../views/Data/List.vue')
                    },
                    {
                        path: 'rlist',
                        name: 'RecordList',
                        meta: { title: '识别记录' },
                        component: () => import('../views/Data/Records.vue')
                    },
                ]
            },
            //员工管理
            {
                path: 'emp',
                name: 'emp',
                component: {
                    render: h => h('router-view')
                },
                meta: { title: '员工管理', icon: 'el-icon-s-custom' },
                children: [
                    {
                        path: 'elist',
                        name: 'emplist',
                        meta: { title: '员工列表' },
                        component: () => import('../views/EmpManage/Emp.vue')
                    },
                    {
                        path: 'elimits',
                        name: 'emplimits',
                        meta: { title: '权限管理' },
                        component: () => import('../views/EmpManage/Limits.vue')
                    },
                ]
            },
            //商城管理
            {
                path: 'mall',
                name: 'mall',
                component: {
                    render: h => h('router-view')
                },
                meta: { title: '商城管理', icon: 'el-icon-shopping-cart-1' },
                children: [
                    {
                        path: 'imall',
                        name: 'indexManage',
                        meta: { title: '目录管理' },
                        component: () => import('../views/MallManage/IMange.vue')
                    },
                    {
                        path: 'mall',
                        name: 'mallManage',
                        meta: { title: '商品管理' },
                        component: () => import('../views/MallManage/Mall.vue')
                    },
                    {
                        path: 'omall',
                        name: 'ordermanage',
                        meta: { title: '订单管理' },
                        component: () => import('../views/MallManage/OManage.vue')
                    },
                ]
            },
            //平台维修
            {
                path: 'maintain',
                name: 'maintain',
                component: {
                    render: h => h('router-view')
                },
                meta: { title: '平台维修', icon: 'el-icon-s-grid' },
                children: [
                    {
                        path: 'feedback',
                        name: 'Feedback',
                        meta: { title: '用户反馈' },
                        component: () => import('../views/Maintain/Feedback.vue')
                    }, {
                        path: 'log',
                        name: 'Log',
                        meta: { title: '日志管理' },
                        component: () => import('../views/Maintain/Log.vue')
                    },
                ]
            },
        ]
    },
    {
        path: '/login',
        name: 'login',
        component: () => import('../views/EmpManage/Login.vue')
    },
    {
        path: '/personal',
        name: 'personal',
        component: () => import('../views/EmpManage/Private.vue')
    }
]

const router = new VueRouter({
    routes
})

//路由守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem("EmpToken")
    console.log("从哪里来: ", from)
    console.log("去往哪里: ", to)
    if (token && to.path !== '/login') {
        //判断token是否过期
        const decodedToken = jwtDecode(token)
        console.log("token过期时间: ",decodedToken)
        const currentTime = Date.now() /1000
        console.log("当前时间: ",currentTime)
        if (decodedToken.exp < currentTime) {
            Message({
                message: '登录已过期，请重新登录',
                type: 'warning'
            })
            localStorage.clear('EmpToken')
            store.dispatch('clear')
            next('/login')
            // next()
        } else {
            next()
        }
    } else if (token && to.path === '/login') {
        // 有token 但是去 login页面 不通过 重定向到首页
        Message({
            message: '您已经登录,无需再登录',
            type: 'warning'
        })
        next('/')
    } else if (!token && to.path !== '/login') {
        // 没有token 但不是去 login页面 不通过（未登录不给进入）
        Message({
            message: '请先登录',
            type: 'warning'
        })
        next('/login')
    } else {
        // 剩下最后一种 没有token 但是去 login页面 通过
        next()
    }

})
export default router
