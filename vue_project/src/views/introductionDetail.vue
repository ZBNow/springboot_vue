<template>
  <div style="width: 70%; margin: 50px auto;">
    <!-- 博主帖子内容-->
    <div style="text-align: center; font-size: 25px; font-weight: bold">{{ data.introductionData.title }}</div>
    <div style="margin-top: 15px; display: flex; align-items: center; justify-content: center;">
      <img  :src="data.introductionData.userAvatar" alt="用户头像" style="width: 30px; height: 30px; border-radius: 50%;"/>
      <div style="font-size: 15px; margin-left: 5px;">{{ data.introductionData.userName }}</div>
      <div style="font-size: 15px; margin-left: 10px;">所属分类: {{ data.introductionData.categoryTitle }}</div>
      <div style="font-size: 15px; margin-left: 10px;">发布时间: {{ data.introductionData.time }}</div>
    </div>
    <div v-html="data.introductionData.content" style="margin-top: 50px; padding: 0 50px"></div>
    <!--发帖区域-->
    <div class="comment" >
      <div v-if="!data.user.id" class="elIcon">
        <el-icon><UserFilled/></el-icon>
        <div style="width: 200px;background-color: #4c87c6;height: 40px; font-size: 18px; font-weight: bold; font-family: 'Bell MT'; text-align: center; cursor: pointer;display: flex;justify-content: center;align-items: center;color: white;border-radius: 15px;" @click="router.push('/login')">
          请登录后评论
        </div>
      </div>
      <div v-else style="display: flex;align-items: center;gap: 10px;">
        <img v-if="data.user?.avatar" :src="data.user?.avatar" style="width: 40px;height: 40px; border-radius: 50%;" />
        <div style="width:300px; display: flex; align-items: center;">
          <input v-model="data.content"placeholder="  说点什么..." style="width:350px; height: 35px;border-radius: 8px">
          <button @click="commit" style="width: 70px; background-color: #599ee8; height: 35px; border-radius: 10px;cursor: pointer; font-size: 15px; margin-left: 5px;" >提交</button>
        </div>
      </div>
    </div>
    <!--评论区-->
    <div class="divider"></div>

    <div class="comment-header">
      <span class="comment-count">评论 <span style="font-size: 15px; font-weight: normal;">{{ data.comments.length }}</span></span>
    </div>
    <!-- 评论列表 -->
    <div class="comment-list">
      <div v-for="comment in data.comments" :key="comment.id" class="comment-item">
        <div style="display: flex">
          <img :src="comment.avatar" alt="头像" class="avatar"/>
          <div class="comment-content">
            <div class="comment-meta">
              <span class="username">{{ comment.name }}</span>
            </div>
            <div class="comment-text">{{ comment.content }}</div>
            <div class="comment-actions">
              <span class="time">{{ comment.time }}</span>
              <span class="action">👍 {{ comment.likes }}</span>
              <span class="action">回复</span>
            </div>
          </div>
        </div>
        <div class="divide"></div>

      </div>
    </div>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('code_user') || '{}'),
  introductionId: router.currentRoute.value.query.id,
  introductionData: {},
  content: null,
  comments:[],

})

const loadIntroduction = () => {
  request.get('/introduction/selectByid/' + data.introductionId).then(res => {
    if (res.code === '200') {
      data.introductionData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
  request.get('introduction/selectComment/' + data.introductionId).then(res => {
    if(res.code === '200'){
      data.comments = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadIntroduction()
const commit = () => {
  const payload = {
    content: data.content,// 如果后端 Commit 实体里有其他字段，比如 userId、targetId，也要一起传
    introductionId: data.introductionId
  }
  request.post('/introduction/commit',payload).then(res => {
    if(res.code === '200') {
      data.content = null
      ElMessage.success('评论成功')
      loadIntroduction()
    }
  })
}
loadIntroduction()
</script>
<style scoped>
.comment{
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  position: fixed;
  bottom: 0;
  width: 20%;
  padding: 10px 20px;
  z-index: 999;
  margin-bottom: 5px;
  left: 50%; /* 从页面左边开始 50% */
  transform: translateX(-50%); /* 向左移动自身宽度的 50%，实现居中 */
}
.elIcon {
  display: flex;
  align-items: center;
  gap: 10px;
}

.elIcon .el-icon {
  font-size: 30px;
  color: #ffffff;
  background-color: #1e1a1a;
  border-radius: 50%;
  padding: 5px;
  margin-left: 70px;
}

::v-deep(.el-input__inner) {
  border-radius: 15px;
}

.comment-section {
  padding: 10px;
  background-color: #f9f9f9;
  border-radius: 8px;
}

/* 标题和排序选项 */
.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 5px;
}

.comment-count {
  font-size: 25px;
  font-weight: bold;
  color: #333;
}

.sort-options {
  font-size: 14px;
  color: #666;
}

/* 分隔线 */
.divider {
  width: 100%;
  height: 2px;
  background-color: #e0e0e0;
  margin: 5px 0;
}
.divide {
  width: 95%;
  height: 1px;
  background-color: #e0e0e0;
  margin: 5px 0;
  margin-bottom: 30px;
  margin-left: 5%;
}

/* 评论列表 */
.comment-list {
  margin-top: 10px;
}

.comment-item {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.avatar {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  margin-right: 10px;
}

.comment-content {
  flex: 1;
}

.comment-meta {
  display: flex;
  align-items: center;
  margin-top:2px;
}

.username {
  font-size: 14px;
  font-weight: bold;
  color: #333;
}

.badge {
  font-size: 12px;
  color: #666;
  margin-left: 5px;
}

.comment-text {
  margin-top:5.5px;
  margin-bottom: 2px;
  font-size: 17px;
  color: #333;
  width: 30em; /* 每行最多显示大约30个汉字 */
  word-break: break-all; /* 强制断词换行 */
  white-space: normal; /* 正常换行 */
  line-height: 1.5; /* 行高可调 */


}

.comment-actions {
  display: flex;
  gap: 10px;
  font-size: 12px;
  color: #999;
}

.time {
  color: #999;
}

.action {
  cursor: pointer;
}
</style>