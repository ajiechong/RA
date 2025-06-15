<template>
  <div class="multiagent-container">
    <!-- 顶部AppBar -->
    <app-bar></app-bar>

    <div class="main-content">
      <!-- 左侧阶段导航 -->
      <div class="stage-nav">
        <div class="stage-header">研究阶段</div>
        <div
            v-for="(stage, index) in stages"
            :key="index"
            class="stage-item"
            :class="{
              active: currentStage === index,
              'locked-stage': !canAccessStage(index),
              'completed-stage': stageStatus[index]
            }"
            @click="canAccessStage(index) ? goToStage(index) : null"
        >
          {{ stage.name }}
          <span v-if="stageStatus[index]" class="completed">✓</span>
        </div>
      </div>

      <!-- 右侧对话区域 -->
      <div class="chat-area">
        <div class="stage-title">
          {{ stages[currentStage].name }}
          <div
              v-if="currentStage < stages.length - 1 && !stageStatus[currentStage]"
              class="approve-btn"
              @click="showConfirmModal = true"
          >
            APPROVED
          </div>
          <div
              v-if="currentStage < stages.length - 1 && stageStatus[currentStage]"
              class="approve-btn gray-btn"
          >
            APPROVED
          </div>
        </div>

        <div class="chat-messages" ref="messageContainer">
          <!-- 欢迎信息 -->
          <div v-if="currentStage === 0 && messages.length === 0" class="welcome-message">
            欢迎使用多智能体研究协作平台，请开始您的研究探索
          </div>

          <!-- 对话消息 -->
          <div
              v-for="(msg, index) in messages"
              :key="msg.id || index"
              class="message-block"
              :class="{ 'user-message': msg.role === 'user', 'agent-message': msg.role === 'agent' }"
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

        <!-- 输入框上方提示文字 -->
        <div v-if="currentStage < stages.length - 1 && !stageStatus[currentStage]" class="input-hint">
          如需进入下一阶段，请点击APPROVED按钮
        </div>

        <!-- 消息输入区域 - 已锁定阶段禁用输入 -->
        <div class="chat-input" :class="{ 'input-locked': stageStatus[currentStage] }">
          <input
              v-model="inputMessage"
              type="text"
              placeholder="输入您的研究问题..."
              @keyup.enter="handleEnter"
              :disabled="isLoading || stageStatus[currentStage]"
          >
          <button
              @click="handleSend"
              :disabled="!inputMessage.trim() || isLoading || stageStatus[currentStage]"
          >
            发送
          </button>
        </div>
      </div>
    </div>

    <!-- 自定义确认模态框 -->
    <div v-if="showConfirmModal" class="confirm-modal">
      <div class="modal-overlay" @click="showConfirmModal = false"></div>
      <div class="modal-content">
        <div class="modal-body">
          <p>确认后此阶段将无法修改，是否继续？</p>
        </div>
        <div class="modal-footer">
          <button @click="showConfirmModal = false">取消</button>
          <button @click="confirmApproveStage">确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue';

