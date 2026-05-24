<script setup>
import { ref, onMounted } from 'vue'

const apps = [
  {
    name: 'AI 恋爱大师',
    description: '你的专属恋爱顾问，解答情感问题、提供约会建议与聊天技巧',
    path: '/love-app',
    avatar: '💕',
    theme: 'love',
    tag: 'LOVE.AI',
  },
  {
    name: 'AI 超级智能体',
    description: '具备工具调用能力的超级 AI 助手，帮你完成复杂任务',
    path: '/manus',
    avatar: '🤖',
    theme: 'manus',
    tag: 'MANUS.AI',
  },
]

const pageEntered = ref(false)

onMounted(() => {
  requestAnimationFrame(() => {
    setTimeout(() => {
      pageEntered.value = true
    }, 120)
  })
})
</script>

<template>
  <div class="home-wrapper">
    <!-- 全屏动态背景 -->
    <div class="home-bg" aria-hidden="true">
      <div class="bg-grid" />
      <div class="bg-aurora bg-aurora-1" />
      <div class="bg-aurora bg-aurora-2" />
      <div class="bg-orb bg-orb-1" />
      <div class="bg-orb bg-orb-2" />
      <div class="bg-orb bg-orb-3" />
      <div class="bg-scan" />
      <div class="bg-lines">
        <span v-for="i in 6" :key="i" class="bg-line" :style="{ '--i': i }" />
      </div>
    </div>

    <div class="home-page" :class="{ entered: pageEntered }">
      <header class="home-header animate-item" style="--delay: 0">
        <div class="brand">
          <span class="brand-icon">◈</span>
          <h1>乐观 AI 应用中心</h1>
        </div>
        <p class="home-desc">选择下方应用，接入下一代智能对话系统</p>
        <div class="header-line" />
      </header>

      <main class="app-grid">
        <router-link
          v-for="(app, index) in apps"
          :key="app.path"
          :to="app.path"
          class="app-card animate-item"
          :class="`card-${app.theme}`"
          :style="{ '--delay': 0.15 + index * 0.15 }"
        >
          <span class="card-tag">{{ app.tag }}</span>
          <div class="card-avatar">{{ app.avatar }}</div>
          <div class="card-body">
            <h2>{{ app.name }}</h2>
            <p>{{ app.description }}</p>
          </div>
          <span class="card-action">
            进入应用
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <path d="M5 12h14M12 5l7 7-7 7" />
            </svg>
          </span>
        </router-link>
      </main>
    </div>
  </div>
</template>

<style scoped>
.home-wrapper {
  position: relative;
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
  overflow: hidden;
}

/* ===== 全屏背景动画 ===== */
.home-bg {
  position: absolute;
  inset: 0;
  pointer-events: none;
  overflow: hidden;
}

.bg-grid {
  position: absolute;
  inset: -50%;
  background-image:
    linear-gradient(rgba(0, 240, 255, 0.05) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 240, 255, 0.05) 1px, transparent 1px);
  background-size: 48px 48px;
  animation: grid-drift 20s linear infinite;
  mask-image: radial-gradient(ellipse at 50% 50%, black 20%, transparent 70%);
}

@keyframes grid-drift {
  0% { transform: translate(0, 0) rotate(0deg); }
  100% { transform: translate(48px, 48px) rotate(0deg); }
}

.bg-aurora {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.35;
}

.bg-aurora-1 {
  width: 500px;
  height: 500px;
  top: -10%;
  left: -5%;
  background: radial-gradient(circle, rgba(0, 240, 255, 0.25) 0%, transparent 70%);
  animation: aurora-float 12s ease-in-out infinite;
}

.bg-aurora-2 {
  width: 450px;
  height: 450px;
  bottom: -15%;
  right: -8%;
  background: radial-gradient(circle, rgba(168, 85, 247, 0.2) 0%, transparent 70%);
  animation: aurora-float 15s ease-in-out infinite reverse;
}

@keyframes aurora-float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -20px) scale(1.05); }
  66% { transform: translate(-20px, 25px) scale(0.95); }
}

.bg-orb {
  position: absolute;
  border-radius: 50%;
  border: 1px solid rgba(0, 240, 255, 0.15);
  box-shadow: 0 0 30px rgba(0, 240, 255, 0.08);
}

.bg-orb-1 {
  width: 120px;
  height: 120px;
  top: 15%;
  right: 12%;
  animation: orb-drift 8s ease-in-out infinite;
}

