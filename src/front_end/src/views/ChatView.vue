<template>
  <div class="content-section">
    <!-- 左侧历史对话栏 -->
    <div class="history-section">
      <!-- AI头像和名称 -->
      <div class="p-4 flex items-center bg-transparent">
        <div class="w-10 h-10 rounded-full overflow-hidden shadow-lg">
          <img :src="aiAvatar" alt="AI头像" class="w-full h-full object-cover" />
        </div>
        <div class="ml-4">
          <h2 class="text-2xl font-bold text-gray-900">RA</h2>
          <p class="text-sm text-gray-600">你的学术智能助手</p>
        </div>
      </div>
      <div class="h-px bg-gray-200 mx-4 my-2"></div>

      <!-- 新建对话按钮固定在顶部 -->
      <div class="p-4 border-t border-gray-200">
        <button
            @click="createNewChat"
            class="w-full flex items-center justify-center py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-lg shadow-md transition-all"
        >
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
          </svg>
          新对话
        </button>
      </div>
      <!-- 历史记录列表 -->
      <div class="flex-1 overflow-y-auto min-h-0">
        <div class="space-y-3 px-4" style="max-height: 60vh">
        <!-- 最近对话标题和折叠按钮 -->
        <div
            class="px-6 py-3 flex items-center justify-between cursor-pointer hover:bg-gray-100 rounded-lg"
            @click="toggleChatList"
        >

          <div class="flex items-center space-x-2">
            <span class="text-base font-medium text-gray-800">最近对话</span>
          </div>
          <svg :class="{'rotate-180': !isChatListCollapsed}" class="w-4 h-4 text-gray-500 transition-transform duration-200">
            <path fill-rule="evenodd" d="M15.707 4.293a1 1 0 010 1.414L10.414 10l5.293 5.293a1 1 0 01-1.414 1.414L9 11.414a1 1 0 010-1.414l5.293-5.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
          </svg>
        </div>

        <!--对话列表，增加预填充数据 -->
        <div v-show="!isChatListCollapsed" class="space-y-3 px-4">
          <div
              v-for="chat in chatSessions"
              :key="chat.cid"
              class="flex items-center justify-between bg-transparent hover:bg-gray-100 rounded-lg p-3 transition-colors duration-200"
          >
              <!-- 聊天名称 -->
              <button
                    @click="selectChat(chat.cid)"
                    class="flex-grow text-left truncate space-x-2 items-center"
                >
                <svg v-if="chat.unread" class="w-3 h-3 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                  <circle cx="12" cy="12" r="10" stroke-width="2"></circle>
                  <path d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4"></path>
                </svg>
                <span :class="{'text-blue-500 font-semibold': currentCid === chat.cid}">
                {{ chat.hname || '新对话' }}
              </span>
              </button>

              <!-- 三点菜单 -->
              <div class="relative ml-2">
                <button
                    @click.stop="openMenu(chat)"
                    class="p-1 rounded-full hover:bg-gray-200 transition-colors"
                >
                  <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 text-gray-600">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M6 15.75L3 12m0 3.75l3-3.75m6 0L9 12m0 3.75l3-3.75M18 12a4.5 4.5 0 10-9 0 4.5 4.5 0 009 0z"></path>
                  </svg>
                </button>

                <!-- 下拉菜单 -->
                <div
                    v-if="activeMenu === chat.cid"
                    class="absolute right-0 mt-2 w-40 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-50"
                >
                  <div class="py-1">
                    <button
                        @click.stop="openRenameDialog(chat)"
                        class="block w-full px-4 py-2 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600"
                    >
                      重命名
                    </button>
                    <button
                        @click.stop="confirmDelete(chat)"
                        class="block w-full px-4 py-2 text-sm text-red-600 hover:bg-red-50 hover:text-red-700 "
                    >
                      删除
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 重命名对话框 -->
        <div v-if="showRenameDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div class="bg-white rounded-lg p-6 w-80 shadow-lg">
            <h3 class="text-lg font-medium text-gray-800 mb-4">重命名对话</h3>
            <input
                v-model="newChatName"
                type="text"
                class="w-full px-4 py-2 border rounded-md mb-4 border-gray-300 focus:border-blue-500"
                placeholder="输入新的对话名称"
            >
            <div class="flex justify-end gap-3">
              <button
                  @click="cancelRename"
                  class="px-6 py-2 text-gray-600 hover:bg-gray-100 rounded-md"
              >
                取消
              </button>
              <button
                  @click="confirmRename"
                  class="px-6 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-md"
              >
                确认
              </button>
            </div>
          </div>
        </div>

          <!-- 删除确认对话框 -->
        <div v-if="showDeleteDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div class="bg-white rounded-lg p-6 w-80 shadow-lg">
            <h3 class="text-lg font-medium text-gray-800 mb-4">确认删除</h3>
            <p class="text-gray-600 mb-4">
              确定要删除 <span class="font-medium">{{ selectedChat?.hname || '对话' }}</span> 吗？<br>
              此操作不可撤销。
            </p>
            <div class="flex justify-end gap-3">
              <button
                  @click="cancelDelete"
                  class="px-6 py-2 text-gray-600 hover:bg-gray-100 rounded-md"
              >
                取消
              </button>
              <button
                  @click="executeDelete"
                  class="px-6 py-2 bg-red-600 text-white hover:bg-red-700 rounded-md"
              >
                删除
              </button>
            </div>
          </div>
        </div>
        </div>

    </div>
    </div>
    <!-- 右侧聊天区域 -->
    <div class="chat-container">
      <div class="chat-messages" ref="messageContainer">
      <!--        消息输入区域-->
        <div
            class="message"
            v-for="(msg, i) in messages"
            :key="msg.tempId || i"
            :class="msg.role === 'user' ? 'user' : 'bot'"
        >
          <!-- 消息内容部分 -->
          <div class="content">
            <template v-if="msg.role === 'user'">
              <div class="user-message">
                {{ msg.content }}
              </div>
            </template>
            <template v-else>
              <TypingMessage
                  v-if="msg.isTyping"
                  :content="msg.content"
                  @complete="onTypingComplete(msg)"
                  @character-typed="scrollToBottom"
                  :parseMarkdown="parseMarkdown"
              />
              <div
                  v-else
                  class="px-3 py-3 rounded-lg bg-white text-gray-800 rounded-tl-none mr-auto message-content"
                  v-html="parseMarkdown(msg.content)"
              ></div>
            </template>
          </div>
          </div>
        <!-- 加载动画 -->
        <div v-if="isLoading" class="message bot">
          <div class="avatar">
            <img :src="aiAvatar" alt="AI头像" />
          </div>
          <div class="content">
            <div class="loading-dots">
              <div class="dot"></div>
              <div class="dot"></div>
              <div class="dot"></div>
            </div>
          </div>
        </div>
        </div>
      <!--   底部输入区域-->
        <div class="chat-input">
        <input
            v-model="inputMessage"
            type="text"
            placeholder="输入消息..."
            @keyup.enter="sendMessage"
            :disabled="isLoading || isTyping"
        >
        <button
            @click="sendMessage"
            :disabled="!inputMessage.trim() || isLoading || isTyping"
        >
          发送
        </button>
      </div>
      </div>

      <!-- 右侧额外内容区域（新加的部分） -->
      <div class="sidebar">
        <div class="sidebar-content">
          <div
              v-for="(item, index) in sidebarItems"
              :key="index"
              class="sidebar-item"
              @click="handleSidebarClick(item.prompt)"
          >
            {{ item.title }}
          </div>
        </div>
      </div>
  </div>
