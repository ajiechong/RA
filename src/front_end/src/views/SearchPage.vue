<template>
  <div class="background">
    <div class="home-page">
      <!-- 搜索结果容器 -->
      <div id="results-container">
        <p v-if="resources.length === 0 && !isLoading">未找到相关资源。</p>
        <p v-if="isLoading">正在搜索，请稍后...</p>

        <!-- 症状名称展示 -->
        <div v-if="symptoms.length > 0" class="symptoms-section">
          <div class="symptom-list">
            <!-- 使用 ArticleBox 来展示症状 -->
            <ArticleBox
                v-for="symptom in symptoms"
                :key="symptom.id"
                :title="symptom.title"
                :description="symptom.description"
                :articleId="symptom.id"
                :image="symptom.image"
                class="symptom-item"
            />
          </div>
        </div>

        <!-- 文章和视频资源展示 -->
        <div class="resources-section">
          <div class="resource-grid">
            <!-- 显示资源 -->
            <div v-for="(resource, index) in resources" :key="index" class="resource-item">
              <a :href="resource.rurl" target="_blank">
                <img
                    :src="require(`@/${resource.rimg}`)"
                    :alt="resource.rname"
                    class="article-resource-image"
                />
                <div class="article-resource-info">
                  <h3 class="article-resource-category">{{ resource.rtag }}</h3>
                  <p class="article-resource-title">{{ resource.rname }}</p>
                </div>
              </a>
              <!-- 收藏按钮 -->
              <img
                  @click.prevent="toggleFavoriteResource(resource)"
                  :src="resource.isFavorited
                ? require('@/assets/imgs/收藏2.png')
                : require('@/assets/imgs/收藏1.png')"
                  alt="收藏"
                  class="favorite-icon"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import ArticleBox from "@/components/ArticleBox.vue";
import axios from "axios";
import { API_BASE_URL } from '@/utils/api';

