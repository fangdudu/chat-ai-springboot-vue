import { createRouter, createWebHistory } from 'vue-router'
import dsr1 from './views/dsr1.vue'
import qwen2_5 from './views/qwen2_5.vue'

// ... 导入其他demo组件

const routes = [
    { path: '/', component: dsr1 },
    { path: '/dsr1', component: dsr1 },
    { path: '/qwen2_5', component: qwen2_5 }
    // ... 可能的其他路由
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router