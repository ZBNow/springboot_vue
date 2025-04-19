<template>
  <div class="container">
    <!-- 左侧导航栏 -->
    <div class="sidebar">
      <div class="logo">大郑王朝</div>
      <div
          class="nav-item"
          :class="{ active: activeSidebar === '攻略中心' }"
          @click="setActiveSidebar('攻略中心')"
      >
        <el-icon><Reading/></el-icon>
        <span>攻略中心</span>
      </div>
      <div
          class="nav-item"
          :class="{ active: activeSidebar === '发布攻略' }"
          @click="setActiveSidebar('发布攻略'); navTo('/manager/introduction')"
      >
        <el-icon><Plus /></el-icon>
        <span>发布攻略</span>
      </div>
      <div
          class="nav-item"
          :class="{ active: activeSidebar === '消息' }"
          @click="setActiveSidebar('消息')"
      >
        <el-icon><Bell /></el-icon>
        <span>消息</span>
      </div>
      <div v-if="data.user.role !== 'ADMIN' && data.user.role !== 'USER'" class="nav-item2">
        <el-button @click="navTo('/login')" style="background-color: #ff3b30; color: #ffffff; border-radius: 20px; width: 150px; height: 50px; font-size: 20px; font-family: 'Agency FB'">登录</el-button>
      </div>
      <div v-else style="display: flex; align-items: center; margin-left: 20px; cursor: pointer" @click="navTo('/manager/person')">
        <img v-if="data.user?.avatar" :src="data.user?.avatar" style="width: 30px; height: 30px; border-radius: 50%;" />
        <span style="margin-left: 5px; color: #ffffff">{{ data.user.name }}</span>
      </div>
    </div>

    <!-- 主内容区域 -->
    <div class="main-content">
      <!-- 轮播图 -->
      <div class="carousel-wrapper">
        <el-carousel height="400px">
          <el-carousel-item v-for="item in data.carouselData" :key="item">
            <img :src="item" alt="" class="carousel-img" />
          </el-carousel-item>
        </el-carousel>
      </div>

      <!-- 攻略内容（卡片式布局） -->
      <div class="notes-section">
        <div style="display: flex; flex-direction: row">
          <div class="nav-item" :class="{ active: activeNav === '全部' }" @click="setActiveNav('全部')">全部</div>
          <div class="nav-item" v-for="item in data.category" :class="{ active: activeNav === item.categoryTitle }" @click="setActiveNav(item.categoryTitle); selectCategory(item.categoryTitle)">{{ item.categoryTitle }}</div>
        </div>
        <!--真正数据渲染-->
        <div class="notes-list">
          <div class="note-card" v-for="item in data.introductionData" :key="item.id" @click="navTo('/front/introductionDetail?id=' + item.id)">
            <img :src="item.img" class="note-img" />
            <div class="note-content">
              <div class="note-title">{{ item.title }}</div>
              <div class="note-meta">
                <img :src="item.userAvatar" class="user-avatar" />
                <span class="user-name">{{ item.userName }}</span>
                <span class="time">{{ item.time }}</span>
                <div class="like-wrapper">
                  <el-icon><View /></el-icon>
                  <span>1万+</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 更多推荐（瀑布流布局） -->

    </div>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request.js";
import lunbo1 from "@/assets/img/lunbo1.jpg";
import lunbo2 from "@/assets/img/lunbo2.jpg";
import lunbo3 from "@/assets/img/lunbo3.jpg";
import lunbo4 from "@/assets/img/lunbo4.jpg";

// 数据
const data = reactive({
  user: JSON.parse(localStorage.getItem("code_user") || "{}"),
  introductionData: [],
  carouselData: [lunbo1, lunbo2, lunbo3, lunbo4],
  category: []
});

// 左侧导航栏选中状态
const activeSidebar = ref("攻略中心");
const setActiveSidebar = (item) => {
  activeSidebar.value = item;
};

// 顶部导航栏选中状态（已存在）
const activeNav = ref("全部");
const setActiveNav = (nav) => {
  activeNav.value = nav;
};

// 瀑布流分列
const waterfallColumns = computed(() => {
  const columns = [[], [], []]; // 分为3列
  data.introductionData.forEach((item, index) => {
    columns[index % 3].push(item);
  });
  return columns;
});