.bg-orb-2 {
  width: 60px;
  height: 60px;
  bottom: 25%;
  left: 8%;
  border-color: rgba(255, 0, 128, 0.2);
  animation: orb-drift 6s ease-in-out infinite reverse;
}

.bg-orb-3 {
  width: 40px;
  height: 40px;
  top: 40%;
  left: 20%;
  animation: orb-drift 10s ease-in-out infinite;
  animation-delay: -3s;
}

@keyframes orb-drift {
  0%, 100% { transform: translate(0, 0); opacity: 0.4; }
  50% { transform: translate(15px, -25px); opacity: 0.8; }
}

.bg-scan {
  position: absolute;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, rgba(0, 240, 255, 0.4), transparent);
  box-shadow: 0 0 16px rgba(0, 240, 255, 0.3);
  animation: bg-scan-move 6s ease-in-out infinite;
  opacity: 0.5;
}

@keyframes bg-scan-move {
  0% { top: -5%; opacity: 0; }
  10% { opacity: 0.6; }
  90% { opacity: 0.6; }
  100% { top: 105%; opacity: 0; }
}

.bg-lines {
  position: absolute;
  inset: 0;
}

.bg-line {
  position: absolute;
  top: 0;
  width: 1px;
  height: 100%;
  background: linear-gradient(180deg, transparent, rgba(0, 240, 255, 0.06), transparent);
  left: calc(var(--i) * 18%);
  animation: line-pulse 4s ease-in-out infinite;
  animation-delay: calc(var(--i) * -0.6s);
}

@keyframes line-pulse {
  0%, 100% { opacity: 0.2; transform: scaleY(0.8); }
  50% { opacity: 0.6; transform: scaleY(1); }
}

/* ===== 主页内容 ===== */
.home-page {
  position: relative;
  z-index: 1;
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--space-2xl) var(--space-lg);
  max-width: var(--home-max-width);
  margin: 0 auto;
  width: 100%;
}

/* 进入动画 */
.animate-item {
  opacity: 0;
  transform: translateY(28px);
  filter: blur(4px);
  transition:
    opacity 0.8s cubic-bezier(0.22, 1, 0.36, 1),
    transform 0.8s cubic-bezier(0.22, 1, 0.36, 1),
    filter 0.8s cubic-bezier(0.22, 1, 0.36, 1);
  transition-delay: calc(var(--delay, 0) * 1s);
}

.home-page.entered .animate-item {
  opacity: 1;
  transform: translateY(0);
  filter: blur(0);
}

.home-header {
  text-align: center;
  margin-bottom: var(--space-xl);
  flex-shrink: 0;
}

.brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-md);
  margin-bottom: var(--space-md);
}

.brand-icon {
  font-size: 32px;
  color: var(--color-primary);
  text-shadow: var(--glow-cyan);
  animation: pulse-glow 3s ease-in-out infinite;
}

@keyframes pulse-glow {
  0%, 100% { opacity: 0.8; }
  50% { opacity: 1; text-shadow: 0 0 30px rgba(0, 240, 255, 0.6); }
}

.home-header h1 {
  margin: 0;
  font-family: var(--font-display);
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 2px;
  text-transform: uppercase;
  background: linear-gradient(135deg, #fff 0%, var(--color-primary) 50%, var(--color-accent) 100%);
  background-size: 200% auto;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: title-shimmer 6s linear infinite;
}

@keyframes title-shimmer {
  0% { background-position: 0% center; }
  100% { background-position: 200% center; }
}

.home-desc {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
  color: var(--color-text-secondary);
  letter-spacing: 1px;
}

.header-line {
  width: 0;
  height: 2px;
  margin: var(--space-lg) auto 0;
  background: linear-gradient(90deg, transparent, var(--color-primary), transparent);
  box-shadow: var(--glow-cyan);
  transition: width 1s cubic-bezier(0.22, 1, 0.36, 1) 0.4s;
}

.home-page.entered .header-line {
  width: 120px;
}

.app-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-lg);
  width: 100%;
  align-items: stretch;
}

.app-card {
  position: relative;
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: var(--space-xl);
  background: var(--color-surface);
  backdrop-filter: blur(12px);
  border-radius: var(--radius-xl);
  text-decoration: none;
  border: 1px solid var(--color-border);
  box-shadow: var(--shadow-md);
  transition: transform 0.25s, box-shadow 0.25s, border-color 0.25s;
  overflow: hidden;
}

