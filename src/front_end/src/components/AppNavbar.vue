<template>
  <div class="app-nav-bar">
    <div class="user-avatar-container"
         @click="isLoggedIn ? goToProfile() : showLoginModal()"
         @mouseover="isLoggedIn && (avatarHover = true, isDropdownVisible = true)"
         @mouseleave="avatarHover = false, isDropdownVisible = false">
      <img
          :src="isLoggedIn ? (avatar_url || 'https://tse3-mm.cn.bing.net/th/id/OIP-C.yp-D-KHI3e2nN4eMBJcEVAAAAA?rs=1&pid=ImgDetMain') : require('@/assets/imgs/avatar/login.png')"
          alt="用户头像"
          class="user-avatar-img"
          :class="{'avatar-hover': avatarHover}">
      <!-- 下拉框 -->
      <div v-if="isDropdownVisible && isLoggedIn" class="dropdown-menu" @click.stop>
        <div class="user-info">
          <div class="username">{{ username }}</div>
        </div>
        <div class="button-group">
          <div class="button" @click="goToProfile">
            <img src="@/assets/imgs/icon/user.png" alt="设置" class="icon">
            <span style="color: black;">个人设置</span>
          </div>
          <div class="button" @click="gotoCollect">
            <img src="@/assets/imgs/icon/collect.png" alt="收藏" class="icon">
            <span style="color: black;">收藏</span>
          </div>
          <div class="button" @click="logout">
            <img src="@/assets/imgs/icon/logout.png" alt="退出" class="icon">
            <span style="color: black;">退出登录</span>
          </div>
        </div>
      </div>
    </div>
    <!-- 登录框 -->
    <div v-if="show_Login" class="login-modal">
      <div class="login-modal-content">
        <span class="close-btn" @click="closeLoginModal">×</span>
        <div class="login-tabs">
          <span :class="{'active': isPasswordLogin}" @click="setLoginMethod('password')">密码登录</span>
          |
          <span :class="{'active': !isPasswordLogin && !isRegistering}" @click="setLoginMethod('captcha')">邮箱验证</span>
        </div>

        <!-- 密码登录 -->
        <div v-if="isPasswordLogin && !isRegistering" class="login-form">
          <div class="form-group">
            <label for="username" class="input-label">账号</label>
            <input id="username" type="text" v-model="identifier" placeholder="请输入用户id或者邮箱" class="input-field" />
          </div>
          <div class="form-group">
            <label for="password" class="input-label">密码</label>
            <el-input id="password" type="password" v-model="password" placeholder="请输入密码" class="input-field" style="width: 80%;height:100%;color: #888;z-index:1000;"
                      show-password />
          </div>
          <div class="button-group">
            <button @click="login_verify" class="btn">登录</button>
            <button @click="startRegister" class="btn">注册</button>
          </div>
        </div>

        <!-- 邮箱验证码登录 -->
        <div v-if="!isPasswordLogin && !isRegistering" class="login-form">
          <div class="form-group">
            <label for="email" class="input-label">邮箱</label>
            <input id="email" type="text" v-model="email" placeholder="请输入邮箱" class="input-field" />
          </div>
          <div class="captcha-group">
            <label for="captcha" class="captcha-input-label">验证码</label>
            <input type="tel" v-model="captcha" class="captcha-input" placeholder="请输入验证码" maxlength="6" @input="validateCaptcha" inputmode="numeric" />
            <button class="captcha-btn" :disabled="isSending || !isEmailValid" @click="startCountdown">{{ countdownText }}</button>
          </div>
          <div class="button-group">
            <button @click="register_verify" class="btn">登录/注册</button>
          </div>
        </div>

        <div class="agreement-text">
          如果你没注册过 SoWellLife，我们将自动帮你注册账号，登录或完成注册代表你同意
          <a href="/user-agreement" target="_blank">用户协议</a> 和
          <a href="/privacy-policy" target="_blank">隐私政策</a>。
        </div>
      </div>
    </div>
    <!-- 欢迎信息 -->
    <div class="welcome">
      <h1>Welcome to Research Assistant</h1>
      <p>Your AI-powered research companion</p>
    </div>
    <!-- 功能导航 -->
    <div class="tabs">
      <button class="tab" :class="{ active: $route.name === 'home' }" @click="goTo('home')">Home</button>
      <button class="tab" :class="{ active: $route.name === 'search' }" @click="goTo('search')">Search</button>
      <button class="tab" :class="{ active: $route.name === 'recommend' }" @click="goTo('recommend')">Recommend</button>
      <button class="tab" :class="{ active: $route.name === 'VideoResourcePage' }" @click="goTo('VideoResourcePage')">Video</button>
      <button class="tab" :class="{ active: $route.name === 'ArticleResourcePage' }" @click="goTo('ArticleResourcePage')">Article</button>
      <button class="tab" :class="{ active: $route.name === 'chat'}" @click="goTo('chat')">Chat</button>
      <button class="tab" :class="{ active: $route.name === 'MultiagentPage' }" @click="goTo('MultiagentPage')">MultiAgent</button>
    </div>
  </div>

