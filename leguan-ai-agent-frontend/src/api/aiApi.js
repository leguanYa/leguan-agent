import request from './request'
import { API_BASE_URL } from '../config'

const BASE_URL = `${API_BASE_URL}/ai`

/**
 * 解析 SSE 流式响应
 */
async function parseSseStream(response, onChunk, signal) {
  const reader = response.body.getReader()
  const decoder = new TextDecoder()
  let buffer = ''

  while (true) {
    if (signal?.aborted) {
      reader.cancel()
      break
    }

    const { done, value } = await reader.read()
    if (done) break

    buffer += decoder.decode(value, { stream: true })
    const lines = buffer.split('\n')
    buffer = lines.pop() || ''

    for (const line of lines) {
      const trimmed = line.trim()
      if (!trimmed) continue

      if (trimmed.startsWith('data:')) {
        const data = trimmed.slice(5).trimStart()
        if (data && data !== '[DONE]') {
          onChunk(data)
        }
      } else if (!trimmed.startsWith('event:') && !trimmed.startsWith('id:') && !trimmed.startsWith(':')) {
        onChunk(trimmed)
      }
    }
  }

  if (buffer.trim()) {
    const trimmed = buffer.trim()
    if (trimmed.startsWith('data:')) {
      const data = trimmed.slice(5).trimStart()
      if (data && data !== '[DONE]') onChunk(data)
    } else if (!trimmed.startsWith('event:') && !trimmed.startsWith('id:')) {
      onChunk(trimmed)
    }
  }
}

/**
 * AI 恋爱大师 SSE 对话
 */
export async function doChatWithLoveAppSse(message, chatId, { onChunk, onError, signal }) {
  const params = new URLSearchParams({ message, chatId })
  const url = `${BASE_URL}/love_app/chat/sse?${params}`

  try {
    const response = await fetch(url, { method: 'GET', signal })
    if (!response.ok) {
      throw new Error(`请求失败: ${response.status}`)
    }
    await parseSseStream(response, onChunk, signal)
  } catch (err) {
    if (err.name !== 'AbortError') {
      onError?.(err)
    }
    throw err
  }
}

/**
 * AI 超级智能体 SSE 对话
 */
export async function doChatWithManus(message, { onChunk, onError, signal }) {
  const params = new URLSearchParams({ message })
  const url = `${BASE_URL}/manus/chat?${params}`

  try {
    const response = await fetch(url, { method: 'GET', signal })
    if (!response.ok) {
      throw new Error(`请求失败: ${response.status}`)
    }
    await parseSseStream(response, onChunk, signal)
  } catch (err) {
    if (err.name !== 'AbortError') {
      onError?.(err)
    }
    throw err
  }
}

export { request }
