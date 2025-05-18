
<template>
  <div class="background">
    <!-- 加载中显示 -->
    <div v-if="isLoading" class="loading-message">加载中...</div>
    <!-- 错误信息显示 -->
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <!-- 资源列表 -->
    <div class="article-resources-container">
      <div
          class="article-resource-card"
          v-for="resource in resources"
          :key="resource.rid"
      >
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
</template>

<script>
import axios from "axios";
import {API_BASE_URL} from "@/utils/api";

export default {
  name: "ArticleResources",
  data() {
    return {
      resources: [], // 存储资源数据
      isLoading: true, // 加载状态
      errorMessage: "", // 错误信息
      userUid: null,
    };
  },
  mounted() {
    this.loadResources();
    this.userUid = this.$store.getters.uid;
  },


  methods: {
    loadResources() {
      axios.get(`${API_BASE_URL}/api/resources/all`)
          .then((response) => {
            console.log("API 返回的数据:", response.data);
            // 移除错误的默认收藏状态设置
            this.resources = response.data.map((resource) => ({
              ...resource,
              isFavorited: false  // 默认设置为未收藏
            }));

            // 只有登录用户才检查收藏状态
            if (this.userUid) {
              this.resources.forEach((resource) => {
                axios.get(`${API_BASE_URL}/api/favorites/check`, {
                  params: {
                    Uid: this.userUid,
                    Rid: resource.rid,
                    flag: 0,
                  },
                })
                    .then((favResponse) => {
                      resource.isFavorited = favResponse.data.isFavorited;
                    })
                    .catch((error) => {
                      console.error("获取文章收藏状态出错：", error);
                    });
              });
            }
            this.isLoading = false;
          })
          .catch((error) => {
            console.error("获取资源数据出错：", error);
            this.errorMessage = "无法加载资源数据，请稍后重试。";
            this.isLoading = false;
          });
    },

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
              console.log("收藏状态已更改为 收藏");
              if (response.data && response.data.success) {
                resource.isFavorited = true;
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
              console.log("收藏状态已更改为 未收藏");
              if (response.data && response.data.success) {
                resource.isFavorited = false;
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


.article-resources-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  padding-top: 130px;
  padding-bottom: 100px;
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
  font-size: 14px;
  color: #449d44;
  font-weight: bold;
  font-family: 'Microsoft YaHei', sans-serif;
  margin-bottom: 10px;
}

.article-resource-title {
  font-size: 18px;
  color: #333;
  line-height: 1.5;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
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