</template>

<script setup>
import aiAvatar from '@/assets/imgs/icon/ai-avatar.svg';
import { ref, onMounted, nextTick, watch } from 'vue'
import axios from 'axios'
import { API_BASE_URL } from '@/utils/api';
// import { useRouter } from 'vue-router'
// import { useStore } from 'vuex'
import TypingMessage from '@/components/TypingMessage.vue';
import { marked } from 'marked';
import DOMPurify from 'dompurify';

// 状态定义
// const router = useRouter()
// const store = useStore()
const showWelcome = ref(true)  // 新增：控制是否显示欢迎界面
const isLoading = ref(false)  // 新增加载状态
const isTyping = ref(false);  // 新增状态
const activeMenu = ref(null)
const showRenameDialog = ref(false)
const showDeleteDialog = ref(false)
const newChatName = ref('')
const selectedChat = ref(null)

const inputMessage = ref('')
const messages = ref([])
const chatSessions = ref([])
const currentCid = ref(null)
const messageContainer = ref(null)
// 添加折叠状态控制
const isChatListCollapsed = ref(false)

// 从本地存储获取用户ID，实际应用中应该从用户认证系统获取
// const uid = ref(store.state.isLoggedIn ? parseInt(store.getters.uid) : null)

//测试使用固定的uid
const uid = ref(1);

