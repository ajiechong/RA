<template>
  <div class="background">
    <!-- 加载中显示 -->
    <div v-if="isLoading" class="loading-message">加载中...</div>
    <!-- 错误信息显示 -->
    <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
    <!-- 视频资源列表 -->
    <div class="video-resources-container">
      <div
          class="video-resource-card"
          v-for="video in videos"
          :key="video.rid"
      >
        <a :href="video.rurl" target="_blank">
          <img
              :src="require(`@/${video.rimg}`)"
              :alt="video.rname"
              class="video-resource-image"
          />
          <div class="video-resource-info">
            <h3 class="video-resource-category">{{ video.rtag }}</h3>
            <p class="video-resource-title">{{ video.rname }}</p>
          </div>
        </a>
        <!-- 收藏按钮 -->
        <img
            @click.prevent="toggleFavorite(video)"
            :src="video.isFavorited
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
import { API_BASE_URL } from '@/utils/api';

export default {
  name: "VideoResources",
  data() {
    return {
      videos: [], // 存储视频数据
      isLoading: true, // 加载状态
      errorMessage: "", // 错误信息
      userUid: null, // 当前用户ID
    };
  },
  mounted() {
    this.loadVideos();
    this.userUid = this.$store.getters.uid;
  },

  methods: {
    loadVideos() {
      axios.get(`${API_BASE_URL}/api/resourceVideos/all`)
          .then((response) => {
            console.log("API 返回的视频数据:", response.data);
            // 移除错误的默认收藏状态设置
            this.videos = response.data.map((video) => ({
              ...video,
              isFavorited: false  // 默认设置为未收藏
            }));

            // 只有登录用户才检查收藏状态
            if (this.userUid) {
              this.videos.forEach((video) => {
                axios.get(`${API_BASE_URL}/api/favorites/check`, {
                  params: {
                    Uid: this.userUid,
                    Rid: video.rid,
                    flag: 1,
                  },
                })
                    .then((favResponse) => {
                      video.isFavorited = favResponse.data.isFavorited;
                    })
                    .catch((error) => {
                      console.error("获取视频收藏状态出错：", error);
                    });
              });
            }
            this.isLoading = false;
          })
          .catch((error) => {
            console.error("获取视频资源数据出错：", error);
            this.errorMessage = "无法加载视频资源，请稍后重试。";
            this.isLoading = false;
          });
    },

    toggleFavorite(video) {
      if (!this.userUid) {
        alert("当前用户没有登陆，请登陆后再进行操作");
        return;
      }

      const rid = video.rid;
      if (!video.isFavorited) {
        axios
            .post(`${API_BASE_URL}/api/favorites/add`, null, {
              params: {
                Uid: this.userUid,
                Rid: rid,
                flag: 1, // 视频资源标识
              },
            })
            .then((response) => {
              console.log("收藏状态已更改为 收藏");
              if (response.data && response.data.success) {
                video.isFavorited = true;
                console.log("收藏成功");
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
        axios
            .post(`${API_BASE_URL}/api/favorites/remove`, null, {
              params: {
                Uid: this.userUid,
                Rid: rid,
                flag: 1, // 视频资源标识
              },
            })
            .then((response) => {
              console.log("收藏状态已更改为 未收藏");
              if (response.data && response.data.success) {
                video.isFavorited = false;
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


.video-resources-container {
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
  font-size: 14px;
  color: #449d44;
  font-weight: bold;
  font-family: 'Microsoft YaHei', sans-serif;
  margin-bottom: 10px;
}

.video-resource-title {
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
