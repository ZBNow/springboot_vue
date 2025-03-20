<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 240px" v-model="data.title" placeholder="请输入title查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px"  >
      <el-button type="primary" @click="handleAdd" >新增</el-button>

    </div>
    <!--数据显示区域-->
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{color: '#333',fontSize: '17px', backgroundColor: '#eaf4ff'}">
        <el-table-column prop="title" label="标题"/>
        <el-table-column prop="content" label="内容">
          <template v-slot="scope">
            <span v-if="data.user.role === 'USER'">{{ scope.row.content}}</span>
            <span v-if="data.user.role === 'USER'">您暂无权限查看</span>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="时间"/>
        <el-table-column label="操作" width="100">
          <template #default="scoped"  v-if="data.user.role === 'ADMIN'">
            <el-button circle icon="Edit" type="primary" @click="handleEdit(scoped.row)"></el-button>
            <el-button circle icon="Delete" type="danger" @click="handleDel(scoped.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div>
      <el-dialog v-model="data.formVisible" title="公告信息" width="500" destroy-on-close>
        <el-form ref="formRef" :rules="data.rules" label-width="80px" style="padding: 20px 30px 0 0" :model="data.form">
          <el-form-item label="公告标题" prop="title" >
            <el-input placeholder="请输入公告标题" v-model="data.form.title" autocomplete="off" />
          </el-form-item>
          <el-form-item label="公告内容" prop="content">
            <el-input type="textarea" :rows="3" placeholder="请输入公告内容" v-model="data.form.content" autocomplete="off" />
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
  rules: {
    title: [
      {required: true, message: "请输入公告标题", trigger: 'blur'}

    ],
    content: [
      {required: true, message: "请输入公告内容", trigger: 'blur'}
    ]

  }
})

const load = () =>{
  request.get('/notice/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
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
  request.post('/notice/add', data.form).then(res => {
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
  request.put('/notice/update', data.form).then(res => {
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
    request.delete('/notice/delete/' + id).then(res => {
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
</script>