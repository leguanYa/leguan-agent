<script setup>
import { COPYRIGHT, FOOTER_LINKS } from '../config/site'
</script>

<template>
  <footer class="app-footer">
    <div class="footer-glow" aria-hidden="true" />
    <div class="footer-content">
      <div class="footer-links">
        <div v-for="group in FOOTER_LINKS" :key="group.title" class="footer-group">
          <h3 class="group-title">{{ group.title }}</h3>
          <ul class="group-list">
            <li v-for="link in group.links" :key="link.label">
              <router-link v-if="link.to" :to="link.to">{{ link.label }}</router-link>
              <a v-else :href="link.href" target="_blank" rel="noopener noreferrer">{{ link.label }}</a>
            </li>
          </ul>
        </div>
      </div>

      <div class="footer-divider" />

      <div class="footer-copyright">
        <p class="copyright-text">
          Copyright © {{ COPYRIGHT.year }} {{ COPYRIGHT.company }} 版权所有
        </p>
        <p v-if="COPYRIGHT.icp" class="icp-text">
          <a :href="COPYRIGHT.icpLink" target="_blank" rel="noopener noreferrer">
            {{ COPYRIGHT.icp }}
          </a>
        </p>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.app-footer {
  position: relative;
  flex-shrink: 0;
  background: rgba(8, 12, 22, 0.95);
  border-top: 1px solid var(--color-border);
  backdrop-filter: blur(12px);
}

.footer-glow {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--color-primary), var(--color-accent), transparent);
  opacity: 0.6;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 24px 24px;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
}

.group-title {
  margin: 0 0 16px;
  font-family: var(--font-display);
  font-size: 12px;
  font-weight: 600;
  letter-spacing: 2px;
  color: var(--color-primary);
  text-transform: uppercase;
}

.group-list {
  list-style: none;
  margin: 0;
  padding: 0;
}

.group-list li + li {
  margin-top: 10px;
}

.group-list a {
  font-size: 14px;
  color: var(--color-text-secondary);
  text-decoration: none;
  transition: color 0.2s, text-shadow 0.2s;
}

.group-list a:hover {
  color: var(--color-primary);
  text-shadow: 0 0 8px rgba(0, 240, 255, 0.5);
}

.footer-divider {
  height: 1px;
  margin: 32px 0 24px;
  background: linear-gradient(90deg, transparent, var(--color-border), transparent);
}

.footer-copyright {
  text-align: center;
}

.copyright-text,
.icp-text {
  margin: 0;
  font-size: 13px;
  color: var(--color-text-muted);
  line-height: 1.8;
  letter-spacing: 0.5px;
}

.icp-text a {
  color: var(--color-text-muted);
  text-decoration: none;
  transition: color 0.2s;
}

.icp-text a:hover {
  color: var(--color-primary);
}

@media (max-width: 768px) {
  .footer-content {
    padding: 32px 20px 20px;
  }

  .footer-links {
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
  }

  .footer-divider {
    margin: 24px 0 20px;
  }
}

@media (max-width: 480px) {
  .footer-links {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .copyright-text,
  .icp-text {
    font-size: 12px;
  }
}
</style>
