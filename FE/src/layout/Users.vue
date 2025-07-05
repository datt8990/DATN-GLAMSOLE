<template>
  <div class="d-flex flex-column w-100 vh-100">
    <!-- Header cố định -->
    <div class="flex-shrink-0">
      <NavBar />
    </div>

    <!-- Nội dung scroll -->
   <div
  ref="mainElement"
  class="flex-grow-1 overflow-auto"
>
  <div class="d-flex flex-column min-vh-100 justify-between" style="padding-top: 90px;">
    <div class="flex-grow-1">
      <router-view />
    </div>
    <BottomFooter />
  </div>
</div>

  </div>
</template>

<script setup lang="ts">
import NavBar from '@/components/custom/layouts/NavBar.vue'
import BottomFooter from '@/components/custom/layouts/BottomFooter.vue'
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const mainElement = ref<HTMLElement | null>(null)
const route = useRoute()

watch(
  () => route.fullPath,
  () => {
    mainElement.value?.scrollTo(0, 0)
  }
)
</script>
