<template>
  <div class="profile-container">

    <!-- 加载覆盖层 -->
    <div v-if="isLoading" class="loading-overlay">
      <p>加载中...</p>
    </div>

    <!-- 头像部分 -->
    <div class="avatar-container">
      <div
          class="avatar-wrapper"
          @mouseover="isHover = true"
          @mouseleave="isHover = false"
      >
        <div class="avatar-frame">
          <img
              :src="previewAvatarUrl ? previewAvatarUrl : avatar_url"
              alt="头像"
              class="avatar"
              @error="handleImageError"
              @load="() => console.log('Image loaded successfully')"
          />
        </div>

        <div v-if="isHover" class="avatar-mask"></div>
        <button
            v-if="isHover"
            @click="triggerFileInput"
            class="change-avatar-btn"
        >
          更换头像
        </button>

        <input
            type="file"
            ref="fileInput"
            style="display: none;"
            @change="onFileChange"
            accept="image/*"
        />
      </div>
    </div>

    <!-- 用户信息部分 -->
    <div class="profile-item">
      <label for="user-id">用户ID:</label>
      <input id="user-id" v-model="id" disabled />
    </div>

    <div class="profile-item">
      <label for="user-email">邮箱:</label>
      <input id="user-email" v-model="email" />
    </div>

    <div class="profile-item">
      <label for="user-name">用户名:</label>
      <input
          id="user-name"
          v-model="username"
          :disabled="isUsernameLocked"
          placeholder="请输入用户名"
      />
    </div>

    <div class="save-item">
      <button @click="savechange" class="save-btn">保存</button>
      <button @click="cancelchange" class="cancel-btn">取消</button>
    </div>

    <!-- 邮箱验证模态窗口 -->
    <div v-if="showEmailVerification" class="email-verification-modal">
      <div class="modal-content">
        <span class="close" @click="cancelEmailVerification">&times;</span>
        <h2>邮箱验证</h2>

        <!-- 原邮箱信息 -->
        <div class="form-group d-flex align-items-center">
          <label for="original-email" class="mr-2">原邮箱:</label>
          <input
              id="original-email"
              v-model="originalEmail"
              type="email"
              class="form-control flex-grow-1 mr-2"
              placeholder="请输入原邮箱"
              :disabled="isOriginalEmailVerified"
          />
        </div>

        <!-- 原邮箱验证码 -->
        <div v-if="!isOriginalEmailVerified" class="form-group d-flex align-items-center">
          <label for="verification-code" class="mr-2">验证码:</label>
          <input id="verification-code" v-model="verificationCode" type="text" class="form-control flex-grow-1" placeholder="请输入验证码" />
          <button
              @click="sendVerificationCode('original')"
              :class="{
        'btn': true,
        'btn-primary': !isCodeSent && countdown <= 0, // 按钮未发送验证码时使用蓝色
        'btn-secondary': isCodeSent || countdown > 0 // 按钮发送验证码后使用灰色
      }"
              :disabled="isCodeSent || countdown > 0">
            {{ countdown > 0 ? countdown + '秒后重试' : (isCodeSent ? '验证码已发送' : '发送验证码') }}
          </button>
        </div>


        <!-- 新邮箱信息 -->
        <div v-if="isOriginalEmailVerified" class="form-group d-flex align-items-center">
          <label for="new-email" class="mr-2">新邮箱:</label>
          <input id="new-email" v-model="email" type="email" class="form-control flex-grow-1 mr-2" placeholder="请输入新邮箱" />
        </div>

        <!-- 新邮箱验证码 -->
        <div v-if="isOriginalEmailVerified" class="form-group d-flex align-items-center">
          <label for="new-verification-code" class="mr-2">验证码:</label>
          <input id="new-verification-code" v-model="newVerificationCode" type="text" class="form-control flex-grow-1" placeholder="请输入验证码" />
          <button
              @click="sendVerificationCode('new')"
              :class="{
        'btn': true,
         'btn-primary': !isCodeSent && countdown <= 0, // 按钮未发送验证码时使用灰色
        'btn-secondary': isCodeSent || countdown > 0 // 按钮发送验证码后使用灰色
      }"
              :disabled="isCodeSent || countdown > 0">
            {{ countdown > 0 ? countdown + '秒后重试' : (isCodeSent ? '验证码已发送' : '发送验证码') }}
          </button>
        </div>

        <div class="save-item">
          <button @click="verifyCode" class="save-btn">保存</button>
          <button @click="cancelEmailVerification" class="cancel-btn">取消</button>
        </div>
      </div>
      <div class="modal-background"></div>
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import { mapState, mapActions } from 'vuex';
import { API_BASE_URL } from '@/utils/api';

