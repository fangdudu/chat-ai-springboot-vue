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
        <div class="p-4 text-white flex justify-between items-center" style="background-color: rgb(122, 118, 232)">
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
          <span>Qwen2.5-VL</span>
          <div class="relative inline-block text-left">
            <!--设置按钮-->
            <button id="setting" class="hover:bg-blue-400 rounded-md p-1">
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


            <div id="dropdown-content"
              class="hidden absolute right-0 mt-2 w-48 bg-white border border-gray-300 rounded-lg shadow-lg p-2">
              <a href="#" class="flex items-center px-4 py-2 text-gray-800 hover:bg-gray-200 rounded-md">
                <svg width="20px" height="20px" viewBox="0 0 24 24" fill="none" class="mr-2"
                  xmlns="http://www.w3.org/2000/svg">
                  <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                  <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                  <g id="SVGRepo_iconCarrier">
                    <path
                      d="M9 21H12M15 21H12M12 21V18M12 18H19C20.1046 18 21 17.1046 21 16V7C21 5.89543 20.1046 5 19 5H5C3.89543 5 3 5.89543 3 7V16C3 17.1046 3.89543 18 5 18H12ZM12.0498 8V8.1L11.9502 8.1002V8H12.0498Z"
                      stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
                  </g>
                </svg>
                Appearance
              </a>
              <a href="#" class="flex items-center px-4 py-2 text-gray-800 hover:bg-gray-200 rounded-md">
                <svg width="20px" height="20px" viewBox="0 0 24 24" class="mr-2" fill="none"
                  xmlns="http://www.w3.org/2000/svg">
                  <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                  <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                  <g id="SVGRepo_iconCarrier">
                    <path
                      d="M11.2691 4.41115C11.5006 3.89177 11.6164 3.63208 11.7776 3.55211C11.9176 3.48263 12.082 3.48263 12.222 3.55211C12.3832 3.63208 12.499 3.89177 12.7305 4.41115L14.5745 8.54808C14.643 8.70162 14.6772 8.77839 14.7302 8.83718C14.777 8.8892 14.8343 8.93081 14.8982 8.95929C14.9705 8.99149 15.0541 9.00031 15.2213 9.01795L19.7256 9.49336C20.2911 9.55304 20.5738 9.58288 20.6997 9.71147C20.809 9.82316 20.8598 9.97956 20.837 10.1342C20.8108 10.3122 20.5996 10.5025 20.1772 10.8832L16.8125 13.9154C16.6877 14.0279 16.6252 14.0842 16.5857 14.1527C16.5507 14.2134 16.5288 14.2807 16.5215 14.3503C16.5132 14.429 16.5306 14.5112 16.5655 14.6757L17.5053 19.1064C17.6233 19.6627 17.6823 19.9408 17.5989 20.1002C17.5264 20.2388 17.3934 20.3354 17.2393 20.3615C17.0619 20.3915 16.8156 20.2495 16.323 19.9654L12.3995 17.7024C12.2539 17.6184 12.1811 17.5765 12.1037 17.56C12.0352 17.5455 11.9644 17.5455 11.8959 17.56C11.8185 17.5765 11.7457 17.6184 11.6001 17.7024L7.67662 19.9654C7.18404 20.2495 6.93775 20.3915 6.76034 20.3615C6.60623 20.3354 6.47319 20.2388 6.40075 20.1002C6.31736 19.9408 6.37635 19.6627 6.49434 19.1064L7.4341 14.6757C7.46898 14.5112 7.48642 14.429 7.47814 14.3503C7.47081 14.2807 7.44894 14.2134 7.41394 14.1527C7.37439 14.0842 7.31195 14.0279 7.18708 13.9154L3.82246 10.8832C3.40005 10.5025 3.18884 10.3122 3.16258 10.1342C3.13978 9.97956 3.19059 9.82316 3.29993 9.71147C3.42581 9.58288 3.70856 9.55304 4.27406 9.49336L8.77835 9.01795C8.94553 9.00031 9.02911 8.99149 9.10139 8.95929C9.16534 8.93081 9.2226 8.8892 9.26946 8.83718C9.32241 8.77839 9.35663 8.70162 9.42508 8.54808L11.2691 4.41115Z"
                      stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
                  </g>
                </svg>
                Favorite
              </a>
              <a href="#" class="flex items-center px-4 py-2 text-gray-800 hover:bg-gray-200 rounded-md">
                <svg width="20px" height="20px" viewBox="0 0 24 24" class="mr-2" fill="none"
                  xmlns="http://www.w3.org/2000/svg">
                  <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
                  <g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g>
                  <g id="SVGRepo_iconCarrier">
                    <g id="Warning / Info">
                      <path id="Vector"
                        d="M12 11V16M12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12C21 16.9706 16.9706 21 12 21ZM12.0498 8V8.1L11.9502 8.1002V8H12.0498Z"
                        stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path>
                    </g>
                  </g>
                </svg>
                More
              </a>
            </div>
          </div>
        </div>
        <div class="flex-1 overflow-y-auto p-4">
          <div class="flex flex-col space-y-2">
            <div v-for="(message, index) in messages" :key="index">
              <!-- 消息文本容器 -->
              <div class="flex" :class="{ 'justify-end': message.isMine }">
                <div :class="{ 'bg-blue-200': message.isMine, 'bg-gray-300': !message.isMine }"
                  class="text-black p-2 rounded-lg max-w-xs">
                  <v-md-preview :text="message.text"></v-md-preview>
                </div>
              </div>
            </div>
          </div>
        </div>




        <!-- 输入框 -->
        <div class="bg-white p-4 flex flex-col space-y-2">
          <!-- 图片预览区域 -->
          <div v-if="selectedImages.length > 0" class="flex gap-1.5 overflow-x-auto pb-2">
            <div v-for="(image, index) in selectedImages" :key="index" class="relative">
              <img :src="image.url" class="h-10 w-10 object-cover rounded-md border border-gray-200" />
              <button @click="removeImage(index)"
                class="absolute top-0 right-0 bg-red-500 text-white rounded-full w-4 h-4 flex items-center justify-center text-xs hover:bg-red-600">
                ×
              </button>
            </div>
          </div>

          <!-- 输入和按钮区域 -->
          <div class="flex items-center gap-1 min-w-0">
            <!-- 图片上传按钮 -->
            <input type="file" @change="handleImageSelect" multiple accept="image/*" class="hidden" ref="fileInput">
            <button @click="$refs.fileInput.click()"
              class="flex-shrink-0 p-1.5 text-gray-500 hover:text-gray-700 hover:bg-gray-100 rounded-full transition-colors"
              title="上传图片">
              <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M4 16L8.586 11.414C8.96106 11.0389 9.46967 10.8284 10 10.8284C10.5303 10.8284 11.0389 11.0389 11.414 11.414L16 16M14 14L15.586 12.414C15.9611 12.0389 16.4697 11.8284 17 11.8284C17.5303 11.8284 18.0389 12.0389 18.414 12.414L20 14M14 8H14.01M6 20H18C18.5304 20 19.0391 19.7893 19.4142 19.4142C19.7893 19.0391 20 18.5304 20 18V6C20 5.46957 19.7893 4.96086 19.4142 4.58579C19.0391 4.21071 18.5304 4 18 4H6C5.46957 4 4.96086 4.21071 4.58579 4.58579C4.21071 4.96086 4 5.46957 4 6V18C4 18.5304 4.21071 19.0391 4.58579 19.4142C4.96086 19.7893 5.46957 20 6 20Z"
                  stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
              </svg>
            </button>

            <!-- 输入框 -->
            <input type="text" v-model="inputText" placeholder="在这里输入消息..."
              class="min-w-0 flex-1 border rounded-full px-3 py-1.5 focus:outline-none">

            <!--输入按钮-->
            <button v-if="!isChatting"
              class="flex-shrink-0 bg-blue-500 text-white rounded-full p-1.5 hover:bg-blue-600 focus:outline-none"
              @click="getMessagesKey">
              <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
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
              class="flex-shrink-0 bg-red-400 text-white rounded-full p-1.5 hover:bg-red-500 focus:outline-none focus:bg-red-500"
              @click="suspendChat">
              <svg class="w-5 h-5" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M8 6V18M16 6V18" stroke="#ffffff" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round" />
              </svg>
            </button>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import axios from 'axios';


