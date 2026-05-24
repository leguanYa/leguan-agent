/** 站点信息配置 */
export const SITE_NAME = '乐观 AI'

export const COPYRIGHT = {
  company: '乐观 AI',
  year: new Date().getFullYear(),
  icp: '',
  icpLink: 'https://beian.miit.gov.cn/',
}

export const FOOTER_LINKS = [
  {
    title: SITE_NAME,
    links: [
      { label: '《用户协议》', href: '#' },
      { label: '《隐私政策》', href: '#' },
    ],
  },
  {
    title: '应用',
    links: [
      { label: 'AI 恋爱大师', to: '/love-app' },
      { label: 'AI 超级智能体', to: '/manus' },
    ],
  },
  {
    title: '关于',
    links: [
      { label: '乐观 AI 应用中心', to: '/' },
      { label: '智能对话平台', href: '#' },
    ],
  },
]
