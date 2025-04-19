<template>
  <!-- 页面加载完成并通过身份校验后再渲染 -->
  <div v-if="isReady" style="height: 100vh; display: flex; flex-direction: column; width:100%">
    <!-- 头部区域 -->
    <div style="height: 60px; display: flex;width:100%">
      <div style="display: flex; padding: 20px; width: 200px; align-items: center; background-color:#3c3c3c;">
        <img src="@/assets/img/dynasty_zheng.jpg" style="width: 40px; height: 40px; border-radius: 50%;" />
        <span style="font-size: 20px;color:#fff; font-weight: bold; margin-left: 5px">大郑王朝</span>
      </div>

      <div style="display: flex; align-items:center; font-size:20px; font-weight:500; border-bottom:1px solid #b1adad;">
        <span style="padding-left: 10px; cursor: pointer;margin-right: 5px;" @click="router.push('/manager/home')">首页</span> /
        <span style="margin-left: 5px;cursor: pointer">{{ router.currentRoute.value.meta.name }}</span>
      </div>

      <div style="flex: 1; border-bottom: 1px solid #b1adad;"></div>
      <div style="width: fit-content; display: flex; align-items: center; border-bottom: 1px solid #b1adad; padding-right: 20px">
        <el-dropdown>
          <div style="display: flex; align-items: center;">
            <img v-if="data.user?.avatar" :src="data.user?.avatar" style="width: 40px;height: 40px; border-radius: 50%;" />
            <img v-else style="width: 40px; height: 40px;border-radius: 50%;" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
            <span style="margin-left: 5px;">{{ data.user?.name }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/updatePassword')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登入</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 主体内容区域 -->
    <div style="display: flex;width:100%">
      <!-- 侧边菜单 -->
      <div style="width: 240px;">
        <el-menu router :default-active="router.currentRoute.value.path" :default-openeds="['1','2']" style="min-height: calc(113vh - 30px);">
          <el-menu-item index="/manager/home">
            <el-icon><House/></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/front/home">
            <el-icon><Reading/></el-icon>
            <span>攻略中心</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Message/></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/notice">系统公告</el-menu-item>
            <el-menu-item index="/manager/introduction">旅游攻略</el-menu-item>
            <el-menu-item v-if="data.user.role === 'ADMIN'" index="/manager/category">攻略分类</el-menu-item>
            <el-menu-item index="/manager/applyIntroduction">帖子发布</el-menu-item>
            <el-menu-item index="/manager/book">图书借阅</el-menu-item>
            <el-menu-item index="/manager/borrow">借阅记录</el-menu-item>
          </el-sub-menu>

          <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Location/></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">用户信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>

      <!-- 数据渲染区域 -->
      <div style="flex: 1; width: 0; padding:10px; background-color: #f2f4ff;">
        <RouterView @updateUser="updateUser" />
      </div>
    </div>
  </div>
</template>

<script setup>
import {House, Location, Message, Reading} from '@element-plus/icons-vue'
import router from "../router/index.js";
import {onMounted, reactive, ref} from "vue"

const data = reactive({
  user: {}
})
const isReady = ref(false) // 控制是否渲染页面

const logout = () => {
  localStorage.removeItem("code_user")
  location.href = '/login'
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('code_user') || '{}')
}

onMounted(() => {
  const userStr = localStorage.getItem("code_user")
  if (userStr) {
    const user = JSON.parse(userStr)
    if (!user.id) {
      location.href = '/login'
    } else {
      data.user = user
      isReady.value = true
    }
  } else {
    location.href = '/login'
  }
})
</script>

<style scoped>
/* 深度作用选择器（Vue 3） */
::v-deep(.el-menu) {
  background-color: #3c3c3c;
  border: none;
}
::v-deep(.el-sub-menu__title) {
  color: #ddd;
  height: 50px;
}
::v-deep(.el-sub-menu__title:hover) {
  background-color: #3c3c3c;
  color: #ddd !important;
}
::v-deep(.el-sub-menu__title .is-active) {
  background-color: #3c3c3c !important;
  color: #ddd !important;
}
::v-deep(.el-menu-item) {
  height: 50px;
  color: #ddd;
}
::v-deep(.el-menu .is-active) {
  background-color: #537bee;
  color: #fff;
}
::v-deep(.el-menu-item:not(.is-active):hover) {
  background-color: #7a9fff;
  color: #333;
}
::v-deep(.el-tooltip__trigger) {
  outline: none;
}
::v-deep(.el-dropdown) {
  cursor: pointer;
}
::v-deep(.el-menu--inline .el-menu-item) {
  padding-left: 48px !important;
}
::v-deep(.el-sub-menu.is-active .el-sub-menu__title) {
  background-color: transparent !important;
  color: #ddd !important;
}
</style>
