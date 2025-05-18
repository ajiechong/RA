<!--src/components/WelcomeAnimation.vue-->

<template>
  <div class="welcome-container">
    <!-- 文字动画部分，分成两行显示 -->
    <div class="text-container">
      <h1 class="title-line first-line">{{ firstLine }}</h1>
      <h1 class="title-line second-line">{{ secondLine }}</h1>
    </div>

    <!-- 几何图形动画保持不变 -->
    <div class="geometric-container">
      <div class="geometric-animation">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
        <div class="center-dot"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const firstLine = ref('')
const secondLine = ref('')
const firstText = "Hello～今天开心吗？"
const secondText = "我们一起来聊聊天吧^o^"

onMounted(() => {
  // 第一行动画
  let index1 = 0
  const interval1 = setInterval(() => {
    if (index1 <= firstText.length) {
      firstLine.value = firstText.slice(0, index1)
      index1++
    } else {
      clearInterval(interval1)
      // 第一行完成后开始第二行
      let index2 = 0
      const interval2 = setInterval(() => {
        if (index2 <= secondText.length) {
          secondLine.value = secondText.slice(0, index2)
          index2++
        } else {
          clearInterval(interval2)
        }
      }, 100)
    }
  }, 100)
})
</script>

<style scoped>
.welcome-container {
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 100%;
  gap: 3rem;
}

.text-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.title-line {
  font-size: 2.4rem;
  margin: 0;
  opacity: 0;
  animation: slideIn 0.5s ease-out forwards;
}

.first-line {
  font-family: -apple-system, "SF Pro SC", "SF Pro Display", "Helvetica Neue", Arial, sans-serif;
  font-weight: 600;
  color: #1a365d;
  animation-delay: 0.2s;
}

.second-line {
  font-family: -apple-system, "SF Pro SC", "SF Pro Display", "Helvetica Neue", Arial, sans-serif;
  font-weight: 500;
  color: #2c5282;
  animation-delay: 0.4s;
}

.geometric-container {
  width: 200px;
  height: 200px;
  position: relative;
  animation: floatAnimation 4s ease-in-out infinite;
}

.geometric-animation {
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.circle {
  position: absolute;
  border-radius: 50%;
  border: 4px solid transparent;
  border-top-color: #60a5fa;
  border-right-color: #60a5fa;
}

.circle-1 {
  width: 100%;
  height: 100%;
  animation: rotate 3s linear infinite;
}

.circle-2 {
  width: 80%;
  height: 80%;
  border-top-color: #93c5fd;
  border-right-color: #93c5fd;
  animation: rotate 2.5s linear infinite reverse;
}

.circle-3 {
  width: 60%;
  height: 60%;
  border-top-color: #bfdbfe;
  border-right-color: #bfdbfe;
  animation: rotate 2s linear infinite;
}

.center-dot {
  position: absolute;
  width: 20px;
  height: 20px;
  background: #3b82f6;
  border-radius: 50%;
  animation: pulse 2s ease-in-out infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.2); opacity: 0.8; }
}

@keyframes floatAnimation {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-15px); }
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

</style>