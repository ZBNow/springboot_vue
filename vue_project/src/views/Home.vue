<template>
  <div class="card">欢迎来到大郑王朝</div>
  <div class="card" style="margin-top: 20px; width:50%;">
    <div style="margin-bottom: 20px; font-size: 20px">系统公告</div>
    <el-timeline>
      <el-timeline-item :timestamp="item.time" placement="top" color="#0bbd87" v-for="item in data.noticeTable">
        <h3>{{ item.title}}</h3>
        <p>{{ item.content}}</p>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  noticeTable: [],

})

const load = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeTable = res.data;
      if (data.noticeTable.length > 3){
        data.noticeTable = data.noticeTable.slice(0, 3)
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>