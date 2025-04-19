<template>
  <div style="display: flex; width: 100%; align-items: center;">
    <!-- 左侧占位元素（flex: 1）会自动填充剩余空间 -->
    <div style="width: 1170px"></div>

    <!-- 菜单栏，使用 margin-left: auto 将其推到右边 -->
    <el-menu class="el-menu-demo" mode="horizontal" router :default-active="router.currentRoute.value.path" style="flex-grow: 1">
      <el-menu-item index="/front/home" style="font-size: 18px; font-weight: bold;">系统首页</el-menu-item>
      <el-menu-item index="/manager/home" style="font-size: 18px; font-weight: bold;">用户中心</el-menu-item>
      <el-menu-item @click="logout" style="font-size: 18px;font-weight: bold;">退出</el-menu-item>
    </el-menu>

    <!-- 右侧用户信息或者按钮 -->
    <div style="display: flex; align-items: center; padding-right: 20px;">
      <div style="display: flex; align-items: center;" v-if="data.user.role === 'USER' || data.user.role === 'ADMIN'">
        <img v-if="data.user?.avatar" :src="data.user?.avatar" style="width: 40px;height: 40px; border-radius: 50%;" />
        <img v-else style="width: 40px; height: 40px;border-radius: 50%;" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
        <span style="margin-left: 5px;">{{ data.user?.name }}</span>
      </div>

      <div v-else>
        <el-button type="primary" @click="router.push('/login')">登入</el-button>
        <el-button @click="router.push('/register')">注册</el-button>
      </div>
    </div>
  </div>

  <!-- 路由区域 -->
  <div>
    <RouterView/>
  </div>
</template>



<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import lunbo1 from'@/assets/img/lunbo1.jpg'
import lunbo2 from'@/assets/img/lunbo2.jpg'
import lunbo3 from'@/assets/img/lunbo3.jpg'
import lunbo4 from'@/assets/img/lunbo4.jpg'
import router from "@/router/index.js";


const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  introductionData:[],
  carouselData: [lunbo1, lunbo2, lunbo3, lunbo4]
})

const logout = () => {
  localStorage.removeItem("code_user")
  location.href = '/login'
}

const loadIntroduction = () => {
  request.get('/introduction/selectAll').then(res => {
    if(res.code === '200'){
      data.introductionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadIntroduction()

const navTo = (url) => {
  location.href= url
}
</script>
<style scoped>
</style>

