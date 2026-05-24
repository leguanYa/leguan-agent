<script setup>
import { ref, nextTick, watch, computed } from 'vue'

const props = defineProps({
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    default: '',
  },
  theme: {
    type: String,
    default: 'default',
  },
  aiAvatar: {
    type: String,
    default: '🤖',
  },
  userAvatar: {
    type: String,
    default: '我',
  },
  loading: {
    type: Boolean,
    default: false,
  },
  /** 每次 SSE 片段后追加的内容，超级智能体每步完成后需换行 */
  chunkSuffix: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['send'])

const inputText = ref('')
const messages = ref([])
const chatContainer = ref(null)

const themeClass = computed(() => `theme-${props.theme}`)

function scrollToBottom() {
  nextTick(() => {
    if (chatContainer.value) {
      chatContainer.value.scrollTop = chatContainer.value.scrollHeight
    }
  })
}

watch(messages, scrollToBottom, { deep: true })
watch(() => props.loading, scrollToBottom)

function handleSend() {
  const text = inputText.value.trim()
  if (!text || props.loading) return

  messages.value.push({ role: 'user', content: text })
  inputText.value = ''
  emit('send', text)
}

function handleKeydown(e) {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    handleSend()
  }
}

function appendAiMessage(chunk) {
  const content = chunk + props.chunkSuffix
  const last = messages.value[messages.value.length - 1]
  if (last?.role === 'assistant' && !last.isError) {
    last.content += content
  } else {
    messages.value.push({ role: 'assistant', content })
  }
}

function appendAiError(errorMsg) {
  messages.value.push({ role: 'assistant', content: errorMsg, isError: true })
}

defineExpose({
  appendAiMessage,
  appendAiError,
})
</script>

<template>
  <div class="page-layout" :class="themeClass">
    <div class="chat-room">
      <header class="chat-header">
        <router-link to="/" class="back-btn" title="返回主页" aria-label="返回主页">
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M19 12H5M12 19l-7-7 7-7" />
          </svg>
        </router-link>
        <div class="header-avatar ai-avatar-header">{{ aiAvatar }}</div>
        <div class="header-info">
          <h1>{{ title }}</h1>
          <p v-if="subtitle" class="subtitle">{{ subtitle }}</p>
        </div>
      </header>

      <main ref="chatContainer" class="chat-messages">
        <div v-if="messages.length === 0" class="empty-state">
          <div class="empty-avatar">{{ aiAvatar }}</div>
          <p class="empty-title">你好，我是 {{ title }}</p>
          <p class="empty-desc">输入消息后按 Enter 发送，Shift + Enter 换行</p>
        </div>

        <div
          v-for="(msg, index) in messages"
          :key="index"
          class="message-row"
          :class="msg.role === 'user' ? 'message-user' : 'message-ai'"
        >
          <div
            class="avatar"
            :class="msg.role === 'user' ? 'avatar-user' : 'avatar-ai'"
            :aria-label="msg.role === 'user' ? '用户' : 'AI'"
          >
            {{ msg.role === 'user' ? userAvatar : aiAvatar }}
          </div>
          <div class="bubble" :class="{ 'bubble-error': msg.isError, 'bubble-user': msg.role === 'user', 'bubble-ai': msg.role === 'assistant' }">
            <pre class="message-text">{{ msg.content }}</pre>
          </div>
        </div>

        <div v-if="loading" class="message-row message-ai">
          <div class="avatar avatar-ai">{{ aiAvatar }}</div>
          <div class="bubble bubble-ai typing">
            <span></span><span></span><span></span>
          </div>
        </div>
      </main>

      <footer class="chat-input-area">
        <div class="input-wrapper">
          <textarea
            v-model="inputText"
            :disabled="loading"
            placeholder="输入消息..."
            rows="1"
            @keydown="handleKeydown"
          />
          <button
            class="send-btn"
            :disabled="loading || !inputText.trim()"
            :aria-label="loading ? '回复中' : '发送'"
            @click="handleSend"
          >
            <svg v-if="!loading" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="22" y1="2" x2="11" y2="13" />
              <polygon points="22 2 15 22 11 13 2 9 22 2" />
            </svg>
            <span v-else class="loading-dot">···</span>
          </button>
        </div>
        <p class="input-hint">Enter 发送 · Shift+Enter 换行</p>
      </footer>
    </div>
  </div>
</template>

<style scoped>
.page-layout {
  flex: 1;
  display: flex;
  justify-content: center;
  min-height: 0;
  background: transparent;
}

.chat-room {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: var(--chat-max-width);
  flex: 1;
  min-height: 0;
  background: var(--color-surface);
  backdrop-filter: blur(16px);
  border-left: 1px solid var(--color-border);
  border-right: 1px solid var(--color-border);
  box-shadow: var(--shadow-md), inset 0 1px 0 rgba(0, 240, 255, 0.05);
}

