<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 240px" v-model="data.title" placeholder="请输入title查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </div>
<!--    表格数据-->
    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" style="width: 100%" :header-cell-style="{color: '#333',fontSize: '17px', backgroundColor: '#eaf4ff'}">
        <el-table-column label="图片" width="100px">
          <template #default="scope">
            <el-image v-if="scope.row.img" :src="scope.row.img" :preview-src-list="[scope.row.img]" :preview-teleported="true"
                      style="width: 60px; height: 50px;border-radius: 5px; display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="攻略标题"/>
        <el-table-column prop="categoryTitle" label="攻略分类"/>
        <el-table-column prop="userName" label="发布作者"/>
        <el-table-column prop="content" label="攻略内容">
          <template v-slot="scope">
            <el-button @click="contentView(scope.row.content)">点击查看</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="time" label="发布时间"/>
        <el-table-column label="操作" width="100">
          <template #default="scoped">
            <el-button circle icon="Edit" type="primary" @click="handleEdit(scoped.row)"></el-button>
            <el-button circle icon="Delete" type="danger" @click="handleDel(scoped.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
<!--   弹窗-->

      <el-dialog v-model="data.formVisible" title="攻略信息" width="60%" destroy-on-close>
        <el-form label-width="80px" style="padding: 20px 30px 0 0;" :model="data.form">
          <el-form-item prop="avatar" label="攻略图片">
            <el-upload
                action="http://localhost:9999/files/upload"
                :headers="{ token: data.user.token }"
                :on-success="handleFileSuccess"
                list-type="picture">
              <el-button type="primary">上传头像</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="攻略标题" prop="title" >
            <el-input placeholder="请输入攻略标题" v-model="data.form.title" autocomplete="off" />
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


    <el-dialog v-model="data.viewVisible" title="攻略信息" width="60%" destroy-on-close >
      <div ref="dialogContent" v-html="data.content" style="padding: 0 20px"></div>
    </el-dialog>
  </div>
</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import { reactive, ref, onBeforeUnmount, shallowRef,nextTick } from "vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
const formRef = ref()
const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  form: {},
  formVisible: false,
  pageNum: 1,
  pageSize: 5,
  pageTotal: 6,
  tableData: [],
  title: null,
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
  server: 'http://localhost:9999/files/wang/upload', // 服务端图片上传
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
  request.get('/introduction/selectPage',{
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title
    }
  }).then(res => {
    if(res.code === '200'){
      data.tableData = res.data.list
      data.title = res.data.title
    } else {
      ElMessage.error("系统异常")
    }
  })
}
load()
const reset = () => {
  data.form = null
}

const handleAdd = () => {
  data.formVisible = true;
  data.form = {};
}

const add = () => {
  request.post('/introduction/add', data.form).then(res => {
    if(res.code === '200'){
      ElMessage.success('新增数据成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const update = () => {
  request.put('introduction/update', data.form).then(res => {
    if(res.code === '200'){
      ElMessage.success('修改成功')
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}
const save = () => {

      data.form.id ? update() : add()
      data.formVisible = false

}


const handleFileSuccess = (res) =>{
  data.form.img = res.data
}
const handleDel = (id) => {
  ElMessageBox.confirm("您确认要删除数据吗，删除后无法恢复", "删除确认" , {type : 'warning'}).then(res =>{
    request.delete('/introduction/delete/' + id).then(res => {
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
const contentView = (content) => {
  data.content = content
  data.viewVisible = true
}
const loadCategory = () => {
  request.get('/category/selectAll').then(res => {
    if(res.code === '200'){
      data.categoryData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadCategory()


</script>