// API 基础URL
const API_BASE_URL0 = '/api/chathistories'

// 在脚本部分添加侧边栏数据
const sidebarItems = ref([
  { title: '✍️学术润色', prompt: '请帮我润色以下学术论文段落，要求保持专业学术语气同时提升表达清晰度：' },
  { title: '📚文献综述', prompt: '我需要关于[主题]的文献综述，请帮我整理最新研究成果并分析趋势：' },
  { title: '🔍 开题报告', prompt: '请根据以下研究内容生成开题报告框架：' },
  { title: '📊文献总结', prompt: '请总结以下文献的核心观点和方法论：' },
  { title: '⚙️实验设计', prompt: '我需要设计一个关于[主题]的实验，请提供方法论建议：' },
  { title: '🧠 实验分析', prompt: '请帮助分析以下实验数据并给出专业解读：' },
  { title: '💻 帮我调试代码', prompt:'我在调试[目标语言]代码时遇到了错误，能帮我看看吗？具体报错信息如下：[此处粘贴错误日志]' },
  { title: '🚀 优化代码性能', prompt:'我的图像处理程序运行速度很慢，能帮忙做一下性能优化吗？代码主要使用OpenCV和NumPy'},
]);

// 预填充测试数据（添加到状态定义处）
chatSessions.value = [
  { cid: 1, hname: '学术论文润色', unread: true },
  { cid: 2, hname: '实验设计讨论', unread: false },
  { cid: 3, hname: '代码调试记录', unread: true }
];

// 初始化加载
onMounted(async () => {
  ////////  ignore log
  // if (!store.state.isLoggedIn) {
  //   alert("请先登录后再使用聊天功能");
  //   router.push('/');
  //   return;
  // }
  await loadChatSessions();
});

// 菜单相关方法
const openMenu = (chat) => {
  activeMenu.value = activeMenu.value === chat.cid ? null : chat.cid;
};


// 点击其他地方关闭菜单
onMounted(() => {
  document.addEventListener('click', () => {
    activeMenu.value = null;
  });
});



// 重命名相关方法（修复数据更新逻辑）
const openRenameDialog = (chat) => {
  selectedChat.value = { ...chat }; // 创建副本避免响应式问题
  newChatName.value = chat.hname || '新对话';
  showRenameDialog.value = true;
  activeMenu.value = null;
};

const confirmRename = async () => {
  if (!selectedChat.value || !newChatName.value.trim()) return;

  try {
    // 调用API更新对话名称
    await axios.put(`${API_BASE_URL0}/sessions/${selectedChat.value.cid}/rename`, {
      newName: newChatName.value.trim()
    });

    // 更新本地数据（使用不可变更新避免响应式问题）
    chatSessions.value = chatSessions.value.map(chat =>
        chat.cid === selectedChat.value.cid ? { ...chat, hname: newChatName.value.trim() } : chat
    );

    // 更新当前对话名称
    if (currentCid.value === selectedChat.value.cid) {
      messages.value = messages.value.map(msg => ({ ...msg, hname: newChatName.value.trim() }));
    }

    cancelRename();
  } catch (error) {
    console.error('重命名失败:', error);
    // 添加错误提示逻辑
  }
};

const cancelRename = () => {
  showRenameDialog.value = false;
  newChatName.value = '';
  selectedChat.value = null;
};

const parseMarkdown = (content) => {
  const rawHtml = marked(content);
  return DOMPurify.sanitize(rawHtml);
};


// 删除相关方法
const confirmDelete = (chat) => {
  selectedChat.value = chat;
  showDeleteDialog.value = true;
  activeMenu.value = null;
};

const cancelDelete = () => {
  showDeleteDialog.value = false;
  selectedChat.value = null;
};

// 删除相关方法（修复数据过滤逻辑）
const executeDelete = async () => {
  if (!selectedChat.value) return;

  try {
    await axios.delete(`${API_BASE_URL0}/sessions/${selectedChat.value.cid}`);

    // 优化数据更新方式
    const deletedCid = selectedChat.value.cid
    chatSessions.value = chatSessions.value.filter(chat => chat.cid !== deletedCid)

    // 自动选择第一个对话或新建
    if (chatSessions.value.length > 0) {
      await selectChat(chatSessions.value[0].cid)
    } else {
      await createNewChat()
    }

    cancelDelete()
  } catch (error) {
    console.error('删除失败:', error)
    alert('删除失败，请稍后重试') // 添加用户提示
  }
}
// 切换折叠状态的函数
const toggleChatList = () => {
  isChatListCollapsed.value = !isChatListCollapsed.value
}
const handleSidebarClick = (prompt) => {
  inputMessage.value = prompt;
};

