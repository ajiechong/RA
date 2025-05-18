<template>
  <div class="background">
    <div class="profile-page">
      <!-- 错误提示区域 -->
      <div v-if="showError" class="error-message">
        <p>Identifier 参数丢失，请检查链接或联系管理员。</p>
      </div>

      <!-- 左侧导航栏 -->
      <div class="sidebar" v-if="!showError">
        <div class="settings-header">
          <span class="settings-label">设置</span>
        </div>

        <!-- 设置子选项 -->
        <div class="settings-options">
          <button
              :class="{ active: currentTab === 'profile' }"
              @click="changeTab('profile')"
          >
            个人设置
          </button>
          <button
              :class="{ active: currentTab === 'security' }"
              @click="changeTab('security')"
          >
            安全设置
          </button>
        </div>
      </div>

      <!-- 右侧内容区 -->
      <div class="content" v-if="!showError">
        <!-- 根据选项渲染不同的组件 -->
        <Profile_Info
            v-if="currentTab === 'profile' "
        />
        <Security_Info v-if="currentTab === 'security' " />
      </div>
    </div>
  </div>

</template>

<script>
import Profile_Info from '../components/Profile_Info.vue';
import Security_Info from '../components/Security_Info.vue';

export default {
  name: 'ProfilePage',
  components: {
    Profile_Info,
    Security_Info,
  },
  data() {
    return {
      currentTab: 'profile', // 默认显示 "个人设置"
      identifier: null,      // 用来存储从查询参数中获取的 identifier
      showError: false,      // 错误提示的状态
    };
  },

  methods: {
    changeTab(tab) {
      this.currentTab = tab;
    },
  },
};
</script>

<style scoped>

.background{
  background-image: url('@/assets/imgs/background4.jpg');
  background-size: cover;
  background-position: center;
  min-height: 1000px;
  height: 1050px;
  padding-top: 130px;
}


/* 错误提示样式 */
.error-message {
  color: red;
  text-align: center;
  font-size: 1.2em;
  margin-top: 20px;
  background-color: #f8d7da;
  padding: 15px;
  border-radius: 8px;
  border: 1px solid #f5c6cb;
}

/* 设置整体容器 */
.profile-page {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  margin-top: 150px;
  height: 600px;
  background-color: #f9f9f9; /* 整体背景色统一 */
  border-radius: 10px; /* 整体圆角 */
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); /* 整体阴影 */
}

/* 左侧导航栏样式 */
.sidebar {
  width: 250px;
  background-color: #ffffff; /* 与右侧统一背景色 */
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 去掉右侧阴影 */
  height: 100%;
  border-radius: 10px 0 0 10px; /* 圆角处理 */
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

/* 设置标签样式 */
.settings-header {
  margin-bottom: 20px;
}

.settings-label {
  font-weight: bold;
  display: flex;  /* 启用flex布局 */
  justify-content: center;  /* 水平居中 */
  align-items: center;  /* 垂直居中 */
  height: 100%;  /* 保证容器有高度 */
}

/* 设置子选项按钮样式 */
.settings-options button {
  display: block;
  width: 100%;
  padding: 12px 20px;
  margin: 10px 0;  /* 设置上下间隔 */
  border: none;
  background-color: #f0f0f0;
  cursor: pointer;
  font-size: 1.1em;
  border-radius: 8px;
  font-weight: 600;
  color: #666;
  transition: all 0.3s ease;
}

.settings-options button:hover {
  background-color: #e0e0e0;  /* 鼠标悬停效果 */
}

.settings-options button.active {
  background-color: #5cb85c;
  color: white;
  transform: translateY(-2px);  /* 激活按钮的按下反馈 */
}

/* 右侧内容区样式 */
.content {
  flex-grow: 1;
  padding: 20px;
  margin-left: 0; /* 取消间隙 */
  background-color: #ffffff; /* 与左侧统一背景色 */
  border-radius: 0 10px 10px 0; /* 圆角处理 */
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); /* 去掉右侧阴影 */
}

/* 标题样式 */
h2 {
  text-align: center;
  font-size: 1.8em;
  font-weight: bold;
  color: #333;
  margin-top: 30px;
}

/* 响应式布局 */
@media (max-width: 768px) {
  .profile-page {
    flex-direction: column;
  }
  .sidebar {
    width: 100%;
    height: auto;
    padding: 15px;
  }
  .content {
    margin-left: 0;
  }
}
</style>
