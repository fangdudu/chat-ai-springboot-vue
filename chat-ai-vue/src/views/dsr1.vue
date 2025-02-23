<template>
  <div class="bg-gray-100 h-screen max-w-lg mx-auto relative overflow-hidden">
    <div class="flex h-full">
      <!-- 侧边栏 -->
      <div class="w-44 bg-white shadow-lg transform transition-transform duration-300 absolute h-full"
        :class="{ 'translate-x-0': isSidebarOpen, '-translate-x-full': !isSidebarOpen }">
        <div class="p-3">
          <div class="flex justify-between items-center mb-4">
            <h2 class="text-lg font-semibold">模型广场</h2>
          </div>
          <div class="space-y-2">
            <button
              class="w-full text-left px-2 py-1 text-sm text-gray-700 rounded-md flex items-center transition-all duration-200 ease-in-out transform hover:scale-105 hover:bg-blue-50 hover:shadow-md hover:text-blue-600"
              @click="goToDeepSeek" @mouseover="isHovering = true" @mouseleave="isHovering = false"
              :class="{ 'bg-blue-50 shadow-md': isHovering }">
              <img src="../assets/deepseek-color.svg" class="w-4 h-4 mr-1" />
              <span class="flex-grow">DeepSeek-R1</span>
              <svg class="w-4 h-4 text-gray-400 opacity-0 transition-opacity duration-200"
                :class="{ 'opacity-100': isHovering }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </button>
            <button
              class="w-full text-left px-2 py-1 text-sm text-gray-700 rounded-md flex items-center transition-all duration-200 ease-in-out transform hover:scale-105 hover:bg-blue-50 hover:shadow-md hover:text-blue-600"
              @click="goToQwen" @mouseover="isHovering2 = true" @mouseleave="isHovering2 = false"
              :class="{ 'bg-blue-50 shadow-md': isHovering2 }">
              <img src="../assets/qwen-color.svg" class="w-4 h-4 mr-1" />
              <span class="flex-grow">Qwen2.5-VL</span>
              <svg class="w-4 h-4 text-gray-400 opacity-0 transition-opacity duration-200"
                :class="{ 'opacity-100': isHovering2 }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <!-- 主要内容区域 -->
      <div class="flex-1 flex flex-col transform transition-transform duration-300 min-w-0"
        :class="{ 'translate-x-44': isSidebarOpen }">
        <div class="p-4 text-white flex justify-between items-center" style="background-color: rgb(77, 107, 254)">
          <!--菜单按钮-->
          <button @click="toggleSidebar" class="hover:bg-blue-400 rounded-md p-1">
            <svg v-if="!isSidebarOpen" class="w-8 h-8 text-white" viewBox="0 0 24 24" fill="none">
              <path d="M4 6H20M4 12H20M4 18H20" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
            </svg>
            <svg v-else class="w-8 h-8 text-white" viewBox="0 0 24 24" fill="none">
              <path d="M6 18L18 6M6 6l12 12" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round" />
            </svg>
          </button>
          <span>DeepSeek-R1</span>

          <!-- 右上角设置按钮 -->
          <div class="relative inline-block text-left">
            <!--设置按钮-->
            <button @click="toggleSettings" class="hover:bg-blue-400 rounded-md p-1">
              <svg width="30px" height="30px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                <g id="SVGRepo_iconCarrier">
                  <path fill-rule="evenodd" clip-rule="evenodd"
                    d="M14.1395 12.0002C14.1395 13.1048 13.2664 14.0002 12.1895 14.0002C11.1125 14.0002 10.2395 13.1048 10.2395 12.0002C10.2395 10.8957 11.1125 10.0002 12.1895 10.0002C13.2664 10.0002 14.1395 10.8957 14.1395 12.0002ZM12.0498 8V8.1L11.9502 8.1002V8H12.0498Z"
                    stroke="#ffffff" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                  <path fill-rule="evenodd" clip-rule="evenodd"
                    d="M7.57381 18.1003L5.12169 12.8133C4.79277 12.2907 4.79277 11.6189 5.12169 11.0963L7.55821 5.89229C7.93118 5.32445 8.55898 4.98876 9.22644 5.00029H12.1895H15.1525C15.8199 4.98876 16.4477 5.32445 16.8207 5.89229L19.2524 11.0923C19.5813 11.6149 19.5813 12.2867 19.2524 12.8093L16.8051 18.1003C16.4324 18.674 15.8002 19.0133 15.1281 19.0003H9.24984C8.5781 19.013 7.94636 18.6737 7.57381 18.1003Z"
                    stroke="#ffffff" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"></path>
                </g>
              </svg>
            </button>

            <!--设置选择框-->
            <div v-show="isSettingsOpen" class="absolute right-0 mt-2 w-48 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5">
              <div class="py-1">
                <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">设置选项 1</a>
                <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">设置选项 2</a>
                <a href="#" class="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100">设置选项 3</a>
              </div>
            </div>
          </div>
        </div>
        <!-- 对话框 -->
        <div class="flex-1 overflow-y-auto p-4">
          <div class="flex flex-col space-y-2">
            <div v-for="(message, index) in messages" :key="index">
              <div v-if="!message.isMine && !message.isFirst" class="mb-4 w-full flex justify-start">
                <!-- 思考框 -->
                <div @click="message.isInfoExpanded = !message.isInfoExpanded"
                  :class="['border', 'text-black', 'p-2', 'rounded-lg', 'inline-block', message.think ? 'bg-yellow-200' : 'bg-gray-50']">
                  <div class="flex items-center space-x-2">
                    <span v-if="message.think" class="whitespace-nowrap flex items-center space-x-1">
                      <span>思考中</span>
                      <!--旋转 -->
                      <svg class="h-4 w-4 animate-spin text-black-200 inline-block" viewBox="0 0 24 24" fill="none"
                        xmlns="http://www.w3.org/2000/svg">
                        <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                        <path class="opacity-75" fill="currentColor"
                          d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
                      </svg>
                    </span>
                    <span v-else class="whitespace-nowrap">已深度思考（用时{{ this.messages.costTime }}秒）</span>
                    <svg
                      :class="['w-4', 'h-4', 'transform', 'transition-transform', message.isInfoExpanded ? 'rotate-180' : '']"
                      fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                    </svg>
                  </div>
                  <div v-show="message.isInfoExpanded" class="mt-2 text-sm text-gray-600">
                    {{ message.infoContent }}
                  </div>
                </div>
              </div>
              <!-- 消息文本容器 -->
              <div class="flex items-center" :class="{ 'justify-end': message.isMine }">
                <div :class="{ 'bg-blue-200': message.isMine, 'bg-gray-300': !message.isMine }"
                  class="text-black p-2 rounded-lg max-w-xs flex items-center">
                  <v-md-preview :text="message.text" class="text-left"></v-md-preview>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="bg-white p-4 flex items-center">
          <input type="text" v-model="this.inputText" placeholder="在这里输入消息..."
            class="flex-1 border rounded-full px-4 py-2 focus:outline-none">
          <!--输入按钮-->
          <button v-if="!isChatting"
            class="bg-blue-500 text-white rounded-full p-2 ml-2 hover:bg-blue-600 focus:outline-none"
            @click="sendMessage">
            <svg width="20px" height="20px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"
              stroke="#ffffff">
              <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
              <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
              <g id="SVGRepo_iconCarrier">
                <path
                  d="M11.5003 12H5.41872M5.24634 12.7972L4.24158 15.7986C3.69128 17.4424 3.41613 18.2643 3.61359 18.7704C3.78506 19.21 4.15335 19.5432 4.6078 19.6701C5.13111 19.8161 5.92151 19.4604 7.50231 18.7491L17.6367 14.1886C19.1797 13.4942 19.9512 13.1471 20.1896 12.6648C20.3968 12.2458 20.3968 11.7541 20.1896 11.3351C19.9512 10.8529 19.1797 10.5057 17.6367 9.81135L7.48483 5.24303C5.90879 4.53382 5.12078 4.17921 4.59799 4.32468C4.14397 4.45101 3.77572 4.78336 3.60365 5.22209C3.40551 5.72728 3.67772 6.54741 4.22215 8.18767L5.24829 11.2793C5.34179 11.561 5.38855 11.7019 5.407 11.8459C5.42338 11.9738 5.42321 12.1032 5.40651 12.231C5.38768 12.375 5.34057 12.5157 5.24634 12.7972Z"
                  stroke="#ffffff" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
              </g>
            </svg>
          </button>
          <button v-if="isChatting"
            class="bg-red-400 text-white rounded-full p-2 ml-2 hover:bg-red-500 focus:outline-none focus:bg-red-500"
            @click="suspendChat">
            <!-- 暂停图标 -->
            <svg width="20px" height="20px" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M8 6V18M16 6V18" stroke="#ffffff" stroke-width="2" stroke-linecap="round"
                stroke-linejoin="round" />
            </svg>
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<script>

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Home',
  data() {
    return {
      isChatting: false,
      isSidebarOpen: false,
      isHovering: false,
      isHovering2: false,
      isSettingsOpen: false,
      //要发送的问题
      inputText: null,
      // 对话数组
      messages: [
        {
          isFirst: true,
          think: false,
          text: "你好，我是DeepSeek-R1，有什么我能帮助你的吗？",
          isMine: false,
          isInfoExpanded: false,
          infoContent: "这是一个信息提示"
        },
      ],
      responseFirst: true, // 对话第一次回复
      eventSource: null,
    };
  },
  beforeUnmount() {
    if (this.eventSource) {
      this.eventSource.close();
    }
    document.removeEventListener('click', this.handleClickOutside)
  },
  mounted() {
    document.addEventListener('click', this.handleClickOutside)
  },
  methods: {
    goToDeepSeek() {
      this.isSidebarOpen = false;
      this.$router.push('/dsr1');
    },
    goToQwen() {
      this.$router.push('/qwen2_5');
    },
    suspendChat() {
      if (this.eventSource) {
        this.eventSource.close();
        this.eventSource = null;
      }
      this.messages[this.messages.length - 1].think = false;
      this.messages[this.messages.length - 1].suspend = true;
      if (this.messages[this.messages.length - 1].text.length == 0) {
        this.messages[this.messages.length - 1].text = '对话已暂停';
      }
      this.isChatting = false;
    },
    sendMessage() {
      // 只有当eventSource不存在时才创建新的EventSource连接
      if (!this.eventSource) {
        this.isChatting = true;
        this.messages.push({ infoContent: "", text: this.inputText, isMine: true });
        this.messages.push({ suspend: false, think: true, costTime: null, infoContent: "", text: "", isMine: false });

        // 创建新的EventSource连接
        this.eventSource = new EventSource('http://192.168.1.3:8089/completions?messages=' + this.inputText);
        // 重置输入框
        this.inputText = "";
        // 设置消息接收的回调函数
        this.eventSource.onmessage = (event) => {
          const data = JSON.parse(event.data);
          /*if(data.finish_reason !== undefined && data.finish_reason !== null){
            //console.log(data.usage);
            data.usage.completion_tokens;
            data.usage.prompt_tokens;
            data.usage.total_tokens

            this.eventSource.close();
            this.eventSource = null;
          }*/
          if (data.choices[0]) {
            if (data.choices[0].delta.reasoning_content && data.choices[0].delta.reasoning_content !== undefined) {
              if (data.choices[0].delta.reasoning_content.trim().length > 0) {
                this.messages[this.messages.length - 1].infoContent += data.choices[0].delta.reasoning_content;
              }
            }
            if (data.choices[0].delta.content && data.choices[0].delta.content !== undefined) {
              if (data.choices[0].delta.content.trim().length > 0) {
                this.messages[this.messages.length - 1].think = false;
                if (this.messages.costTime == null) {
                  this.messages.costTime = (Math.floor(Date.now() / 1000) - data.created);
                  this.messages[this.messages.length - 1].isInfoExpanded = false;
                }
                this.messages[this.messages.length - 1].text += data.choices[0].delta.content;
              }
            }
          }
        };

        // 可选：监听错误事件，以便在出现问题时能够重新连接或处理错误
        this.eventSource.onerror = (event) => {
          // console.log(event);
          // console.error("EventSource failed:", event);
          // 关闭出错的连接
          this.eventSource.close();
          // 重置eventSource变量，允许重建连接
          this.eventSource = null;

          this.isChatting = false;
        };
      }
    },
    toggleSettings(event) {
      event.stopPropagation() // 阻止事件冒泡
      this.isSettingsOpen = !this.isSettingsOpen
    },
    toggleSidebar() {
      this.isSidebarOpen = !this.isSidebarOpen;
    },
    handleClickOutside(event) {
      const settingsContainer = document.querySelector('.relative.inline-block.text-left')
      if (settingsContainer && !settingsContainer.contains(event.target)) {
        this.isSettingsOpen = false
      }
    },
  }
}
</script>

<style>
.github-markdown-body {
  padding: 1px 1px 1px !important;
}

.github-markdown-body blockquote,
.github-markdown-body details,
.github-markdown-body dl,
.github-markdown-body ol,
.github-markdown-body p,
.github-markdown-body pre,
.github-markdown-body table,
.github-markdown-body ul {
  margin-bottom: 2px;
}

/* 添加过渡效果 */
.transform {
  transition-property: transform;
}

.transition-transform {
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
}

.duration-300 {
  transition-duration: 300ms;
}

.github-markdown-body blockquote, .github-markdown-body details, .github-markdown-body dl, .github-markdown-body ol, .github-markdown-body p, .github-markdown-body pre, .github-markdown-body table, .github-markdown-body ul {
    margin-top: 0;
    margin-bottom: 1px;
}
</style>