export default {
  name: 'Home',
  data() {
    return {
      isChatting: false,
      isSidebarOpen: false,
      isHovering: false,
      isHovering2: false,
      // 存储选中的图片
      selectedImages: [],
      //要发送的问题
      inputText: null,
      // 对话数组
      messages: [
        {
          isFirst: true,
          think: false,
          text: "你好，我是Qwen2.5-VL，有什么我能帮助你的吗？",
          isMine: false,
          infoContent: "这是一个信息提示"
        },
      ],
      responseFirst: true, // 对话第一次回复
      eventSource: null
    };
  },
  beforeUnmount() {
    if (this.eventSource) {
      this.eventSource.close();
    }
  },
  methods: {
    goToDeepSeek() {
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
    getMessagesKey() {
      if (!this.selectedImages || this.selectedImages.length === 0) {
        alert('请选择图片');
        return;
      }
      // 先通过 post 将数据传回 后端  后端返回 key
      // 再拿这个key 去做sse 请求
      const formData = new FormData();
      this.selectedImages.forEach(image => {
        formData.append('file', image.file); // 添加图片文件
      });
      formData.append('messages', this.inputText);
      axios.post("http://127.0.0.1:8089/completions/getChatKey", formData).then(response => {
        if (response.status == 200) {
          const chatKey = response.data.chatKey;
          this.sendMessage(chatKey);
        }
      }).catch(error => {
        console.error('Error uploading images:', error);
      });
    },
    sendMessage(chatKey) {
      // 只有当eventSource不存在时才创建新的EventSource连接
      if (!this.eventSource) {
        this.isChatting = true;
        this.messages.push({ infoContent: "", text: this.inputText, isMine: true });
        this.messages.push({ suspend: false, think: true, costTime: null, infoContent: "", text: "", isMine: false });

        // 创建新的EventSource连接
        this.eventSource = new EventSource('http://127.0.0.1:8089/completions/pic?chatKey=' + chatKey);
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
    toggleSidebar() {
      this.isSidebarOpen = !this.isSidebarOpen;
    },
    handleImageSelect(event) {
      const files = event.target.files;
      if (files) {
        Array.from(files).forEach(file => {
          if (file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = (e) => {
              this.selectedImages.push({
                file: file,
                url: e.target.result
              });
            };
            reader.readAsDataURL(file);
          }
        });
      }
      // 清空input，这样相同的文件可以再次选择
      event.target.value = '';
    },
    removeImage(index) {
      this.selectedImages.splice(index, 1);
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
</style>
