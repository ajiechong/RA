<template>
  <div class="survey-container">
    <div class="main-content">
      <div class="survey-area">
        <!-- 标题区域 -->
        <div class="survey-header">
          <h3>🔬 5阶段文献调研工作流</h3>
          <div class="header-buttons">
            <button class="restart-btn" @click="showRestartModal = true">
              重新开始
            </button>
          </div>
        </div>

        <!-- 工作流进度显示 -->
        <div class="workflow-progress">
          <div class="progress-title">
            <h4>📋 5阶段调研进度跟踪</h4>
            <span class="progress-info">
              {{ currentStageIndex + 1 }} / 5 阶段
            </span>
          </div>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: progressPercentage + '%' }"></div>
          </div>
          <div class="stages-flow">
            <div
                v-for="(stage, index) in surveyStages"
                :key="index"
                class="stage-node"
                :class="{
                'stage-completed': index < currentStageIndex,
                'stage-current': index === currentStageIndex,
                'stage-waiting': index === currentStageIndex && isWaitingForApproval,
                'stage-pending': index > currentStageIndex
              }"
            >
              <div class="stage-icon">
                <span v-if="index < currentStageIndex">✅</span>
                <span v-else-if="index === currentStageIndex && isWaitingForApproval">⏳</span>
                <span v-else-if="index === currentStageIndex">🔄</span>
                <span v-else>⭕</span>
              </div>
              <div class="stage-info">
                <div class="stage-name">{{ stage.name }}</div>
                <div class="stage-agent">{{ stage.agent }}</div>
                <div class="stage-desc">{{ stage.description }}</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 对话消息区域 -->
        <div class="messages-area" ref="messageContainer">
          <!-- 欢迎信息 -->
          <div v-if="messages.length === 0" class="welcome-section">
            <div class="welcome-icon">🔬</div>
            <h3>5阶段文献调研智能助手</h3>
            <p>真正的autogen智能体协作 - 按照 <strong>5阶段专业化流程</strong> 执行高质量文献调研</p>

            <div class="workflow-explanation">
              <h4>🎯 5阶段调研流程说明：</h4>
              <div class="flow-steps">
                <div class="flow-step">
                  <span class="step-number">1</span>
                  <div class="step-content">
                    <strong>🎯 调研策略制定</strong> (SurveyDirector)
                    <p>深度分析研究主题，制定系统化检索策略和关键词体系</p>
                  </div>
                </div>
                <div class="flow-step">
                  <span class="step-number">2</span>
                  <div class="step-content">
                    <strong>🔍 论文检索获取</strong> (PaperRetriever)
                    <p>多轮系统化检索，获取25-30篇高质量学术论文</p>
                  </div>
                </div>
                <div class="flow-step">
                  <span class="step-number">3</span>
                  <div class="step-content">
                    <strong>📊 深度论文分析</strong> (PaperAnalyzer)
                    <p>逐篇深度分析，提取核心贡献和技术方法</p>
                  </div>
                </div>
                <div class="flow-step">
                  <span class="step-number">4</span>
                  <div class="step-content">
                    <strong>🔗 知识综合整合</strong> (KnowledgeSynthesizer)
                    <p>跨文献知识整合，构建统一理论框架</p>
                  </div>
                </div>
                <div class="flow-step">
                  <span class="step-number">5</span>
                  <div class="step-content">
                    <strong>📝 综述报告生成</strong> (ReportGenerator)
                    <p>生成6000-8000词完整学术综述报告</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="interaction-guide">
              <h4>💡 交互说明：</h4>
              <ul>
                <li>🔄 <strong>5阶段顺序执行</strong>：专业智能体按固定顺序完成各自任务</li>
                <li>⏸️ <strong>阶段确认机制</strong>：每个阶段完成后等待您的确认</li>
                <li>✅ <strong>继续/重做选择</strong>：您可以确认继续或要求重新执行</li>
                <li>📝 <strong>指导调整功能</strong>：提供具体意见指导智能体调整工作</li>
                <li>🎓 <strong>高质量产出</strong>：最终生成6000-8000词专业学术综述报告</li>
              </ul>
            </div>

            <div class="quality-features">
              <h4>🌟 核心优势：</h4>
              <div class="features-grid">
                <div class="feature-item">
                  <span class="feature-icon">🎯</span>
                  <div class="feature-text">
                    <strong>精准策略制定</strong>
                    <p>AI智能分析研究主题</p>
                  </div>
                </div>
                <div class="feature-item">
                  <span class="feature-icon">🔍</span>
                  <div class="feature-text">
                    <strong>智能化检索</strong>
                    <p>多源高质量论文获取</p>
                  </div>
                </div>
                <div class="feature-item">
                  <span class="feature-icon">📊</span>
                  <div class="feature-text">
                    <strong>专业学术分析</strong>
                    <p>深度提取核心贡献</p>
                  </div>
                </div>
                <div class="feature-item">
                  <span class="feature-icon">🔗</span>
                  <div class="feature-text">
                    <strong>系统知识整合</strong>
                    <p>构建统一理论框架</p>
                  </div>
                </div>
                <div class="feature-item">
                  <span class="feature-icon">📝</span>
                  <div class="feature-text">
                    <strong>高质量报告</strong>
                    <p>符合期刊标准综述</p>
                  </div>
                </div>
              </div>
            </div>

            <p class="start-hint">请输入您的研究主题，开始5阶段智能文献调研 👇</p>
          </div>

          <!-- 对话消息 -->
          <div
              v-for="(msg, index) in messages"
              :key="msg.id || index"
              class="message-item"
              :class="{
              'user-message': msg.role === 'user',
              'agent-message': msg.role === 'agent',
              'system-message': msg.role === 'system',
              'stage-approval-message': msg.type === 'stage_completion_request'
            }"
          >
            <!-- 消息头部 -->
            <div v-if="msg.role === 'agent'" class="message-header">
              <span class="agent-avatar">{{ getAgentIcon(msg.agent) }}</span>
              <div class="agent-info">
                <span class="agent-name">{{ msg.agent }}</span>
                <span class="agent-stage">{{ getAgentStage(msg.agent) }}</span>
              </div>
            </div>
            <div v-else-if="msg.role === 'system'" class="message-header">
              <span class="system-avatar">🤖</span>
              <span class="system-label">系统消息</span>
            </div>
            <div v-else-if="msg.type === 'stage_completion_request'" class="message-header">
              <span class="approval-avatar">🎯</span>
              <span class="approval-label">阶段确认</span>
            </div>

            <!-- 消息内容 -->
            <div class="message-content" v-html="formatMessage(msg.content)"></div>

            <!-- 阶段确认操作区 -->
            <div v-if="msg.type === 'stage_completion_request'" class="approval-actions">
              <div class="stage-summary">
                <div class="summary-header">
                  <strong>{{ getStageDisplayName(msg.stage_info?.stage_name) }}</strong>
                  <span class="stage-badge">{{ msg.stage_info?.agent_name }}</span>
                </div>
                <div class="summary-progress">
                  第 {{ msg.stage_info?.stage_index + 1 }} / {{ msg.stage_info?.total_stages }} 阶段已完成
                </div>
              </div>
              <div class="action-buttons">
                <button @click="handleStageAction('APPROVE')" class="action-btn approve-btn">
                  ✅ 确认继续
                </button>
                <button @click="handleStageAction('REGENERATE')" class="action-btn regenerate-btn">
                  🔄 重新执行
                </button>
                <button @click="handleStageAction('END')" class="action-btn end-btn">
                  🏁 提前结束
                </button>
              </div>
            </div>
          </div>

          <!-- 当前等待确认提示 -->
          <div v-if="isWaitingForApproval && currentStageInfo" class="current-approval-prompt">
            <div class="approval-card">
              <div class="approval-icon">⏳</div>
              <h4>等待阶段确认</h4>
              <div class="current-stage-info">
                <div class="stage-title">{{ currentStageInfo.name }}</div>
                <div class="stage-agent">执行者：{{ currentStageInfo.agent }}</div>
                <div class="stage-description">{{ currentStageInfo.description }}</div>
              </div>
              <div class="approval-options">
                <p><strong>请选择操作：</strong></p>
                <div class="quick-options">
                  <button @click="handleStageAction('APPROVE')" class="quick-btn approve">
                    ✅ 继续下一阶段
                  </button>
                  <button @click="handleStageAction('REGENERATE')" class="quick-btn regenerate">
                    🔄 重新执行当前阶段
                  </button>
                </div>
                <p class="custom-hint">💡 或在下方输入具体的调整意见</p>
              </div>
            </div>
          </div>
        </div>

        <!-- 消息输入区域 -->
        <div class="input-section" :class="{ 'approval-mode': isWaitingForApproval }">
          <div v-if="isWaitingForApproval" class="input-status">
            <span class="status-icon">🎯</span>
            <span class="status-text">等待您确认 "{{ currentStageInfo?.name }}" 阶段</span>
          </div>
          <div class="input-controls">
            <input
                v-model="inputMessage"
                type="text"
                :placeholder="getInputPlaceholder()"
                @keyup.enter="handleSend"
                :disabled="isLoading"
                ref="messageInput"
                class="message-input"
            />
            <button
                @click="handleSend"
                :disabled="!inputMessage.trim() || isLoading"
                :class="{ 'approval-send': isWaitingForApproval }"
                class="send-button"
            >
              {{ isWaitingForApproval ? '确认' : '发送' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 重新开始确认弹窗 -->
    <div v-if="showRestartModal" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h4>重新开始确认</h4>
        </div>
        <div class="modal-body">
          <p>确认要重新开始5阶段文献调研吗？当前的所有进度和对话记录将被清空。</p>
        </div>
        <div class="modal-footer">
          <button @click="showRestartModal = false" class="cancel-btn">取消</button>
          <button @click="confirmRestart" class="confirm-btn">确认重新开始</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, nextTick, computed } from 'vue';

// 5阶段文献调研流程
const surveyStages = ref([
  {
    name: '调研策略制定',
    agent: 'SurveyDirector',
    description: '深度分析研究主题，制定系统化检索策略'
  },
  {
    name: '论文检索获取',
    agent: 'PaperRetriever',
    description: '多轮系统化检索，获取高质量学术论文'
  },
  {
    name: '深度论文分析',
    agent: 'PaperAnalyzer',
    description: '逐篇深度分析，提取核心贡献和技术方法'
  },
  {
    name: '知识综合整合',
    agent: 'KnowledgeSynthesizer',
    description: '跨文献知识整合，构建统一理论框架'
  },
  {
    name: '综述报告生成',
    agent: 'ReportGenerator',
    description: '生成6000-8000词完整学术综述报告'
  }
]);

const messages = ref([]);
const inputMessage = ref('');
const isLoading = ref(false);
const showRestartModal = ref(false);
const messageId = ref(1);
const isWaitingForApproval = ref(false);
const currentStageIndex = ref(-1);
const messageInput = ref(null);

let ws = null;

// 计算进度百分比
const progressPercentage = computed(() => {
  if (currentStageIndex.value < 0) return 0;
  return Math.round(((currentStageIndex.value) / surveyStages.value.length) * 100);
});

// 当前阶段信息
const currentStageInfo = computed(() => {
  if (currentStageIndex.value >= 0 && currentStageIndex.value < surveyStages.value.length) {
    return surveyStages.value[currentStageIndex.value];
  }
  return null;
});

// 5阶段智能体图标映射
const agentIcons = {
  'SurveyDirector': '🎯',
  'PaperRetriever': '🔍',
  'PaperAnalyzer': '📊',
  'KnowledgeSynthesizer': '🔗',
  'ReportGenerator': '📝'
};

// 获取智能体图标
const getAgentIcon = (agentName) => {
  return agentIcons[agentName] || '🤖';
};

// 获取智能体阶段信息
const getAgentStage = (agentName) => {
  const stage = surveyStages.value.find(s => s.agent === agentName);
  return stage ? stage.name : '';
};

// 5阶段显示名称映射
const getStageDisplayName = (stageName) => {
  const stageMap = {
    '🎯 调研策略制定': '调研策略制定',
    '🔍 论文检索获取': '论文检索获取',
    '📊 深度论文分析': '深度论文分析',
    '🔗 知识综合整合': '知识综合整合',
    '📝 综述报告生成': '综述报告生成',
    // 向后兼容
    '任务分配阶段': '调研策略制定',
    '论文获取阶段': '论文检索获取',
    '单篇摘要阶段': '深度论文分析',
    '综述报告阶段': '综述报告生成'
  };
  return stageMap[stageName] || stageName;
};

// 获取输入框占位符
const getInputPlaceholder = () => {
  if (isWaitingForApproval.value && currentStageInfo.value) {
    return `请确认 "${currentStageInfo.value.name}" 阶段或输入调整意见...`;
  }
  return '请输入您的研究主题开始5阶段文献调研...';
};

// 格式化消息内容
const formatMessage = (content) => {
  if (typeof content !== 'string') {
    content = String(content);
  }

  // 转换换行符为HTML
  let formatted = content.replace(/\n/g, '<br>');

  // 转换markdown样式的粗体
  formatted = formatted.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

  // 转换markdown样式的标题
  formatted = formatted.replace(/^### (.*$)/gm, '<h4>$1</h4>');
  formatted = formatted.replace(/^## (.*$)/gm, '<h3>$1</h3>');
  formatted = formatted.replace(/^# (.*$)/gm, '<h2>$1</h2>');

  return formatted;
};

// 处理阶段操作
const handleStageAction = (action) => {
  inputMessage.value = action;
  handleSend();
};

// 添加消息
const addMessage = (role, agent, content, type = null, stageInfo = null) => {
  messages.value.push({
    id: messageId.value++,
    role,
    agent,
    content,
    type,
    stage_info: stageInfo,
    timestamp: new Date()
  });
  scrollToBottom();
};

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    const container = document.querySelector('.messages-area');
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

  const wsUrl = 'ws://localhost:8000/ws/survey';
  ws = new WebSocket(wsUrl);

  ws.onopen = () => {
    console.log('5阶段文献调研 WebSocket 已连接');
    isLoading.value = false;
    isWaitingForApproval.value = false;
  };

  ws.onmessage = (event) => {
    console.log('收到5阶段工作流消息:', event.data);
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
        isWaitingForApproval.value = false;
        break;

      case 'agent_message':
        addMessage('agent', name, content);
        isWaitingForApproval.value = false;
        // 更新当前阶段索引
        updateCurrentStageIndex(name);
        break;

      case 'user_message':
        break;

      case 'stage_completion_request':
        addMessage('system', 'system', content, 'stage_completion_request', msg.stage_info);
        isWaitingForApproval.value = true;
        if (msg.stage_info) {
          currentStageIndex.value = msg.stage_info.stage_index;
        }
        break;

      case 'workflow_started':
        addMessage('system', 'system', content);
        currentStageIndex.value = 0;
        isWaitingForApproval.value = false;
        break;

      case 'workflow_completed':
        addMessage('system', 'system', content);
        isWaitingForApproval.value = false;
        currentStageIndex.value = surveyStages.value.length;
        break;

      case 'error':
        addMessage('system', 'system', `❌ 错误: ${content}`);
        isWaitingForApproval.value = false;
        break;

      default:
        console.warn('未知消息类型:', msg);
        addMessage('system', 'system', content);
    }

    isLoading.value = false;
  };

  ws.onerror = (error) => {
    console.error('5阶段工作流 WebSocket 出错:', error);
    isLoading.value = false;
    isWaitingForApproval.value = false;
  };

  ws.onclose = () => {
    console.log('5阶段工作流 WebSocket 已断开，尝试重连...');
    isLoading.value = false;
    isWaitingForApproval.value = false;
    setTimeout(connectWebSocket, 3000);
  };
};

// 更新当前阶段索引
const updateCurrentStageIndex = (agentName) => {
  const stageIndex = surveyStages.value.findIndex(stage => stage.agent === agentName);
  if (stageIndex >= 0) {
    currentStageIndex.value = stageIndex;
  }
};

onMounted(() => {
  connectWebSocket();
});

onBeforeUnmount(() => {
  if (ws) ws.close();
});

// 发送消息到服务器
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

// 处理发送
const handleSend = () => {
  if (!inputMessage.value.trim() || isLoading.value) return;

  addMessage('user', 'user', inputMessage.value);
  isLoading.value = true;
  isWaitingForApproval.value = false;
  sendMessageToServer(inputMessage.value);

  inputMessage.value = '';
};

// 确认重新开始
const confirmRestart = () => {
  showRestartModal.value = false;

  messages.value = [];
  inputMessage.value = '';
  isLoading.value = false;
  isWaitingForApproval.value = false;
  currentStageIndex.value = -1;
  messageId.value = 1;

  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.send(JSON.stringify({type: 'restart'}));
  }

  // 重新连接WebSocket
  setTimeout(connectWebSocket, 500);
};
</script>

<style scoped>
/* 整体容器 */
.survey-container {
  margin: 20px auto;
  height: calc(100vh - 40px);
  max-width: 1400px;
  display: flex;
  flex-direction: column;
  background-color: #f8fafc;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.main-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.survey-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  background-color: #ffffff;
  min-height: 0;
}

/* 头部样式 */
.survey-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-bottom: 1px solid #e2e8f0;
  flex-shrink: 0;
}

