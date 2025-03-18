import { createRouter, createWebHistory } from 'vue-router'
//定义路由的文件

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', redirect: '/manager/home'},
    { path: '/notFound', component: () => import('../views/404.vue')},
    { path: '/login', component: () => import('../views/Login.vue')},
    { path: '/register', meta:{name: '注册信息'}, component: () => import('../views/register.vue')},
    { path: '/:pathMatch(.*)', redirect: '/notFound'},
    {path: '/manager', component: () => import('../views/Manager.vue'),
      children: [
        { path: 'home', meta:{name: '主页'}, component: () => import('../views/Home.vue')},
        { path: 'admin', meta:{name: '管理员信息'}, component: () => import('../views/admin.vue')},
        { path: 'user', meta:{name: '用户信息'}, component: () => import('../views/user.vue')},
        { path: 'person', meta:{name: '个人中心'}, component: () => import('../views/Person.vue')},

      ]
    }
  ]
})

export default router
