<template>
  <div class="content-section">
    <!-- 左侧历史对话栏 -->
    <div class="history-section">
      <div class="history-item" v-for="(item, index) in historyMessages" :key="index">
        {{ item }}
      </div>
    </div>

    <!-- 右侧聊天区域 -->
    <div class="chat-container">
      <div class="chat-messages">
        <div
            class="message"
            v-for="(msg, i) in chatMessages"
            :key="i"
            :class="msg.sender"
        >
          {{ msg.text }}
        </div>
      </div>
      <div class="chat-input">
        <input
            type="text"
            v-model="chatInput"
            placeholder="Ask me anything..."
            @keydown.enter="sendMessage"
        />
        <button @click="sendMessage">Send</button>
      </div>
    </div>

    <!-- 右侧额外内容区域（新加的部分） -->
    <div class="sidebar">
      <div class="sidebar-content">
        <div class="sidebar-item">学术润色</div>
        <div class="sidebar-item">文献综述</div>
        <div class="sidebar-item">开题报告</div>
        <div class="sidebar-item">文献总结</div>
        <div class="sidebar-item">实验设计</div>
        <div class="sidebar-item">实验分析</div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatView',
  data() {
    return {
      chatInput: '',
      historyMessages: [
        '历史对话 1: LLM 模型详解',
        '历史对话 2: 实验报告分析',
        '历史对话 3: 帮我写文献综述',
      ],
      chatMessages: [{ sender: 'bot', text: '你好！请问有什么可以帮到你的？' }],
    };
  },
  methods: {
    sendMessage() {
      if (this.chatInput.trim()) {
        this.chatMessages.push({ sender: 'user', text: this.chatInput });
        this.chatMessages.push({ sender: 'bot', text: `You asked: ${this.chatInput}` });
        this.chatInput = '';
      }
    },
  },
};
</script>
<style scoped>
html, body {
  height: 100%;
  margin: 0;
  overflow: hidden; /* 禁止页面滚动 */
}

.content-section {
  margin: 0;
  display: flex;
  padding: 1rem;
  gap: 1rem;
  height: calc(100vh - 200px); /* 除去 AppNavBar 高度 */
  box-sizing: border-box;
  overflow: hidden;
}

/* 左侧历史对话栏 */
.history-section {
  width: 250px;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 1rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  height: 100%;
  overflow-y: auto;
  font-size: 16px;
  margin-left: 1.5rem;
  margin-right: 1.5rem;
}

.history-item {
  margin-bottom: 1rem;
  color: white;
  cursor: pointer;
  font-size: 16px;
  line-height: 2;
}

/* 中间聊天区域 */
.chat-container {
  width: 60%;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  height: 100%;
  overflow: hidden;
}

.chat-messages {
  flex: 1;
  padding: 1.5rem;
  overflow-y: auto;
  background-color: #f9f9f9;
  display: flex;
  flex-direction: column;
  gap: 1rem; /* 增加消息之间的间距 */
}

.message {
  padding: 0.75rem 1rem;
  border-radius: 8px;
  max-width: 60%; /* 限制最大宽度 */
  color: black;
  word-wrap: break-word; /* 防止长单词撑破容器 */
  display: inline-block;
}

.message.bot {
//background-color: #e5f1f6;
//background-color: #faf0dd;
  background-color: #efeeee;
  align-self: flex-start; /* 机器人的消息左对齐 */
}

.message.user {
  background-color: #e1f1f8;
//background-color: #faf0dd;
//background-color: #e4e1e1;
  align-self: flex-end; /* 用户的消息右对齐 */
}

/* 发送框样式 */
.chat-input {
  display: flex;
  padding: 1rem;
  background-color: white;
  border-top: 1px solid #eee;
  justify-content: center;
}

.chat-input input {
  flex: 1;
  padding: 0.75rem 1rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  margin-right: 1rem;
  background-color: white;
  color: black;
}

.chat-input button {
  padding: 0.75rem 1.5rem;
  background-color: #1a365d;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

/* 右侧额外内容区域 */
.sidebar {
  width: 20%;
  /* 移除背景和边框等 */
  height: 100%;
  overflow-y: auto;
  font-size: 16px;
  /* 取消视觉效果 */
  background: none;
  border: none;
  box-shadow: none;
}

.sidebar-content {
  display: flex;
  flex-direction: column; /* 改为垂直布局 */
  margin-left: 1.5rem;
  gap: 1.5rem; /* 调整间距 */
}

.sidebar-item {
  background-color: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 1rem;
  border-radius: 8px;
  width: 60%; /* 宽度改为100%以填满容器 */
  color: white;
  text-align: center;
  backdrop-filter: blur(6px);
  transition: transform 0.3s ease;
  height: 90px; /* 高度自动调整 */
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.2rem;
  font-weight: 200;
  letter-spacing: 1px;
}

.sidebar-item:hover {
  transform: translateY(-5px);
  background-color: rgba(255, 255, 255, 0.15);
}

</style>
