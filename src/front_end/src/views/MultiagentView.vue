<template>
  <div class="multiagent-container">
    <div class="main-content">
      <!-- 右侧对话区域 -->
      <div class="chat-area">
        <div class="stage-title">
          <h3>{{ stages[0].name }}</h3>
          <div class="stage-buttons">
            <button class="restart-btn" @click="showRestartModal = true">
              RESTART
            </button>
          </div>
        </div>

        <div class="chat-messages" ref="messageContainer">
          <!-- 欢迎信息 -->
          <div v-if="messages.length === 0" class="welcome-message">
            欢迎使用多智能体研究协作平台，请开始您的研究探索
          </div>

          <!-- 对话消息 -->
          <div
              v-for="(msg, index) in messages"
              :key="msg.id || index"
              class="message-block"
              :class="{
              'user-message': msg.role === 'user',
              'agent-message': msg.role === 'agent',
              'system-message': msg.role === 'system'
            }"
          >
            <!-- 用户消息不显示标题 -->
            <div v-if="msg.role === 'agent'" class="message-header">
              智能体 {{ msg.agent }} 回复
            </div>
            <div class="message-content">{{ msg.content }}</div>
          </div>

          <!-- 加载更多历史消息按钮 -->
          <div v-if="showLoadMore" class="load-more" @click="loadMoreMessages">
            加载更多历史消息
          </div>
        </div>

        <!-- 消息输入区域 -->
        <div class="chat-input">
          <input
              v-model="inputMessage"
              type="text"
              placeholder="输入您的研究问题..."
              @keyup.enter="handleEnter"
              :disabled="isLoading"
          />
          <button
              @click="handleSend"
              :disabled="!inputMessage.trim() || isLoading"
          >
            发送
          </button>
        </div>
      </div>
    </div>

    <!-- 重新开始确认模态框 -->
    <div v-if="showRestartModal" class="confirm-modal">
      <div class="modal-overlay" @click="showRestartModal = false"></div>
      <div class="modal-content">
        <div class="modal-body">
          <p>确认重新开始吗？您的所有对话记录将被清空。</p>
        </div>
        <div class="modal-footer">
          <button @click="showRestartModal = false">取消</button>
          <button @click="confirmRestart">确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue';

const stages = ref([
  {
    name: '研究协作阶段',
    agents: ['SurveyDirector', 'PaperRetriever', 'PaperSummarizer', 'SurveyAnalyst']
  }
]);

const currentStage = ref(0);
const messages = ref([]);
const inputMessage = ref('');
const isLoading = ref(false);
const showLoadMore = ref(false);
const showRestartModal = ref(false);
const messageId = ref(1);

let ws = null;
let reconnectTimeout = null;

// 添加消息到对话
const addMessage = (role, agent, content) => {
  messages.value.push({
    id: messageId.value++,
    role,
    agent,
    content,
    timestamp: new Date()
  });
  scrollToBottom();
};

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    const container = document.querySelector('.chat-messages');
    if (container) {
      container.scrollTop = container.scrollHeight;
    }
  });
};

// 连接WebSocket
const connectWebSocket = () => {
  if (ws) {
    ws.close();
    ws = null;
  }

  // 请根据需要修改为后端实际地址
  const wsUrl = 'ws://localhost:8000/ws/survey';
  ws = new WebSocket(wsUrl);

  ws.onopen = () => {
    console.log('WebSocket 已连接');
    isLoading.value = false;
  };

  // 统一按 type 处理消息
  ws.onmessage = (event) => {
    console.log('收到WebSocket消息:', event.data);
    let msg;
    try {
      msg = JSON.parse(event.data);
    } catch {
      console.error('收到非JSON格式消息:', event.data);
      return;
    }

    const {type, content, name = 'system'} = msg;

    switch (type) {
      case 'system_message':
        addMessage('system', 'system', content);
        break;
      case 'agent_message':
        addMessage('agent', name, content);
        break;
      case 'user_message': // 服务器 echo，可省略显示
        break;
      case 'workflow_started':
      case 'workflow_completed':
        addMessage('system', 'system', content);
        break;
      default:
        console.warn('消息格式不正确:', msg);
    }
    isLoading.value = false;
  };

  ws.onerror = (error) => {
    console.error('WebSocket 出错:', error);
    isLoading.value = false;
  };

  ws.onclose = () => {
    console.log('WebSocket 已断开，尝试重连...');
    isLoading.value = false;
    reconnectTimeout = setTimeout(connectWebSocket, 3000);
  };
};

onMounted(() => {
  connectWebSocket();
});

onBeforeUnmount(() => {
  if (ws) ws.close();
  if (reconnectTimeout) clearTimeout(reconnectTimeout);
});