.survey-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.restart-btn {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
}

.restart-btn:hover {
  background-color: rgba(255, 255, 255, 0.3);
}

/* 工作流进度样式 */
.workflow-progress {
  padding: 20px 24px;
  background-color: #f8fafc;
  border-bottom: 2px solid #e2e8f0;
  flex-shrink: 0;
}

.progress-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.progress-title h4 {
  margin: 0;
  color: #1e293b;
  font-size: 16px;
}

.progress-info {
  color: #64748b;
  font-weight: 600;
  font-size: 14px;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background-color: #e2e8f0;
  border-radius: 4px;
  overflow: hidden;
  margin-bottom: 20px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea, #764ba2);
  transition: width 0.5s ease;
}

/* 5阶段流程显示 */
.stages-flow {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 12px;
  align-items: center;
  overflow-x: auto;
  padding: 8px 0;
}

.stage-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border-radius: 12px;
  background-color: white;
  border: 2px solid #e2e8f0;
  min-width: 140px;
  transition: all 0.3s ease;
  position: relative;
  text-align: center;
}

.stage-node.stage-completed {
  border-color: #10b981;
  background: linear-gradient(135deg, #f0fdf4, #dcfce7);
}

.stage-node.stage-current {
  border-color: #667eea;
  background: linear-gradient(135deg, #eff6ff, #dbeafe);
  animation: pulse 2s infinite;
}

.stage-node.stage-waiting {
  border-color: #f59e0b;
  background: linear-gradient(135deg, #fffbeb, #fef3c7);
  animation: glow 2s infinite alternate;
}

.stage-node.stage-pending {
  border-color: #d1d5db;
  background-color: #f9fafb;
}

.stage-icon {
  font-size: 20px;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.8);
}

.stage-info {
  flex: 1;
  text-align: center;
}

.stage-name {
  font-weight: 600;
  font-size: 12px;
  color: #1f2937;
  margin-bottom: 2px;
}

.stage-agent {
  font-size: 10px;
  color: #6b7280;
  margin-bottom: 4px;
}

.stage-desc {
  font-size: 9px;
  color: #9ca3af;
  line-height: 1.2;
}

/* 消息区域样式 */
.messages-area {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background-color: #f8fafc;
  min-height: 0;
}

/* 欢迎区域样式 */
.welcome-section {
  text-align: center;
  padding: 40px 20px;
  max-width: 800px;
  margin: 0 auto;
}

.welcome-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.welcome-section h3 {
  color: #1f2937;
  margin-bottom: 12px;
  font-size: 28px;
}

.welcome-section p {
  color: #6b7280;
  margin-bottom: 24px;
  font-size: 16px;
}

.workflow-explanation {
  background-color: #f0f9ff;
  border: 2px solid #bfdbfe;
  border-radius: 16px;
  padding: 24px;
  margin: 24px 0;
  text-align: left;
}

.workflow-explanation h4 {
  color: #1e40af;
  margin-bottom: 16px;
  font-size: 18px;
}

.flow-steps {
  display: grid;
  gap: 16px;
}

.flow-step {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

.step-number {
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  font-size: 14px;
  flex-shrink: 0;
}

.step-content {
  flex: 1;
}

.step-content strong {
  color: #1e40af;
  font-size: 16px;
  display: block;
  margin-bottom: 4px;
}

.step-content p {
  color: #64748b;
  margin: 0;
  font-size: 14px;
}

.interaction-guide {
  background-color: #fef7ff;
  border: 2px solid #e9d5ff;
  border-radius: 16px;
  padding: 20px;
  margin: 20px 0;
  text-align: left;
}

.interaction-guide h4 {
  color: #7c3aed;
  margin-bottom: 12px;
  font-size: 16px;
}

.interaction-guide ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.interaction-guide li {
  color: #374151;
  margin-bottom: 8px;
  font-size: 14px;
}

/* 核心优势展示区域 */
.quality-features {
  background-color: #f0f9ff;
  border: 2px solid #bfdbfe;
  border-radius: 16px;
  padding: 20px;
  margin: 20px 0;
  text-align: left;
}

.quality-features h4 {
  color: #1e40af;
  margin-bottom: 16px;
  font-size: 16px;
  text-align: center;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background-color: white;
  border-radius: 8px;
  border: 1px solid #e0f2fe;
}

.feature-icon {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  background-color: #f0f9ff;
  border-radius: 50%;
  flex-shrink: 0;
}

.feature-text {
  flex: 1;
}

.feature-text strong {
  display: block;
  color: #1e40af;
  font-size: 13px;
  margin-bottom: 2px;
}

.feature-text p {
  color: #64748b;
  font-size: 11px;
  margin: 0;
  line-height: 1.3;
}

.start-hint {
  color: #059669;
  font-weight: 600;
  font-size: 18px;
  margin-top: 24px;
}

/* 消息样式 */
.message-item {
  margin-bottom: 20px;
  border-radius: 12px;
  padding: 16px;
  max-width: 85%;
  clear: both;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-message {
  background: linear-gradient(135deg, #e0f2fe, #b3e5fc);
  color: #1f2937;
  float: right;
  border: 1px solid #81d4fa;
}

.agent-message {
  background-color: #ffffff;
  color: #1f2937;
  float: left;
  border: 1px solid #e5e7eb;
}

.system-message {
  background: linear-gradient(135deg, #fff7ed, #fed7aa);
  border: 1px solid #fdba74;
  color: #92400e;
  float: left;
}

.stage-approval-message {
  background: linear-gradient(135deg, #fef3c7, #fde68a);
  border: 2px solid #f59e0b;
  color: #92400e;
  float: left;
  max-width: 90%;
}

.message-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  font-size: 14px;
  font-weight: 600;
}

.agent-avatar, .system-avatar, .approval-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  background-color: rgba(255, 255, 255, 0.8);
}

.agent-info {
  display: flex;
  flex-direction: column;
}

.agent-name {
  color: #1f2937;
  font-size: 14px;
}

.agent-stage {
  color: #6b7280;
  font-size: 12px;
  font-weight: 400;
}

.message-content {
  font-size: 15px;
  line-height: 1.6;
  color: #1f2937;
}

.message-content ::v-deep(h2) {
  font-size: 18px;
  color: #1f2937;
  margin: 16px 0 8px 0;
}

.message-content ::v-deep(h3) {
  font-size: 16px;
  color: #374151;
  margin: 12px 0 6px 0;
}

.message-content ::v-deep(h4) {
  font-size: 14px;
  color: #4b5563;
  margin: 8px 0 4px 0;
}

.message-content ::v-deep(strong) {
  font-weight: 600;
  color: #1f2937;
}

/* 阶段确认操作区域 */
.approval-actions {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f59e0b;
}

.stage-summary {
  background-color: rgba(251, 191, 36, 0.1);
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 12px;
}

.summary-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}

.summary-header strong {
  color: #92400e;
  font-size: 16px;
}

.stage-badge {
  background-color: #f59e0b;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 10px;
  font-weight: 600;
}

.summary-progress {
  color: #a16207;
  font-size: 12px;
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.action-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
  font-weight: 500;
}

.approve-btn {
  background-color: #10b981;
  color: white;
}

.approve-btn:hover {
  background-color: #059669;
}

.regenerate-btn {
  background-color: #f59e0b;
  color: white;
}

.regenerate-btn:hover {
  background-color: #d97706;
}

.end-btn {
  background-color: #ef4444;
  color: white;
}

.end-btn:hover {
  background-color: #dc2626;
}

/* 当前等待确认提示 */
.current-approval-prompt {
  margin: 20px 0;
  display: flex;
  justify-content: center;
}

.approval-card {
  background: linear-gradient(135deg, #fef3c7, #fde68a);
  border: 2px solid #f59e0b;
  border-radius: 16px;
  padding: 24px;
  text-align: center;
  max-width: 500px;
  box-shadow: 0 4px 12px rgba(245, 158, 11, 0.2);
  animation: glow 2s infinite alternate;
}

.approval-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.approval-card h4 {
  color: #92400e;
  margin-bottom: 16px;
  font-size: 20px;
}

.current-stage-info {
  background-color: rgba(255, 255, 255, 0.8);
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 20px;
}

.stage-title {
  font-weight: 600;
  font-size: 18px;
  color: #92400e;
  margin-bottom: 8px;
}

.stage-agent {
  color: #a16207;
  font-size: 14px;
  margin-bottom: 8px;
}

.stage-description {
  color: #a16207;
  font-size: 12px;
  line-height: 1.4;
}

.approval-options {
  text-align: left;
}

.approval-options p {
  color: #92400e;
  margin-bottom: 12px;
  font-size: 14px;
}

.quick-options {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.quick-btn {
  flex: 1;
  padding: 10px 12px;
  border: none;
  border-radius: 8px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
  font-weight: 500;
}

.quick-btn.approve {
  background-color: #10b981;
  color: white;
}

.quick-btn.approve:hover {
  background-color: #059669;
}

.quick-btn.regenerate {
  background-color: #f59e0b;
  color: white;
}

.quick-btn.regenerate:hover {
  background-color: #d97706;
}

.custom-hint {
  color: #a16207;
  font-size: 12px;
  font-style: italic;
  margin: 0;
}

/* 输入区域样式 */
.input-section {
  display: flex;
  flex-direction: column;
  padding: 16px 20px;
  background-color: #ffffff;
  border-top: 2px solid #e2e8f0;
  transition: all 0.3s;
  flex-shrink: 0;
  min-height: 80px;
}

.input-section.approval-mode {
  background: linear-gradient(135deg, #fef3c7, #fffbeb);
  border-top: 2px solid #f59e0b;
}

.input-status {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
  color: #d97706;
  font-size: 14px;
  font-weight: 500;
}

.status-icon {
  font-size: 16px;
}

.input-controls {
  display: flex;
  gap: 12px;
  align-items: center;
}

.message-input {
  flex: 1;
  padding: 12px 16px;
  border: 2px solid #e2e8f0;
  border-radius: 8px;
  font-size: 15px;
  color: #1f2937;
  outline: none;
  transition: border-color 0.2s;
  min-height: 44px;
}

.message-input:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.approval-mode .message-input {
  border-color: #f59e0b;
  background-color: #fffbeb;
}

.approval-mode .message-input:focus {
  border-color: #d97706;
  box-shadow: 0 0 0 3px rgba(245, 158, 11, 0.1);
}

.send-button {
  padding: 12px 24px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
  min-height: 44px;
}

.send-button:hover {
  background: linear-gradient(135deg, #5a67d8, #6b46c1);
  transform: translateY(-1px);
}

.approval-send {
  background: linear-gradient(135deg, #f59e0b, #d97706) !important;
  animation: pulse 1.5s infinite;
}

.approval-send:hover {
  background: linear-gradient(135deg, #d97706, #b45309) !important;
}

.send-button:disabled {
  background-color: #9ca3af;
  cursor: not-allowed;
  transform: none;
}

/* 模态框样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  width: 400px;
  max-width: 90%;
  overflow: hidden;
}

.modal-header {
  padding: 20px 24px 16px;
  border-bottom: 1px solid #e2e8f0;
}

.modal-header h4 {
  margin: 0;
  color: #1f2937;
  font-size: 18px;
  font-weight: 600;
}

.modal-body {
  padding: 20px 24px;
  color: #6b7280;
  line-height: 1.6;
}

.modal-footer {
  padding: 16px 24px;
  border-top: 1px solid #e2e8f0;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  background-color: #f8fafc;
}

.modal-footer button {
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s;
}

.cancel-btn {
  background-color: #f3f4f6;
  color: #4b5563;
  border: 1px solid #d1d5db;
}

.cancel-btn:hover {
  background-color: #e5e7eb;
}

.confirm-btn {
  background-color: #ef4444;
  color: white;
  border: none;
}

.confirm-btn:hover {
  background-color: #dc2626;
}

/* 动画效果 */
@keyframes pulse {
  0%, 100% {
    opacity: 1;
  }
  50% {
    opacity: 0.8;
  }
}

@keyframes glow {
  from {
    box-shadow: 0 4px 12px rgba(245, 158, 11, 0.2);
  }
  to {
    box-shadow: 0 4px 20px rgba(245, 158, 11, 0.4);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .stages-flow {
    grid-template-columns: repeat(2, 1fr);
    gap: 8px;
  }

  .stage-node {
    min-width: auto;
    padding: 8px;
  }

  .stage-name {
    font-size: 11px;
  }

  .stage-agent {
    font-size: 9px;
  }

  .stage-desc {
    font-size: 8px;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 8px;
  }

  .feature-item {
    padding: 8px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .action-btn {
    width: 100%;
  }

  .quick-options {
    flex-direction: column;
  }

  .input-controls {
    flex-direction: column;
  }

  .send-button {
    width: 100%;
  }
}

@media (max-width: 480px) {
  .stages-flow {
    grid-template-columns: 1fr;
  }

  .workflow-progress {
    padding: 12px 16px;
  }

  .survey-header {
    padding: 16px 20px;
  }

  .survey-header h3 {
    font-size: 18px;
  }
}

/* 滚动条样式 */
.messages-area::-webkit-scrollbar {
  width: 8px;
}

.messages-area::-webkit-scrollbar-track {
  background: #f1f5f9;
  border-radius: 4px;
}

.messages-area::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 4px;
}

.messages-area::-webkit-scrollbar-thumb:hover {
  background: #94a3b8;
}

</style>