<template>
  <div class="bg">
    <div style="width: 350px; height: 350px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1 ); background-color: #ffffff; border-radius: 5px; padding: 20px 20px;">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="text-align: center; font-size: 28px; font-weight: bold; margin-bottom: 30px;">欢 迎 登 入</div>
        <el-form-item prop="username">
          <el-input prefix-icon="User" v-model="data.form.username" size="large" autocomplete="new-password" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="Lock" v-model="data.form.password" size="large" autocomplete="new-password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" style="width: 100%;" v-model="data.form.role">
            <el-option label="管理员" value="ADMIN"></el-option>
            <el-option label="用户" value="USER"></el-option>

          </el-select>
        </el-form-item>
        <div style="display: flex; justify-content: center; margin-bottom: 20px;margin-top: 20px">
          <el-button type="primary" style="width: 100%" @click="handleLogin">
              登  入
          </el-button>
        </div>
        <div style="text-align: right">
          还没有账号吗? 请<a href="/register" style="color:#2c82ff">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import home from "@/views/Home.vue";
const formRef = ref({})
const data = reactive({
  form: {role:"ADMIN"},
  rules: {
    username:[
      {required: true, message: "请输入账号", trigger: 'blur'},
      {min: 3, message: "不能少于三位", trigger: 'blur'}
    ],
    password:[
      {required: true, message: "请输入密码", trigger: 'blur'},
    ]
  }
})
const handleLogin = () => {
  formRef.value.validate((valid) =>{
    if(valid){
      request.post("/login", data.form).then(res => {
      if(res.code === "200"){
        //存储用户消息到浏览器中
        localStorage.setItem("code_user", JSON.stringify(res.data || {}))
        ElMessage.success("登入成功");
        router.push("/manager/home");

      } else {
        ElMessage.error(res.msg)
      }
    })
    }
  })
}


</script>
<style>
.bg {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-image: url("@/assets/img/瑟提.jpg");
  background-size: cover;
  overflow: hidden;
}

</style>