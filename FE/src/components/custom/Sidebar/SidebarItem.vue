<script setup>
import { ref } from 'vue'
import SidebarDropdown from './SidebarDropdown.vue'

const props = defineProps({
  item: {
    type: Object,
    required: true
  }
})

const isOpen = ref(false)

function toggleDropdown() {
  isOpen.value = !isOpen.value
}
</script>

<template>
  <div>
    <router-link
      v-if="!item.children"
      :to="{ name: item.routeName }"
      class="d-flex align-items-center px-3 py-2 rounded text-dark text-decoration-none fw-bold"
    >
      {{ item.label }}
    </router-link>

    <div v-else>
      <div
        @click="toggleDropdown"
        class="d-flex align-items-center px-3 py-2 rounded text-dark text-decoration-none cursor-pointer fw-bold"
      >
        {{ item.label }}
      </div>
      <SidebarDropdown :items="item.children" :isOpen="isOpen" @item-click="toggleDropdown" />
    </div>
  </div>
</template>
