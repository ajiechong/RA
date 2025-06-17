<template>
    <div class="content-section">
        <!-- 左侧历史对话栏 -->
        <div class="history-section">
            <!-- AI头像和名称 -->
            <div class="p-4 flex items-center bg-transparent">
                <div class="w-10 h-10 rounded-full overflow-hidden shadow-lg">
                    <img :src="aiAvatar" alt="AI头像" class="w-full h-full object-cover" />
                </div>
                <NavigationMenu
                    v-bind:ref="menuRef"
                    :selected-option="currentPage"
                />
            </div>
            <div class="h-px bg-gray-200 mx-4 my-2"></div>

            <!-- 新建对话按钮固定在顶部 -->
            <div class="p-4 border-t border-gray-200">
                <button
                        @click="createNewChat"
                        class="mx-auto w-full flex items-center justify-center py-3 px-4 bg-blue-600 hover:bg-blue-700 text-white rounded-lg shadow-md transition-all"
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

                    <!-- 收藏对话标题和折叠按钮 -->
                    <div
                            class="px-6 py-3 flex items-center justify-between cursor-pointer hover:bg-gray-100 rounded-lg"
                            @click="toggleFavoriteList"
                    >
                        <div class="flex items-center space-x-2">
                            <svg class="w-4 h-4 text-yellow-500" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path>
                            </svg>
                            <span class="text-base font-medium text-gray-800">收藏对话</span>
                        </div>
                        <svg :class="{'rotate-180': !isFavoriteListCollapsed}" class="w-4 h-4 text-gray-500 transition-transform duration-200">
                            <path fill-rule="evenodd" d="M15.707 4.293a1 1 0 010 1.414L10.414 10l5.293 5.293a1 1 0 01-1.414 1.414L9 11.414a1 1 0 010-1.414l5.293-5.293a1 1 0 011.414 0z" clip-rule="evenodd"></path>
                        </svg>
                    </div>

                    <!-- 收藏对话列表 -->
                    <div v-show="!isFavoriteListCollapsed" class="space-y-3 px-4">
                        <div
                                v-for="chat in favoriteChatSessions"
                                :key="'fav-' + chat.cid"
                                class="flex items-center justify-between bg-transparent hover:bg-gray-100 rounded-lg p-3 transition-colors duration-200"
                        >
                            <!-- 聊天名称 -->
                            <button
                                    @click="selectChat(chat.cid)"
                                    class="flex-grow text-left truncate space-x-2 items-center"
                            >
                                <svg class="w-3 h-3 text-yellow-500 inline-block mr-2" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path>
                                </svg>
                                <span :class="{'text-blue-500 font-semibold': currentCid === chat.cid}">
          {{ chat.hname || '新对话' }}
        </span>
                            </button>

                            <!-- 三点菜单 -->
                            <div class="relative ml-2">
                                <button
                                        @click.stop="openFavoriteMenu(chat)"
                                        class="p-1 rounded-full hover:bg-gray-200 transition-colors"
                                >
                                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 text-gray-600">
                                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 15.75L3 12m0 3.75l3-3.75m6 0L9 12m0 3.75l3-3.75M18 12a4.5 4.5 0 10-9 0 4.5 4.5 0 009 0z"></path>
                                    </svg>
                                </button>

                                <!-- 下拉菜单（收藏对话的菜单） -->
                                <div
                                        v-if="activeFavoriteMenu === chat.cid"
                                        class="absolute right-0 mt-2 w-40 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 z-50"
                                >
                                    <div class="py-1">
                                        <button
                                                @click.stop="unfavoriteChat(chat)"
                                                class="block w-full px-4 py-2 text-sm text-yellow-600 hover:bg-yellow-50 hover:text-yellow-700"
                                        >
                                            取消收藏
                                        </button>
                                    </div>
                                </div>


                            </div>
                        </div>

                        <!-- 收藏为空时的提示 -->
                        <div v-if="favoriteChatSessions.length === 0" class="text-center text-gray-500 py-4">
                            暂无收藏对话
                        </div>
                    </div>

                    <!-- 在这里添加分隔线 -->
                    <div class="h-px bg-gray-200 mx-4 my-4"></div>

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
                                :style="{
      'background-color': currentCid === chat.cid ? '#cbe2ef' : '#e8e9ea'
    }"
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
                                <span :class="{'text-black-500 font-semibold': currentCid === chat.cid}">
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

                                        <!-- 新增收藏按钮 -->
                                        <button
                                                @click="favoriteChat(selectedChat)"
                                                v-if="!isChatFavorited(selectedChat?.cid)"
                                                class="block w-full px-4 py-2 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600"
                                        >
                                            收藏
                                        </button>

                                        <!-- 如果已收藏，显示取消收藏按钮 -->
                                        <button
                                                @click="unfavoriteChat(selectedChat)"
                                                v-else
                                                class="block w-full px-4 py-2 text-sm text-gray-700 hover:bg-blue-50 hover:text-blue-600"
                                        >
                                            取消收藏
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

                        <!-- 如果消息包含文件，显示文件信息 -->
                        <div v-if="msg.files && msg.files.length > 0" class="message-files">
                            <div v-for="file in msg.files" :key="file.fid" class="file-item">
                                <div class="file-info">
                                    <i :class="getFileIcon(file.fileType)" class="file-icon"></i>
                                    <span class="file-name">{{ file.fileName }}</span>
                                    <span class="file-size">{{ formatFileSize(file.fileSize) }}</span>
                                    <button @click="removeFile(file.fid)" class="remove-file-btn">×</button>
                                </div>
                            </div>
                        </div>

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
                                    class="px-1 py-1 rounded-lg text-gray-800 rounded-tl-none mr-auto message-content"
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

            <!-- 在输入区域前添加文件预览区域 -->
            <div v-if="uploadedFiles.length > 0" class="file-preview-area">
                <div class="file-preview-header">
                    <span>已上传文件 ({{ uploadedFiles.length }})</span>
                    <button @click="clearAllFiles" class="clear-all-btn">清空所有</button>
                </div>
                <div class="file-preview-list">
                    <div v-for="file in uploadedFiles" :key="file.fid" class="preview-file-item">
                        <i :class="getFileIcon(file.fileType)" class="file-icon"></i>
                        <div class="file-details">
                            <span class="file-name">{{ file.fileName }}</span>
                            <span class="file-info">{{ file.fileType.toUpperCase() }} • {{ formatFileSize(file.fileSize) }}</span>
                        </div>
                        <button @click="removeUploadedFile(file.fid)" class="remove-btn">删除</button>
                    </div>
                </div>
            </div>


            <!--   底部输入区域-->
            <div class="chat-input">
                <!-- 文件上传按钮 -->
                <div class="file-upload-wrapper">
                    <input
                            ref="fileInput"
                            type="file"
                            @change="handleFileUpload"
                            multiple
                            accept=".pdf,.docx,.doc,.txt,.jpg,.jpeg,.png,.gif,.bmp"
                            style="display: none"
                    />
                    <button
                            @click="$refs.fileInput.click()"
                            class="file-upload-btn"
                            :disabled="isLoading || isTyping"
                            title="上传文件"
                    >
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13"></path>
                        </svg>
                    </button>
                </div>
                <input
                        v-model="inputMessage"
                        type="text"
                        placeholder="输入消息..."
                        @keyup.enter="sendMessageLi"
                        :disabled="isLoading || isTyping"
                >
                <button
                        @click="sendMessageLi"
                        :disabled="!inputMessage.trim() || isLoading || isTyping"
                >
                    发送
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import aiAvatar from '@/assets/imgs/icon/ai-avatar.svg';
import {ref, onMounted, nextTick, watch} from 'vue'
import axios from 'axios'
import { API_BASE_URL } from '@/utils/api';
// import { useRouter } from 'vue-router'
// import { useStore } from 'vuex'
import TypingMessage from '@/components/TypingMessage.vue';
import { marked } from 'marked';
import DOMPurify from 'dompurify';