// 在 <script setup> 的顶部添加
axios.defaults.baseURL = `${API_BASE_URL}`; // 替换为你的后端服务地址
axios.defaults.timeout = 10000; // 10秒超时
axios.interceptors.request.use(config => {
  console.log('发送请求:', config);
  return config;
});

axios.interceptors.response.use(
    response => {
      console.log('收到响应:', response);
      return response;
    },
    error => {
      console.error('请求错误:', {
        url: error.config?.url,
        status: error.response?.status,
        data: error.response?.data,
        message: error.message
      });
      return Promise.reject(error);
    }
);

// 加载聊天会话
async function loadChatSessions() {
  try {
    const response = await axios.get(`${API_BASE_URL0}/sessions/${uid.value}`);
    console.log('Chat sessions response:', response.data);
    console.log('Raw response data:', response.data); // 打印原始数据

    chatSessions.value = response.data.map(session => {
      console.log('Single session data:', session); // 打印单个会话数据
      return {
        ...session,
        // 使用正确的字段名 hname
        hname: session.hname
      };
    });
  } catch (error) {
    console.error('Failed to load chat sessions:', error);
  }
}
// 创建新会话
async function createNewChat() {
  try {
    const response = await axios.post(`${API_BASE_URL0}/sessions/new`, {
      uid: uid.value,
    })
    const newCid = response.data
    await loadChatSessions()
    await selectChat(newCid)
  } catch (error) {
    console.error('Failed to create new chat:', error)
    // 使用测试数据
    const newChat = {
      cid: Date.now(),
      hname: '新对话'
    }
    chatSessions.value.unshift(newChat)
    await selectChat(newChat.cid)
  }
}

// 选择会话
//在选择会话的函数中也确保数据格式正确
async function selectChat(cid) {
  showWelcome.value = false;
  currentCid.value = cid;
  try {
    const response = await axios.get(`${API_BASE_URL0}/messages/${cid}`);
    // 过滤掉系统指令消息
    messages.value = response.data
        .filter(msg => msg.role !== 'system')
        .map(msg => ({
          ...msg,
          role: msg.role || 'assistant'
        }));
  } catch (error) {
    console.error('Failed to load chat messages:', error);
    messages.value = [
      { content: '你好！我是您专属的科研助手RA，最近有什么感兴趣的方向吗？欢迎和我一起进行头脑风暴～🧠⛈️', role: 'assistant', htime: new Date() }
    ];
  }
  await scrollToBottom();
}

// 发送消息
async function sendMessage() {
  ///////   ignore log
  // if (!store.state.isLoggedIn) {
  //   alert("请先登录后再发送消息");
  //   return;
  // }
  if (!inputMessage.value.trim() || isLoading.value) return;

  const messageContent = inputMessage.value;
  inputMessage.value = '';
  isLoading.value = true;

  // 如果当前是欢迎界面或没有当前会话，先创建新会话
  if (showWelcome.value || !currentCid.value) {
    try {
      const response = await axios.post(`${API_BASE_URL0}/sessions/new`, {
        uid: uid.value
      });
      currentCid.value = response.data;
      showWelcome.value = false;  // 隐藏欢迎界面
    } catch (error) {
      console.error('Failed to create new chat session:', error);
      const newCid = Date.now();
      currentCid.value = newCid;
      showWelcome.value = false;
    }
  }

  // 先在界面上显示用户消息，但不保存到数据库
  const userMessage = {
    content: messageContent,
    role: 'user',
    htime: new Date(),
    tempId: Date.now() // 添加临时ID用于标识
  };
  messages.value.push(userMessage);
  await scrollToBottom();

  // 重试相关变量
  const maxRetries = 5;
  let currentTry = 0;
  let success = false;

  while (currentTry < maxRetries && !success) {
    try {
      const response = await axios.post(`${API_BASE_URL0}/send`, {
        cid: parseInt(currentCid.value),
        uid: parseInt(uid.value),
        content: messageContent,
        shouldSave: currentTry === 0 // 只在第一次尝试时设置为保存
      });

      if (response.data && response.data.content) {
        // 找到临时消息并更新（避免直接修改数组索引）
        const tempMsgIndex = messages.value.findIndex(msg => msg.tempId === userMessage.tempId);
        if (tempMsgIndex !== -1) {
          messages.value[tempMsgIndex].tempId = null; // 标记为已保存
        }

        const aiMessage = {
          content: response.data.content,
          role: 'assistant',
          htime: new Date(),
          isTyping: true  // 新增标记
        };
        messages.value.push(aiMessage);
        isTyping.value = true;

        success = true;
      } else {
        throw new Error('无效的响应数据');
      }
    } catch (error) {
      console.error(`第 ${currentTry + 1} 次尝试失败:`, error);
      currentTry++;

      if (currentTry === maxRetries) {
        // 所有重试都失败，添加错误消息，保留用户消息但不存数据库
        messages.value.push({
          content: "RA出了点小问题，请你稍候再试试吧~",
          role: 'assistant',
          htime: new Date(),
          isError: true // 标记为错误消息
        });
      } else {
        // 在重试之前等待一段时间，时间随重试次数增加
        await new Promise(resolve => setTimeout(resolve, 1000 * currentTry));
      }
    }
  }

  isLoading.value = false;
  await scrollToBottom();
}


