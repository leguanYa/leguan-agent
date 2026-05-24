<script setup>
import { ref, onMounted } from 'vue'
import ChatRoom from '../components/ChatRoom.vue'
import { doChatWithLoveAppSse } from '../api/aiApi'
import { generateChatId } from '../utils/uuid'

const chatRoomRef = ref(null)
const loading = ref(false)
const chatId = ref('')
const abortController = ref(null)

onMounted(() => {
  chatId.value = generateChatId()
})

async function handleSend(message) {
  if (loading.value) return

  loading.value = true
  abortController.value?.abort()
  abortController.value = new AbortController()

  try {
    await doChatWithLoveAppSse(message, chatId.value, {
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
    title="AI 恋爱大师"
    :subtitle="`会话 ID：${chatId}`"
    theme="love"
    ai-avatar="💕"
    :loading="loading"
    @send="handleSend"
  />
</template>