import NavigationMenu from "@/components/NavigationMenu.vue";

// 状态定义
// const router = useRouter()
// const store = useStore()
const uploadedFiles = ref([])  // 当前会话已上传的文件
const isUploading = ref(false)  // 文件上传状态
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

const favoriteChatSessions = ref([])  // 收藏的对话列表
const isFavoriteListCollapsed = ref(false)  // 收藏对话折叠状态

// 从本地存储获取用户ID，实际应用中应该从用户认证系统获取
// const uid = ref(store.state.isLoggedIn ? parseInt(store.getters.uid) : null)

//测试使用固定的uid
const uid = ref(1);
const activeFavoriteMenu = ref(null)

// API 基础URL
const API_BASE_URL0 = '/api/chathistories'

// 预填充测试数据（添加到状态定义处）
chatSessions.value = [
    { cid: 1, hname: '学术论文润色', unread: true },
    { cid: 2, hname: '实验设计讨论', unread: false },
    { cid: 3, hname: '代码调试记录', unread: true }
];
const menuRef = ref(null);
const currentPage = ref('Literature'); // 初始选中项

// 初始化加载
onMounted(async () => {
    ////////  ignore log
    // if (!store.state.isLoggedIn) {
    //   alert("请先登录后再使用聊天功能");
    //   router.push('/');
    //   return;
    // }
    await loadChatSessions();
    // 页面挂载后，直接设置菜单选中项
});

