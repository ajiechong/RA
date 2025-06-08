<template>
  <div class="content-section">


    <h2 class="intro-title">Explore and discover research papers effortlessly.</h2>

    <div class="features">
      <div class="feature-card">
        <h3>文献搜索</h3>
        <p>根据关键词搜索文献，提供快速高效的检索。</p>
      </div>
      <div class="feature-card">
        <h3>文献推荐</h3>
        <p>根据您的兴趣推送多样化的科研文献。</p>
      </div>
      <div class="feature-card">
        <h3>AI 对话</h3>
        <p>提供学术润色、文献综述、实验设计等帮助。</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomePage',

};
</script>

<style scoped>

.content-section {
  padding: 2rem;
  text-align: center;
}

.intro-title {
  font-size: 2.5rem;
  font-weight: 800;
  text-align: center;
  margin-top: 2rem;
  background: linear-gradient(90deg, #bbb, #eee);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.2);
  letter-spacing: 2px;
  animation: fadeSlideIn 1s ease-out;
  font-family: "Helvetica Neue", "Segoe UI", "Roboto", Arial, sans-serif;
  font-weight: 300;
  letter-spacing: 0.5px;
}

@keyframes fadeSlideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.features {
  display: flex;
  justify-content: center;
  gap: 2.5rem;
  margin-top: 2rem;
  flex-wrap: wrap;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  margin-top: 2rem;
  padding: 1.5rem;
  border-radius: 10px;
  width: 250px;
  height: 150px;
  text-align: left;
  color: white;
  backdrop-filter: blur(6px);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  background: rgba(255, 255, 255, 0.15);
}

.feature-card h3 {
  font-size: 1.5rem;
  font-weight: 300;
  padding-bottom: 0.5rem;
  letter-spacing: 1px;
}

.feature-card p {
  font-size: 1rem;
  font-weight: 300;
  letter-spacing: 1px;
  color: #e2e0e0;
}
</style>
