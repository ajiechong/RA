<template>
  <div>
    <div class="content-section">
      <h2 class="intro-title">Explore and discover research papers effortlessly.</h2>

      <div class="news-section">
        <!-- 加入轮播图组件 -->
        <div class="carousel-wrapper">
          <HomeCarousel :items="carouselItems" />
        </div>

        <!-- 右侧资源列表部分 -->
        <div class="news-list">
          <div class="resource-list">
            <!-- 点击资源卡片跳转到对应 URL -->
            <ResourceCard
                v-for="resource in resources"
                :key="resource.id"
                :title="resource.title"
                :author="resource.author"
                :time="resource.time"
                :url="resource.url"
            />
          </div>
          <button class="view-all-btn" @click="goToResources">
            更多文章视频资源
            <span class="logo-arrow-container">
            <img src="@/assets/imgs/arrow_grey.png" alt="arrow" class="logo-arrow" />
          </span>
          </button>
        </div>
      </div>

      <div class="features">
        <div class="feature-card">
          <h3>文献搜索</h3>
          <p>根据关键词搜索科研论文，提供快速高效的检索。</p>
        </div>
        <div class="feature-card">
          <h3>文献推荐</h3>
          <p>根据您的兴趣推送多样化的科研文献。</p>
        </div>
        <div class="feature-card">
          <h3>AI 对话</h3>
          <p>提供学术润色、文献综述、实验设计等帮助。</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import HomeCarousel from '@/components/HomeCarousel.vue'; // 引入轮播组件
import word1 from '@/assets/imgs/word1.png';
import wordcloud from '@/assets/imgs/wordcloud.png';
import ResourceCard from "@/components/ResourceCard.vue"

export default {
  name: 'HomePage',
  components: {
    HomeCarousel, // 注册组件
    ResourceCard
  },
  methods: {
    goToResources() {
      // 使用 $router 跳转到资源页面
      this.$router.push({ name: 'VideoResourcePage' })
    }
  },
  data() {
    return {
      // 示例轮播数据
      carouselItems: [
        {
          id: 1,
          image: word1,
          title: 'AI in Healthcare',
          author: '作者',
          category: '标签',
          url: 'https://example.com/article1'
        },
        {
          id: 2,
          image: wordcloud,
          title: 'Quantum Computing Advances',
          author: '作者',
          category: '标签',
          url: 'https://example.com/article2'
        }
      ],
      resources: [
        {
          id: 1,
          title: '【科普】什么是LLM大语言模型？',
          author: '',
          time: '发布于 2023-04-17 14:46',
          url: 'https://zhuanlan.zhihu.com/p/622518771'
        },
        {
          id: 2,
          title: '【实战】AI大模型开发实战教程',
          author: '',
          time: '发布于 2025-05-08 09:00',
          url: 'https://www.bilibili.com/video/BV1h1VbzHER2/?spm_id_from=333.337.search-card.all.click&vd_source=a6b12fba12fff5997f048865311b0f5a'
        },
        {
          id: 3,
          title: 'Machine Learning: 十大机器学习算法',
          author: '',
          time: '发布于 2018-02-13 01:21',
          url: 'https://zhuanlan.zhihu.com/p/33794257'
        },
        {
          id: 4,
          title: '2025年中国大模型行业全景图谱',
          author: '',
          time: '发布于 2025-04-18 10:00',
          url: 'https://www.qianzhan.com/analyst/detail/220/250418-c45ee160.html'
        }
      ]
    };
  }
};

</script>

<style scoped>
.content-section {
  padding: 2rem;
  text-align: center;
}

.intro-title {
  font-size: 2.5rem;
  font-weight: 800;
  text-align: center;
  margin-top: 2rem;
  background: linear-gradient(90deg, #bbb, #eee);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.2);
  letter-spacing: 2px;
  animation: fadeSlideIn 1s ease-out;
  font-family: "Helvetica Neue", "Segoe UI", "Roboto", Arial, sans-serif;
  font-weight: 300;
  letter-spacing: 0.5px;
}

@keyframes fadeSlideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.news-section {
  display: flex;
  gap: 2rem;
  margin-bottom: 3rem;
  margin-top: 3rem;
}

.carousel-wrapper {
  width: 45%; /* 调整这里控制宽度 */
  margin-left: 10rem;
  margin-right: 2rem;
  margin-top: 1rem;
}


.resource-list {
  display: flex;
  flex-direction: column;
  gap: 0.7rem;
  margin-top: 1rem;
}

@media (max-width: 992px) {
  .news-section {
    flex-direction: column;
  }

  .carousel-container,
  .news-list {
    flex: none;
    width: 100%;
  }
}

.view-all-btn {
  background-color: transparent;
  line-height: 1.2;
  color: #c8c7c7;
  border: none;
  font-size: 1.2rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-left: auto;
  margin-right: auto;
  font-weight: 450;
}

.logo-arrow {
  width: 40px;
  height: auto; /* 高度自动，保持比例 */
}

.features {
  display: flex;
  justify-content: center;
  gap: 2.5rem;
  margin-top: 2rem;
  flex-wrap: wrap;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  margin-top: 2rem;
  padding: 1.5rem;
  border-radius: 10px;
  width: 250px;
  height: 150px;
  text-align: left;
  color: white;
  backdrop-filter: blur(6px);
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-card h3 {
  font-size: 1.5rem;
  font-weight: 300;
  padding-bottom: 0.5rem;
  letter-spacing: 1px;
}

.feature-card p {
  font-size: 1rem;
  font-weight: 300;
  letter-spacing: 1px;
  color: #e2e0e0;
}
</style>
