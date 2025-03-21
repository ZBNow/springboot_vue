<template>
  <div class="card" style="width: 35%">
    <div style="color:#2c82ff; font-size: 17px">修改密码</div>
    <div>
      <el-form ref="formRef" label-width="80px" style="padding: 20px 30px 0 0" :model="data.user" :rules="data.rules">
        <el-form-item label="原密码" prop="password" >
          <el-input v-model="data.user.password"  prefix-icon="Lock" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="data.user.newPassword"  prefix-icon="Lock" autocomplete="off" show-password/>
        </el-form-item>
        <el-form-item label="确认密码" prop="new2Password" >
        <el-input v-model="data.user.new2Password"  prefix-icon="Lock" autocomplete="off" show-password/>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" style="padding: 20px" @click="updatePassword">保存</el-button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { reactive,ref } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";


const formRef = ref()
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user',)),
  rules:{
    password:[
      {required: true, message: "请输入密码", trigger: 'blur'},
    ],
    newPassword:[
      {required: true, message: "请输入密码", trigger: 'blur'},
    ],
    new2Password:[
      {required: true, message: "请输入密码", trigger: 'blur'},
    ]
  }
})
const updatePassword = () => {
  formRef.value.validate((valid) => {
    if(valid){
      request.post('/updatePassword', data.user).then(res =>{
        if(res.code === '200'){
          ElMessage.success('修改成功')
          setInterval(() => {
            localStorage.removeItem('code_user')
            location.href = '/login'
          }, 500)
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>