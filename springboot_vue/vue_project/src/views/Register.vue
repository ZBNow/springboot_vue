<template>
  <div class="bg">
    <div style="width: 350px; height: 350px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1 ); background-color: #ffffff; border-radius: 5px; padding: 20px 20px;">
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <div style="text-align: center; font-size: 28px; font-weight: bold; margin-bottom: 30px;">欢 迎 注 册</div>
        <el-form-item prop="username">
          <el-input prefix-icon="User" v-model="data.form.username" size="large" autocomplete="new-password" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="Lock" v-model="data.form.password" size="large" autocomplete="new-password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input prefix-icon="Lock" v-model="data.form.confirmPassword" size="large" autocomplete="new-password" placeholder="请再次输入密码" show-password></el-input>
        </el-form-item>

        <div style="display: flex; justify-content: center; margin-bottom: 20px;margin-top: 20px">
          <el-button type="primary" style="width: 100%" @click="handleRegister">
              注  册
          </el-button>
        </div>
        <div style="text-align: right">
          已有账号 请<a href="/login" style="color:#2c82ff">登入</a>
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
const validatePass = (rule, value, callback) =>{
  //value 表示用户输入的确认密码
  if(value === ''){
    callback(new Error("请在此输入密码"))
  } else if(value !== data.form.password) {
    callback(new Error("两次输入密码不同"))
  } else {
    callback()
  }

}
const formRef = ref({})
const data = reactive({
  form: {},
  rules: {
    username:[
      {required: true, message: "请输入账号", trigger: 'blur'},
      {min: 3, message: "不能少于三位", trigger: 'blur'}
    ],
    password:[
      {required: true, message: "请输入密码", trigger: 'blur'},
    ],
    confirmPassword: [
      {validator: validatePass, trigger: 'blur'},
      {required: true, message: "请在此输入密码", trigger: 'blur'},

    ]
  }
})
const handleRegister = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post("/register", data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("注册成功");
        } else {
          ElMessage.error(res.msg);
        }
      })
    } else {
      ElMessage.error("请填写完整的注册信息");
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
  background-image: url("@/assets/img/永恩.jpg");
  background-size: cover;
  overflow: hidden;
}

</style>