.app-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--color-primary), transparent);
  opacity: 0.6;
}

.app-card::after {
  content: '';
  position: absolute;
  inset: 0;
  background: linear-gradient(105deg, transparent 40%, rgba(0, 240, 255, 0.04) 50%, transparent 60%);
  transform: translateX(-100%);
  transition: transform 0.6s ease;
}

.app-card:hover::after {
  transform: translateX(100%);
}

.card-love::before {
  background: linear-gradient(90deg, transparent, var(--color-love), transparent);
}

.card-manus::before {
  background: linear-gradient(90deg, transparent, var(--color-manus), transparent);
}

.app-card:hover {
  transform: translateY(-6px);
  border-color: var(--color-border-glow);
}

.card-love:hover {
  box-shadow: var(--glow-pink), var(--shadow-lg);
  border-color: rgba(255, 0, 128, 0.4);
}

.card-manus:hover {
  box-shadow: var(--glow-cyan), var(--shadow-lg);
  border-color: rgba(0, 212, 255, 0.4);
}

.card-tag {
  position: absolute;
  top: 16px;
  right: 16px;
  font-family: var(--font-display);
  font-size: 10px;
  font-weight: 600;
  letter-spacing: 1.5px;
  color: var(--color-text-muted);
  opacity: 0.7;
}

.card-love .card-tag { color: var(--color-love-light); }
.card-manus .card-tag { color: var(--color-manus-light); }

.card-avatar {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  margin-bottom: var(--space-lg);
  border: 1px solid var(--color-border);
}

.card-love .card-avatar {
  background: rgba(255, 0, 128, 0.1);
  border-color: rgba(255, 0, 128, 0.3);
  box-shadow: 0 0 16px rgba(255, 0, 128, 0.15);
}

.card-manus .card-avatar {
  background: rgba(0, 212, 255, 0.1);
  border-color: rgba(0, 212, 255, 0.3);
  box-shadow: 0 0 16px rgba(0, 212, 255, 0.15);
}

.card-body {
  flex: 1;
}

.card-body h2 {
  margin: 0 0 var(--space-sm);
  font-family: var(--font-display);
  font-size: 18px;
  font-weight: 600;
  color: var(--color-text);
  letter-spacing: 1px;
}

.card-body p {
  margin: 0;
  font-size: 15px;
  line-height: 1.65;
  color: var(--color-text-secondary);
}

.card-action {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-top: auto;
  padding-top: var(--space-lg);
  font-family: var(--font-display);
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 1px;
  color: var(--color-primary);
  transition: gap 0.2s, text-shadow 0.2s;
}

.card-love .card-action { color: var(--color-love-light); }
.card-manus .card-action { color: var(--color-manus-light); }

.app-card:hover .card-action {
  gap: 10px;
  text-shadow: 0 0 12px currentColor;
}

@media (max-width: 1024px) {
  .home-page {
    padding: var(--space-xl) var(--space-lg);
  }

  .home-header h1 {
    font-size: 26px;
  }

  .app-grid {
    grid-template-columns: 1fr;
    max-width: 480px;
    margin: 0 auto;
  }

  .bg-orb-1 { width: 80px; height: 80px; }
}

@media (max-width: 640px) {
  .home-page {
    padding: var(--space-lg) var(--space-md);
  }

  .brand-icon {
    font-size: 26px;
  }

  .home-header h1 {
    font-size: 22px;
    letter-spacing: 1px;
  }

  .home-desc {
    font-size: 14px;
  }

  .app-card {
    padding: var(--space-lg);
  }

  .card-avatar {
    width: 48px;
    height: 48px;
    font-size: 24px;
    margin-bottom: var(--space-md);
  }

  .card-body h2 {
    font-size: 16px;
  }

  .bg-aurora-1,
  .bg-aurora-2 {
    width: 280px;
    height: 280px;
  }
}

@media (prefers-reduced-motion: reduce) {
  .animate-item {
    opacity: 1;
    transform: none;
    filter: none;
    transition: none;
  }

  .home-page.entered .header-line {
    width: 120px;
  }

  .bg-grid,
  .bg-aurora,
  .bg-orb,
  .bg-scan,
  .bg-line,
  .brand-icon,
  .home-header h1 {
    animation: none !important;
  }
}
</style>
