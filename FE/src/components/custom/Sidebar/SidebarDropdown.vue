<template>
  <transition name="slide-fade">
    <ul v-if="isOpen" class="mt-3 mb-4 list-group list-group-flush">
      <template v-for="(childItem, index) in items" :key="index">
        <li class="list-group-item bg-light border-0 p-2">
          <router-link
            :to="{ name: childItem.routeName }"
            @click="handleItemClick(index)"
            class="d-flex align-items-center px-3 py-2 rounded text-dark text-decoration-none"
          >
            {{ childItem.label }}
          </router-link>
        </li>
      </template>
    </ul>
  </transition>
</template>

<script setup>
// props: items, isOpen
import { useSidebarStore } from "@/stores/sidebar";
const sidebarStore = useSidebarStore();

const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
  isOpen: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["item-click"]);

function handleItemClick(index) {
  emit("item-click", index);
}
</script>

<style scoped>
.slide-fade-enter-active {
  transition: all 0.3s ease;
}
.slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}
</style>
