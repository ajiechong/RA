
<template>
  <div class="background">
    <div class="test-page">
      <ArticleBoxTest
          v-for="(article) in articles"
          :key="article.id"
          :title="article.title"
          :description="article.description"
          :articleId="article.id"
          :image="getImagePath(article.image)"
          :jumpUrl="article.url"
      />
    </div>
  </div>

</template>

<script>
import ArticleBoxTest from "@/components/ArticleBoxTest.vue";
import axios from 'axios';
import { API_BASE_URL } from '@/utils/api';

export default {
  name: "TestPage",
  components: {
    ArticleBoxTest
  },
  data() {
    return {
      articles: [],
      isLoading: true,
      errorMessage: ''
    };
  },
  created() {
    this.fetchArticles();
  },
  methods: {
    async fetchArticles() {
      try {
        const response = await axios.get(`${API_BASE_URL}/api/test/data`);
        this.articles = response.data;
        this.isLoading = false;
      } catch (error) {
        console.error("There was an error fetching the articles!", error);
        this.errorMessage = '无法加载文章，请稍后重试。';
        this.isLoading = false;
      }
    },
    getImagePath(imgPath) {
      // 使用 require 动态加载图片
      try {
        return require(`@/${imgPath}`);  // 假设 imgPath 是相对于 src 目录的路径
      } catch (error) {
        console.warn(`未能找到图片: ${imgPath}`, error);
        return '';  // 如果图片不存在，返回空字符串或默认图片路径
      }
    }
  }
};
</script>

<style scoped>
.background{
  background-image: url('@/assets/imgs/background4.jpg');
  background-size: cover;
  background-position: center;
  padding-top: 15px;
}


.test-page {
  padding: 50px;
  padding-left: 200px;
  padding-right: 200px;
  margin-top: 90px;
}
</style>