export default {
  name: 'Profile_Info',
  data() {
    return {
      isUsernameLocked: false,
      isHover: false,
      id: '',
      email: '',
      username: '',
      avatar_url: '', // 存储用户头像 URL
      isLoading: false,
      errorMessage: '',
      avatarFile: null, // 保存上传的头像文件
      showEmailVerification: false,
      originalEmail: '',
      verificationCode: '',
      newVerificationCode: '',  // 新邮箱验证码字段
      isCodeSent:false,
      isOriginalEmailVerified: false, // 记录原邮箱验证码是否通过验证
      countdown: 0,
      timer: null,
      orgin_username:'',
      orgin_avatar_url:'',
      previewAvatarUrl: '', // 用于本地预览的临时URL

    };
  },

  computed: {
    ...mapState({
      isLoggedIn: (state) => state.isLoggedIn,
      username: (state) => state.username,
      storeAvatarUrl: (state) => state.avatar_url, // 从 store 中获取头像 URL
    }),
  },



  methods: {
    ...mapActions(['updateUserInfo']),

    // 获取用户信息
    async getUserInfo() {
      const uid = this.$store.getters.uid;
      if (!uid) {
        this.errorMessage = '无法获取用户信息，uid 未定义。';
        return;
      }

      const url = `${API_BASE_URL}/api/login/profile`;
      this.isLoading = true;

      try {
        const response = await axios.get(url, { params: { identifier: uid } });

        if (response.status === 200 && response.data) {
          this.id = response.data.uid;
          this.originalEmail = response.data.email;
          this.email = this.originalEmail;
          this.username = response.data.uname;
          this.orgin_username = this.username;

          // 保持使用 Vuex store 中的头像 URL
          const storeAvatarUrl = this.$store.state.avatar_url;
          if (storeAvatarUrl) {
            this.avatar_url = storeAvatarUrl;
            this.orgin_avatar_url = storeAvatarUrl;
          } else {
            // 如果 store 中没有，则使用接口返回的
            this.avatar_url = response.data.avatarUser;
            this.orgin_avatar_url = response.data.avatarUser;
          }
        }
      } catch (error) {
        this.errorMessage = error.response?.data?.message || '网络错误，无法获取用户信息。';
      } finally {
        this.isLoading = false;
      }
    },


    // 文件选择后的处理

    onFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.avatarFile = file;
        // 创建本地预览URL
        const reader = new FileReader();
        reader.onload = (e) => {
          const previewUrl = e.target.result;
          this.avatar_url = previewUrl; // 更新本地预览
        };
        reader.readAsDataURL(file);
      }
    },

    // 保存修改
    savechange() {
      // 防止重复提交
      if (!this.isOriginalEmailVerified && this.email !== this.originalEmail) {
        // 检查邮箱是否已注册
        axios.post(`${API_BASE_URL}/api/login/verify-email`, null, {params: {newemail: this.email}})
            .then(response => {
              if (!response.data.success) {
                alert('邮箱已经注册过！');
                return;
              }
              this.showEmailVerification = true; // 显示邮箱验证提示框
            })
            .catch(error => {
              console.error('邮箱验证请求失败', error);
            });
        return;
      }
      this.savechangeothers();
    },



    async savechangeothers() {
      this.isLoading = true;
      const formData = new FormData();
      formData.append('uid', this.id);
      formData.append('newUserName', this.username);

      // 如果上传了头像文件，则先上传头像
      if (this.avatarFile) {
        const avatarFormData = new FormData();
        avatarFormData.append('uid', this.id);
        avatarFormData.append('avatarFile', this.avatarFile);

        try {
          const avatarResponse = await axios.post(`${API_BASE_URL}/api/login/upload-avatar`, avatarFormData);
          if (avatarResponse.status === 200 && avatarResponse.data.fileUrl) {
            formData.append('newAvatarUrl', avatarResponse.data.fileUrl);
          }
        } catch (error) {
          console.error('头像上传失败:', error);
          this.errorMessage = '头像上传失败，请稍后再试。';
          this.isLoading = false;
          return;
        }
      }

      // 上传其他用户信息
      try {
        const updateResponse = await axios.post(`${API_BASE_URL}/api/login/update`, formData);
        if (updateResponse.status === 200) {
          // 上传成功后，才去更新全局 store
          const newAvatarUrl = updateResponse.data.avatarUrl || this.avatar_url;
          this.$store.commit('UPDATE_USER_INFO', {
            uid: this.id,
            username: this.username,
            avatar_url: newAvatarUrl,
          });
          // 本地也更新
          this.avatar_url = newAvatarUrl;
          this.orgin_avatar_url = newAvatarUrl;
          // 清空临时预览
          this.previewAvatarUrl = null;
          // 提示成功
          alert('用户信息更新成功');
        }
      } catch (error) {
        console.error('用户信息更新失败:', error);
        this.errorMessage = '用户信息更新失败，请稍后再试。';
      } finally {
        this.isLoading = false;
        this.isOriginalEmailVerified = false;
      }
    },

    async updateUserInfo(formData) {
      try {
        const updateResponse = await axios.post(`${API_BASE_URL}/api/login/update`, formData);

        if (updateResponse.status === 200) {
          const data = updateResponse.data;

          // 如果头像更新成功，使用新的头像URL，否则保持原有URL
          const newAvatarUrl = data.avatarUrl || this.avatar_url;

          // 更新 Vuex store，保持头像URL的格式一致
          await this.$store.commit('UPDATE_USER_INFO', {
            uid: this.id,
            username: this.username,
            avatar_url: newAvatarUrl
          });

          // 更新本地状态
          this.avatar_url = newAvatarUrl;
          this.orgin_avatar_url = newAvatarUrl;

          alert('用户信息更新成功');
        } else {
          this.errorMessage = '用户信息更新失败，请稍后再试。';
        }
      } catch (error) {
        console.error('用户信息更新失败:', error);
        this.errorMessage = '用户信息更新失败，请稍后再试。';
      } finally {
        this.isLoading = false;
        this.isOriginalEmailVerified = false;
      }
    },

    // 取消修改
    cancelchange() {
      // 恢复原先的用户名
      this.username = this.orgin_username;
      // 恢复原先的头像
      this.avatar_url = this.orgin_avatar_url;
      // 清空本地预览
      this.previewAvatarUrl = null;
      // 清空文件
      this.$refs.fileInput.value = '';
    },

    // 发送验证码
    async sendVerificationCode(type) {
      if (this.isCodeSent) return; // 防止重复发送验证码
      this.isCodeSent = true; // 标记验证码已发送
      this.startTimer(); // 启动倒计时

      if (this.isOriginalEmailVerified) {
        try {
          const response = await axios.post(`${API_BASE_URL}/api/login/verify-email`, null, { params: { newemail: this.email } });
          if (!response.data.success) {
            alert('邮箱已经注册过！');
            this.resetCodeSendingState();
            return;
          }
        } catch (error) {
          console.error('邮箱验证请求失败', error);
          alert('邮箱验证请求失败，请稍后重试');
          this.resetCodeSendingState();
          return;
        }
      }

      const targetEmail = type === 'original' ? this.originalEmail : this.email;
      try {
        await axios.post(`${API_BASE_URL}/api/login/register`, null, { params: { email: targetEmail } });
        console.log('验证码发送成功');
      } catch (error) {
        console.error('验证码发送失败:', error);
        alert('验证码发送失败，请稍后重试');
        this.resetCodeSendingState(); // 发送失败时重置状态
      }
    }
    ,

    // 启动倒计时
    startTimer() {
      this.countdown = 60;  // 初始倒计时 60 秒
      this.countdownText = `${this.countdown}秒后重试`;
      this.timer = setInterval(() => {
        if (this.countdown > 0) {
          this.countdown--;
          this.countdownText = `${this.countdown}秒后重试`;
        } else {
          this.resetCodeSendingState(); // 倒计时结束，重置状态
        }
      }, 1000);
    },

    // 重置验证码发送状态
    resetCodeSendingState() {
      clearInterval(this.timer); // 清除定时器
      this.isCodeSent = false;  // 允许重新发送验证码
      this.countdownText = '获取验证码';  // 恢复按钮文本
      this.countdown = 0;
    },


    // 验证验证码
    // 验证验证码
    async verifyCode() {
      const emailToVerify = this.isOriginalEmailVerified ? this.email : this.originalEmail;
      const codeToVerify = this.isOriginalEmailVerified ? this.newVerificationCode : this.verificationCode;

      try {
        const response = await axios.post(`${API_BASE_URL}/api/login/verify-code`, null, {
          params: { email: emailToVerify, code: codeToVerify },
        });

        if (response.data.success) {
          this.resetCodeSendingState();
          alert('验证成功');
          if (this.isOriginalEmailVerified) {
            this.showEmailVerification = false;
            // 邮箱修改后，更新信息并进行保存
            const url = `${API_BASE_URL}/api/login/change-email`;
            this.$store.dispatch('login', {
              email: response.data.email,
              uid: this.id,
              avatar_url: this.avatar_url,
              isLoggedIn: true
            });

            const formData = new URLSearchParams();
            formData.append('uid', this.id);
            formData.append('newemail', this.email);

            try {
              const emailResponse = await axios.post(url, formData, {
                headers: {
                  'Content-Type': 'application/x-www-form-urlencoded'
                }
              });
              if (emailResponse.data.success) {
                alert('邮箱修改成功');
              } else {
                alert('邮箱修改失败: ' + (emailResponse.data.message || '请稍后重试'));
              }
            } catch (error) {
              console.error('邮箱修改请求失败', error);
              alert('邮箱修改失败，请稍后再试');
            }
          }
          if(this.isOriginalEmailVerified)
            this.savechangeothers();  // 在邮箱验证成功后，保存其他用户信息
          this.isOriginalEmailVerified = true;
        } else {
          alert(response.data.message);  // 显示错误消息
        }
      } catch (error) {
        console.error('验证码请求失败', error);
        alert('验证失败，请稍后重试');
      }
    },


    // 取消邮箱验证
    cancelEmailVerification() {
      this.showEmailVerification = false;
      this.email=this.originalEmail;
    },



    triggerFileInput() {
      this.$refs.fileInput.click();
    },

    handleImageError() {
      console.log('Current avatar_url:', this.avatar_url);
      console.log('Store avatar_url:', this.$store.state.avatar_url);
    },
  },
  mounted() {
    this.getUserInfo();


  },
};
</script>

