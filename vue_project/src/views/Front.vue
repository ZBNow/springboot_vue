<template>
  <div style="background-color: #f8f9fb; min-height: 100vh; " >
    <div style="display: flex; align-items: center; justify-content: space-between; width: 100%;">
      <el-menu
          style="flex: 1"
          :default-active="1"
          class="el-menu-demo"
          mode="horizontal">
        <el-menu-item index="1" style="font-size: 18px;">系统首页</el-menu-item>
        <el-menu-item index="3" @click="router.push('/manager/home')" style="font-size: 18px;">用户中心</el-menu-item>
        <el-menu-item index="2" @click="logout" style="font-size: 18px;">退出</el-menu-item>
      </el-menu>

      <div style="display: flex; align-items: center; padding-right: 20px;">

        <div style="display: flex; align-items: center;" v-if="data.user.role === 'USER'">
          <img v-if="data.user?.avatar" :src="data.user?.avatar" style="width: 40px;height: 40px; border-radius: 50%;"/>
          <img v-else style="width: 40px; height: 40px;border-radius: 50%;"
               src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"/>
          <span style="margin-left: 5px;">{{ data.user?.name }}</span>
        </div>

        <div v-else>
          <el-button type="primary" @click="router.push('/login')">登入</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
      </div>
    </div>



    <!--轮播图-->
    <div style="margin-bottom: 20px; margin-top: 10px">
      <el-carousel height="400px">
        <el-carousel-item v-for="item in data.carouselData" :key="item" style="display: flex; justify-content: center; align-items: center;">
          <img :src="item" alt="" style="height: 400px; width: 70%"/>
        </el-carousel-item>
      </el-carousel>
    </div>

    <!--攻略内容-->
    <div style="width: 80%; margin: 20px auto">
      <div style="font-size: 20px; border-left: 5px solid #37d337; padding-left: 5px; height: 30px; ">旅游攻略</div>
      <div style="margin-top: 20px; display: flex; grid-gap: 20px" v-for="item in data.introductionData">
        <div style="flex: 1">
          <img @click="navTo('/front/introductionDetail?id=' + item.id)" :src="item.img" style="width: 100%; height: 220px; display: block; border-radius: 5px; cursor: pointer" />
        </div>
        <div style="flex: 3">
          <div style="font-size: 20px; font-weight: bold; cursor: pointer;" @click="navTo('/front/introductionDetail?id=' + item.id)">{{ item.title }}</div>
          <div style="margin-top: 10px; font-size: 15px; color: #666666; line-height: 25px; height: 125px; text-align: justify;" class="line5">{{ item.description }}</div>
          <div style="display: flex; align-items: center; margin-top: 10px; grid-gap: 10px">
            <img :src="item.userAvatar" alt="" style="width: 30px; height: 30px; border-radius: 50%">
            <div style="font-size: 15px">{{ item.userName }}</div>
            <div style="color: #666666">{{ item.time }}</div>
          </div>
        </div>
      </div>

    </div>

    <div style="width: 80%; margin: 20px auto">
      <div style="font-size: 20px; border-left: 5px solid #37d337; padding-left: 5px; height: 30px; ">旅游攻略</div>
      <div style="margin-top: 20px;">
        <el-row :gutter="10">
          <el-col :span="6" v-for="item in data.introductionData" style="margin-bottom: 20px">
            <img :src="item.img" alt="" style="width: 100%; height: 250px; border-radius: 5px"/>
            <div style="font-size: 16px; font-weight: bold; margin-top: 5px">{{item.title}}</div>
            <div style="display: flex; align-items: center; grid-gap: 10px;">
              <img :src="item.userAvatar" style="width: 30px; height: 30px; border-radius: 50%"/>
              <div style="font-size: 15px;">{{ item.userName }}</div>
              <div style="color:#666666">{{ item.time }}</div>
            </div>
          </el-col>
        </el-row>

      </div>
    </div>

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
.line5{
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 5;
  overflow: hidden
}
</style>