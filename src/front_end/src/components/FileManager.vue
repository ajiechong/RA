<template>
  <div class="file-manager">
    <div class="file-manager-header">
      <h3>文件管理</h3>
      <button @click="refreshFiles" class="refresh-btn">刷新</button>
    </div>

    <div class="file-filters">
      <select v-model="filterType" @change="filterFiles">
        <option value="">所有类型</option>
        <option value="pdf">PDF</option>
        <option value="docx">Word</option>
        <option value="txt">文本</option>
        <option value="image">图片</option>
      </select>

      <input
          v-model="searchKeyword"
          @input="filterFiles"
          placeholder="搜索文件名..."
          class="search-input"
      />
    </div>

    <div class="file-list">
      <div
          v-for="file in filteredFiles"
          :key="file.fid"
          class="file-item"
          @click="selectFile(file)"
          :class="{ active: selectedFile?.fid === file.fid }"
      >
        <div class="file-icon-wrapper">
          <i :class="getFileIcon(file.fileType)"></i>
        </div>

        <div class="file-info">
          <div class="file-name">{{ file.fileName }}</div>
          <div class="file-meta">
            {{ formatFileSize(file.fileSize) }} •
            {{ formatDate(file.uploadTime) }}
          </div>
        </div>

        <div class="file-actions">
          <button @click.stop="downloadFile(file)" class="action-btn">
            <i class="fas fa-download"></i>
          </button>
          <button @click.stop="deleteFile(file)" class="action-btn delete">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- 文件详情面板 -->
    <div v-if="selectedFile" class="file-details">
      <h4>文件详情</h4>
      <div class="detail-item">
        <span class="label">文件名:</span>
        <span class="value">{{ selectedFile.fileName }}</span>
      </div>
      <div class="detail-item">
        <span class="label">类型:</span>
        <span class="value">{{ selectedFile.fileType.toUpperCase() }}</span>
      </div>
      <div class="detail-item">
        <span class="label">大小:</span>
        <span class="value">{{ formatFileSize(selectedFile.fileSize) }}</span>
      </div>
      <div class="detail-item">
        <span class="label">上传时间:</span>
        <span class="value">{{ formatDate(selectedFile.uploadTime) }}</span>
      </div>

      <div v-if="selectedFile.extractedText" class="extracted-text">
        <h5>提取的文本内容:</h5>
        <div class="text-preview">
          {{ selectedFile.extractedText.substring(0, 500) }}
          <span v-if="selectedFile.extractedText.length > 500">...</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import axios from 'axios'
import { API_BASE_URL } from '@/utils/api'

// eslint-disable-next-line no-undef
const props = defineProps({
  uid: {
    type: Number,
    required: true
  }
})

// 状态
const allFiles = ref([])
const selectedFile = ref(null)
const filterType = ref('')
const searchKeyword = ref('')
const isLoading = ref(false)

// 计算属性
const filteredFiles = computed(() => {
  let files = allFiles.value

  // 类型过滤
  if (filterType.value) {
    if (filterType.value === 'image') {
      files = files.filter(f => ['jpg', 'jpeg', 'png', 'gif', 'bmp'].includes(f.fileType))
    } else {
      files = files.filter(f => f.fileType === filterType.value)
    }
  }

  // 关键词搜索
  if (searchKeyword.value) {
    files = files.filter(f =>
        f.fileName.toLowerCase().includes(searchKeyword.value.toLowerCase())
    )
  }

  return files
})

// 方法
const loadFiles = async () => {
  isLoading.value = true
  try {
    const response = await axios.get(`${API_BASE_URL}/api/files/user/${props.uid}`)
    allFiles.value = response.data
  } catch (error) {
    console.error('加载文件失败:', error)
  } finally {
    isLoading.value = false
  }
}

const refreshFiles = () => {
  loadFiles()
}

const filterFiles = () => {
  // 触发计算属性更新
  selectedFile.value = null
}

const selectFile = (file) => {
  selectedFile.value = file
}

const downloadFile = (file) => {
  window.open(file.ossUrl, '_blank')
}

const deleteFile = async (file) => {
  if (!confirm(`确定要删除文件 "${file.fileName}" 吗？`)) {
    return
  }

  try {
    await axios.delete(`${API_BASE_URL}/api/files/${file.fid}`)
    allFiles.value = allFiles.value.filter(f => f.fid !== file.fid)
    if (selectedFile.value?.fid === file.fid) {
      selectedFile.value = null
    }
  } catch (error) {
    console.error('删除文件失败:', error)
    alert('删除文件失败')
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

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleString('zh-CN')
}

// 生命周期
onMounted(() => {
  loadFiles()
})
</script>

<style scoped>
.file-manager {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.file-manager-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #eee;
  background: #f8f9fa;
}

.file-manager-header h3 {
  margin: 0;
  color: #333;
}

.refresh-btn {
  padding: 0.5rem 1rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.file-filters {
  display: flex;
  gap: 1rem;
  padding: 1rem;
  border-bottom: 1px solid #eee;
}

.file-filters select,
.search-input {
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-input {
  flex: 1;
}

.file-list {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem;
}

.file-item {
  display: flex;
  align-items: center;
  padding: 0.75rem;
  margin: 0.25rem 0;
  border: 1px solid #eee;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
}

.file-item:hover {
  background: #f0f0f0;
  border-color: #ccc;
}

.file-item.active {
  background: #e3f2fd;
  border-color: #2196f3;
}

.file-icon-wrapper {
  margin-right: 0.75rem;
}

.file-icon-wrapper i {
  font-size: 1.5rem;
}

.file-info {
  flex: 1;
}

.file-name {
  font-weight: 500;
  color: #333;
  margin-bottom: 0.25rem;
}

.file-meta {
  font-size: 0.8rem;
  color: #666;
}

.file-actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  padding: 0.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background: #6c757d;
  color: white;
  transition: background 0.2s;
}

.action-btn:hover {
  background: #5a6268;
}

.action-btn.delete {
  background: #dc3545;
}

.action-btn.delete:hover {
  background: #c82333;
}

.file-details {
  padding: 1rem;
  border-top: 1px solid #eee;
  background: #f8f9fa;
  max-height: 300px;
  overflow-y: auto;
}

.file-details h4 {
  margin: 0 0 1rem 0;
  color: #333;
}

.detail-item {
  display: flex;
  margin-bottom: 0.5rem;
}

.label {
  font-weight: 500;
  width: 80px;
  color: #666;
}

.value {
  color: #333;
}

.extracted-text {
  margin-top: 1rem;
}

.extracted-text h5 {
  margin: 0 0 0.5rem 0;
  color: #333;
}

.text-preview {
  background: white;
  padding: 0.75rem;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 0.9rem;
  line-height: 1.4;
  color: #555;
  white-space: pre-wrap;
  max-height: 150px;
  overflow-y: auto;
}
</style>