// 加载攻略数据
const loadIntroduction = () => {
  request.get("/introduction/selectAll").then((res) => {
    if (res.code === "200") {
      data.introductionData = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  });
};
const loadCategory = () => {
  request.get("/introduction/selectCategory").then((res) => {
    if (res.code === "200") {
      data.category = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  });
};
onMounted(() => {
  loadIntroduction();
  loadCategory();
});

// 跳转函数
const navTo = (url) => {
  location.href = url;
};

// 退出登录
const logout = () => {
  localStorage.removeItem("code_user");
  location.href = "/login";
};

const selectCategory = (category) => {
  if (category !== '全部') {
    request.get('/introduction/selectAll', {
      params: {
        category: category
      }
    }).then(res => {
      if (res.code === '200') {
        data.introductionData = res.data
      } else {
        ElMessage.error(res.msg);
      }
    })
  }
}
</script>

<style scoped>
.container {
  display: flex;
  min-height: 100vh;
  background-color: #1a1a1a; /* 深色背景 */
}

/* 左侧导航栏 */
.sidebar {
  width: 230px;
  background-color: #222222;
  display: flex;
  flex-direction: column;
  padding: 20px 0;
  position: fixed;
  height: 100vh;
  z-index: 10;
}

.logo {
  width: 80px;
  font-size: 18px; /* 文字大小 */
  color: #ffffff; /* 文字颜色 */
  background-color: #ff3b30; /* 背景颜色 */
  border-radius: 20px; /* 圆角矩形 */
  padding: 4px 4px; /* 上下4px，左右20px的内边距 */
  margin-bottom: 30px; /* 底部间距 */
  font-weight: bold; /* 字体加粗 */
  text-align: center;
  white-space: nowrap; /* 防止文字换行 */
  margin-left: 15px;
}

.nav-item {
  display: flex;

  padding-left: 20px;
  align-items: center; /* 垂直方向居中 */
  gap: 10px;
  user-select: none;
  color: #ffffff;
  margin-bottom: 30px;
  cursor: pointer;
  opacity: 0.7;
  transition: opacity 0.3s;
  font-size: 19px; /* 统一设置，图标和文字都继承 */
}

.nav-item2 {
  display: flex;
  flex-direction: row;
  padding-left: 20px;
  align-items: center; /* 垂直方向居中 */
  gap: 10px;
  color: #ffffff;
  margin-bottom: 30px;
  cursor: pointer;
  opacity: 1;
  transition: opacity 0.3s;
  font-size: 19px; /* 统一设置，图标和文字都继承 */
  user-select: none;
}

.nav-item .el-icon {
  font-size: 20px; /* 图标大小控制在视觉舒适范围 */
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  top: 1px; /* 微调，解决图标与文字垂直对齐偏差 */
}

.nav-item span {
  font-size: inherit; /* 继承父级 */
  font-family: "Arial", sans-serif;
  font-weight: 500;
  user-select: none;
}

.nav-item:hover,
.nav-item.active {
  opacity: 1;
  user-select: none;
}

.logout-btn {
  margin-top: auto;
  width: 60px;
  font-size: 12px;
  padding: 8px;
}

/* 主内容区域 */
.main-content {
  flex: 1;
  margin-left: 230px;
  padding: 20px;
  overflow-y: auto;
}

/* 轮播图 */
.carousel-wrapper {
  margin-bottom: 20px;
}

.carousel-img {
  width: 100%;
  height: 400px;
  object-fit: cover;
  border-radius: 10px;
}

/* 攻略内容 */
.notes-section {
  margin-bottom: 40px;
}

.section-title {
  font-size: 24px;
  color: #ffffff;
  margin-bottom: 20px;
  font-weight: bold;
}

/* 卡片列表 */
.notes-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.note-card {
  width: calc(33.33% - 14px); /* 每行3个卡片 */
  background: #2a2a2a;
  border-radius: 10px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
}

.note-card:hover {
  transform: translateY(-5px);
}

.note-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.note-content {
  padding: 10px;
}

.note-title {
  font-size: 16px;
  color: #ffffff;
  font-weight: bold;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.note-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #cccccc;
  font-size: 12px;
}

.user-avatar {
  width: 24px;
  height: 24px;
  border-radius: 50%;
}

.user-name {
  flex: 1;
}

.time {
  color: #999999;
}

.like-wrapper {
  display: flex;
  align-items: center;
  gap: 5px;
}


/* 瀑布流 */
.waterfall {
  display: flex;
  gap: 20px;
}

.waterfall-column {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.waterfall .note-card {
  width: 100%;
}

@media (max-width: 768px) {
  .sidebar {
    width: 60px;
  }

  .main-content {
    margin-left: 60px;
  }

  .note-card {
    width: calc(50% - 10px); /* 每行2个卡片 */
  }

  .waterfall {
    flex-direction: column;
  }

  .waterfall-column {
    width: 100%;
  }
}
</style>