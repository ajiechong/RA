
<template>
  <div class="fixed top-[120px] bottom-0 left-0 right-0 flex bg-gray-100">
    <!-- 左侧边栏 -->

    <div class="w-64 bg-white border-r border-gray-200 flex flex-col">

      <!-- AI头像和名称 -->
      <div class="p-4 flex items-center">
        <div class="w-12 h-12 rounded-full overflow-hidden">
          <img
              :src="require('@/assets/imgs/icon/ai-avatar.svg')"
              alt="AI头像"
              class="w-full h-full object-cover"
          >
        </div>
        <span class="ml-3 text-lg font-bold text-gray-900">沙包</span>
      </div>

      <!-- 分割线 -->
      <div class="h-px bg-gray-200 mx-4"></div>


      <!-- 对话列表区域 -->
      <div class="flex-1 overflow-y-auto">
        <!-- 最近对话标题和折叠按钮 -->
        <div
            class="px-7 py-2 flex items-center justify-between cursor-pointer hover:bg-gray-50"
            @click="toggleChatList"
        >

          <div class="flex items-center">
  <span class="mr-4">
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path
          d="M12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2C6.47715 2 2 6.47715 2 12C2 13.8214 2.48697 15.5291 3.33782 17L2.5 21.5L7 20.6622C8.47087 21.513 10.1786 22 12 22Z"
          stroke="currentColor"
          stroke-width="2"
          stroke-linecap="round"
          stroke-linejoin="round"
      />
    </svg>
  </span>
            <span class="text-base font-medium text-gray-900">最近对话</span>
          </div>

          <svg
              class="w-4 h-4 text-gray-500 transform transition-transform duration-200"
              :class="{'rotate-90': !isChatListCollapsed}"
              viewBox="0 0 20 20"
              fill="currentColor"
          >
            <path fill-rule="evenodd" d="M7.293 4.293a1 1 0 011.414 0L14.414 10l-5.707 5.707a1 1 0 01-1.414-1.414L11.586 10 7.293 5.707a1 1 0 010-1.414z" clip-rule="evenodd" />
          </svg>
        </div>

        <!-- 在聊天列表部分修改 -->
        <div v-show="!isChatListCollapsed" class="space-y-1 px-2">
          <div
              v-for="chat in chatSessions"
              :key="chat.cid"
              class="flex items-center justify-between w-full px-4 py-3 rounded-lg hover:bg-gray-100"
              :class="{'bg-blue-50': currentCid === chat.cid}"
          >
            <!-- 聊天名称部分，点击选择对话 -->
            <button
                @click="selectChat(chat.cid)"
                class="flex-grow text-left truncate"
                :class="{'text-blue-600': currentCid === chat.cid}"
            >
              {{ chat.hname || '新对话' }}
            </button>

            <!-- 三点菜单 -->
            <div class="relative ml-2">
              <button
                  @click.stop="openMenu(chat)"
                  class="p-1 rounded-full hover:bg-gray-200"
              >
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                  <circle cx="5" cy="12" r="1"></circle>
                  <circle cx="12" cy="12" r="1"></circle>
                  <circle cx="19" cy="12" r="1"></circle>
                </svg>
              </button>

              <!-- 下拉菜单 -->
              <div
                  v-if="activeMenu === chat.cid"
                  class="absolute right-0 mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-50"
              >
                <div class="py-1">
                  <button
                      @click.stop="openRenameDialog(chat)"
                      class="block w-full px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
                  >
                    重命名
                  </button>
                  <button
                      @click.stop="confirmDelete(chat)"
                      class="block w-full px-4 py-2 text-sm text-red-600 hover:bg-gray-100"
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
          <div class="bg-white rounded-lg p-6 w-96">
            <h3 class="text-lg font-medium mb-4">重命名对话</h3>
            <input
                v-model="newChatName"
                type="text"
                class="w-full px-3 py-2 border rounded-md mb-4"
                placeholder="输入新的对话名称"
            >
            <div class="flex justify-end gap-2">
              <button
                  @click="cancelRename"
                  class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-md"
              >
                取消
              </button>
              <button
                  @click="confirmRename"
                  class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
              >
                确认
              </button>
            </div>
          </div>
        </div>

        <!-- 删除确认对话框 -->
        <div v-if="showDeleteDialog" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div class="bg-white rounded-lg p-6 w-96">
            <h3 class="text-lg font-medium mb-4">确认删除</h3>
            <p class="mb-4">确定要删除这个对话吗？此操作不可撤销。</p>
            <div class="flex justify-end gap-2">
              <button
                  @click="cancelDelete"
                  class="px-4 py-2 text-gray-600 hover:bg-gray-100 rounded-md"
              >
                取消
              </button>
              <button
                  @click="executeDelete"
                  class="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600"
              >
                删除
              </button>
            </div>
          </div>
        </div>



      </div>



      <!-- 新建对话按钮固定在底部 -->
      <div class="p-4 border-t border-gray-200">
        <button
            @click="createNewChat"
            class="w-full flex items-center justify-center py-2 px-4 bg-blue-500 hover:bg-blue-600 text-white rounded-lg transition-colors"
        >
          <span class="mr-2">+</span>
          <span>新对话🤓☝️</span>
        </button>
      </div>
    </div>


    <!-- 右侧聊天区域 -->
    <div class="flex-1 flex flex-col chat-background" >
      <!-- 聊天消息区域 -->
      <div class="flex-1 overflow-y-auto p-4 pb-24 relative" ref="messageContainer">
        <div class="max-w-3xl mx-auto space-y-4">
          <template v-if="showWelcome">
            <!-- 使用 React 组件作为欢迎界面 -->
            <WelcomeAnimation />
          </template>

          <template v-else>
            <div class="max-w-6xl ml-1 mr-auto space-y-4">
              <div v-for="(message, index) in messages"
                   :key="index"
                   class="flex items-start gap-2 mb-5"
                   :class="[message.role === 'user' ? 'flex-row-reverse pr-0' : 'pl-0']"
              >
                <!-- 头像部分 -->
                <div class="flex-shrink-0 w-10 h-10 rounded-full overflow-hidden">
                  <img
                      :src="message.role === 'user'
        ? (store.state.avatar_url || require('@/assets/imgs/icon/default-avatar.jpg'))
        : require('@/assets/imgs/icon/ai-avatar.svg')"
                      :alt="message.role === 'user' ? '用户头像' : 'AI头像'"
                      class="w-full h-full object-cover"
                  >
                </div>

                <!-- 消息内容部分 -->
                <div class="flex flex-col max-w-[65%]">



                  <template v-if="message.role === 'user'">
                    <div class="px-3 py-3 rounded-lg bg-blue-500 text-white rounded-tr-none ml-auto">
                      {{ message.content }}
                    </div>
                  </template>
                  <template v-else>
                    <TypingMessage
                        v-if="message.isTyping"
                        :content="message.content"
                        @complete="onTypingComplete(message)"
                        @character-typed="scrollToBottom"
                        :parseMarkdown="parseMarkdown"
                    />
                    <div
                        v-else
                        class="px-3 py-3 rounded-lg bg-white text-gray-800 rounded-tl-none mr-auto message-content"
                        v-html="parseMarkdown(message.content)"
                    >
                    </div>
                  </template>



                </div>
              </div>

              <!-- 加载动画 -->
              <div v-if="isLoading" class="flex items-start gap-2 mb-5">
                <div class="flex-shrink-0 w-10 h-10 rounded-full overflow-hidden">
                  <img
                      :src="require('@/assets/imgs/icon/ai-avatar.svg')"
                      alt="AI头像"
                      class="w-full h-full object-cover"
                  >
                </div>
                <div class="flex flex-col max-w-[65%]">
                  <div class="px-6 py-4 rounded-lg bg-white text-gray-800 rounded-tl-none mr-auto">
                    <div class="flex space-x-2">
                      <div class="w-2 h-2 bg-gray-500 rounded-full animate-bounce"></div>
                      <div class="w-2 h-2 bg-gray-500 rounded-full animate-bounce" style="animation-delay: 0.2s"></div>
                      <div class="w-2 h-2 bg-gray-500 rounded-full animate-bounce" style="animation-delay: 0.4s"></div>
                    </div>
                  </div>
                </div>
              </div>

            </div>
          </template>
        </div>
      </div>

      <!-- 底部输入区域 -->
      <div class="fixed bottom-0 left-64 right-0 border-t border-gray-200 bg-white p-4">
        <div class="max-w-3xl mx-auto flex gap-4">
          <input
              v-model="inputMessage"
              type="text"
              placeholder="输入消息..."
              class="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:border-blue-500"
              @keyup.enter="sendMessage"
              :disabled="isLoading || isTyping"
          >
          <button
              @click="sendMessage"
              class="px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors"
              :disabled="!inputMessage.trim() || isLoading || isTyping"
          >
            发送
          </button>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import axios from 'axios'
