<!-- DoctorCard.vue -->
<template>
  <a :href="doctorUrl" target="_blank" class="doctor-card-link">
    <div class="doctor-card">
      <img :src="image" :alt="name" class="doctor-avatar">
      <div class="doctor-info">
        <h3>{{ name }}</h3>
        <h3>{{ hospital }}</h3>
        <p>{{ description }}</p>
      </div>
    </div>
  </a>
</template>

<script>
export default {
  name: "DoctorCard",
  props: {
    image: {
      type: String,
      required: true
    },
    name: {
      type: String,
      required: true
    },
    hospital: {
      type: String,
      required: true
    },
    description: {
      type: String,
      required: true
    },
    doctorUrl: {
      type: String,
      required: true
    }
  }
}
</script>

<style scoped>
.doctor-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: white;
  border-radius: 8px;
}

.doctor-avatar {
  width: 120px;
  height: 120px;
  border-radius: 4px;
  object-fit: cover;
}

.doctor-info {
  flex: 1;
}

.doctor-info h3 {
  color: #333;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.doctor-info p {
  color: #666;
  font-size: 0.9rem;
  margin: 0;
}
</style>