/* ===== Header ===== */
.chat-header {
  display: flex;
  align-items: center;
  gap: var(--space-md);
  height: var(--header-height);
  padding: 0 var(--space-lg);
  background: rgba(10, 14, 23, 0.9);
  border-bottom: 1px solid var(--color-border);
  flex-shrink: 0;
  position: sticky;
  top: 0;
  z-index: 10;
}

.chat-header::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--color-primary), transparent);
  opacity: 0.4;
}

.theme-love .chat-header::after {
  background: linear-gradient(90deg, transparent, var(--color-love), transparent);
}

.theme-manus .chat-header::after {
  background: linear-gradient(90deg, transparent, var(--color-manus), transparent);
}

.back-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: var(--radius-sm);
  color: var(--color-text-secondary);
  text-decoration: none;
  border: 1px solid var(--color-border-light);
  transition: color 0.2s, border-color 0.2s, box-shadow 0.2s;
  flex-shrink: 0;
}

.back-btn:hover {
  color: var(--color-primary);
  border-color: var(--color-border-glow);
  box-shadow: 0 0 12px rgba(0, 240, 255, 0.2);
}

.header-avatar {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  flex-shrink: 0;
  border: 1px solid var(--color-border);
}

.header-info {
  min-width: 0;
  flex: 1;
}

.header-info h1 {
  margin: 0;
  font-family: var(--font-display);
  font-size: 15px;
  font-weight: 600;
  color: var(--color-text);
  letter-spacing: 1px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.subtitle {
  margin: 2px 0 0;
  font-size: 11px;
  color: var(--color-text-muted);
  font-family: var(--font-display);
  letter-spacing: 0.5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* ===== Messages ===== */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: var(--space-lg);
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
  background:
    linear-gradient(rgba(0, 240, 255, 0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 240, 255, 0.02) 1px, transparent 1px),
    rgba(8, 12, 22, 0.6);
  background-size: 32px 32px, 32px 32px, 100% 100%;
}

.empty-state {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-2xl) var(--space-md);
  text-align: center;
}

.empty-avatar {
  width: 72px;
  height: 72px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 36px;
  margin-bottom: var(--space-md);
  border: 1px solid var(--color-border);
  box-shadow: var(--glow-cyan);
}

.empty-title {
  margin: 0 0 var(--space-sm);
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
  letter-spacing: 1px;
}

.empty-desc {
  margin: 0;
  font-size: 14px;
  color: var(--color-text-muted);
}

.message-row {
  display: flex;
  gap: var(--space-sm);
  max-width: 82%;
}

.message-user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message-ai {
  align-self: flex-start;
}

.avatar {
  flex-shrink: 0;
  width: var(--avatar-size);
  height: var(--avatar-size);
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 600;
}

.avatar-user {
  background: linear-gradient(135deg, rgba(0, 240, 255, 0.2), rgba(168, 85, 247, 0.2));
  color: var(--color-primary);
  border: 1px solid rgba(0, 240, 255, 0.4);
  box-shadow: 0 0 12px rgba(0, 240, 255, 0.15);
}

.avatar-ai {
  background: rgba(15, 23, 42, 0.8);
  font-size: 20px;
  border: 1px solid var(--color-border);
}

.theme-love .avatar-ai,
.theme-love .ai-avatar-header,
.theme-love .empty-avatar {
  background: rgba(255, 0, 128, 0.08);
  border-color: rgba(255, 0, 128, 0.35);
  box-shadow: var(--glow-pink);
}

.theme-manus .avatar-ai,
.theme-manus .ai-avatar-header,
.theme-manus .empty-avatar {
  background: rgba(0, 212, 255, 0.08);
  border-color: rgba(0, 212, 255, 0.35);
  box-shadow: var(--glow-cyan);
}

.bubble {
  padding: 12px 16px;
  border-radius: var(--radius-md);
  min-width: 0;
}

.bubble-user {
  background: linear-gradient(135deg, rgba(0, 240, 255, 0.15), rgba(168, 85, 247, 0.15));
  border: 1px solid rgba(0, 240, 255, 0.3);
  border-bottom-right-radius: 2px;
  box-shadow: 0 0 16px rgba(0, 240, 255, 0.1);
}

.bubble-ai {
  background: rgba(15, 23, 42, 0.85);
  border-bottom-left-radius: 2px;
  border: 1px solid var(--color-border);
  border-left: 2px solid var(--color-primary);
}

.theme-love .bubble-ai {
  border-left-color: var(--color-love);
}

.theme-manus .bubble-ai {
  border-left-color: var(--color-manus);
}

.bubble-error {
  background: var(--color-error-bg) !important;
  border-color: rgba(255, 68, 102, 0.4) !important;
  border-left-color: var(--color-error) !important;
}

.message-text {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
  font-family: inherit;
  font-size: 15px;
  line-height: 1.65;
}

.bubble-ai .message-text {
  text-align: left;
  color: var(--color-text);
}

.bubble-user .message-text {
  text-align: left;
  color: #e0f7fa;
}

.bubble-error .message-text {
  text-align: left;
  color: var(--color-error);
}

/* ===== Typing ===== */
.typing {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 14px 18px;
}

