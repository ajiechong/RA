<template>
  <div class="background">
    <div class="doctor-detail-wrapper" v-if="doctor">
      <div class="doctor-info">
        <div class="top-section">
          <div class="image-and-text">
            <img :src="require(`@/assets/imgs/${doctor.docImage}`)"  alt="医生照片" class="doctor-photo" />
            <div class="text-details">
              <div class="doctor-info-text">
                <div class="doctor-name-and-title">
                  <div class="name-and-title-wrapper">
                    <div class="doctor-name">{{ doctor.docName }}</div>
                    <div class="doctor-job-title" style="font-size: 18px; color: #777; margin-left: 10px;">{{ doctor.title }}</div>
                  </div>
                </div>
                <div class="doctor-details">
                  <div style="margin-top: 20px;">
                    <strong>所在医院：</strong>{{ doctor.docHospital }}
                  </div>
                  <div style="margin-top: 20px;">
                    <strong>专业特长：</strong><span class="tag">{{ doctor.docSpecialties }}</span>
                  </div>
                  <div style="margin-top: 20px; width: 700px">
                    <strong>擅长：</strong>{{ doctor.spec_symptom }}
                  </div>
                  <div style="width: 700px; margin-top: 25px; border-bottom: 2px dashed #006400;"></div>
                  <p style="margin-top: 20px;">如果你想了解该医生的更多信息（包括学习经历、患者评价），请点击右方链接：
                    <a :href="doctor.hosUrl" class="learn-more" target="_blank">了解更多</a>
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="bottom-section">
          <div class="introduction-box">
            <div class="introduction-title">个人简介</div>
            <p>{{ doctor.docProfile }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script>
import axios from 'axios';
import { API_BASE_URL } from '@/utils/api';

export default {
  name: "Doctor_Detail",
  data() {
    return {
      doctor: null, // 初始为空，数据获取后赋值
    };
  },
  mounted() {
    // 从URL获取doctor_id参数
    const urlParams = new URLSearchParams(window.location.search);
    const docId = urlParams.get('doc_id');

    if (docId) {
      this.getDoctorDetail(docId);
    }
  },
  methods: {
    getDoctorDetail(docId) {
      // 发送请求获取医生数据
      axios.get(`${API_BASE_URL}/api/doc_information/${docId}`)
          .then(response => {
            if (response.data) {
              this.doctor = response.data; // 将后端返回的数据赋值给 doctor
            }
          })
          .catch(error => {
            console.error('获取医生信息失败', error);
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
  padding-top: 25px;
}


.doctor-detail-wrapper {
  background-color: #f9f9f9;
  padding: 60px 30px;
  margin-top: 120px;
  border-radius: 10px;
  margin-left: 400px;
  max-width: 1150px;
}

.top-section {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
}

.image-and-text {
  display: flex;
  align-items: flex-start;
}

.doctor-photo {
  flex: 0 0 300px; /* 固定宽度为 130px */
  height: 380px;   /* 固定高度为 130px，保证宽高一致 */
  margin-right: 40px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.1);
  object-fit: cover; /* 保证图片填充整个容器 */
}


.text-details {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 130px;
  margin-left: 50px;
}

.doctor-info-text {
  font-size: 16px;
}

.name-and-title-wrapper {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
}

.doctor-name {
  font-size: 28px;
  font-weight: bold;
}

.doctor-job-title {
  font-size: 18px;
  color: #777;
}

.tag {
  display: inline-block;
  padding: 5px 10px;
  background-color: #1a365d;
  color: white;
  border-radius: 15px;
}

.doctor-details strong {
  font-weight: bold;
}

.bottom-section {
  margin-top: 100px;
}

.introduction-box {
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 15px;
  width: 1090px;
}

.introduction-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 8px;

}

.introduction-box p {
  font-size: 20px;
  line-height: 2;

}

.learn-more {
  color: #1a365d;
  text-decoration: underline;
  margin-bottom: 10px;
}
</style>
