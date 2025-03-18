<template xmlns="">

  <div class="card" style="margin-bottom: 5px">
    <el-input clearable @clear="load" style="width: 240px" v-model="data.username" placeholder="请输入username查询" :prefix-icon="Search"></el-input>
    <el-input clearable @clear="load" style="width: 240px" v-model="data.name" placeholder="请输入name查询" :prefix-icon="Search"></el-input>
    <el-button type="primary" style="margin-left: 5px" @click="load">查 询</el-button>
    <el-button type="info" style="margin-left: 5px" @click="reset">重 置</el-button>

  </div>

  <div class="card" style="margin-bottom: 5px">
    <el-button type="primary" @click="handleAdd">新增</el-button>
    <el-button type="danger" @click="deleteBatch">批量删除</el-button>
    <el-button type="info" @click="exportData">批量导出</el-button>
    <el-upload
        :show-file-list="false"
        action="http://localhost:9999/admin/import"
        :on-success="handleImport"
        style="display: inline-block; margin-left: 10px">
       <el-button type="success">批量导入</el-button>
    </el-upload>
  </div>

  <div>
    <el-dialog v-model="data.formVisible" title="管理员信息" width="500" destroy-on-close>
      <el-form ref="formRef" :rules="data.rules" label-width="80px" style="padding: 20px 30px 0 0" :model="data.form">
        <el-form-item label="账号" prop="username" >
          <el-input v-model="data.form.username" autocomplete="off" />
        </el-form-item>
        <el-form-item label="名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="data.form.phone" autocomplete="off" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.form.email" autocomplete="off" />
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
      <template #footer>
        <div class="dialog-footer" style="padding: 0 0 0 0 ">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确认</el-button>
        </div>

      </template>
    </el-dialog>
  </div>
  <!-- table组件，用户信息展示区域-->
  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange"
              :header-cell-style="{color: '#333',fontSize: '17px', backgroundColor: '#eaf4ff'}">
      <el-table-column type="selection" width="55"/>
      <el-table-column label="头像" width="100px">
        <template #default="scope">
          <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" :preview-teleported="true" style="width: 40px; height: 40px;border-radius: 50%; display: block"/>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="账号"  />
      <el-table-column prop="phone" label="电话"  />
      <el-table-column prop="name" label="名称"/>
      <el-table-column prop="email" label="邮箱"  />
      <el-table-column label="操作" width="100">
        <template #default="scoped">
          <el-button circle icon="Edit" type="primary" @click="handleEdit(scoped.row)"></el-button>
          <el-button circle icon="Delete" type="danger" @click="handleDel(scoped.row.id)"></el-button>
        </template>
      </el-table-column>


    </el-table>
  </div>
  <!--  分页组件，用于设置每页的大小和跳转到下一页-->
  <div class="card" style="margin-bottom: 5px">
    <el-pagination v-model:current-page="data.pageNum"
                   v-model:page-size="data.pageSize"
                   :total="data.pageTotal"
                   :page-sizes="[5, 10, 20]"
                   layout="total, sizes, prev, pager, next, jumper"
                   @current-change="load"
                   @size-change="load"/>
  </div>
</template>
<script setup>
import { Search } from '@element-plus/icons-vue';
import {reactive, ref} from 'vue'
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  name: '',
  username: '',
  pageNum: 1,
  pageSize: 5,
  pageTotal: 6,
  tableData: [],
  formVisible: false,
  form: {},
  //rules验证规则
  rows: [], // 存储批量选择的数据
  rules: {
    username: [
      {required: true, message: "请输入账号", trigger: 'blur'}
    ],
    name: [
      {required: true, message: "请输入账号", trigger: 'blur'}
    ],
    phone: [
      {required: true, message: "请输入账号", trigger: 'blur'}
    ],
    email: [
      {required: true, message: "请输入账号", trigger: 'blur'}
    ]
  },
})
const load = () =>{
  request.get('/admin/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      username : data.username,
      name: data.name
    }
  }).then(res =>{
    if(res.code === '200'){
      data.pageTotal = res.data.total;
      data.tableData = res.data.list;
    } else {
      ElMessage.error(res.msg)
    }
    console.log("显示form的信息",data.form)
  })
}
load()
const reset = () => {
  data.username = null;
  data.name = null;
  load();
}
const handleAdd = () => {
  data.formVisible = true;
  data.form = {};//编辑的要提交的表单
}
const confirm = () => {
  //formRef进行验证，表单项是否进行正确填写
  formRef.value.validate((valid) =>{//valid检测所有表单项是否填写正确，正确true
    if(valid){
      request.post("/admin/add", data.form).then(res => {
        if(res.code === '200'){
          data.formVisible = false;
          ElMessage.success('信息添加成功')
          load();
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const update = () => {
  formRef.value.validate((valid) =>{//valid检测所有表单项是否填写正确，正确true
    if(valid){
      request.put("/admin/update", data.form).then(res => {
        if(res.code === '200'){
          data.formVisible = false;
          ElMessage.success('信息修改成功')
          load();
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))//深度拷贝 JSON.string(row)将row转化为字符串，JSON.parse转化为json格式
  data.formVisible = true;
}
const save = () => {
  data.form.id ? update() : confirm()
}
const handleDel = (id) => {
  ElMessageBox.confirm("您确认要删除数据吗，删除后无法恢复", "删除确认" , {type : 'warning'}).then(res =>{
    request.delete('/admin/delete/' + id).then(res => {
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
const handleSelectionChange = (rows) => {
  data.rows = rows
  console.log(rows)
}
const deleteBatch = () => {
  if(data.rows.length === 0){
    ElMessage.warning("请选择数据")
    return
  }
  ElMessageBox.confirm("您确认要删除数据吗，删除后无法恢复", "删除确认" , {type : 'warning'}).then(res =>{//跳出弹窗来选择是否删除ElMessageBox
    request.delete('/admin/deleteBatch', {data: data.rows}).then(res => {
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
const exportData = () => {
  let url = `http://localhost:9999/admin/export?username=${data.username}&name=${data.name}` + `&token=${data.user.token}`
  console.log(data.username,data.name)
  window.open(url)
}
const handleImport = (res) => {
  if(res.code === '200'){
    ElMessage.success('批量导入成功')
    load()
  } else {
    ElMessage.error(res.msg)
  }
}
const handleFileSuccess = (res) =>{
  data.form.avatar = res.data
}
</script>
