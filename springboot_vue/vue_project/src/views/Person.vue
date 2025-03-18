<template>
  <div class="card" style="width: 50%;">
    <div>
      <el-form label-width="80px" style="padding: 20px 30px 0 0" :model="data.user">
        <el-form-item label="账号">
          <el-input v-model="data.user.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="data.user.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="data.user.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="data.user.email" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="avatar" label="头像">
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture">
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>

      </el-form>
    </div>
    <div style="text-align: center">
      <el-button type="primary" style="padding: 18px 35px" @click="update">保存</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}')
})
const handleFileSuccess = (res) => {
  data.user.avatar = res.data

}
const update = () => {
  let url
  if("ADMIN" === data.user.role){
    url = "/admin/update"
  }
  if("USER" === data.user.role){
    url = "/user/update"
  }
  request.put(url, data.user).then(res =>{
    if(res.code === '200'){
      ElMessage.success("信息修改成功")
      console.log(data.user)
      localStorage.setItem('code_user', JSON.stringify(data.user))
    }
  })
}
</script>
