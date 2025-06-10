<template>
  <div class="content-section">
    <div class="recommendation-list">
      <router-link
          class="paper-card"
          v-for="paper in recommendedPapers"
          :key="paper.paperId"
          :to="{ name: 'PaperDetail', params: { id: paper.paperId } }"
      >
        <h3>{{ paper.title }}</h3>
        <p class="meta">{{ paper.authors }} <br> {{ paper.submissionDate }}</p>
      </router-link>
    </div>

    <!-- 分页控制 -->
    <div class="pagination" v-if="totalPages > 1">
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">上一页</button>
      <span>第 {{ currentPage }} 页 / 共 {{ totalPages }} 页</span>
      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">下一页</button>
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
      totalPages: 1,
      pageSize: 10,
    };
  },

  watch: {
    '$route.query.page': {
      immediate: true,
      handler(newPage) {
        const page = parseInt(newPage) || 1;
        this.currentPage = page;
        this.fetchPapers();
      }
    }
  },

  methods: {
    changePage(newPage) {
      if (newPage >= 1 && newPage <= this.totalPages) {
        this.$router.push({
          name: 'recommend',
          query: { page: newPage }
        });
      }
    },

    fetchPapers() {
      axios.get('http://localhost:8082/papers', {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize
        }
      })
          .then(response => {
            const data = response.data;
            this.recommendedPapers = data.records || [];
            this.totalPages = data.pages || 1;
          })
          .catch(error => {
            console.error('获取推荐论文失败:', error);
            this.recommendedPapers = [];
          });
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
  text-decoration: none;
  color: inherit;
}

.paper-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
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
