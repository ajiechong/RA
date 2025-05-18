<!-- src/components/ArticleBox.vue -->
<template>
  <div class="article-box" @click="goToDetail">
    <img :src="image" alt="Article Image" class="article-image" /> <!-- 添加图片 -->
    <div>
      <h2 class="article-title">{{ title }}</h2>
      <p class="description">{{ description }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "ArticleBox",
  props: {
    title: String,
    description: String,
    articleId: Number,  // 每个文章的唯一 ID
    image: String      // 图片
  },
  methods: {
    goToDetail() {
      // 跳转到 ArticleDetail 页面，传入 articleId 作为参数
      this.$router.push({ name: "ArticleDetail", params: { id: this.articleId } });
    }
  }
};
</script>

<style scoped>
.article-box {
  display: flex; /* 使用 flexbox 布局 */
  border: 1px solid #ccc;
  padding: 20px;
  margin-bottom: 20px;
  background-color: #f6fff8; /* 浅绿色背景 */
  border: 0.1px solid #005f00; /* 深绿色边框，调整边框宽度 */
  border-radius: 20px; /* 设置圆角，数值可以根据需要调整 */
  box-shadow: 4px 4px 8px rgba(0, 0, 0, 0.2); /* 右下角的阴影效果 */
  cursor: pointer;
}

.article-image {
  margin-top: 10px; /* 上方间距 */
  width: 180px;
  height: 150px;
  margin-right: 30px; /* 右侧间距 */
  border-radius: 20px; /* 设置圆角，数值可以根据需要调整 */
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.1);
}

.article-content {
  flex: 1; /* 使内容区域占据剩余空间 */
}

.article-title {
  color: #005f00; /* 深绿色标题 */
  margin: 10px 0;
  margin-bottom: 25px;
  position: relative; /* 添加相对定位，以便使用伪元素 */
  padding-left: 20px; /* 给标题增加左侧内边距，以留出空间放置竖线 */
  /*font-family: "Georgia", "Times New Roman", Times, serif;*/  /*系统自带的有衬线字体*/
  font-family: Arial, Helvetica, sans-serif; /*暂时统一Arial字体*/
}

.article-title::before {
  content: ""; /* 创建一个空的伪元素 */
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px; /* 竖线的宽度 */
  background-color: #0a7b0a; /* 竖线的颜色 */
  border-radius: 2px; /* 如果想要圆角竖线，可以设置小的 border-radius */
}

.description {
  /* font-family: "Georgia", "Times New Roman", Times, serif; */
  font-family: Arial, Helvetica, sans-serif;
  line-height: 1.6;
}

</style>
