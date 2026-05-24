<script setup>
import { ref } from 'vue'
import { RouterView } from 'vue-router'
import AppFooter from './components/AppFooter.vue'
import AppLoading from './components/AppLoading.vue'

const appReady = ref(false)
</script>

<template>
  <AppLoading @finished="appReady = true" />

  <Transition name="app-fade">
    <div v-show="appReady" class="app-layout">
      <main class="app-main">
        <RouterView />
      </main>
      <AppFooter />
    </div>
  </Transition>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  min-height: 100dvh;
  display: flex;
  flex-direction: column;
}

.app-main {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.app-fade-enter-active {
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.app-fade-enter-from {
  opacity: 0;
  transform: scale(0.98);
}
</style>
