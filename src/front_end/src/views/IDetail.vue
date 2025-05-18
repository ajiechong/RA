<template>
  <div class="article-detail">
    <!-- 第一行：左侧图片，右侧拆开三行：症状名称、症状描述、原因分析 -->
    <div class="article-content">
      <!-- 左侧图片 -->
      <div class="article-image-container">
        <img :src="article.picture" alt="Article Image" class="article-image" />
      </div>

      <!-- 右侧：症状名称、症状描述、原因分析 -->
      <div class="article-info">
        <div class="symptom-title">
          <h1>{{ article.type }}</h1>
        </div>
        <div class="card card1">
          <h3>症状描述</h3>
          <p>{{ article.symptom }}</p>
        </div>
        <div class="card card2">
          <h3>原因分析</h3>
          <p>{{ article.causes }}</p>
        </div>
      </div>
    </div>

    <!-- 第二行：治疗方法 -->
    <div class="treatment-method">
      <div class="card">
        <h3>治疗方法</h3>
        <p>{{ article.treatment }}</p>
      </div>
    </div>

    <!-- 相关文章推荐 -->
    <h2 class="related-title">相关推荐</h2>
    <div class="related-articles">
      <RelatedArticleBox
          v-for="related in relatedArticles"
          :key="related.id"
          :rimg="related.rimg"
          :rname="related.rname"
          :rurl="related.rurl"
      />
    </div>
  </div>
</template>

<script>
import RelatedArticleBox from '@/components/RelatedArticleBox.vue';
import axios from "axios";
import { API_BASE_URL } from '@/utils/api';

export default {
  name: "ArticleDetail",
  components: {
    RelatedArticleBox
  },
  data() {
    return {
      article: {},
      relatedArticles: []
    };
  },
  created() {
    const articleId = this.$route.params.id;
    this.fetchArticle(articleId);
  },
  methods: {
    async fetchArticle(id) {
      try {
        const response = await axios.get(`${API_BASE_URL}/api/introductions/${id}`);
        const articleData = response.data;

        if (articleData.picture) {
          articleData.picture = require('@/assets/imgs/' + articleData.picture);
        }
        this.article = articleData;

        this.fetchRelatedArticles(this.article.type);
      } catch (error) {
        console.error("Error fetching article:", error);
      }
    },
    async fetchRelatedArticles(symptomName) {
      try {
        const resourcesResponse = await axios.get(`${API_BASE_URL}/api/resources/by-tag`, {
          params: { name: symptomName }
        });
        const videoResponse = await axios.get(`${API_BASE_URL}/api/resourceVideos/byTag`, {
          params: { name: symptomName }
        });

        const combinedArticles = [
          ...resourcesResponse.data,
          ...videoResponse.data
        ];

        this.relatedArticles = combinedArticles;
      } catch (error) {
        console.error("Error fetching related articles:", error);
      }
    }
  }
};
</script>

<style scoped>
.article-detail {
  padding: 20px 200px; /* 上下20px，左右200px */
  margin-top: 130px;
}

/* 第一行：左侧图片，右侧拆开三行：症状名称、症状描述、原因分析 */
.article-content {
  display: flex;
  gap: 20px; /* 图片和文字之间的间距 */
  align-items: center;  /* 图片和右侧卡片底部对齐 */
  margin-bottom: 20px;
}

/* 图片容器 */
.article-image-container {
  width: 500px; /* 固定宽度 */
  height: 400px; /* 固定高度 */
  display: flex;
  justify-content: center; /* 图片水平居中 */
  align-items: center; /* 图片垂直居中 */
  overflow: hidden;
  margin-right: 20px; /* 确保右侧的内容与图片分开 */
}

.article-image {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 图片等比缩放 */
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

/* 右侧的资料部分 */
.article-info {
  display: flex;
  flex-direction: column;
  flex: 1;
  gap: 20px;
}

@import url('https://fonts.googleapis.com/css2?family=Merriweather:wght@700&display=swap');
/* 症状名称样式 */
.symptom-title h1 {
  font-size: 2rem; /* 增加标题字号 */
  font-family: 'Merriweather', serif;
  font-weight: 580;
  color: #096892;
  text-align: left;
  margin-left: 10px;
  letter-spacing: 1px;
  text-transform: capitalize; /* 首字母大写 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1);
  /* 动画效果：让标题更生动 */
  animation: titleAnimation 1.5s ease-in-out;
}

/* 为标题添加动画效果 */
@keyframes titleAnimation {
  0% {
    opacity: 0;
    transform: translateY(-20px);
  }
  50% {
    opacity: 0.7;
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 卡片样式 */
.card {
  border: 1px solid #ddd;
  border-radius: 12px; /* 增加圆角 */
  padding: 20px; /* 增加内边距 */
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1); /* 增加阴影效果 */
  transition: transform 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease;
}

.card1 {
  background-color: #f0f9fa;
}

.card2 {
  background-color: #faf9f0;
}

.card h3 {
  font-size: 1.3rem;
  font-family: 'Merriweather', serif;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.card p {
  font-size: 1rem;
  color: #555;
  line-height: 1.6;
}

/* 第二行：治疗方法 */
.treatment-method {
  margin-top: 20px;
}

.treatment-method .card {
  background-color: #f0faf2;
}

/* 相关文章推荐标题 */
.related-title {
  margin-top: 50px;
  margin-bottom: 20px;
  margin-left: 10px;
  font-size: 1.6rem;
  color: #333;
}

.related-articles {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  gap: 20px;
}

.related-articles > * {
  flex: 1 1 calc(25% - 20px);
  max-width: 300px;
  box-sizing: border-box;
}
</style>
