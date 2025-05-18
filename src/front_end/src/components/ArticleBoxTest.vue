<!--测试界面-->
<template>
  <div class="article-box-test" @click="goToNewDetail">
    <img :src="image" alt="Article Image" class="article-image-test" />
    <div>
      <h2 class="article-title-test">{{ title }}</h2>
      <p class="description-test">{{ description }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "ArticleBoxTest",
  props: {
    title: String,
    description: String,
    articleId: Number,
    image: String,
    jumpUrl: String
  },
  methods: {
    goToNewDetail() {
      window.location.href = this.jumpUrl;
    }
  }
};
</script>

<style scoped>
.article-box-test {
  display: flex;
  border: 1px solid #ccc;
  padding: 20px;
  margin-bottom: 20px;
  background-color: #f6fff8;
  border: 0.1px solid #005f00;
  border-radius: 20px;
  box-shadow: 4px 4px 8px rgba(0, 0, 0, 0.2);
  cursor: pointer;
}

.article-image-test {
  margin-top: 10px;
  width: 220px;
  height: auto;
  margin-right: 30px;
  border-radius: 20px;
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.1);
}

.article-content-test {
  flex: 1;
}

.article-title-test {
  color: #005f00;
  margin: 10px 0;
  margin-bottom: 25px;
  position: relative;
  padding-left: 20px;
  font-family: Arial, Helvetica, sans-serif;
}

.article-title-test::before {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background-color: #0a7b0a;
  border-radius: 2px;
}

.description-test {
  font-family: Arial, Helvetica, sans-serif;
  line-height: 1.6;
}
</style>