</template>

<script>
import {mapState, mapActions, useStore} from 'vuex';
import axios from 'axios';
// import {onMounted} from "vue";
import { API_BASE_URL } from '@/utils/api';

export default {
  name: "AppNavbar",

  setup() {
    useStore();
    // onMounted(() => {
    //   store.dispatch('restoreUser');
    // });
  },

  data() {
    return {
      isPasswordLogin: true,
      isRegistering: false,
      identifier: '',
      username: '',
      password: '',
      email: '',
      captcha: '',
      isSending: false,
      countdown: 60,
      countdownText: '获取验证码',
      isEmailValid: true,
      avatarHover: false,
      isDropdownVisible: false,
      show_Login: false,
      showDropdown: false,
      searchQuery: '' // 搜索框输入内容
    };
  },
  computed: {
    ...mapState({
      isLoggedIn: state => state.isLoggedIn,
      username: state => state.username,
      avatar_url: state => state.avatar_url,
    })
  },
  methods: {
    goTo(tabName) {
      this.$router.push({ name: tabName });
    },


    ...mapActions(['login', 'logout']),
    goToProfile() {
      this.$router.push('/profile');
    },

    gotoCollect() {
      this.$router.push('/collection');
    },

    showLoginModal() {
      this.show_Login = true;
    },

    closeLoginModal() {
      this.show_Login = false;
    },

    setLoginMethod(method) {
      if (method === 'password') {
        this.isPasswordLogin = true;
        this.isRegistering = false;
      } else if (method === 'captcha') {
        this.isPasswordLogin = false;
        this.isRegistering = false;
      }
    },

    startRegister() {
      this.setLoginMethod('captcha');
    },

    performSearch() {
      if (!this.searchQuery.trim()) return;
      this.$router.push({ path: '/search', query: { q: this.searchQuery.trim() } });
    },


    // 登录功能
    async login_verify() {
      try {
        console.log("开始发起登录请求...");

        // 发起密码验证请求
        const response = await axios.post(`${API_BASE_URL}/api/login/verify-password`, null, {
          params: {
            identifier: this.identifier,
            password: this.password
          },
          headers: { 'Content-Type': 'application/json' }
        });

        if (response.data.success) {
          // 获取用户信息
          const userInfoResponse = await axios.post(`${API_BASE_URL}/api/login/profile`, null, {
            params: { identifier: this.identifier }
          });

          // 打印用户信息，包括头像信息
          console.log("用户信息：", userInfoResponse.data);
          console.log("用户名：", userInfoResponse.data.uname);
          console.log("头像 URL：", userInfoResponse.data.avatarUser);
          console.log("用户 ID：", userInfoResponse.data.uid);

          // 存储用户信息到 Vuex
          this.$store.dispatch('login', {
            username: userInfoResponse.data.uname,
            avatar_url: userInfoResponse.data.avatarUser,
            uid: userInfoResponse.data.uid
          });

          // 登录成功的状态更新
          this.isLoggedIn = true;
          this.closeLoginModal();

        } else {
          alert('登录失败，请检查用户名或密码');
        }
      } catch (error) {
        console.error('登录请求失败', error);
        alert('登录失败，请稍后重试');
      }
    },


    logout() {
      this.$store.dispatch('logout');
      this.isDropdownVisible = false;
      this.$router.push('/');
    },

    goToResources(type) {
      this.$router.push(`/resources/${type}`);
    },
    // 注册功能
    async register_verify() {
      try {
        console.log("开始发起验证码登录请求...");

        // 发送验证码验证请求
        const response = await axios.post(`${API_BASE_URL}/api/login/login-by-code`, null, {
          params: {
            email: this.email,
            code: this.captcha
          },
          headers: { 'Content-Type': 'application/json' }
        });

        console.log("验证码登录返回的数据：", response.data);

        if (response.data.success) {
          // 登录成功后，获取完整的用户信息
          const userInfo = response.data.user;  // 获取返回的用户信息

          if (userInfo && userInfo.uid) {
            // 获取用户的完整信息
            try {
              const userInfoResponse = await axios.get(`${API_BASE_URL}/api/login/profile`, {
                params: { identifier: userInfo.uid }
              });

              const fullUserInfo = userInfoResponse.data;

              // 使用默认头像如果没有设置头像
              const defaultAvatarUrl = "https://tse3-mm.cn.bing.net/th/id/OIP-C.yp-D-KHI3e2nN4eMBJcEVAAAAA?rs=1&pid=ImgDetMain";

              this.$store.dispatch('login', {
                username: fullUserInfo.uname,
                avatar_url: fullUserInfo.avatarUser || defaultAvatarUrl,
                uid: fullUserInfo.uid
              });

              this.isLoggedIn = true;
              this.closeLoginModal();
              alert('登录成功');
            } catch (error) {
              console.error('获取用户完整信息失败:', error);
              alert('登录失败，无法获取用户信息');
            }
          } else {
            alert('登录失败，用户信息不完整');
          }
        } else {
          alert(response.data.message);  // 显示错误消息
        }
      } catch (error) {
        console.error('验证码登录请求失败', error);
        alert('登录失败，请稍后重试');
      }
    },


    // 发送验证码
    startCountdown() {
      // 如果邮箱无效，提示并返回
      if (!this.isEmailValid) {
        alert('请先输入有效的邮箱地址');
        return;
      }

      // 如果正在发送验证码，不允许再次点击
      if (this.isSending) {
        return;
      }

      // 设置正在发送验证码
      this.isSending = true;
      this.countdown = 60;  // 重置倒计时为60秒
      this.countdownText = `${this.countdown}秒`;

      // 发送验证码请求
      axios.post(`${API_BASE_URL}/api/login/register`, null, {
        params: { email: this.email }
      })
          .then(response => {
            // 验证码发送成功
            console.log("验证码发送成功：", response.data);

            // 启动倒计时
            this.startTimer();
          })
          .catch(error => {
            // 验证码发送失败
            console.error("验证码发送失败：", error);
            alert('验证码发送失败，请稍后重试');

            // 发送失败时取消倒计时
            this.isSending = false;
            this.countdownText = '获取验证码';  // 重置按钮文字
          });
    },

    startTimer() {
      // 启动倒计时
      this.countdownInterval = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--;
          this.countdownText = `${this.countdown}秒`;
        } else {
          clearInterval(this.countdownInterval);  // 倒计时结束时清除定时器
          this.isSending = false;
          this.countdownText = '获取验证码';
        }
      }, 1000);
    },



    // 校验邮箱和验证码
    validateCaptcha() {
      this.isEmailValid = this.email && this.email.length > 0;
    }
  }
};
</script>