export default {
  name: "SearchPage",
  components: {
    ArticleBox
  },
  props: {
    query: {
      type: String,
      default: "", // 如果没有传递 query，设置默认值为空字符串
    },
  },
  data() {
    return {
      localQuery: "", // 本地存储的搜索关键字
      resources: [], // 搜索结果
      isLoading: false, // 搜索加载状态
      symptoms: [], // 存储症状名称
      userUid: null, // 用户UID
    };
  },
  watch: {
    query: {
      immediate: true,
      handler(newQuery) {
        this.localQuery = newQuery; // 更新本地的搜索关键字
        this.fetchResources(); // 根据新的搜索关键字发起搜索
      },
    },
    "$route.query.q": {
      immediate: true,
      handler(newQuery) {
        this.localQuery = newQuery || ""; // 更新本地的搜索关键字
        this.fetchResources(); // 根据新的搜索关键字发起搜索
      },
    },
  },
  mounted() {
    this.userUid = this.$store.getters.uid;
  },
  methods: {
    // 获取资源并加载收藏状态
    fetchResources() {
      const searchQuery = this.localQuery ? this.localQuery.trim() : "";

      if (!searchQuery) {
        console.warn("搜索关键字为空，未发起请求！");
        this.resources = [];
        return;
      }

      // 发起请求前清空旧的搜索结果并设置加载状态
      this.resources = [];
      this.isLoading = true;

      const byNameUrl = `${API_BASE_URL}/api/resources/by-name?rname=${encodeURIComponent(searchQuery)}`;
      const byTagUrl = `${API_BASE_URL}/api/resources/by-tag?name=${encodeURIComponent(searchQuery)}`;
      const byVideoNameUrl = `${API_BASE_URL}/api/resourceVideos/byName?rname=${encodeURIComponent(searchQuery)}`;
      const byVideoTagUrl = `${API_BASE_URL}/api/resourceVideos/byTag?name=${encodeURIComponent(searchQuery)}`;
      const bySymptomNameUrl = `${API_BASE_URL}/api/introductions/search?symptomName=${encodeURIComponent(searchQuery)}`;

      // 发起请求
      Promise.all([fetch(byNameUrl), fetch(byTagUrl), fetch(byVideoNameUrl), fetch(byVideoTagUrl), fetch(bySymptomNameUrl)]).then(([nameResponse, tagResponse, videoNameResponse, videoTagResponse, symptomResponse]) => {
        if (!nameResponse.ok || !tagResponse.ok || !videoNameResponse.ok || !videoTagResponse.ok || !symptomResponse.ok) {
          throw new Error("网络请求失败");
        }
        return Promise.all([nameResponse.json(), tagResponse.json(), videoNameResponse.json(), videoTagResponse.json(), symptomResponse.json()]);
      })
          .then(([nameData, tagData, videoNameData, videoTagData, symptomData]) => {
            // 将症状名称、图片和描述提取出来，存入症状列表
            this.symptoms = symptomData.map(symptom => ({
              title: symptom.title,
              image: symptom.image,  // 返回的图片路径
              description: symptom.description, // 返回的症状描述
              id: symptom.id
            }));

            // 合并文章和视频资源
            let allResources = [...nameData, ...tagData, ...videoNameData, ...videoTagData];
            allResources = this.removeDuplicates(allResources);

            // 获取每个资源的收藏状态（如果用户已经登录）
            if (this.userUid) {
              this.checkFavorites(allResources); // 获取并设置收藏状态
            } else {
              // 如果未登录，默认所有资源都为未收藏
              allResources.forEach(resource => {
                resource.isFavorited = false;
              });
            }

            this.resources = allResources;
          })
          .catch((error) => {
            console.error("搜索资源时发生错误:", error);
            alert("搜索资源失败，请稍后重试！");
          })
          .finally(() => {
            this.isLoading = false;
          });
    },

    // 去重函数，根据资源的唯一标识去重
    removeDuplicates(resources) {
      const seen = new Set();
      return resources.filter((resource) => {
        const isDuplicate = seen.has(resource.rurl);
        seen.add(resource.rurl);
        return !isDuplicate;
      });
    },

    // 获取并设置每个资源的收藏状态
    checkFavorites(resources) {
      const checkFavoritePromises = resources.map((resource) => {
        return axios.get(`${API_BASE_URL}/api/favorites/check`, {
          params: {
            Uid: this.userUid,
            Rid: resource.rid,
            flag: 0,
          }
        })
            .then((response) => {
              if (response.data && response.data.isFavorited) {
                resource.isFavorited = true;
              } else {
                resource.isFavorited = false;
              }
            })
            .catch((error) => {
              console.error("获取收藏状态失败：", error);
            });
      });

      // 等待所有收藏状态的请求完成后更新状态
      Promise.all(checkFavoritePromises).then(() => {
        this.resources = [...resources];
      });
    },

    // 切换收藏状态
    toggleFavoriteResource(resource) {
      if (!this.userUid) {
        alert("当前用户没有登陆，请登陆后再进行操作");
        return;
      }

      const rid = resource.rid;
      if (!resource.isFavorited) {
        const params = new URLSearchParams();
        params.append("Rid", rid);
        params.append("flag", 0);
        params.append("Uid", this.userUid);

        axios
            .post(`${API_BASE_URL}/api/favorites/add`, params)
            .then((response) => {
              if (response.data && response.data.success) {
                resource.isFavorited = true; // 更新为已收藏
              } else {
                console.error("添加收藏失败:", response.data.message);
                alert("添加收藏失败，请稍后再试");
              }
            })
            .catch((error) => {
              console.error("添加收藏失败:", error);
              alert("添加收藏失败，请稍后再试");
            });
      } else {
        const params = new URLSearchParams();
        params.append("Rid", rid);
        params.append("flag", 0);
        params.append("Uid", this.userUid);

        axios
            .post(`${API_BASE_URL}/api/favorites/remove`, params)
            .then((response) => {
              if (response.data && response.data.success) {
                resource.isFavorited = false; // 更新为未收藏
              } else {
                console.error("取消收藏失败:", response.data.message);
                alert("取消收藏失败，请稍后再试");
              }
            })
            .catch((error) => {
              console.error("取消收藏失败:", error);
              alert("取消收藏失败，请稍后再试");
            });
      }
    },
  },
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
  margin-top: 40px;

}

#results-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
  padding-top: 60px;
  padding-bottom: 80px;
  max-width: 1200px;
  margin: 20px auto;
  padding-left: 40px;
  padding-right: 40px;
}

.symptoms-section {
  margin-bottom: 10px;
}

.resource-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); /* 每行3个资源 */
  gap: 20px;
}

.resource-item {
  position: relative;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease;
}

.article-resource-image {
  width: 100%;
  height: 210px;
  object-fit: cover;
}

.resource-item:hover {
  transform: translateY(-5px);
}

.symptom-item {
  width: 100%;
  margin-bottom: 20px;
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

.favorite-icon {
  width: 30px;
  height: 30px;
  cursor: pointer;
  position: absolute;
  bottom: 10px;
  right: 10px;
}
</style>