const sendMessageToServer = (content) => {
  if (!ws || ws.readyState !== WebSocket.OPEN) {
    alert('WebSocket未连接，请稍后重试');
    return;
  }
  const message = {
    type: 'user_message',
    content,
    name: 'user',
    timestamp: new Date().toISOString()
  };
  ws.send(JSON.stringify(message));
};

// 处理发送按钮和回车
const handleSend = () => {
  if (!inputMessage.value.trim() || isLoading.value) return;

  addMessage('user', 'user', inputMessage.value);
  isLoading.value = true;
  sendMessageToServer(inputMessage.value);

  inputMessage.value = '';
};

const handleEnter = (event) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault();
    handleSend();
  }
};

const confirmRestart = () => {
  showRestartModal.value = false;

  currentStage.value = 0;
  messages.value = [];
  inputMessage.value = '';
  isLoading.value = false;
  showLoadMore.value = false;
  messageId.value = 1;

  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.send(JSON.stringify({type: 'restart'}));
  }
};
</script>

<style scoped>
/* 顶部AppBar样式 */
.multiagent-container {
  margin: 20px auto;
  height: calc(100vh - 40px);
  max-width: 1400px;
  display: flex;
  flex-direction: column;
  position: relative;
}

.app-bar {
  background-color: #ffffff;
  border-bottom: 1px solid #e4e6eb;
  padding: 0 24px;
  height: 60px;
  display: flex;
  align-items: center;
}

.app-bar-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.app-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2937;
}

.restart-btn {
  background-color: #ef4444;
  color: white;
  padding: 8px 16px;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s;
}

.restart-btn:hover {
  background-color: #dc2626;
}

.main-content {
  display: flex;
  flex: 1;
}

/* 右侧对话区域样式 */
.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
}

.stage-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background-color: #f9fafb;
  border-bottom: 1px solid #e4e6eb;
}

.stage-buttons {
  display: flex;
  gap: 12px;
}

.chat-messages {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
  background-color: #f9fafb;
  max-height: calc(100vh - 200px);
}

.message-block {
  margin-bottom: 24px;
  border-radius: 8px;
  padding: 16px;
  max-width: 80%;
  clear: both;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.user-message {
  background-color: #e0f2fe;
  color: #1f2937;
  float: right;
  border: 1px solid #bfdbfe;
}

.agent-message {
  background-color: #ffffff;
  color: #1f2937;
  float: left;
  border: 1px solid #e5e7eb;
}

/* ----------- 新增：system 消息配色（可按需调整） -------- */
.system-message {
  background-color: #fff7ed;
  border: 1px solid #fde68a;
  color: #92400e;
}

/* ------------------------------------------------------- */

.message-header {
  font-size: 14px;
  margin-bottom: 8px;
  font-weight: 500;
  color: #4b5563;
}

.message-content {
  font-size: 16px;
  line-height: 1.6;
  white-space: pre-wrap;
  color: #1f2937;
}

.welcome-message {
  text-align: center;
  color: #6b7280;
  font-size: 18px;
  padding: 24px 0;
}

.load-more {
  text-align: center;
  padding: 12px 0;
  color: #3b82f6;
  cursor: pointer;
  font-size: 14px;
}

/* 消息输入区域样式 */
.chat-input {
  display: flex;
  padding: 16px 24px;
  background-color: #ffffff;
  border-top: 1px solid #e4e6eb;
}

.chat-input input {
  flex: 1;
  padding: 10px 16px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 16px;
  color: #1f2937;
  outline: none;
  transition: border-color 0.2s;
}

.chat-input input:focus {
  border-color: #93c5fd;
  box-shadow: 0 0 0 3px rgba(59, 130, 253, 0.2);
}

.chat-input button {
  margin-left: 12px;
  padding: 10px 24px;
  background-color: #3b82f6;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.chat-input button:hover {
  background-color: #2563eb;
}

.chat-input button:disabled {
  background-color: #9ca3af;
  cursor: not-allowed;
}

/* 自定义确认模态框样式 */
.confirm-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 100;
}

.modal-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
  width: 400px;
  max-width: 90%;
  z-index: 1;
}

.modal-body {
  padding: 24px;
  color: #666;
  line-height: 1.5;
}

.modal-footer {
  padding: 16px 24px;
  border-top: 1px solid #e4e6eb;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.modal-footer button {
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.modal-footer button:first-child {
  background-color: #f3f4f6;
  color: #4b5563;
  border: 1px solid #d1d5db;
}

.modal-footer button:last-child {
  background-color: #10b981;
  color: white;
  border: none;
}

.modal-footer button:last-child:hover {
  background-color: #059669;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stage-title {
    flex-direction: column;
    align-items: flex-start;
  }

  .stage-buttons {
    margin-top: 12px;
    width: 100%;
    justify-content: flex-end;
  }

  .modal-content {
    width: 90%;
  }
}
</style>
