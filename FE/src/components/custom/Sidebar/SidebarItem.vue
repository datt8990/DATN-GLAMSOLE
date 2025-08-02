<template>
  <li class="sidebar-item">
    <router-link
      v-if="!item.children"
      :to="{ name: item.routeName }"
      class="nav-link"
      :class="{ active: isActive }"
      tabindex="0"
    >
      <span class="icon" v-html="item.icon"></span>
      <span class="label">{{ item.label }}</span>
    </router-link>

    <div v-else class="dropdown-container">
      <button
        @click="toggleDropdown"
        type="button"
        tabindex="0"
        @keydown.enter.prevent="toggleDropdown"
        @keydown.space.prevent="toggleDropdown"
        :aria-expanded="isOpen"
        class="nav-link nav-dropdown"
        :class="{ active: isActive, open: isOpen }"
      >
        <span class="icon" v-html="item.icon"></span>
        <span class="label">{{ item.label }}</span>
        <span class="dropdown-arrow" :class="{ rotate: isOpen }" aria-hidden="true">
          <svg width="14" height="14" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" viewBox="0 0 24 24">
            <polyline points="6 9 12 15 18 9"></polyline>
          </svg>
        </span>
      </button>
      
      <SidebarDropdown 
        :key="`dropdown-${item.label}`"
        :items="item.children" 
        :isOpen="isOpen" 
        @item-click="handleChildClick" 
      />
    </div>
  </li>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useRoute } from 'vue-router';
import SidebarDropdown from './SidebarDropdown.vue';

const props = defineProps({
  item: {
    type: Object,
    required: true,
  },
});

const isOpen = ref(false);
const route = useRoute();

const isActive = computed(() => {
  if (props.item.routeName) {
    return route.name === props.item.routeName;
  }
  if (props.item.children && props.item.children.length) {
    return props.item.children.some(child => child.routeName === route.name);
  }
  return false;
});

// Auto open dropdown if has active child item
watch(isActive, (active) => {
  if (active && props.item.children && props.item.children.length) {
    isOpen.value = true;
  }
});

function toggleDropdown() {
  isOpen.value = !isOpen.value;
}

function handleChildClick(index) {
  console.log('Child clicked:', index);
}
</script>

<style scoped>
.sidebar-item {
  margin-bottom: 2px;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: #6b7280;
  text-decoration: none;
  font-weight: 500;
  font-size: 15px;
  border-radius: 8px;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
  user-select: none;
  position: relative;
  background-color: transparent;
  border: none;
  /* Cố định width và box-sizing */
  width: 100%;
  box-sizing: border-box;
  text-align: left;
}

.sidebar-item > .nav-link:hover {
  background-color: #59bddb;
  color: white;
}

.nav-link.active {
  background-color: #59bddb;
  color: white;
  font-weight: 500;
}

.nav-link.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background-color: #59bddb;
  border-radius: 0 2px 2px 0;
}

.icon {
  width: 20px;
  height: 20px;
  margin-right: 0.75rem;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  color: inherit;
}

.icon :deep(svg) {
  width: 100%;
  height: 100%;
}

.label {
  flex-grow: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  line-height: 1.4;
}

.nav-dropdown {
  justify-content: space-between;
  background: none;
  border: none;
  outline: none;
}

.nav-dropdown:focus {
  outline: 2px solid #59bddb;
  outline-offset: 2px;
}

.nav-dropdown.open {
  background-color: #f3f4f6;
  color: #374151;
}

.dropdown-arrow {
  transition: transform 0.3s ease-in-out;
  display: flex;
  align-items: center;
  color: inherit;
  margin-left: 0.5rem;
}

.dropdown-arrow.rotate {
  transform: rotate(180deg);
}

.dropdown-container {
  width: 100%;
  position: relative;
}

/* Đảm bảo container có width cố định */
.sidebar-item {
  width: 100%;
}

/* Đảm bảo tất cả nav-link có cùng width */
.nav-link,
.nav-dropdown {
  width: 100% !important;
  max-width: 100%;
  min-width: 0;
}
</style>