// 模拟智能体响应
const agentResponses = {
  'SurveyDirector': [
    '请提供更具体的研究主题，以便我为您制定详细的调研计划。例如，您可以提到特定的机器学习领域，如"深度学习在医学影像中的应用"或"强化学习在游戏中的最新进展"。',
    '根据您提供的主题，我已制定初步调研计划：1. 确定核心研究问题 2. 检索近5年相关文献 3. 组织文献阅读小组 4. 每周生成进展报告'
  ],
  'PaperRetriever': [
    '请提供您感兴趣的具体机器学习主题，以便我能够进行更有针对性的文献检索。例如，您可以提到"自然语言处理中的迁移学习"或"机器学习在金融预测中的应用"等。',
    '已检索到相关文献12篇，其中高被引文献3篇，最新研究4篇，需要我为您列出摘要吗？'
  ],
  'PaperSummarizer': [
    '请提供您关注的具体研究主题或论文标题，以便我对此进行详细的总结和分析。',
    '已完成文献《深度学习在医学影像中的应用进展》的总结：该研究提出了一种多模态融合模型，在肺部CT诊断中取得了98.7%的准确率...'
  ],
  'SurveyAnalyst': [
    '请您提供具体的机器学习研究主题或论文标题，以便我能够为您进行详细的分析和总结。',
    '通过对10篇相关文献的分析，发现当前研究主要集中在三个方向：1. 模型效率优化 2. 小样本学习 3. 跨领域迁移应用'
  ],
  'SolutionDesigner': [
    '请根据调研结果提供您的研究目标，我将为您设计创新方案。',
    '基于调研结果，我提出三种方案：1. 改进型卷积神经网络 2. 注意力机制融合模型 3. 联邦学习框架'
  ],
  'DesignReviewer': [
    '请提供需要评估的方案细节，我将进行可行性和创新性评审。',
    '对三种方案的评审结果：方案2在创新性和可行性上表现最佳，建议选择该方案进行细化'
  ],
  'SolutionRefiner': [
    '请提供需要细化的方案概要，我将进行技术细节设计。',
    '方案2的技术细节设计：采用多头自注意力机制，结合残差连接，输入层采用数据增强策略...'
  ],
  'CodingAssistant': [
    '请提供方案的技术细节，我将为您编写实现代码。',
    '以下是方案2的核心代码实现：\n```python\nimport torch\nimport torch.nn as nn\n\nclass AttentionModel(nn.Module):\n    def __init__(self, input_dim, hidden_dim, num_heads):\n        super().__init__()\n        # 模型定义...\n```'
  ],
  'ExperimentRunner': [
    '请提供实验代码和配置，我将为您执行实验。',
    '实验已在GPU集群上启动，预计运行时间2小时，将实时监控并报告进度'
  ],
  'DataAnalyst': [
    '请提供实验结果数据，我将进行分析。',
    '实验数据分析结果：在测试集上取得了92.3%的准确率，相比基线模型提升了5.6%，具体指标如下...'
  ],
  'ExperimentRecorder': [
    '请提供实验过程和结果，我将进行记录。',
    '实验记录已完成，包括：1. 环境配置 2. 超参数设置 3. 运行日志 4. 结果图表，需要生成报告吗？'
  ],
  'WritingAssistant': [
    '请提供研究背景和结果，我将开始撰写论文。',
    '论文引言部分已完成：\n机器学习在过去十年取得了显著进展，特别是在计算机视觉和自然语言处理领域...'
  ],
  'FigureGenerator': [
    '请提供数据和图表需求，我将生成相应图表。',
    '已根据实验结果生成准确率对比图和损失曲线，需要调整样式或添加标注吗？'
  ],
  'ReferenceManager': [
    '请提供参考文献列表，我将进行格式整理。',
    '参考文献已按IEEE格式整理完成，共23篇，需要生成BibTeX文件吗？'
  ],
  'PaperPolisher': [
    '请提供论文草稿，我将进行润色和结构调整。',
    '论文润色建议：1. 优化摘要的逻辑结构 2. 补充相关工作部分的比较 3. 增强实验部分的分析深度'
  ]
};

// 研究阶段定义
const stages = ref([
  {
    name: '文献调研阶段',
    agents: ['SurveyDirector', 'PaperRetriever', 'PaperSummarizer', 'SurveyAnalyst']
  },
  {
    name: '方案设计阶段',
    agents: ['SolutionDesigner', 'DesignReviewer', 'SolutionRefiner']
  },
  {
    name: '实验阶段',
    agents: ['CodingAssistant', 'ExperimentRunner', 'DataAnalyst', 'ExperimentRecorder']
  },
  {
    name: '论文写作阶段',
    agents: ['WritingAssistant', 'FigureGenerator', 'ReferenceManager', 'PaperPolisher']
  }
]);

