<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const emit = defineEmits(['finished'])

const visible = ref(true)
const progress = ref(0)
const statusText = ref('SYSTEM BOOT')

const bootSteps = [
  { at: 15, text: 'INITIALIZING CORE...' },
  { at: 35, text: 'LOADING NEURAL MODULES...' },
  { at: 55, text: 'CONNECTING AI SERVICES...' },
  { at: 75, text: 'SYNCING INTERFACE...' },
  { at: 92, text: 'SYSTEM READY' },
]

let progressTimer = null

function startProgress() {
  progressTimer = setInterval(() => {
    if (progress.value >= 100) {
      clearInterval(progressTimer)
      return
    }
    const step = progress.value < 80 ? 2 + Math.random() * 4 : 1 + Math.random() * 2
    progress.value = Math.min(100, progress.value + step)

    const current = [...bootSteps].reverse().find((item) => progress.value >= item.at)
    if (current) statusText.value = current.text
  }, 60)
}

onMounted(async () => {
  startProgress()

  const minDelay = new Promise((resolve) => setTimeout(resolve, 2200))
  await Promise.all([useRouter().isReady(), minDelay])

  progress.value = 100
  statusText.value = 'ACCESS GRANTED'
  clearInterval(progressTimer)

  await new Promise((resolve) => setTimeout(resolve, 400))
  visible.value = false
  await new Promise((resolve) => setTimeout(resolve, 600))
  emit('finished')
})
</script>

<template>
  <Transition name="splash">
    <div v-if="visible" class="app-loading" aria-live="polite" aria-label="页面加载中">
      <div class="loading-grid" aria-hidden="true" />
      <div class="scan-line" aria-hidden="true" />

      <div class="loading-content">
        <div class="logo-wrap">
          <span class="logo-icon">◈</span>
          <span class="logo-ring ring-1" />
          <span class="logo-ring ring-2" />
        </div>

        <h1 class="loading-title">乐观 AI</h1>
        <p class="loading-subtitle">INTELLIGENT APPLICATION CENTER</p>

        <div class="progress-wrap">
          <div class="progress-track">
            <div class="progress-bar" :style="{ width: `${progress}%` }" />
          </div>
          <div class="progress-meta">
            <span class="status-text">{{ statusText }}</span>
            <span class="progress-value">{{ Math.round(progress) }}%</span>
          </div>
        </div>

        <div class="loading-dots" aria-hidden="true">
          <span /><span /><span />
        </div>
      </div>
    </div>
  </Transition>
</template>

<style scoped>
.app-loading {
  position: fixed;
  inset: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #060912;
  overflow: hidden;
}

.loading-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(0, 240, 255, 0.06) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 240, 255, 0.06) 1px, transparent 1px);
  background-size: 40px 40px;
  mask-image: radial-gradient(ellipse at center, black 30%, transparent 75%);
}

.scan-line {
  position: absolute;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--color-primary), transparent);
  box-shadow: 0 0 20px var(--color-primary);
  animation: scan 2.5s ease-in-out infinite;
  opacity: 0.6;
}

@keyframes scan {
  0% { top: 0; opacity: 0; }
  10% { opacity: 0.8; }
  90% { opacity: 0.8; }
  100% { top: 100%; opacity: 0; }
}

.loading-content {
  position: relative;
  z-index: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: min(420px, 88vw);
  padding: 0 24px;
  text-align: center;
}

.logo-wrap {
  position: relative;
  width: 88px;
  height: 88px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 28px;
}

.logo-icon {
  font-size: 42px;
  color: var(--color-primary);
  text-shadow: var(--glow-cyan);
  animation: pulse-icon 2s ease-in-out infinite;
}

.logo-ring {
  position: absolute;
  border-radius: 50%;
  border: 1px solid rgba(0, 240, 255, 0.25);
}

.ring-1 {
  inset: 0;
  animation: spin 4s linear infinite;
  border-top-color: var(--color-primary);
}

.ring-2 {
  inset: -12px;
  animation: spin 6s linear infinite reverse;
  border-bottom-color: var(--color-accent);
  opacity: 0.6;
}

@keyframes pulse-icon {
  0%, 100% { transform: scale(1); opacity: 0.85; }
  50% { transform: scale(1.08); opacity: 1; }
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-title {
  margin: 0 0 8px;
  font-family: var(--font-display);
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #fff 0%, var(--color-primary) 50%, var(--color-accent) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.loading-subtitle {
  margin: 0 0 36px;
  font-family: var(--font-display);
  font-size: 11px;
  letter-spacing: 3px;
  color: var(--color-text-muted);
}

.progress-wrap {
  width: 100%;
}

.progress-track {
  height: 3px;
  background: rgba(0, 240, 255, 0.1);
  border-radius: var(--radius-full);
  overflow: hidden;
  box-shadow: inset 0 0 8px rgba(0, 0, 0, 0.5);
}

.progress-bar {
  height: 100%;
  background: linear-gradient(90deg, var(--color-primary), var(--color-accent));
  border-radius: var(--radius-full);
  box-shadow: 0 0 12px rgba(0, 240, 255, 0.6);
  transition: width 0.15s ease-out;
}

.progress-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
  gap: 12px;
}

.status-text {
  font-family: var(--font-display);
  font-size: 10px;
  letter-spacing: 1.5px;
  color: var(--color-text-secondary);
  text-align: left;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.progress-value {
  font-family: var(--font-display);
  font-size: 12px;
  font-weight: 600;
  color: var(--color-primary);
  flex-shrink: 0;
}

.loading-dots {
  display: flex;
  gap: 6px;
  margin-top: 28px;
}

.loading-dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: var(--color-primary);
  animation: dot-bounce 1.4s infinite ease-in-out both;
  box-shadow: 0 0 8px var(--color-primary);
}

.loading-dots span:nth-child(1) { animation-delay: -0.32s; }
.loading-dots span:nth-child(2) { animation-delay: -0.16s; }

@keyframes dot-bounce {
  0%, 80%, 100% { transform: scale(0.5); opacity: 0.3; }
  40% { transform: scale(1); opacity: 1; }
}

.splash-enter-active,
.splash-leave-active {
  transition: opacity 0.6s ease;
}

.splash-leave-active .loading-content {
  transition: transform 0.6s ease, opacity 0.6s ease;
}

.splash-enter-from,
.splash-leave-to {
  opacity: 0;
}

.splash-leave-to .loading-content {
  transform: scale(0.96);
  opacity: 0;
}
</style>