<style scoped>
/* 头像框样式 */
.avatar-frame {
  position: relative;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 3px solid #ddd;
  overflow: hidden;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 加载覆盖层样式 */
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.error-message {
  color: #f44336;
  text-align: center;
  margin-bottom: 20px;
}

.profile-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: transparent;
  border: none;
  box-shadow: none;
}

.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.avatar-wrapper {
  position: relative;
}

/* 更换头像按钮样式 */
.change-avatar-btn {
  position: absolute;
  top: 0;
  left: 0;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  border: 3px solid #ddd;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  font-weight: bold;
  z-index: 11;
  display: block;
  text-align: center;
  line-height: 120px;
  transition: all 0.3s ease;
}

.change-avatar-btn:hover {
  transform: scale(1.05); /* 鼠标悬停时按钮放大 */
}

/* 遮罩层样式 */
.avatar-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.4);
  z-index: 10;
  transition: background 0.3s ease;
}

.avatar-mask:hover {
  background: rgba(0, 0, 0, 0.6); /* 鼠标悬停时背景变深 */
}

/* 用户信息部分 */
.profile-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.profile-item label {
  font-size: 1em;
  font-weight: bold;
  color: #555;
  width: 120px;
  margin-right: 10px;
}

.profile-item input {
  width: 100%;
  max-width: 300px;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1em;
  color: #333;
}