// 菜单相关方法
const openMenu = (chat) => {
    // 确保selectedChat被正确设置
    selectedChat.value = chat;
    activeMenu.value = activeMenu.value === chat.cid ? null : chat.cid;
};



// 新增收藏菜单处理函数
const openFavoriteMenu = (chat) => {
    selectedChat.value = chat;
    activeFavoriteMenu.value = activeFavoriteMenu.value === chat.cid ? null : chat.cid;
};

// 修改原有的点击事件监听
onMounted(() => {
    document.addEventListener('click', () => {
        activeMenu.value = null;
        activeFavoriteMenu.value = null; // 添加这行
    });
});

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

// 加载收藏的对话
async function loadFavoriteChatSessions() {
    try {
        const response = await axios.get(`${API_BASE_URL0}/favorites/${uid.value}`);
        favoriteChatSessions.value = response.data.map(session => ({
            ...session,
            hname: session.hname || '新对话'
        }));
    } catch (error) {
        console.error('Failed to load favorite chat sessions:', error);
        if (error.response?.status === 404) {
            favoriteChatSessions.value = [];
        }
    }
}

// 收藏对话 - 修复null引用问题
const favoriteChat = async (chat) => {
    // 确保chat对象存在
    if (!chat || !chat.cid) {
        console.error('Chat object is null or missing cid');
        alert('收藏失败：对话信息不完整');
        return;
    }

    try {
        const response = await axios.post(`${API_BASE_URL0}/favorites`, {
            uid: uid.value,
            cid: chat.cid
        });

        if (response.data.success) {
            // 更新本地收藏列表
            await loadFavoriteChatSessions();
            alert('收藏成功！');
        } else {
            alert(response.data.message || '收藏失败');
        }
    } catch (error) {
        console.error('收藏失败:', error);
        alert('收藏失败，请稍后重试');
    } finally {
        activeMenu.value = null;
    }
};

// 取消收藏 - 修复null引用问题
const unfavoriteChat = async (chat) => {
    // 确保chat对象存在
    if (!chat || !chat.cid) {
        console.error('Chat object is null or missing cid');
        alert('取消收藏失败：对话信息不完整');
        return;
    }

    try {
        const response = await axios.delete(`${API_BASE_URL0}/favorites/${uid.value}/${chat.cid}`);

        if (response.data.success) {
            // 更新本地收藏列表
            await loadFavoriteChatSessions();
            alert('已取消收藏');
        } else {
            alert(response.data.message || '取消收藏失败');
        }
    } catch (error) {
        console.error('取消收藏失败:', error);
        alert('取消收藏失败，请稍后重试');
    } finally {
        activeMenu.value = null;
    }
};

// 切换收藏列表折叠状态
const toggleFavoriteList = () => {
    isFavoriteListCollapsed.value = !isFavoriteListCollapsed.value;
};

// 检查对话是否已收藏
const isChatFavorited = (cid) => {
    if (!cid) return false;
    return favoriteChatSessions.value.some(chat => chat.cid === cid);
};

