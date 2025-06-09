<script setup>
import { useSidebarStore } from '@/stores/sidebar'
import { computed, defineProps } from 'vue'
import DropdownUser from './DropdownUser.vue'

const { toggleSidebar } = useSidebarStore()
const sidebarStore = useSidebarStore()

const props = defineProps({
  isNoSidebarPage: Boolean
})

const headerStyle = computed(() => {
  return props.isNoSidebarPage
    ? { backgroundImage: "url('/images/quiz-room-background-1.png')" }
    : { backgroundColor: 'white' }
})
</script>

<template>
  <header :class="['position-sticky top-0 z-3 w-100 shadow-sm', props.isNoSidebarPage ? 'bg-dark' : 'bg-white']" :style="headerStyle">
    <div class="d-flex align-items-center justify-content-between py-3 px-4 shadow-sm">
      <div class="d-flex align-items-center gap-2">
        <!-- Hamburger Toggle BTN -->
        <button class="btn btn-outline-secondary d-lg-none" @click="toggleSidebar">
          <span class="position-relative d-block" style="width: 24px; height: 24px;">
            <span class="position-absolute start-0 top-0 w-100 bg-dark"
              :class="{ 'd-none': sidebarStore.isSidebarOpen }" style="height: 3px; transition: 0.3s;"></span>
            <span class="position-absolute start-0 top-50 translate-middle-y w-100 bg-dark"
              :class="{ 'd-none': sidebarStore.isSidebarOpen }" style="height: 3px; transition: 0.3s;"></span>
            <span class="position-absolute start-0 bottom-0 w-100 bg-dark"
              :class="{ 'd-none': sidebarStore.isSidebarOpen }" style="height: 3px; transition: 0.3s;"></span>

            <!-- X icon -->
            <span class="position-absolute start-50 top-50 translate-middle d-none bg-dark"
              :class="{ 'd-block': sidebarStore.isSidebarOpen }"
              style="width: 20px; height: 3px; rotate: 45deg; transition: 0.3s;"></span>
            <span class="position-absolute start-50 top-50 translate-middle d-none bg-dark"
              :class="{ 'd-block': sidebarStore.isSidebarOpen }"
              style="width: 20px; height: 3px; rotate: -45deg; transition: 0.3s;"></span>
          </span>
        </button>
      </div>
      <div class="d-flex align-items-center gap-3">
        <DropdownUser :isNoSidebarPage="props.isNoSidebarPage" />
      </div>
    </div>
  </header>
</template>

<style scoped>
header {
  z-index: 9999;
}

.shadow-sm {
  background-color: #f7f8fb;

}
</style>