<!-- src/components/RelatedArticleBox.vue -->
<template>
  <div class="related-article-box" @click="handleClick">
    <img :src="require(`@/${rimg}`)" alt="Related Article Image" class="related-article-image" @error="onImageError"/>
    <p class="related-article-title">{{ rname }}</p>
  </div>
</template>

<script>

export default {
  name: "RelatedArticleBox",
  props: {
    rimg: String,
    rname: String,
    rid: Number,
    rurl: String
  },
  methods: {
    handleClick() {
      // 当点击时，跳转到对应的文章详情页面
      if (this.rurl) {
        window.open(this.rurl, "_blank");  // 打开新窗口跳转
      } else {
        console.log("无链接的文章: ", this.rname);
      }
    },
    onImageError() {
      console.error('图片加载失败');
    }
  }
};
</script>

<style scoped>
.related-article-box {
  position: relative;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  transition: transform 0.2s ease;
}
.related-article-box:hover {
  transform: translateY(-5px);
}
.related-article-image {
  width: 100%;
  height: 210px;
  object-fit: cover;
}
.related-article-title {
  font-size: 16px;
  color: #000000;
  padding: 10px;
  font-weight: bold;
}
</style>
  