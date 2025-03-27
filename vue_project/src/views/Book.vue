<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 240px" v-model="data.name" placeholder="请输入name查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px"  >
      <el-button type="primary" @click="handleAdd" >新增</el-button>
    </div>
    <!--数据显示区域-->
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{color: '#333',fontSize: '17px', backgroundColor: '#eaf4ff'}">
        <el-table-column label="图书封面">
          <template #default="scope">
            <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list="[scope.row.img]" :preview-teleported="true" style="width: 40px; height: 40px;border-radius: 50%; display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="图书名字"/>
        <el-table-column prop="price" label="图书价格"/>
        <el-table-column prop="author" label="图书作者"/>
        <el-table-column prop="num" label="剩余数量"/>
        <el-table-column label="操作" width="100">
          <template #default="scoped">
            <el-button  type="primary" @click="handleBorrow(scoped.row)">借阅</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--弹窗-->
    <el-dialog v-model="data.formVisible" title="公告信息" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" label-width="80px" style="padding: 20px 30px 0 0" :model="data.form">
        <el-form-item prop="img" label="图书封面">
          <el-upload
              action="http://localhost:9999/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture">
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="图书名字" prop="name">
          <el-input  v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="图书价格" prop="price">
          <el-input v-model="data.form.price" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="图书作者" prop="author">
          <el-input v-model="data.form.author" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer" style="padding: 0 0 0 0 ">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确认</el-button>
        </div>

      </template>
    </el-dialog>

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
  rules: {
    name: [
      {required: true, message: "请输入图书名字", trigger: 'blur'}

    ],
    price: [
      {required: true, message: "请输入图书价格", trigger: 'blur'}
    ],
    author: [
      {required: true, message: "请输入图书作者", trigger: 'blur'}
    ],
    img: [
      {required: true, message: "请输入图书封面", trigger: 'blur'}
    ]

  }
})

const load = () =>{
  request.get('/book/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res =>{
    if(res.code === "200"){
      data.tableData = res.data?.list
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

const handleBorrow = (row) => {
  request.post('/borrow/add', row).then(res => {
    if(res.code === '200') {
      ElMessage.success('借阅成功')
    }
  })
}
</script>