<template>
  <div>
  <!--顶部常规组件-->
    <div>
      <div class="card" style="margin-bottom: 5px">
        <el-input clearable @clear="load" style="width: 240px" v-model="data.title" placeholder="请输入title查询" :prefix-icon="Search"></el-input>
        <el-button type="primary" @click="load">查 询</el-button>
        <el-button @click="reset">重 置</el-button>
      </div>
      <div class="card" style="margin-bottom: 5px" v-if="'USER' === data.user.role">
        <el-button type="info" @click="handleAdd">发布帖子</el-button>
      </div>
    </div>
    <!--  表格显示数据-->
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange"
                :header-cell-style="{color: '#333',fontSize: '17px', backgroundColor: '#eaf4ff'}">
        <el-table-column type="selection" width="55"/>
        <el-table-column label="攻略标题" prop="title"></el-table-column>
        <el-table-column prop="content" label="攻略内容">
          <template v-slot="scoped">
            <el-button @click="contentView(scoped.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column v-if="data.user.role === 'ADMIN'" prop="userName" label="请假人"/>
        <el-table-column prop="categoryTitle" label="攻略分类"/>
        <el-table-column prop="time" label="发布时间"/>
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
            <el-button :disabled="scoped.row.status ==='审核拒绝'" circle icon="Edit" type="primary" @click="handleEdit(scoped.row)"></el-button>
            <el-button circle icon="Delete" type="danger" @click="handleDel(scoped.row.id)"></el-button>
          </template>
          <template #default="scoped" v-if="'ADMIN' === data.user.role">
            <el-button type="primary" @click="handleEdit(scoped.row)">审核</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--帖子的发布-->
    <el-dialog ref="formRef" v-model="data.formVisible" title="攻略信息" width="60%" destroy-on-close>
      <el-form v-if="data.user.role === 'USER'"label-width="80px" style="padding: 20px 30px 0 0;" :model="data.form">
        <el-form-item prop="avatar" label="攻略图片">
          <el-upload
              action="http://43.143.94.185:9999/files/upload"
              :headers="{ token: data.user.token }"
              :on-success="handleFileSuccess"
              list-type="picture">
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="攻略标题" prop="title">
          <el-input placeholder="请输入攻略标题" v-model="data.form.title" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="攻略标题" prop="title">
          <el-select
              v-model="data.form.categoryId"
              placeholder="请选择攻略分类"
              style="width: 30%;">
            <el-option
                v-for="item in data.categoryData"
                :key="item.id"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <!--富文本编辑器-->
        <el-form-item label="攻略内容" prop="content">
          <div style="border: 1px solid #ccc; width: 100%">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"/>
            <Editor
                style="height: 250px; overflow: hidden"
                v-model="data.form.content"
                :mode="mode"
                :defaultConfig="editorConfig"
                @onCreated="handleCreated"/>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer" style="padding: 0 0 0 0 ">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确认</el-button>
        </div>
      </template>
    </el-dialog>
    <!--管理员审核弹窗-->
    <el-dialog v-if="data.user.role === 'ADMIN'" v-model="data.formVisible2" title="请假信息" width="500" destroy-on-close>
      <el-form ref="formRef" label-width="90px" style="padding: 20px 30px 0 0" :model="data.form">
        <el-form-item label="审核状态" prop="status">
          <el-radio-group v-model="data.form.status">
            <el-radio-button label="审核通过" value="审核通过"/>
            <el-radio-button label="审核不通过" value="审核拒绝"/>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="WHY不通过" prop="reason" v-if="data.form.status === '审核拒绝'">
          <el-input v-model="data.form.reason" autocomplete="off" placeholder="请输入不通过原因"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer" style="padding: 0 0 0 0 ">
          <el-button @click="data.formVisible = false">取消</el-button>
          <el-button type="primary" @click="save">确认</el-button>
        </div>
      </template>
    </el-dialog>
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
    <el-dialog
        v-model="data.viewVisible"
        title="攻略信息"
        width="60%"
        destroy-on-close
        top="20px"
    >
      <div ref="dialogContent" v-html="data.content" style="padding: 0 20px"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import { onBeforeUnmount } from 'vue';
import {Search} from "@element-plus/icons-vue";
import {reactive, ref, shallowRef} from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import '@wangeditor/editor/dist/css/style.css'
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
  formVisible2: false,
  rules: {
    title: [
      {required: true, message: '请填写请假标题', trigger: 'blur'}
    ],
    content: [
      {required: true, message: '请填写请假内容', trigger: 'blur'}
    ]
  },
  pageTotal: 6,
  content: null,
  viewVisible: false,
  categoryData: []
})

//wangEditor5 初始化开始
const editorRef = shallowRef(); // 编辑器实例，必须用 shallowRef
const mode = 'default';
const editorConfig = { MENU_CONF: {} }

// 图片上传配置
editorConfig.MENU_CONF['uploadImage'] = {
  headers: {
    token: data.user.token,
  },
  server: 'http://43.143.94.185:9999/files/wang/upload', // 服务端图片上传
  fieldName: 'file' // 服务端图片上传接口参数
};

// 组件销毁时，也及时销毁编辑器，否则可能会造成内存泄漏
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});

// 记录 editor 实例，重要！
const handleCreated = (editor) => {
  editorRef.value = editor;
};
//wangEditor5 初始化结束
const load = () => {
  request.get('/introduction/selectAllStatus',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if(res.code === '200'){
      data.tableData = res.data
      data.title = res.data.title
    } else {
      ElMessage.error("系统异常")
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
  request.post("/introduction/add", data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('提交成功， 等待管理员审核')
      load()
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const contentView = (content) => {
  data.content = content
  data.viewVisible = true
}
const update = () => {
  request.put("/introduction/update", data.form).then(res => {
    if (res.code === '200') {
      data.formVisible = false;
      data.formVisible2 = false
      ElMessage.success('信息修改成功')
      load();
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const save = () => {
      data.form.id ? update(): add()
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
<style scoped>

</style>