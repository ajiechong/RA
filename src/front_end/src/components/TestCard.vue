<!-- TestCard.vue -->
<template>
  <a :href="testUrl" target="_blank" class="test-card-link">
    <div class="test-card">
      <img :src="image" :alt="title">
      <h3>{{ title }}</h3>
    </div>
  </a>
</template>

<script>
export default {
  name: "TestCard",
  props: ["image", "title", "testUrl"]
}
</script>

<style scoped>
.test-card-link {
  text-decoration: none;
  color: inherit;
}

.test-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  transition: transform 0.3s ease;
}

.test-card:hover {
  transform: translateY(-5px);
}

.test-card img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}

.test-card h3 {
  padding: 1rem;
  margin: 0;
  font-size: 1.1rem;
  color: #333;
  //color: rgb(0,100,0);
  text-align: center;
}
</style>