// 状态管理
const currentStage = ref(0);
const messages = ref([]);
const inputMessage = ref('');
const isLoading = ref(false);
const showLoadMore = ref(false);
const currentAgentIndex = ref(0);
const stageStatus = ref(Array(stages.value.length).fill(false)); // 阶段锁定状态
const showConfirmModal = ref(false); // 显示确认模态框
const messageId = ref(1); // 消息ID生成器

// 保存每个阶段的消息历史
const stageMessages = ref(
    stages.value.map(() => [])
);

// 初始化
onMounted(() => {
  // 加载欢迎信息
  if (messages.value.length === 0) {
    const welcomeAgents = stages.value[currentStage.value].agents;
    welcomeAgents.forEach(agent => {
      addMessage('system', agent, `欢迎使用${agent}智能体，我将协助您完成${stages.value[currentStage.value].name}`);
    });
  }
});

// 处理发送按钮点击和回车事件
const handleSend = () => {
  if (!inputMessage.value.trim()) return;
  if (isLoading.value) return;
  if (stageStatus.value[currentStage.value]) return;

  // 添加用户消息
  const userMessage = {
    id: messageId.value++,
    role: 'user',
    agent: 'user',
    content: inputMessage.value,
    timestamp: new Date()
  };
  messages.value.push(userMessage);
  inputMessage.value = '';

  // 触发DOM更新后处理智能体响应
  nextTick(() => {
    isLoading.value = true;
    // 重置智能体索引，确保每次都从第一个智能体开始回复
    currentAgentIndex.value = 0;
    processAgentResponses(stages.value[currentStage.value].agents);
  });
};

const handleEnter = (event) => {
  if (event.key === 'Enter' && !event.shiftKey) {
    event.preventDefault();
    handleSend();
  }
};

// 处理智能体响应 - 无延迟连续回复
const processAgentResponses = (agents) => {
  if (currentAgentIndex.value >= agents.length) {
    isLoading.value = false;
    return;
  }

  const agent = agents[currentAgentIndex.value];
  const responseIndex = messages.value.filter(msg => msg.agent === agent && msg.role === 'agent').length;
  const response = agentResponses[agent][responseIndex % agentResponses[agent].length];

  // 添加智能体响应
  addMessage('agent', agent, response);

  // 立即处理下一个智能体
  currentAgentIndex.value++;
  processAgentResponses(agents);
};

// 添加消息到对话
const addMessage = (role, agent, content) => {
  messages.value.push({
    id: messageId.value++,
    role,
    agent,
    content,
    timestamp: new Date()
  });

  // 滚动到底部
  scrollToBottom();
};

// 确认批准当前阶段
const confirmApproveStage = () => {
  showConfirmModal.value = false;
  approveCurrentStage();
};

// 批准当前阶段，进入下一阶段
const approveCurrentStage = () => {
  if (currentStage.value >= stages.value.length - 1) return;

  // 保存当前阶段的消息
  stageMessages.value[currentStage.value] = [...messages.value];

  // 标记当前阶段为已批准
  stageStatus.value[currentStage.value] = true;

  // 进入下一阶段
  currentStage.value++;
  messages.value = [...stageMessages.value[currentStage.value]];

  // 如果新阶段没有消息，加载欢迎信息
  if (messages.value.length === 0) {
    const welcomeAgents = stages.value[currentStage.value].agents;
    welcomeAgents.forEach(agent => {
      addMessage('system', agent, `欢迎进入${stages.value[currentStage.value].name}，我是${agent}，将协助您完成当前阶段工作`);
    });
  }
};

// 跳转到指定阶段
const goToStage = (stageIndex) => {
  // 保存当前阶段的消息
  stageMessages.value[currentStage.value] = [...messages.value];

  // 切换到目标阶段
  currentStage.value = stageIndex;
  messages.value = [...stageMessages.value[stageIndex]];

  // 如果目标阶段没有消息，加载欢迎信息
  if (messages.value.length === 0) {
    const welcomeAgents = stages.value[stageIndex].agents;
    welcomeAgents.forEach(agent => {
      addMessage('system', agent, `欢迎使用${agent}智能体，我将协助您完成${stages.value[stageIndex].name}`);
    });
  }

  scrollToBottom();
};

