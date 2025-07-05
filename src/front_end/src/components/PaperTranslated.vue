<template>
    <div class="chat-window">
        <div class="chat-history" ref="chatHistory">
            <div class="message" v-for="(msg, index) in messages" :key="index">
                <div class="user-message" v-if="msg.from === 'user'">
                    <div class="paper-card">
                        {{ msg.text }}
                    </div>
                </div>
                <!-- 文献卡片展示区域 -->
                <div v-if="msg.from === 'agent' && msg.text" class="agent-papers">
                    <div class="paper-card">
                        <!-- 动态渲染所有章节 -->
                        <div v-for="(section, idx) in paperSections" :key="idx" class="paper-section">
                            <!-- 标题显示 -->
                            <h2 class="section-title">{{ section.title }}</h2>

                            <!-- 其他章节内容展示 -->
                            <div class="section-body">
                                <ul v-if="section.content && section.content.startsWith('- ')" class="section-list">
                                    <li v-for="(item, itemIdx) in section.content.split('\n')" :key="itemIdx">
                                        {{ item.trim() }}
                                    </li>
                                </ul>
                                <div v-else class="section-content">
                                    <p>{{ section.content }}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="input-area-parent">
            <div class="input-area">
                <button disabled>
                    {{ isFinished? '思考结束' : '正在思考...'  }}
                </button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            messages: [],
            paperId: 0,
            paperTitle: '',
            pdfLink: '',
            userInput: '',
            isLoading: true,
            isFinished: false,
            paperSections: []
        };
    },
    methods: {
        resetChat() {
            this.messages = []; // 重置聊天历史
        },
        // 解析论文内容（适配指定结构）
        parsePaperContent(paperText) {
            const sections = [];
            const lines = paperText.split('\n');
            let currentSection = null;

            lines.forEach((line) => {
                const trimmedLine = line.trim();

                // 检测章节标题（以[章节名]格式）
                const sectionTitleMatch = trimmedLine.match(/^\[(.+?)\]$/);
                if (sectionTitleMatch) {
                    // 保存当前章节
                    if (currentSection) {
                        currentSection.content = currentSection.content.trim();
                        sections.push(currentSection);
                    }

                    // 创建新章节
                    currentSection = {
                        title: sectionTitleMatch[1],
                        content: '',
                    };
                } else if (currentSection) {
                    // 累加章节内容
                    currentSection.content += line + '\n';
                }
            });

            // 添加最后一个章节
            if (currentSection) {
                currentSection.content = currentSection.content.trim();
                sections.push(currentSection);
            }

            // 解析内容为列表或段落
            sections.forEach(section => {
                const content = section.content;
                if (!content) return;

                const lines = content.split('\n');
                let listItems = [];
                let paragraphContent = [];
                let isList = false;

                lines.forEach(line => {
                    const trimmedLine = line.trim();
                    if (!trimmedLine) return;

                    if (trimmedLine.startsWith('- ')) {
                        isList = true;
                        listItems.push(trimmedLine.replace('- ', ''));
                    } else {
                        if (isList && listItems.length > 0) {
                            isList = false;
                        }
                        paragraphContent.push(trimmedLine);
                    }
                });

                section.listItems = listItems;
                section.paragraphContent = paragraphContent;
            });

            return sections;
        },
        extractField(content, fieldName) {
            const startIndex = content.indexOf(fieldName) + fieldName.length;
            const nextFieldIndex = content.indexOf('\n', startIndex);

            if (nextFieldIndex === -1) {
                // 没有下一个字段，直接返回剩余内容
                return content.substring(startIndex).trim();
            } else {
                // 返回两个字段之间的内容
                return content.substring(startIndex, nextFieldIndex).trim();
            }
        },
        // 处理论文数据
        processPaperData(paperText) {
            // 过滤参考文献部分
            const filteredText = paperText.split('### 参考文献')[0];

            // 解析论文内容
            this.paperSections = this.parsePaperContent(filteredText);
            console.log('解析后的章节数据:', this.paperSections);
        },
        async fetchRelatedPapers() {
            console.log('fetchRelatedPapers被调用，参数:', this.paperId, this.paperTitle, this.pdfLink);
            const userMessage = `翻译并概括 title 为 《${this.paperTitle}》的文章`;
            this.messages.push({ from: 'user', text: userMessage });
            this.userInput = '';

            try {
                const response = await axios.post(
                    `http://localhost:8080/api/chat/literature_analyst`,
                    {
                        query: userMessage, // 必填参数
                        context: this.pdfLink
                    },
                    {
                        headers: {
                            'Content-Type': 'application/json', // 确保请求体为 JSON 格式
                        },
                    }
                );
                console.log('API返回数据:', response.data.response);

                // 确保response.data.response存在且为字符串
                const paperText = response.data.response || '';

                // 调用处理函数（修正此处调用）
                this.processPaperData(paperText);
                // 添加解析结果到消息列表（修正数据绑定）
                this.messages.push({
                    from: 'agent',
                    text: this.paperSections
                });
                this.$refs.chatHistory.scrollTop = this.$refs.chatHistory.scrollHeight;
            } catch (error) {
                console.error('对话失败:', error.response?.data?.error || error.message);
                this.messages.push({
                    from: 'system',
                    text: '抱歉，暂时无法处理你的请求，请稍后再试。',
                });
            } finally {
                this.isLoading = false;
                this.isFinished = true;
            }
        },
    },
    mounted() {
        const paperId = this.$route?.params?.id;
        const paper = JSON.parse(sessionStorage.getItem('selectedPaper'));
        if (paper) {
            this.paperId = paper.id;
            this.paperTitle = paper.title;
            this.pdfLink = paper.pdf;
        }
        if (paperId) {
            this.fetchRelatedPapers();
        }
    },
};
</script>

