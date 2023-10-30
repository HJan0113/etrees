import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '../views/Main.vue'
import store from '@/store'
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'main',
    component: Main,
    redirect: '/home',
    children: [
      { path: 'home', name: 'home', component: () => import('../views/Navigation/Home.vue') },
      { path: 'result', name: 'result', component: () => import('../views/Navigation/Result.vue') },
      { path: 'message', name: 'message', component: () => import('../views/Navigation/Message.vue') },
      { path: 'about', name: 'about', component: () => import('../views/Navigation/About.vue') },
      { path: 'complete', name: 'complete', component: () => import('../views/Navigation/handBook.vue') },
      { path: 'search', name: 'search', component: () => import('../views/Navigation/Search.vue') },
      { path: 'history', name: 'history', component: () => import('../views/Mall/Home.vue') },
      {
        path: 'person',
        name: 'person',
        component: () => import('../views/Personal/Person.vue'),
        redirect: '/person/personPage',
        children: [
          { path: 'personPage', name: 'personPage', component: () => import('../views/Personal/PersonPage.vue') },
          { path: 'records', name: 'records', component: () => import('../views/Personal/Records.vue') },
          { path: 'orders', name: 'orders', component: () => import('../views/Personal/Orders.vue') },
          { path: 'address', name: 'address', component: () => import('../views/Personal/Address.vue') },
          { path: 'help', name: 'help', component: () => import('../views/Personal/Help.vue') },
          { path: 'feedback', name: 'feedback', component: () => import('../views/Personal/Feedback.vue') },
          { path: 'login', name: 'login', component: () => import('../views/Personal/Details/login.vue') },
          { path: 'auth', name: 'auth', component: () => import('../views/Personal/Details/auth.vue') },
          { path: 'pay', name: 'pay', component: () => import('../views/Personal/Details/pay.vue') },
          { path: 'security', name: 'security', component: () => import('../views/Personal/Details/security.vue') },
          { path: 'tips', name: 'tips', component: () => import('../views/Personal/Details/tips.vue') },
        ]
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login/Login.vue')
  },
  {
    path: '/mall',
    name: 'mall',
    component: () => import('../views/Mall/Home.vue'),
    redirect: '/mall/goods',
    children: [
      { path: 'goods', name: 'goods', component: () => import('../views/Mall/Goods.vue') },
      { path: 'details/:id', name: 'details', component: () => import('../views/Mall/Details.vue') },
      { path: 'buy/:data', name: 'buy', component: () => import('../views/Mall/Buy.vue') },
      { path: 'shopcar', name: 'shopcar', component: () => import('../views/Mall/ShopCar.vue') },
      { path: 'buycar', name: 'buycar', component: () => import('../views/Mall/buyCar.vue') },
      { path: 'address', name: 'address', component: () => import('../views/Mall/Address.vue') },
      { path: 'checkAddress', name: 'checkAddress', component: () => import('../views/Mall/CheckAddress.vue') },
      { path: 'search', name: 'search', component: () => import('../views/Mall/Search.vue') },
    ]
  }
]

const router = new VueRouter({
  routes
})
router.beforeEach((to, from, next) => {
  const index = localStorage.getItem('activeMenu') || ''; // 从本地存储中读取上一次保存的激活菜单项
  console.log("路由跳转前获取本地参数: ",index)
  // store.dispatch('setactive',index)
  next();
});

router.afterEach((to,from) => {
  const index = to.path
  localStorage.setItem('activeMenu', index); // 将当前激活菜单项保存到本地存储
  store.dispatch('setactive',index)
});
export default router
