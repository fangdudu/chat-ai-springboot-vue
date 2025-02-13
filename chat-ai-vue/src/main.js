import { createApp } from "vue"
import App from "./App.vue"
import router from "./router" // 确保这里的路径是正确的
import "./assets/styles/tailwind.css"
import axios from "./axios"; // 导入axios实例

const app = createApp(App);
app.config.globalProperties.$axios = axios; // 将axios添加到全局属性
app.use(router).mount('#app');

// v-md-editor
import VMdPreview from "@kangc/v-md-editor/lib/preview";
import "@kangc/v-md-editor/lib/style/preview.css";
import githubTheme from "@kangc/v-md-editor/lib/theme/github.js";
import "@kangc/v-md-editor/lib/theme/style/github.css";
import hljs from "highlight.js";
VMdPreview.use(githubTheme, {
    Hljs: hljs,
});
app.use(VMdPreview);

