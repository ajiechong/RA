<template>
  <div class="content-section">
    <div class="search-box">
      <input type="text" v-model="searchQuery" placeholder="请输入关键词："  />
      <button @click="handleSearch()">搜 索</button>
    </div>
    <div class="search-results">
      <p v-if="searchResults.length === 0 && searchPerformed">无相关结果</p>
      <div class="results-grid" v-else>
        <router-link
            class="paper-card"
            v-for="(result, index) in searchResults"
            :key="index"
            :to="{ name: 'PaperDetail', params: { id: result.paperId } }"
            role="link"
            tabindex="0"
        >
          <h3>{{ result.title }}</h3>
          <p class="meta">{{ result.authors }} <br> {{ result.submissionDate }}</p>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'SearchView',
  data() {
    return {
      searchQuery: '',
      searchResults: [],
      searchPerformed: false,
    };
  },

  // 监听路由参数变化
  watch: {
    '$route.query.keyword': {
      immediate: true,
      handler(newKeyword) {
        if (newKeyword) {
          this.searchQuery = newKeyword;
          this.performSearch();  // 自动触发搜索
        } else {
          this.searchResults = [];
          this.searchPerformed = false;
        }
      }
    }
  },

  methods: {
    handleSearch() {
      const trimmed = this.searchQuery.trim();
      if (!trimmed) {
        this.searchResults = [];
        this.searchPerformed = true;
        return;
      }

      // 更新路由 query 参数（触发 watch）
      this.$router.push({
        name: 'search',
        query: { keyword: trimmed }
      });
    },

    performSearch(pageNum = 1) {
      if (!this.searchQuery.trim()) {
        this.searchResults = [];
        this.searchPerformed = true;
        return;
      }

      axios
          .get('http://localhost:8082/papers/search', {
            params: {
              keyword: this.searchQuery,
              pageNum,
              pageSize: 10,
            },
          })
          .then((response) => {
            this.searchResults = response.data.records || [];
            this.searchPerformed = true;
          })
          .catch((error) => {
            console.error('搜索失败:', error);
            this.searchResults = [];
            this.searchPerformed = true;
            if (error.response) {
              console.error('错误详情:', error.response.data);
            }
          });
    },
  }
};
</script>


<style scoped>
.content-section {
  text-align: center;
  padding-bottom: 2rem;
}

.search-box {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  margin-top: 1.5rem;
  margin-bottom: 1rem;
}

.search-box input {
  padding: 0.75rem 1rem;
  width: 13rem;
  font-size: 1rem;
  margin-right: 10px;
  border-radius: 8px;
  border: 1px solid #ddd;
  color: black;
}

.search-box button {
  padding: 0.75rem 1rem;
  background-color: #1a365d;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
}

.search-results {
  margin-top: 20px;
  padding: 0 1rem;
}

.results-grid {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  column-gap: 2rem;
  row-gap: 2rem;
  margin-top: 1rem;
}

.paper-card {
  background-color: #e4ecef;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 40%;
  min-width: 300px;
  cursor: pointer;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
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
</style>