// 修改 onMounted 函数，添加加载收藏对话
onMounted(async () => {
    await loadChatSessions();
    await loadFavoriteChatSessions();  // 新增这行
});



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

    // 添加这一行来清空文件
    uploadedFiles.value = [];
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
            { content: '你好！我是您专属的科研助手RA，最近有什么感兴趣的方向吗？欢迎和我一起进行头脑风暴～🧠⛈️', role: 'assistant', htime: new Date() }
        ];
    }
    await scrollToBottom();

    // 添加这一行来加载文件
    await loadSessionFiles();
}



// 发送消息
async function sendMessageLi() {
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
            //更改
            const response = await axios.post(
                // 修正路径参数：仅传递 role（this.agentId），chatId 和 userId 作为查询参数
                `http://localhost:8082/api/chat/literature_analyst`,
                {
                    query: messageContent, // 必填参数
                    // 可选：若后端需要在请求体中传递 userId 和 chatId（根据后端接口而定）
                    // userId: this.userId,
                    // chatId: this.chatId,
                },
                {
                    // 传递 chatId 和 userId 作为查询参数（推荐方式）
                    params: {
                        chatId: parseInt(currentCid.value),
                        userId:  parseInt(uid.value),
                        // shouldSave: currentTry === 0,
                    },
                    headers: {
                        'Content-Type': 'application/json', // 确保请求体为 JSON 格式
                    },
                    timeout : 30000,
                }
            );
            console.log(response)
            console.log(response.data.response)
            if (response.data.response) {
                // 找到临时消息并更新（避免直接修改数组索引）
                const tempMsgIndex = messages.value.findIndex(msg => msg.tempId === userMessage.tempId);
                if (tempMsgIndex !== -1) {
                    messages.value[tempMsgIndex].tempId = null; // 标记为已保存
                }

                const aiMessage = {
                    content: response.data.response,
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


// 文件上传处理
const handleFileUpload = async (event) => {
    const files = Array.from(event.target.files)
    if (files.length === 0) return

    if (!currentCid.value) {
        await createNewChat()
    }

    isUploading.value = true

    try {
        for (const file of files) {
            await uploadSingleFile(file)
        }
        event.target.value = ''
        await loadSessionFiles()
    } catch (error) {
        console.error('文件上传失败:', error)
        alert('文件上传失败，请重试')
    } finally {
        isUploading.value = false
    }
}

const uploadSingleFile = async (file) => {
    if (file.size > 50 * 1024 * 1024) {
        throw new Error(`文件 ${file.name} 超过50MB限制`)
    }

    const allowedTypes = ['pdf', 'docx', 'doc', 'txt', 'jpg', 'jpeg', 'png', 'gif', 'bmp']
    const fileType = file.name.split('.').pop().toLowerCase()
    if (!allowedTypes.includes(fileType)) {
        throw new Error(`不支持的文件类型: ${fileType}`)
    }

    const formData = new FormData()
    formData.append('file', file)
    formData.append('cid', currentCid.value)
    formData.append('uid', uid.value)

    const response = await axios.post(`${API_BASE_URL}/api/files/upload`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })

    if (!response.data.success) {
        throw new Error(response.data.message || '上传失败')
    }

    return response.data.data
}

const loadSessionFiles = async () => {
    if (!currentCid.value) return

    try {
        const response = await axios.get(`${API_BASE_URL}/api/files/session/${currentCid.value}`)
        uploadedFiles.value = response.data
    } catch (error) {
        console.error('加载文件失败:', error)
    }
}

const removeUploadedFile = async (fid) => {
    try {
        await axios.delete(`${API_BASE_URL}/api/files/${fid}`)
        uploadedFiles.value = uploadedFiles.value.filter(f => f.fid !== fid)
    } catch (error) {
        console.error('删除文件失败:', error)
        alert('删除文件失败')
    }
}

const clearAllFiles = async () => {
    if (confirm('确定要删除所有已上传的文件吗？')) {
        try {
            for (const file of uploadedFiles.value) {
                await axios.delete(`${API_BASE_URL}/api/files/${file.fid}`)
            }
            uploadedFiles.value = []
        } catch (error) {
            console.error('清空文件失败:', error)
            alert('清空文件失败')
        }
    }
}

const getFileIcon = (fileType) => {
    const iconMap = {
        'pdf': 'fas fa-file-pdf text-red-500',
        'docx': 'fas fa-file-word text-blue-500',
        'doc': 'fas fa-file-word text-blue-500',
        'txt': 'fas fa-file-alt text-gray-500',
        'jpg': 'fas fa-file-image text-green-500',
        'jpeg': 'fas fa-file-image text-green-500',
        'png': 'fas fa-file-image text-green-500',
        'gif': 'fas fa-file-image text-green-500',
        'bmp': 'fas fa-file-image text-green-500'
    }
    return iconMap[fileType] || 'fas fa-file text-gray-400'
}

const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}


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
    margin-top: 10px;
    margin-left: 135px;
    display: flex;
    padding: 1rem;
    gap: 1rem;
    height: calc(100vh - 200px); /* 除去 AppNavBar 高度 */
    box-sizing: border-box;
    overflow: hidden;
}