.typing span {
  width: 7px;
  height: 7px;
  border-radius: var(--radius-full);
  background: var(--color-primary);
  animation: bounce 1.4s infinite ease-in-out both;
  box-shadow: 0 0 6px var(--color-primary);
}

.theme-love .typing span {
  background: var(--color-love);
  box-shadow: 0 0 6px var(--color-love);
}

.theme-manus .typing span {
  background: var(--color-manus);
  box-shadow: 0 0 6px var(--color-manus);
}

.typing span:nth-child(1) { animation-delay: -0.32s; }
.typing span:nth-child(2) { animation-delay: -0.16s; }

@keyframes bounce {
  0%, 80%, 100% { transform: scale(0.6); opacity: 0.4; }
  40% { transform: scale(1); opacity: 1; }
}

/* ===== Input ===== */
.chat-input-area {
  padding: var(--space-md) var(--space-lg);
  padding-bottom: max(var(--space-md), env(safe-area-inset-bottom));
  background: rgba(10, 14, 23, 0.95);
  border-top: 1px solid var(--color-border);
  flex-shrink: 0;
}

.input-wrapper {
  display: flex;
  align-items: flex-end;
  gap: var(--space-sm);
  padding: var(--space-sm);
  background: rgba(8, 12, 22, 0.8);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-lg);
  transition: border-color 0.2s, box-shadow 0.2s;
}

.input-wrapper:focus-within {
  border-color: var(--color-border-glow);
  box-shadow: 0 0 20px rgba(0, 240, 255, 0.12);
}

.theme-love .input-wrapper:focus-within {
  border-color: rgba(255, 0, 128, 0.5);
  box-shadow: 0 0 20px rgba(255, 0, 128, 0.12);
}

.theme-manus .input-wrapper:focus-within {
  border-color: rgba(0, 212, 255, 0.5);
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.12);
}

.input-wrapper textarea {
  flex: 1;
  resize: none;
  padding: 8px 4px;
  border: none;
  background: transparent;
  font-size: 15px;
  line-height: 1.5;
  color: var(--color-text);
  outline: none;
  max-height: 120px;
  min-height: 24px;
}

.input-wrapper textarea::placeholder {
  color: var(--color-text-muted);
}

.input-wrapper textarea:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.send-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border: 1px solid rgba(0, 240, 255, 0.4);
  border-radius: var(--radius-md);
  background: linear-gradient(135deg, rgba(0, 240, 255, 0.2), rgba(168, 85, 247, 0.2));
  color: var(--color-primary);
  cursor: pointer;
  transition: transform 0.15s, box-shadow 0.2s;
  flex-shrink: 0;
}

.theme-love .send-btn {
  border-color: rgba(255, 0, 128, 0.4);
  background: linear-gradient(135deg, rgba(255, 0, 128, 0.2), rgba(255, 77, 166, 0.15));
  color: var(--color-love-light);
}

.theme-manus .send-btn {
  border-color: rgba(0, 212, 255, 0.4);
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.2), rgba(56, 189, 248, 0.15));
  color: var(--color-manus-light);
}

.send-btn:hover:not(:disabled) {
  transform: scale(1.05);
  box-shadow: var(--glow-cyan);
}

.theme-love .send-btn:hover:not(:disabled) {
  box-shadow: var(--glow-pink);
}

.send-btn:disabled {
  opacity: 0.35;
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
}

.loading-dot {
  font-size: 14px;
  letter-spacing: 2px;
}

.input-hint {
  margin: var(--space-sm) 0 0;
  font-size: 11px;
  font-family: var(--font-display);
  letter-spacing: 1px;
  color: var(--color-text-muted);
  text-align: center;
}

@media (max-width: 1024px) {
  .chat-room {
    border-left: none;
    border-right: none;
  }

  .chat-messages {
    padding: var(--space-md);
  }

  .message-row {
    max-width: 88%;
  }
}

@media (max-width: 640px) {
  :root {
    --header-height: 52px;
    --avatar-size: 34px;
  }

  .chat-header {
    padding: 0 var(--space-md);
    gap: var(--space-sm);
  }

  .header-info h1 {
    font-size: 13px;
  }

  .subtitle {
    font-size: 10px;
  }

  .chat-messages {
    padding: var(--space-sm) var(--space-md);
    gap: 12px;
  }

  .message-row {
    max-width: 92%;
  }

  .avatar {
    font-size: 12px;
  }

  .avatar-ai {
    font-size: 16px;
  }

  .bubble {
    padding: 10px 14px;
  }

  .message-text {
    font-size: 14px;
  }

  .chat-input-area {
    padding: var(--space-sm) var(--space-md);
    padding-bottom: max(var(--space-sm), env(safe-area-inset-bottom));
  }

  .input-hint {
    display: none;
  }

  .empty-avatar {
    width: 56px;
    height: 56px;
    font-size: 28px;
  }

  .empty-title {
    font-size: 14px;
  }

  .empty-desc {
    font-size: 13px;
  }
}
</style>
