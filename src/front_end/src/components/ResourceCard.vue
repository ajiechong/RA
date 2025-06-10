<!-- ResourceCard.vue -->
<template>
  <a :href="url" target="_blank" class="resource-card">
    <div class="content">
      <h3>{{ title }}</h3>
      <div class="meta">
        <span class="time">{{ time }}</span>
        <span class="author">{{ author }}</span>
      </div>
    </div>
  </a>
</template>


<script>
export default {
  name: "ResourceCard",
  props: {
    title: {
      type: String,
      required: true
    },
    time: {
      type: String,
      required: true
    },
    author: {
      type: String,
      required: false
    },
    url: {
      type: String,
      required: true
    }
  }
}
</script>

<style scoped>
.resource-card {
  background-color: rgb(228, 226, 226);
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding-top: 0.6rem;
  padding-left: 1rem;
  padding-right: 0.6rem;
  padding-bottom: 0.8rem;
  margin-bottom: 1rem;
  border-radius: 8px;
  width: 110%;
  text-align: center;
  backdrop-filter: blur(6px);
  transition: transform 0.3s ease;
  height: 95px;
  display: flex; /* 使用Flexbox布局 */
  justify-content: left; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  letter-spacing: 1px;
  text-decoration: none;   /* 去掉下划线 */
  color: inherit;          /* 继承文字颜色 */
}

.resource-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}


.content {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.content h3 {
  margin: 0.7rem 0 0.7rem;  /* 增加标题和meta信息之间的距离 */
  font-size: 1.2rem;
  font-weight: 500;
  font-family: 'Microsoft YaHei', sans-serif;
  color: #000000;
  //color: rgb(0,100,0);
  line-height: 1.4;
  margin-left: 10px;
}

.meta {
  display: flex;
  gap: 1rem;
  font-size: 0.875rem;
  color: #efeeee;
  margin-top: auto;
}


.time {
  //margin: 0;
  color: #7f7e7e;
  font-size: 0.9rem;
  line-height: 1.5;
  margin-left: 10px;
  margin-bottom: 5px;
}

.author {
  margin: 0;
  color: #888;
  font-size: 0.95rem;
  line-height: 1.5;
}
</style>
