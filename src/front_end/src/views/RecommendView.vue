<template>
  <div class="content-section">
    <!-- 论文列表 -->
    <div class="recommendation-list">
      <router-link
          class="paper-card"
          v-for="paper in recommendedPapers"
          :key="paper.paperId"
          :to="{ name: 'PaperDetail', params: { id: paper.paperId } }"
      >
        <h3>{{ paper.title }}</h3>
        <p class="meta">{{ paper.authors }} <br />{{ paper.submissionDate }}</p>
      </router-link>
    </div>

    <!-- 分页控制（只根据 hasNext / hasPrev） -->
    <div class="pagination" v-if="recommendedPapers.length">
      <button @click="changePage('prev')" :disabled="currentPage === 1">上一页</button>
      <span>第 {{ currentPage }} 页</span>
      <button @click="changePage('next')" :disabled="!hasNext">下一页</button>
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
      hasNext: false,   // ⬅ 仅靠这个判断“下一页”是否可点
    };
  },

  watch: {
    '$route.query.page': {
      immediate: true,
      handler(newPage) {
        const page = parseInt(newPage) || 1;
        this.currentPage = page;
        this.fetchPapers();
      },
    },
  },

  methods: {
    changePage(direction) {
      const newPage =
          direction === 'prev' ? this.currentPage - 1 : this.currentPage + 1;
      if (newPage >= 1 && (direction === 'prev' || this.hasNext)) {
        this.$router.push({
          name: 'recommend',
          query: { page: newPage },
        });
      }
    },

    fetchPapers() {
      axios
          .get('http://localhost:8082/papers/fast', {
            // 你的“跳过 COUNT(*)”接口路径
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
            },
            timeout: 30000,
          })
          .then((response) => {
            const data = response.data;
            this.recommendedPapers = data.records || [];
            this.hasNext = !!data.hasNext; // 布尔化
          })
          .catch((error) => {
            console.error('获取推荐论文失败:', error);
            this.recommendedPapers = [];
            this.hasNext = false;
          });
    },
  },
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
  transition: transform 0.3s ease, box-shadow 0.3s ease;
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
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
}

.pagination span {
  margin-bottom: 1rem;
}

.pagination button {
  padding: 0.5rem 1rem;
  margin-bottom: 1rem;
  background-color: #1a365d;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
}

.pagination button:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}
</style>
