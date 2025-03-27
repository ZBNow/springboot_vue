<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 240px" v-model="data.name" placeholder="请输入name查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>
    <!--数据显示区域-->
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{color: '#333',fontSize: '17px', backgroundColor: '#eaf4ff'}">
        <el-table-column prop="userName" label="借阅人姓名"/>
        <el-table-column prop="bookName" label="书名"/>
      </el-table>
    </div>
    <!--弹窗-->
  </div>
</template>
<script setup>
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import { reactive, ref } from 'vue'
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData:[],
  form: {},
  formVisible: false,
  name: null,

})

const load = () =>{
  request.get('/borrow/selectAll').then(res =>{
    if(res.code === "200"){
      data.tableData = res.data
      data.total = res.data?.total
    } else {
      Element.error(res.msg)
    }
  })
}
load()

const handleAdd = () => {
  data.form = {}
  data.formVisible = true
}

const add =() => {
  request.post('/book/add', data.form).then(res => {
    if(res.code === '200'){
      ElMessage.success('新增成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
      data.formVisible = false
    }

  })
}
const update = () => {
  request.put('/book/update', data.form).then(res => {
    if(res.code === '200'){
      ElMessage.success('更新成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const save = () => {
  formRef.value.validate((valid) =>{
    if(valid){
      data.form.id ? update() : add()
    }
  })
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleDel = (id) => {
  ElMessageBox.confirm("您确认要删除数据吗，删除后无法恢复", "删除确认" , {type : 'warning'}).then(res =>{
    request.delete('/book/delete/' + id).then(res => {
      if(res.code === '200'){
        ElMessage.success('删除成功')
        console.log("删除成功")
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err =>{
  })
}

const reset = () => {
  data.title = null
  load()
}

const handleFileSuccess = (res) => {
  data.form.img = res.data
}

const handleBorrow = () => {
  request.post('/borrow/add', data.form).then(res => {
    if(res.code === '200') {

      ElMessage.success('借阅成功')
    }
  })
}
</script>