<style scoped>


.app-nav-bar {
  text-align: center;
  margin-top: 10vh;
  color: white;
  height: 200px;
}

.welcome h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
  margin-top: -50px;
}

.welcome p {
  font-size: 1.5rem;
  color: #c8c6c6;
  font-family: 'Papyrus', cursive;
}

.tabs {
  display: flex;
  justify-content: center;
  margin-top: 2rem;
  gap: 1rem;
}

.tab {
  background: transparent;
  border: 2px solid white;
  color: white;
  padding: 0.5rem 1rem;
  margin: 0 0.5rem;
  width: 130px;
  height: 40px;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.3s;
}

.tab:hover,
.tab.active {
  background: white;
  color: #333;
}

.nav-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 80px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  z-index: 100;
}

header {
  background: linear-gradient(135deg, #4e7d9b, #132234);
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  padding: 1rem 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
}

.logo {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

.logo-img {
  height: 40px; /* 根据需要调整大小 */
  width: auto;
  /* 如果需要在hover时添加效果 */
  transition: transform 0.3s ease;
}


.search-login {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  margin-left: auto;
}

.search-wrapper {
  position: relative;
}

.search-bar {
  padding: 0.5rem 2.5rem 0.5rem 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  width: 150px;
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.search-bar:focus {
  outline: none;
  border-color: #4CAF50;
  box-shadow: 0 0 0 2px rgba(76, 175, 80, 0.1);
}

.search-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
  cursor: pointer;
}

.avatar-container {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s ease;
  border: 2px solid transparent;
}

.avatar-container:hover {
  transform: scale(1.05);
  border-color: #2C7A7B;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}


.navbar {
  background-color: rgb(143, 188, 160);
  padding: 0.7rem 15rem;
  border-bottom: 1px solid #e0e0e0;
}

.nav-links {
  display: flex;
  justify-content: center;
  gap: 6rem;
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-links a {
  color: #666;
  text-decoration: none;
  font-size: 1.1rem;
  font-weight: 550;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.nav-links a:hover {
  color: #4CAF50;
  background-color: #f0f0f0;
}

.resource-menu {
  position: relative;
}

.menu-wrapper {
  position: relative;
  padding: 0.5rem 0;
}

.resource-link {
  color: #666;
  text-decoration: none;
  font-size: 1.1rem;
  font-weight: 550;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.resource-link:hover {
  color: #4CAF50;
}

.dropdown {
  position: absolute;
  top: 100%;
  left: 0;
  background-color: white;
  min-width: 100px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
  border-radius: 4px;
  /* 移除 margin-top，改用 padding-top 消除间隙 */
  padding-top: 0.1rem;
  z-index: 1000;
  display: flex;
  flex-direction: column;
}

.dropdown::before {
  content: '';
  position: absolute;
  top: -0.5px; /* 延伸覆盖区域 */
  left: 0;
  right: 0;
  height: 0.5px;
  background: transparent; /* 保持透明 */
}

.dropdown a {
  padding: 0.55rem 1rem;
  color: #666;
  text-decoration: none;
  transition: all 0.3s ease;
  display: block;
  white-space: nowrap;
}

.dropdown a:hover {
  background-color: #f0f0f0;
  color: #4CAF50;
}

/* 登录框的样式 */
.login-modal {
  position: fixed;
  top: 10px;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5); /* 背景遮罩 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 10000;
}

.login-modal-content {
  position: relative; /* 保证伪元素可以相对于此元素定位 */
  padding: 30px;  /* 增加内边距 */
  border-radius: 10px;  /* 增加圆角效果 */
  width: 500px;  /* 增大宽度 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);  /* 增大阴影 */
  /* 移除背景颜色，让背景图片透过伪元素显示 */
}

.login-modal-content::before {
  content: ''; /* 必须设置content属性 */
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('@/assets/imgs/background/6.png'),
  #e0e0e0; /* 第二个参数是背景颜色，最后一个数字是透明度 */
  background-position:right bottom;
  background-size: 50%;
  background-blend-mode: multiply; /* 可选：混合模式可以使颜色和图片更好地融合 */
  z-index: -1; /* 确保背景图片和颜色位于内容之下 */
  border-radius: 10px;  /* 确保伪元素也有圆角效果 */
}


input {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

/* 右上角的关闭按钮 */
.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 32px;
  cursor: pointer;
  color: #333;
  background: none;
  border: none;
  padding: 0;
  line-height: 1;
  transition: color 0.3s;
}

.close-btn:hover {
    color: #00008B; /* 鼠标悬停时改变颜色 */
    transform: scale(1.2);
    opacity: 0.8;
}


/* 包裹按钮的父元素 */
.button-group {
  display: flex;  /* 使用 flexbox 布局 */
  justify-content: center;  /* 在水平方向上平均分配按钮 */
  gap: 10px;  /* 按钮之间的间隔，可以根据需要调整 */
}

/* 按钮样式 */
.btn {
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: #87CEEB;
  transition: background-color 0.3s;
  margin-bottom: -5px;
  margin-top: 10px;
}

.btn:hover {
    background-color: #c8e4ff;
    color: #0056b3;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}


/* 登录框中的标签和输入框样式 */
.login-tabs {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;     /* 垂直居中 */
  font-size: 18px;         /* 字体大小 */
  color: #333;             /* 默认字体颜色 */
  gap: 0; /* 去除子元素之间的间隙 */
  margin-bottom: 20px;
}

.login-tabs span {
  cursor: pointer;
  padding: 0 10px;
  transition: color 0.3s;
}

.login-tabs span.active {
  font-weight: bold;
  color: #007BFF; /* 激活状态字体颜色 */
}

.login-tabs span:hover {
  color: #0056b3; /* 鼠标悬停字体颜色 */
}


.login-form {
  display: flex;
  flex-direction: column; /* 让每个字段垂直排列 */
  width: 100%;
}

.form-group {
  display: flex; /* 使用 flex 布局 */
  flex-direction: row; /* 标签和输入框横向排列 */
  align-items: center; /* 垂直居中 */
  height: 40px;
  width: 80%; /* 确保有足够宽度 */
  border: 1px solid #ccc; /* 整体框的边框 */
  border-radius: 4px; /* 整体框的圆角 */
  background-color: #f5f5f5; /* 背景色 */
  margin: 0 auto 10px auto; /* 居中并设置底部间距 */
}


.input-label {
  width: 20%; /* 标签占 20% 宽度 */
  padding: 0 12px; /* 控制标签内边距 */
  font-size: 14px; /* 标签字体大小 */
  display: flex; /* 使用 flex 布局 */
  justify-content: center; /* 标签内容水平居中 */
  align-items: center; /* 标签内容垂直居中 */
  color: black;
}

.input-field {
  width: 80%; /* 输入框占 80% 宽度 */
  height: 100%; /* 确保输入框高度和 .form-group 一致 */
  font-size: 14px;
  border: none; /* 移除输入框的边框 */
  background-color: #f5f5f5; /* 背景色透明 */
  border-radius: 0; /* 让输入框的边框和整体框拼接 */
  margin-top: auto;
  /* 确保文本垂直居中 */
  display: flex;
  align-items: center; /* 让文本垂直居中 */
  justify-content: flex-start; /* 文本从左侧开始 */
  outline: none; /* 移除焦点时的外边框 */
}

/* 让验证码区域有独立的布局 */
.captcha-group {
  display: flex; /* 使用 flex 布局 */
  flex-direction: row; /* 标签和输入框横向排列 */
  align-items: center; /* 垂直居中 */
  height: 40px;
  width: 80%; /* 确保有足够宽度 */
  border: 1px solid #ccc; /* 整体框的边框 */
  border-radius: 4px; /* 整体框的圆角 */
  background-color: #f5f5f5; /* 背景色 */
  margin: 0 auto 10px auto; /* 居中并设置底部间距 */
}


/* 验证码输入框标签样式，仿照密码输入框的标签 */
.captcha-input-label {
  width: 20%;
  padding: 0 12px; /* 控制标签内边距 */
  font-size: 14px; /* 标签字体大小 */
  display: flex; /* 使用 flex 布局 */
  justify-content: center; /* 标签内容水平居中 */
  align-items: center; /* 标签内容垂直居中 */
  color: black;
}

/* 验证码输入框样式 */
.captcha-input {
  width: 50%; /* 输入框占 80% 宽度 */
  height: 100%; /* 确保输入框高度和 .form-group 一致 */
  font-size: 14px;
  border: none; /* 移除输入框的边框 */
  background-color: transparent; /* 背景色透明 */
  border-radius: 0; /* 让输入框的边框和整体框拼接 */
  margin-top: auto;
  /* 确保文本垂直居中 */
  display: flex;
  align-items: center; /* 让文本垂直居中 */
  justify-content: flex-start; /* 文本从左侧开始 */
  outline: none; /* 移除焦点时的外边框 */
  -moz-appearance: textfield; /* 去除 Firefox 默认的增减符号 */
  -webkit-appearance: none; /* 去除 WebKit 默认的增减符号 */
}

/* 去除数字输入框增减符号 */
.captcha-input::-webkit-outer-spin-button,
.captcha-input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

/* 发送验证码按钮样式 */
.captcha-btn {
  width: 30%;
  display: inline-block;
  padding: 6px 12px;
  cursor: pointer;
  font-size: 14px;
  border: none;
  background-color: transparent;
  transition: color 0.3s ease;
  color: #1e9be3;
}

/* 禁用状态下的按钮样式 */
.captcha-btn:disabled {
  cursor: not-allowed;  /* 禁用时鼠标显示禁止符号 */
  color: #ccc;  /* 禁用时的文字颜色 */
  text-decoration: none;  /* 禁用时移除下划线 */
}
.agreement-text {
  font-size: 12px;
  color: #888;
  text-align: center;
  margin-top: 20px;
  margin-bottom: -15px;
}

.agreement-text a {
  color: #007bff;
  text-decoration: none;
}

.agreement-text a:hover {
  text-decoration: underline;
}
.user-avatar-container {
  background: white;
  position: relative;
  display: inline-block;
  text-align: center; /* 确保头像容器内的内容水平居中 */
  z-index: 10;
  box-shadow: 0 0 0 3px hsl(124, 44%, 93%); /* 模拟圆形边框 */
  transform: translateX(800px);
  top: -65px;
}

.user-avatar-img {
  width: 50px;
  height: 50px;
  border-radius: 50%; /* 保持圆形 */
  transition: transform 0.3s ease;
  position: relative;
  z-index: 11;
  box-shadow: 0 0 0 3px hsl(124, 44%, 93%); /* 模拟圆形边框 */
}

.user-avatar-img:hover {
  transform: scale(1.2); /* 鼠标悬停时可以做放大效果 */
}


.dropdown-menu {
  position: absolute;
  top: 45px; /* 头像高度 + 一些间距 */
  left: 50%; /* 水平居中 */
  transform: translateX(-50%); /* 调整位置使其完全居中 */
  background-color: rgb(255, 255, 255);
  width: auto; /* 自适应宽度 */
  min-width: 150px; /* 设置最小宽度 */
  max-width: 300px; /* 限制最大宽度 */
  box-shadow: 0 1px 4px rgba(234, 74, 74, 0.1);
  border-radius: 4px;
  padding-top: 0.1rem;
  display: flex;
  flex-direction: column;
  align-items: center; /* 确保内容居中 */
  z-index: 10; /* 确保下拉菜单在头像之下 */
}


.dropdown-menu .user-info {
  margin-bottom: 10px;
  text-align: center; /* 确保用户名等文本居中 */
}

.dropdown-menu .username {
  font-size: 15px;
  margin-top: 20px;
  font-weight: bold;
}
.dropdown-menu .button-group {
  display: flex;
  flex-direction: column;
  gap: 10px; /* 增加按钮之间的间距 */
  width: 100%; /* 确保按钮组占满整个宽度 */
}

.dropdown-menu .button {
  display: flex;
  align-items: center; /* 图标和文字垂直居中 */
  justify-content: flex-start; /* 图标和文字靠左对齐 */
  gap: 10px; /* 图标和文字之间的水平间距 */
  padding: 5px 10px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  border-radius: 4px;
  text-align: left; /* 确保内容靠左对齐 */
}

.dropdown-menu .button:hover {
  background-color: #f0f0f0;
}

.dropdown-menu .button:last-child {
  border-bottom: none;
}

.dropdown-menu .button img {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.dropdown-menu .button span {
  font-size: 15px;
}

.icon {
  width: 20px;
  height: 20px;
}

</style>
