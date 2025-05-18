<template>
  <div class="section">
    <div class="security-container" >
      <!-- 当前密码 -->
      <div class="profile-item">
        <label for="current-password">当前密码:</label>
        <el-input

            id="current-password"
            type="password"
            v-model="security.currentPassword"
            placeholder="请输入当前密码"
            show-password
            style="width: 300px;height:40px;"
        />
        <div v-if="currentPasswordError" class="error-message">
          {{ currentPasswordError }}
        </div>
      </div>

      <!-- 新密码 -->
      <div class="profile-item">
        <label for="new-password">新密码:</label>
        <el-input
            id="new-password"
            type="password"
            v-model="security.newPassword"
            placeholder="请输入新密码"
            show-password
            style="width: 300px;height:40px;"
        />
      </div>
      <!-- 新密码的错误提示 -->
      <div v-if="newPasswordError" class="error-message">
        {{ newPasswordError }}
      </div>

      <!-- 新密码的格式提示 -->
      <div v-if="security.newPassword && !isPasswordValid" class="hint-message">
        密码至少6个字符，包含字母、数字和特殊字符。
      </div>

      <!-- 确认密码 -->
      <div class="profile-item">
        <label for="confirm-password">确认密码:</label>
        <el-input
            id="confirm-password"
            type="password"
            v-model="security.confirmPassword"
            placeholder="请确认新密码"
            style="width: 300px;height:40px;"
            show-password
        />

      </div>
      <!-- 确认密码的错误提示 -->
      <div v-if="security.newPassword && confirmPasswordError" class="error-message">
        {{ confirmPasswordError }}
      </div>

      <!-- 修改密码按钮 -->
      <div class="profile-item">
        <button
            @click="changePassword"
            class="save-btn"
            :disabled="isLoading || !isFormValid"
        >
          修改密码
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { API_BASE_URL } from '@/utils/api';

