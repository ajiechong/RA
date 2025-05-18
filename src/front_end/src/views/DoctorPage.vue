<template>
  <div class="background">
    <div class="home-container">
      <!-- 错误信息显示 -->
      <div v-if="isLoading" class="loading-message">加载中...</div>
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

      <div class="caption">
        <span class="s1">医师介绍</span> <span class="s2">共{{ doctors.length }}名医生</span>
      </div>
      <section class="doctors">
        <div class="doctors-content">
          <div class="doctor-grid">
            <!-- 渲染医生信息 -->
            <div class="item" v-for="doctor in doctors" :key="doctor.docId">
              <!-- 使用 router-link 来实现跳转并传递 doc_id -->
              <router-link :to="`/doctor_detail?doc_id=${doctor.docId}`">
                <div class="doctor-avatar-wrapper">
                  <img :src="require(`@/assets/imgs/${doctor.docImage}`)" alt="医生头像" class="doctor-avatar" />

                  <div class="info">
                    <div class="inline">
                      <div class="h2">{{ doctor.docName }}</div>
                      <div class="p">{{ doctor.title }}</div>
                    </div>
                  </div>
                </div>
              </router-link>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>

</template>


<script>
import axios from 'axios';
import { API_BASE_URL } from '@/utils/api';

export default {
  name: 'DoctorPage',
  data() {
    return {
      doctors: [],           // 存储医生数据
      isLoading: true,       // 加载状态
      errorMessage: '',      // 错误信息
    };
  },
  mounted() {
    this.loadDoctors();     // 组件挂载后加载医生数据
  },
  methods: {
    // 方法：加载医生数据
    loadDoctors() {
      axios
          .get(`${API_BASE_URL}/api/doc_information/all`)  // 获取所有医生信息的接口
          .then((response) => {
            console.log('API 返回的数据:', response.data);  // 打印返回的数据
            this.doctors = response.data;    // 更新医生数据
            console.log('加载的医生数据:', this.doctors);  // 打印医生数据
            this.isLoading = false;           // 数据加载完成
          })
          .catch((error) => {
            console.error('获取医生数据出错：', error);
            this.errorMessage = '无法加载医生数据，请稍后重试。'; // 设置错误信息
            this.isLoading = false;  // 错误时更新加载状态
          });
    }
  }
};
</script>

<style scoped>

.background{
  background-image: url('@/assets/imgs/background4.jpg');
  background-size: cover;
  background-position: center;
  min-height: 1000px;
  height: 1050px;
  padding-top: 15px;
}


.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  margin-top: 100px;
}

.caption {
  font-size: 1.5rem;
  margin-bottom: 2rem;
}

.s1 {
  font-weight: bold;
}

.s2 {
  font-size: 0.9rem;
  color: #666;
}

.doctor-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
}

.item {
  position: relative;
  overflow: hidden;
  /* 去掉这里的 border-radius */
  height: 350px;
}

.doctor-avatar-wrapper {
  width: 100%;
  aspect-ratio: 247 / 347;
  overflow: hidden;
  position: relative;
  border-radius: 0; /* 确保容器没有圆角 */
}

.doctor-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 0; /* 确保头像没有圆角 */
}



.info {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  padding: 10px;
  display: flex;
  flex-direction: column;
  justify-content: center;  /* 垂直居中 */
  align-items: center;      /* 水平居中 */
  height: 30%;
}

.inline {
  text-align: center;
  flex: 1;
  display: flex;
  flex-direction: column;

}


.h2 {
  font-weight: bold;
  margin: 5px;
  font-size: 1.2rem;
  line-height: 1.5;
}

.p {
  font-size: 1rem;
  margin: 5px 0 0;
  line-height: 1.5;
}

@media (max-width: 1024px) {
  .doctor-grid {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .doctor-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .doctor-grid {
    grid-template-columns: 1fr;
  }
}
</style>
