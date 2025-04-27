# chat-ai-springboot-vue
基于springboot-vue实现的AI聊天demo

前端技术栈：Vue3, tailwindcss

后端技术栈：Springboot, Webflux, Redis

已接入
- 大模型对话
  - 阿里云百炼平台(deepseek-r1/qwen2.5-vl)
  - 字节火山引擎(deepseek-r1)
  - 硅基流动平台(deepseek-r1)
- Edge-TTS语音生成
- OCR图片识别功能

![deepseek-r1](https://i.miji.bid/2025/02/27/436ee1277135a1d90ac5ea0476b90758.gif)

![千问2.5VL](https://i.miji.bid/2025/02/27/ff71ad62937dceb31fa57ea8dd824f0b.gif)

![EdgeTTS](https://i.miji.bid/2025/04/27/9ba1cd6563d20ec15517ab17918cd35b.gif)

运维流程：

1. 宿主机安装Docker
2. 通过Docker-Compose将服务部署到宿主机中

![cc4c588faff068ad0cfbaf009af45213.png](https://i.miji.bid/2025/02/27/cc4c588faff068ad0cfbaf009af45213.png)

