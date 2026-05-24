<script setup>
import { ref } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { doChatWithManus } from '../api/aiApi'

const chatRoomRef = ref(null)
const loading = ref(false)
const abortController = ref(null)
const chunkSuffix = '\n'

async function handleSend(message) {
  if (loading.value) return

  loading.value = true
  abortController.value?.abort()
  abortController.value = new AbortController()

  try {
    await doChatWithManus(message, {
      signal: abortController.value.signal,
      onChunk: (chunk) => {
        chatRoomRef.value?.appendAiMessage(chunk)
      },
      onError: (err) => {
        chatRoomRef.value?.appendAiError(`请求出错：${err.message}`)
      },
    })
  } catch (err) {
    if (err.name !== 'AbortError') {
      chatRoomRef.value?.appendAiError(`请求出错：${err.message}`)
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <ChatRoom
    ref="chatRoomRef"
    title="AI 超级智能体"
    subtitle="具备工具调用能力的智能助手"
    theme="manus"
    ai-avatar="🤖"
    :chunk-suffix="chunkSuffix"
    :loading="loading"
    @send="handleSend"
  />
</template>
