import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoveAppView from '../views/LoveAppView.vue'
import ManusView from '../views/ManusView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { title: '乐观 AI 应用中心' },
  },
  {
    path: '/love-app',
    name: 'love-app',
    component: LoveAppView,
    meta: { title: 'AI 恋爱大师' },
  },
  {
    path: '/manus',
    name: 'manus',
    component: ManusView,
    meta: { title: 'AI 超级智能体' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.afterEach((to) => {
  document.title = to.meta.title || '乐观 AI 应用中心'
})

export default router
