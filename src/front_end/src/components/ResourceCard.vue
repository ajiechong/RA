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
      required: true
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
  background: white;
  overflow: hidden;
  box-shadow: 2px 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  height: 90px;
  display: flex;
  flex-direction: column;
  padding:0.2rem 0.2rem;   /* 调整上下左右的内边距 */
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
  font-family: 'Microsoft YaHei', sans-serif;
  color: #333;
  //color: rgb(0,100,0);
  line-height: 1.4;
  margin-left: 10px;
}

.meta {
  display: flex;
  gap: 1rem;
  font-size: 0.875rem;
  color: #666;
  margin-top: auto;
}


.time {
  //margin: 0;
  color: #666;
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