// 判断是否可以访问指定阶段
const canAccessStage = (stageIndex) => {
  // 第一阶段总是可访问
  if (stageIndex === 0) return true;

  // 后续阶段：如果前一阶段已批准，则当前阶段可访问
  return stageStatus.value[stageIndex - 1];
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

// 加载更多历史消息（示例功能）
const loadMoreMessages = () => {
  showLoadMore.value = false;
  // 这里可以实现加载更多历史消息的逻辑
};
</script>

<style scoped>
/* 保持样式不变 */
.multiagent-container {
  margin: 20px auto;
  height: calc(100vh - 40px);
  max-width: 1400px;
  display: flex;
  flex-direction: column;
  position: relative;
}

.main-content {
  display: flex;
  flex: 1;
}

/* 左侧阶段导航样式 */
.stage-nav {
  width: 240px;
  background-color: #f5f7fa;
  border-right: 1px solid #e4e6eb;
  display: flex;
  flex-direction: column;
  padding: 16px;
  box-sizing: border-box;
}

.stage-header {
  font-size: 18px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e6eb;
}

.stage-item {
  padding: 12px 16px;
  margin-bottom: 8px;
  border-radius: 8px;
  cursor: pointer;
  background-color: #ffffff;
  border: 1px solid #e4e6eb;
  color: #464646;
  transition: all 0.2s;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stage-item:hover:not(.locked-stage):not(.completed-stage) {
  background-color: #f9fafb;
  border-color: #d1d5db;
  transform: translateY(-2px);
}

.stage-item.active {
  background-color: #eef2ff;
  border-color: #a5b4fc;
  color: #3b82f6;
  font-weight: 500;
  transform: translateY(0);
}

.stage-item.locked-stage {
  cursor: not-allowed;
  opacity: 0.5;
  background-color: #f8f9fa;
  border-color: #e9ecef;
}

.stage-item.completed-stage {
  background-color: #f0fdf4;
  border-color: #d1fae5;
  color: #065f46;
}

.completed {
  background-color: #10b981;
  color: white;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 12px;
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

.approve-btn {
  background-color: #10b981;
  color: white;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.2s;
}

.approve-btn:hover {
  background-color: #059669;
}

.gray-btn {
  background-color: #d1d5db !important;
  cursor: not-allowed !important;
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
  transition: all 0.3s;
}

.chat-input.input-locked {
  background-color: #f8f9fa;
}

.chat-input.input-locked input,
.chat-input.input-locked button {
  cursor: not-allowed;
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

/* 输入框上方提示文字样式 */
.input-hint {
  padding: 8px 24px;
  color: #6b7280;
  font-size: 14px;
  background-color: #f3f4f6;
  border-radius: 0 0 8px 8px;
  margin-bottom: 0;
  display: flex;
  justify-content: center;
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

.modal-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
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
@media (max-width: 992px) {
  .stage-nav {
    width: 60px;
  }

  .stage-header {
    display: none;
  }

  .stage-item {
    padding: 12px;
    justify-content: center;
  }

  .stage-item span,
  .stage-item .completed {
    display: none;
  }
}

@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
  }

  .stage-nav {
    width: 100%;
    flex-direction: row;
    max-height: 60px;
    overflow-x: auto;
    padding: 8px;
  }

  .stage-header {
    display: block;
    border-bottom: none;
    margin-bottom: 0;
    padding-bottom: 0;
  }

  .stage-item {
    margin-right: 8px;
    margin-bottom: 0;
  }

  .chat-area {
    margin-top: 8px;
  }

  .modal-content {
    width: 90%;
  }
}
</style>