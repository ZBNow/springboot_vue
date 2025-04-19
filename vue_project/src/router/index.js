import { createRouter, createWebHistory } from 'vue-router'
//定义路由的文件

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/', redirect: '/entry'},
    { path: '/notFound', component: () => import('../views/404.vue')},
    { path: '/entry', component: () => import('../views/demo.vue')},
    { path: '/login', component: () => import('../views/Login.vue')},
    { path: '/register', meta:{name: '注册信息'}, component: () => import('../views/register.vue')},
    { path: '/:pathMatch(.*)', redirect: '/notFound'},
    {path: '/manager', component: () => import('../views/Manager.vue'),
      children: [
        { path: 'home', meta:{name: '主页'}, component: () => import('../views/Home.vue')},
        { path: 'admin', meta:{name: '管理员信息'}, component: () => import('../views/admin.vue')},
        { path: 'user', meta:{name: '用户信息'}, component: () => import('../views/user.vue')},
        { path: 'person', meta:{name: '个人中心'}, component: () => import('../views/Person.vue')},
        { path: 'UpdatePassword', meta:{name: '修改密码'}, component: () => import('../views/UpdatePassword.vue')},
        { path: 'notice', meta:{name: '系统公告'}, component: () => import('../views/Notice.vue')},
        { path: 'introduction', meta:{name: '旅游攻略'}, component: () => import('../views/Introduction.vue')},
        { path: 'category', meta:{name: '攻略分类'}, component: () => import('../views/Category.vue')},
        { path: 'apply', meta:{name: '请假申请'}, component: () => import('../views/Apply.vue')},
        { path: 'book', meta:{name: '图书借阅'}, component: () => import('../views/Book.vue')},
        { path: 'borrow', meta:{name: '借阅记录'}, component: () => import('../views/Borrow.vue')},
        { path: 'applyIntroduction', meta:{name:'贴子发布审核'}, component:()=> import('../views/ApplyIntroduction.vue')}
      ]
    },
    {path: '/front', component: () => import('../views/Front.vue'),
      children: [
        {path: 'home', meta:{name: '首页'}, component: () => import('../views/Front.vue')}
      ]
    },
    {path: '/front/introductionDetail', component: () => import('../views/introductionDetail.vue')}
  ]
})

export default router