<style scoped>
.chat-window {
    max-width: 1000px;
    margin: 20px auto;
    padding: 20px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.chat-history {
    height: 600px;
    overflow-y: auto;
    margin-bottom: 20px;
    padding: 10px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
}
.message {
    margin: 10px 0;
    padding: 8px 12px;
    border-radius: 4px;
}
.user-message {
    margin-left: auto;
    background: #f5f5f5;
    max-width: 70%;
    padding: 10px;
    border-radius: 8px;
    color: black;
}

.agent-papers {
    background: #f5f5f5;
    max-width: 70%;
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 10px;
}
.paper-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 15px;
    color: #333;
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
    border-left: 4px solid #165DFF;
}

.input-area {
    display: flex;
    gap: 10px;
    color: black;
    margin-left: auto; /* 即使父容器有其他元素，也会靠右 */
}
.input-area-parent{
    display: flex;
    /* 方法A：直接设置右侧对齐 */
    justify-content: flex-end;
    color:black
}

textarea {
    flex-grow: 1;
    padding: 10px;
    border: 1px solid #e0e0e0;
    border-radius: 4px;
    resize: vertical;
}
button {
    padding: 10px 20px;
    background: #4a90e2;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}
button:disabled {
    background: #ccc;
    cursor: not-allowed;
}
.section-title {
    font-weight: 800;
    margin-bottom: 16px;
    color: #165DFF;
    font-size: 24px;
    border-bottom: 2px solid #165DFF;
    padding-bottom: 8px;
}

.section-content{
    font-weight: 400;
    margin-bottom: 18px;
    color: #444;
    font-size: 16px;
    line-height: 1.8;
}
.section-list li {
    font-weight: 400;
    line-height: 1.7;
    color: #444;
    font-size: 16px;
}

.section-content p {
    margin-bottom: 12px;
}

.section-list {
    padding-left: 10px;
    margin-top: 10px;
    margin-bottom: 20px;
}

.section-list li {
    margin-bottom: 10px;
}

.basic-info {
    margin-bottom: 20px;
}
.info-item {
    margin-bottom: 12px;
    display: flex;
    align-items: flex-start;
}
.info-label {
    font-weight: 600;
    color: #495057;
    min-width: 40px;
    margin-right: 5px;
}
.info-value {
    color: #333;
    flex-grow: 1;
}
</style>