export default {
  name: 'Security_Info',
  data() {
    return {
      security: {
        currentPassword: '',  // 当前密码
        newPassword: '',      // 新密码
        confirmPassword: '',  // 确认密码
        uid: '',              // 用户标识符（UID）
        originPassword: ''    // 存储原密码
      },
      isLoading: false,       // 加载状态
      currentPasswordError: '',  // 当前密码错误信息
      newPasswordError: '',      // 新密码错误信息
      confirmPasswordError: '',  // 确认密码错误信息
    };
  },

  computed: {
    isFormValid() {
      // 校验表单是否有效
      return (
          this.security.currentPassword &&
          this.security.newPassword &&
          this.security.confirmPassword &&
          this.security.newPassword === this.security.confirmPassword
      );
    },

    // 新密码的校验规则
    isPasswordValid() {
      const password = this.security.newPassword;
      return (
          password.length >= 6 &&
          /[a-zA-Z]/.test(password) &&
          /\d/.test(password) &&
          /[!@#$%^&*(),.?":{}|<>]/.test(password)
      );
    }
  },

  methods: {
    // 获取当前用户的UID，并初始化相关数据
    async getOrignalPassword() {
      this.security.uid = this.$store.getters.uid;

      // 检查 UID 是否存在
      if (!this.security.uid) {
        console.error('password: uid 未定义');
        this.errorMessage = '无法获取用户标识符。';
        return;
      }

      // 发送请求获取用户原始密码等信息
      const url = `${API_BASE_URL}/api/login/profile`;

      try {
        const response = await axios.get(url, {
          params: { identifier: this.security.uid }
        });

        if (response.status === 200 && response.data) {
          // 映射响应数据
          this.security.originPassword = response.data.userPSW;
        } else {
          console.error('获取用户信息失败');
          this.errorMessage = '获取用户信息失败，请稍后重试。';
        }
      } catch (error) {
        console.error('请求失败', error);
        this.errorMessage = '无法获取用户信息，请稍后再试。';
      }
    },

    // 修改密码操作
    async changePassword() {
      const { currentPassword, newPassword, confirmPassword, uid, originPassword } = this.security;

      console.log('User identifier:', uid);
      console.log('Request data:', {
        uid: uid,
        oldPassword: currentPassword,
        newPassword: newPassword
      });

      // 重置错误信息
      this.currentPasswordError = '';
      this.newPasswordError = '';
      this.confirmPasswordError = '';

      // 校验基本输入
      if (!currentPassword || !newPassword || !confirmPassword) {
        this.errorMessage = '请填写所有字段';
        return;
      }

      // 校验原密码是否正确
      if (currentPassword !== originPassword) {
        alert('当前密码不正确，请重新输入。');
        return;
      }
      // 校验新密码和当前密码是否相同
      if (newPassword === currentPassword) {
        alert( '新密码不能与当前密码相同，请重新输入。');
        return;
      }
      // 校验新密码和确认密码是否一致
      if (newPassword !== confirmPassword) {
        alert( '新密码和确认密码不匹配，请重新输入。');
        return;
      }

      // 确保 identifier 不为空
      if (!uid) {
        this.errorMessage = '用户ID不能为空';
        return;
      }

      // 向后端发送请求修改密码
      this.isLoading = true;
      const url = `${API_BASE_URL}/api/login/change-password`;

      // 使用 URLSearchParams 发送表单数据
      const formData = new URLSearchParams();
      formData.append('uid', uid);
      formData.append('oldPassword', currentPassword);
      formData.append('newPassword', newPassword);

      try {
        const response = await axios.post(url, formData, {
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        });

        console.log('Response:', response.data);

        if (response.data.success) {
          alert('密码修改成功');
          // 在组件中调用 LOGOUT action
          this.$store.dispatch('logout');
          this.$router.push('/');



        } else {
          alert('密码修改失败: ' + (response.data.message || '请稍后重试'));
        }
      } catch (error) {
        console.error('请求失败', error);
        alert('密码修改失败，请稍后再试');
      } finally {
        this.isLoading = false;
      }
    }
  },

  // 监听 newPassword 和 confirmPassword 的变化
  watch: {
    // 监听 newPassword 和 confirmPassword 的变化，确保只有当 newPassword 存在时才进行验证
    'security.newPassword': function() {
      if (this.security.newPassword && this.security.confirmPassword) {
        if (this.security.newPassword !== this.security.confirmPassword) {
          this.confirmPasswordError = '新密码和确认密码不匹配，请重新输入。';
        } else {
          this.confirmPasswordError = '';
        }
      } else {
        this.confirmPasswordError = ''; // 清除错误信息
      }
    },
    'security.confirmPassword': function() {
      if (this.security.newPassword && this.security.confirmPassword) {
        if (this.security.newPassword !== this.security.confirmPassword) {
          this.confirmPasswordError = '新密码和确认密码不匹配，请重新输入。';
        } else {
          this.confirmPasswordError = '';
        }
      } else {
        this.confirmPasswordError = ''; // 清除错误信息
      }
    }
  },

  mounted() {
    this.getOrignalPassword();
  }
};
</script>

<style scoped>
/* 样式保持不变 */
.section {
  max-width: 600px;
  margin: 0 auto;
  padding: 50px 20px;
  background-color: transparent;
  border: none;
  box-shadow: none;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  min-height: 80vh;

}


.profile-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  gap: 12px;
  margin-top: 50px;
}

.profile-item label {
  font-size: 1em;
  font-weight: bold;
  color: #555;
  width: 120px;  /* 设置标签宽度，确保与输入框对齐 */
}

.profile-item input {
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 1em;
  width: 100%;
  max-width: 300px;
  transition: border-color 0.3s;
}

.profile-item input:focus {
  border-color: #007bff;
  outline: none;
}

.save-btn {
  padding: 12px 20px;
  background-color: #218838;
  color: white;
  font-size: 1.2em;
  font-weight: bold;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.2s, box-shadow 0.2s;
  display: block;
  width: 100%;
  max-width: 200px;
  margin-left: 178px;
}

.save-btn:hover {
  background-color: rgba(33, 136, 56, 0.9);
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.save-btn:active {
  transform: scale(0.98);
}

.error-message {
  color: red;
  font-size: 0.9em;
  margin-top: 5px;
  margin-left: 140px; /* 错误信息与输入框对齐 */
  margin-bottom: 20px;
}

.hint-message {
  color: red;
  font-size: 0.85em;
  margin-top: 5px;
  margin-left: 140px; /* 提示信息与输入框对齐 */
  margin-bottom: 20px;
}
</style>
