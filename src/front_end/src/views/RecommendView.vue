<template>
  <div class="content-section">
    <div class="recommendation-list">
      <div class="paper-card" v-for="(paper) in recommendedPapers" :key="paper.paperId">
        <h3>{{ paper.title }}</h3>
        <p class="meta">{{ paper.authors }} <br> {{ paper.submissionDate }}</p>
      </div>
    </div>
    <div class="pagination">
      <button @click="prevPage" :disabled="currentPage === 1">上一页</button>
      <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
      <button @click="nextPage" :disabled="currentPage === totalPages">下一页</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'RecommendView',
  data() {
    return {
      recommendedPapers: [],
      currentPage: 1,
      pageSize: 10,
      totalPages: 1
    };
  },
  created() {
    this.fetchPapers();
  },
  methods: {
    fetchPapers() {
      console.log('请求页码：', this.currentPage);
      axios.get('http://localhost:8080/papers', {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize
        }
      })
        .then(response => {
          const data = response.data;
          console.log('收到记录：', data.records);
          this.recommendedPapers = data.records || [];
          this.totalPages = data.pages || 1;
        })
        .catch(error => {
          console.error('获取论文数据失败:', error);
          this.recommendedPapers = [];
        });
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchPapers();
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchPapers();
      }
    }
  }
};
</script>

<style scoped>
.content-section {
  text-align: center;
}

.recommendation-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 1.5rem;
  margin-top: 2rem;
}

.paper-card {
  background-color: #e4ecef;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 40%;
  min-width: 300px;
}

.paper-card h3 {
  color: #1a365d;
  margin-bottom: 0.5rem;
  font-size: 1.2rem;
}

.meta {
  color: #666;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.pagination {
  margin-top: 2rem;
}
</style>
