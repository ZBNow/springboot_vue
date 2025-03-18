import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/css/global.css'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

// ✅ 正确导入所有 Element Plus 图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

app.use(router)
app.use(ElementPlus, { locale: zhCn })

// ✅ 方式 1：全局注册所有 Element Plus 图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.mount('#app')