import WelcomeAnimation from '@/components/WelcomeAnimation.vue' // 导入欢迎动画组件
import { API_BASE_URL } from '@/utils/api';
import { useRouter } from 'vue-router'  // 添加这行
import { useStore } from 'vuex'
import TypingMessage from '@/components/TypingMessage.vue';  // 引入组件
import { marked } from 'marked';
import DOMPurify from 'dompurify';


// 状态定义
const router = useRouter()
const store = useStore()
const showWelcome = ref(true)  // 新增：控制是否显示欢迎界面
const isLoading = ref(false)  // 新增加载状态
const isTyping = ref(false);  // 新增状态
const activeMenu = ref(null);
const showRenameDialog = ref(false);
const showDeleteDialog = ref(false);
const newChatName = ref('');
const selectedChat = ref(null);

const inputMessage = ref('')
const messages = ref([])
const chatSessions = ref([])
const currentCid = ref(null)
const messageContainer = ref(null)
// 添加折叠状态控制
const isChatListCollapsed = ref(false)

// 从本地存储获取用户ID，实际应用中应该从用户认证系统获取
const uid = ref(store.state.isLoggedIn ? parseInt(store.getters.uid) : null)

// API 基础URL
const API_BASE_URL0 = '/api/chathistories'


// 初始化加载
onMounted(async () => {
  if (!store.state.isLoggedIn) {
    alert("请先登录后再使用聊天功能");
    router.push('/');
    return;
  }
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



// 重命名相关方法
const openRenameDialog = (chat) => {
  selectedChat.value = chat;
  newChatName.value = chat.hname || '新对话';
  showRenameDialog.value = true;
  activeMenu.value = null;
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

const confirmRename = async () => {
  if (!selectedChat.value || !newChatName.value.trim()) return;

  try {
    await axios.put(`${API_BASE_URL0}/sessions/${selectedChat.value.cid}/rename`, {
      newName: newChatName.value.trim()
    });

    // 更新本地状态
    const chat = chatSessions.value.find(c => c.cid === selectedChat.value.cid);
    if (chat) {
      chat.hname = newChatName.value.trim();
    }

    // 如果是当前选中的对话，也更新消息列表中的名称
    if (currentCid.value === selectedChat.value.cid) {
      messages.value.forEach(msg => {
        msg.hname = newChatName.value.trim();
      });
    }

    cancelRename();
  } catch (error) {
    console.error('Failed to rename chat:', error);
    // 可以添加错误提示
  }
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

const executeDelete = async () => {
  if (!selectedChat.value) return;

  try {
    await axios.delete(`${API_BASE_URL0}/sessions/${selectedChat.value.cid}`);

    // 更新本地状态
    chatSessions.value = chatSessions.value.filter(
        chat => chat.cid !== selectedChat.value.cid
    );

    // 如果删除的是当前选中的对话，显示欢迎界面
    if (currentCid.value === selectedChat.value.cid) {
      currentCid.value = null;
      messages.value = [];
      showWelcome.value = true;
    }

    cancelDelete();
  } catch (error) {
    console.error('Failed to delete chat:', error);
    // 可以添加错误提示
  }
};

// 切换折叠状态的函数
const toggleChatList = () => {
  isChatListCollapsed.value = !isChatListCollapsed.value
}

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
// 在选择会话的函数中也确保数据格式正确
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
      { content: '你好！我是AI心理助手"沙包"，很高兴和你一起聊天😊', role: 'assistant', htime: new Date() }
    ];
  }
  await scrollToBottom();
}

// 发送消息
async function sendMessage() {
  if (!store.state.isLoggedIn) {
    alert("请先登录后再发送消息");
    return;
  }
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
        // AI回复成功，删除临时消息标记
        userMessage.tempId = null;

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
          content: "沙包出了点小问题，后面再试试吧~😀",
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

.chat-background {
  background-image: url('@/assets/imgs/background4.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
}


::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f9f9f9;
}

::-webkit-scrollbar-thumb {
  background-color: #f9f9f9;
  border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
  background-color: #f9f9f9;
}

/* 加载动画样式 */
@keyframes bounce {
  0%, 80%, 100% { transform: scale(0.0) }
  40% { transform: scale(1.0) }
}

.animate-bounce {
  animation: bounce 1.4s infinite ease-in-out both;
}

/* 添加过渡效果 */
.rotate-90 {
  transform: rotate(90deg);
}
/* Markdown 样式 */
.message-content {
  word-break: break-word;
}

.message-content p {
  margin-bottom: 0.5rem;
}

.message-content strong {
  font-weight: 600;
  color: #1a365d;  /* 深蓝色，让粗体更醒目 */
}

.message-content em {
  font-style: italic;
}

.message-content h3 {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 1rem 0 0.5rem;
  color: #2d3748;
}

.message-content ul {
  list-style-type: disc;
  margin: 0.5rem 0 0.5rem 1.5rem;
}

.message-content ol {
  list-style-type: decimal;
  margin: 0.5rem 0 0.5rem 1.5rem;
}

</style>