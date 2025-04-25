<template>
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
          <img src="../assets/deepseek-color.webp" class="w-4 h-4 mr-1" />
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
          <img src="../assets/qwen-color.webp" class="w-4 h-4 mr-1" />
          <span class="flex-grow">Qwen2.5-VL</span>
          <svg class="w-4 h-4 text-gray-400 opacity-0 transition-opacity duration-200"
            :class="{ 'opacity-100': isHovering2 }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
        <button
          class="w-full text-left px-2 py-1 text-sm text-gray-700 rounded-md flex items-center transition-all duration-200 ease-in-out transform hover:scale-105 hover:bg-blue-50 hover:shadow-md hover:text-blue-600"
          @click="goToTTS" @mouseover="isHovering3 = true" @mouseleave="isHovering3 = false"
          :class="{ 'bg-blue-50 shadow-md': isHovering3 }">
          <img src="../assets/tts.webp" class="w-4 h-4 mr-1" />
          <span class="flex-grow">Edge-TTS</span>
          <svg class="w-4 h-4 text-gray-400 opacity-0 transition-opacity duration-200"
            :class="{ 'opacity-100': isHovering3 }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
        <button
          class="w-full text-left px-2 py-1 text-sm text-gray-700 rounded-md flex items-center transition-all duration-200 ease-in-out transform hover:scale-105 hover:bg-blue-50 hover:shadow-md hover:text-blue-600"
          @click="goToOcr" @mouseover="isHovering4 = true" @mouseleave="isHovering4 = false"
          :class="{ 'bg-blue-50 shadow-md': isHovering4 }">
          <img src="../assets/ocr.webp" class="w-4 h-4 mr-1" />
          <span class="flex-grow">OCR</span>
          <svg class="w-4 h-4 text-gray-400 opacity-0 transition-opacity duration-200"
            :class="{ 'opacity-100': isHovering4 }" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Sidebar',
  data() {
    return {
      isHovering: false,
      isHovering2: false,
      isHovering3: false,
      isHovering4: false,
    }
  },
  props: {
    isSidebarOpen: {
      type: Boolean,
      required: true
    }
  },
  methods: {
    goToDeepSeek() {
      this.$router.push('/dsr1');
    },
    goToQwen() {
      this.$router.push('/qwen2_5');
    },
    goToTTS() {
      this.$router.push('/tts');
    },
    goToOcr() {
      this.$router.push('/ocr');
    }
  }
}
</script>

<style scoped>
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