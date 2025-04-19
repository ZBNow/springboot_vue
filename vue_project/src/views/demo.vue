<template>
  <div class="body">
    <div class="stars" ref="starsRef">
      <div class="star" v-for="(item, index) in starsCount" :key="index" :style="{ background: getRandomStarColor() }"></div>
    </div>
    <!-- 文字和按钮 -->
    <div class="content">
      <div class="text">星辰大海 不是终点 而是心之所向</div>
      <button class="enter-btn" @click="navigateTo">Enter</button>
    </div>
  </div>
</template>

<script>
import { onMounted, ref } from "vue";

export default {
  setup() {
    let starsRef = ref(null);

    const starsCount = 800; // 星星数量
    const distance = 900; // 间距

    // 随机生成星星颜色（星空冷色调）
    const getRandomStarColor = () => {
      const colors = ["#ffffff", "#c9e4ff", "#e6e6fa", "#b0c4de"]; // 白色、浅蓝、淡紫、浅灰蓝
      return colors[Math.floor(Math.random() * colors.length)];
    };

    onMounted(() => {
      let starNodes = Array.from(starsRef.value.children);
      starNodes.forEach((item) => {
        let speed = 0.2 + Math.random() * 1;
        let thisDistance = distance + Math.random() * 300;
        item.style.transformOrigin = `0 0 ${thisDistance}px`;
        item.style.transform = `
        translate3d(0,0,-${thisDistance}px)
        rotateY(${Math.random() * 360}deg)
        rotateX(${Math.random() * -50}deg)
        scale(${speed},${speed})`;
      });
    });

    // 跳转函数
    const navigateTo = () => {
      window.location.href = "/front/home"; // 跳转到指定页面
    };

    return {
      starsRef,
      starsCount,
      navigateTo,
      getRandomStarColor,
    };
  },
};
</script>

<style lang="css" scoped>
.body {
  position: absolute;
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  background: radial-gradient(
      200% 100% at bottom center,
      #0d1b2a, /* 深蓝 */
      #2b1e66, /* 紫色 */
      #1b263b /* 深蓝灰 */
  );
  background: radial-gradient(
      200% 105% at top center,
      #1b263b 10%, /* 深蓝灰 */
      #2b1e66 40%, /* 紫色 */
      #415a77 65%, /* 深蓝 */
      #778da9 /* 浅蓝灰 */
  );
  background-attachment: fixed;
  overflow: hidden;
}

@keyframes rotate {
  0% {
    transform: perspective(400px) rotateZ(20deg) rotateX(-40deg) rotateY(0);
  }
  100% {
    transform: perspective(400px) rotateZ(20deg) rotateX(-40deg)
    rotateY(-360deg);
  }
}

.stars {
  transform: perspective(500px);
  transform-style: preserve-3d;
  position: absolute;
  perspective-origin: 50% 100%;
  left: 45%;
  animation: rotate 90s infinite linear;
  bottom: 0;
}

/* 星星样式 */
.star {
  width: 2px;
  height: 2px;
  position: absolute;
  left: 0;
  top: 0;
  backface-visibility: hidden;
}

/* 文字和按钮容器 */
.content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  align-items: center;
  gap: 30px;
  z-index: 2;
}

/* 文字样式 */
.text {
  font-size: 36px;
  color: #ffffff;
  font-family: "FangZhengZhunYuan", "PingFang SC", "Microsoft YaHei", sans-serif;
  text-shadow: 0 0 10px #415a77, 0 0 20px #778da9; /* 星空蓝色调阴影 */
}

/* 按钮样式 */
.enter-btn {
  padding: 12px 24px;
  font-size: 18px;
  background: linear-gradient(45deg, #1b263b, #778da9); /* 星空深蓝到浅蓝渐变 */
  border: none;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
  font-family: "FangZhengZhunYuan", "PingFang SC", "Microsoft YaHei", sans-serif;
  /* 文字渐变色 */
  color: transparent;
  background-image: linear-gradient(45deg, #ffffff, #c9e4ff, #e6e6fa); /* 白色 -> 浅蓝 -> 淡紫 */
  -webkit-background-clip: text;
  background-clip: text;
}

.enter-btn:hover {
  background: linear-gradient(45deg, #778da9, #1b263b); /* 悬停时反转渐变方向 */
  transform: scale(1.05);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.4);
  background-image: linear-gradient(45deg, #e6e6fa, #c9e4ff, #ffffff); /* 反转文字渐变 */
  -webkit-background-clip: text;
  background-clip: text;
}
</style>