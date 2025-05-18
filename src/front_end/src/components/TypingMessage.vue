<template>
  <div
      class="px-3 py-3 rounded-lg bg-white text-gray-800 rounded-tl-none mr-auto message-content"
      v-html="parsedContent"
  ></div>
</template>

<script setup>
import { ref, watch, onMounted, defineProps, defineEmits } from 'vue';

const props = defineProps({
  content: {
    type: String,
    required: true
  },
  parseMarkdown: {
    type: Function,
    required: true
  }
});

const emit = defineEmits(['complete', 'character-typed']);
const displayedContent = ref('');
const parsedContent = ref('');
let currentIndex = 0;
let typingInterval;

// 打字效果函数
const typeNextChar = () => {
  if (currentIndex < props.content.length) {
    displayedContent.value = props.content.slice(0, currentIndex + 1);
    parsedContent.value = props.parseMarkdown(displayedContent.value);
    currentIndex++;
  } else {
    clearInterval(typingInterval);
    emit('complete');
  }
};

// 启动打字效果
const startTyping = () => {
  currentIndex = 0;
  displayedContent.value = '';
  parsedContent.value = '';
  if (typingInterval) clearInterval(typingInterval);
  typingInterval = setInterval(typeNextChar, 30);
};

// 监听内容变化
watch(() => props.content, (newContent) => {
  if (newContent) {
    startTyping();
  }
}, { immediate: true });

// 组件卸载时清理
onMounted(() => {
  return () => {
    if (typingInterval) clearInterval(typingInterval);
  };
});
</script>

<style scoped>
.message-content :deep(p) {
  margin-bottom: 0.5rem;
}

.message-content :deep(strong) {
  font-weight: 600;
  color: #1a365d;
}

.message-content :deep(em) {
  font-style: italic;
}

.message-content :deep(h3) {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 1rem 0 0.5rem;
  color: #2d3748;
}

.message-content :deep(ul) {
  list-style-type: disc;
  margin: 0.5rem 0 0.5rem 1.5rem;
}

.message-content :deep(ol) {
  list-style-type: decimal;
  margin: 0.5rem 0 0.5rem 1.5rem;
}
</style>