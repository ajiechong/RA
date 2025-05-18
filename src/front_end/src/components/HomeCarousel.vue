<!-- HomeCarousel.vue -->
<template>
  <div class="carousel">
    <div class="carousel-inner" :style="{ transform: `translateX(-${currentSlide * 100}%)` }">
      <div
          v-for="item in items"
          :key="item.id"
          class="carousel-item"
      >
        <a :href="item.url" target="_blank"> <!-- 添加a标签并设置href -->
          <div class="carousel-content">
            <img :src="item.image" :alt="item.title">
            <div class="carousel-caption">
              <span class="category">{{ item.category }}</span>
              <h2>{{ item.title }}</h2>
              <p>{{ item.author }}</p>
            </div>
          </div>
        </a>
      </div>
    </div>

    <button class="carousel-control prev" @click="prevSlide">&lt;</button>
    <button class="carousel-control next" @click="nextSlide">&gt;</button>

    <div class="carousel-indicators">
      <button
          v-for="(_, index) in items"
          :key="index"
          :class="{ active: currentSlide === index }"
          @click="goToSlide(index)"
      ></button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomeCarousel',
  props: {
    items: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      currentSlide: 0,
      intervalId: null
    }
  },
  methods: {
    nextSlide() {
      this.currentSlide = (this.currentSlide + 1) % this.items.length;
    },
    prevSlide() {
      this.currentSlide = (this.currentSlide - 1 + this.items.length) % this.items.length;
    },
    goToSlide(index) {
      this.currentSlide = index;
    },
    startAutoPlay() {
      this.intervalId = setInterval(this.nextSlide, 5000);
    },
    stopAutoPlay() {
      clearInterval(this.intervalId);
    }
  },
  mounted() {
    this.startAutoPlay();
  },
  beforeUnmount() { // 修改为 beforeUnmount
    this.stopAutoPlay();
  }
}
</script>

<style scoped>
.carousel {
  position: relative;
  width: 100%;
  height: 500px;
  overflow: hidden;
  border-radius: 8px;
}

.carousel-inner {
  display: flex;
  transition: transform 0.5s ease-in-out;
  height: 100%;
}

.carousel-item {
  flex: 0 0 100%;
  position: relative;
}

.carousel-content {
  position: relative;
  width: 100%;
  height: 100%;
}

.carousel-content img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.carousel-caption {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 2rem;
  background: linear-gradient(transparent, rgba(0,0,0,0.8));
  color: white;
}

.category {
  display: inline-block;
  background-color: #1B4D62;
  padding: 0.25rem 0.75rem;
  border-radius: 4px;
  margin-bottom: 0.5rem;
  font-size: 0.875rem;
}

.carousel-caption h2 {
  margin: 0.5rem 0;
  font-size: 1.75rem;
  font-family: 'Microsoft YaHei', sans-serif;

}

.carousel-control {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(255,255,255,0.3);
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  cursor: pointer;
  color: white;
  font-size: 1.5rem;
}

.carousel-control.prev {
  left: 1rem;
}

.carousel-control.next {
  right: 1rem;
}

.carousel-indicators {
  position: absolute;
  bottom: 1rem;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 0.5rem;
}

.carousel-indicators button {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  border: none;
  background: rgba(255,255,255,0.5);
  cursor: pointer;
}

.carousel-indicators button.active {
  background: white;
}
</style>