/* 左侧历史对话栏 */
.history-section {
    border-radius: 8px;
    background: #f4f4f4;
    color: black;   /* 字体颜色 */
    backdrop-filter: blur(10px);
    border: 2px solid rgba(255, 255, 255, 0.2);
    width: 260px;
}


/* 对话列表项样式 */
.history-section .chat-item {
    @apply px-4 py-3 transition-all rounded-xl;
    margin: 0 8px;
    border: 1px solid transparent; /* 点击时显示边框 */
}

.history-section .chat-item:hover {
    background: #b8dced; /* 悬停浅色背景 */
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
    background-color: #3574ab;
}

/* 中间聊天区域 */
.chat-container {
    width: 70%;
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
    margin-top: 0.5rem;
    display: flex;
    flex-direction: column; /* 改为垂直布局 */
    margin-left: 1rem;
    gap: 1rem; /* 调整间距 */
}

.sidebar-item {
    background-color: rgba(219, 218, 218, 0.35);
    border: 1px solid rgba(255, 255, 255, 0.2);
    padding: 1rem;
    border-radius: 8px;
    width: 75%; /* 宽度改为100%以填满容器 */
    color: white;
    text-align: center;
    backdrop-filter: blur(6px);
    transition: transform 0.3s ease;
    height: 55px; /* 高度自动调整 */
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

/* 文件相关样式 */
.file-upload-wrapper {
    display: flex;
    align-items: center;
    margin-right: 0.5rem;
}

.file-upload-btn {
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 6px;
    background: white;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s;
}

.file-upload-btn:hover:not(:disabled) {
    background: #f0f0f0;
    border-color: #999;
}

.file-upload-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
}

.file-preview-area {
    margin: 1rem;
    padding: 1rem;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    background: #f9f9f9;
}

.file-preview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 0.5rem;
    font-weight: 500;
}

.clear-all-btn {
    padding: 0.25rem 0.5rem;
    background: #dc3545;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.8rem;
}

.file-preview-list {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
}

.preview-file-item {
    display: flex;
    align-items: center;
    padding: 0.5rem;
    background: white;
    border-radius: 6px;
    border: 1px solid #ddd;
}

.file-icon {
    margin-right: 0.75rem;
    font-size: 1.2rem;
}

.file-details {
    flex: 1;
    display: flex;
    flex-direction: column;
}

.file-name {
    font-weight: 500;
    color: #333;
}

.file-info {
    font-size: 0.8rem;
    color: #666;
}

.remove-btn {
    padding: 0.25rem 0.5rem;
    background: #6c757d;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 0.8rem;
}

.remove-btn:hover {
    background: #5a6268;
}

.message-files {
    margin-bottom: 0.5rem;
}

.file-item {
    display: inline-block;
    margin: 0.25rem;
}

.file-info {
    display: flex;
    align-items: center;
    padding: 0.5rem;
    background: #f0f0f0;
    border-radius: 6px;
    font-size: 0.9rem;
}

.remove-file-btn {
    margin-left: 0.5rem;
    padding: 0.25rem 0.5rem;
    background: #dc3545;
    color: white;
    border: none;
    border-radius: 3px;
    cursor: pointer;
}

</style>
