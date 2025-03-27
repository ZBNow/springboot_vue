<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 240px" v-model="data.title" placeholder="请输入title查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px" v-if="'USER' === data.user.role">
      <el-button type="primary" @click="handleAdd">提交请假申请</el-button>
    </div>
  </div>


  <el-dialog v-model="data.formVisible" title="请假信息" width="500" destroy-on-close>
    <el-form ref="formRef" :rules="data.rules" label-width="80px" style="padding: 20px 30px 0 0" :model="data.form">
      <el-form-item label="请假标题" prop="title" v-if="'USER' === data.user.role">
        <el-input placeholder="请输入f分类标题" v-model="data.form.title" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="请假说明" prop="content" v-if="'USER' === data.user.role">
        <el-input type="textarea" :rows="3" placeholder="请输入分类标题" v-model="data.form.content" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="审核状态" prop="status" v-if="'ADMIN' === data.user.role">
        <el-radio-group v-model="data.form.status">
          <el-radio-button label="审核通过" value="审核通过"/>
          <el-radio-button label="审核拒绝" value="审核拒绝"/>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="拒绝原因" prop="reason" v-if="'ADMIN' === data.user.role && data.form.status === '审核拒绝'">
        <el-input v-model="data.form.reason" autocomplete="off" placeholder="请输入拒绝原因"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer" style="padding: 0 0 0 0 ">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">确认</el-button>
      </div>
    </template>
  </el-dialog>
<!--  表格显示数据-->
  <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange"
              :header-cell-style="{color: '#333',fontSize: '17px', backgroundColor: '#eaf4ff'}">
      <el-table-column type="selection" width="55"/>
      <el-table-column label="请假标题" prop="title">
        <template #default="scope">
          <el-image v-if="scope.row.avatar" :src="scope.row.avatar" :preview-src-list="[scope.row.avatar]" :preview-teleported="true" style="width: 40px; height: 40px;border-radius: 50%; display: block"/>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="请假内容"/>
      <el-table-column prop="userName" label="请假人"/>
      <el-table-column prop="time" label="请假时间"/>
      <el-table-column prop="status" label="审核状态">
        <template v-slot="scoped">
          <el-tag type="warning" v-if="scoped.row.status === '待审核'">待审核</el-tag>
          <el-tag type="danger" v-if="scoped.row.status === '审核拒绝'">审核拒绝</el-tag>
          <el-tag type="success" v-if="scoped.row.status === '审核通过'">审核通过</el-tag>

        </template>
      </el-table-column>
      <el-table-column prop="reason" label="审核说明"/>

      <el-table-column label="操作" width="100">
        <template #default="scoped" v-if="'USER' === data.user.role">
          <el-button :disabled="scoped.row.status !=='待审核'" circle icon="Edit" type="primary" @click="handleEdit(scoped.row)"></el-button>
          <el-button :disabled="scoped.row.status !=='待审核'" circle icon="Delete" type="danger" @click="handleDel(scoped.row.id)"></el-button>
        </template>
        <template #default="scoped" v-if="'ADMIN' === data.user.role">
          <el-button :disabled="scoped.row.status !=='待审核'" type="primary" @click="handleEdit(scoped.row)">审核</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
  <!--  分页组件，用于设置每页的大小和跳转到下一页-->
  <div class="card" style="margin-bottom: 5px">
    <el-pagination v-model:current-page="data.pageNum"
                   v-model:page-size="data.pageSize"
                   :total="data.total"
                   :page-sizes="[5, 10, 20]"
                   layout="total, sizes, prev, pager, next, jumper"
                   @current-change="load"
                   @size-change="load"/>
  </div>

</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const formRef = ref()
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  pageNum: 1,
  pageSize: 5,
  total: 0,
  title: null,
  tableData: [],
  form: {},
  formVisible: false,
  rules: {
    title: [
      {required: true, message: '请填写请假标题', trigger: 'blur'}
    ],
    content: [
      {required: true, message: '请填写请假内容', trigger: 'blur'}
    ]
  }
})

const load = () => {
  request.get('apply/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
    }
  }).then(res => {
    if(res.code === '200'){
      data.tableData = res.data?.list
      data.total = res.data?.total

    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
const handleAdd = () => {
  data.form = {}
  data.formVisible = true
  data.form.status = '待审核'
  data.form.useId = data.user.id
}

const add = () => {
  request.post("/apply/add", data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('提交成功， 等待管理员审核')
      load()
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  formRef.value.validate((valid) =>{//valid检测所有表单项是否填写正确，正确true
    if(valid){
      request.put("/apply/update", data.form).then(res => {
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
const save = () => {
  formRef.value.validate(valid => {
    if (valid){
      data.form.id ? update(): add()
    }
  })
}
const handleDel = (id) => {
  ElMessageBox.confirm("您确认要删除数据吗，删除后无法恢复", "删除确认" , {type : 'warning'}).then(res =>{
    request.delete('/apply/delete/' + id).then(res => {
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
const handleEdit = (row) => {
   data.form = JSON.parse(JSON.stringify(row))
   data.formVisible = true

}
</script>