.profile-item input:disabled {
  background-color: #f1f1f1;
  color: #888;
}

.save-item {
  margin-top: 50px;
  display: flex;
  justify-content: center; /* 水平居中 */
  gap: 10px;               /* 按钮之间的间距 */
}

.save-btn,
.cancel-btn {
  padding: 12px 20px;
  background-color: #218838;
  color: white;
  font-size: 1.2em;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s, box-shadow 0.2s;
  max-width: 200px; /* 限制最大宽度 */
  flex: 1;           /* 让每个按钮占据相等的空间 */
  text-align: center; /* 确保文本居中 */
}

.save-btn:hover,
.cancel-btn:hover {
  background-color: rgba(33, 136, 56, 0.9);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.save-btn:active,
.cancel-btn:active {
  transform: scale(0.98);
}

.email-verification-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  z-index: 1000;
  position: relative;
  width: 500px; /* 调整为更大的宽度 */
  max-width: 90%; /* 确保在小屏幕上也能适配 */
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.modal-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 999;
}

.form-group {
  margin-bottom: 20px;
  align-items: center; /* 垂直居中 */
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group p {
  margin-bottom: 10px;
}

.form-group .btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.form-group .btn-primary {
  background-color: #007bff;
  color: white;
}

.form-group .btn-secondary {
  background-color: #6c757d;
  color: white;
}

.form-group .btn-success {
  background-color: #28a745;
  color: white;
}

.form-group .btn-danger {
  background-color: #dc3545;
  color: white;
}

.form-group .btn:hover {
  opacity: 0.9;
}

.form-control {
  width: 40%;
  padding: 10px;
  border: 1px solid #ced4da;
  border-radius: 5px;
  margin-right: 20px;
}

.form-group.d-flex {
  display: flex;
  align-items: center;
}

.form-group .mr-2 {
  margin-right: 8px; /* 调整间距 */
}

.form-group .flex-grow-1 {
  flex-grow: 1;
}

.modal-content h2 {
  text-align: center;
  margin-bottom: 10px;
}
</style>
