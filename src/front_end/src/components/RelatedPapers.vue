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
                <div v-if="msg.from === 'agent' && msg.papers" class="agent-papers">
                    <div v-for="(paper, paperIndex) in msg.papers" :key="paperIndex" class="paper-card">
                        <h3 class="paper-title">{{ paper.Title }}</h3>
                        <div class="paper-meta">
                            <span class="paper-authors">{{ paper.Authors }}</span>
                            <span class="paper-date">{{ paper.Date }}</span>
                        </div>
                        <div class="paper-abstract">{{ paper.Abstract }}</div>
                        <div class="paper-actions">
                            <a :href="paper.PDF" target="_blank" class="btn btn-pdf">
                                <i class="fa fa-file-pdf-o"></i> 查看PDF
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="input-area">
            <textarea
                    v-model="userInput"
                    placeholder="不支持输入问题"
                    rows="3"
                    :disabled="isLoading"
            ></textarea>
            <button disabled>
                {{ isFinished? '思考结束' : '正在思考...'  }}
            </button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            messages: [],
            userInput: '',
            isLoading: true,
            isFinished : false,
            paperId : 0,
            paperTitle : '',
            pdfLink : '',
        };
    },
    methods: {
        resetChat() {
            this.messages = []; // 重置聊天历史
        },
        async fetchRelatedPapers() {
            console.log('fetchRelatedPapers被调用，参数:', this.paperId, this.paperTitle, this.pdfLink);
            const userMessage = `帮我查找出与 title为：《${this.paperTitle}》 的文章相关的五篇论文`;
            this.messages.push({ from: 'user', text: userMessage });
            this.userInput = '';

            try {
                const response = await axios.post(
                    `http://localhost:8080/api/rag/query`,
                    {
                        query: userMessage, // 必填参数
                    },
                    {
                        headers: {
                            'Content-Type': 'application/json', // 确保请求体为 JSON 格式
                        },
                    }
                );
                console.log('API响应:', response.data);

                // 处理文献数据（假设response.data.results是文献数组）
                const papersData = response.data.results || [];
                const formattedPapers = papersData.map(paper => ({
                    Title: paper.Title || '未知标题',
                    Authors: paper.Authors || '未知作者',
                    Abstract: this.formatAbstract(paper.Abstract),
                    Date: paper.Date || '未知日期',
                    PDF: paper.PDF || '#' // 占位链接
                }));

                this.messages.push({
                    from: 'agent',
                    papers: formattedPapers // 附加文献数据
                });
                this.$refs.chatHistory.scrollTop = this.$refs.chatHistory.scrollHeight;
            } catch (error) {
                console.error('对话失败:', error.response?.data?.error || error.message);
                this.messages.push({
                    from: 'system',
                    text: '抱歉，暂时无法处理你的请求，请稍后再试。',
                });
            } finally {
                this.isLoading = true;
                this.isFinished = true;
            }
        },
        // 格式化摘要（去除多余标记）
        formatAbstract(abstractText) {
            if (!abstractText) return '无摘要信息';
            return abstractText.replace(/\s*△ Less$/, '').trim();
        }
    },
    mounted() {
        const paperId = this.$route.params.id;
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
.chat-header {
    text-align: center;
    margin-bottom: 20px;
}
.chat-history {
    height: 400px;
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
    border-radius: 6px;
    padding: 12px;
    margin-bottom: 10px;
    box-shadow: 0 1px 3px rgba(0,0,0,0.05);
    border-left: 4px solid #165DFF;
}
.paper-title {
    font-weight: 600;
    margin-bottom: 6px;
    color: #333;
}
.paper-meta {
    font-size: 13px;
    color: #666;
    margin-bottom: 8px;
}
.paper-authors {
    margin-right: 12px;
}
.paper-abstract {
    font-size: 14px;
    line-height: 1.5;
    color: #444;
    margin-bottom: 10px;
    white-space: pre-wrap;
}
.paper-actions {
    text-align: right;
}
.btn {
    display: inline-block;
    padding: 4px 10px;
    background: #165DFF;
    color: white;
    border-radius: 4px;
    text-decoration: none;
    font-size: 12px;
    transition: background-color 0.2s;
}
.btn:hover {
    background-color: #0E42B3;
}
.btn-pdf {
    background: #E64340;
}
.btn-pdf:hover {
    background: #C53030;
}
.input-area {
    display: flex;
    gap: 10px;
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
</style>
