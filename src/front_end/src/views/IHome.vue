<!-- IHome.vue -->
<template>
  <div class="background">
    <div class="home-page">
      <!--      <LoadingSpinner v-if="isLoading"></LoadingSpinner> &lt;!&ndash; 加载时显示加载动画,这里这个控件还没有得到 &ndash;&gt;-->
      <ArticleBox
          v-for="article in articles"
          :key="article.id"
          :title="article.title"
          :description="article.description"
          :articleId="article.id"
          :image="article.image"
      />
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div> <!-- 显示错误信息 -->
    </div>
  </div>

</template>

<script>
import ArticleBox from "@/components/ArticleBox.vue";
import axios from "axios";
import { API_BASE_URL } from '@/utils/api';

export default {
  name: "IHome",
  components: {
    ArticleBox
  },
  data() {
    return {
      articles: [],
      isLoading:true,
      errorMessage:''
    };
  },
  mounted() {

    try {
      this.loadArticles();
    } catch (e) {
      console.error('加载文章数据时出错：', e);
      this.isLoading = false;
      this.errorMessage = '加载文章数据出错，请稍后重试。';
    }
  },
  methods:{
    loadArticles(){
      axios.get(`${API_BASE_URL}/api/introductions/articles`,{
        headers: {
          'Accept': 'application/json' // 只发送必要的请求头
        }
      })
          .then(response => {
            console.log(response.config.headers); // 打印请求头
            // 获取文章数据后，检查每篇文章的图片字段
            this.articles = response.data.map(article => {
              if (article.image) {
                // 使用 require 动态加载图片
                article.image = require('@/assets/imgs/' + article.image);
              }
              return article;
            });
            this.isLoading = false; // 数据获取成功后，将加载状态设为false
          })
          .catch(error => {
            console.error('获取文章数据出错：', error);
            this.isLoading = false; // 出错时也将加载状态设为false
            this.errorMessage = '无法获取文章数据，请稍后重试。'; // 设置错误信息
          });
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

.home-page {
  padding: 50px;
  padding-left: 200px;
  padding-right: 200px;
  margin-top: 90px
}
</style>
  