// 滚动到底部
async function scrollToBottom() {
  await nextTick()
  if (messageContainer.value) {
    const container = messageContainer.value;
    const isNearBottom = container.scrollHeight - container.scrollTop - container.clientHeight < 100;

    // 如果用户已经接近底部或者正在打字，则自动滚动
    if (isNearBottom || isTyping.value) {
      container.scrollTop = container.scrollHeight;
    }
  }
}
// 新增打字完成处理函数
const onTypingComplete = (message) => {
  const targetMessage = messages.value.find(m => m === message);
  if (targetMessage) {
    targetMessage.isTyping = false;
  }
  isTyping.value = false;
  scrollToBottom();
};


// 监听消息变化，自动滚动到底部
watch(messages, async () => {
  await scrollToBottom()
}, { deep: true })

</script>
<style scoped>
html, body {
  height: 100%;
  margin: 0;
  overflow: hidden; /* 禁止页面滚动 */
}

/* 统一颜色样式 */
:root {
  --primary-blue: #4B7BEC;
  --hover-bg: rgba(255, 255, 255, 0.1);
  --selected-bg: #E1F1F8;
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
  background: rgba(255, 255, 255, 0.1); /* 透明背景 */
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}


/* 对话列表项样式 */
.history-section .chat-item {
  @apply px-4 py-3 transition-all rounded-xl;
  margin: 0 8px;
  background: transparent; /* 默认透明 */
  border: 1px solid transparent; /* 点击时显示边框 */
}

.history-section .chat-item:hover {
  background: rgba(75, 123, 236, 0.05); /* 悬停浅色背景 */
}

.history-section .selected {
  @apply border-l-4 border-blue-500 bg-blue-100; /* 选中时浅蓝色背景 */
  background: rgba(75, 123, 236, 0.1); /* 透明蓝色背景 */
}

/* 移除冲突的bg-blue-50样式 */
.history-section .bg-blue-50 {
  display: none; /* 或直接删除该选择器 */
}

/* 菜单样式 */
.history-section .absolute {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

/* 新建按钮样式 */
.history-section .bg-blue-600 {
  box-shadow: 0 2px 4px rgba(75, 123, 236, 0.2);
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
  background-color: #efeeee;
  align-self: flex-start; /* 机器人的消息左对齐 */
}

.message.user {
  background-color: #e1f1f8;
  align-self: flex-end;
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
  height: 90%;
  overflow-y: auto;
  font-size: 14px;
  /* 取消视觉效果 */
  background: none;
  border: none;
  box-shadow: none;
}

.sidebar-content {
  display: flex;
  flex-direction: column; /* 改为垂直布局 */
  margin-left: 1.5rem;
  gap: 0.8rem; /* 调整间距 */
}

.sidebar-item {
  background-color: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  padding: 1rem;
  border-radius: 8px;
  width: 80%; /* 宽度改为100%以填满容器 */
  color: white;
  text-align: center;
  backdrop-filter: blur(6px);
  transition: transform 0.3s ease;
  height: 50px; /* 高度自动调整 */
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.0rem;
  font-weight: 200;
  letter-spacing: 1px;
}

.sidebar-item:hover {
  transform: translateY(-5px);
  background-color: rgba(255, 255, 255, 0.15);
}

/* 对话框样式优化 */
.fixed .bg-white {
  border-radius: 16px;
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
}

/* 添加输入框可见性样式 */
input[type="text"] {
  color: #333 !important;
  background: #fff !important;
}

</style>
