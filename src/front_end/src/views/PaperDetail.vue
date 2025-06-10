<template>
  <div class="paper-detail">
    <div class="card">
      <h1>{{ paper.title }}</h1>
      <p class="meta">作者：{{ paper.authors }}<br>发表日期：{{ paper.submissionDate }}</p>
      <div class="abstract-section">
        <h3>摘要</h3>
        <p>{{ paper.abstract }}</p>
      </div>
      <div class="link-section">
        <a :href="paper.pdfLink" target="_blank" rel="noopener">点击查看原文 PDF</a>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PaperDetail',
  data() {
    return {
      paper: {}
    };
  },
  created() {
    const id = this.$route.params.id;
    this.fetchPaperDetail(id);
  },
  methods: {
    fetchPaperDetail(id) {
      axios.get(`http://localhost:8082/papers/${id}`)
        .then(response => {
          this.paper = {
            id: response.data.paperId,
            title: response.data.title,
            authors: response.data.authors,
            abstract: response.data.abstractText,
            submissionDate: response.data.submissionDate,
            pdfLink: response.data.pdfLink
          };
        })
        .catch(error => {
          console.error('获取论文详情失败:', error);
        });
    }
  }
};
</script>

<style scoped>
.paper-detail {
  padding: 40px 20px;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.card {
  background-color: #fff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 1000px;
}

h1 {
  font-size: 2rem;
  color: #1a365d;
  margin-bottom: 10px;
}

.meta {
  font-size: 0.9rem;
  color: #666;
  margin-bottom: 20px;
}

.abstract-section {
  margin-top: 20px;
}

.abstract-section h3 {
  margin-bottom: 10px;
  color: #333;
}

.abstract-section p {
  line-height: 1.6;
  color: #555;
}

.link-section {
  margin-top: 30px;
}

.link-section a {
  color: #0077cc;
  font-weight: bold;
  text-decoration: underline;
}
</style>
