<template>
  <div class="background"></div>
  <div class="collection-container">
    <div class="user-info" style="margin-top: 400px;">
      <img :src="avatarUrl || require('@/assets/imgs/tests/1.png')" alt="用户头像" class="avatar-img">
      <span class="nickname">{{ username }}</span>
    </div>
    <div class="collection-content">
      <div class="tab-container">
        <span class="tab" :class="{ active: articleActive }" @click="toggleTab('article')">文章</span>
        <span class="tab" :class="{ active: videoActive }" @click="toggleTab('video')">视频</span>
      </div>
      <div v-if="showArticle" class="article-resources-container active">
        <div v-if="loading">加载中...</div>
        <div v-else-if="articleCollection.length === 0">暂无收藏的文章</div>
        <div class="article-resource-card" v-for="article in articleCollection" :key="article.id">
          <a :href="article.link" target="_blank">
            <img :src="article.img" :alt="article.title" class="article-resource-image" />
            <div class="article-resource-info">
              <h3 class="article-resource-category">{{ article.category }}</h3>
              <p class="article-resource-title">{{ article.title }}</p>
            </div>
          </a>
          <img
              @click="toggleFavorite(article, 0)"
              src="@/assets/imgs/收藏2.png"
              alt="取消收藏"
              class="favorite-icon"
          />
        </div>
      </div>
      <div v-if="showVideo" class="video-resources-container active">
        <div v-if="loading">加载中...</div>
        <div v-else-if="videoCollection.length === 0">暂无收藏的视频</div>
        <div class="video-resource-card" v-for="video in videoCollection" :key="video.id">
          <a :href="video.link" target="_blank">
            <img :src="video.img" :alt="video.title" class="video-resource-image" />
            <div class="video-resource-info">
              <h3 class="video-resource-category">{{ video.category }}</h3>
              <p class="video-resource-title">{{ video.title }}</p>
            </div>
          </a>
          <img
              @click="toggleFavorite(video, 1)"
              src="@/assets/imgs/收藏2.png"
              alt="取消收藏"
              class="favorite-icon"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';
import { API_BASE_URL } from '@/utils/api';

const showArticle = ref(true);
const showVideo = ref(false);
const articleActive = ref(true);
const videoActive = ref(false);
const loading = ref(false);
const articleCollection = ref([]);
const videoCollection = ref([]);

const store = useStore();
const username = computed(() => store.getters.username);
const avatarUrl = computed(() => store.getters.avatar_url);
const Uid = store.getters.uid;

onMounted(() => {
  fetchFavoriteResources();
});

const toggleTab = (type) => {
  if (type === 'article') {
    showArticle.value = true;
    showVideo.value = false;
    articleActive.value = true;
    videoActive.value = false;
  } else if (type === 'video') {
    showArticle.value = false;
    showVideo.value = true;
    articleActive.value = false;
    videoActive.value = true;
  }
  fetchFavoriteResources();
};


const toggleFavorite = async (resource, flag) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/api/favorites/remove`, null, {
      params: {
        Uid,
        Rid: resource.id,
        flag
      }
    });

    if (response.data && response.data.success) {
      // Remove the item from the collection
      if (flag === 0) {
        articleCollection.value = articleCollection.value.filter(item => item.id !== resource.id);
      } else {
        videoCollection.value = videoCollection.value.filter(item => item.id !== resource.id);
      }
    } else {
      alert("取消收藏失败，请稍后再试");
    }
  } catch (error) {
    console.error("取消收藏失败:", error);
    alert("取消收藏失败，请稍后再试");
  }
};

const fetchFavoriteResources = async () => {
  loading.value = true;
  const flag = showArticle.value? 0 : 1;
  try {
    const response = await axios.get(`${API_BASE_URL}/api/favorites/resources`, { params: { Uid, flag } });
    const resources = response.data;
    console.log("response.data:",response.data)
    if (flag === 0) {
      articleCollection.value = resources.map(resource => ({
        id: resource.resource.rid,
        img: require(`@/${resource.resource.rimg}`),
        category: resource.resource.rtag,
        title: resource.resource.rname,
        link: resource.resource.rurl
      }));
      console.log("articleCollection.value:",articleCollection.value)
    } else {
      videoCollection.value = resources.map(resource => ({
        id: resource.resource.rid,
        img: require(`@/${resource.resource.rimg}`),
        category: resource.resource.rtag,
        title: resource.resource.rname,
        link: resource.resource.rurl
      }));
    }
  } catch (error) {
    console.error('获取收藏资源失败:', error);
  } finally {
    loading.value = false;
  }
};



</script>



<style scoped>
.collection-container {
  position: relative;
  width: 100%;
  height: 100vh;
  background-image: url('@/assets/imgs/background4.jpg');
  background-size: cover;
  background-position: center;
}
.background {
  position: absolute;
  top: -500px;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: 1800px auto;
  background-position: center;
  z-index: -1;
  background-image: url('@/assets/imgs/final.jpg');
}
.user-info {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}
.avatar-img {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  transform: translateY(20px);
}
.nickname {
  margin-top: 10px;
  font-size: 18px;
  transform: translateY(20px);
}
.collection-content {
  margin-top: 50px;
  padding: 0 50px;
}
/* 新增的标签容器样式 */
.tab-container {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-bottom: 20px;
}
.tab {
  cursor: pointer;
  padding: 10px 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
  transition: background-color 0.3s;
}
.tab.active {
  background-color: #007bff; /* 这里设置你想要的深色 */
  color: white;
}
/* 文章和视频容器样式调整 */
.article-resources-container,
.video-resources-container {
  display: none;
  padding-top: 0;
}
.article-resources-container.active,
.video-resources-container.active {
  display: grid;
}
.article-resources-container {
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  max-width: 1200px;
  margin: 20px auto;
  padding-left: 40px;
  padding-right: 40px;
}
.article-resource-card {
  position: relative;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease;
}
.article-resource-card:hover {
  transform: translateY(-5px);
}
.article-resource-image {
  width: 100%;
  height: 210px;
  object-fit: cover;
}
.article-resource-info {
  padding: 15px;
}
.article-resource-category {
  font-size: 16px;
  color: #449d44;
  font-weight: bold;
}
.article-resource-title {
  font-size: 18px;
  color: #555;
  line-height: 1.5;
}
.video-resources-container {
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  max-width: 1200px;
  margin: 20px auto;
  padding-left: 40px;
  padding-right: 40px;
}
.video-resource-card {
  position: relative;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease;
}
.video-resource-card:hover {
  transform: translateY(-5px);
}
.video-resource-image {
  width: 100%;
  height: 210px;
  object-fit: cover;
}
.video-resource-info {
  padding: 15px;
}
.video-resource-category {
  font-size: 16px;
  color: #449d44;
  font-weight: bold;
}
.video-resource-title {
  font-size: 18px;
  color: #555;
  line-height: 1.5;
}


.favorite-icon {
  width: 30px;
  height: 30px;
  cursor: pointer;
  position: absolute;
  bottom: 10px;
  right: 10px;
}
</style>