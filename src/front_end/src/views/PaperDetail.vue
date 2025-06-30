<template>
  <div class="paper-detail">
    <div class="card">
      <h1>{{ paper.title }}</h1>
      <p class="meta">作者：{{ paper.authors }}<br>发表日期：{{ paper.submissionDate }}</p>
      <div class="abstract-section">
        <h3>摘要</h3>
        <p>{{ paper.abstract }}</p>
      </div>
      <div class="link-section" style="display: flex;justify-content: center; flex-wrap: wrap; gap: 35px; margin-left: -10px">
          <div>
            <a :href="paper.pdfLink" target="_blank" rel="noopener" class="btn-style">
              <i class="fa fa-file-pdf text-red-500"></i> 点击查看原文 PDF
            </a>
          </div>
          <div>
            <a href="javascript:void(0)" @click="navigateToSummary(paper.id,paper.title,paper.pdfLink)" class="btn-style">
              <i class="fa fa-summary text-blue-500"></i> 点击进行文献分析
            </a>
          </div>
          <div>
            <a href="javascript:void(0)" @click="navigateToRelated(paper.id,paper.title,paper.pdfLink)" class="btn-style">
              <i class="fa fa-book text-green-500"></i> 点击搜索相关文献
            </a>
          </div>
          <div>
            <a href="javascript:void(0)" @click="navigateToTranslated(paper.id,paper.title,paper.pdfLink)" class="btn-style">
              <i class="fa fa-book text-green-500"></i> 点击翻译并概括论文
            </a>
          </div>
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
    },
      // 跳转到文献总结页面
      navigateToSummary(paperId,paperTitle,paperPdfLink) {
          this.$router.push({
              name: 'SummaryPage',
              params: { id: paperId }
          });
          sessionStorage.setItem('selectedPaper', JSON.stringify({
              id: paperId,
              title: paperTitle,
              pdf: paperPdfLink
          }));
      },
      // 跳转到相关文献页面
      navigateToRelated(paperId,paperTitle,paperPdfLink) {
          this.$router.push({
              name: 'RelatedPapers',
              params: { id: paperId }
          })
          sessionStorage.setItem('selectedPaper', JSON.stringify({
              id: paperId,
              title: paperTitle,
              pdf: paperPdfLink
          }));
      },
      //跳转到文献翻译页面
      navigateToTranslated(paperId,paperTitle,paperPdfLink) {
          this.$router.push({
              name: 'Translated',
              params: { id: paperId }
          });
          sessionStorage.setItem('selectedPaper', JSON.stringify({
              id: paperId,
              title: paperTitle,
              pdf: paperPdfLink
          }));
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
